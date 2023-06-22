package com.apriori.qms.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.controller.QmsProjectResources;
import com.apriori.qms.entity.request.bidpackage.BidPackageItemParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageItemRequest;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectNotificationRequest;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectUserParameters;
import com.apriori.qms.entity.response.bidpackage.BidPackageItemResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectNotificationResponse;
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
import io.qameta.allure.Issue;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import utils.QmsApiTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QmsProjectsTest extends TestUtil {
    private static final UserCredentials currentUser = UserUtil.getUser();
    private static final List<ScenarioItem> scenarioItemRemoveList = new ArrayList<>();
    private SoftAssertions softAssertions = new SoftAssertions();
    private ScenarioItem scenarioItem;

    @AfterClass
    public static void afterClass() {
        for (ScenarioItem removeScenario : scenarioItemRemoveList) {
            QmsApiTestUtils.deleteScenarioViaCidApp(removeScenario, currentUser);
        }
    }

    @Before
    public void beforeTest() {
        softAssertions = new SoftAssertions();
        scenarioItem = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        scenarioItemRemoveList.add(scenarioItem);
    }

    @After
    public void afterTest() {
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
        HashMap<String, String> projectAttributesMap = new HashMap<>();
        projectAttributesMap.put("projectStatus", "COMPLETED");
        projectAttributesMap.put("projectDueAt", "N/A");
        scenarioItemRemoveList.add(QmsApiTestUtils.createAndVerifyProjectWithStatus(currentUser, softAssertions, projectAttributesMap));
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
    @TestRail(testCaseId = {"17221"})
    @Description("Verify that the User can filter project by status (operator EQ (equal))")
    public void getFilteredProjectsByStatusWithOperatorEQ() {
        HashMap<String, String> projectAttributesMap = new HashMap<>();
        projectAttributesMap.put("projectStatus", "COMPLETED");
        scenarioItemRemoveList.add(QmsApiTestUtils.createAndVerifyProjectWithStatus(currentUser, softAssertions, projectAttributesMap));
        projectAttributesMap = new HashMap<>();
        projectAttributesMap.put("projectStatus", "IN_NEGOTIATION");
        scenarioItemRemoveList.add(QmsApiTestUtils.createAndVerifyProjectWithStatus(currentUser, softAssertions, projectAttributesMap));
        projectAttributesMap = new HashMap<>();
        projectAttributesMap.put("projectStatus", "PURCHASED");
        scenarioItemRemoveList.add(QmsApiTestUtils.createAndVerifyProjectWithStatus(currentUser, softAssertions, projectAttributesMap));
        projectAttributesMap = new HashMap<>();
        projectAttributesMap.put("projectStatus", "SENT_FOR_QUOTATION");
        scenarioItemRemoveList.add(QmsApiTestUtils.createAndVerifyProjectWithStatus(currentUser, softAssertions, projectAttributesMap));
    }

    @Test
    @TestRail(testCaseId = {"17222"})
    @Description("Verify that the User can filter project by status (operator NI (is none of) )")
    public void getFilteredProjectsByStatusWithOperatorNI() {
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, "pageNumber,1", "status[NI],IN_NEGOTIATION");
        QmsApiTestUtils.verifyStatusForFilteredProjects(filteredProjectsResponse, softAssertions, "NONE", "IN_NEGOTIATION");
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, "pageNumber,1", "status[NI],IN_NEGOTIATION|COMPLETED");
        QmsApiTestUtils.verifyMultipleStatusesForFilteredProjects(filteredProjectsResponse, softAssertions, "NONE", "IN_NEGOTIATION", "COMPLETED");
    }

    @Test
    @TestRail(testCaseId = {"17223"})
    @Description("Verify that the User can filter project by status (operator IN)")
    public void getFilteredProjectsByStatusWithOperatorIN() {
        HashMap<String, String> projectAttributesMap = new HashMap<>();
        projectAttributesMap.put("projectStatus", "IN_NEGOTIATION");
        scenarioItemRemoveList.add(QmsApiTestUtils.createAndVerifyProjectWithStatus(currentUser, softAssertions, projectAttributesMap));
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, "pageNumber,1", "status[IN],IN_NEGOTIATION");
        QmsApiTestUtils.verifyStatusForFilteredProjects(filteredProjectsResponse, softAssertions, "ALL", "IN_NEGOTIATION");
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, "pageNumber,1", "status[IN],IN_NEGOTIATION|COMPLETED");
        QmsApiTestUtils.verifyMultipleStatusesForFilteredProjects(filteredProjectsResponse, softAssertions, "ALL", "IN_NEGOTIATION", "COMPLETED");
    }

    @Test
    @TestRail(testCaseId = {"17224"})
    @Description("Verify that the User can filter project by status (operator NE (not equal))")
    public void getFilteredProjectsByStatusWithOperatorNE() {
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, "pageNumber,1", "status[NE],IN_NEGOTIATION");
        QmsApiTestUtils.verifyStatusForFilteredProjects(filteredProjectsResponse, softAssertions, "NONE", "IN_NEGOTIATION");
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, "pageNumber,1", "status[NE],COMPLETED");
        QmsApiTestUtils.verifyStatusForFilteredProjects(filteredProjectsResponse, softAssertions, "NONE", "COMPLETED");
    }

    @Test
    @TestRail(testCaseId = {"22773"})
    @Description("Verify that the User can filter project by owner with operators IN")
    public void getFilteredProjectsByOwnerWithOperatorIN() {
        String ownerIdentity = new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail());
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, "pageNumber,1", "owner[IN]," + ownerIdentity);
        QmsApiTestUtils.verifyOwnerForFilteredProjects(filteredProjectsResponse, softAssertions, "ALL", ownerIdentity);
    }

    @Test
    @TestRail(testCaseId = {"22774"})
    @Description("Verify that the User can filter project by owner with operators NI")
    public void getFilteredProjectsByOwnerWithOperatorNI() {
        String ownerIdentity = new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail());
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, "pageNumber,1", "owner[NI]," + ownerIdentity);
        QmsApiTestUtils.verifyOwnerForFilteredProjects(filteredProjectsResponse, softAssertions, "NONE", ownerIdentity);
    }

    @Test
    @TestRail(testCaseId = {"23771"})
    @Description("Verify that the User can filter projects by Members (operator IN)")
    public void getFilteredProjectsByMembersWithOperatorIN() {
        ScenarioItem scenarioItem = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        scenarioItemRemoveList.add(scenarioItem);
        List<BidPackageItemRequest> itemsList = new ArrayList<>();
        itemsList.add(BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity(scenarioItem.getComponentIdentity())
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build());

        List<BidPackageProjectUserParameters> usersList = new ArrayList<>();
        UserCredentials newUserFirst = UserUtil.getUser();
        String newUserIdentityFirst = new AuthUserContextUtil().getAuthUserIdentity(newUserFirst.getEmail());
        usersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity(newUserIdentityFirst)
            .customerIdentity(PropertiesContext.get("${env}.customer_identity"))
            .build());

        UserCredentials newUserSecond = UserUtil.getUser();
        String newUserIdentitySecond = new AuthUserContextUtil().getAuthUserIdentity(newUserSecond.getEmail());
        usersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity(newUserIdentitySecond)
            .customerIdentity(PropertiesContext.get("${env}.customer_identity"))
            .build());

        QmsProjectResources.createProject(new HashMap<>(),
            itemsList,
            usersList,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        String[] params = {"pageNumber,1", "members[IN]," + newUserIdentityFirst};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getUsers().stream()
                    .anyMatch(u -> u.getUserIdentity().equals(newUserIdentityFirst)))).isTrue();
        }

        params[1] = "members[IN]," + newUserIdentityFirst + "|" + newUserIdentitySecond;
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getUsers().stream()
                    .anyMatch(u -> u.getUserIdentity().equals(newUserIdentityFirst) ||
                        u.getUserIdentity().equals(newUserIdentitySecond)))).isTrue();
        }
    }

    @Test
    @TestRail(testCaseId = {"23772"})
    @Description("Verify that the User can filter projects by Members (operator NI)")
    public void getFilteredProjectsByMembersWithOperatorNI() {
        ScenarioItem scenarioItem = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        scenarioItemRemoveList.add(scenarioItem);
        List<BidPackageItemRequest> itemsList = new ArrayList<>();
        itemsList.add(BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity(scenarioItem.getComponentIdentity())
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build());

        QmsProjectResources.createProject(new HashMap<>(),
            itemsList,
            null,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        String newUserIdentityFirst = new AuthUserContextUtil().getAuthUserIdentity(UserUtil.getUser().getEmail());
        String newUserIdentitySecond = new AuthUserContextUtil().getAuthUserIdentity(UserUtil.getUser().getEmail());

        String[] params = {"pageNumber,1", "members[NI]," + newUserIdentityFirst};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getUsers().stream()
                    .noneMatch(u -> u.getUserIdentity().equals(newUserIdentityFirst)))).isTrue();
        }

        params[1] = "members[NI]," + newUserIdentityFirst + "|" + newUserIdentitySecond;
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getUsers().stream()
                    .noneMatch(u -> u.getUserIdentity().equals(newUserIdentityFirst)))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getUsers().stream()
                    .noneMatch(u -> u.getUserIdentity().equals(newUserIdentitySecond)))).isTrue();
        }
    }

    @Test
    @TestRail(testCaseId = {"23774"})
    @Description("Verify that the User will receive an empty list if he used 'NI='current user identity'' (members NI)")
    public void getFilteredProjectsByMembersCurrentUserWithOperatorNI() {
        String currentUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail());
        String[] params = {"pageNumber,1", "members[NI]," + currentUserIdentity};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isZero();
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
}