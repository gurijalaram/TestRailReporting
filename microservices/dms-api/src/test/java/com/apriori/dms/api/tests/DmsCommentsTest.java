package com.apriori.dms.api.tests;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.API_SANITY;

import com.apriori.dms.api.models.request.CommentsRequestParameters;
import com.apriori.dms.api.models.request.DmsCommentsRequest;
import com.apriori.dms.api.models.response.DmsCommentResponse;
import com.apriori.dms.api.models.response.DmsCommentsResponse;
import com.apriori.dms.api.models.response.DmsErrorMessageResponse;
import com.apriori.dms.api.utils.DmsApiTestDataUtils;
import com.apriori.dms.api.utils.DmsApiTestUtils;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.AuthUserContextUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;

@ExtendWith(TestRulesAPI.class)
public class DmsCommentsTest extends DmsApiTestDataUtils {

    @BeforeEach
    public void testSetup() {
    }

    @Test
    @Tag(API_SANITY)
    @TestRail(id = {13169, 14222})
    @Description("Verify user can add and delete comment")
    public void createAndDeleteCommentTest() {
        String commentDescription = new GenerateStringUtil().getRandomStringSpecLength(12);
        DmsCommentResponse dcResponse = DmsApiTestUtils.addCommentToDiscussion(currentUser, commentDescription, dmsScenarioDiscussionResponse.getItems()
            .get(0).getIdentity(), DmsCommentResponse.class, HttpStatus.SC_CREATED);
        softAssertions.assertThat(dcResponse.getContent()).isEqualTo(commentDescription);
        DmsApiTestUtils.deleteComment(dmsScenarioDiscussionResponse.getItems().get(0)
            .getIdentity(), dcResponse.getIdentity(), currentUser, null, HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @TestRail(id = {15484})
    @Description("Verify user can add and update comment status to deleted")
    public void createAndUpdateCommentTest() {
        String commentDescription = new GenerateStringUtil().getRandomStringSpecLength(12);
        DmsCommentResponse dcResponse = DmsApiTestUtils.addCommentToDiscussion(currentUser, commentDescription, dmsScenarioDiscussionResponse.getItems()
            .get(0).getIdentity(), DmsCommentResponse.class, HttpStatus.SC_CREATED);
        softAssertions.assertThat(dcResponse.getContent()).isEqualTo(commentDescription);
        DmsCommentResponse dcuResponse = DmsApiTestUtils.updateComment("DELETED", dmsScenarioDiscussionResponse.getItems()
                .get(0).getIdentity(),
            dcResponse.getIdentity(), currentUser);
        softAssertions.assertThat(dcuResponse.getStatus()).isEqualTo("DELETED");
    }

    @Test
    @Tag(API_SANITY)
    @TestRail(id = {13170})
    @Description("Get valid comments")
    public void getCommentsTest() {
        DmsCommentsResponse responseWrapper = DmsApiTestUtils.getDiscussionComments(currentUser,
            dmsScenarioDiscussionResponse.getItems().get(0).getIdentity());
        softAssertions.assertThat(responseWrapper.items.size()).isGreaterThan(0);
    }

    @Test
    @TestRail(id = {15483})
    @Description("Get a valid comment")
    public void getCommentTest() {
        DmsCommentResponse responseWrapper = DmsApiTestUtils.getDiscussionComment(currentUser,
            dmsScenarioDiscussionResponse.getItems().get(0).getIdentity(), dmsCommentResponse.getIdentity());
        softAssertions.assertThat(responseWrapper.getIdentity()).isNotNull();
    }

    @Test
    @TestRail(id = {13171})
    @Description("Update a valid comment")
    public void updateCommentTest() {
        DmsCommentResponse commentUpdateResponse = DmsApiTestUtils.updateComment(dmsCommentResponse.getStatus(),
            dmsScenarioDiscussionResponse.getItems().get(0)
                .getIdentity(), dmsCommentResponse.getIdentity(), currentUser);
        softAssertions.assertThat(commentUpdateResponse.getMentionedUsers().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(id = {16443})
    @Description("Verify user can add comment to discussion with another mentioned user")
    public void addCommentWithAnotherMentionedUserTest() {
        UserCredentials otherUser = UserUtil.getUser();
        String commentDescription = new GenerateStringUtil().getRandomStringSpecLength(12);
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
    public void addCommentByOtherUserTest() {
        UserCredentials otherUser = UserUtil.getUser();
        String commentsDescription = new GenerateStringUtil().getRandomStringSpecLength(12);
        DmsErrorMessageResponse dcResponse = DmsApiTestUtils.addCommentToDiscussion(otherUser, commentsDescription, dmsScenarioDiscussionResponse.getItems()
            .get(0).getIdentity(), DmsErrorMessageResponse.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(dcResponse.getErrorMessage()).contains("Participant with user identity '"
            + new AuthUserContextUtil().getAuthUserIdentity(otherUser.getEmail()) +
            "' is not a participant in discussion with identity '" +
            dmsScenarioDiscussionResponse.getItems().get(0).getIdentity() + "'");
    }
}