package com.apriori.dms.tests;


import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import entity.response.DmsCommentResponse;
import entity.response.DmsCommentViewResponse;
import entity.response.DmsCommentViewsResponse;
import entity.response.DmsDiscussionResponse;
import io.qameta.allure.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.DmsApiTestUtils;

public class DmsCommentViewTest extends TestUtil {

    private static String userContext;
    private static SoftAssertions softAssertions;
    private static ResponseWrapper<DmsDiscussionResponse> discussionResponse;
    private static ResponseWrapper<DmsCommentResponse> commentResponse;
    private static ResponseWrapper<DmsCommentViewResponse> commentViewResponse;
    private static String contentDesc = StringUtils.EMPTY;
    private static UserCredentials currentUser = UserUtil.getUser();

    @Before
    public void testSetup() {
        contentDesc = RandomStringUtils.randomAlphabetic(12);
        softAssertions = new SoftAssertions();
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
        discussionResponse = DmsApiTestUtils.createDiscussion(contentDesc, currentUser);
        commentResponse = DmsApiTestUtils.createDiscussionComment(currentUser, contentDesc, discussionResponse.getResponseEntity().getIdentity());
        commentViewResponse = DmsApiTestUtils.markCommentViewAsRead(
            discussionResponse.getResponseEntity().getIdentity(),
            commentResponse.getResponseEntity().getIdentity(),
            commentResponse.getResponseEntity().getCommentView().get(0).getUserIdentity(),
            commentResponse.getResponseEntity().getCommentView().get(0).getUserCustomerIdentity(),
            commentResponse.getResponseEntity().getCommentView().get(0).getParticipantIdentity(),
            currentUser);
    }

    @Test
    @TestRail(testCaseId = {"15478", "15481"})
    @Description("Mark and Delete a comment as a viewed")
    public void markAndDeleteCommentView() {
        ResponseWrapper<DmsCommentViewResponse> markViewResponse = DmsApiTestUtils.markCommentViewAsRead(
            discussionResponse.getResponseEntity().getIdentity(),
            commentResponse.getResponseEntity().getIdentity(),
            commentResponse.getResponseEntity().getCommentView().get(0).getUserIdentity(),
            commentResponse.getResponseEntity().getCommentView().get(0).getUserCustomerIdentity(),
            commentResponse.getResponseEntity().getCommentView().get(0).getParticipantIdentity(),
            currentUser);
        softAssertions.assertThat(markViewResponse.getResponseEntity().getUserIdentity())
            .isEqualTo(commentResponse.getResponseEntity().getCommentView().get(0).getUserIdentity());

        DmsApiTestUtils.deleteCommentView(discussionResponse.getResponseEntity().getIdentity(),
            commentResponse.getResponseEntity().getIdentity(),
            markViewResponse.getResponseEntity().getIdentity(),
            currentUser);
    }

    @Test
    @TestRail(testCaseId = {"15479"})
    @Description("Find comment views")
    public void getCommentViews() {
        ResponseWrapper<DmsCommentViewsResponse> responseWrapper = DmsApiTestUtils.getDiscussionCommentViews(
            discussionResponse.getResponseEntity().getIdentity(),
            commentResponse.getResponseEntity().getIdentity(),
            currentUser);
        softAssertions.assertThat(responseWrapper.getResponseEntity().items.size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"15480"})
    @Description("get a comment view by identity")
    public void getCommentView() {
        ResponseWrapper<DmsCommentViewResponse> responseWrapper = DmsApiTestUtils.getDiscussionCommentView(
            discussionResponse.getResponseEntity().getIdentity(), commentResponse.getResponseEntity().getIdentity(),
            commentViewResponse.getResponseEntity().getIdentity(), currentUser);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getIdentity()).isNotNull();
    }

    @After
    public void testCleanup() {
        DmsApiTestUtils.deleteCommentView(discussionResponse.getResponseEntity().getIdentity(),
            commentResponse.getResponseEntity().getIdentity(),
            commentViewResponse.getResponseEntity().getIdentity(),
            currentUser);
        DmsApiTestUtils.deleteComment(discussionResponse.getResponseEntity().getIdentity(), commentResponse.getResponseEntity().getIdentity(), currentUser);
        softAssertions.assertAll();
    }
}
