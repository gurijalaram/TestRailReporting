package com.apriori.qms.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.controller.QmsProjectResources;
import com.apriori.qms.entity.request.bidpackage.BidPackageItemParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageItemRequest;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectUserParameters;
import com.apriori.qms.entity.response.bidpackage.BidPackageItemResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectsResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageResponse;
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

        BidPackageProjectResponse bppResponse = QmsProjectResources.createProject(new GenerateStringUtil().getRandomNumbers(),
            new GenerateStringUtil().getRandomNumbers(),
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

        BidPackageProjectResponse bppResponse = QmsProjectResources.createProject(new GenerateStringUtil().getRandomNumbers(),
            new GenerateStringUtil().getRandomNumbers(),
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

        BidPackageProjectResponse bppResponse = QmsProjectResources.createProject(new GenerateStringUtil().getRandomNumbers(),
            new GenerateStringUtil().getRandomNumbers(),
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

        BidPackageProjectResponse bppResponse = QmsProjectResources.createProject(new GenerateStringUtil().getRandomNumbers(),
            new GenerateStringUtil().getRandomNumbers(),
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

        BidPackageProjectResponse bppResponse = QmsProjectResources.createProject(new GenerateStringUtil().getRandomNumbers(),
            new GenerateStringUtil().getRandomNumbers(),
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
}
