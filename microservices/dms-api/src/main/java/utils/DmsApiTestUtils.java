package utils;

import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;

import entity.request.CommentViewParameters;
import entity.request.CommentsRequestParameters;
import entity.request.DiscussionsRequest;
import entity.request.DiscussionsRequestParameters;
import entity.request.DmsCommentViewRequest;
import entity.request.DmsCommentsRequest;
import entity.response.DmsCommentResponse;
import entity.response.DmsCommentViewResponse;
import entity.response.DmsCommentViewsResponse;
import entity.response.DmsCommentsResponse;
import entity.response.DmsDiscussionResponse;
import entity.response.DmsDiscussionsResponse;
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
     * Create Discussion
     *
     * @param discussionDescription - description
     * @param currentUser           - user context
     * @return ResponseWrapper of DiscussionResponse object
     */
    public static ResponseWrapper<DmsDiscussionResponse> createDiscussion(String discussionDescription, UserCredentials currentUser) {
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

        return HTTPRequest.build(requestEntity).post();
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
     * Create Discussion comment
     *
     * @param commentDesc        - description
     * @param discussionIdentity - discussion identity
     * @param currentUser        - UserCredentials
     * @return - ResponseWrapper of CommentResponse object
     */
    public static ResponseWrapper<DmsCommentResponse> createDiscussionComment(UserCredentials currentUser, String commentDesc, String discussionIdentity) {
        DmsCommentsRequest dmsCommentsRequest = DmsCommentsRequest.builder()
            .comment(CommentsRequestParameters.builder()
                .status("ACTIVE")
                .content(commentDesc)
                .mentionedUserEmails(Collections.singletonList(currentUser.getEmail())).build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION_COMMENTS, DmsCommentResponse.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity)
            .body(dmsCommentsRequest)
            .headers(setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_CREATED);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Get list of all discussions
     * @param currentUser - UserCredentials
     * @return ResponseWrapper<DmsDiscussionsResponse>
     */
    public static ResponseWrapper<DmsDiscussionsResponse> getDiscussions(UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSIONS, DmsDiscussionsResponse.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"))
            .headers(DmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Get Dicussion Comments
     *
     * @param currentUser
     * @param discussionIdentity
     * @return ResponseWrapper<DmsCommentsResponse>
     */
    public static ResponseWrapper<DmsCommentsResponse> getDiscussionComments(UserCredentials currentUser, String discussionIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION_COMMENTS, DmsCommentsResponse.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity)
            .headers(DmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Get Discussion Comment
     *
     * @param currentUser
     * @param discussionIdentity
     * @param commentIdentity
     * @return ResponseWrapper<DmsCommentResponse>
     */
    public static ResponseWrapper<DmsCommentResponse> getDiscussionComment(UserCredentials currentUser, String discussionIdentity, String commentIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION_COMMENT, DmsCommentResponse.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"),
                discussionIdentity, commentIdentity)
            .headers(DmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Update discussion comment
     *
     * @param status
     * @param discussionIdentity
     * @param commentIdentity
     * @param currentUser
     * @return ResponseWrapper<DmsCommentResponse>
     */
    public static ResponseWrapper<DmsCommentResponse> updateComment(String status, String discussionIdentity, String commentIdentity, UserCredentials currentUser) {
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

        return HTTPRequest.build(requestEntity).patch();
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
     * @return ResponseWrapper of class type
     */
    public static <T> ResponseWrapper<T> updateDiscussion(String discussionDescription, String status, String discussionIdentity, UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
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

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * Delete Discussion comment
     *
     * @param discussionIdentity
     * @param commentIdentity
     * @param currentUser        - object of UserCredentials class
     * @return ResponseWrapper<String>
     */
    public static ResponseWrapper<String> deleteComment(String discussionIdentity, String commentIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION_COMMENT, null)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity, commentIdentity)
            .headers(setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        return HTTPRequest.build(requestEntity).delete();
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
     * @return ResponseWrapper<DmsCommentViewResponse>
     */
    public static ResponseWrapper<DmsCommentViewResponse> markCommentViewAsRead(String discussionIdentity, String discussionCommentIdentity, String userIdentity, String userCustomerIdentity, String participantIdentity, UserCredentials currentUser) {
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
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Get discussion comment views
     *
     * @param discussionIdentity
     * @param commentIdentity
     * @param currentUser        - UserCredentials
     * @return ResponseWrapper<DmsCommentViewsResponse>
     */
    public static ResponseWrapper<DmsCommentViewsResponse> getDiscussionCommentViews(String discussionIdentity, String commentIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION_COMMENT_VIEWS, DmsCommentViewsResponse.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity, commentIdentity)
            .headers(DmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Get discussion comment view by identity
     *
     * @param discussionIdentity
     * @param commentIdentity
     * @param commentViewIdentity
     * @param currentUser         - UserCredentials
     * @return ResponseWrapper<DmsCommentViewResponse>
     */
    public static ResponseWrapper<DmsCommentViewResponse> getDiscussionCommentView(String discussionIdentity, String commentIdentity, String commentViewIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION_COMMENT_VIEW, DmsCommentViewResponse.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity, commentIdentity, commentViewIdentity)
            .headers(DmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Delete discussion comment view
     *
     * @param discussionIdentity
     * @param commentIdentity
     * @param commentViewIdentity
     * @param currentUser         - UserCredentials
     * @return ResponseWrapper<String>
     */
    public static ResponseWrapper<String> deleteCommentView(String discussionIdentity, String commentIdentity, String commentViewIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSION_COMMENT_VIEW, null)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"), discussionIdentity, commentIdentity, commentViewIdentity)
            .headers(setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        return HTTPRequest.build(requestEntity).delete();
    }
}
