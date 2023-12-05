package com.apriori.qms.api.controller;

import com.apriori.cid.api.models.response.scenarios.ScenarioResponse;
import com.apriori.cid.api.utils.ComponentsUtil;
import com.apriori.css.api.enums.CssSearch;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.qms.api.enums.QMSAPIEnum;
import com.apriori.qms.api.models.request.bidpackage.AssignedComponentRequest;
import com.apriori.qms.api.models.request.scenariodiscussion.ProjectUserParameters;
import com.apriori.qms.api.models.request.scenariodiscussion.ProjectUserRequest;
import com.apriori.qms.api.models.response.component.ComponentResponse;
import com.apriori.qms.api.models.response.scenario.ScenariosResponse;
import com.apriori.qms.api.models.response.scenariodiscussion.ScenarioProjectUserResponse;
import com.apriori.qms.api.utils.QmsApiTestUtils;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.AuthUserContextUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.models.response.component.componentiteration.ComponentIteration;

import org.apache.http.HttpStatus;

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
        RequestEntity requestEntity = RequestEntityUtil_Old.init(QMSAPIEnum.COMPONENT, ComponentResponse.class)
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
        RequestEntity requestEntity = RequestEntityUtil_Old.init(QMSAPIEnum.COMPONENT_SCENARIO, ScenarioResponse.class)
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
        RequestEntity requestEntity = RequestEntityUtil_Old.init(QMSAPIEnum.COMPONENT_SCENARIOS, ScenariosResponse.class)
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
        RequestEntity requestEntity = RequestEntityUtil_Old.init(QMSAPIEnum.COMPONENT_SCENARIO_USERS, ScenarioProjectUserResponse.class)
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
        RequestEntity requestEntity = RequestEntityUtil_Old.init(QMSAPIEnum.COMPONENT_ITERATION_LATEST_BY_COMPONENT_SCENARIO_ID, ComponentIteration.class)
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

        RequestEntity requestEntity = RequestEntityUtil_Old.init(QMSAPIEnum.COMPONENT_SCENARIO_USERS, ScenarioProjectUserResponse.class)
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

        RequestEntity requestEntity = RequestEntityUtil_Old.init(QMSAPIEnum.COMPONENT_SCENARIO_USERS, responseClass)
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
        RequestEntity requestEntity = RequestEntityUtil_Old.init(QMSAPIEnum.COMPONENT_SCENARIO_USERS, ScenarioProjectUserResponse.class)
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
        RequestEntity requestEntity = RequestEntityUtil_Old.init(QMSAPIEnum.COMPONENT_SCENARIO_USERS, null)
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
        RequestEntity requestEntity = RequestEntityUtil_Old.init(QMSAPIEnum.COMPONENT_SCENARIO_USERS, responseClass)
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
    public static <T> T getComponentsAssigned(AssignedComponentRequest assignedComponentRequest, Class<T> responseClass, Integer httpStatus, UserCredentials currentUser) {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(QMSAPIEnum.COMPONENTS_ASSIGNED, responseClass)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .body(assignedComponentRequest)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();
        return responseWrapper.getResponseEntity();
    }
}
