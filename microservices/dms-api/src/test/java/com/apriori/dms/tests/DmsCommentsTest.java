package com.apriori.dms.tests;


import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.ErrorMessage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import entity.request.CommentsRequestParameters;
import entity.request.DmsCommentsRequest;
import entity.response.DmsCommentResponse;
import entity.response.DmsCommentsResponse;
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

import java.util.Collections;

public class DmsCommentsTest extends TestUtil {

    private static String userContext;
    private static SoftAssertions softAssertions;
    private static DmsDiscussionResponse discussionResponse = null;
    private static DmsCommentResponse commentResponse = null;
    private static String contentDesc = StringUtils.EMPTY;
    private static UserCredentials currentUser = UserUtil.getUser();

    @Before
    public void testSetup() {
        contentDesc = RandomStringUtils.randomAlphabetic(12);
        softAssertions = new SoftAssertions();
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
        discussionResponse = DmsApiTestUtils.createDiscussion(contentDesc, currentUser);
        commentResponse = DmsApiTestUtils.addCommentToDiscussion(currentUser, contentDesc, discussionResponse.getIdentity(), DmsCommentResponse.class, HttpStatus.SC_CREATED);
    }

    @Test
    @TestRail(testCaseId = {"13169", "14222"})
    @Description("Verify user can add and delete comment")
    public void createAndDeleteComment() {
        String cDesc = new GenerateStringUtil().getRandomString();
        DmsCommentResponse dcResponse = DmsApiTestUtils.addCommentToDiscussion(currentUser, cDesc, discussionResponse.getIdentity(), DmsCommentResponse.class, HttpStatus.SC_CREATED);
        softAssertions.assertThat(dcResponse.getContent()).isEqualTo(cDesc);
        DmsApiTestUtils.deleteComment(discussionResponse.getIdentity(), dcResponse.getIdentity(), currentUser,null,HttpStatus.SC_NO_CONTENT);

    }

    @Test
    @TestRail(testCaseId = {"15484"})
    @Description("Verify user can add and update comment status to deleted")
    public void createAndUpdateComment() {
        String cDesc = new GenerateStringUtil().getRandomString();
        DmsCommentResponse dcResponse = DmsApiTestUtils.addCommentToDiscussion(currentUser, cDesc, discussionResponse.getIdentity(), DmsCommentResponse.class, HttpStatus.SC_CREATED);
        softAssertions.assertThat(dcResponse.getContent()).isEqualTo(cDesc);

        DmsCommentResponse dcuResponse = DmsApiTestUtils.updateComment("DELETED",discussionResponse.getIdentity(),
            dcResponse.getIdentity(),currentUser);

        softAssertions.assertThat(dcuResponse.getStatus()).isEqualTo("DELETED");
    }

    @Test
    @TestRail(testCaseId = {"13170"})
    @Description("get a valid comments")
    public void getComments() {
        DmsCommentsResponse responseWrapper = DmsApiTestUtils.getDiscussionComments(currentUser,
            discussionResponse.getIdentity());
        softAssertions.assertThat(responseWrapper.items.size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"15483"})
    @Description("get a valid comment")
    public void getComment() {
        DmsCommentResponse responseWrapper = DmsApiTestUtils.getDiscussionComment(currentUser,
            discussionResponse.getIdentity(), commentResponse.getIdentity());
        softAssertions.assertThat(responseWrapper.getIdentity()).isNotNull();
    }

    @Test
    @TestRail(testCaseId = {"13171"})
    @Description("update a valid comment")
    public void updateComment() {
        DmsCommentResponse commentUpdateResponse = DmsApiTestUtils.updateComment(commentResponse.getStatus(),
            discussionResponse.getIdentity(), commentResponse.getIdentity(), currentUser);

        softAssertions.assertThat(commentUpdateResponse.getMentionedUsers().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"16443"})
    @Description("Verify user can add comment to discussion with another mentioned user")
    public void addCommentWithAnotherMentionedUser() {
        UserCredentials otherUser = UserUtil.getUser();
        String cDesc = new GenerateStringUtil().getRandomString();
        DmsCommentsRequest dmsCommentsRequest = DmsCommentsRequest.builder()
            .comment(CommentsRequestParameters.builder()
                .status("ACTIVE")
                .content(cDesc)
                .mentionedUserEmails(Collections.singletonList(otherUser.getEmail())).build())
            .build();

        DmsCommentResponse commentResponse = DmsApiTestUtils.addCommentToDiscussion(dmsCommentsRequest,
            discussionResponse.getIdentity(),
            currentUser);

        softAssertions.assertThat(commentResponse.getContent()).isEqualTo(cDesc);
    }

    @Test
    @TestRail(testCaseId = {"16445"})
    @Description("Verify user can add and update comment status to deleted")
    public void addCommentByOtherUser() {
        UserCredentials otherUser = UserUtil.getUser();
        String cDesc = new GenerateStringUtil().getRandomString();
        DmsErrorMessageResponse dcResponse = DmsApiTestUtils.addCommentToDiscussion(otherUser, cDesc, discussionResponse.getIdentity(), DmsErrorMessageResponse.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(dcResponse.getErrorMessage()).contains("Participant with user identity '"
            + new AuthUserContextUtil().getAuthUserIdentity(otherUser.getEmail()) +
            "' is not a participant in discussion with identity '" +
            discussionResponse.getIdentity() + "'");
    }

    @After
    public void testCleanup() {
        DmsApiTestUtils.deleteComment(discussionResponse.getIdentity(), commentResponse.getIdentity(), currentUser, null, HttpStatus.SC_NO_CONTENT);
        softAssertions.assertAll();
    }
}