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
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.DmsApiTestUtils;

public class DmsCommentViewTest extends TestUtil {

    private static String userContext;
    private static SoftAssertions softAssertions;
    private static DmsDiscussionResponse discussionResponse;
    private static DmsCommentResponse commentResponse;
    private static DmsCommentViewResponse commentViewResponse;
    private static String contentDesc = StringUtils.EMPTY;
    private static UserCredentials currentUser = UserUtil.getUser();

    @Before
    public void testSetup() {
        contentDesc = RandomStringUtils.randomAlphabetic(12);
        softAssertions = new SoftAssertions();
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
        discussionResponse = DmsApiTestUtils.createDiscussion(contentDesc, currentUser);
        commentResponse = DmsApiTestUtils.addCommentToDiscussion(currentUser, contentDesc, discussionResponse.getIdentity(), DmsCommentResponse.class, HttpStatus.SC_CREATED);
        commentViewResponse = DmsApiTestUtils.markCommentViewAsRead(
            discussionResponse.getIdentity(),
            commentResponse.getIdentity(),
            commentResponse.getCommentView().get(0).getUserIdentity(),
            commentResponse.getCommentView().get(0).getUserCustomerIdentity(),
            commentResponse.getCommentView().get(0).getParticipantIdentity(),
            currentUser);
    }

    @Test
    @TestRail(testCaseId = {"15478", "15481"})
    @Description("Mark and Delete a comment as a viewed")
    public void markAndDeleteCommentView() {
        DmsCommentViewResponse markViewResponse = DmsApiTestUtils.markCommentViewAsRead(
            discussionResponse.getIdentity(),
            commentResponse.getIdentity(),
            commentResponse.getCommentView().get(0).getUserIdentity(),
            commentResponse.getCommentView().get(0).getUserCustomerIdentity(),
            commentResponse.getCommentView().get(0).getParticipantIdentity(),
            currentUser);
        softAssertions.assertThat(markViewResponse.getUserIdentity())
            .isEqualTo(commentResponse.getCommentView().get(0).getUserIdentity());

        DmsApiTestUtils.deleteCommentView(discussionResponse.getIdentity(),
            commentResponse.getIdentity(),
            markViewResponse.getIdentity(),
            currentUser);
    }

    @Test
    @TestRail(testCaseId = {"15479"})
    @Description("Find comment views")
    public void getCommentViews() {
        DmsCommentViewsResponse responseWrapper = DmsApiTestUtils.getDiscussionCommentViews(
            discussionResponse.getIdentity(),
            commentResponse.getIdentity(),
            currentUser);
        softAssertions.assertThat(responseWrapper.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"15480"})
    @Description("get a comment view by identity")
    public void getCommentView() {
        DmsCommentViewResponse responseWrapper = DmsApiTestUtils.getDiscussionCommentView(
            discussionResponse.getIdentity(), commentResponse.getIdentity(),
            commentViewResponse.getIdentity(), currentUser);

        softAssertions.assertThat(responseWrapper.getIdentity()).isNotNull();
    }

    @After
    public void testCleanup() {
        DmsApiTestUtils.deleteCommentView(discussionResponse.getIdentity(),
            commentResponse.getIdentity(),
            commentViewResponse.getIdentity(),
            currentUser);
        DmsApiTestUtils.deleteComment(discussionResponse.getIdentity(), commentResponse.getIdentity(), currentUser, null, HttpStatus.SC_NO_CONTENT);
        softAssertions.assertAll();
    }
}
