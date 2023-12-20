package com.apriori.cis.api.tests;

import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.cis.api.controller.CisCommentResources;
import com.apriori.cis.api.controller.CisDiscussionResources;
import com.apriori.cis.api.models.request.discussion.InternalCommentRequest;
import com.apriori.cis.api.models.response.bidpackage.CisErrorMessage;
import com.apriori.cis.api.models.response.scenariodiscussion.DiscussionCommentResponse;
import com.apriori.cis.api.models.response.scenariodiscussion.DiscussionCommentsResponse;
import com.apriori.cis.api.models.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.cis.api.util.CISTestUtil;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import jdk.jfr.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class CisCommentsTest extends CISTestUtil {

    private static ComponentInfoBuilder componentInfoBuilder;
    private SoftAssertions softAssertions;
    private ScenarioDiscussionResponse scenarioDiscussionResponse;
    private List<String> emailAddressList;
    private String commentContent;

    @BeforeAll
    public static void beforeClass() {
        componentInfoBuilder = new ScenariosUtil().postAndPublishComponent(new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING));
    }

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        emailAddressList = new ArrayList<>();
        emailAddressList.add(componentInfoBuilder.getUser().getEmail());
        commentContent = new GenerateStringUtil().getRandomString();
        scenarioDiscussionResponse = CisDiscussionResources.createInternalDiscussion(CisDiscussionResources.getDiscussionRequestBuilder(),
            componentInfoBuilder.getComponentIdentity(),
            componentInfoBuilder.getScenarioIdentity(),
            ScenarioDiscussionResponse.class,
            HttpStatus.SC_CREATED, componentInfoBuilder.getUser());
    }

    @Test
    @TestRail(id = {13291, 13292, 13295})
    @Description("Create Internal comment, Get Internal comment and Delete internal comment")
    public void createGetAndDeleteInternalComment() {
        DiscussionCommentResponse discussionCommentResponse = CisCommentResources.createInternalComment(CisCommentResources.getCommentRequestBuilder(commentContent, emailAddressList),
            scenarioDiscussionResponse.getComponentIdentity(),
            scenarioDiscussionResponse.getScenarioIdentity(),
            scenarioDiscussionResponse.getIdentity(),
            DiscussionCommentResponse.class,
            HttpStatus.SC_CREATED,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(discussionCommentResponse.getContent()).isEqualTo(commentContent);
        softAssertions.assertThat(discussionCommentResponse.getIdentity()).isNotEmpty();

        DiscussionCommentResponse getDiscussionCommentResponse = CisCommentResources.getInternalComment(scenarioDiscussionResponse.getComponentIdentity(),
            scenarioDiscussionResponse.getScenarioIdentity(),
            scenarioDiscussionResponse.getIdentity(),
            discussionCommentResponse.getIdentity(),
            DiscussionCommentResponse.class,
            HttpStatus.SC_OK,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(getDiscussionCommentResponse.getDiscussionIdentity()).isEqualTo(scenarioDiscussionResponse.getIdentity());
        softAssertions.assertThat(getDiscussionCommentResponse.getContent()).isEqualTo(commentContent);

        CisCommentResources.deleteInternalComment(scenarioDiscussionResponse.getComponentIdentity(),
            scenarioDiscussionResponse.getScenarioIdentity(),
            scenarioDiscussionResponse.getIdentity(),
            getDiscussionCommentResponse.getIdentity(),
            null,
            HttpStatus.SC_NO_CONTENT,
            componentInfoBuilder.getUser());
    }

    @Test
    @TestRail(id = {13293, 13294})
    @Description("Create Internal comment, Update comment, Get All comments and Delete discussion")
    public void createUpdateAndDeleteInternalComment() {
        InternalCommentRequest internalCommentRequest = CisCommentResources.getCommentRequestBuilder(commentContent, emailAddressList);
        String updateContent = new GenerateStringUtil().getRandomString();

        DiscussionCommentResponse discussionCommentResponse = CisCommentResources.createInternalComment(internalCommentRequest,
            scenarioDiscussionResponse.getComponentIdentity(),
            scenarioDiscussionResponse.getScenarioIdentity(),
            scenarioDiscussionResponse.getIdentity(),
            DiscussionCommentResponse.class,
            HttpStatus.SC_CREATED,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(discussionCommentResponse.getContent()).isEqualTo(commentContent);
        softAssertions.assertThat(discussionCommentResponse.getIdentity()).isNotEmpty();

        internalCommentRequest.getComment().setContent(updateContent);

        DiscussionCommentResponse updatedCommentResponse = CisCommentResources.updateInternalComment(internalCommentRequest,
            scenarioDiscussionResponse.getComponentIdentity(),
            scenarioDiscussionResponse.getScenarioIdentity(),
            scenarioDiscussionResponse.getIdentity(),
            discussionCommentResponse.getIdentity(),
            DiscussionCommentResponse.class,
            HttpStatus.SC_OK,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(updatedCommentResponse.getContent()).isEqualTo(updateContent);

        DiscussionCommentsResponse getCommentsResponse = CisCommentResources.getInternalComments(scenarioDiscussionResponse.getComponentIdentity(),
            scenarioDiscussionResponse.getScenarioIdentity(),
            scenarioDiscussionResponse.getIdentity(),
            DiscussionCommentsResponse.class,
            HttpStatus.SC_OK,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(getCommentsResponse.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(id = {14152})
    @Description("Create internal comment with invalid component identity, scenario identity and comment identity")
    public void createInternalCommentWithInvalid() {
        InternalCommentRequest internalCommentRequest = CisCommentResources.getCommentRequestBuilder(commentContent, emailAddressList);
        CisErrorMessage cisInvalidCidErrorResponse = CisCommentResources.createInternalComment(internalCommentRequest,
            "INVALID",
            scenarioDiscussionResponse.getScenarioIdentity(),
            scenarioDiscussionResponse.getIdentity(),
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST, componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidCidErrorResponse.getMessage()).isEqualTo("'componentIdentity' is not a valid identity.");

        CisErrorMessage cisInvalidSidErrorResponse = CisCommentResources.createInternalComment(internalCommentRequest,
            scenarioDiscussionResponse.getComponentIdentity(),
            "INVALID",
            scenarioDiscussionResponse.getIdentity(),
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST, componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidSidErrorResponse.getMessage()).isEqualTo("'scenarioIdentity' is not a valid identity.");

        internalCommentRequest.getComment().setStatus("INVALID");
        CisErrorMessage cisInvalidStatusErrorResponse = CisCommentResources.createInternalComment(internalCommentRequest,
            scenarioDiscussionResponse.getComponentIdentity(),
            scenarioDiscussionResponse.getScenarioIdentity(),
            scenarioDiscussionResponse.getIdentity(),
            CisErrorMessage.class,
            HttpStatus.SC_INTERNAL_SERVER_ERROR, componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidStatusErrorResponse.getMessage()).contains("INVALID");
    }

    @Test
    @TestRail(id = {14365})
    @Description("get internal comment with invalid component identity, scenario identity, discussion identity and comment identity")
    public void getInternalCommentWithInvalid() {
        DiscussionCommentResponse discussionCommentResponse = CisCommentResources.createInternalComment(CisCommentResources.getCommentRequestBuilder(commentContent, emailAddressList),
            scenarioDiscussionResponse.getComponentIdentity(),
            scenarioDiscussionResponse.getScenarioIdentity(),
            scenarioDiscussionResponse.getIdentity(),
            DiscussionCommentResponse.class,
            HttpStatus.SC_CREATED,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(discussionCommentResponse.getContent()).isEqualTo(commentContent);
        softAssertions.assertThat(discussionCommentResponse.getIdentity()).isNotEmpty();

        CisErrorMessage cisInvalidCidErrorResponse = CisCommentResources.getInternalComment("INVALID",
            scenarioDiscussionResponse.getScenarioIdentity(),
            discussionCommentResponse.getDiscussionIdentity(),
            discussionCommentResponse.getIdentity(),
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidCidErrorResponse.getMessage()).isEqualTo("'componentIdentity' is not a valid identity.");

        CisErrorMessage cisInvalidSidErrorResponse = CisCommentResources.getInternalComment(scenarioDiscussionResponse.getComponentIdentity(),
            "INVALID",
            discussionCommentResponse.getDiscussionIdentity(),
            discussionCommentResponse.getIdentity(),
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidSidErrorResponse.getMessage()).isEqualTo("'scenarioIdentity' is not a valid identity.");

        CisErrorMessage cisInvalidTypeErrorResponse = CisCommentResources.getInternalComment(scenarioDiscussionResponse.getComponentIdentity(),
            scenarioDiscussionResponse.getScenarioIdentity(),
            discussionCommentResponse.getDiscussionIdentity(),
            "INVALID",
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidTypeErrorResponse.getMessage()).isEqualTo("'identity' is not a valid identity.");
    }

    @Test
    @TestRail(id = {14366})
    @Description("delete internal comment with invalid component identity, scenario identity and discussion identity")
    public void deleteInternalCommentWithInvalid() {
        DiscussionCommentResponse discussionCommentResponse = CisCommentResources.createInternalComment(CisCommentResources.getCommentRequestBuilder(commentContent, emailAddressList),
            scenarioDiscussionResponse.getComponentIdentity(),
            scenarioDiscussionResponse.getScenarioIdentity(),
            scenarioDiscussionResponse.getIdentity(),
            DiscussionCommentResponse.class,
            HttpStatus.SC_CREATED,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(discussionCommentResponse.getContent()).isEqualTo(commentContent);
        softAssertions.assertThat(discussionCommentResponse.getIdentity()).isNotEmpty();

        CisErrorMessage cisInvalidCidErrorResponse = CisCommentResources.deleteInternalComment("INVALID",
            scenarioDiscussionResponse.getScenarioIdentity(),
            discussionCommentResponse.getDiscussionIdentity(),
            discussionCommentResponse.getIdentity(),
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidCidErrorResponse.getMessage()).isEqualTo("'componentIdentity' is not a valid identity.");

        CisErrorMessage cisInvalidSidErrorResponse = CisCommentResources.deleteInternalComment(scenarioDiscussionResponse.getComponentIdentity(),
            "INVALID",
            discussionCommentResponse.getDiscussionIdentity(),
            discussionCommentResponse.getIdentity(),
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidSidErrorResponse.getMessage()).isEqualTo("'scenarioIdentity' is not a valid identity.");

        CisErrorMessage cisInvalidTypeErrorResponse = CisCommentResources.deleteInternalComment(scenarioDiscussionResponse.getComponentIdentity(),
            scenarioDiscussionResponse.getScenarioIdentity(),
            discussionCommentResponse.getDiscussionIdentity(),
            "INVALID",
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidTypeErrorResponse.getMessage()).isEqualTo("'identity' is not a valid identity.");
    }

    @Test
    @TestRail(id = {14367})
    @Description("update internal comment with invalid component identity, scenario identity and discussion identity")
    public void updateInternalCommentWithInvalid() {
        InternalCommentRequest internalCommentRequest = CisCommentResources.getCommentRequestBuilder(commentContent, emailAddressList);

        DiscussionCommentResponse discussionCommentResponse = CisCommentResources.createInternalComment(internalCommentRequest,
            scenarioDiscussionResponse.getComponentIdentity(),
            scenarioDiscussionResponse.getScenarioIdentity(),
            scenarioDiscussionResponse.getIdentity(),
            DiscussionCommentResponse.class,
            HttpStatus.SC_CREATED,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(discussionCommentResponse.getContent()).isEqualTo(commentContent);
        softAssertions.assertThat(discussionCommentResponse.getIdentity()).isNotEmpty();

        CisErrorMessage cisInvalidCidErrorResponse = CisCommentResources.updateInternalComment(internalCommentRequest,
            "INVALID",
            scenarioDiscussionResponse.getScenarioIdentity(),
            discussionCommentResponse.getDiscussionIdentity(),
            discussionCommentResponse.getIdentity(),
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidCidErrorResponse.getMessage()).isEqualTo("'componentIdentity' is not a valid identity.");

        CisErrorMessage cisInvalidSidErrorResponse = CisCommentResources.updateInternalComment(internalCommentRequest,
            scenarioDiscussionResponse.getComponentIdentity(),
            "INVALID",
            discussionCommentResponse.getDiscussionIdentity(),
            discussionCommentResponse.getIdentity(),
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidSidErrorResponse.getMessage()).isEqualTo("'scenarioIdentity' is not a valid identity.");

        CisErrorMessage cisInvalidDidErrorResponse = CisCommentResources.updateInternalComment(internalCommentRequest,
            scenarioDiscussionResponse.getComponentIdentity(),
            scenarioDiscussionResponse.getScenarioIdentity(),
            discussionCommentResponse.getDiscussionIdentity(),
            "INVALID",
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidDidErrorResponse.getMessage()).isEqualTo("'identity' is not a valid identity.");
    }

    @Test
    @TestRail(id = {14368})
    @Description("get internal comments with invalid component identity and scenario identity")
    public void getInternalCommentsWithInvalid() {
        DiscussionCommentResponse discussionCommentResponse = CisCommentResources.createInternalComment(CisCommentResources.getCommentRequestBuilder(commentContent, emailAddressList),
            scenarioDiscussionResponse.getComponentIdentity(),
            scenarioDiscussionResponse.getScenarioIdentity(),
            scenarioDiscussionResponse.getIdentity(),
            DiscussionCommentResponse.class,
            HttpStatus.SC_CREATED,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(discussionCommentResponse.getContent()).isEqualTo(commentContent);
        softAssertions.assertThat(discussionCommentResponse.getIdentity()).isNotEmpty();

        CisErrorMessage cisInvalidCidErrorResponse = CisCommentResources.getInternalComments("INVALID",
            scenarioDiscussionResponse.getScenarioIdentity(),
            discussionCommentResponse.getDiscussionIdentity(),
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidCidErrorResponse.getMessage()).isEqualTo("'componentIdentity' is not a valid identity.");

        CisErrorMessage cisInvalidSidErrorResponse = CisCommentResources.getInternalComments(scenarioDiscussionResponse.getComponentIdentity(),
            "INVALID",
            discussionCommentResponse.getDiscussionIdentity(),
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            componentInfoBuilder.getUser());

        softAssertions.assertThat(cisInvalidSidErrorResponse.getMessage()).isEqualTo("'scenarioIdentity' is not a valid identity.");
    }


    @AfterEach
    public void testClean() {
        softAssertions.assertAll();
        CisDiscussionResources.deleteInternalDiscussion(scenarioDiscussionResponse.getComponentIdentity(),
            scenarioDiscussionResponse.getScenarioIdentity(),
            scenarioDiscussionResponse.getIdentity(),
            null,
            HttpStatus.SC_NO_CONTENT,
            componentInfoBuilder.getUser());
    }

    @AfterAll
    public static void afterClass() {
        new ScenariosUtil().deleteScenario(componentInfoBuilder.getComponentIdentity(), componentInfoBuilder.getScenarioIdentity(), componentInfoBuilder.getUser());
    }
}
