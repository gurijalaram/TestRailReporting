package com.apriori;

import com.apriori.dms.models.request.CommentsRequestParameters;
import com.apriori.dms.models.request.DmsCommentsRequest;
import com.apriori.dms.models.response.DmsCommentResponse;
import com.apriori.dms.models.response.DmsCommentsResponse;
import com.apriori.dms.models.response.DmsErrorMessageResponse;
import com.apriori.dms.utils.DmsApiTestDataUtils;
import com.apriori.dms.utils.DmsApiTestUtils;
import com.apriori.http.utils.AuthUserContextUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class DmsCommentsTest extends DmsApiTestDataUtils {
    private static String userContext;

    @BeforeEach
    public void testSetup() {
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
    }

    @Test
    @TestRail(id = {13169, 14222})
    @Description("Verify user can add and delete comment")
    public void createAndDeleteComment() {
        String commentDescription = new GenerateStringUtil().getRandomString();
        DmsCommentResponse dcResponse = DmsApiTestUtils.addCommentToDiscussion(currentUser, commentDescription, dmsScenarioDiscussionResponse.getItems()
            .get(0).getIdentity(), DmsCommentResponse.class, HttpStatus.SC_CREATED);
        softAssertions.assertThat(dcResponse.getContent()).isEqualTo(commentDescription);
        DmsApiTestUtils.deleteComment(dmsScenarioDiscussionResponse.getItems().get(0)
            .getIdentity(), dcResponse.getIdentity(), currentUser, null, HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @TestRail(id = {15484})
    @Description("Verify user can add and update comment status to deleted")
    public void createAndUpdateComment() {
        String commentDescription = new GenerateStringUtil().getRandomString();
        DmsCommentResponse dcResponse = DmsApiTestUtils.addCommentToDiscussion(currentUser, commentDescription, dmsScenarioDiscussionResponse.getItems()
            .get(0).getIdentity(), DmsCommentResponse.class, HttpStatus.SC_CREATED);
        softAssertions.assertThat(dcResponse.getContent()).isEqualTo(commentDescription);
        DmsCommentResponse dcuResponse = DmsApiTestUtils.updateComment("DELETED", dmsScenarioDiscussionResponse.getItems()
                .get(0).getIdentity(),
            dcResponse.getIdentity(), currentUser);
        softAssertions.assertThat(dcuResponse.getStatus()).isEqualTo("DELETED");
    }

    @Test
    @TestRail(id = {13170})
    @Description("get a valid comments")
    public void getComments() {
        DmsCommentsResponse responseWrapper = DmsApiTestUtils.getDiscussionComments(currentUser,
            dmsScenarioDiscussionResponse.getItems().get(0).getIdentity());
        softAssertions.assertThat(responseWrapper.items.size()).isGreaterThan(0);
    }

    @Test
    @TestRail(id = {15483})
    @Description("get a valid comment")
    public void getComment() {
        DmsCommentResponse responseWrapper = DmsApiTestUtils.getDiscussionComment(currentUser,
            dmsScenarioDiscussionResponse.getItems().get(0).getIdentity(), dmsCommentResponse.getIdentity());
        softAssertions.assertThat(responseWrapper.getIdentity()).isNotNull();
    }

    @Test
    @TestRail(id = {13171})
    @Description("update a valid comment")
    public void updateComment() {
        DmsCommentResponse commentUpdateResponse = DmsApiTestUtils.updateComment(dmsCommentResponse.getStatus(),
            dmsScenarioDiscussionResponse.getItems().get(0)
                .getIdentity(), dmsCommentResponse.getIdentity(), currentUser);
        softAssertions.assertThat(commentUpdateResponse.getMentionedUsers().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(id = {16443})
    @Description("Verify user can add comment to discussion with another mentioned user")
    public void addCommentWithAnotherMentionedUser() {
        UserCredentials otherUser = UserUtil.getUser();
        String commentDescription = new GenerateStringUtil().getRandomString();
        DmsCommentsRequest dmsCommentsRequest = DmsCommentsRequest.builder()
            .comment(CommentsRequestParameters.builder()
                .status("ACTIVE")
                .content(commentDescription)
                .mentionedUserEmails(Collections.singletonList(otherUser.getEmail())).build())
            .build();
        DmsCommentResponse commentResponse = DmsApiTestUtils.addCommentToDiscussion(dmsCommentsRequest,
            dmsScenarioDiscussionResponse.getItems().get(0).getIdentity(), currentUser);
        softAssertions.assertThat(commentResponse.getContent()).isEqualTo(commentDescription);
    }

    @Test
    @TestRail(id = {16445})
    @Description("Verify that only discussion participants can add comment to current discussion")
    public void addCommentByOtherUser() {
        UserCredentials otherUser = UserUtil.getUser();
        String commentsDescription = new GenerateStringUtil().getRandomString();
        DmsErrorMessageResponse dcResponse = DmsApiTestUtils.addCommentToDiscussion(otherUser, commentsDescription, dmsScenarioDiscussionResponse.getItems()
            .get(0).getIdentity(), DmsErrorMessageResponse.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(dcResponse.getErrorMessage()).contains("Participant with user identity '"
            + new AuthUserContextUtil().getAuthUserIdentity(otherUser.getEmail()) +
            "' is not a participant in discussion with identity '" +
            dmsScenarioDiscussionResponse.getItems().get(0).getIdentity() + "'");
    }
}