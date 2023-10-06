package com.apriori;

import com.apriori.dms.utils.DmsApiTestDataUtils;
import com.apriori.dms.utils.DmsApiTestUtils;
import com.apriori.models.response.ApwErrorMessage;
import com.apriori.qms.controller.QmsScenarioDiscussionResources;
import com.apriori.qms.models.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.qms.models.response.scenariodiscussion.ScenarioDiscussionsResponse;
import com.apriori.rules.TestRulesApi;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesApi.class)
public class DmsDiscussionProjectItemTest extends DmsApiTestDataUtils {
    @Test
    @TestRail(id = {24413, 24414})
    @Description("Verify all discussions associated with the project item gets deleted by discussions delete by projectItemIdentity API")
    public void verifyDeleteAllDiscussionsOnProjectItemDelete() {
        ScenarioDiscussionResponse qmsScenarioDiscussionResponse2 = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
        ScenarioDiscussionResponse qmsScenarioDiscussionResponse3 = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
        DmsApiTestUtils.deleteDiscussionProjectItem(qmsScenarioDiscussionResponse.getProjectItemIdentity(), null, currentUser, HttpStatus.SC_NO_CONTENT);

        //Verify the related discussions are deleted
        ScenarioDiscussionsResponse scenarioDiscussionResponse = DmsApiTestUtils.getScenarioDiscussions(ScenarioDiscussionsResponse.class, HttpStatus.SC_OK, currentUser, qmsScenarioDiscussionResponse);
        softAssertions.assertThat(scenarioDiscussionResponse.getItems().size()).isZero();
        scenarioDiscussionResponse = DmsApiTestUtils.getScenarioDiscussions(ScenarioDiscussionsResponse.class, HttpStatus.SC_OK, currentUser, qmsScenarioDiscussionResponse2);
        softAssertions.assertThat(scenarioDiscussionResponse.getItems().size()).isZero();
        scenarioDiscussionResponse = DmsApiTestUtils.getScenarioDiscussions(ScenarioDiscussionsResponse.class, HttpStatus.SC_OK, currentUser, qmsScenarioDiscussionResponse3);
        softAssertions.assertThat(scenarioDiscussionResponse.getItems().size()).isZero();

        //Delete Project Item again
        ApwErrorMessage discussionPrjItemDeleteErrorResponse = DmsApiTestUtils.deleteDiscussionProjectItem(qmsScenarioDiscussionResponse.getProjectItemIdentity(), ApwErrorMessage.class, currentUser, HttpStatus.SC_NOT_FOUND);
        softAssertions.assertThat(discussionPrjItemDeleteErrorResponse.getMessage()).matches("Resource 'Discussion' with identity .* was not found");
    }
}
