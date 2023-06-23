package com.apriori.qms.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.qms.controller.QmsProjectResources;
import com.apriori.qms.controller.QmsScenarioDiscussionResources;
import com.apriori.qms.entity.request.bidpackage.BidPackageItemParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageItemRequest;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectNotificationRequest;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectUserParameters;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectNotificationResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectsResponse;
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
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.QmsApiTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QmsProjectsFilteredTest extends TestUtil {
    private static String displayName;
    private static String status;
    private static String owner;
    private static String dueAtLT;
    private static String dueAtGT;
    private static ScenarioItem scenarioItemInNegotiation;
    private static ScenarioItem scenarioItemCompleted;
    private static ScenarioItem scenarioItemPurchased;
    private static ScenarioItem scenarioItemSentQuotation;
    private static String newOwner;
    private static String newUserIdentityFirst;
    private static String newUserIdentitySecond;
    private static final UserCredentials currentUser = UserUtil.getUser();
    private static SoftAssertions softAssertions = new SoftAssertions();

    @BeforeClass
    public static void beforeClass() {
        scenarioItemInNegotiation = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        List<BidPackageItemRequest> itemsList = new ArrayList<>();
        itemsList.add(BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity(scenarioItemInNegotiation.getComponentIdentity())
                .scenarioIdentity(scenarioItemInNegotiation.getScenarioIdentity())
                .iterationIdentity(scenarioItemInNegotiation.getIterationIdentity())
                .build())
            .build());

        List<BidPackageProjectUserParameters> usersList = new ArrayList<>();
        UserCredentials newUserFirst = QmsApiTestUtils.getNextUser(currentUser);
        newUserIdentityFirst = new AuthUserContextUtil().getAuthUserIdentity(newUserFirst.getEmail());
        usersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity(newUserIdentityFirst)
            .customerIdentity(PropertiesContext.get("${env}.customer_identity"))
            .build());

        UserCredentials newUserSecond = QmsApiTestUtils.getNextUser(currentUser);
        newUserIdentitySecond = new AuthUserContextUtil().getAuthUserIdentity(newUserSecond.getEmail());
        usersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity(newUserIdentitySecond)
            .customerIdentity(PropertiesContext.get("${env}.customer_identity"))
            .build());

        displayName = new GenerateStringUtil().getRandomString();
        status = "IN_NEGOTIATION";
        owner = new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail());
        dueAtLT = DateUtil.getDateDaysAfter(12, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);
        dueAtGT = DateUtil.getDateDaysBefore(30, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);
        newOwner = new AuthUserContextUtil().getAuthUserIdentity(QmsApiTestUtils.getNextUser(currentUser).getEmail());
        HashMap<String, String> projectAttributesMap = new HashMap<>();
        projectAttributesMap.put("projectStatus", status);
        projectAttributesMap.put("projectDisplayName", displayName);
        QmsProjectResources.createProject(projectAttributesMap,
            itemsList,
            usersList,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        projectAttributesMap = new HashMap<>();
        projectAttributesMap.put("projectStatus", "COMPLETED");
        projectAttributesMap.put("projectDueAt", "N/A");
        scenarioItemCompleted = QmsApiTestUtils.createAndVerifyProjectWithStatus(currentUser, softAssertions, projectAttributesMap);
        projectAttributesMap = new HashMap<>();
        projectAttributesMap.put("projectStatus", "PURCHASED");
        scenarioItemPurchased = QmsApiTestUtils.createAndVerifyProjectWithStatus(currentUser, softAssertions, projectAttributesMap);
        projectAttributesMap = new HashMap<>();
        projectAttributesMap.put("projectStatus", "SENT_FOR_QUOTATION");
        scenarioItemSentQuotation = QmsApiTestUtils.createAndVerifyProjectWithStatus(currentUser, softAssertions, projectAttributesMap);
    }

    @AfterClass
    public static void afterClass() {
        QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItemInNegotiation, currentUser);
        QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItemCompleted, currentUser);
        QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItemPurchased, currentUser);
        QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItemSentQuotation, currentUser);
        softAssertions.assertAll();
    }

    @Before
    public void beforeTest() {
        softAssertions = new SoftAssertions();
    }

    @After
    public void afterTest() {
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"C24073"})
    @Description("Search by Status[IN] + Display Name[CN]")
    public void getFilteredProjectsByStatusINDisplayNameCN() {
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
    }

    @Test
    @TestRail(testCaseId = {"24074"})
    @Description("Search by Status[IN] + Display Name[CN] + Owner[IN]")
    public void getFilteredProjectsByStatusINDisplayNameCNOwnerIN() {
        String[] params = {"pageNumber,1", "status[IN]," + status, "displayName[CN]," + displayName, "owner[IN]," + owner};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
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
    }

    @Test
    @TestRail(testCaseId = {"24075"})
    @Description("Search by Status[IN] + Display Name[CN] + Owner[IN] + DueAt[LT]")
    public void getFilteredProjectsByStatusINDisplayNameCNOwnerINDueAtLT() {
        String[] params = {"pageNumber,1", "status[IN]," + status, "displayName[CN]," + displayName, "owner[IN]," + owner, "dueAt[LT]," + dueAtLT};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
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
    }

    @Test
    @TestRail(testCaseId = {"24078"})
    @Description("Search by Status[IN] + Owner[IN]")
    public void getFilteredProjectsByStatusINOwnerIN() {
        String[] params = {"pageNumber,1", "status[IN]," + status, "owner[IN]," + owner};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getStatus().equals(status))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getOwner().equals(owner))).isTrue();
        }
    }

    @Test
    @TestRail(testCaseId = {"24079"})
    @Description("Search by Display Name[CN] + Owner[IN]")
    public void getFilteredProjectsByDisplayNameCNOwnerIN() {
        String[] params = {"pageNumber,1", "displayName[CN]," + displayName, "owner[IN]," + owner};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getDisplayName().contains(displayName))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getOwner().equals(owner))).isTrue();
        }
    }

    @Test
    @TestRail(testCaseId = {"24080"})
    @Description("Search by Status[NI] + Owner[IN]")
    public void getFilteredProjectsByStatusNIOwnerIN() {
        String[] params = {"pageNumber,1", "status[NI],COMPLETED", "owner[IN]," + owner};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .noneMatch(i -> i.getStatus().equals("COMPLETED"))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getOwner().equals(owner))).isTrue();
        }
    }

    @Test
    @TestRail(testCaseId = {"24081"})
    @Description("Search by Status[NI] + Owner[NI]")
    public void getFilteredProjectsByStatusNIOwnerNI() {
        String[] params = {"pageNumber,1", "status[NI],COMPLETED", "owner[NI]," + newOwner};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .noneMatch(i -> i.getStatus().equals("COMPLETED"))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .noneMatch(i -> i.getOwner().equals(newOwner))).isTrue();
        }
    }

    @Test
    @TestRail(testCaseId = {"24082"})
    @Description("Search by Status[NI] + Display Name[CN]")
    public void getFilteredProjectsByStatusNIDisplayNameCN() {
        String[] params = {"pageNumber,1", "status[NI],COMPLETED", "displayName[CN]," + displayName};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .noneMatch(i -> i.getStatus().equals("COMPLETED"))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getDisplayName().contains(displayName))).isTrue();
        }
    }

    @Test
    @TestRail(testCaseId = {"24083"})
    @Description("Search by Display Name[CN] + Owner[NI]")
    public void getFilteredProjectsByDisplayNameCNOwnerNI() {
        String[] params = {"pageNumber,1", "displayName[CN]," + displayName, "owner[NI]," + newOwner};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getDisplayName().contains(displayName))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .noneMatch(i -> i.getOwner().equals(newOwner))).isTrue();
        }
    }

    @Test
    @TestRail(testCaseId = {"24104"})
    @Description("Search by Status[IN] + Display Name[CN] + Owner[IN] + DueAt[GT]")
    public void getFilteredProjectsByStatusINDisplayNameCNOwnerINDueAtGT() {
        String[] params = {"pageNumber,1", "status[IN]," + status, "displayName[CN]," + displayName, "owner[IN]," + owner, "dueAt[GT]," + dueAtGT};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
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
    }

    @Test
    @TestRail(testCaseId = {"24111"})
    @Description("VSearch by Status[IN] + Members[IN]")
    public void getFilteredProjectsByStatusINMembersIN() {
        String[] params = {"pageNumber,1", "status[IN]," + status, "members[IN]," + newUserIdentityFirst};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getStatus().equals(status))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getUsers().stream()
                    .anyMatch(u -> u.getUserIdentity().equals(newUserIdentityFirst)))).isTrue();
        }
    }

    @Test
    @TestRail(testCaseId = {"24112"})
    @Description("Search by Display Name[CN] + Members[NI]")
    public void getFilteredProjectsByDisplayNameCNMembersNI() {
        String projectNonMemberUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(QmsApiTestUtils.getNextUser(currentUser).getEmail());
        String[] params = {"pageNumber,1", "displayName[CN]," + displayName, "members[NI]," + projectNonMemberUserIdentity};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getDisplayName().contains(displayName))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getUsers().stream()
                    .noneMatch(u -> u.getUserIdentity().equals(projectNonMemberUserIdentity)))).isTrue();
        }
    }

    @Test
    @TestRail(testCaseId = {"24076"})
    @Description("Search by Status[IN] + Display Name[CN] + Owner[IN] + DueAt[LT] + AllMessages[unread=no]")
    public void getFilteredProjectsByStatusINDisplayNameCNOwnerINDueAtLTUnReadNo() {
        String[] params = {"pageNumber,1", "status[IN]," + status, "displayName[CN]," + displayName, "owner[IN]," + owner, "dueAt[LT]," + dueAtLT, "hasUnreadMessages[EQ],no"};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
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
    }

    @Test
    @TestRail(testCaseId = {"24077"})
    @Issue("COL-1831")
    @Description("Search by Status[IN] + Display Name[CN] + Owner[IN] + DueAt[LT] + Unread Messages[unread=yes]")
    public void getFilteredProjectsByStatusINDisplayNameCNOwnerINDueAtLTUnReadYes() {
        ScenarioDiscussionResponse scenarioDiscussionResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItemInNegotiation.getComponentIdentity(), scenarioItemInNegotiation.getScenarioIdentity(), currentUser);
        QmsScenarioDiscussionResources.addCommentToDiscussion(scenarioDiscussionResponse.getIdentity(), displayName, "ACTIVE", currentUser);
        String[] params = {"pageNumber,1", "status[IN]," + status, "displayName[CN]," + displayName, "owner[IN]," + owner, "dueAt[LT]," + dueAtLT, "hasUnreadMessages[EQ],yes"};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
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
    }

    @Test
    @TestRail(testCaseId = {"24105"})
    @Issue("COL-1831")
    @Description("Search by Status[IN] + Owner[IN]+Unread Messages[unread=yes]")
    public void getFilteredProjectsByStatusINOwnerINUnReadYes() {
        String[] params = {"pageNumber,1", "status[IN]," + status, "owner[IN]," + owner, "hasUnreadMessages[EQ],yes"};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getStatus().equals(status))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getOwner().equals(owner))).isTrue();
        }
    }

    //New Added
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
    @TestRail(testCaseId = {"17221"})
    @Description("Verify that the User can filter project by status (operator EQ (equal))")
    public void getFilteredProjectsByStatusWithOperatorEQ() {
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, "pageNumber,1", "status[EQ],COMPLETED");
        QmsApiTestUtils.verifyStatusForFilteredProjects(filteredProjectsResponse, softAssertions, "ALL", "COMPLETED");

        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, "pageNumber,1", "status[EQ],IN_NEGOTIATION");
        QmsApiTestUtils.verifyStatusForFilteredProjects(filteredProjectsResponse, softAssertions, "ALL", "IN_NEGOTIATION");

        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, "pageNumber,1", "status[EQ],PURCHASED");
        QmsApiTestUtils.verifyStatusForFilteredProjects(filteredProjectsResponse, softAssertions, "ALL", "PURCHASED");

        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, "pageNumber,1", "status[EQ],SENT_FOR_QUOTATION");
        QmsApiTestUtils.verifyStatusForFilteredProjects(filteredProjectsResponse, softAssertions, "ALL", "SENT_FOR_QUOTATION");
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
        String[] params = {"pageNumber,1", "members[IN]," + newUserIdentityFirst};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getUsers().stream()
                    .anyMatch(u -> u.getIdentity().equals(newUserIdentityFirst)))).isTrue();
        }

        params[1] = "members[IN]," + newUserIdentityFirst + "|" + newUserIdentitySecond;
        filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getUsers().stream()
                    .anyMatch(u -> u.getIdentity().equals(newUserIdentityFirst)))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getUsers().stream()
                    .anyMatch(u -> u.getIdentity().equals(newUserIdentitySecond)))).isTrue();
        }
    }

    @Test
    @TestRail(testCaseId = {"23772"})
    @Description("Verify that the User can filter projects by Members (operator NI)")
    public void getFilteredProjectsByMembersWithOperatorNI() {
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