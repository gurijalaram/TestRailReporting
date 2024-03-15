package com.apriori.dds.api.utils;

import com.apriori.dds.api.enums.DDSApiEnum;
import com.apriori.dds.api.models.request.CommentsRequest;
import com.apriori.dds.api.models.request.DiscussionsRequest;
import com.apriori.dds.api.models.request.DiscussionsRequestParameters;
import com.apriori.dds.api.models.response.DiscussionResponse;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.AuthUserContextUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.properties.PropertiesContext;

import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class DdsApiTestUtils {

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
     * Create Discussion
     *
     * @param discussionDesc - description
     * @param userContext    - user context
     * @return ResponseWrapper of DiscussionResponse object
     */
    public static ResponseWrapper<DiscussionResponse> createDiscussion(String discussionDesc, String userContext) {
        DiscussionsRequest discussionsRequest = DiscussionsRequest.builder()
            .discussion(DiscussionsRequestParameters.builder()
                .status("ACTIVE")
                .description(discussionDesc)
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil_Old.init(DDSApiEnum.CUSTOMER_DISCUSSIONS, DiscussionResponse.class)
            .inlineVariables(PropertiesContext.get("${customer}.${env}.customer_identity"))
            .headers(setUpHeader())
            .body(discussionsRequest)
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_CREATED);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Delete Discussion
     *
     * @param discussionIdentity - Discussion Identity
     * @param userContext        - user context
     * @return ResponseWrapper of String
     */
    public static ResponseWrapper<String> deleteDiscussion(String discussionIdentity, String userContext) {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(DDSApiEnum.CUSTOMER_DISCUSSION, null)
            .inlineVariables(PropertiesContext.get("${customer}.${env}.customer_identity"), discussionIdentity)
            .headers(setUpHeader())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        return HTTPRequest.build(requestEntity).delete();
    }

    /**
     *
     * @param commentsRequestBuilder
     * @param discussionIdentity
     * @param currentUser - UserCredentials class object
     * @param responseClass - response Class Name
     * @param httpStatus - Http status code
     * @param <T> - Response class Type
     * @return ResponseWrapper of response class object
     */
    public static <T> ResponseWrapper<T> createComment(CommentsRequest commentsRequestBuilder, String discussionIdentity, UserCredentials currentUser, Class<T> responseClass, Integer httpStatus) {

        RequestEntity requestEntity = RequestEntityUtil_Old.init(DDSApiEnum.CUSTOMER_DISCUSSION_COMMENTS, responseClass)
            .inlineVariables(PropertiesContext.get("${customer}.${env}.customer_identity"), discussionIdentity)
            .body(commentsRequestBuilder)
            .headers(DdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Delete discussion Comment
     *
     * @param discussionIdentity - discussion identity
     * @param userContext        - user context
     * @return - ResponseWrapper of string object
     */
    public static ResponseWrapper<String> deleteComment(String discussionIdentity, String commentIdentity, String userContext) {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(DDSApiEnum.CUSTOMER_DISCUSSION_COMMENT, null)
            .inlineVariables(PropertiesContext.get("${customer}.${env}.customer_identity"), discussionIdentity, commentIdentity)
            .headers(setUpHeader())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        return HTTPRequest.build(requestEntity).delete();
    }
}
