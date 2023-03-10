package com.apriori.qms.controller;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.componentiteration.ComponentIteration;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.entity.enums.CssSearch;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.qms.entity.request.scenariodiscussion.ProjectUserParameters;
import com.apriori.qms.entity.request.scenariodiscussion.ProjectUserRequest;
import com.apriori.qms.entity.response.bidpackage.ComponentResponse;
import com.apriori.qms.entity.response.bidpackage.ScenariosResponse;
import com.apriori.qms.enums.QMSAPIEnum;
import com.apriori.utils.CssComponent;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;

import org.apache.http.HttpStatus;
import utils.QmsApiTestUtils;

import java.io.File;
import java.util.Collections;

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
        ComponentInfoBuilder componentInfoBuilder = ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build();

        new ComponentsUtil().postComponent(componentInfoBuilder);
        return new CssComponent().getBaseCssComponents(currentUser,
            CssSearch.COMPONENT_NAME_EQ.getKey() + componentName,
            CssSearch.SCENARIO_NAME_EQ.getKey() + scenarioName).get(0);
    }

    /**
     * Get Component with componentID
     *
     * @param userContext
     * @param componentIdentity
     * @return ResponseWrapper of class object ComponentResponse
     */
    public static ResponseWrapper<ComponentResponse> getComponent(String userContext, String componentIdentity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(QMSAPIEnum.COMPONENT, ComponentResponse.class)
                .inlineVariables(componentIdentity)
                .apUserContext(userContext);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Get Component scenario using component id and scenario id.
     *
     * @param userContext
     * @param componentIdentity
     * @param scenarioIdentity
     * @return ResponseWrapper[ScenarioResponse]
     */
    public static ResponseWrapper<ScenarioResponse> getComponentScenario(String userContext, String componentIdentity, String scenarioIdentity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(QMSAPIEnum.COMPONENT_SCENARIO, ScenarioResponse.class)
                .inlineVariables(componentIdentity, scenarioIdentity)
                .apUserContext(userContext);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * get component scenarios
     *
     * @param userContext
     * @param componentIdentity
     * @return ResponseWrapper of class object ScenariosResponse
     */
    public static ResponseWrapper<ScenariosResponse> getComponentScenarios(String userContext, String componentIdentity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(QMSAPIEnum.COMPONENT_SCENARIOS, ScenariosResponse.class)
                .inlineVariables(componentIdentity)
                .apUserContext(userContext);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Get component scenario latest iteration
     *
     * @param userContext
     * @param componentIdentity
     * @param scenarioIdentity
     * @param iterationIdentity
     * @return ResponseWrapper[ComponentIteration]
     */
    public static ResponseWrapper<ComponentIteration> getLatestIteration(String userContext, String componentIdentity, String scenarioIdentity, String iterationIdentity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(QMSAPIEnum.COMPONENT_ITERATION_LATEST_BY_COMPONENT_SCENARIO_ID, ComponentIteration.class)
                .inlineVariables(componentIdentity, scenarioIdentity, iterationIdentity)
                .apUserContext(userContext);
        return HTTPRequest.build(requestEntity).get();
    }

    public static ResponseWrapper<String> addProjectUser(String componentIdentity, String scenarioIdentity, ProjectUserParameters projectUsers, UserCredentials currentUser) {
        ProjectUserRequest projectUserRequest = ProjectUserRequest.builder()
            .users(Collections.singletonList(projectUsers))
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.COMPONENT_SCENARIO_USERS, null)
            .inlineVariables(componentIdentity, scenarioIdentity)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .body(projectUserRequest)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_CREATED);

        return HTTPRequest.build(requestEntity).post();
    }
}
