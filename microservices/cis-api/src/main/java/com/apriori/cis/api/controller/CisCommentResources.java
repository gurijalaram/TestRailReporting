package com.apriori.cis.api.controller;

import com.apriori.cis.api.enums.CisAPIEnum;
import com.apriori.cis.api.models.request.discussion.InternalCommentRequest;
import com.apriori.cis.api.models.request.discussion.InternalDiscussionComments;
import com.apriori.cis.api.util.CISTestUtil;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.ResponseWrapper;

import java.util.List;

public class CisCommentResources extends CISTestUtil {

    /**
     * create scenario comment request
     *
     * @return InternalCommentRequest
     */
    public static InternalCommentRequest getCommentRequestBuilder(String content, List<String> emails) {
        return InternalCommentRequest.builder()
            .comment(InternalDiscussionComments.builder()
                .status("ACTIVE")
                .content(content)
                .mentionedUserEmails(emails)
                .build())
            .build();
    }

    /**
     * Post internal comment
     *
     * @param internalCommentRequest - internalCommentRequest request builder
     * @param componentIdentity      - Component Identity
     * @param scenarioIdentity       - Scenario Identity
     * @param responseClass          - response class
     * @param httpStatus             - expected http status code
     * @param currentUser            - User
     * @param <T>                    - expected response class type
     * @return - response class object
     */
    public static <T> T createInternalComment(InternalCommentRequest internalCommentRequest, String componentIdentity, String scenarioIdentity, String discussionIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = requestEntityUtil.init(CisAPIEnum.SCENARIO_DISCUSSION_COMMENTS, responseClass)
            .inlineVariables(componentIdentity, scenarioIdentity, discussionIdentity)
            .body(internalCommentRequest)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> commentResponseResponseWrapper = HTTPRequest.build(requestEntity).post();
        return commentResponseResponseWrapper.getResponseEntity();
    }

    /**
     * delete internal comment
     *
     * @param componentIdentity  - Component Identity
     * @param scenarioIdentity   - Scenario Identity
     * @param discussionIdentity - Discussion Identity
     * @param responseClass      - response class
     * @param httpStatus         - expected http status code
     * @param currentUser        - User
     * @param <T>                - expected response class type
     * @return - response class object
     */
    public static <T> T deleteInternalComment(String componentIdentity, String scenarioIdentity, String discussionIdentity, String commentIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = requestEntityUtil.init(CisAPIEnum.SCENARIO_DISCUSSION_COMMENT, responseClass)
            .inlineVariables(componentIdentity, scenarioIdentity, discussionIdentity, commentIdentity)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).delete();
        return responseWrapper.getResponseEntity();
    }

    /**
     * get internal comment
     *
     * @param componentIdentity  - Component Identity
     * @param scenarioIdentity   - Scenario Identity
     * @param discussionIdentity - Discussion Identity
     * @param responseClass      - response class
     * @param httpStatus         - expected http status code
     * @param currentUser        - User
     * @param <T>                - expected response class type
     * @return - response class object
     */
    public static <T> T getInternalComment(String componentIdentity, String scenarioIdentity, String discussionIdentity, String commentIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = requestEntityUtil.init(CisAPIEnum.SCENARIO_DISCUSSION_COMMENT, responseClass)
            .inlineVariables(componentIdentity, scenarioIdentity, discussionIdentity, commentIdentity)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * get all internal comments
     *
     * @param componentIdentity - Component Identity
     * @param scenarioIdentity  - Scenario Identity
     * @param responseClass     - response class
     * @param httpStatus        - expected http status code
     * @param currentUser       - User
     * @param <T>               - expected response class type
     * @return - response class object
     */
    public static <T> T getInternalComments(String componentIdentity, String scenarioIdentity, String discussionIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = requestEntityUtil.init(CisAPIEnum.SCENARIO_DISCUSSION_COMMENTS, responseClass)
            .inlineVariables(componentIdentity, scenarioIdentity, discussionIdentity)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * update internal comment
     *
     * @param internalCommentRequest - internalCommentRequest request builder
     * @param componentIdentity      - Component Identity
     * @param scenarioIdentity       - Scenario Identity
     * @param discussionIdentity     - Discussion Identity
     * @param responseClass          - response class
     * @param httpStatus             - expected http status code
     * @param currentUser            - User
     * @param <T>                    - expected response class type
     * @return - response class object
     */
    public static <T> T updateInternalComment(InternalCommentRequest internalCommentRequest, String componentIdentity, String scenarioIdentity, String discussionIdentity, String commentIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = requestEntityUtil.init(CisAPIEnum.SCENARIO_DISCUSSION_COMMENT, responseClass)
            .inlineVariables(componentIdentity, scenarioIdentity, discussionIdentity, commentIdentity)
            .body(internalCommentRequest)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).patch();
        return responseWrapper.getResponseEntity();
    }
}
