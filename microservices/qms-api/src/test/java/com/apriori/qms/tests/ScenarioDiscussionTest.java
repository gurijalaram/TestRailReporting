package com.apriori.qms.tests;


import com.apriori.apibase.utils.TestUtil;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.qms.controller.QmsScenarioDiscussionResources;
import com.apriori.qms.entity.request.scenariodiscussion.DiscussionCommentParameters;
import com.apriori.qms.entity.request.scenariodiscussion.DiscussionCommentRequest;
import com.apriori.qms.entity.request.scenariodiscussion.ScenarioDiscussionRequest;
import com.apriori.qms.entity.response.scenariodiscussion.DiscussionCommentResponse;
import com.apriori.qms.entity.response.scenariodiscussion.DiscussionCommentsResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioDiscussionsResponse;
import com.apriori.qms.enums.QMSAPIEnum;
import com.apriori.utils.CssComponent;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.QmsApiTestUtils;

public class ScenarioDiscussionTest extends TestUtil {

    private static ScenarioItem scenarioItem;
    private static SoftAssertions softAssertions;
    private static ResponseWrapper<ScenarioDiscussionResponse> scenarioDiscussionResponse;
    private static ResponseWrapper<DiscussionCommentResponse> discussionCommentResponse;
    private UserCredentials currentUser = UserUtil.getUser();

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        scenarioItem = new CssComponent().getBaseCssComponents(currentUser).get(0);
        softAssertions.assertThat(scenarioItem.getComponentIdentity()).isNotNull();
        scenarioDiscussionResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
        discussionCommentResponse = QmsScenarioDiscussionResources.addCommentToDiscussion(scenarioDiscussionResponse.getResponseEntity().getIdentity(),
            new GenerateStringUtil().generateNotes(), "ACTIVE", currentUser);
    }

    @Test
    @TestRail(testCaseId = {"14608", "14613"})
    @Description("Create and delete Scenario Discussion")
    public void createAndDeleteScenarioDiscussion() {
        ResponseWrapper<ScenarioDiscussionResponse> csdResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
        softAssertions.assertThat(csdResponse.getResponseEntity().getIdentity()).isNotNull();
        QmsScenarioDiscussionResources.deleteScenarioDiscussion(csdResponse.getResponseEntity().getIdentity(), currentUser);
    }

    @Test
    @TestRail(testCaseId = {"14610"})
    @Description("Get Scenario Discussion by identity")
    public void getScenarioDiscussion() {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSION, ScenarioDiscussionResponse.class)
            .inlineVariables(scenarioDiscussionResponse.getResponseEntity().getIdentity())
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ScenarioDiscussionResponse> responseWrapper = HTTPRequest.build(requestEntity).get();

        softAssertions.assertThat(responseWrapper.getResponseEntity().getParticipants().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"14609"})
    @Description("Get list of all Scenario Discussions")
    public void getScenarioDiscussions() {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSIONS, ScenarioDiscussionsResponse.class)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ScenarioDiscussionsResponse> responseWrapper = HTTPRequest.build(requestEntity).get();

        softAssertions.assertThat(responseWrapper.getResponseEntity().getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"14611"})
    @Description("Verify that User can update Scenario discussion description")
    public void updateScenarioDiscussionDescription() {
        String description = new GenerateStringUtil().generateNotes();
        ScenarioDiscussionRequest scenarioDiscussionRequest = QmsScenarioDiscussionResources.getScenarioDiscussionRequestBuilder(scenarioItem.getComponentIdentity(), scenarioDiscussionResponse.getResponseEntity().getIdentity());
        scenarioDiscussionRequest.getScenarioDiscussion().setDescription(description);

        ResponseWrapper<ScenarioDiscussionResponse> updateResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(scenarioDiscussionResponse.getResponseEntity().getIdentity(),
            scenarioDiscussionRequest,
            ScenarioDiscussionResponse.class,
            HttpStatus.SC_OK, currentUser);

        softAssertions.assertThat(updateResponse.getResponseEntity().getDescription()).isEqualTo(description);
    }

    @Test
    @TestRail(testCaseId = {"14675", "14678"})
    @Description("Verify that User can add comment and update status to delete to scenario discussion")
    public void addAndDeleteCommentToDiscussion() {
        String commentContent = new GenerateStringUtil().generateNotes();
        DiscussionCommentRequest discussionCommentRequest = DiscussionCommentRequest.builder()
            .comment(DiscussionCommentParameters.builder()
                .content(commentContent)
                .status("ACTIVE")
                .build())
            .build();

        ResponseWrapper<DiscussionCommentResponse> createResponseWrapper = QmsScenarioDiscussionResources.addCommentToDiscussion(scenarioDiscussionResponse.getResponseEntity().getIdentity(),
            discussionCommentRequest, currentUser);

        softAssertions.assertThat(createResponseWrapper.getResponseEntity().getContent()).isEqualTo(commentContent);

        DiscussionCommentRequest discussionDeleteCommentRequest = DiscussionCommentRequest.builder()
            .comment(DiscussionCommentParameters.builder()
                .content(commentContent)
                .status("DELETED")
                .build())
            .build();

        ResponseWrapper<DiscussionCommentResponse> deleteResponseWrapper = QmsScenarioDiscussionResources.updateCommentToDiscussion(scenarioDiscussionResponse.getResponseEntity().getIdentity(),
            createResponseWrapper.getResponseEntity().getIdentity(), discussionDeleteCommentRequest, DiscussionCommentResponse.class, HttpStatus.SC_OK, currentUser);

        softAssertions.assertThat(deleteResponseWrapper.getResponseEntity().getStatus()).isEqualTo("DELETED");
    }

    @Test
    @TestRail(testCaseId = {"15473"})
    @Description("Verify that user can GET discussion's comment by identity")
    public void getDiscussionComment() {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSION_COMMENT, DiscussionCommentResponse.class)
            .inlineVariables(scenarioDiscussionResponse.getResponseEntity().getIdentity(), discussionCommentResponse.getResponseEntity().getIdentity())
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<DiscussionCommentResponse> responseWrapper = HTTPRequest.build(requestEntity).get();

        softAssertions.assertThat(responseWrapper.getResponseEntity().getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    @TestRail(testCaseId = {"14677"})
    @Description("Verify that user can FIND list of discussion's comment")
    public void getDiscussionComments() {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSION_COMMENTS, DiscussionCommentsResponse.class)
            .inlineVariables(scenarioDiscussionResponse.getResponseEntity().getIdentity())
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<DiscussionCommentsResponse> responseWrapper = HTTPRequest.build(requestEntity).get();

        softAssertions.assertThat(responseWrapper.getResponseEntity().getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"14676"})
    @Description("Verify that User can add comment with mentioning user to scenario discussion")
    public void addCommentWithUserToDiscussion() {
        String commentContent = new GenerateStringUtil().generateNotes();
        ResponseWrapper<DiscussionCommentResponse> responseWrapper = QmsScenarioDiscussionResources.addCommentToDiscussion(scenarioDiscussionResponse.getResponseEntity().getIdentity(),
            commentContent, "ACTIVE", currentUser);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getContent()).isEqualTo(commentContent);
    }

    @After
    public void testCleanup() {
        QmsScenarioDiscussionResources.deleteScenarioDiscussion(scenarioDiscussionResponse.getResponseEntity().getIdentity(), currentUser);
        softAssertions.assertAll();
    }
}