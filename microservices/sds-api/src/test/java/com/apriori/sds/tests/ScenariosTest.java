package com.apriori.sds.tests;

import static org.junit.Assert.assertEquals;

import com.apriori.cidappapi.entity.request.CostRequest;
import com.apriori.css.entity.response.Item;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.request.PostComponentRequest;
import com.apriori.sds.entity.response.Scenario;
import com.apriori.sds.entity.response.ScenarioCostingDefaultsResponse;
import com.apriori.sds.entity.response.ScenarioHoopsImage;
import com.apriori.sds.entity.response.ScenarioItemsResponse;
import com.apriori.sds.entity.response.ScenarioManifest;
import com.apriori.sds.util.SDSRequestEntityUtil;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ScenariosTest extends SDSTestUtil {
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
    public void getCustomImage() {
        final RequestEntity requestEntity =
            SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_SCENARIO_CUSTOM_IMAGE_BY_COMPONENT_SCENARIO_IDS, ScenarioHoopsImage.class)
                .inlineVariables(
                    getComponentId(), getScenarioId()
                );

        ResponseWrapper<ScenarioHoopsImage> response = HTTP2Request.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"8589"})
    @Description("Returns the scenario image containing a Base64 encoded SCS file for a scenario.")
    public void getWebImage() {
        final RequestEntity requestEntity =
            SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_SCENARIO_WEB_IMAGE_BY_COMPONENT_SCENARIO_IDS, ScenarioHoopsImage.class)
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
        final Item testingRollUp = postRollUp(new GenerateStringUtil().generateScenarioName(), "AutomationRollup");

        final RequestEntity requestEntity =
            SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_SCENARIO_MANIFEST_BY_COMPONENT_SCENARIO_IDS, ScenarioManifest.class)
                .inlineVariables(
                    testingRollUp.getComponentIdentity(), testingRollUp.getScenarioIdentity()
                );

        ResponseWrapper<ScenarioManifest> response = HTTP2Request.build(requestEntity).get();
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
        final String copiedScenarioName = new GenerateStringUtil().generateScenarioName();

        PostComponentRequest scenarioRequestBody = PostComponentRequest.builder()
            .name(copiedScenarioName)
            .createdBy(getTestingComponent().getComponentCreatedBy())
            .build();

        final RequestEntity requestEntity =
            SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.POST_COPY_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .inlineVariables(getComponentId(), getScenarioId())
                .body("scenario", scenarioRequestBody);

        ResponseWrapper<Scenario> responseWrapper = HTTP2Request.build(requestEntity).post();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, responseWrapper.getStatusCode());

        final Scenario copiedScenario = responseWrapper.getResponseEntity();

        assertEquals("Copied scenario should present for a component",
            copiedScenarioName, this.getReadyToWorkScenario(copiedScenario.getIdentity()).getScenarioName());

        this.addScenarioToDelete(copiedScenario.getIdentity());
    }

    @Test
    @TestRail(testCaseId = "8431")
    @Description("Cost a scenario.")
    public void testCostScenario() {
        this.costAndGetReadyScenario();
    }

    @Test
    @TestRail(testCaseId = "8429")
    @Description("Update an existing scenario. ")
    public void testUpdateScenario() {
        final String updatedNotes = "Automation Notes";
        final String updatedDescription = "Automation Description";
        final Item scenarioForUpdate = postTestingComponent();

        PostComponentRequest scenarioRequestBody = PostComponentRequest.builder()
            .notes(updatedNotes)
            .description(updatedDescription)
            .updatedBy(scenarioForUpdate.getCreatedBy())
            .build();

        final RequestEntity requestEntity =
            SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.PATCH_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .inlineVariables(scenarioForUpdate.getComponentIdentity(), scenarioForUpdate.getScenarioIdentity())
                .body("scenario", scenarioRequestBody);

        ResponseWrapper<Scenario> responseWrapper = HTTP2Request.build(requestEntity).patch();
        final Scenario scenario = responseWrapper.getResponseEntity();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, responseWrapper.getStatusCode());

        assertEquals("Scenario notes should be updated.",
            scenario.getNotes(), updatedNotes);

        assertEquals("Scenario description should be updated.",
            scenario.getDescription(), updatedDescription);
    }


    @Test
    @TestRail(testCaseId = "8433")
    @Description("Publish a scenario.")
    public void testPublishScenario() {
        this.publishAndGetReadyToWorkScenario();
    }

    @Test
    @TestRail(testCaseId = "8432")
    @Description("Fork a scenario.")
    public void testForkScenario() {
        final String forkScenarioName = new GenerateStringUtil().generateScenarioName();

        PostComponentRequest scenarioRequestBody = PostComponentRequest.builder()
            .scenarioName(forkScenarioName)
            .override(false)
            .updatedBy(getTestingComponent().getCreatedBy())
            .build();

        final RequestEntity requestEntity =
            SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.POST_FORK_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .inlineVariables(getComponentId(), publishAndGetReadyToWorkScenario().getIdentity())
                .body("scenario", scenarioRequestBody);

        ResponseWrapper<Scenario> responseWrapper = HTTP2Request.build(requestEntity).post();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, responseWrapper.getStatusCode());

        final Scenario forkScenario = responseWrapper.getResponseEntity();


        assertEquals("Fork scenario should present for a component",
            forkScenarioName, this.getReadyToWorkScenario(forkScenario.getIdentity()).getScenarioName()
        );

        addScenarioToDelete(forkScenario.getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"8590"})
    @Description("GET a completed watchpoint report for a scenario.")
    public void getWatchPoint() {
        final RequestEntity requestEntity =
            SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_WATCHPOINT_REPORT_SCENARIO_BY_COMPONENT_SCENARIO_IDs, null)
                .inlineVariables(
                    getComponentId(), getScenarioId()
                );

        ResponseWrapper<ScenarioHoopsImage> response = HTTP2Request.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = "8434")
    @Description("Create a watchpoint report.")
    public void testCreateWatchpointReport() {
        final Scenario scenario = this.costAndGetReadyScenario();

        PostComponentRequest scenarioRequestBody = PostComponentRequest.builder()
            .scenarioName(scenario.getScenarioName())
            .override(false)
            .updatedBy(getTestingComponent().getCreatedBy())
            .build();

        final RequestEntity requestEntity =
            SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.POST_WATCHPOINT_REPORT_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .inlineVariables(getComponentId(), scenario.getIdentity())
                .body("scenario", scenarioRequestBody);

        ResponseWrapper<Scenario> responseWrapper = HTTP2Request.build(requestEntity).post();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, responseWrapper.getStatusCode());

        addScenarioToDelete(responseWrapper.getResponseEntity().getIdentity());
    }

    @Test
    @TestRail(testCaseId = "7246")
    @Description("Delete an existing scenario.")
    public void deleteScenario() {
        final Item componentToDelete = postTestingComponent();

        removeTestingScenario(componentToDelete.getComponentIdentity(), componentToDelete.getScenarioIdentity());
        scenariosToDelete.remove(componentToDelete);
    }

    private Scenario getReadyToWorkScenario(final String identity) {
        final int attemptsCount = 15;
        final int secondsToWait = 10;
        int currentCount = 0;
        Scenario scenario;

        do {
            this.doSleep(secondsToWait);
            scenario = this.getScenarioByIdentity(identity);

            if (scenario.getScenarioState().toUpperCase().contains("FAILED")) {
                throw new IllegalStateException(String.format("Scenario failed state: %s. Scenario Id: %s",
                    scenario.getIdentity(), scenario.getScenarioState())
                );
            }

            if (isScenarioStateAsExpected(scenario)) {
                return scenario;
            }
        } while (currentCount++ < attemptsCount);

        throw new IllegalArgumentException(
            String.format("Failed to get scenario by identity: %s, after %d attempts with period in %d seconds.",
                identity, attemptsCount, secondsToWait)
        );
    }

    private void doSleep(final int secondsToWait) {
        try {
            TimeUnit.SECONDS.sleep(secondsToWait);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isScenarioStateAsExpected(final Scenario scenario) {
        final String currentScenarioState = scenario.getScenarioState().toUpperCase();

        return currentScenarioState.equals(ScenarioStateEnum.NOT_COSTED.getState())
            || currentScenarioState.equals(ScenarioStateEnum.COST_COMPLETE.getState());
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

    private Scenario publishAndGetReadyToWorkScenario() {
        final String publishScenarioName = new GenerateStringUtil().generateScenarioName();
        final Item testingComponent = postTestingComponent();

        PostComponentRequest scenarioRequestBody = PostComponentRequest.builder()
            .scenarioName(publishScenarioName)
            .override(false)
            .updatedBy(testingComponent.getCreatedBy())
            .build();

        final RequestEntity requestEntity =
            SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.POST_PUBLISH_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .inlineVariables(testingComponent.getComponentIdentity(), testingComponent.getScenarioIdentity())
                .body("scenario", scenarioRequestBody);

        ResponseWrapper<Scenario> responseWrapper = HTTP2Request.build(requestEntity).post();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, responseWrapper.getStatusCode());

        final Scenario publishedScenario = responseWrapper.getResponseEntity();

        assertEquals("Published scenario should present for a component",
            publishScenarioName, this.getReadyToWorkScenario(publishedScenario.getIdentity()).getScenarioName()
        );

        scenariosToDelete.add(Item.builder()
            .componentIdentity(testingComponent.getComponentIdentity())
            .scenarioIdentity(publishedScenario.getIdentity())
            .build()
        );

        return publishedScenario;
    }

    private Scenario costAndGetReadyScenario() {
        return this.getReadyToWorkScenario(
            this.costScenario().getIdentity()
        );
    }

    private Scenario costScenario() {
        final RequestEntity requestEntity =
            SDSRequestEntityUtil.initWithApUserContext(SDSAPIEnum.POST_COST_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .inlineVariables(getComponentId(), getScenarioId())
                .body("costingInputs", CostRequest.builder()
                    .annualVolume(5500)
                    .batchSize(458)
                    .materialName("Aluminum, Stock, ANSI 1050A")
                    .processGroupName("Sheet Metal")
                    .productionLife(5.0)
                    .vpeName("aPriori USA")
                    .costingTemplateIdentity(getFirstCostingTemplate().getIdentity())
                    .deleteTemplateAfterUse(false)
                    .build()
                );

        ResponseWrapper<Scenario> responseWrapper = HTTP2Request.build(requestEntity).post();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, responseWrapper.getStatusCode());

        return responseWrapper.getResponseEntity();
    }
}
