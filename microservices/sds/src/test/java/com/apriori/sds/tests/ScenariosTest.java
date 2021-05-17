package com.apriori.sds.tests;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.CommonRequestUtil;
import com.apriori.cidapp.entity.response.PostComponentResponse;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.response.Scenario;
import com.apriori.sds.entity.response.ScenarioCostingDefaultsResponse;
import com.apriori.sds.entity.response.ScenarioHoopsImage;
import com.apriori.sds.entity.response.ScenarioItemsResponse;
import com.apriori.sds.entity.response.ScenarioManifest;
import com.apriori.sds.entity.response.ScenarioSecondaryProcess;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Ignore;
import org.junit.Test;
import util.SDSTestUtil;

public class ScenariosTest extends SDSTestUtil {

    @Test
    @TestRail(testCaseId = {"6922"})
    @Description("Find scenarios for a given component matching a specified query.")
    public void getScenarios() {
        ResponseWrapper<ScenarioItemsResponse> response =
            new CommonRequestUtil().getCommonRequestWithInlineVariables(SDSAPIEnum.GET_SCENARIOS_BY_COMPONENT_IDS, ScenarioItemsResponse.class,
                new APIAuthentication().initAuthorizationHeaderContent(token), getComponentId()
            );

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }


    @Test
    @TestRail(testCaseId = {"6923"})
    @Description("Get the current representation of a scenario.")
    public void getScenarioByIdentity() {
        ResponseWrapper<Scenario> response =
            new CommonRequestUtil().getCommonRequestWithInlineVariables(SDSAPIEnum.GET_SCENARIO_SINGLE_BY_COMPONENT_SCENARIO_IDS, Scenario.class,
                new APIAuthentication().initAuthorizationHeaderContent(token), getComponentId(), getScenarioId()
            );

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"6924"})
    @Description("Get production defaults for a scenario.")
    public void getCostingDefaults() {
        ResponseWrapper<ScenarioCostingDefaultsResponse> response =
            new CommonRequestUtil().getCommonRequestWithInlineVariables(SDSAPIEnum.GET_SCENARIO_COSTING_DEFAULTS_BY_COMPONENT_SCENARIO_IDS, ScenarioCostingDefaultsResponse.class,
                new APIAuthentication().initAuthorizationHeaderContent(token), getComponentId(), getScenarioId()
            );

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"6925"})
    @Description("Returns the scenario image containing a Base64 encoded SCS file for a scenario.")
    public void getHoopsImage() {
        ResponseWrapper<ScenarioHoopsImage> response =
            new CommonRequestUtil().getCommonRequestWithInlineVariables(SDSAPIEnum.GET_SCENARIO_HOOPS_IMAGE_BY_COMPONENT_SCENARIO_IDS, ScenarioHoopsImage.class,
                new APIAuthentication().initAuthorizationHeaderContent(token), getComponentId(), getScenarioId()
            );

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"6926"})
    @Description("Returns the manifest for a scenario if the component type is a container.")
    public void getManifest() {
        ResponseWrapper<ScenarioManifest> response =
            new CommonRequestUtil().getCommonRequestWithInlineVariables(SDSAPIEnum.GET_SCENARIO_MANIFEST_BY_COMPONENT_SCENARIO_IDS, ScenarioManifest.class,
                new APIAuthentication().initAuthorizationHeaderContent(token), getComponentId(), getScenarioId()
            );

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"6927"})
    @Description("Get the available secondary processes for a scenario.")
    public void getSecondaryProcesses() {
        ResponseWrapper<ScenarioSecondaryProcess> response =
            new CommonRequestUtil().getCommonRequestWithInlineVariables(SDSAPIEnum.GET_SCENARIO_SECONDARY_PROCESSES_BY_COMPONENT_SCENARIO_IDS, ScenarioSecondaryProcess.class,
                new APIAuthentication().initAuthorizationHeaderContent(token), getComponentId(), getScenarioId()
            );

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = "7246")
    @Description("Delete an existing scenario.")
    @Ignore
    public void deleteScenario() {
        final ResponseWrapper<PostComponentResponse> postComponentResponseWrapper = postTestingComponent();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, postComponentResponseWrapper.getStatusCode());

        final PostComponentResponse postComponentResponse = postComponentResponseWrapper.getResponseEntity();

        final ResponseWrapper removeComponentResponseWrapper = removeTestingComponent(postComponentResponse.getComponentIdentity(),
            postComponentResponse.getScenarioIdentity());
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT, removeComponentResponseWrapper.getStatusCode());
    }
}
