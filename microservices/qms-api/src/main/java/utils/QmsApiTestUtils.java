package utils;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.componentiteration.ComponentIteration;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.css.entity.enums.CssSearch;
import com.apriori.css.entity.response.CssComponentResponse;
import com.apriori.css.entity.response.ScenarioItem;
import com.apriori.qms.entity.response.ComponentResponse;
import com.apriori.qms.entity.response.ScenariosResponse;
import com.apriori.qms.enums.QMSAPIEnum;
import com.apriori.utils.CssComponent;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;

import java.io.File;

public class QmsApiTestUtils {

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
                CssSearch.SCENARIO_NAME_EQ.getKey() + scenarioName)
            .getResponseEntity().getItems().get(0);
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
     * @return ResponseWrapper<ScenarioResponse>
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
     * @return ResponseWrapper<ComponentIteration>
     */
    public static ResponseWrapper<ComponentIteration> getLatestIteration(String userContext, String componentIdentity, String scenarioIdentity, String iterationIdentity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(QMSAPIEnum.COMPONENT_ITERATION_LATEST_BY_COMPONENT_SCENARIO_ID, ComponentIteration.class)
                .inlineVariables(componentIdentity, scenarioIdentity, iterationIdentity)
                .apUserContext(userContext);
        return HTTPRequest.build(requestEntity).get();
    }
}

