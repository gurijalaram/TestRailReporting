package utils;

import com.apriori.qms.entity.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.QueryParams;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import entity.request.CommentViewParameters;
import entity.request.CommentsRequestParameters;
import entity.request.DiscussionParticipantParameters;
import entity.request.DiscussionParticipantRequest;
import entity.request.DiscussionsRequest;
import entity.request.DiscussionsRequestParameters;
import entity.request.DmsCommentViewRequest;
import entity.request.DmsCommentsRequest;
import entity.response.DmsCommentResponse;
import entity.response.DmsCommentViewResponse;
import entity.response.DmsCommentViewsResponse;
import entity.response.DmsCommentsResponse;
import entity.response.DmsDiscussionParticipantResponse;
import entity.response.DmsDiscussionParticipantsResponse;
import entity.response.DmsDiscussionResponse;
import enums.DMSApiEnum;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DmsApiTestUtils {

    /**
     * setup header information for DDS API Authorization
     *
     * @return Map
     */
    public static Map<String, String> setUpHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "*/*");
        header.put("Content-Type", "application/json");
        return header;
    }

    /**
     * setup header information for DDS API Authorization
     *
     * @return Map
     */
    public static Map<String, String> setUpHeader(String cloudContext) {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "*/*");
        header.put("Content-Type", "application/json");
        header.put("ap-cloud-context", cloudContext);
        return header;
    }

    /**
     * Delete Discussion
     *
     * @param discussionIdentity - Discussion Identity
     * @param currentUser        - UserCredentials
     * @return ResponseWrapper of String
     */
    public static ResponseWrapper<String> deleteDiscussion(String discussionIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION, null)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity)
            .headers(setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        return HTTPRequest.build(requestEntity).delete();
    }

    /**
     * Get list of all discussions
     *
     * @param responseClass response class name
     * @param httpStatus    expected http status code
     * @param currentUser   UserCredentials
     * @param <T>           response class type
     * @return response class
     */
    public static <T> T getDiscussions(Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSIONS, responseClass)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"))
            .headers(DmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    public static <T> T getScenarioDiscussions(Class<T> responseClass, Integer httpStatus, UserCredentials currentUser, ScenarioDiscussionResponse qmsScenarioDiscussionResponse) {
        QueryParams queryParams = new QueryParams();
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSIONS, responseClass)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"))
            .queryParams(queryParams.use("scenarioDiscussionIdentity[EQ]", qmsScenarioDiscussionResponse.getIdentity()))
            .headers(DmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Get Dicussion Comments
     *
     * @param currentUser
     * @param discussionIdentity
     * @return DmsCommentsResponse
     */
    public static DmsCommentsResponse getDiscussionComments(UserCredentials currentUser, String discussionIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION_COMMENTS, DmsCommentsResponse.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity)
            .headers(DmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        return (DmsCommentsResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Get Discussion Comment
     *
     * @param currentUser
     * @param discussionIdentity
     * @param commentIdentity
     * @return DmsCommentResponse
     */
    public static DmsCommentResponse getDiscussionComment(UserCredentials currentUser, String discussionIdentity, String commentIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION_COMMENT, DmsCommentResponse.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"),
                discussionIdentity, commentIdentity)
            .headers(DmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        return (DmsCommentResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Update discussion comment
     *
     * @param status
     * @param discussionIdentity
     * @param commentIdentity
     * @param currentUser
     * @return DmsCommentResponse
     */
    public static DmsCommentResponse updateComment(String status, String discussionIdentity, String commentIdentity, UserCredentials currentUser) {
        String commentContent = RandomStringUtils.randomAlphabetic(15);
        DmsCommentsRequest dmsCommentsRequest = DmsCommentsRequest.builder()
            .comment(CommentsRequestParameters.builder()
                .status(status)
                .content(commentContent)
                .mentionedUserEmails(Collections.singletonList(currentUser.getEmail()))
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION_COMMENT, DmsCommentResponse.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity, commentIdentity)
            .body(dmsCommentsRequest)
            .headers(DmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        return (DmsCommentResponse) HTTPRequest.build(requestEntity).patch().getResponseEntity();
    }

    /**
     * Update DMS discussion request with valid test data
     *
     * @param discussionDescription
     * @param status
     * @param discussionIdentity
     * @param currentUser
     * @param klass                 response class (response class can be DiscussionResponse for valid data
     *                              ErrorMessage response class for invalid data
     * @param httpStatus
     * @param <T>
     * @return response class object
     */
    public static <T> T updateDiscussion(String discussionDescription, String status, String discussionIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        DiscussionsRequest discussionsRequest = DiscussionsRequest.builder()
            .discussion(DiscussionsRequestParameters.builder()
                .status(status)
                .description(discussionDescription)
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION, klass)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity)
            .headers(DmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .body(discussionsRequest)
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).patch().getResponseEntity();
    }

    /**
     * Update discussion
     *
     * @param discussionsRequestBuilder
     * @param discussionIdentity
     * @param currentUser               UserCredentials
     * @param klass                     expected response class name
     * @param httpStatus                expected status code
     * @param <T>                       response class type
     * @return response class object
     */
    public static <T> T updateDiscussion(DiscussionsRequest discussionsRequestBuilder, String discussionIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION, klass)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity)
            .headers(DmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .body(discussionsRequestBuilder)
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).patch().getResponseEntity();
    }


    /**
     * Delete discussion comment
     *
     * @param discussionIdentity
     * @param commentIdentity
     * @param currentUser
     * @param responseClass      - response class name
     * @param httpStatus         - expected httpstatus
     * @param <T>                - response class type
     * @return response class object
     */
    public static <T> T deleteComment(String discussionIdentity, String commentIdentity, UserCredentials currentUser, Class<T> responseClass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION_COMMENT, responseClass)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity, commentIdentity)
            .headers(setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).delete().getResponseEntity();
    }

    /**
     * Mark comment view as read
     *
     * @param discussionIdentity
     * @param discussionCommentIdentity
     * @param userIdentity
     * @param userCustomerIdentity
     * @param participantIdentity
     * @param currentUser               - UserCredentials
     * @return DmsCommentViewResponse
     */
    public static DmsCommentViewResponse markCommentViewAsRead(String discussionIdentity, String discussionCommentIdentity, String userIdentity, String userCustomerIdentity, String participantIdentity, UserCredentials currentUser) {
        DmsCommentViewRequest dmsCommentViewRequest = DmsCommentViewRequest.builder()
            .commentView(CommentViewParameters.builder()
                .participantIdentity(participantIdentity)
                .userCustomerIdentity(userCustomerIdentity)
                .userIdentity(userIdentity)
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION_COMMENT_VIEWS, DmsCommentViewResponse.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity, discussionCommentIdentity)
            .headers(DmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .body(dmsCommentViewRequest)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_CREATED);
        return (DmsCommentViewResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Get discussion comment views
     *
     * @param discussionIdentity
     * @param commentIdentity
     * @param currentUser        - UserCredentials
     * @return DmsCommentViewsResponse
     */
    public static DmsCommentViewsResponse getDiscussionCommentViews(String discussionIdentity, String commentIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION_COMMENT_VIEWS, DmsCommentViewsResponse.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity, commentIdentity)
            .headers(DmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        return (DmsCommentViewsResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Get discussion comment view by identity
     *
     * @param discussionIdentity
     * @param commentIdentity
     * @param commentViewIdentity
     * @param currentUser         - UserCredentials
     * @return DmsCommentViewResponse
     */
    public static DmsCommentViewResponse getDiscussionCommentView(String discussionIdentity, String commentIdentity, String commentViewIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION_COMMENT_VIEW, DmsCommentViewResponse.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity, commentIdentity, commentViewIdentity)
            .headers(DmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        return (DmsCommentViewResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Delete discussion comment view
     *
     * @param discussionIdentity
     * @param commentIdentity
     * @param commentViewIdentity
     * @param currentUser         - UserCredentials
     * @return ResponseWrapper[String]
     */
    public static ResponseWrapper<String> deleteCommentView(String discussionIdentity, String commentIdentity, String commentViewIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION_COMMENT_VIEW, null)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity, commentIdentity, commentViewIdentity)
            .headers(setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        return HTTPRequest.build(requestEntity).delete();
    }

    /**
     * create discussion
     *
     * @param discussionsRequestBuilder
     * @param currentUser
     * @return ResponseWrapper[DmsDiscussionResponse]
     */
    public static DmsDiscussionResponse createDiscussion(DiscussionsRequest discussionsRequestBuilder, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSIONS, DmsDiscussionResponse.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"))
            .headers(setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .body(discussionsRequestBuilder)
            .expectedResponseCode(HttpStatus.SC_CREATED);

        return (DmsDiscussionResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Create Discussion
     *
     * @param discussionDescription - description
     * @param currentUser           - user context
     * @return DiscussionResponse object
     */
    public static DmsDiscussionResponse createDiscussion(String discussionDescription, UserCredentials currentUser) {
        DiscussionsRequest discussionsRequest = DiscussionsRequest.builder()
            .discussion(DiscussionsRequestParameters.builder()
                .status("ACTIVE")
                .description(discussionDescription)
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSIONS, DmsDiscussionResponse.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"))
            .headers(setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .body(discussionsRequest)
            .expectedResponseCode(HttpStatus.SC_CREATED);

        return (DmsDiscussionResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }


    /**
     * Create Discussion comment
     *
     * @param currentUser        - UserCredentials
     * @param commentDesc        - description
     * @param discussionIdentity - discussion identity
     * @param responseClass      - response class object
     * @param httpStatus         - http status code
     * @return response class object
     */
    public static <T> T addCommentToDiscussion(UserCredentials currentUser, String commentDesc, String discussionIdentity, Class<T> responseClass, Integer httpStatus) {
        DmsCommentsRequest dmsCommentsRequest = DmsCommentsRequest.builder()
            .comment(CommentsRequestParameters.builder()
                .status("ACTIVE")
                .content(commentDesc)
                .mentionedUserEmails(Collections.singletonList(currentUser.getEmail()))
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION_COMMENTS, responseClass)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity)
            .body(dmsCommentsRequest)
            .headers(setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Add comment to discussion.
     *
     * @param dmsCommentsRequestBuilder
     * @param discussionIdentity
     * @param currentUser
     * @return DmsCommentResponse
     */
    public static DmsCommentResponse addCommentToDiscussion(DmsCommentsRequest dmsCommentsRequestBuilder, String discussionIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION_COMMENTS, DmsCommentResponse.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity)
            .body(dmsCommentsRequestBuilder)
            .headers(setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_CREATED);

        return (DmsCommentResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Get list of all discussion participants
     *
     * @param discussionIdentity
     * @param currentUser
     * @return DmsDiscussionParticipantsResponse
     */
    public static DmsDiscussionParticipantsResponse getDiscussionParticipants(String discussionIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION_PARTICIPANTS, DmsDiscussionParticipantsResponse.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity)
            .headers(setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        return (DmsDiscussionParticipantsResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Get Single discussion participant
     *
     * @param discussionIdentity
     * @param participantIdentity
     * @param currentUser
     * @return DmsDiscussionParticipantResponse
     */
    public static DmsDiscussionParticipantResponse getDiscussionParticipant(String discussionIdentity, String participantIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION_PARTICIPANT, DmsDiscussionParticipantResponse.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity, participantIdentity)
            .headers(setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        return (DmsDiscussionParticipantResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Add participant to discussion
     * This is overloaded method having different parameter DiscussionParticipantRequest builder class
     *
     * @param discussionParticipantRequestBuilder
     * @param discussionIdentity
     * @param currentUser
     * @return DmsDiscussionParticipantResponse
     */
    public static DmsDiscussionParticipantResponse addDiscussionParticipant(DiscussionParticipantRequest discussionParticipantRequestBuilder, String discussionIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION_PARTICIPANTS, DmsDiscussionParticipantResponse.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity)
            .headers(setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .body(discussionParticipantRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_CREATED);

        return (DmsDiscussionParticipantResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Add participant to discussion
     * This is overloaded method adds participant to discussion with default value
     *
     * @param discussionIdentity
     * @param currentUser        UserCredentials
     * @param responseClass      response class name
     * @param httpStatus         expected https status code
     * @param <T>                Response class type
     * @return response class object
     */
    public static <T> T addDiscussionParticipant(String discussionIdentity, UserCredentials currentUser, Class<T> responseClass, Integer httpStatus) {
        UserCredentials otherUser = UserUtil.getUser();
        DiscussionParticipantRequest discussionParticipantRequest = DiscussionParticipantRequest.builder()
            .participant(DiscussionParticipantParameters.builder()
                .userIdentity(new AuthUserContextUtil().getAuthUserIdentity(otherUser.getEmail()))
                .userCustomerIdentity(PropertiesContext.get("${env}.customer_identity"))
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION_PARTICIPANTS, responseClass)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity)
            .headers(setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .body(discussionParticipantRequest)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Delete discussion participant
     *
     * @param discussionIdentity
     * @param participantIdentity
     * @param responseClass       response class name
     * @param httpStatus          expected https status code
     * @param <T>                 Response class type
     * @return response class object
     */
    public static <T> T deleteDiscussionParticipant(String discussionIdentity, String participantIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION_PARTICIPANT, responseClass)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity, participantIdentity)
            .headers(setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).delete().getResponseEntity();
    }
}
