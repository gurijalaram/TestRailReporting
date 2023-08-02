package com.apriori.dms.utils;

import com.apriori.AuthUserContextUtil;
import com.apriori.dms.enums.DMSApiEnum;
import com.apriori.dms.models.request.CommentViewParameters;
import com.apriori.dms.models.request.CommentsRequestParameters;
import com.apriori.dms.models.request.DiscussionParticipantParameters;
import com.apriori.dms.models.request.DiscussionParticipantRequest;
import com.apriori.dms.models.request.DiscussionsRequest;
import com.apriori.dms.models.request.DiscussionsRequestParameters;
import com.apriori.dms.models.request.DmsCommentViewRequest;
import com.apriori.dms.models.request.DmsCommentsRequest;
import com.apriori.dms.models.response.DmsCommentResponse;
import com.apriori.dms.models.response.DmsCommentViewResponse;
import com.apriori.dms.models.response.DmsCommentViewsResponse;
import com.apriori.dms.models.response.DmsCommentsResponse;
import com.apriori.dms.models.response.DmsDiscussionParticipantResponse;
import com.apriori.dms.models.response.DmsDiscussionParticipantsResponse;
import com.apriori.dms.models.response.DmsDiscussionResponse;
import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.QueryParams;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.properties.PropertiesContext;
import com.apriori.qms.models.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Dms api test utils.
 */
public class DmsApiTestUtils {

    /**
     * setup header information for DDS API Authorization
     *
     * @return Map up header
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
     * @param cloudContext the cloud context
     * @return Map up header
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
     * @param <T>           response class type
     * @param responseClass response class name
     * @param httpStatus    expected http status code
     * @param currentUser   UserCredentials
     * @return response class
     */
    public static <T> T getDiscussions(Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSIONS, responseClass)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"))
            .headers(DmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);
        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Gets scenario discussions.
     *
     * @param <T>                           the type parameter
     * @param responseClass                 the response class
     * @param httpStatus                    the http status
     * @param currentUser                   the current user
     * @param qmsScenarioDiscussionResponse the qms scenario discussion response
     * @return the scenario discussions
     */
    public static <T> T getScenarioDiscussions(Class<T> responseClass, Integer httpStatus, UserCredentials currentUser, ScenarioDiscussionResponse qmsScenarioDiscussionResponse) {
        QueryParams queryParams = new QueryParams();
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSIONS, responseClass)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"))
            .queryParams(queryParams.use("scenarioDiscussionIdentity[EQ]", qmsScenarioDiscussionResponse.getIdentity()))
            .headers(DmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Get Dicussion Comments
     *
     * @param currentUser        the current user
     * @param discussionIdentity the discussion identity
     * @return DmsCommentsResponse discussion comments
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
     * @param currentUser        the current user
     * @param discussionIdentity the discussion identity
     * @param commentIdentity    the comment identity
     * @return DmsCommentResponse discussion comment
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
     * @param status             the status
     * @param discussionIdentity the discussion identity
     * @param commentIdentity    the comment identity
     * @param currentUser        the current user
     * @return DmsCommentResponse dms comment response
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
     * @param <T>                   the type parameter
     * @param discussionDescription the discussion description
     * @param status                the status
     * @param discussionIdentity    the discussion identity
     * @param currentUser           the current user
     * @param klass                 response class (response class can be DiscussionResponse for valid data                              ErrorMessage response class for invalid data
     * @param httpStatus            the http status
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

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).patch();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Update discussion
     *
     * @param <T>                       response class type
     * @param discussionsRequestBuilder the discussions request builder
     * @param discussionIdentity        the discussion identity
     * @param currentUser               UserCredentials
     * @param klass                     expected response class name
     * @param httpStatus                expected status code
     * @return response class object
     */
    public static <T> T updateDiscussion(DiscussionsRequest discussionsRequestBuilder, String discussionIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION, klass)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity)
            .headers(DmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .body(discussionsRequestBuilder)
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).patch();
        return responseWrapper.getResponseEntity();
    }


    /**
     * Delete discussion comment
     *
     * @param <T>                - response class type
     * @param discussionIdentity the discussion identity
     * @param commentIdentity    the comment identity
     * @param currentUser        the current user
     * @param responseClass      - response class name
     * @param httpStatus         - expected httpstatus
     * @return response class object
     */
    public static <T> T deleteComment(String discussionIdentity, String commentIdentity, UserCredentials currentUser, Class<T> responseClass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION_COMMENT, responseClass)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity, commentIdentity)
            .headers(setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).delete();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Mark comment view as read
     *
     * @param discussionIdentity        the discussion identity
     * @param discussionCommentIdentity the discussion comment identity
     * @param userIdentity              the user identity
     * @param userCustomerIdentity      the user customer identity
     * @param participantIdentity       the participant identity
     * @param currentUser               - UserCredentials
     * @return DmsCommentViewResponse dms comment view response
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
     * @param discussionIdentity the discussion identity
     * @param commentIdentity    the comment identity
     * @param currentUser        - UserCredentials
     * @return DmsCommentViewsResponse discussion comment views
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
     * @param discussionIdentity  the discussion identity
     * @param commentIdentity     the comment identity
     * @param commentViewIdentity the comment view identity
     * @param currentUser         - UserCredentials
     * @return DmsCommentViewResponse discussion comment view
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
     * @param discussionIdentity  the discussion identity
     * @param commentIdentity     the comment identity
     * @param commentViewIdentity the comment view identity
     * @param currentUser         - UserCredentials
     * @return ResponseWrapper[String] response wrapper
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
     * @param discussionsRequestBuilder the discussions request builder
     * @param currentUser               the current user
     * @return ResponseWrapper[DmsDiscussionResponse] dms discussion response
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
     * @param <T>                the type parameter
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

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Add comment to discussion.
     *
     * @param dmsCommentsRequestBuilder the dms comments request builder
     * @param discussionIdentity        the discussion identity
     * @param currentUser               the current user
     * @return DmsCommentResponse dms comment response
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
     * @param discussionIdentity the discussion identity
     * @param currentUser        the current user
     * @return DmsDiscussionParticipantsResponse discussion participants
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
     * @param discussionIdentity  the discussion identity
     * @param participantIdentity the participant identity
     * @param currentUser         the current user
     * @return DmsDiscussionParticipantResponse discussion participant
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
     * @param discussionParticipantRequestBuilder the discussion participant request builder
     * @param discussionIdentity                  the discussion identity
     * @param currentUser                         the current user
     * @return DmsDiscussionParticipantResponse dms discussion participant response
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
     * @param <T>                Response class type
     * @param discussionIdentity the discussion identity
     * @param currentUser        UserCredentials
     * @param responseClass      response class name
     * @param httpStatus         expected https status code
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

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Delete discussion participant
     *
     * @param <T>                 Response class type
     * @param discussionIdentity  the discussion identity
     * @param participantIdentity the participant identity
     * @param responseClass       response class name
     * @param httpStatus          expected https status code
     * @param currentUser         the current user
     * @return response class object
     */
    public static <T> T deleteDiscussionParticipant(String discussionIdentity, String participantIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION_PARTICIPANT, responseClass)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity, participantIdentity)
            .headers(setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).delete();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Delete discussion project item.
     *
     * @param <T>                 the type parameter
     * @param projectItemIdentity the project item identity
     * @param responseClass       the response class
     * @param currentUser         the current user
     * @param httpStatus          the http status
     * @return the t
     */
    public static <T> T deleteDiscussionProjectItem(String projectItemIdentity, Class<T> responseClass, UserCredentials currentUser, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION_PROJECT_ITEM, responseClass)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), projectItemIdentity)
            .headers(setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> deleteResponse = HTTPRequest.build(requestEntity).delete();
        return deleteResponse.getResponseEntity();
    }
}
