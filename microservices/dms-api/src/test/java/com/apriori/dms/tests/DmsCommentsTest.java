package com.apriori.dms.tests;


import com.apriori.apibase.utils.TestUtil;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.qms.controller.QmsScenarioDiscussionResources;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.utils.CssComponent;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import entity.request.CommentsRequestParameters;
import entity.request.DmsCommentsRequest;
import entity.response.DmsCommentResponse;
import entity.response.DmsCommentsResponse;
import entity.response.DmsErrorMessageResponse;
import entity.response.DmsScenarioDiscussionResponse;
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
    private static String contentDesc = StringUtils.EMPTY;
    private static ScenarioItem scenarioItem;
    private static DmsScenarioDiscussionResponse dmsScenarioDiscussionResponse;
    private static ScenarioDiscussionResponse qmsScenarioDiscussionResponse;
    private static DmsCommentResponse dmsCommentResponse = null;
    private static final UserCredentials currentUser = UserUtil.getUser();

    @Before
    public void testSetup() {
        contentDesc = RandomStringUtils.randomAlphabetic(12);
        softAssertions = new SoftAssertions();
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());

        //Create scenario discussion on QMS
        scenarioItem = new CssComponent().getBaseCssComponents(currentUser).get(0);
        softAssertions.assertThat(scenarioItem.getComponentIdentity()).isNotNull();
        qmsScenarioDiscussionResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);

        //Get generic DMS discussion identity from QMS discussion
        dmsScenarioDiscussionResponse = DmsApiTestUtils.getScenarioDiscussions(DmsScenarioDiscussionResponse.class, HttpStatus.SC_OK, currentUser, qmsScenarioDiscussionResponse);
        dmsCommentResponse = DmsApiTestUtils.addCommentToDiscussion(currentUser, contentDesc, dmsScenarioDiscussionResponse.getItems()
            .get(0).getIdentity(), DmsCommentResponse.class, HttpStatus.SC_CREATED);
    }

    @Test
    @TestRail(testCaseId = {"13169", "14222"})
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
    @TestRail(testCaseId = {"15484"})
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
    @TestRail(testCaseId = {"13170"})
    @Description("get a valid comments")
    public void getComments() {
        DmsCommentsResponse responseWrapper = DmsApiTestUtils.getDiscussionComments(currentUser,
            dmsScenarioDiscussionResponse.getItems().get(0).getIdentity());
        softAssertions.assertThat(responseWrapper.items.size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"15483"})
    @Description("get a valid comment")
    public void getComment() {
        DmsCommentResponse responseWrapper = DmsApiTestUtils.getDiscussionComment(currentUser,
            dmsScenarioDiscussionResponse.getItems().get(0).getIdentity(), dmsCommentResponse.getIdentity());
        softAssertions.assertThat(responseWrapper.getIdentity()).isNotNull();
    }

    @Test
    @TestRail(testCaseId = {"13171"})
    @Description("update a valid comment")
    public void updateComment() {
        DmsCommentResponse commentUpdateResponse = DmsApiTestUtils.updateComment(dmsCommentResponse.getStatus(),
            dmsScenarioDiscussionResponse.getItems().get(0)
                .getIdentity(), dmsCommentResponse.getIdentity(), currentUser);
        softAssertions.assertThat(commentUpdateResponse.getMentionedUsers().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"16443"})
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
    @TestRail(testCaseId = {"16445"})
    @Description("Verify that only discussion participants can add comment to current discussion")
    public void addCommentByOtherUser() {
        UserCredentials otherUser = UserUtil.getUser();
        String commentcDescription = new GenerateStringUtil().getRandomString();
        DmsErrorMessageResponse dcResponse = DmsApiTestUtils.addCommentToDiscussion(otherUser, commentcDescription, dmsScenarioDiscussionResponse.getItems()
            .get(0).getIdentity(), DmsErrorMessageResponse.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(dcResponse.getErrorMessage()).contains("Participant with user identity '"
            + new AuthUserContextUtil().getAuthUserIdentity(otherUser.getEmail()) +
            "' is not a participant in discussion with identity '" +
            dmsScenarioDiscussionResponse.getItems().get(0).getIdentity() + "'");
    }

    @After
    public void testCleanup() {
        QmsScenarioDiscussionResources.deleteScenarioDiscussion(qmsScenarioDiscussionResponse.getIdentity(), currentUser);
        softAssertions.assertAll();
    }
}