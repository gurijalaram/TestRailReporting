package com.apriori.dms.tests;


import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.ErrorMessage;
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import entity.request.DiscussionsRequest;
import entity.request.DiscussionsRequestParameters;
import entity.response.DmsCommentResponse;
import entity.response.DmsDiscussionParticipantResponse;
import entity.response.DmsDiscussionParticipantsResponse;
import entity.response.DmsDiscussionResponse;
import entity.response.DmsErrorMessageResponse;
import io.qameta.allure.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.DmsApiTestUtils;

public class DmsDiscussionParticipantTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static DmsDiscussionResponse discussionResponse;
    UserCredentials currentUser;
    private static String discussionDescription = StringUtils.EMPTY;


    @Before
    public void testSetup() {
        currentUser = UserUtil.getUser();
        discussionDescription = RandomStringUtils.randomAlphabetic(12);
        softAssertions = new SoftAssertions();
        discussionResponse = DmsApiTestUtils.createDiscussion(discussionDescription, currentUser);
    }

    @Test
    @TestRail(testCaseId = {"13167"})
    @Description("get discussion participants")
    public void getDiscussionParticipants() {
        DmsDiscussionParticipantsResponse responseWrapper =
            DmsApiTestUtils.getDiscussionParticipants(discussionResponse.getIdentity(), currentUser);

        softAssertions.assertThat(responseWrapper.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(responseWrapper.getIsFirstPage()).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"13055", "13168", "15482"})
    @Description("Add, Get and remove participant to discussion")
    public void addGetAndRemoveParticipantToDiscussion() {
        DmsDiscussionParticipantResponse createParticipantResponse = DmsApiTestUtils.addDiscussionParticipant(
            discussionResponse.getIdentity(),
            currentUser,
            DmsDiscussionParticipantResponse.class,
            HttpStatus.SC_CREATED);
        softAssertions.assertThat(createParticipantResponse.getIdentity()).isNotNull();

        DmsDiscussionParticipantResponse getDDPResponse = DmsApiTestUtils.getDiscussionParticipant(discussionResponse.getIdentity(),
            createParticipantResponse.getIdentity(), currentUser);

        softAssertions.assertThat(createParticipantResponse.getUserCustomerIdentity()).isEqualTo(getDDPResponse.getUserCustomerIdentity());

        DmsApiTestUtils.deleteDiscussionParticipant(discussionResponse.getIdentity(),
            createParticipantResponse.getIdentity(),
            null, HttpStatus.SC_NO_CONTENT,
            currentUser);
    }

    @Test
    @TestRail(testCaseId = {"14614"})
    @Description("Verify that user can not Delete discussion's participant which was assigned to this discussion")
    public void verifyAssigneeCannotDeleteParticipant() {
        DiscussionsRequest discussionsRequest = DiscussionsRequest.builder()
            .discussion(DiscussionsRequestParameters.builder()
                .status("ACTIVE")
                .description(discussionDescription)
                .assigneeEmail(currentUser.getEmail())
                .build())
            .build();

        DmsDiscussionResponse disResponse = DmsApiTestUtils.createDiscussion(discussionsRequest, currentUser);

        softAssertions.assertThat(disResponse.getDescription()).isEqualTo(discussionDescription);

        DmsErrorMessageResponse dmsErrorMessageResponse = DmsApiTestUtils.deleteDiscussionParticipant(disResponse.getIdentity(),
            disResponse.getAssignee().getIdentity(), DmsErrorMessageResponse.class, HttpStatus.SC_CONFLICT, currentUser);

        softAssertions.assertThat(dmsErrorMessageResponse.getErrorMessage()).contains("Can't remove a participant if they are the assignee for the discussion");
    }

    @Test
    @TestRail(testCaseId = {"16444"})
    @Description("Verify that only discussion's participants can change its status")
    public void verifyDiscussionParticipantCanChangeStatus() {
        UserCredentials otherUser = UserUtil.getUser();
        DiscussionsRequest discussionsRequest = DiscussionsRequest.builder()
            .discussion(DiscussionsRequestParameters.builder()
                .status("ACTIVE")
                .description(discussionDescription)
                .assigneeEmail(currentUser.getEmail())
                .build())
            .build();

        DmsDiscussionResponse disResponse = DmsApiTestUtils.createDiscussion(discussionsRequest, currentUser);

        DmsErrorMessageResponse dmsErrorMessageResponse = DmsApiTestUtils.updateDiscussion(discussionDescription,
            "RESOLVED", disResponse.getIdentity(),
            otherUser, DmsErrorMessageResponse.class,
            HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(dmsErrorMessageResponse.getErrorMessage()).contains("Participant with user identity '"
            + new AuthUserContextUtil().getAuthUserIdentity(otherUser.getEmail()) +
            "' is not a participant in discussion with identity '" +
            disResponse.getIdentity() + "'");
    }

    @Test
    @TestRail(testCaseId = {"16446"})
    @Description("Verify that only discussion participants can add new participants to current discussion")
    public void verifyDiscussionParticipantAddNewParticipant() {
        UserCredentials otherUser = UserUtil.getUser();
        DmsErrorMessageResponse dmsErrorMessageResponse = DmsApiTestUtils.addDiscussionParticipant(
            discussionResponse.getIdentity(),
            otherUser,
            DmsErrorMessageResponse.class,
            HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(dmsErrorMessageResponse.getErrorMessage()).contains("Participant with user identity '"
            + new AuthUserContextUtil().getAuthUserIdentity(otherUser.getEmail()) +
            "' is not a participant in discussion with identity '" +
            discussionResponse.getIdentity() + "'");
    }

    @Test
    @TestRail(testCaseId = {"16447"})
    @Description("Verify that only user who created a comment can delete it")
    public void verifyCommentCanBeDeletedByCreatedUser() {
        UserCredentials otherUser = UserUtil.getUser();
        String contentDesc = RandomStringUtils.randomAlphabetic(12);
        DmsCommentResponse commentResponse = DmsApiTestUtils.addCommentToDiscussion(currentUser, contentDesc, discussionResponse.getIdentity(), DmsCommentResponse.class, HttpStatus.SC_CREATED);
        softAssertions.assertThat(commentResponse.getContent()).isEqualTo(contentDesc);

        DmsDiscussionParticipantResponse createParticipantResponse = DmsApiTestUtils.addDiscussionParticipant(
            discussionResponse.getIdentity(),
            currentUser,
            DmsDiscussionParticipantResponse.class,
            HttpStatus.SC_CREATED);
        softAssertions.assertThat(createParticipantResponse.getIdentity()).isNotNull();

        ErrorMessage deleteResponse = DmsApiTestUtils.deleteComment(discussionResponse.getIdentity(),
            commentResponse.getIdentity(),
            otherUser,
            ErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(deleteResponse.getMessage()).contains("Participant with user identity '"
            + new AuthUserContextUtil().getAuthUserIdentity(otherUser.getEmail()) +
            "' is not a participant in discussion with identity '" +
            discussionResponse.getIdentity() + "'");

        DmsApiTestUtils.deleteComment(discussionResponse.getIdentity(), commentResponse.getIdentity(), currentUser, null, HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @TestRail(testCaseId = {"16448"})
    @Description("Verify that only discussion participants can delete participants from current discussion")
    public void verifyParticipantCanBeDeletedByDiscussionParticipant() {
        DmsDiscussionParticipantResponse createParticipantResponse = DmsApiTestUtils.addDiscussionParticipant(
            discussionResponse.getIdentity(),
            currentUser,
            DmsDiscussionParticipantResponse.class,
            HttpStatus.SC_CREATED);

        softAssertions.assertThat(createParticipantResponse.getIdentity()).isNotNull();

        UserCredentials user3 = UserUtil.getUser();
        ErrorMessage deleteErrorMsgResponse = DmsApiTestUtils.deleteDiscussionParticipant(discussionResponse.getIdentity(),
            createParticipantResponse.getIdentity(),
            ErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            user3);

        softAssertions.assertThat(deleteErrorMsgResponse.getMessage()).contains("Participant with user identity '"
            + new AuthUserContextUtil().getAuthUserIdentity(user3.getEmail()) +
            "' is not a participant in discussion with identity '" +
            discussionResponse.getIdentity() + "'");

        DmsApiTestUtils.deleteDiscussionParticipant(discussionResponse.getIdentity(),
            createParticipantResponse.getIdentity(),
            null,
            HttpStatus.SC_NO_CONTENT,
            currentUser);
    }

    @After
    public void testCleanup() {
        softAssertions.assertAll();
    }
}
