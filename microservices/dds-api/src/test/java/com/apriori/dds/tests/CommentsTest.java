package com.apriori.dds.tests;


import com.apriori.apibase.utils.TestUtil;
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
        commentResponse = DdsApiTestUtils.createComment(contentDesc, users, discussionResponse.getResponseEntity().getIdentity(), userContext);
    }

    @Test
    @TestRail(testCaseId = {"12360"})
    @Description("Create a valid comment")
    public void createComment() {
        softAssertions.assertThat(commentResponse.getStatusCode()).isEqualTo(HttpStatus.SC_CREATED);
        softAssertions.assertThat(commentResponse.getResponseEntity().getContent()).isEqualTo(contentDesc);
    }

    @Test
    @TestRail(testCaseId = {"12376"})
    @Description("get a valid comments")
    public void getComments() {
        RequestEntity requestEntity = RequestEntityUtil.init(DDSApiEnum.CUSTOMER_DISCUSSION_COMMENTS, CommentsResponse.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionResponse.getResponseEntity().getIdentity())
            .headers(DdsApiTestUtils.setUpHeader())
            .apUserContext(userContext);

        ResponseWrapper<CommentsResponse> responseWrapper = HTTPRequest.build(requestEntity).get();
        softAssertions.assertThat(responseWrapper.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
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
            .apUserContext(userContext);

        ResponseWrapper<CommentResponse> responseWrapper = HTTPRequest.build(requestEntity).get();
        softAssertions.assertThat(responseWrapper.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
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
                .mentionedUserEmails((ArrayList<String>) users).build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(DDSApiEnum.CUSTOMER_DISCUSSION_COMMENT, CommentResponse.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionResponse.getResponseEntity().getIdentity(), commentResponse.getResponseEntity().getIdentity())
            .body(commentsRequest)
            .headers(DdsApiTestUtils.setUpHeader())
            .apUserContext(userContext);

        ResponseWrapper<CommentResponse> commentUpdateResponse = HTTPRequest.build(requestEntity).patch();
        softAssertions.assertThat(commentUpdateResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(commentUpdateResponse.getResponseEntity().getContent()).isEqualTo(commentContent);
        softAssertions.assertThat(commentUpdateResponse.getResponseEntity().getMentionedUsers().size()).isGreaterThan(0);
    }

    @After
    public void testCleanup() {
        ResponseWrapper<String> commentDeleteResponse = DdsApiTestUtils.deleteComment(discussionResponse.getResponseEntity().getIdentity(), commentResponse.getResponseEntity().getIdentity(), userContext);
        ResponseWrapper<String> discussionDeleteResponse = DdsApiTestUtils.deleteDiscussion(discussionResponse.getResponseEntity().getIdentity(), userContext);
        softAssertions.assertThat(commentDeleteResponse.getStatusCode()).isEqualTo(HttpStatus.SC_NO_CONTENT);
        softAssertions.assertThat(discussionDeleteResponse.getStatusCode()).isEqualTo(HttpStatus.SC_NO_CONTENT);
        softAssertions.assertAll();
    }
}
