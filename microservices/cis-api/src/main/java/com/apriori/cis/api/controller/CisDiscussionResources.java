package com.apriori.cis.api.controller;

import com.apriori.cis.api.enums.CisAPIEnum;
import com.apriori.cis.api.models.request.discussion.InternalDiscussionAttributes;
import com.apriori.cis.api.models.request.discussion.InternalDiscussionComments;
import com.apriori.cis.api.models.request.discussion.InternalDiscussionParameters;
import com.apriori.cis.api.models.request.discussion.InternalDiscussionRequest;
import com.apriori.cis.api.models.request.discussion.InternalScenarioUserRequest;
import com.apriori.cis.api.util.CISTestUtil;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;

import java.util.ArrayList;
import java.util.Collections;

public class CisDiscussionResources extends CISTestUtil {

    /**
     * create scenario discussion request
     *
     * @return InternalDiscussionRequest
     */
    public static InternalDiscussionRequest getDiscussionRequestBuilder() {
        return InternalDiscussionRequest.builder()
            .discussion(InternalDiscussionParameters.builder()
                .type("SCENARIO_FIELD")
                .status("ACTIVE")
                .description("testDescription")
                .attributes(InternalDiscussionAttributes.builder()
                    .attribute("analysisOfScenario.sgaCost")
                    .attributeDisplayValue("SG&A")
                    .subject("2MM-LOW-001b")
                    .build())
                .comments(Collections.singletonList(InternalDiscussionComments.builder()
                    .status("ACTIVE")
                    .content(new GenerateStringUtil().getRandomString())
                    .mentionedUserEmails(new ArrayList<>())
                    .build()))
                .build())
            .build();
    }

    /**
     * Post internal discussion
     *
     * @param internalDiscussionRequest - InternalDiscussionRequest request builder
     * @param componentIdentity         - Component Identity
     * @param scenarioIdentity          - Scenario Identity
     * @param responseClass             - response class
     * @param httpStatus                - expected http status code
     * @param currentUser               - User
     * @param <T>                       - expected response class type
     * @return - response class object
     */
    public static <T> T createInternalDiscussion(InternalDiscussionRequest internalDiscussionRequest, String componentIdentity, String scenarioIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = requestEntityUtil.init(CisAPIEnum.SCENARIO_DISCUSSIONS, responseClass)
            .inlineVariables(componentIdentity, scenarioIdentity)
            .body(internalDiscussionRequest)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }

    /**
     * delete internal discussion
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
    public static <T> T deleteInternalDiscussion(String componentIdentity, String scenarioIdentity, String discussionIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = requestEntityUtil.init(CisAPIEnum.SCENARIO_DISCUSSION, responseClass)
            .inlineVariables(componentIdentity, scenarioIdentity, discussionIdentity)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).delete();
        return responseWrapper.getResponseEntity();
    }

    /**
     * get internal discussion
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
    public static <T> T getInternalDiscussion(String componentIdentity, String scenarioIdentity, String discussionIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = requestEntityUtil.init(CisAPIEnum.SCENARIO_DISCUSSION, responseClass)
            .inlineVariables(componentIdentity, scenarioIdentity, discussionIdentity)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * get all internal discussions
     *
     * @param componentIdentity - Component Identity
     * @param scenarioIdentity  - Scenario Identity
     * @param responseClass     - response class
     * @param httpStatus        - expected http status code
     * @param currentUser       - User
     * @param <T>               - expected response class type
     * @return - response class object
     */
    public static <T> T getInternalDiscussions(String componentIdentity, String scenarioIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = requestEntityUtil.init(CisAPIEnum.SCENARIO_DISCUSSIONS, responseClass)
            .inlineVariables(componentIdentity, scenarioIdentity)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * update internal discussion
     *
     * @param internalDiscussionRequest - InternalDiscussionRequest request builder
     * @param componentIdentity         - Component Identity
     * @param scenarioIdentity          - Scenario Identity
     * @param discussionIdentity        - Discussion Identity
     * @param responseClass             - response class
     * @param httpStatus                - expected http status code
     * @param currentUser               - User
     * @param <T>                       - expected response class type
     * @return - response class object
     */
    public static <T> T updateInternalDiscussion(InternalDiscussionRequest internalDiscussionRequest, String componentIdentity, String scenarioIdentity, String discussionIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = requestEntityUtil.init(CisAPIEnum.SCENARIO_DISCUSSION, responseClass)
            .inlineVariables(componentIdentity, scenarioIdentity, discussionIdentity)
            .body(internalDiscussionRequest)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).patch();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Get user discussions
     *
     * @param responseClass - expected response class
     * @param httpStatus    - expected http status code
     * @param currentUser   - User
     * @param <T>           - expected response class type
     * @return - response class object
     */
    public static <T> T getUserDiscussions(Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = requestEntityUtil.init(CisAPIEnum.USER_DISCUSSIONS, responseClass)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Add project user to scenario
     *
     * @param <T>                  - response class object
     * @param projectUserInfoList  - Project users list
     * @param componentInfoBuilder - ComponentInfoBuilder class object
     * @param responseClass        - expected response class
     * @param httpStatus           - expected http status code
     * @return - expected response class type
     */
    public static <T> T addScenarioUser(InternalScenarioUserRequest projectUserInfoList, ComponentInfoBuilder componentInfoBuilder, Class<T> responseClass, Integer httpStatus) {
        RequestEntity requestEntity = requestEntityUtil.init(CisAPIEnum.INTERNAL_SCENARIO_USERS, responseClass)
            .inlineVariables(componentInfoBuilder.getComponentIdentity(), componentInfoBuilder.getScenarioIdentity())
            .body(projectUserInfoList)
            .token(componentInfoBuilder.getUser().getToken())
            .expectedResponseCode(httpStatus);
        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }

    /**
     * get Internal scenario users
     *
     * @param <T>                  - Expected response class type
     * @param componentInfoBuilder - ComponentInfoBuilder
     * @param klass                - expected response class
     * @param httpStatus           - expected http status code
     * @return - expected response class
     */
    public static <T> T getInternalScenarioUsers(ComponentInfoBuilder componentInfoBuilder, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = requestEntityUtil.init(CisAPIEnum.INTERNAL_SCENARIO_USERS, klass)
            .inlineVariables(componentInfoBuilder.getComponentIdentity(), componentInfoBuilder.getScenarioIdentity())
            .token(componentInfoBuilder.getUser().getToken())
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Delete internal scenario users
     *
     * @param projectUserInfoList  - Internal project users list
     * @param componentInfoBuilder - ComponentInfoBuilder
     * @param responseClass        - expected response class
     * @param httpStatus           - expected http status code
     * @param <T>                  - expected response class type
     * @return - expected response class
     */
    public static <T> T deleteScenarioUsers(InternalScenarioUserRequest projectUserInfoList, ComponentInfoBuilder componentInfoBuilder, Class<T> responseClass, Integer httpStatus) {
        RequestEntity requestEntity = requestEntityUtil.init(CisAPIEnum.INTERNAL_SCENARIO_USERS, responseClass)
            .inlineVariables(componentInfoBuilder.getComponentIdentity(), componentInfoBuilder.getScenarioIdentity())
            .body(projectUserInfoList)
            .token(componentInfoBuilder.getUser().getToken())
            .expectedResponseCode(httpStatus);
        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).delete();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Delete single scenario user
     *
     * @param <T>                  - expected response class type
     * @param componentInfoBuilder - ComponentInfoBuilder
     * @param userIdentity         - user identity to delete
     * @param responseClass        - expected response class
     * @param httpStatus           - expected http status code
     * @return - expected response class
     */
    public static <T> T deleteScenarioUser(ComponentInfoBuilder componentInfoBuilder, String userIdentity, Class<T> responseClass, Integer httpStatus) {
        RequestEntity requestEntity = requestEntityUtil.init(CisAPIEnum.INTERNAL_SCENARIO_USER, responseClass)
            .inlineVariables(componentInfoBuilder.getComponentIdentity(), componentInfoBuilder.getScenarioIdentity(), userIdentity)
            .token(componentInfoBuilder.getUser().getToken())
            .expectedResponseCode(httpStatus);
        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).delete();
        return responseWrapper.getResponseEntity();
    }
}
