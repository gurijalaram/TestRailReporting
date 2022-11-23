package com.apriori.qms.controller;

import com.apriori.qms.entity.request.scenariodiscussion.Attributes;
import com.apriori.qms.entity.request.scenariodiscussion.DiscussionCommentParameters;
import com.apriori.qms.entity.request.scenariodiscussion.DiscussionCommentRequest;
import com.apriori.qms.entity.request.scenariodiscussion.ScenarioDiscussionParameters;
import com.apriori.qms.entity.request.scenariodiscussion.ScenarioDiscussionRequest;
import com.apriori.qms.entity.response.scenariodiscussion.DiscussionCommentResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.qms.enums.QMSAPIEnum;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;

import org.apache.http.HttpStatus;
import utils.QmsApiTestUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class QmsScenarioDiscussionResources {

    /**
     * build and return Scenario Discussion data request builder
     *
     * @param componentIdentity
     * @param scenarioIdentity
     * @return
     */
    public static ScenarioDiscussionRequest getScenarioDiscussionRequestBuilder(String componentIdentity, String scenarioIdentity) {
        return ScenarioDiscussionRequest.builder()
            .scenarioDiscussion(ScenarioDiscussionParameters.builder()
                .status("ACTIVE")
                .type("SCENARIO")
                .description(new GenerateStringUtil().generateScenarioName())
                .componentIdentity(componentIdentity)
                .scenarioIdentity(scenarioIdentity)
                .attributes(Attributes.builder()
                    .attribute("materialName")
                    .subject("4056-23423-003")
                    .build())
                .build())
            .build();
    }

    /**
     * Create Scenario Discussion
     *
     * @param componentIdentity
     * @param scenarioIdentity
     * @param currentUser
     * @return ResponseWrapper<ScenarioDiscussionResponse>
     */
    public static ResponseWrapper<ScenarioDiscussionResponse> createScenarioDiscussion(String componentIdentity, String scenarioIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSIONS, ScenarioDiscussionResponse.class)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .body(QmsScenarioDiscussionResources.getScenarioDiscussionRequestBuilder(componentIdentity, scenarioIdentity))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_CREATED);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Update scenario discussion by identity
     *
     * @param scenarioDiscussionIdentity
     * @param scenarioDiscussionRequestBuilder
     * @param responseClass                    - response class name
     * @param httpStatus                       Https Status code in integer
     * @param currentUser                      - UserCredentials object
     * @param <T>                              - response class type
     * @return ResponseWrapper of klass object
     */
    public static <T> ResponseWrapper<T> updateScenarioDiscussion(String scenarioDiscussionIdentity, ScenarioDiscussionRequest scenarioDiscussionRequestBuilder, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSION, responseClass)
            .inlineVariables(scenarioDiscussionIdentity)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .body(scenarioDiscussionRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * Add Comment to scenario discussion
     *
     * @param scenarioDiscussionIdentity
     * @param commentContent
     * @param commentStatus
     * @param currentUser                - UserCredentials object
     * @return ResponseWrapper<DiscussionCommentResponse>
     */
    public static ResponseWrapper<DiscussionCommentResponse> addCommentToDiscussion(String scenarioDiscussionIdentity, String commentContent, String commentStatus, UserCredentials currentUser) {
        DiscussionCommentRequest discussionCommentRequestBuilder = DiscussionCommentRequest.builder()
            .comment(DiscussionCommentParameters.builder()
                .content(commentContent)
                .status(commentStatus)
                .mentionedUserEmails(Collections.singletonList(currentUser.getEmail()))
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSION_COMMENTS, DiscussionCommentResponse.class)
            .inlineVariables(scenarioDiscussionIdentity)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .body(discussionCommentRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_CREATED);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Add Comment to scenario discussion
     *
     * @param scenarioDiscussionIdentity
     * @param discussionCommentRequestBuilder - DiscussionCommentRequestBuilder object
     * @param currentUser                     - UserCredentials object
     * @return ResponseWrapper<DiscussionCommentResponse>
     */
    public static ResponseWrapper<DiscussionCommentResponse> addCommentToDiscussion(String scenarioDiscussionIdentity, DiscussionCommentRequest discussionCommentRequestBuilder, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSION_COMMENTS, DiscussionCommentResponse.class)
            .inlineVariables(scenarioDiscussionIdentity)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .body(discussionCommentRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_CREATED);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Update comment in scenario discussion
     *
     * @param scenarioDiscussionIdentity
     * @param commentIdentity
     * @param discussionCommentRequestBuilder
     * @param responseClass                   - response class name
     * @param httpStatus                      Https Status code in integer
     * @param currentUser                     - UserCredentials object
     * @param <T>                             - response class type
     * @return ResponseWrapper of klass object
     */
    public static <T> ResponseWrapper<T> updateCommentToDiscussion(String scenarioDiscussionIdentity, String commentIdentity, DiscussionCommentRequest discussionCommentRequestBuilder, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSION_COMMENT, responseClass)
            .inlineVariables(scenarioDiscussionIdentity, commentIdentity)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .body(discussionCommentRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * Delete Scenario Discussion
     *
     * @param scenarioDiscussionIdentity
     * @param currentUser
     * @return ResponseWrapper<String>
     */
    public static ResponseWrapper<String> deleteScenarioDiscussion(String scenarioDiscussionIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSION, null)
            .inlineVariables(scenarioDiscussionIdentity)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        return HTTPRequest.build(requestEntity).delete();
    }
}
