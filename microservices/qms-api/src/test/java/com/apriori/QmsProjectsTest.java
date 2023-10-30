package com.apriori;

import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.AuthUserContextUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.TestUtil;
import com.apriori.models.response.component.ScenarioItem;
import com.apriori.properties.PropertiesContext;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.controller.QmsProjectResources;
import com.apriori.qms.controller.QmsScenarioDiscussionResources;
import com.apriori.qms.models.request.bidpackage.BidPackageItemParameters;
import com.apriori.qms.models.request.bidpackage.BidPackageItemRequest;
import com.apriori.qms.models.request.bidpackage.BidPackageProjectUserParameters;
import com.apriori.qms.models.response.bidpackage.BidPackageItemResponse;
import com.apriori.qms.models.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.models.response.bidpackage.BidPackageProjectsResponse;
import com.apriori.qms.models.response.bidpackage.BidPackageResponse;
import com.apriori.qms.models.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.qms.utils.QmsApiTestUtils;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class QmsProjectsTest extends TestUtil {
    private static final UserCredentials currentUser = UserUtil.getUser();
    private SoftAssertions softAssertions = new SoftAssertions();
    private ScenarioItem scenarioItem;

    @BeforeEach
    public void beforeTest() {
        softAssertions = new SoftAssertions();
        scenarioItem = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
    }

    @AfterEach
    public void afterTest() {
        QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItem, currentUser);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {22128})
    @Description("Verify user can add  project users(default) by project creation API (without mentioning any user in request payload)")
    public void createProjectWithDefaultUser() {
        List<BidPackageItemRequest> itemsList = new ArrayList<>();
        itemsList.add(BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity(scenarioItem.getComponentIdentity())
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build());

        BidPackageProjectResponse bppResponse = QmsProjectResources.createProject(new HashMap<>(),
            itemsList,
            null,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        softAssertions.assertThat(bppResponse.getUsers().size()).isEqualTo(1);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(bppResponse.getUsers().get(0).getUser().getIdentity())
                .isEqualTo(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()));
        }

        softAssertions.assertThat(bppResponse.getItems().size()).isEqualTo(1);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(bppResponse.getItems().get(0).getBidPackageItem().getComponentIdentity())
                .isEqualTo(scenarioItem.getComponentIdentity());
            softAssertions.assertThat(bppResponse.getItems().get(0).getBidPackageItem().getScenarioIdentity())
                .isEqualTo(scenarioItem.getScenarioIdentity());
            softAssertions.assertThat(bppResponse.getItems().get(0).getBidPackageItem().getIterationIdentity())
                .isEqualTo(scenarioItem.getIterationIdentity());
        }
    }

    @Test
    @TestRail(id = {22127, 22071, 22917})
    @Issue("COL-1704")
    @Description("Verify user can add more than 10 project users (multiple users) by project creation API")
    public void createProjectWithMoreThan10Users() {
        List<String> projectUsersList = new ArrayList<>();
        List<BidPackageItemRequest> itemsList = new ArrayList<>();
        itemsList.add(BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity(scenarioItem.getComponentIdentity())
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build());

        projectUsersList.add(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()));
        List<BidPackageProjectUserParameters> usersList = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            UserCredentials user = UserUtil.getUser();
            String userIdentity = new AuthUserContextUtil().getAuthUserIdentity(user.getEmail());
            usersList.add(BidPackageProjectUserParameters.builder()
                .userIdentity(userIdentity)
                .customerIdentity(PropertiesContext.get("${env}.customer_identity"))
                .build());
            projectUsersList.add(userIdentity);
        }

        BidPackageProjectResponse bppResponse = QmsProjectResources.createProject(new HashMap<>(),
            itemsList,
            usersList,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        softAssertions.assertThat(bppResponse.getUsers().size()).isEqualTo(11);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(bppResponse.getUsers().stream()
                .anyMatch(u -> projectUsersList.contains(u.getUserIdentity()))).isTrue();
            softAssertions.assertThat(bppResponse.getUsers().stream()
                .allMatch(u -> u.getUser().getAvatarColor() != null)).isTrue();
        }
    }

    @Test
    @TestRail(id = {22126})
    @Description("Verify duplicate project users are discarded by project creation API")
    public void createProjectWithDuplicateUsers() {
        List<String> projectUsersList = new ArrayList<>();
        List<BidPackageItemRequest> itemsList = new ArrayList<>();
        itemsList.add(BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity(scenarioItem.getComponentIdentity())
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build());

        projectUsersList.add(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()));
        List<BidPackageProjectUserParameters> usersList = new ArrayList<>();
        usersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()))
            .customerIdentity(PropertiesContext.get("${env}.customer_identity"))
            .build());

        UserCredentials duplicateUser = UserUtil.getUser();
        String duplicateUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(duplicateUser.getEmail());
        usersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity(duplicateUserIdentity)
            .customerIdentity(PropertiesContext.get("${env}.customer_identity"))
            .build());
        projectUsersList.add(duplicateUserIdentity);

        usersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity(duplicateUserIdentity)
            .customerIdentity(PropertiesContext.get("${env}.customer_identity"))
            .build());

        BidPackageProjectResponse bppResponse = QmsProjectResources.createProject(new HashMap<>(),
            itemsList,
            usersList,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        softAssertions.assertThat(bppResponse.getUsers().size()).isEqualTo(2);
        if (softAssertions.wasSuccess()) {
            for (int i = 0; i < projectUsersList.size(); i++) {
                softAssertions.assertThat(projectUsersList).contains(bppResponse.getUsers().get(i).getUser()
                    .getIdentity());
            }
        }
        softAssertions.assertThat(bppResponse.getItems().size()).isEqualTo(1);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(bppResponse.getItems().get(0).getBidPackageItem().getComponentIdentity())
                .isEqualTo(scenarioItem.getComponentIdentity());
            softAssertions.assertThat(bppResponse.getItems().get(0).getBidPackageItem().getScenarioIdentity())
                .isEqualTo(scenarioItem.getScenarioIdentity());
            softAssertions.assertThat(bppResponse.getItems().get(0).getBidPackageItem().getIterationIdentity())
                .isEqualTo(scenarioItem.getIterationIdentity());
        }
    }

    @Test
    @TestRail(id = {22125})
    @Issue("COL-1837")
    @Description("Verify user can add multiple project users by project creation API(Invalid/null and without User identity)")
    public void createProjectWithInvalidUserIdentity() {
        List<BidPackageItemRequest> itemsList = new ArrayList<>();
        itemsList.add(BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity(scenarioItem.getComponentIdentity())
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build());

        List<BidPackageProjectUserParameters> usersList = new ArrayList<>();
        usersList.add(BidPackageProjectUserParameters.builder()
            .customerIdentity(PropertiesContext.get("${env}.customer_identity"))
            .build());

        usersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity("123456789012")
            .customerIdentity(PropertiesContext.get("${env}.customer_identity"))
            .build());

        usersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity("")
            .customerIdentity(PropertiesContext.get("${env}.customer_identity"))
            .build());

        BidPackageProjectResponse bppResponse = QmsProjectResources.createProject(new HashMap<>(),
            itemsList,
            usersList,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        softAssertions.assertThat(bppResponse.getUsers().size()).isEqualTo(1);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(bppResponse.getUsers().get(0).getUser().getIdentity())
                .isEqualTo(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()));
        }

        softAssertions.assertThat(bppResponse.getItems().size()).isEqualTo(1);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(bppResponse.getItems().get(0).getBidPackageItem().getComponentIdentity())
                .isEqualTo(scenarioItem.getComponentIdentity());
            softAssertions.assertThat(bppResponse.getItems().get(0).getBidPackageItem().getScenarioIdentity())
                .isEqualTo(scenarioItem.getScenarioIdentity());
            softAssertions.assertThat(bppResponse.getItems().get(0).getBidPackageItem().getIterationIdentity())
                .isEqualTo(scenarioItem.getIterationIdentity());
        }
    }

    @Test
    @TestRail(id = {22124})
    @Description("Verify user can add multiple project users by project creation API(Invalid/null and without Customer identity)")
    public void createProjectWithInvalidCustomerIdentity() {
        List<String> projectUsersList = new ArrayList<>();
        List<BidPackageItemRequest> itemsList = new ArrayList<>();
        itemsList.add(BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity(scenarioItem.getComponentIdentity())
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build());

        projectUsersList.add(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()));
        List<BidPackageProjectUserParameters> usersList = new ArrayList<>();
        String firstUserEmail = UserUtil.getUser().getEmail();
        String firstUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(firstUserEmail);
        usersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity(new AuthUserContextUtil().getAuthUserIdentity(firstUserEmail))
            .build());
        projectUsersList.add(firstUserIdentity);

        String secondUserEmail = UserUtil.getUser().getEmail();
        String secondUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(secondUserEmail);
        usersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity(new AuthUserContextUtil().getAuthUserIdentity(secondUserEmail))
            .customerIdentity("123456789012")
            .build());
        projectUsersList.add(secondUserIdentity);

        String thirdUserEmail = UserUtil.getUser().getEmail();
        String userIdentity3 = new AuthUserContextUtil().getAuthUserIdentity(thirdUserEmail);
        usersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity(new AuthUserContextUtil().getAuthUserIdentity(thirdUserEmail))
            .customerIdentity("")
            .build());
        projectUsersList.add(userIdentity3);

        BidPackageProjectResponse bppResponse = QmsProjectResources.createProject(new HashMap<>(),
            itemsList,
            usersList,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        softAssertions.assertThat(bppResponse.getUsers().size()).isEqualTo(4);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(bppResponse.getUsers().stream()
                .anyMatch(u -> projectUsersList.contains(u.getUserIdentity()))).isTrue();
        }
    }

    @Test
    @TestRail(id = {22956})
    @Description("Verify user is able to retrieve avatarColor inside of a specific QMS project's user model")
    public void getProjectByIdentity() {
        BidPackageResponse bidPackageResponse = QmsBidPackageResources.createBidPackage("BPN" + new GenerateStringUtil().getRandomNumbers(), currentUser);
        if (bidPackageResponse != null) {
            BidPackageItemResponse bidPackageItemResponse = QmsBidPackageResources.createBidPackageItem(
                QmsBidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
                bidPackageResponse.getIdentity(), currentUser, BidPackageItemResponse.class, HttpStatus.SC_CREATED);
            if (bidPackageItemResponse != null) {
                BidPackageProjectResponse bidPackageProjectResponse = QmsBidPackageResources.createBidPackageProject(new HashMap<>(), bidPackageResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
                {
                    BidPackageProjectResponse bppResponse = QmsProjectResources.getProject(
                        bidPackageProjectResponse.getIdentity(),
                        BidPackageProjectResponse.class,
                        HttpStatus.SC_OK,
                        currentUser);
                    softAssertions.assertThat(bppResponse.getUsers().size()).isGreaterThan(0);
                    if (softAssertions.wasSuccess()) {
                        for (int j = 0; j < bppResponse.getUsers().size(); j++) {
                            softAssertions.assertThat(bppResponse.getUsers().get(j).getUser().getAvatarColor())
                                .isNotNull();
                        }
                    }
                }
            }
            QmsBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        }
    }

    @Test
    @TestRail(id = {23094})
    @Description("Verify user is able to retrieve avatarColor inside all QMS project's user model")
    public void getAllProjects() {
        BidPackageProjectsResponse bppResponse = QmsProjectResources.getProjects(
            BidPackageProjectsResponse.class,
            HttpStatus.SC_OK,
            currentUser);
        softAssertions.assertThat(bppResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(bppResponse.getPageNumber()).isEqualTo(1);
        softAssertions.assertThat(bppResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            for (int i = 0; i < bppResponse.getItems().size(); i++) {
                for (int j = 0; j < bppResponse.getItems().get(i).getUsers().size(); j++) {
                    softAssertions.assertThat(bppResponse.getItems().get(i).getUsers().get(j).getUser()
                        .getAvatarColor()).isNotNull();
                }
            }
        }
    }

    @Test
    @TestRail(id = {24482})
    @Description("For /project? endpoint || Verify project can be created with OPEN or IN_PROGRESS status)")
    public void createProjectWithStatusOpenAndInProgress() {
        //OPEN
        ScenarioItem scenarioItemOpen = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        List<BidPackageItemRequest> itemsList = new ArrayList<>();
        itemsList.add(BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity(scenarioItemOpen.getComponentIdentity())
                .scenarioIdentity(scenarioItemOpen.getScenarioIdentity())
                .iterationIdentity(scenarioItemOpen.getIterationIdentity())
                .build())
            .build());

        HashMap<String, String> prjAttributesMap = new HashMap<>();
        prjAttributesMap.put("projectStatus", "OPEN");
        BidPackageProjectResponse bppResponse = QmsProjectResources.createProject(prjAttributesMap,
            itemsList,
            null,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        softAssertions.assertThat(bppResponse.getStatus()).isEqualTo("OPEN");
        QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItem, currentUser);

        //IN_PROGRESS
        ScenarioItem scenarioItemInProgress = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        itemsList = new ArrayList<>();
        itemsList.add(BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity(scenarioItemInProgress.getComponentIdentity())
                .scenarioIdentity(scenarioItemInProgress.getScenarioIdentity())
                .iterationIdentity(scenarioItemInProgress.getIterationIdentity())
                .build())
            .build());

        prjAttributesMap = new HashMap<>();
        prjAttributesMap.put("projectStatus", "IN_PROGRESS");
        bppResponse = QmsProjectResources.createProject(prjAttributesMap,
            itemsList,
            null,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        softAssertions.assertThat(bppResponse.getStatus()).isEqualTo("IN_PROGRESS");
        QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItem, currentUser);
    }

    @Test
    @TestRail(id = {24483})
    @Description("Verify the default project created in the background during scenarios discussion creation  will have status as OPEN")
    public void verifyDefaultProjectStatusToBeOpen() {
        //Create default project via scenario discussion
        ScenarioDiscussionResponse scenarioDiscussionResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
        softAssertions.assertThat(scenarioDiscussionResponse.getIdentity()).isNotNull();

        //Get Project
        BidPackageProjectResponse bppResponse = QmsProjectResources.getProject(
            scenarioDiscussionResponse.projectIdentity,
            BidPackageProjectResponse.class,
            HttpStatus.SC_OK,
            currentUser);

        softAssertions.assertThat(bppResponse.getStatus()).isEqualTo("OPEN");

        //Delete Discussion
        QmsScenarioDiscussionResources.deleteScenarioDiscussion(scenarioDiscussionResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(id = {14625})
    @Description("Verify that the user can find only those projects in which he participates")
    public void getProjectsForParticipant() {
        BidPackageProjectsResponse bidPackageProjectsResponse = QmsProjectResources.getProjects(BidPackageProjectsResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(bidPackageProjectsResponse.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(bidPackageProjectsResponse.getItems().stream()
            .allMatch(i -> i.getOwnerUserIdentity() != null)).isTrue();
    }

    @Test
    @TestRail(id = {14626, 14669})
    @Description("Verify that the user can find a project by identity in which he participates")
    public void getProjectForParticipant() {
        List<BidPackageItemRequest> itemsList = new ArrayList<>();
        itemsList.add(BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity(scenarioItem.getComponentIdentity())
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build());

        BidPackageProjectResponse bppResponse = QmsProjectResources.createProject(new HashMap<>(),
            itemsList,
            null,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);
        BidPackageProjectResponse getBppResponse = QmsProjectResources.getProject(bppResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(getBppResponse.getItems().stream()
            .anyMatch(i -> i.getProjectIdentity().equals(bppResponse.getIdentity()))).isTrue();
        softAssertions.assertThat(getBppResponse.getUsers().stream()
            .anyMatch(u -> u.getUserIdentity().equals(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail())))).isTrue();
        softAssertions.assertThat(getBppResponse.getOwnerUserIdentity()).isNotNull();
    }

    @Test
    @TestRail(id = {14627})
    @Description("Verify that the user can find a project by identity in which he participates")
    public void getEmptyProjectsForParticipant() {
        BidPackageProjectsResponse bidProjectsResponse = QmsProjectResources.getProjects(BidPackageProjectsResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(bidProjectsResponse.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(id = {17102, 17103})
    @Description("Verifying Project creation with bid package items in request. Verify that User can create project using /project URL")
    public void createProjectWithBidPackageItemsAndValidUserIdentity() {
        ScenarioItem secondScenarioItem = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        List<BidPackageItemRequest> itemsList = new ArrayList<>();
        itemsList.add(BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity(scenarioItem.getComponentIdentity())
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build());
        itemsList.add(BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity(secondScenarioItem.getComponentIdentity())
                .scenarioIdentity(secondScenarioItem.getScenarioIdentity())
                .iterationIdentity(secondScenarioItem.getIterationIdentity())
                .build())
            .build());

        List<BidPackageProjectUserParameters> usersList = new ArrayList<>();
        String firstUserEmail = UserUtil.getUser().getEmail();
        String firstUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(firstUserEmail);
        usersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity(new AuthUserContextUtil().getAuthUserIdentity(firstUserEmail))
            .customerIdentity(PropertiesContext.get("${env}.customer_identity"))
            .build());

        BidPackageProjectResponse bppResponse = QmsProjectResources.createProject(new HashMap<>(),
            itemsList,
            usersList,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        softAssertions.assertThat(bppResponse.getUsers().stream()
            .anyMatch(u -> u.getUserIdentity().equals(firstUserIdentity))).isTrue();
        softAssertions.assertThat(bppResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(bppResponse.getItems().stream()
                .anyMatch(i -> i.getBidPackageItem().getScenarioIdentity().equals(scenarioItem.getScenarioIdentity()) &&
                    i.getBidPackageItem().getComponentIdentity().equals(scenarioItem.getComponentIdentity()) &&
                    i.getBidPackageItem().getIterationIdentity().equals(scenarioItem.getIterationIdentity()))).isTrue();
            softAssertions.assertThat(bppResponse.getItems().stream()
                .anyMatch(i -> i.getBidPackageItem().getScenarioIdentity().equals(secondScenarioItem.getScenarioIdentity()) &&
                    i.getBidPackageItem().getComponentIdentity().equals(secondScenarioItem.getComponentIdentity()) &&
                    i.getBidPackageItem().getIterationIdentity().equals(secondScenarioItem.getIterationIdentity()))).isTrue();
        }
        softAssertions.assertThat(bppResponse.getOwnerUserIdentity()).isNotNull();
        QmsApiTestUtils.deleteScenarioViaCidApp(secondScenarioItem, currentUser);
    }
}