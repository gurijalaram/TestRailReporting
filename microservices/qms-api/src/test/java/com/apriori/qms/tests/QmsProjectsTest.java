package com.apriori.qms.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.controller.QmsProjectResources;
import com.apriori.qms.controller.QmsScenarioDiscussionResources;
import com.apriori.qms.entity.request.bidpackage.BidPackageItemParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageItemRequest;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectNotificationRequest;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectUserParameters;
import com.apriori.qms.entity.response.bidpackage.BidPackageItemResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectNotificationResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectsResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.utils.DateFormattingUtils;
import com.apriori.utils.DateUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.Link;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.QmsApiTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QmsProjectsTest extends TestUtil {
    private final UserCredentials currentUser = UserUtil.getUser();
    private SoftAssertions softAssertions;
    private ScenarioItem scenarioItem;

    @Before
    public void beforeTest() {
        softAssertions = new SoftAssertions();
        scenarioItem = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
    }

    @After
    public void afterTest() {
        QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItem, currentUser);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"22128"})
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
    @TestRail(testCaseId = {"22127", "22071", "22917"})
    @Link("Defect - https://jira.apriori.com/browse/COL-1704")
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
        for (int i = 1; i < 12; i++) {
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

        softAssertions.assertThat(bppResponse.getUsers().size()).isEqualTo(12);
        if (softAssertions.wasSuccess()) {
            for (int i = 0; i < projectUsersList.size(); i++) {
                softAssertions.assertThat(projectUsersList).contains(bppResponse.getUsers().get(i).getUser()
                    .getIdentity());
                softAssertions.assertThat(bppResponse.getUsers().get(i).getUser().getAvatarColor()).isNotNull();
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
    @TestRail(testCaseId = {"22126"})
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
    @TestRail(testCaseId = {"22125"})
    @Link("Defect - https://jira.apriori.com/browse/COL-1837")
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
    @TestRail(testCaseId = {"22124"})
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
        UserCredentials user1 = UserUtil.getUser();
        String userIdentity1 = new AuthUserContextUtil().getAuthUserIdentity(user1.getEmail());
        usersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity(new AuthUserContextUtil().getAuthUserIdentity(user1.getEmail()))
            .build());
        projectUsersList.add(userIdentity1);

        UserCredentials user2 = UserUtil.getUser();
        String userIdentity2 = new AuthUserContextUtil().getAuthUserIdentity(user2.getEmail());
        usersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity(new AuthUserContextUtil().getAuthUserIdentity(user2.getEmail()))
            .customerIdentity("123456789012")
            .build());
        projectUsersList.add(userIdentity2);

        UserCredentials user3 = UserUtil.getUser();
        String userIdentity3 = new AuthUserContextUtil().getAuthUserIdentity(user3.getEmail());
        usersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity(new AuthUserContextUtil().getAuthUserIdentity(user3.getEmail()))
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
    @TestRail(testCaseId = {"22956"})
    @Description("Verify user is able to retrieve avatarColor inside of a specific QMS project's user model")
    public void getProjectByIdentity() {
        if (scenarioItem != null) {
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
            }
            if (bidPackageResponse != null) {
                QmsBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
            }
        }
    }

    @Test
    @TestRail(testCaseId = {"23094"})
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
    @TestRail(testCaseId = {"21603"})
    @Description("Verify that the User can filter project by dueAt(operator NL)")
    public void getFilteredProjectsByDueAtWithOperatorNL() {
        String[] params = {"dueAt[NL],null"};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getDueAt() == null)).isTrue();
        }
    }

    @Test
    @TestRail(testCaseId = {"21604"})
    @Description("Verify that the User can filter project by dueAt(operator GT)")
    public void getFilteredProjectsByDueAtWithOperatorGT() {
        //DueAt format yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
        String[] params = {"dueAt[GT]," + DateUtil.getDateDaysBefore(30, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ)};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getDueAt().isAfter(LocalDateTime.now().minusDays(30)))).isTrue();
        }

        //DueAt format yyyy-MM-dd
        params[0] = "dueAt[GT]," + DateUtil.getDateDaysBefore(30, DateFormattingUtils.dtf_yyyyMMdd);
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getDueAt().isAfter(LocalDateTime.now().minusDays(30)))).isTrue();
        }
    }

    @Test
    @TestRail(testCaseId = {"21605"})
    @Description("Verify that the User can filter project by dueAt(operator LT)")
    public void getFilteredProjectsByDueAtWithOperatorLT() {
        //DueAt format yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
        String[] params = {"dueAt[LT]," + DateUtil.getDateDaysBefore(0, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ)};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getDueAt().isBefore(LocalDateTime.now()))).isTrue();
        }

        //DueAt format yyyy-MM-dd
        params[0] = "dueAt[LT]," + DateUtil.getDateDaysBefore(0, DateFormattingUtils.dtf_yyyyMMdd);
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getDueAt().isBefore(LocalDateTime.now()))).isTrue();
        }
    }

    @Test
    @TestRail(testCaseId = {"24072"})
    @Description("Verify that User can filter project by using range of dates")
    public void getFilteredProjectsByDueAtRange() {
        //DueAt format yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
        String[] params = {"dueAt[LT]," + DateUtil.getDateDaysBefore(0, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ),
            "dueAt[GT]," + DateUtil.getDateDaysBefore(30, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ)};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getDueAt().isBefore(LocalDateTime.now()) &&
                    i.getDueAt().isAfter(LocalDateTime.now().minusDays(30)))).isTrue();
        }

        //DueAt format yyyy-MM-dd
        params[0] = "dueAt[LT]," + DateUtil.getDateDaysBefore(0, DateFormattingUtils.dtf_yyyyMMdd);
        params[1] = "dueAt[GT]," + DateUtil.getDateDaysBefore(30, DateFormattingUtils.dtf_yyyyMMdd);
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getDueAt().isBefore(LocalDateTime.now()) &&
                    i.getDueAt().isAfter(LocalDateTime.now().minusDays(30)))).isTrue();
        }
    }

    @Test
    @TestRail(testCaseId = {"17221", "17222", "17223", "17224"})
    @Description("Verify that the User can filter project by status with operators EQ, NI, IN, NE")
    public void getFilteredProjectsByStatus() {
        //C17221
        //COMPLETED
        ScenarioItem scenarioItem = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        List<BidPackageItemRequest> itemsList = new ArrayList<>();
        itemsList.add(BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity(scenarioItem.getComponentIdentity())
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build());

        HashMap<String, String> projectAttributesMap = new HashMap<>();
        projectAttributesMap.put("projectStatus", "COMPLETED");
        QmsProjectResources.createProject(projectAttributesMap,
            itemsList,
            null,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        String[] params = {"status[EQ],COMPLETED", "pageNumber,1"};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getStatus().equals("COMPLETED"))).isTrue();
        }

        QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItem, currentUser);

        //IN_NEGOTIATION
        scenarioItem = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        itemsList = new ArrayList<>();
        itemsList.add(BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity(scenarioItem.getComponentIdentity())
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build());
        projectAttributesMap = new HashMap<>();
        projectAttributesMap.put("projectStatus", "IN_NEGOTIATION");
        QmsProjectResources.createProject(projectAttributesMap,
            itemsList,
            null,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        params[0] = "status[EQ],IN_NEGOTIATION";
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getStatus().equals("IN_NEGOTIATION"))).isTrue();
        }

        QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItem, currentUser);

        //PURCHASED
        scenarioItem = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        itemsList = new ArrayList<>();
        itemsList.add(BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity(scenarioItem.getComponentIdentity())
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build());
        projectAttributesMap = new HashMap<>();
        projectAttributesMap.put("projectStatus", "PURCHASED");
        QmsProjectResources.createProject(projectAttributesMap,
            itemsList,
            null,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        params[0] = "status[EQ],PURCHASED";
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getStatus().equals("PURCHASED"))).isTrue();
        }

        QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItem, currentUser);

        //SENT_FOR_QUOTATION
        scenarioItem = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        itemsList = new ArrayList<>();
        itemsList.add(BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity(scenarioItem.getComponentIdentity())
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build());
        projectAttributesMap = new HashMap<>();
        projectAttributesMap.put("projectStatus", "SENT_FOR_QUOTATION");
        QmsProjectResources.createProject(projectAttributesMap,
            itemsList,
            null,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        params[0] = "status[EQ],SENT_FOR_QUOTATION";
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getStatus().equals("SENT_FOR_QUOTATION"))).isTrue();
        }

        QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItem, currentUser);

        //C17222
        params[0] = "status[NI],IN_NEGOTIATION";
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .noneMatch(i -> i.getStatus().equals("IN_NEGOTIATION"))).isTrue();
        }

        params[0] = "status[NI],IN_NEGOTIATION|COMPLETED";
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .noneMatch(i -> i.getStatus().equals("IN_NEGOTIATION") ||
                    i.getStatus().equals("COMPLETED"))).isTrue();
        }

        //C17223
        params[0] = "status[IN],IN_NEGOTIATION";
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getStatus().equals("IN_NEGOTIATION"))).isTrue();
        }

        params[0] = "status[IN],IN_NEGOTIATION|COMPLETED";
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getStatus().equals("IN_NEGOTIATION") ||
                    i.getStatus().equals("COMPLETED"))).isTrue();
        }

        //C17224
        params[0] = "status[NE],IN_NEGOTIATION";
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .noneMatch(i -> i.getStatus().equals("IN_NEGOTIATION"))).isTrue();
        }

        params[0] = "status[NE],COMPLETED";
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .noneMatch(i -> i.getStatus().equals("COMPLETED"))).isTrue();
        }

        //C24073
        params[0] = "status[NE],IN_NEGOTIATION";
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .noneMatch(i -> i.getStatus().equals("IN_NEGOTIATION"))).isTrue();
        }
    }

    @Test
    @TestRail(testCaseId = {"22773", "22774"})
    @Description("Verify that the User can filter project by owner with operators IN, NI")
    public void getFilteredProjectsByOwner() {
        //C22773
        String[] params = {"pageNumber,1", "owner[IN]," + new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail())};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getOwner()
                    .equals(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail())))).isTrue();
        }

        //C22774
        params[1] = "owner[NI]," + new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail());
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .noneMatch(i -> i.getOwner()
                    .equals(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail())))).isTrue();
        }
    }

    @Test
    @TestRail(testCaseId = {"23771", "23772", "23774"})
    @Description("Verify that the User can filter projects by Members with operators IN, NI")
    public void getFilteredProjectsByMembers() {
        String currentUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail());
        ScenarioItem scenarioItem1 = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        List<BidPackageItemRequest> itemsList = new ArrayList<>();
        itemsList.add(BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity(scenarioItem1.getComponentIdentity())
                .scenarioIdentity(scenarioItem1.getScenarioIdentity())
                .iterationIdentity(scenarioItem1.getIterationIdentity())
                .build())
            .build());

        String displayName = new GenerateStringUtil().getRandomString();
        HashMap<String, String> projectAttributesMap = new HashMap<>();
        projectAttributesMap.put("projectStatus", "COMPLETED");
        projectAttributesMap.put("projectDisplayName", displayName);
        QmsProjectResources.createProject(projectAttributesMap,
            itemsList,
            null,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        ScenarioItem scenarioItem2 = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        itemsList = new ArrayList<>();
        itemsList.add(BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity(scenarioItem2.getComponentIdentity())
                .scenarioIdentity(scenarioItem2.getScenarioIdentity())
                .iterationIdentity(scenarioItem2.getIterationIdentity())
                .build())
            .build());

        List<BidPackageProjectUserParameters> usersList = new ArrayList<>();
        UserCredentials newUser1 = UserUtil.getUser();
        String newUserIdentity1 = new AuthUserContextUtil().getAuthUserIdentity(newUser1.getEmail());
        usersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity(newUserIdentity1)
            .customerIdentity(PropertiesContext.get("${env}.customer_identity"))
            .build());

        UserCredentials newUser2 = UserUtil.getUser();
        String newUserIdentity2 = new AuthUserContextUtil().getAuthUserIdentity(newUser2.getEmail());
        usersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity(newUserIdentity2)
            .customerIdentity(PropertiesContext.get("${env}.customer_identity"))
            .build());

        projectAttributesMap = new HashMap<>();
        projectAttributesMap.put("projectStatus", "COMPLETED");
        projectAttributesMap.put("projectDisplayName", displayName);
        QmsProjectResources.createProject(projectAttributesMap,
            itemsList,
            usersList,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        //C23771
        String[] params = {"pageNumber,1", "members[IN]," + newUserIdentity1, "displayName[CN]," + displayName};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getUsers().stream()
                    .anyMatch(u -> u.getUserIdentity().equals(newUserIdentity1)))).isTrue();
        }

        params[1] = "members[IN]," + newUserIdentity1 + "|" + newUserIdentity2;
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getUsers().stream()
                    .anyMatch(u -> u.getUserIdentity().equals(newUserIdentity1) ||
                        u.getUserIdentity().equals(newUserIdentity2)))).isTrue();
        }

        //C23772
        params[1] = "members[NI]," + newUserIdentity1;
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getUsers().stream()
                    .noneMatch(u -> u.getUserIdentity().equals(newUserIdentity1)))).isTrue();
        }

        params[1] = "members[NI]," + newUserIdentity1 + "|" + newUserIdentity2;
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getUsers().stream()
                    .noneMatch(u -> u.getUserIdentity().equals(newUserIdentity1)))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getUsers().stream()
                    .noneMatch(u -> u.getUserIdentity().equals(newUserIdentity2)))).isTrue();
        }

        //C23774
        params[1] = "members[NI]," + currentUserIdentity;
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isZero();

        QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItem1, currentUser);
        QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItem2, currentUser);
    }

    @Test
    @TestRail(testCaseId = {"22775"})
    @Issue("COL-1831")
    @Description("Verify that the User can filter projects by Unread messages(yes)")
    public void getFilteredProjectsByUnReadMessagesYes() {
        String[] params = {"pageNumber,1", "hasUnreadMessages[EQ],yes"};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            List<String> projectIdsList = new ArrayList<>();
            filteredProjectsResponse.getItems()
                .stream().iterator()
                .forEachRemaining(i -> projectIdsList.add(i.getIdentity()));
            BidPackageProjectNotificationRequest notifyRequest = BidPackageProjectNotificationRequest.builder()
                .projectIdentities(projectIdsList)
                .build();
            BidPackageProjectNotificationResponse notifyResponse = QmsProjectResources.retrieveProjectNotifications(notifyRequest,
                BidPackageProjectNotificationResponse.class, HttpStatus.SC_CREATED, currentUser);
            softAssertions.assertThat(notifyResponse.getNotificationsCount().stream()
                .allMatch(n -> n.getUnreadNotificationsCount() > 0)).isTrue();
        }
    }

    @Test
    @TestRail(testCaseId = {"22776"})
    @Description("Verify that the User can filter projects by Unread messages(no)")
    public void getFilteredProjectsByUnReadMessagesNo() {
        String[] params = {"pageNumber,1", "hasUnreadMessages[EQ],no"};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            List<String> projectIdsList = new ArrayList<>();
            filteredProjectsResponse.getItems()
                .stream().iterator()
                .forEachRemaining(i -> projectIdsList.add(i.getIdentity()));
            BidPackageProjectNotificationRequest notifyRequest = BidPackageProjectNotificationRequest.builder()
                .projectIdentities(projectIdsList)
                .build();
            BidPackageProjectNotificationResponse notifyResponse = QmsProjectResources.retrieveProjectNotifications(notifyRequest,
                BidPackageProjectNotificationResponse.class, HttpStatus.SC_CREATED, currentUser);
            softAssertions.assertThat(notifyResponse.getNotificationsCount().stream()
                .allMatch(n -> n.getUnreadNotificationsCount() == 0)).isTrue();
        }
    }

    @Test
    @Issue("COL-1831")
    @TestRail(testCaseId = {"24073", "24074", "24075", "24076", "24077", "24078", "24079", "24080", "24081", "24082", "24083", "24104", "24105", "24111", "24112"})
    @Description("Verify that the User can filter project by status, owner, displayName, members, dueAt, read/unread messages")
    public void getFilteredProjectsByMultipleFields() {
        ScenarioItem scenarioItem = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        List<BidPackageItemRequest> itemsList = new ArrayList<>();
        itemsList.add(BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity(scenarioItem.getComponentIdentity())
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build());

        List<BidPackageProjectUserParameters> usersList = new ArrayList<>();
        UserCredentials projectMemberUser = UserUtil.getUser();
        String projectMemberUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(projectMemberUser.getEmail());
        usersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity(projectMemberUserIdentity)
            .customerIdentity(PropertiesContext.get("${env}.customer_identity"))
            .build());

        String displayName = new GenerateStringUtil().getRandomString();
        String status = "IN_NEGOTIATION";
        String owner = new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail());
        String dueAtLT = DateUtil.getDateDaysAfter(12, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);
        String dueAtGT = DateUtil.getDateDaysBefore(30, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);
        HashMap<String, String> projectAttributesMap = new HashMap<>();
        projectAttributesMap.put("projectStatus", status);
        projectAttributesMap.put("projectDisplayName", displayName);
        QmsProjectResources.createProject(projectAttributesMap,
            itemsList,
            usersList,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        //C24073
        String[] params = {"pageNumber,1", "status[IN]," + status, "displayName[CN]," + displayName};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getStatus().equals(status))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getDisplayName().contains(displayName))).isTrue();
        }

        //24074
        String[] params1 = {"pageNumber,1", "status[IN]," + status, "displayName[CN]," + displayName, "owner[IN]," + owner};
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params1);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getStatus().equals(status))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getDisplayName().contains(displayName))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getOwner().equals(owner))).isTrue();
        }

        //24075
        String[] params2 = {"pageNumber,1", "status[IN]," + status, "displayName[CN]," + displayName, "owner[IN]," + owner, "dueAt[LT]," + dueAtLT};
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params2);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getStatus().equals(status))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getDisplayName().contains(displayName))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getOwner().equals(owner))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream().allMatch(i -> i.getDueAt()
                .isBefore(LocalDateTime.parse(dueAtLT, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ)))).isTrue();
        }

        //24078
        String[] params5 = {"pageNumber,1", "status[IN]," + status, "owner[IN]," + owner};
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params5);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getStatus().equals(status))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getOwner().equals(owner))).isTrue();
        }

        //24079
        String[] params6 = {"pageNumber,1", "displayName[CN]," + displayName, "owner[IN]," + owner};
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params6);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getDisplayName().contains(displayName))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getOwner().equals(owner))).isTrue();
        }

        //24080
        String[] params7 = {"pageNumber,1", "status[NI],COMPLETED", "owner[IN]," + owner};
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params7);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .noneMatch(i -> i.getStatus().equals("COMPLETED"))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getOwner().equals(owner))).isTrue();
        }

        //24081
        String newOwner = new AuthUserContextUtil().getAuthUserIdentity(UserUtil.getUser().getEmail());
        String[] params8 = {"pageNumber,1", "status[NI],COMPLETED", "owner[NI]," + newOwner};
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params8);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .noneMatch(i -> i.getStatus().equals("COMPLETED"))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .noneMatch(i -> i.getOwner().equals(newOwner))).isTrue();
        }

        //24082
        String[] params9 = {"pageNumber,1", "status[NI],COMPLETED", "displayName[CN]," + displayName};
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params9);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .noneMatch(i -> i.getStatus().equals("COMPLETED"))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getDisplayName().contains(displayName))).isTrue();
        }

        //24083
        String[] params10 = {"pageNumber,1", "displayName[CN]," + displayName, "owner[NI]," + newOwner};
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params10);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getDisplayName().contains(displayName))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .noneMatch(i -> i.getOwner().equals(newOwner))).isTrue();
        }

        //24104
        String[] params11 = {"pageNumber,1", "status[IN]," + status, "displayName[CN]," + displayName, "owner[IN]," + owner, "dueAt[GT]," + dueAtGT};
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params11);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getStatus().equals(status))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getDisplayName().contains(displayName))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getOwner().equals(owner))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream().allMatch(i -> i.getDueAt()
                .isAfter(LocalDateTime.parse(dueAtGT, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ)))).isTrue();
        }

        //24111
        String[] params13 = {"pageNumber,1", "status[IN]," + status, "members[IN]," + projectMemberUserIdentity};
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params13);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getStatus().equals(status))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getUsers().stream()
                    .anyMatch(u -> u.getUserIdentity().equals(projectMemberUserIdentity)))).isTrue();
        }

        //24112
        String[] params14 = {"pageNumber,1", "displayName[CN]," + displayName, "members[NI]," + projectMemberUserIdentity};
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params14);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getDisplayName().contains(displayName))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getUsers().stream()
                    .noneMatch(u -> u.getUserIdentity().equals(projectMemberUserIdentity)))).isTrue();
        }

        //24076
        String[] params3 = {"pageNumber,1", "status[IN]," + status, "displayName[CN]," + displayName, "owner[IN]," + owner, "dueAt[LT]," + dueAtLT, "hasUnreadMessages[EQ],no"};
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params3);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getStatus().equals(status))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getDisplayName().contains(displayName))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getOwner().equals(owner))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream().allMatch(i -> i.getDueAt()
                .isBefore(LocalDateTime.parse(dueAtLT, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ)))).isTrue();
        }

        //24077
        ScenarioDiscussionResponse scenarioDiscussionResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
        QmsScenarioDiscussionResources.addCommentToDiscussion(scenarioDiscussionResponse.getIdentity(), displayName, "ACTIVE", currentUser);
        String[] params4 = {"pageNumber,1", "status[IN]," + status, "displayName[CN]," + displayName, "owner[IN]," + owner, "dueAt[LT]," + dueAtLT, "hasUnreadMessages[EQ],yes"};
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params4);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getStatus().equals(status))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getDisplayName().contains(displayName))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getOwner().equals(owner))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream().allMatch(i -> i.getDueAt()
                .isBefore(LocalDateTime.parse(dueAtLT, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ)))).isTrue();
        }

        //24105
        String[] params12 = {"pageNumber,1", "status[IN]," + status, "owner[IN]," + owner, "hasUnreadMessages[EQ],yes"};
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params12);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getStatus().equals(status))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getOwner().equals(owner))).isTrue();
        }

        QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItem, currentUser);
    }
}