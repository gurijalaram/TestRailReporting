package utils;

import com.apriori.utils.ErrorMessage;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;

import entity.request.CommentsRequestParameters;
import entity.request.DiscussionsRequest;
import entity.request.DiscussionsRequestParameters;
import entity.request.DmsCommentsRequest;
import entity.response.DiscussionResponse;
import entity.response.DmsCommentResponse;
import entity.response.DmsCommentsResponse;
import enums.DMSApiEnum;
import org.apache.commons.lang3.RandomStringUtils;

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
    public static ResponseWrapper<DiscussionResponse> createDiscussion(String discussionDescription, UserCredentials currentUser) {
        DiscussionsRequest discussionsRequest = DiscussionsRequest.builder()
            .discussion(DiscussionsRequestParameters.builder()
                .status("ACTIVE")
                .description(discussionDescription)
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(DMSApiEnum.CUSTOMER_DISCUSSIONS, DiscussionResponse.class)
            .inlineVariables(PropertiesContext.get("${env}.customer_identity"))
            .headers(setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .body(discussionsRequest);

        return HTTPRequest.build(requestEntity).post();
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
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()));

        return HTTPRequest.build(requestEntity).post();
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
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()));

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
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()));

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
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()));

        return HTTPRequest.build(requestEntity).patch();
    }


    /**
     * Update DMS discussion request with valid test data
     *
     * @param discussionDescription
     * @param status
     * @param discussionIdentity
     * @param currentUser
     * @param klass response class (response class can be DiscussionResponse for valid data
     *              ErrorMessage response class for invalid data
     * @param <T>
     * @return ResponseWrapper of class type
     */
    public static <T> ResponseWrapper<T> updateDiscussion(String discussionDescription, String status, String discussionIdentity, UserCredentials currentUser, Class<T> klass) {
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
            .body(discussionsRequest);

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
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()));

        return HTTPRequest.build(requestEntity).delete();
    }

}
