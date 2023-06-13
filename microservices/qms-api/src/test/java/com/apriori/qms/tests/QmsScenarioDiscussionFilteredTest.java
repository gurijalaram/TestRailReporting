package com.apriori.qms.tests;


import com.apriori.qms.controller.QmsScenarioDiscussionResources;
import com.apriori.qms.entity.request.scenariodiscussion.ScenarioDiscussionRequest;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioDiscussionsResponse;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.QmsApiTestDataUtils;
import utils.QmsApiTestUtils;

public class QmsScenarioDiscussionFilteredTest extends QmsApiTestDataUtils {
    @BeforeClass
    public static void beforeClass() {
        createTestData();
    }

    @AfterClass
    public static void afterClass() {
        deleteTestDataAndClearEntities();
    }

    @Test
    @TestRail(testCaseId = {"16057", "14672"})
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
    @TestRail(testCaseId = {"14673"})
    @Description("Create, get scenario discussion with mentioned user profile identity")
    public void getFilteredScenarioDiscussionsByMentionedUserIdentity() {
        String[] params = {"mentionedUsers.userIdentity[EQ]," + scenarioDiscussionResponse.getParticipants().get(0)
            .getUserIdentity(), "pageNumber,1", "status[NE],DELETED"};
        ScenarioDiscussionsResponse responseWrapper = QmsScenarioDiscussionResources.getFilteredScenarioDiscussions(currentUser, params);
        softAssertions.assertThat(responseWrapper.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"14674"})
    @Description("Verify that user can GET a list of all Unresolved discussions")
    public void getFilteredUnResolvedScenarioDiscussions() {
        ScenarioDiscussionsResponse responseWrapper = QmsScenarioDiscussionResources.getFilteredScenarioDiscussions(currentUser, "pageNumber,1", "status[NE],DELETED", "status[IN],ACTIVE");
        softAssertions.assertThat(responseWrapper.getItems().size()).isGreaterThan(0);
    }
}