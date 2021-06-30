package com.apriori.sds.tests;

import static org.junit.Assert.*;

import com.apriori.cidappapi.entity.request.CostRequest;
import com.apriori.css.entity.response.Item;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.request.PostComponentRequest;
import com.apriori.sds.entity.response.Scenario;
import com.apriori.sds.entity.response.ScenarioCostingDefaultsResponse;
import com.apriori.sds.entity.response.ScenarioHoopsImage;
import com.apriori.sds.entity.response.ScenarioItemsResponse;
import com.apriori.sds.entity.response.ScenarioManifest;
import com.apriori.sds.entity.response.ScenarioSecondaryProcess;
import com.apriori.sds.util.SDSRequestEntityUtil;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;

import io.qameta.allure.Description;
import lombok.SneakyThrows;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ScenariosTest extends SDSTestUtil {
    private static Scenario testingScenario;

    @Test
    @TestRail(testCaseId = {"6922"})
    @Description("Find scenarios for a gfiven component matching a specified query.")
    public void testGetScenarios() {
        this.getScenarios();
    }

    @Test
    @TestRail(testCaseId = {"6923"})
    @Description("Get the current representation of a scenario.")
    public void testGetScenarioByIdentity() {
        this.getScenarioByIdentity(getScenarioId());
    }

    @Test
    @TestRail(testCaseId = {"6924"})
    @Description("Get production defaults for a scenario.")
    public void getCostingDefaults() {
        final RequestEntity requestEntity =
            SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_SCENARIO_COSTING_DEFAULTS_BY_COMPONENT_SCENARIO_IDS, ScenarioCostingDefaultsResponse.class)
                .inlineVariables(
                    getComponentId(), getScenarioId()
                );

        ResponseWrapper<ScenarioCostingDefaultsResponse> response = HTTP2Request.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"6925"})
    @Description("Returns the scenario image containing a Base64 encoded SCS file for a scenario.")
    public void getHoopsImage() {
        final RequestEntity requestEntity =
            SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_SCENARIO_HOOPS_IMAGE_BY_COMPONENT_SCENARIO_IDS, ScenarioHoopsImage.class)
                .inlineVariables(
                    getComponentId(), getScenarioId()
                );

        ResponseWrapper<ScenarioHoopsImage> response = HTTP2Request.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"6926"})
    @Description("Returns the manifest for a scenario if the component type is a container.")
    public void getManifest() {
        final RequestEntity requestEntity =
            SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_SCENARIO_MANIFEST_BY_COMPONENT_SCENARIO_IDS, ScenarioManifest.class)
                .inlineVariables(
                    getComponentId(), getScenarioId()
                );

        ResponseWrapper<ScenarioManifest> response = HTTP2Request.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"6927"})
    @Description("Get the available secondary processes for a scenario.")
    public void getSecondaryProcesses() {
        final RequestEntity requestEntity =
            SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_SCENARIO_SECONDARY_PROCESSES_BY_COMPONENT_SCENARIO_IDS, ScenarioSecondaryProcess.class)
                .inlineVariables(
                    getComponentId(), getScenarioId()
                );

        ResponseWrapper<ScenarioSecondaryProcess> response = HTTP2Request.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = "8427")
    @Description("Add a scenario to a component.")
    public void testPostScenario() {
        this.postTestingScenario();
    }

    @Test
    @TestRail(testCaseId = "8430")
    @Description("Copy a scenario.")
    public void testCopyScenario() {
        final String copiedScenarioName = "CopiedScenarioName";
        final Scenario scenarioToCopy = this.postTestingScenario();

        this.getReadyToWorkScenario(scenarioToCopy.getIdentity());

        PostComponentRequest scenarioRequestBody = PostComponentRequest.builder()
            .name(copiedScenarioName)
            .createdBy(getTestingComponent().getComponentCreatedBy())
            .build();

        final RequestEntity requestEntity =
            SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.POST_COPY_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .inlineVariables(getComponentId(), scenarioToCopy.getIdentity())
                .body("scenario", scenarioRequestBody);

        ResponseWrapper<Scenario> responseWrapper = HTTP2Request.build(requestEntity).post();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, responseWrapper.getStatusCode());

        final Scenario copiedScenario = responseWrapper.getResponseEntity();
        this.getReadyToWorkScenario(copiedScenario.getIdentity());

        assertEquals("Copied scenario should present for a component", copiedScenarioName, this.getScenarioByIdentity(copiedScenario.getIdentity()).getScenarioName());

        scenariosToDelete.add(Item.builder()
            .componentIdentity(getComponentId())
            .scenarioIdentity(copiedScenario.getIdentity())
            .build()
        );
    }

    @Test
    @TestRail(testCaseId = "8431")
    @Description("Cost a scenario.")
    public void testCostScenario() {
//        {\n    \"costingInputs\": {\n        \"annualVolume\": 5500,\n        \"customAttributes\": {\n            \"priority\": \"3\",\n            \"target_cost\": 17.5\n        },\n        \"materialName\": \"Aluminum, Stock, ANSI 1050A\",\n        \"processGroupName\": \"Sheet Metal\",\n        \"productionLife\": 5,\n        \"updatedBy\": \"{{user_identity}}\",\n        \"vpeName\": \"aPriori USA\"\n    }\n}\n
//        final String copiedScenarioName = "CopiedScenarioName";
        final Scenario testingScenario = this.getTestingScenario();

        final RequestEntity requestEntity =
            SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.POST_COST_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .inlineVariables(getComponentId(), testingScenario.getIdentity())
                .body("costingInputs", new CostRequest().setAnnualVolume(5500)
                    .setBatchSize(458)
                    .setMaterialName("Aluminum, Stock, ANSI 1050A")
                    .setProcessGroupName("Sheet Metal")
                    .setProductionLife(5.0)
                    .setVpeName("aPriori USA"));

        ResponseWrapper<Scenario> responseWrapper = HTTP2Request.build(requestEntity).post();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, responseWrapper.getStatusCode());

        List<Scenario> copiedScenario = this.getScenarios();
//        assertTrue("Copied scenario should present for component", copiedScenario.isPresent());

    }

    //TODO z: should be finished
    @Test
    @TestRail(testCaseId = "8429")
    @Description("Update an existing scenario. ")
    public void testUpdateScenario() {
        final String updatedScenarioName = "UpdatedScenarioName";
        Scenario oldScenario = this.postTestingScenario();

        PostComponentRequest scenarioRequestBody = PostComponentRequest.builder()
            .scenarioName(updatedScenarioName)
            .updatedBy(getTestingComponent().getCreatedBy())
            .build();

        final RequestEntity requestEntity =
            SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.PATCH_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .inlineVariables(getComponentId(), oldScenario.getIdentity())
                .body("scenario", scenarioRequestBody);

        ResponseWrapper<Scenario> responseWrapper = HTTP2Request.build(requestEntity).patch();
        final Scenario scenario = responseWrapper.getResponseEntity();

        assertEquals(String.format("The scenario with a name %s, was not updated.", scenarioRequestBody.getScenarioName()),
            HttpStatus.SC_OK, responseWrapper.getStatusCode());

        assertEquals("Scenario name should be updated.",
            scenario.getScenarioName(), updatedScenarioName);
    }

    @Test
    @TestRail(testCaseId = "7246")
    @Description("Delete an existing scenario.")
    public void deleteScenario() {
        removeTestingScenario(getComponentId(), postTestingScenario().getIdentity());
    }

    private Scenario getTestingScenario() {
        if (testingScenario != null) {
            return testingScenario;
        }

        return testingScenario = this.postTestingScenario();
    }

    @SneakyThrows
    private Scenario getReadyToWorkScenario(final String identity) {
        Scenario scenarioRepresentation;
        int attemptsCount = 10;
        int secondsToWait = 2;
        int currentCount = 0;

        do {
            TimeUnit.SECONDS.sleep(secondsToWait);
            scenarioRepresentation = this.getScenarioByIdentity(identity);
        } while (isScenarioStateIsProcessing(scenarioRepresentation) && currentCount++ < attemptsCount );

        return scenarioRepresentation;
    }

    private boolean isScenarioStateIsProcessing(final Scenario scenario) {
        return scenario.getScenarioState().toUpperCase().equals(ScenarioStateEnum.PROCESSING.getState());
    }

    private Scenario getScenarioByIdentity(final String scenarioIdentity) {
        final RequestEntity requestEntity =
            SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_SCENARIO_SINGLE_BY_COMPONENT_SCENARIO_IDS, Scenario.class)
                .inlineVariables(
                    getComponentId(), scenarioIdentity
                );

        ResponseWrapper<Scenario> response = HTTP2Request.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());

        return response.getResponseEntity();
    }

    private List<Scenario> getScenarios() {
        final RequestEntity requestEntity =
            SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_SCENARIOS_BY_COMPONENT_IDS, ScenarioItemsResponse.class)
                .inlineVariables(
                    getComponentId()
                );

        ResponseWrapper<ScenarioItemsResponse> response = HTTP2Request.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());

        return response.getResponseEntity().getItems();
    }

    private Scenario postTestingScenario() {
        PostComponentRequest scenarioRequestBody = PostComponentRequest.builder()
            .scenarioName(new GenerateStringUtil().generateScenarioName())
            .override(false)
            .updatedBy(getTestingComponent().getCreatedBy())
            .build();

        final RequestEntity requestEntity =
            SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.POST_SCENARIO_BY_COMPONENT_ID, Scenario.class)
                .inlineVariables(getComponentId())
                .body("scenario", scenarioRequestBody);

        ResponseWrapper<Scenario> responseWrapper = HTTP2Request.build(requestEntity).post();
        final Scenario scenario = responseWrapper.getResponseEntity();

        assertEquals(String.format("The scenario with a name %s, was not uploaded.", scenarioRequestBody.getScenarioName()),
            HttpStatus.SC_CREATED, responseWrapper.getStatusCode());

        return scenario;
    }
}
