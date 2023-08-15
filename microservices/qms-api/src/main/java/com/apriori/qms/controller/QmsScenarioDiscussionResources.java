package com.apriori.qms.controller;

import com.apriori.exceptions.KeyValueException;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.AuthUserContextUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.QueryParams;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.qms.enums.QMSAPIEnum;
import com.apriori.qms.models.request.scenariodiscussion.Attributes;
import com.apriori.qms.models.request.scenariodiscussion.DiscussionCommentParameters;
import com.apriori.qms.models.request.scenariodiscussion.DiscussionCommentRequest;
import com.apriori.qms.models.request.scenariodiscussion.ScenarioDiscussionParameters;
import com.apriori.qms.models.request.scenariodiscussion.ScenarioDiscussionRequest;
import com.apriori.qms.models.response.scenariodiscussion.DiscussionCommentResponse;
import com.apriori.qms.models.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.qms.models.response.scenariodiscussion.ScenarioDiscussionsResponse;
import com.apriori.qms.utils.QmsApiTestUtils;
import com.apriori.reader.file.user.UserCredentials;

import org.apache.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Qms scenario discussion resources.
 */
public class QmsScenarioDiscussionResources {

    /**
     * build and return Scenario Discussion data request builder
     *
     * @param componentIdentity the component identity
     * @param scenarioIdentity  the scenario identity
     * @return scenario discussion request builder
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
     * Gets scenario discussion.
     *
     * @param <T>                        the type parameter
     * @param scenarioDiscussionIdentity the scenario discussion identity
     * @param responseClass              the response class
     * @param httpStatus                 the http status
     * @param currentUser                the current user
     * @return the scenario discussion
     */
    public static <T> T getScenarioDiscussion(String scenarioDiscussionIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSION, responseClass)
            .inlineVariables(scenarioDiscussionIdentity)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Gets scenario discussions with parameters.
     *
     * @param <T>             the type parameter
     * @param paramKeysValues the param keys values
     * @param responseClass   the response class
     * @param httpStatus      the http status
     * @param currentUser     the current user
     * @return the scenario discussions with parameters
     */
    public static <T> T getScenarioDiscussionsWithParameters(String[] paramKeysValues, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        QueryParams queryParams = new QueryParams();
        List<String[]> paramKeyValue = Arrays.stream(paramKeysValues).map(o -> o.split(","))
            .collect(Collectors.toList());
        Map<String, String> paramMap = new HashMap<>();
        try {
            paramKeyValue.forEach(o -> paramMap.put(o[0].trim(), o[1].trim()));
        } catch (ArrayIndexOutOfBoundsException ae) {
            throw new KeyValueException(ae.getMessage(), paramKeyValue);
        }

        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSIONS, responseClass)
            .queryParams(queryParams.use(paramMap))
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Update scenario discussion by identity
     *
     * @param <T>                              - response class type
     * @param scenarioDiscussionIdentity       the scenario discussion identity
     * @param scenarioDiscussionRequestBuilder the scenario discussion request builder
     * @param responseClass                    - response class name
     * @param httpStatus                       Https Status code in integer
     * @param currentUser                      - UserCredentials object
     * @return klass object
     */
    public static <T> T updateScenarioDiscussion(String scenarioDiscussionIdentity, ScenarioDiscussionRequest scenarioDiscussionRequestBuilder, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSION, responseClass)
            .inlineVariables(scenarioDiscussionIdentity)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .body(scenarioDiscussionRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).patch();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Add Comment to scenario discussion
     *
     * @param scenarioDiscussionIdentity the scenario discussion identity
     * @param commentContent             the comment content
     * @param commentStatus              the comment status
     * @param currentUser                - UserCredentials object
     * @return DiscussionCommentResponse discussion comment response
     */
    public static DiscussionCommentResponse addCommentToDiscussion(String scenarioDiscussionIdentity, String commentContent, String commentStatus, UserCredentials currentUser) {
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

        ResponseWrapper<DiscussionCommentResponse> discussionCommentResponse = HTTPRequest.build(requestEntity).post();
        return discussionCommentResponse.getResponseEntity();
    }

    /**
     * Add comment to discussion discussion comment response.
     *
     * @param scenarioDiscussionIdentity the scenario discussion identity
     * @param commentContent             the comment content
     * @param commentStatus              the comment status
     * @param currentUser                the current user
     * @param mentionedUser              the mentioned user
     * @return the discussion comment response
     */
    public static DiscussionCommentResponse addCommentToDiscussion(String scenarioDiscussionIdentity, String commentContent, String commentStatus, UserCredentials currentUser, UserCredentials mentionedUser) {
        DiscussionCommentRequest discussionCommentRequestBuilder = DiscussionCommentRequest.builder()
            .comment(DiscussionCommentParameters.builder()
                .content(commentContent)
                .status(commentStatus)
                .mentionedUserEmails(Collections.singletonList(mentionedUser.getEmail()))
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSION_COMMENTS, DiscussionCommentResponse.class)
            .inlineVariables(scenarioDiscussionIdentity)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .body(discussionCommentRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<DiscussionCommentResponse> discussionCommentResponse = HTTPRequest.build(requestEntity).post();
        return discussionCommentResponse.getResponseEntity();
    }

    /**
     * Add Comment to scenario discussion
     *
     * @param scenarioDiscussionIdentity      the scenario discussion identity
     * @param discussionCommentRequestBuilder - DiscussionCommentRequestBuilder object
     * @param currentUser                     - UserCredentials object
     * @return DiscussionCommentResponse discussion comment response
     */
    public static DiscussionCommentResponse addCommentToDiscussion(String scenarioDiscussionIdentity, DiscussionCommentRequest discussionCommentRequestBuilder, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSION_COMMENTS, DiscussionCommentResponse.class)
            .inlineVariables(scenarioDiscussionIdentity)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .body(discussionCommentRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<DiscussionCommentResponse> discussionCommentResponse = HTTPRequest.build(requestEntity).post();
        return discussionCommentResponse.getResponseEntity();
    }

    /**
     * Add comment to discussion
     *
     * @param <T>                        the type parameter
     * @param scenarioDiscussionIdentity the scenario discussion identity
     * @param commentContent             the comment content
     * @param responseClass              the response class
     * @param httpStatus                 the http status
     * @param currentUser                the current user
     * @return the t
     */
    public static <T> T addCommentToDiscussion(String scenarioDiscussionIdentity, String commentContent, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        DiscussionCommentRequest discussionCommentRequestBuilder = DiscussionCommentRequest.builder()
            .comment(DiscussionCommentParameters.builder()
                .content(commentContent)
                .status("ACTIVE")
                .mentionedUserEmails(Collections.singletonList(currentUser.getEmail()))
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSION_COMMENTS, responseClass)
            .inlineVariables(scenarioDiscussionIdentity)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .body(discussionCommentRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Update comment in scenario discussion
     *
     * @param <T>                             - response class type
     * @param scenarioDiscussionIdentity      the scenario discussion identity
     * @param commentIdentity                 the comment identity
     * @param discussionCommentRequestBuilder the discussion comment request builder
     * @param responseClass                   - response class name
     * @param httpStatus                      Https Status code in integer
     * @param currentUser                     - UserCredentials object
     * @return klass object
     */
    public static <T> T updateCommentToDiscussion(String scenarioDiscussionIdentity, String commentIdentity, DiscussionCommentRequest discussionCommentRequestBuilder, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSION_COMMENT, responseClass)
            .inlineVariables(scenarioDiscussionIdentity, commentIdentity)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .body(discussionCommentRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).patch();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Delete Scenario Discussion
     *
     * @param scenarioDiscussionIdentity the scenario discussion identity
     * @param currentUser                the current user
     */
    public static void deleteScenarioDiscussion(String scenarioDiscussionIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSION, null)
            .inlineVariables(scenarioDiscussionIdentity)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        HTTPRequest.build(requestEntity).delete();
    }

    /**
     * Create Scenario Discussion
     *
     * @param componentIdentity the component identity
     * @param scenarioIdentity  the scenario identity
     * @param currentUser       the current user
     * @return ScenarioDiscussionResponse scenario discussion response
     */
    public static ScenarioDiscussionResponse createScenarioDiscussion(String componentIdentity, String scenarioIdentity, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSIONS, ScenarioDiscussionResponse.class)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .body(QmsScenarioDiscussionResources.getScenarioDiscussionRequestBuilder(componentIdentity, scenarioIdentity))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<ScenarioDiscussionResponse> scenarioDiscussionResponse = HTTPRequest.build(requestEntity)
            .post();
        return scenarioDiscussionResponse.getResponseEntity();
    }

    /**
     * Create Scenario Discussion using ScenarioDiscussionRequest builder
     *
     * @param scenarioDiscussionRequestBuilder the scenario discussion request builder
     * @param currentUser                      the current user
     * @return ScenarioDiscussionResponse scenario discussion response
     */
    public static ScenarioDiscussionResponse createScenarioDiscussion(ScenarioDiscussionRequest scenarioDiscussionRequestBuilder, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSIONS, ScenarioDiscussionResponse.class)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .body(scenarioDiscussionRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<ScenarioDiscussionResponse> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }

    /**
     * get filter scenario discussions based on query params
     *
     * @param currentUser     the current user
     * @param paramKeysValues - key, pair values
     * @return ScenarioDiscussionsResponse filtered scenario discussions
     */
    public static ScenarioDiscussionsResponse getFilteredScenarioDiscussions(UserCredentials currentUser, String... paramKeysValues) {
        QueryParams queryParams = new QueryParams();

        List<String[]> paramKeyValue = Arrays.stream(paramKeysValues).map(o -> o.split(","))
            .collect(Collectors.toList());
        Map<String, String> paramMap = new HashMap<>();

        try {
            paramKeyValue.forEach(o -> paramMap.put(o[0].trim(), o[1].trim()));
        } catch (ArrayIndexOutOfBoundsException ae) {
            throw new KeyValueException(ae.getMessage(), paramKeyValue);
        }

        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSIONS_FILTER, ScenarioDiscussionsResponse.class)
            .queryParams(queryParams.use(paramMap))
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ScenarioDiscussionsResponse> scenarioDiscussionsResponse = HTTPRequest.build(requestEntity)
            .get();
        return scenarioDiscussionsResponse.getResponseEntity();
    }

    /**
     * Update discussion comment.
     *
     * @param <T>                             the type parameter
     * @param scenarioDiscussionIdentity      the scenario discussion identity
     * @param discussionCommentIdentity       the discussion comment identity
     * @param discussionCommentRequestBuilder the discussion comment request builder
     * @param responseClass                   the response class
     * @param httpStatus                      the http status
     * @param currentUser                     the current user
     * @return the t
     */
    public static <T> T updateDiscussionComment(String scenarioDiscussionIdentity, String discussionCommentIdentity, DiscussionCommentRequest discussionCommentRequestBuilder, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSION_COMMENT, responseClass)
            .inlineVariables(scenarioDiscussionIdentity, discussionCommentIdentity)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .body(discussionCommentRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).patch();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Posts scenario discussion comment view status.
     *
     * @param <T>                        the type parameter
     * @param scenarioDiscussionIdentity the scenario discussion identity
     * @param discussionCommentIdentity  the discussion comment identity
     * @param responseClass              the response class
     * @param httpStatus                 the http status
     * @param currentUser                the current user
     * @return the scenario discussion comment view status
     */
    public static <T> T postScenarioDiscussionCommentViewStatus(String scenarioDiscussionIdentity, String discussionCommentIdentity, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.SCENARIO_DISCUSSION_COMMENT_VIEW_STATUS, responseClass)
            .inlineVariables(scenarioDiscussionIdentity, discussionCommentIdentity)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .customBody("{}")
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }
}
