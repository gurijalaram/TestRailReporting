package com.apriori.qms.tests;


import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.controller.QmsScenarioDiscussionResources;
import com.apriori.qms.entity.request.scenariodiscussion.Attributes;
import com.apriori.qms.entity.request.scenariodiscussion.DiscussionCommentParameters;
import com.apriori.qms.entity.request.scenariodiscussion.DiscussionCommentRequest;
import com.apriori.qms.entity.request.scenariodiscussion.ScenarioDiscussionParameters;
import com.apriori.qms.entity.request.scenariodiscussion.ScenarioDiscussionRequest;
import com.apriori.qms.entity.response.bidpackage.BidPackageItemResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageResponse;
import com.apriori.qms.entity.response.scenariodiscussion.DiscussionCommentResponse;
import com.apriori.qms.entity.response.scenariodiscussion.DiscussionCommentsResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioDiscussionsResponse;
import com.apriori.qms.enums.QMSAPIEnum;
import com.apriori.sds.entity.response.Scenario;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.utils.ErrorMessage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.QueryParams;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.QmsApiTestUtils;

import java.util.HashSet;

public class ScenarioDiscussionTest extends SDSTestUtil {
    private static String bidPackageName;
    private static String projectName;
    private static ScenarioItem scenarioItem;
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageItemResponse bidPackageItemResponse;
    private static BidPackageProjectResponse bidPackageProjectResponse;
    private static SoftAssertions softAssertions;
    private static ScenarioDiscussionResponse scenarioDiscussionResponse;
    private static DiscussionCommentResponse discussionCommentResponse;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        projectName = "PROJ" + new GenerateStringUtil().getRandomNumbers();

        // Create new Component and published Scenario via SDS
        scenarioItem = postTestingComponentAndAddToRemoveList();
        publishAssembly(ComponentInfoBuilder.builder().scenarioName(scenarioItem.getScenarioName()).user(testingUser)
            .componentIdentity(scenarioItem.getComponentIdentity()).scenarioIdentity(scenarioItem.getScenarioIdentity())
            .build(), Scenario.class, HttpStatus.SC_OK);

        //Create new bid-package & project
        bidPackageResponse = QmsBidPackageResources.createBidPackage(bidPackageName, currentUser);
        bidPackageItemResponse = QmsBidPackageResources.createBidPackageItem(
            QmsBidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bidPackageResponse.getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_CREATED);
        bidPackageProjectResponse = QmsBidPackageResources.createBidPackageProject(projectName, bidPackageResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);

        //Create Scenario Discussion and Comment
        scenarioDiscussionResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
        discussionCommentResponse = QmsScenarioDiscussionResources.addCommentToDiscussion(scenarioDiscussionResponse.getIdentity(),
            new GenerateStringUtil().generateNotes(), "ACTIVE", currentUser);
    }

    @Test
    @TestRail(testCaseId = {"14608", "14613"})
    @Description("Create and delete Scenario Discussion")
    public void createAndDeleteScenarioDiscussion() {
        ScenarioDiscussionResponse csdResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
        softAssertions.assertThat(csdResponse.getIdentity()).isNotNull();
        QmsScenarioDiscussionResources.deleteScenarioDiscussion(csdResponse.getIdentity(), currentUser);
    }

    @Test
    @TestRail(testCaseId = {"14610"})
    @Description("Get Scenario Discussion by identity")
    public void getScenarioDiscussion() {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSION, ScenarioDiscussionResponse.class)
            .inlineVariables(scenarioDiscussionResponse.getIdentity())
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ScenarioDiscussionResponse> responseWrapper = HTTPRequest.build(requestEntity).get();

        softAssertions.assertThat(responseWrapper.getResponseEntity().getParticipants().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"14609"})
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
    @TestRail(testCaseId = {"14611"})
    @Description("Verify that User can update Scenario discussion description")
    public void updateScenarioDiscussionDescription() {
        String description = new GenerateStringUtil().generateNotes();
        ScenarioDiscussionRequest scenarioDiscussionRequest = QmsScenarioDiscussionResources.getScenarioDiscussionRequestBuilder(scenarioItem.getComponentIdentity(), scenarioDiscussionResponse.getIdentity());
        scenarioDiscussionRequest.getScenarioDiscussion().setDescription(description);

        ScenarioDiscussionResponse updateResponse = QmsScenarioDiscussionResources.updateScenarioDiscussion(scenarioDiscussionResponse.getIdentity(),
            scenarioDiscussionRequest,
            ScenarioDiscussionResponse.class,
            HttpStatus.SC_OK, currentUser);

        softAssertions.assertThat(updateResponse.getDescription()).isEqualTo(description);
    }

    @Test
    @TestRail(testCaseId = {"14675", "14678", "15477"})
    @Description("Verify that User can add comment, update status to delete to scenario discussion" +
        "and verify commentview is created for the same user")
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
    @TestRail(testCaseId = {"15473"})
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
    @TestRail(testCaseId = {"14677"})
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
    @TestRail(testCaseId = {"14676"})
    @Description("Verify that User can add comment with mentioning user to scenario discussion")
    public void addCommentWithUserToDiscussion() {
        String commentContent = new GenerateStringUtil().generateNotes();
        DiscussionCommentResponse responseWrapper = QmsScenarioDiscussionResources.addCommentToDiscussion(scenarioDiscussionResponse.getIdentity(),
            commentContent, "ACTIVE", currentUser);

        softAssertions.assertThat(responseWrapper.getContent()).isEqualTo(commentContent);
    }

    @Test
    @TestRail(testCaseId = {"16057", "14672"})
    @Description("Create, get scenario discussion with assignee user and useridentity")
    public void getScenarioDiscussionsByUserIdentity() {
        UserCredentials assignedUser = UserUtil.getUser();
        if (assignedUser.getEmail().equals(currentUser.getEmail())) {
            assignedUser = UserUtil.getUser();
        }

        //Create Discussion with assignee
        String description = new GenerateStringUtil().generateNotes();
        ScenarioDiscussionRequest scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder()
                .status("ACTIVE")
                .type("SCENARIO")
                .assigneeEmail(assignedUser.getEmail())
                .description(description)
                .componentIdentity(scenarioItem.getComponentIdentity())
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .attributes(Attributes.builder()
                    .attribute("materialName")
                    .subject("4056-23423-003")
                    .build())
                .build())
            .build();

        ScenarioDiscussionResponse scenarioDiscussionAssigneeResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioDiscussionRequest, currentUser);
        softAssertions.assertThat(scenarioDiscussionAssigneeResponse.getDescription()).isEqualTo(description);

        //Add comment to assigned discussion
        QmsScenarioDiscussionResources.addCommentToDiscussion(scenarioDiscussionAssigneeResponse.getIdentity(), new GenerateStringUtil().generateNotes(), "ACTIVE", currentUser);

        //Get scenario discussion with assignee useridentity
        String[] params = {"pageNumber,1", "status[NE],DELETED", "assignee.userIdentity[IN]," + scenarioDiscussionAssigneeResponse.getAssigneeUserIdentity(), "sortBy[DESC],createdAt"};
        ScenarioDiscussionsResponse responseWrapper = QmsScenarioDiscussionResources.getFilteredScenarioDiscussions(currentUser, params);
        softAssertions.assertThat(responseWrapper.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"14673"})
    @Description("Create, get scenario discussion with mentioned user profile identity")
    public void getScenarioDiscussionsByMentionedUserIdentity() {
        String[] params = {"mentionedUsers.userIdentity[EQ]," + scenarioDiscussionResponse.getParticipants().get(0)
            .getUserIdentity(), "pageNumber,1", "status[NE],DELETED"};
        ScenarioDiscussionsResponse responseWrapper = QmsScenarioDiscussionResources.getFilteredScenarioDiscussions(currentUser, params);
        softAssertions.assertThat(responseWrapper.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"14674"})
    @Description("Verify that user can GET a list of all Unresolved discussions")
    public void getUnresolvedScenarioDiscussions() {
        ScenarioDiscussionsResponse responseWrapper = QmsScenarioDiscussionResources.getFilteredScenarioDiscussions(currentUser, "pageNumber,1", "status[NE],DELETED", "status[IN],ACTIVE");
        softAssertions.assertThat(responseWrapper.getItems().size()).isGreaterThan(0);
    }

    @After
    public void testCleanup() {
        //Delete Scenario Discussion
        QmsScenarioDiscussionResources.deleteScenarioDiscussion(scenarioDiscussionResponse.getIdentity(), currentUser);

        //Delete Bidpackage and Scenario
        QmsBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        if (!scenariosToDelete.isEmpty()) {
            scenariosToDelete.forEach(component -> {
                removeTestingScenario(component.getComponentIdentity(), component.getScenarioIdentity());
            });
        }
        scenariosToDelete = new HashSet<>();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"22256"})
    @Description("Verify that User will not get 409 error on valid actions after getting this error on invalid action")
    public void verifyScenarioDiscussionNo409ErrorWith2Users() {
        UserCredentials assignedUser = UserUtil.getUser();
        if (assignedUser.getEmail().equals(currentUser.getEmail())) {
            assignedUser = UserUtil.getUser();
        }

        //User_A and User_B must be participants of this discussion
        //one of the users must be assignee of this discussion
        String description = new GenerateStringUtil().generateNotes();
        ScenarioDiscussionRequest scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder()
                .status("ACTIVE")
                .type("SCENARIO")
                .assigneeEmail(assignedUser.getEmail())
                .description(description)
                .componentIdentity(scenarioItem.getComponentIdentity())
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .attributes(Attributes.builder()
                    .attribute("materialName")
                    .subject("4056-23423-003")
                    .build())
                .build())
            .build();

        ScenarioDiscussionResponse scenarioDiscussionAssigneeResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioDiscussionRequest, currentUser);
        softAssertions.assertThat(scenarioDiscussionAssigneeResponse.getDescription()).isEqualTo(description);

        //As the User_A - unassign the discussion
        scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder()
                .assigneeEmail("").build()).build();
        ScenarioDiscussionResponse updateResponseCurrentUser = QmsScenarioDiscussionResources.updateScenarioDiscussion(scenarioDiscussionAssigneeResponse.getIdentity(),
            scenarioDiscussionRequest,
            ScenarioDiscussionResponse.class,
            HttpStatus.SC_OK, currentUser);

        softAssertions.assertThat(updateResponseCurrentUser.getStatus()).isEqualTo("ACTIVE");
        softAssertions.assertThat(updateResponseCurrentUser.getAssigneeUserIdentity()).isNull();
        softAssertions.assertThat(updateResponseCurrentUser.getAssignee()).isNull();

        //As the User_B try to unassign same discussion again
        scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder()
                .assigneeEmail("").build()).build();
        ErrorMessage updateResponseAssigneeUserError = QmsScenarioDiscussionResources.updateScenarioDiscussion(scenarioDiscussionAssigneeResponse.getIdentity(),
            scenarioDiscussionRequest,
            ErrorMessage.class,
            HttpStatus.SC_CONFLICT, assignedUser);

        softAssertions.assertThat(updateResponseAssigneeUserError.getMessage())
            .isEqualTo("Can't update discussion. No changes detected.");

        //As the User_B RESOLVE this discussion
        scenarioDiscussionRequest = ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder()
                .status("RESOLVED").build()).build();
        ScenarioDiscussionResponse updateResponseAssigneeUser = QmsScenarioDiscussionResources.updateScenarioDiscussion(scenarioDiscussionAssigneeResponse.getIdentity(),
            scenarioDiscussionRequest,
            ScenarioDiscussionResponse.class,
            HttpStatus.SC_OK, assignedUser);

        softAssertions.assertThat(updateResponseAssigneeUser.getStatus()).isEqualTo("RESOLVED");
    }

    @Test
    @TestRail(testCaseId = {"22257"})
    @Description("Verify that by default, User will GET 300 discussion (PageSize)")
    public void verifyPageSizeDefault() {
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

    private static final UserCredentials currentUser = testingUser;
}