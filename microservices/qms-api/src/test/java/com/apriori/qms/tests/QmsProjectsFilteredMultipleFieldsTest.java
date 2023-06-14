package com.apriori.qms.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.qms.controller.QmsProjectResources;
import com.apriori.qms.controller.QmsScenarioDiscussionResources;
import com.apriori.qms.entity.request.bidpackage.BidPackageItemParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageItemRequest;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectUserParameters;
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

public class QmsProjectsFilteredMultipleFieldsTest extends TestUtil {
    private static String displayName;
    private static String status;
    private static String owner;
    private static String dueAtLT;
    private static String dueAtGT;
    private static ScenarioItem scenarioItem;
    private static String newOwner;
    private static String projectMemberUserIdentity;
    private SoftAssertions softAssertions;

    @BeforeClass
    public static void beforeClass() {
        scenarioItem = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
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
        UserCredentials projectMemberUser = UserUtil.getUser();
        projectMemberUserIdentity = new AuthUserContextUtil().getAuthUserIdentity(projectMemberUser.getEmail());
        usersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity(projectMemberUserIdentity)
            .customerIdentity(PropertiesContext.get("${env}.customer_identity"))
            .build());

        displayName = new GenerateStringUtil().getRandomString();
        status = "IN_NEGOTIATION";
        owner = new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail());
        dueAtLT = DateUtil.getDateDaysAfter(12, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);
        dueAtGT = DateUtil.getDateDaysBefore(30, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);
        newOwner = new AuthUserContextUtil().getAuthUserIdentity(UserUtil.getUser().getEmail());
        HashMap<String, String> projectAttributesMap = new HashMap<>();
        projectAttributesMap.put("projectStatus", status);
        projectAttributesMap.put("projectDisplayName", displayName);
        QmsProjectResources.createProject(projectAttributesMap,
            itemsList,
            usersList,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);
    }

    @AfterClass
    public static void afterClass() {
        for (ScenarioItem removeScenario : scenarioItemRemoveList) {
            QmsApiTestUtils.deleteScenarioViaCidApp(removeScenario, currentUser);
        }
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
        String[] params1 = {"pageNumber,1", "status[IN]," + status, "displayName[CN]," + displayName, "owner[IN]," + owner};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params1);
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
        String[] params2 = {"pageNumber,1", "status[IN]," + status, "displayName[CN]," + displayName, "owner[IN]," + owner, "dueAt[LT]," + dueAtLT};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params2);
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
        String[] params5 = {"pageNumber,1", "status[IN]," + status, "owner[IN]," + owner};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params5);
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
        String[] params6 = {"pageNumber,1", "displayName[CN]," + displayName, "owner[IN]," + owner};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params6);
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
        String[] params7 = {"pageNumber,1", "status[NI],COMPLETED", "owner[IN]," + owner};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params7);
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
        String[] params8 = {"pageNumber,1", "status[NI],COMPLETED", "owner[NI]," + newOwner};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params8);
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
        String[] params9 = {"pageNumber,1", "status[NI],COMPLETED", "displayName[CN]," + displayName};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params9);
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
        String[] params10 = {"pageNumber,1", "displayName[CN]," + displayName, "owner[NI]," + newOwner};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params10);
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
        String[] params11 = {"pageNumber,1", "status[IN]," + status, "displayName[CN]," + displayName, "owner[IN]," + owner, "dueAt[GT]," + dueAtGT};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params11);
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
        String[] params13 = {"pageNumber,1", "status[IN]," + status, "members[IN]," + projectMemberUserIdentity};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params13);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getStatus().equals(status))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getUsers().stream()
                    .anyMatch(u -> u.getUserIdentity().equals(projectMemberUserIdentity)))).isTrue();
        }
    }

    @Test
    @TestRail(testCaseId = {"24112"})
    @Description("Search by Display Name[CN] + Members[NI]")
    public void getFilteredProjectsByDisplayNameCNMembersNI() {
        String[] params14 = {"pageNumber,1", "displayName[CN]," + displayName, "members[NI]," + projectMemberUserIdentity};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params14);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getDisplayName().contains(displayName))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getUsers().stream()
                    .noneMatch(u -> u.getUserIdentity().equals(projectMemberUserIdentity)))).isTrue();
        }
    }

    @Test
    @TestRail(testCaseId = {"24076"})
    @Description("Search by Status[IN] + Display Name[CN] + Owner[IN] + DueAt[LT] + AllMessages[unread=no]")
    public void getFilteredProjectsByStatusINDisplayNameCNOwnerINDueAtLTUnReadNo() {
        String[] params3 = {"pageNumber,1", "status[IN]," + status, "displayName[CN]," + displayName, "owner[IN]," + owner, "dueAt[LT]," + dueAtLT, "hasUnreadMessages[EQ],no"};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params3);
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
    @Description("Search by Status[IN] + Display Name[CN] + Owner[IN] + DueAt[LT] + Unread Messages[unread=yes]")
    public void getFilteredProjectsByStatusINDisplayNameCNOwnerINDueAtLTUnReadYes() {
        ScenarioDiscussionResponse scenarioDiscussionResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
        QmsScenarioDiscussionResources.addCommentToDiscussion(scenarioDiscussionResponse.getIdentity(), displayName, "ACTIVE", currentUser);
        String[] params4 = {"pageNumber,1", "status[IN]," + status, "displayName[CN]," + displayName, "owner[IN]," + owner, "dueAt[LT]," + dueAtLT, "hasUnreadMessages[EQ],yes"};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params4);
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
        String[] params12 = {"pageNumber,1", "status[IN]," + status, "owner[IN]," + owner, "hasUnreadMessages[EQ],yes"};
        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, params12);
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getStatus().equals(status))).isTrue();
            softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                .allMatch(i -> i.getOwner().equals(owner))).isTrue();
        }
    }

    private static final UserCredentials currentUser = UserUtil.getUser();
    private static final List<ScenarioItem> scenarioItemRemoveList = new ArrayList<>();
}