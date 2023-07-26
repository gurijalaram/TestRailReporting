package com.apriori.qms.tests;

import com.apriori.AuthUserContextUtil;
import com.apriori.GenerateStringUtil;
import com.apriori.TestUtil;
import com.apriori.authorization.response.ApwErrorMessage;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.QueryParams;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.qms.controller.QmsScenarioDiscussionResources;
import com.apriori.qms.entity.request.scenariodiscussion.DiscussionCommentParameters;
import com.apriori.qms.entity.request.scenariodiscussion.DiscussionCommentRequest;
import com.apriori.qms.entity.request.scenariodiscussion.ScenarioDiscussionParameters;
import com.apriori.qms.entity.request.scenariodiscussion.ScenarioDiscussionRequest;
import com.apriori.qms.entity.response.bidpackage.BidPackageResponse;
import com.apriori.qms.entity.response.scenariodiscussion.DiscussionCommentResponse;
import com.apriori.qms.entity.response.scenariodiscussion.DiscussionCommentViewResponse;
import com.apriori.qms.entity.response.scenariodiscussion.DiscussionCommentsResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioDiscussionsResponse;
import com.apriori.qms.enums.QMSAPIEnum;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.QmsApiTestUtils;

import java.util.Collections;

public class QmsScenarioDiscussionTest extends TestUtil {
    private static SoftAssertions softAssertions = new SoftAssertions();
    private static BidPackageResponse bidPackageResponse;
    private static ScenarioDiscussionResponse scenarioDiscussionResponse;
    private static DiscussionCommentResponse discussionCommentResponse;
    private static ScenarioItem scenarioItem;
    private static final UserCredentials currentUser = UserUtil.getUser();

    @BeforeClass
    public static void beforeClass() {
        scenarioItem = QmsApiTestUtils
            .createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        bidPackageResponse = QmsApiTestUtils.createTestDataBidPackage(currentUser, softAssertions);
        QmsApiTestUtils.createTestDataBidPackageItem(scenarioItem, bidPackageResponse, currentUser, softAssertions);
        QmsApiTestUtils.createTestDataBidPackageProject(bidPackageResponse, currentUser, softAssertions);
        scenarioDiscussionResponse = QmsApiTestUtils
            .createTestDataScenarioDiscussion(scenarioItem, currentUser, softAssertions);
        discussionCommentResponse = QmsApiTestUtils
            .createTestDataAddCommentToDiscussion(scenarioDiscussionResponse, currentUser, softAssertions);
    }

    @AfterClass
    public static void afterClass() {
        QmsApiTestUtils.deleteTestData(scenarioItem, bidPackageResponse, currentUser);
        softAssertions.assertAll();
    }

    @Before
    public void beforeTest() {
        softAssertions = new SoftAssertions();
    }

    @After
    public void afterTest() {
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14608, 14613})
    @Description("Create and delete Scenario Discussion")
    public void createAndDeleteScenarioDiscussion() {
        ScenarioDiscussionResponse csdResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
        softAssertions.assertThat(csdResponse.getIdentity()).isNotNull();
        QmsScenarioDiscussionResources.deleteScenarioDiscussion(csdResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(id = {14610})
    @Description("Get Scenario Discussion by identity")
    public void getScenarioDiscussion() {
        ScenarioDiscussionResponse getScenarioDiscussionResponse = QmsScenarioDiscussionResources.getScenarioDiscussion(scenarioDiscussionResponse.getIdentity(), ScenarioDiscussionResponse.class,
            HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(getScenarioDiscussionResponse.getIdentity())
            .isEqualTo(scenarioDiscussionResponse.getIdentity());
    }

    @Test
    @TestRail(id = {14609})
    @Description("Get list of all Scenario Discussions")
    public void getScenarioDiscussions() {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSIONS, ScenarioDiscussionsResponse.class)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ScenarioDiscussionsResponse> responseWrapper = HTTPRequest.build(requestEntity).get();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(id = {14611, 14612, 15472, 16050})
    @Issue("COL-1824")
    @Description("Verify that User can update Scenario discussion description and status (ACTIVE & RESOLVED")
    public void updateScenarioDiscussionDescriptionAndStatus() {
        ScenarioDiscussionResponse csdResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
        softAssertions.assertThat(csdResponse.getIdentity()).isNotNull();

        String description = new GenerateStringUtil().generateNotes();
        ScenarioDiscussionRequest scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder().description(description).build()).build();
        ScenarioDiscussionResponse updateResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(csdResponse.getIdentity(),
            scenarioDiscussionRequest,
            ScenarioDiscussionResponse.class,
            HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateResponse.getDescription()).isEqualTo(description);

        scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder().status("RESOLVED").build()).build();
        updateResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(csdResponse.getIdentity(),
            scenarioDiscussionRequest,
            ScenarioDiscussionResponse.class,
            HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateResponse.getStatus()).isEqualTo("RESOLVED");

        scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder().status("ACTIVE").build()).build();
        updateResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(csdResponse.getIdentity(),
            scenarioDiscussionRequest,
            ScenarioDiscussionResponse.class,
            HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateResponse.getStatus()).isEqualTo("ACTIVE");

        QmsScenarioDiscussionResources.deleteScenarioDiscussion(csdResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(id = {14675, 14678, 15477})
    @Description("Verify that User can add comment, update status to delete to scenario discussion" +
        "and verify comment view is created for the same user")
    public void addAndDeleteCommentToDiscussion() {
        String commentContent = new GenerateStringUtil().generateNotes();
        DiscussionCommentRequest discussionCommentRequest = DiscussionCommentRequest.builder()
            .comment(DiscussionCommentParameters.builder()
                .content(commentContent)
                .status("ACTIVE")
                .build())
            .build();

        DiscussionCommentResponse createResponseWrapper = QmsScenarioDiscussionResources.addCommentToDiscussion(scenarioDiscussionResponse.getIdentity(),
            discussionCommentRequest, currentUser);
        softAssertions.assertThat(createResponseWrapper.getContent()).isEqualTo(commentContent);
        softAssertions.assertThat(createResponseWrapper.getCommentView().size()).isGreaterThan(0);

        DiscussionCommentRequest discussionDeleteCommentRequest = DiscussionCommentRequest.builder()
            .comment(DiscussionCommentParameters.builder()
                .content(commentContent)
                .status("DELETED")
                .build())
            .build();

        DiscussionCommentResponse deleteResponseWrapper = QmsScenarioDiscussionResources.updateCommentToDiscussion(scenarioDiscussionResponse.getIdentity(),
            createResponseWrapper.getIdentity(), discussionDeleteCommentRequest, DiscussionCommentResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(deleteResponseWrapper.getStatus()).isEqualTo("DELETED");
    }

    @Test
    @TestRail(id = {15473})
    @Description("Verify that user can GET discussion's comment by identity")
    public void getDiscussionComment() {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSION_COMMENT, DiscussionCommentResponse.class)
            .inlineVariables(scenarioDiscussionResponse.getIdentity(), discussionCommentResponse.getIdentity())
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<DiscussionCommentResponse> responseWrapper = HTTPRequest.build(requestEntity).get();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    @TestRail(id = {14677})
    @Description("Verify that user can FIND list of discussion's comment")
    public void getDiscussionComments() {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSION_COMMENTS, DiscussionCommentsResponse.class)
            .inlineVariables(scenarioDiscussionResponse.getIdentity())
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<DiscussionCommentsResponse> responseWrapper = HTTPRequest.build(requestEntity).get();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(id = {14676})
    @Description("Verify that User can add comment with mentioning user to scenario discussion")
    public void addCommentWithUserToDiscussion() {
        String commentContent = new GenerateStringUtil().generateNotes();
        DiscussionCommentResponse responseWrapper = QmsScenarioDiscussionResources.addCommentToDiscussion(scenarioDiscussionResponse.getIdentity(),
            commentContent, "ACTIVE", currentUser);
        softAssertions.assertThat(responseWrapper.getContent()).isEqualTo(commentContent);
    }

    @Test
    @TestRail(id = {22256})
    @Description("Verify that User will not get 409 error on valid actions after getting this error on invalid action")
    public void verifyScenarioDiscussionNo409ErrorWith2Users() {
        UserCredentials assignedUser = UserUtil.getUser();
        String description = new GenerateStringUtil().generateNotes();
        ScenarioDiscussionRequest scenarioDiscussionRequest = QmsApiTestUtils
            .getScenarioDiscussionRequest(assignedUser, scenarioItem, description);
        ScenarioDiscussionResponse scenarioDiscussionAssigneeResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioDiscussionRequest, currentUser);
        softAssertions.assertThat(scenarioDiscussionAssigneeResponse.getDescription()).isEqualTo(description);

        scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder()
                .assigneeEmail("").build()).build();
        ScenarioDiscussionResponse updateDiscussionResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(scenarioDiscussionAssigneeResponse.getIdentity(),
            scenarioDiscussionRequest,
            ScenarioDiscussionResponse.class,
            HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateDiscussionResponse.getStatus()).isEqualTo("ACTIVE");
        softAssertions.assertThat(updateDiscussionResponse.getAssigneeUserIdentity()).isNull();
        softAssertions.assertThat(updateDiscussionResponse.getAssignee()).isNull();

        scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder()
                .assigneeEmail("").build()).build();
        ApwErrorMessage updateResponseAssigneeUserError = QmsScenarioDiscussionResources.updateScenarioDiscussion(scenarioDiscussionAssigneeResponse.getIdentity(),
            scenarioDiscussionRequest,
            ApwErrorMessage.class,
            HttpStatus.SC_CONFLICT, assignedUser);
        softAssertions.assertThat(updateResponseAssigneeUserError.getMessage())
            .isEqualTo("Can't update discussion. No changes detected.");

        scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder()
                .status("RESOLVED").build()).build();
        ScenarioDiscussionResponse updateResponseAssigneeUser = QmsScenarioDiscussionResources.updateScenarioDiscussion(scenarioDiscussionAssigneeResponse.getIdentity(),
            scenarioDiscussionRequest,
            ScenarioDiscussionResponse.class,
            HttpStatus.SC_OK, assignedUser);
        softAssertions.assertThat(updateResponseAssigneeUser.getStatus()).isEqualTo("RESOLVED");

        QmsScenarioDiscussionResources.deleteScenarioDiscussion(scenarioDiscussionAssigneeResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(id = {22257})
    @Description("Verify that by default, User will GET 300 discussion (PageSize)")
    public void verifyGetDiscussionsPageSizeDefault() {
        QueryParams queryParams = new QueryParams();
        queryParams.put("componentIdentity[EQ]", scenarioItem.getComponentIdentity());
        queryParams.put("scenarioIdentity[EQ]", scenarioItem.getScenarioIdentity());
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSIONS, ScenarioDiscussionsResponse.class)
            .queryParams(queryParams)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ScenarioDiscussionsResponse> responseWrapper = HTTPRequest.build(requestEntity).get();

        softAssertions.assertThat(responseWrapper.getResponseEntity().getPageSize()).isEqualTo(300);
    }

    @Test
    @TestRail(id = {15448})
    @Description("Verify that user can Assign / Un-assign scenario discussion")
    public void assignUnAssignDiscussion() {
        UserCredentials assigneeUser = UserUtil.getUser();
        String description = new GenerateStringUtil().generateNotes();
        ScenarioDiscussionRequest scenarioDiscussionRequest = QmsApiTestUtils
            .getScenarioDiscussionRequest(currentUser, scenarioItem, description);
        ScenarioDiscussionResponse scenarioDiscussionAssigneeResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioDiscussionRequest, currentUser);
        softAssertions.assertThat(scenarioDiscussionAssigneeResponse.getDescription()).isEqualTo(description);

        scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder()
                .assigneeEmail(assigneeUser.getEmail()).build()).build();
        ScenarioDiscussionResponse updateDiscussionResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(scenarioDiscussionAssigneeResponse.getIdentity(),
            scenarioDiscussionRequest,
            ScenarioDiscussionResponse.class,
            HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateDiscussionResponse.getStatus()).isEqualTo("ACTIVE");
        softAssertions.assertThat(updateDiscussionResponse.getAssigneeUserIdentity())
            .isEqualTo(new AuthUserContextUtil().getAuthUserIdentity(assigneeUser.getEmail()));
        softAssertions.assertThat(updateDiscussionResponse.getAssignee().getIdentity())
            .isEqualTo(new AuthUserContextUtil().getAuthUserIdentity(assigneeUser.getEmail()));

        scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder()
                .assigneeEmail("").build()).build();
        updateDiscussionResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(scenarioDiscussionAssigneeResponse.getIdentity(),
            scenarioDiscussionRequest,
            ScenarioDiscussionResponse.class,
            HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateDiscussionResponse.getStatus()).isEqualTo("ACTIVE");
        softAssertions.assertThat(updateDiscussionResponse.getAssigneeUserIdentity()).isNull();
        softAssertions.assertThat(updateDiscussionResponse.getAssignee()).isNull();

        QmsScenarioDiscussionResources.deleteScenarioDiscussion(scenarioDiscussionAssigneeResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(id = {16565})
    @Issue("COL-1824")
    @Description("Verify that User cannot add comments to discussion with RESOLVED status")
    public void verifyCannotAddCommentsResolvedStatus() {
        ScenarioDiscussionResponse csdResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
        softAssertions.assertThat(csdResponse.getIdentity()).isNotNull();

        ScenarioDiscussionRequest scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder().status("RESOLVED").build()).build();
        ScenarioDiscussionResponse updateResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(csdResponse.getIdentity(),
            scenarioDiscussionRequest,
            ScenarioDiscussionResponse.class,
            HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateResponse.getStatus()).isEqualTo("RESOLVED");

        ApwErrorMessage addCommentError = QmsScenarioDiscussionResources.addCommentToDiscussion(csdResponse.getIdentity(), RandomStringUtils.randomAlphabetic(12), ApwErrorMessage.class, HttpStatus.SC_CONFLICT, currentUser);
        softAssertions.assertThat(addCommentError.getStatus()).isEqualTo(HttpStatus.SC_CONFLICT);
        softAssertions.assertThat(addCommentError.getMessage()).contains("User can not update resolved discussion");

        scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder().status("ACTIVE").build()).build();
        updateResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(csdResponse.getIdentity(),
            scenarioDiscussionRequest,
            ScenarioDiscussionResponse.class,
            HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateResponse.getStatus()).isEqualTo("ACTIVE");

        String commentContent = new GenerateStringUtil().generateNotes();
        DiscussionCommentResponse responseWrapper = QmsScenarioDiscussionResources.addCommentToDiscussion(csdResponse.getIdentity(),
            commentContent, "ACTIVE", currentUser);
        softAssertions.assertThat(responseWrapper.getContent()).isEqualTo(commentContent);

        QmsScenarioDiscussionResources.deleteScenarioDiscussion(csdResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(id = {16052, 16051})
    @Issue("COL-1814")
    @Description("Verify that user can DELETE & UNDELETE discussion (Patch Method)")
    public void deleteDiscussionByPatchMethod() {
        ScenarioDiscussionRequest scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder().status("DELETED").build()).build();
        ScenarioDiscussionResponse updateResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(scenarioDiscussionResponse.getIdentity(),
            scenarioDiscussionRequest,
            ScenarioDiscussionResponse.class,
            HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateResponse.getStatus()).isEqualTo("DELETED");

        scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder().status("ACTIVE").build()).build();
        updateResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(scenarioDiscussionResponse.getIdentity(),
            scenarioDiscussionRequest,
            ScenarioDiscussionResponse.class,
            HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateResponse.getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    @TestRail(id = {14649})
    @Description("Verify that User can not change Status of Deleted Discussion")
    public void verifyNoStatusChangeAfterDiscussionDeleted() {
        ScenarioDiscussionResponse csdResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
        softAssertions.assertThat(csdResponse.getIdentity()).isNotNull();
        QmsScenarioDiscussionResources.deleteScenarioDiscussion(csdResponse.getIdentity(), currentUser);

        ScenarioDiscussionRequest scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder().status("ACTIVE").build()).build();
        ApwErrorMessage discussionError = QmsScenarioDiscussionResources.updateScenarioDiscussion(csdResponse.getIdentity(),
            scenarioDiscussionRequest,
            ApwErrorMessage.class,
            HttpStatus.SC_NOT_FOUND, currentUser);
        softAssertions.assertThat(discussionError.getStatus()).isEqualTo(HttpStatus.SC_NOT_FOUND);
        softAssertions.assertThat(discussionError.getMessage())
            .contains(String.format("Discussion with identity %s was not found", csdResponse.getIdentity()));
    }

    @Test
    @TestRail(id = {16056, 14679})
    @Description("Verify that user can Update discussion comment's Status (UnDeleted) and Content")
    public void updateDiscussionCommentStatusAndContent() {
        String commentContent = new GenerateStringUtil().generateNotes();
        DiscussionCommentRequest addCommentRequest = DiscussionCommentRequest.builder()
            .comment(DiscussionCommentParameters.builder()
                .content(commentContent)
                .status("ACTIVE")
                .build())
            .build();

        DiscussionCommentResponse addCommentResponse = QmsScenarioDiscussionResources.addCommentToDiscussion(scenarioDiscussionResponse.getIdentity(),
            addCommentRequest, currentUser);
        softAssertions.assertThat(addCommentResponse.getContent()).isEqualTo(commentContent);
        softAssertions.assertThat(addCommentResponse.getStatus()).isEqualTo("ACTIVE");
        softAssertions.assertThat(addCommentResponse.getCommentView().size()).isGreaterThan(0);

        DiscussionCommentRequest updateCommentRequest = DiscussionCommentRequest.builder()
            .comment(DiscussionCommentParameters.builder()
                .status("DELETED")
                .build())
            .build();

        DiscussionCommentResponse updateCommentResponse = QmsScenarioDiscussionResources.updateCommentToDiscussion(scenarioDiscussionResponse.getIdentity(),
            addCommentResponse.getIdentity(), updateCommentRequest, DiscussionCommentResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateCommentResponse.getStatus()).isEqualTo("DELETED");

        DiscussionCommentRequest updateCommentRequestActive = DiscussionCommentRequest.builder()
            .comment(DiscussionCommentParameters.builder()
                .status("ACTIVE")
                .build())
            .build();

        DiscussionCommentResponse updateCommentResponseActive = QmsScenarioDiscussionResources.updateCommentToDiscussion(scenarioDiscussionResponse.getIdentity(),
            addCommentResponse.getIdentity(), updateCommentRequestActive, DiscussionCommentResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateCommentResponseActive.getStatus()).isEqualTo("ACTIVE");

        String newCommentContent = new GenerateStringUtil().generateNotes();
        DiscussionCommentRequest updateCommentRequestNewContent = DiscussionCommentRequest.builder()
            .comment(DiscussionCommentParameters.builder()
                .content(newCommentContent)
                .build())
            .build();

        DiscussionCommentResponse updateCommentResponseNewContent = QmsScenarioDiscussionResources.updateCommentToDiscussion(scenarioDiscussionResponse.getIdentity(),
            addCommentResponse.getIdentity(), updateCommentRequestNewContent, DiscussionCommentResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateCommentResponseNewContent.getContent()).isEqualTo(newCommentContent);
    }

    @Test
    @TestRail(id = {14686})
    @Description("Verify that mentioned User in the comment will be added as participant")
    public void verifyCommentMentionedUserAsParticipant() {
        UserCredentials mentionedUser = UserUtil.getUser();
        String commentContent = new GenerateStringUtil().generateNotes();
        DiscussionCommentRequest discussionCommentRequest = DiscussionCommentRequest.builder()
            .comment(DiscussionCommentParameters.builder()
                .content(commentContent)
                .mentionedUserEmails(Collections.singletonList(mentionedUser.getEmail()))
                .status("ACTIVE")
                .build())
            .build();

        DiscussionCommentResponse createResponseWrapper = QmsScenarioDiscussionResources.addCommentToDiscussion(scenarioDiscussionResponse.getIdentity(),
            discussionCommentRequest, currentUser);
        softAssertions.assertThat(createResponseWrapper.getMentionedUsers().get(0).getIdentity())
            .isEqualTo(new AuthUserContextUtil().getAuthUserIdentity(mentionedUser.getEmail()));

        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSION, ScenarioDiscussionResponse.class)
            .inlineVariables(scenarioDiscussionResponse.getIdentity())
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ScenarioDiscussionResponse> responseWrapper = HTTPRequest.build(requestEntity).get();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getParticipants().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(responseWrapper.getResponseEntity().getParticipants().stream()
                .anyMatch(p -> p.getUserIdentity()
                    .equals(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail())))).isTrue();
            softAssertions.assertThat(responseWrapper.getResponseEntity().getParticipants().stream()
                .anyMatch(p -> p.getUserIdentity()
                    .equals(new AuthUserContextUtil().getAuthUserIdentity(mentionedUser.getEmail())))).isTrue();
        }
    }

    @Test
    @TestRail(id = {16564})
    @Description("Verify that comment-view will be created for participant who read the comment")
    public void verifyCommentViewCreatedForParticipant() {
        UserCredentials assigneeUser = UserUtil.getUser();
        ScenarioDiscussionRequest scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder()
                .assigneeEmail(assigneeUser.getEmail()).build()).build();
        ScenarioDiscussionResponse updateDiscussionResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(scenarioDiscussionResponse.getIdentity(),
            scenarioDiscussionRequest,
            ScenarioDiscussionResponse.class,
            HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(updateDiscussionResponse.getStatus()).isEqualTo("ACTIVE");
        softAssertions.assertThat(updateDiscussionResponse.getAssigneeUserIdentity())
            .isEqualTo(new AuthUserContextUtil().getAuthUserIdentity(assigneeUser.getEmail()));
        softAssertions.assertThat(updateDiscussionResponse.getAssignee().getIdentity())
            .isEqualTo(new AuthUserContextUtil().getAuthUserIdentity(assigneeUser.getEmail()));

        DiscussionCommentViewResponse commentViewResponse = QmsScenarioDiscussionResources.postScenarioDiscussionCommentViewStatus(
            scenarioDiscussionResponse.getIdentity(),
            discussionCommentResponse.getIdentity(),
            DiscussionCommentViewResponse.class,
            HttpStatus.SC_CREATED,
            assigneeUser);
        softAssertions.assertThat(commentViewResponse.getCommentView().stream()
                .anyMatch(cv -> cv.getUserIdentity()
                    .equals(new AuthUserContextUtil().getAuthUserIdentity(assigneeUser.getEmail()))))
            .isTrue();
    }
}