package com.apriori;

import com.apriori.enums.ProcessGroupEnum;
import com.apriori.models.response.ScenarioItem;
import com.apriori.qms.controller.QmsScenarioDiscussionResources;
import com.apriori.qms.models.request.scenariodiscussion.ScenarioDiscussionRequest;
import com.apriori.qms.models.response.bidpackage.BidPackageResponse;
import com.apriori.qms.models.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.qms.models.response.scenariodiscussion.ScenarioDiscussionsResponse;
import com.apriori.qms.utils.QmsApiTestUtils;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QmsScenarioDiscussionFilteredTest extends TestUtil {
    private static SoftAssertions softAssertions = new SoftAssertions();
    private static BidPackageResponse bidPackageResponse;
    private static ScenarioDiscussionResponse scenarioDiscussionResponse;
    private static ScenarioItem scenarioItem;
    private static final UserCredentials currentUser = UserUtil.getUser();

    @BeforeAll
    public static void beforeClass() {
        scenarioItem = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        bidPackageResponse = QmsApiTestUtils.createTestDataBidPackage(currentUser, softAssertions);
        QmsApiTestUtils.createTestDataBidPackageItem(scenarioItem, bidPackageResponse, currentUser, softAssertions);
        QmsApiTestUtils.createTestDataBidPackageProject(bidPackageResponse, currentUser, softAssertions);
        scenarioDiscussionResponse = QmsApiTestUtils.createTestDataScenarioDiscussion(scenarioItem, currentUser, softAssertions);
        QmsApiTestUtils.createTestDataAddCommentToDiscussion(scenarioDiscussionResponse, currentUser, softAssertions);
    }

    @AfterAll
    public static void afterClass() {
        QmsApiTestUtils.deleteTestData(scenarioItem, bidPackageResponse, currentUser);
        softAssertions.assertAll();
    }

    @BeforeEach
    public void beforeTest() {
        softAssertions = new SoftAssertions();
    }

    @AfterEach
    public void afterTest() {
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {16057, 14672})
    @Description("Create, get scenario discussion with assignee user and useridentity")
    public void getFilteredScenarioDiscussionsByUserIdentity() {
        UserCredentials assignedUser = UserUtil.getUser();
        String description = new GenerateStringUtil().generateNotes();
        ScenarioDiscussionRequest scenarioDiscussionRequest = QmsApiTestUtils.getScenarioDiscussionRequest(assignedUser, scenarioItem, description);
        ScenarioDiscussionResponse scenarioDiscussionAssigneeResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioDiscussionRequest, currentUser);
        softAssertions.assertThat(scenarioDiscussionAssigneeResponse.getDescription()).isEqualTo(description);

        QmsScenarioDiscussionResources.addCommentToDiscussion(scenarioDiscussionAssigneeResponse.getIdentity(), new GenerateStringUtil().generateNotes(), "ACTIVE", currentUser);
        String[] params = {"pageNumber,1", "status[NE],DELETED", "assignee.userIdentity[IN]," + scenarioDiscussionAssigneeResponse.getAssigneeUserIdentity(), "sortBy[DESC],createdAt"};
        ScenarioDiscussionsResponse responseWrapper = QmsScenarioDiscussionResources.getFilteredScenarioDiscussions(currentUser, params);
        softAssertions.assertThat(responseWrapper.getItems().size()).isGreaterThan(0);

        QmsScenarioDiscussionResources.deleteScenarioDiscussion(scenarioDiscussionAssigneeResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(id = {14673})
    @Description("Create, get scenario discussion with mentioned user profile identity")
    public void getFilteredScenarioDiscussionsByMentionedUserIdentity() {
        String[] params = {"mentionedUsers.userIdentity[EQ]," + scenarioDiscussionResponse.getParticipants().get(0)
            .getUserIdentity(), "pageNumber,1", "status[NE],DELETED"};
        ScenarioDiscussionsResponse responseWrapper = QmsScenarioDiscussionResources.getFilteredScenarioDiscussions(currentUser, params);
        softAssertions.assertThat(responseWrapper.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(id = {14674})
    @Description("Verify that user can GET a list of all Unresolved discussions")
    public void getFilteredUnResolvedScenarioDiscussions() {
        ScenarioDiscussionsResponse responseWrapper = QmsScenarioDiscussionResources.getFilteredScenarioDiscussions(currentUser, "pageNumber,1", "status[NE],DELETED", "status[IN],ACTIVE");
        softAssertions.assertThat(responseWrapper.getItems().size()).isGreaterThan(0);
    }
}