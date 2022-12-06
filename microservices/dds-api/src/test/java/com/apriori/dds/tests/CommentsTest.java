package com.apriori.dds.tests;


import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.ErrorMessage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import entity.request.CommentsRequest;
import entity.request.CommentsRequestParameters;
import entity.response.CommentResponse;
import entity.response.CommentsResponse;
import entity.response.DiscussionResponse;
import enums.DDSApiEnum;
import io.qameta.allure.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.DdsApiTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommentsTest extends TestUtil {

    private static String userContext;
    private static SoftAssertions softAssertions;
    private static ResponseWrapper<DiscussionResponse> discussionResponse;
    private static ResponseWrapper<CommentResponse> commentResponse;
    private static String contentDesc = StringUtils.EMPTY;
    private static UserCredentials currentUser = UserUtil.getUser();
    private static List<String> users = new ArrayList<>();

    @Before
    public void testSetup() {
        contentDesc = RandomStringUtils.randomAlphabetic(12);
        users.add(currentUser.getEmail());
        softAssertions = new SoftAssertions();
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
        discussionResponse = DdsApiTestUtils.createDiscussion(contentDesc, userContext);
        CommentsRequest commentsRequestBuilder = CommentsRequest.builder()
            .comment(CommentsRequestParameters.builder()
                .status("ACTIVE")
                .content(new GenerateStringUtil().getRandomString())
                .mentionedUserEmails(Collections.singletonList(currentUser.getEmail()))
                .build())
            .build();
        commentResponse = DdsApiTestUtils.createComment(commentsRequestBuilder,
            discussionResponse.getResponseEntity().getIdentity(),
            currentUser,
            CommentResponse.class,
            HttpStatus.SC_CREATED);
    }

    @Test
    @TestRail(testCaseId = {"12360", "12378"})
    @Description("Create and Delete a valid comment")
    public void createAndDeleteComment() {
        String content = new GenerateStringUtil().getRandomString();
        CommentsRequest commentsRequestBuilder = CommentsRequest.builder()
            .comment(CommentsRequestParameters.builder()
                .status("ACTIVE")
                .content(content)
                .mentionedUserEmails(Collections.singletonList(currentUser.getEmail()))
                .build())
            .build();
        ResponseWrapper<CommentResponse> commentCreateResponse = DdsApiTestUtils.createComment(commentsRequestBuilder,
            discussionResponse.getResponseEntity().getIdentity(),
            currentUser,
            CommentResponse.class,
            HttpStatus.SC_CREATED);
        softAssertions.assertThat(commentCreateResponse.getResponseEntity().getContent()).isEqualTo(content);
        DdsApiTestUtils.deleteComment(discussionResponse.getResponseEntity().getIdentity(), commentCreateResponse.getResponseEntity().getIdentity(), userContext);
    }

    @Test
    @TestRail(testCaseId = {"12376", "14326"})
    @Description("get a valid comments and verify pagination")
    public void getComments() {
        RequestEntity requestEntity = RequestEntityUtil.init(DDSApiEnum.CUSTOMER_DISCUSSION_COMMENTS, CommentsResponse.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionResponse.getResponseEntity().getIdentity())
            .headers(DdsApiTestUtils.setUpHeader())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<CommentsResponse> responseWrapper = HTTPRequest.build(requestEntity).get();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getIsFirstPage()).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"12373"})
    @Description("get a valid comment")
    public void getComment() {
        RequestEntity requestEntity = RequestEntityUtil.init(DDSApiEnum.CUSTOMER_DISCUSSION_COMMENT, CommentResponse.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"),
                discussionResponse.getResponseEntity().getIdentity(),
                commentResponse.getResponseEntity().getIdentity())
            .headers(DdsApiTestUtils.setUpHeader())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<CommentResponse> responseWrapper = HTTPRequest.build(requestEntity).get();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getIdentity()).isNotNull();
    }

    @Test
    @TestRail(testCaseId = {"12375"})
    @Description("update a valid comment")
    public void updateComment() {
        String commentContent = RandomStringUtils.randomAlphabetic(15);
        CommentsRequest commentsRequest = CommentsRequest.builder()
            .comment(CommentsRequestParameters.builder()
                .status(commentResponse.getResponseEntity().getStatus())
                .content(commentContent)
                .mentionedUserEmails(Collections.singletonList(currentUser.getEmail()))
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(DDSApiEnum.CUSTOMER_DISCUSSION_COMMENT, CommentResponse.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionResponse.getResponseEntity().getIdentity(), commentResponse.getResponseEntity().getIdentity())
            .body(commentsRequest)
            .headers(DdsApiTestUtils.setUpHeader())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<CommentResponse> commentUpdateResponse = HTTPRequest.build(requestEntity).patch();
        softAssertions.assertThat(commentUpdateResponse.getResponseEntity().getContent()).isEqualTo(commentContent);
        softAssertions.assertThat(commentUpdateResponse.getResponseEntity().getMentionedUsers().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"12371"})
    @Description("Create a valid comment")
    public void createCommentWithInvalidStatus() {
        CommentsRequest commentsRequestBuilder = CommentsRequest.builder()
            .comment(CommentsRequestParameters.builder()
                .status("INVALID")
                .content(new GenerateStringUtil().getRandomString())
                .mentionedUserEmails(Collections.singletonList(currentUser.getEmail()))
                .build())
            .build();

        ResponseWrapper<ErrorMessage> responseWrapper = DdsApiTestUtils.createComment(commentsRequestBuilder,
            discussionResponse.getResponseEntity().getIdentity(),
            currentUser, ErrorMessage.class,
            HttpStatus.SC_INTERNAL_SERVER_ERROR);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).contains("Unable to find constant 'INVALID' in enum 'CommentStatus'");
    }

    @Test
    @TestRail(testCaseId = {"12372"})
    @Description("Create a comment with invalid discussion")
    public void createCommentWithInvalidDiscussion() {
        CommentsRequest commentsRequestBuilder = CommentsRequest.builder()
            .comment(CommentsRequestParameters.builder()
                .status("ACTIVE")
                .content(new GenerateStringUtil().getRandomString())
                .mentionedUserEmails(Collections.singletonList(currentUser.getEmail()))
                .build())
            .build();

        ResponseWrapper<ErrorMessage> responseWrapper = DdsApiTestUtils.createComment(commentsRequestBuilder,
            "INVALID",
            currentUser, ErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).contains("'discussionIdentity' is not a valid identity");
    }

    @Test
    @TestRail(testCaseId = {"14330"})
    @Description("Create a comment with empty content")
    public void createCommentWithEmptyContent() {
        CommentsRequest commentsRequestBuilder = CommentsRequest.builder()
            .comment(CommentsRequestParameters.builder()
                .status("ACTIVE")
                .content("")
                .mentionedUserEmails(Collections.singletonList(currentUser.getEmail()))
                .build())
            .build();

        ResponseWrapper<ErrorMessage> responseWrapper = DdsApiTestUtils.createComment(commentsRequestBuilder,
            discussionResponse.getResponseEntity().getIdentity(),
            currentUser, ErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).contains("'content' should not be null");
    }

    @Test
    @TestRail(testCaseId = {"12374"})
    @Description("get a invalid comment")
    public void getInvalidComment() {
        RequestEntity requestEntity = RequestEntityUtil.init(DDSApiEnum.CUSTOMER_DISCUSSION_COMMENT, ErrorMessage.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"),
                discussionResponse.getResponseEntity().getIdentity(),
                "INVALID")
            .headers(DdsApiTestUtils.setUpHeader())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_BAD_REQUEST);

        ResponseWrapper<ErrorMessage> responseWrapper = HTTPRequest.build(requestEntity).get();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).contains("'identity' is not a valid identity");
    }

    @Test
    @TestRail(testCaseId = {"12377"})
    @Description("get a comment With Invalid discussion")
    public void getCommentWithInvalidDiscussion() {
        RequestEntity requestEntity = RequestEntityUtil.init(DDSApiEnum.CUSTOMER_DISCUSSION_COMMENT, ErrorMessage.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"),
                "INVALID",
                commentResponse.getResponseEntity().getIdentity())
            .headers(DdsApiTestUtils.setUpHeader())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_BAD_REQUEST);

        ResponseWrapper<ErrorMessage> responseWrapper = HTTPRequest.build(requestEntity).get();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).contains("'discussionIdentity' is not a valid identity");
    }

    @Test
    @TestRail(testCaseId = {"14329"})
    @Description("Create a comment with empty mentioned users")
    public void createCommentWithEmptyMentionedUsers() {
        String content = new GenerateStringUtil().getRandomString();
        CommentsRequest commentsRequestBuilder = CommentsRequest.builder()
            .comment(CommentsRequestParameters.builder()
                .status(commentResponse.getResponseEntity().getStatus())
                .content(content)
                .mentionedUserEmails(Collections.emptyList())
                .build())
            .build();
        ResponseWrapper<CommentResponse> commentCreateResponse = DdsApiTestUtils.createComment(commentsRequestBuilder,
            discussionResponse.getResponseEntity().getIdentity(),
            currentUser,
            CommentResponse.class,
            HttpStatus.SC_CREATED);
        softAssertions.assertThat(commentCreateResponse.getResponseEntity().getContent()).isEqualTo(content);
        softAssertions.assertThat(commentCreateResponse.getResponseEntity().getMentionedUsers().size()).isEqualTo(0);
    }

    @After
    public void testCleanup() {
        DdsApiTestUtils.deleteComment(discussionResponse.getResponseEntity().getIdentity(), commentResponse.getResponseEntity().getIdentity(), userContext);
        DdsApiTestUtils.deleteDiscussion(discussionResponse.getResponseEntity().getIdentity(), userContext);
        softAssertions.assertAll();
    }
}
