package com.apriori.qms.controller;

import com.apriori.AuthUserContextUtil;
import com.apriori.FileResourceUtil;
import com.apriori.GenerateStringUtil;
import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.componentiteration.ComponentIteration;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.entity.enums.CssSearch;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.qms.entity.request.scenariodiscussion.ProjectUserParameters;
import com.apriori.qms.entity.request.scenariodiscussion.ProjectUserRequest;
import com.apriori.qms.entity.response.component.ComponentResponse;
import com.apriori.qms.entity.response.scenario.ScenariosResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioProjectUserResponse;
import com.apriori.qms.enums.QMSAPIEnum;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.utils.CssComponent;

import org.apache.http.HttpStatus;
import utils.QmsApiTestUtils;

import java.io.File;
import java.util.Collections;

/**
 * The type Qms component resources.
 */
public class QmsComponentResources {

    /**
     * Create Component
     *
     * @param processGroupEnum - ProcessGroupEnum
     * @param componentName    - component name
     * @param currentUser      - UserCredentials
     * @return ScenarioItem object
     */
    public static ScenarioItem createAndQueryComponent(ProcessGroupEnum processGroupEnum, String componentName, UserCredentials currentUser) {
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        ComponentInfoBuilder componentInfoBuilder = ComponentInfoBuilder.builder().componentName(componentName)
            .scenarioName(scenarioName).resourceFile(resourceFile).user(currentUser).build();

        new ComponentsUtil().postComponent(componentInfoBuilder);
        return new CssComponent().getBaseCssComponents(currentUser, CssSearch.COMPONENT_NAME_EQ.getKey() + componentName, CssSearch.SCENARIO_NAME_EQ.getKey() + scenarioName)
            .get(0);
    }

    /**
     * Get Component with componentID
     *
     * @param userContext       the user context
     * @param componentIdentity the component identity
     * @return ResponseWrapper of class object ComponentResponse
     */
    public static ResponseWrapper<ComponentResponse> getComponent(String userContext, String componentIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.COMPONENT, ComponentResponse.class)
            .inlineVariables(componentIdentity).apUserContext(userContext);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Get Component scenario using component id and scenario id.
     *
     * @param userContext       the user context
     * @param componentIdentity the component identity
     * @param scenarioIdentity  the scenario identity
     * @return ResponseWrapper[ScenarioResponse] component scenario
     */
    public static ResponseWrapper<ScenarioResponse> getComponentScenario(String userContext, String componentIdentity, String scenarioIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.COMPONENT_SCENARIO, ScenarioResponse.class)
            .inlineVariables(componentIdentity, scenarioIdentity).apUserContext(userContext);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * get component scenarios
     *
     * @param userContext       the user context
     * @param componentIdentity the component identity
     * @return ResponseWrapper of class object ScenariosResponse
     */
    public static ResponseWrapper<ScenariosResponse> getComponentScenarios(String userContext, String componentIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.COMPONENT_SCENARIOS, ScenariosResponse.class)
            .inlineVariables(componentIdentity).apUserContext(userContext);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * get component scenarios users
     *
     * @param userContext       the user context
     * @param componentIdentity the component identity
     * @param scenarioIdentity  the scenario identity
     * @return ResponseWrapper of class object ScenariosResponse
     */
    public static ResponseWrapper<ScenarioProjectUserResponse> getComponentScenarioUsers(String userContext,
                                                                                         String componentIdentity, String scenarioIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.COMPONENT_SCENARIO_USERS, ScenarioProjectUserResponse.class)
            .inlineVariables(componentIdentity, scenarioIdentity)
            .headers(QmsApiTestUtils.setUpHeader(UserUtil.getUser().generateCloudContext().getCloudContext()))
            .apUserContext(userContext);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Get component scenario latest iteration
     *
     * @param userContext       the user context
     * @param componentIdentity the component identity
     * @param scenarioIdentity  the scenario identity
     * @param iterationIdentity the iteration identity
     * @return ResponseWrapper[ComponentIteration] latest iteration
     */
    public static ResponseWrapper<ComponentIteration> getLatestIteration(String userContext, String componentIdentity, String scenarioIdentity, String iterationIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.COMPONENT_ITERATION_LATEST_BY_COMPONENT_SCENARIO_ID, ComponentIteration.class)
            .inlineVariables(componentIdentity, scenarioIdentity, iterationIdentity).apUserContext(userContext);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Add component scenario user response wrapper.
     *
     * @param componentIdentity the component identity
     * @param scenarioIdentity  the scenario identity
     * @param projectUsers      the project users
     * @param currentUser       the current user
     * @return the response wrapper
     */
    public static ResponseWrapper<ScenarioProjectUserResponse> addComponentScenarioUser(String componentIdentity, String scenarioIdentity, ProjectUserParameters projectUsers, UserCredentials currentUser) {
        ProjectUserRequest projectUserRequest = ProjectUserRequest.builder()
            .users(Collections.singletonList(projectUsers)).build();

        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.COMPONENT_SCENARIO_USERS, ScenarioProjectUserResponse.class)
            .inlineVariables(componentIdentity, scenarioIdentity)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .body(projectUserRequest)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_CREATED);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Add component scenario user.
     *
     * @param <T>               the type parameter
     * @param componentIdentity the component identity
     * @param scenarioIdentity  the scenario identity
     * @param projectUsers      the project users
     * @param responseClass     the response class
     * @param httpStatus        the http status
     * @param currentUser       the current user
     * @return the response wrapper entity
     */
    public static <T> T addComponentScenarioUser(String componentIdentity, String scenarioIdentity, ProjectUserParameters projectUsers, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        ProjectUserRequest projectUserRequest = ProjectUserRequest.builder()
            .users(Collections.singletonList(projectUsers)).build();

        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.COMPONENT_SCENARIO_USERS, responseClass)
            .inlineVariables(componentIdentity, scenarioIdentity)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .body(projectUserRequest)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Add component scenario user scenario project user response.
     *
     * @param componentIdentity        the component identity
     * @param scenarioIdentity         the scenario identity
     * @param createProjectUserRequest the create project user request
     * @param currentUser              the current user
     * @return the scenario project user response
     */
    public static ScenarioProjectUserResponse addComponentScenarioUser(String componentIdentity, String scenarioIdentity, ProjectUserRequest createProjectUserRequest, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.COMPONENT_SCENARIO_USERS, ScenarioProjectUserResponse.class)
            .inlineVariables(componentIdentity, scenarioIdentity)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .body(createProjectUserRequest)
            .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<ScenarioProjectUserResponse> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Delete component scenario user response wrapper.
     *
     * @param componentIdentity        the component identity
     * @param scenarioIdentity         the scenario identity
     * @param deleteProjectUserRequest the delete project user request
     * @param currentUser              the current user
     */
    public static void deleteComponentScenarioUser(String componentIdentity, String scenarioIdentity, ProjectUserRequest deleteProjectUserRequest, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.COMPONENT_SCENARIO_USERS, null)
            .inlineVariables(componentIdentity, scenarioIdentity)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .body(deleteProjectUserRequest)
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        HTTPRequest.build(requestEntity).delete();
    }

    /**
     * Delete component scenario user.
     *
     * @param <T>                      the type parameter
     * @param componentIdentity        the component identity
     * @param scenarioIdentity         the scenario identity
     * @param deleteProjectUserRequest the delete project user request
     * @param responseClass            the response class
     * @param httpStatus               the http status
     * @param currentUser              the current user
     * @return the response wrapper entity
     */
    public static <T> T deleteComponentScenarioUser(String componentIdentity, String scenarioIdentity, ProjectUserRequest deleteProjectUserRequest, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.COMPONENT_SCENARIO_USERS, responseClass)
            .inlineVariables(componentIdentity, scenarioIdentity)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .body(deleteProjectUserRequest)
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).delete();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Gets components assigned.
     *
     * @param <T>           the type parameter
     * @param responseClass the response class
     * @param httpStatus    the http status
     * @param currentUser   the current user
     * @return the components assigned
     */
    public static <T> T getComponentsAssigned(Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.COMPONENTS_ASSIGNED, responseClass)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }
}
