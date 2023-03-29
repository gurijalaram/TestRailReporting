package com.apriori.dms.tests;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.controller.QmsScenarioDiscussionResources;
import com.apriori.qms.entity.response.bidpackage.BidPackageItemResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.sds.entity.response.Scenario;
import com.apriori.sds.util.SDSTestUtil;
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
import java.util.HashSet;

public class DmsCommentsTest extends SDSTestUtil {
    private static SoftAssertions softAssertions;
    private static String bidPackageName;
    private static String projectName;
    private static ScenarioItem scenarioItem;
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageItemResponse bidPackageItemResponse;
    private static BidPackageProjectResponse bidPackageProjectResponse;
    private static String userContext;
    private static String contentDesc = StringUtils.EMPTY;
    private static DmsScenarioDiscussionResponse dmsScenarioDiscussionResponse;
    private static ScenarioDiscussionResponse qmsScenarioDiscussionResponse;
    private static DmsCommentResponse dmsCommentResponse;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        contentDesc = RandomStringUtils.randomAlphabetic(12);
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
        bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        projectName = "PROJ" + new GenerateStringUtil().getRandomNumbers();
        scenarioItem = postTestingComponentAndAddToRemoveList();
        publishAssembly(ComponentInfoBuilder.builder().scenarioName(scenarioItem.getScenarioName()).user(testingUser)
            .componentIdentity(scenarioItem.getComponentIdentity()).scenarioIdentity(scenarioItem.getScenarioIdentity())
            .build(), Scenario.class, HttpStatus.SC_OK);
        bidPackageResponse = QmsBidPackageResources.createBidPackage(bidPackageName, currentUser);
        bidPackageItemResponse = QmsBidPackageResources.createBidPackageItem(
            QmsBidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bidPackageResponse.getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_CREATED);
        bidPackageProjectResponse = QmsBidPackageResources.createBidPackageProject(projectName, bidPackageResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
        qmsScenarioDiscussionResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
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
        if (otherUser.getEmail().equals(currentUser.getEmail())) {
            otherUser = UserUtil.getUser();
        }
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
        if (otherUser.getEmail().equals(currentUser.getEmail())) {
            otherUser = UserUtil.getUser();
        }
        String commentsDescription = new GenerateStringUtil().getRandomString();
        DmsErrorMessageResponse dcResponse = DmsApiTestUtils.addCommentToDiscussion(otherUser, commentsDescription, dmsScenarioDiscussionResponse.getItems()
            .get(0).getIdentity(), DmsErrorMessageResponse.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(dcResponse.getErrorMessage()).contains("Participant with user identity '"
            + new AuthUserContextUtil().getAuthUserIdentity(otherUser.getEmail()) +
            "' is not a participant in discussion with identity '" +
            dmsScenarioDiscussionResponse.getItems().get(0).getIdentity() + "'");
    }

    @After
    public void testCleanup() {
        QmsScenarioDiscussionResources.deleteScenarioDiscussion(qmsScenarioDiscussionResponse.getIdentity(), currentUser);
        QmsBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        softAssertions.assertAll();
    }

    private static final UserCredentials currentUser = testingUser;
}