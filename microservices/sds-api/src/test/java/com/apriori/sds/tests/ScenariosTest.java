package com.apriori.sds.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.apriori.cidappapi.utils.CidAppTestUtil;
import com.apriori.cidappapi.utils.CostComponentInfo;
import com.apriori.css.entity.response.Item;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.request.PostComponentRequest;
import com.apriori.sds.entity.request.PostWatchpointReportRequest;
import com.apriori.sds.entity.response.Scenario;
import com.apriori.sds.entity.response.ScenarioCostingDefaultsResponse;
import com.apriori.sds.entity.response.ScenarioHoopsImage;
import com.apriori.sds.entity.response.ScenarioItemsResponse;
import com.apriori.sds.entity.response.ScenarioManifest;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ScenariosTest extends SDSTestUtil {

    private static final UserCredentials currentUser = UserUtil.getUser();
    private static Item testingScenario;
    private static Item testingScenarioWithWatchpoint;

    @Test
    @TestRail(testCaseId = {"6922"})
    @Description("Find scenarios for a given component matching a specified query.")
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
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_SCENARIO_COSTING_DEFAULTS_BY_COMPONENT_SCENARIO_IDS, ScenarioCostingDefaultsResponse.class)
                .inlineVariables(
                    getComponentId(), getScenarioId()
                );

        ResponseWrapper<ScenarioCostingDefaultsResponse> response = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    //TODO should be resolved after adding an API to create a custom image for CID.
    @Ignore("API that allow to create a custom image, doesn't exist for CID. Custom image is user guided.")
    @TestRail(testCaseId = {"6925"})
    @Description("Returns the scenario image containing a Base64 encoded SCS file for a scenario.")
    public void getCustomImage() {
        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_SCENARIO_CUSTOM_IMAGE_BY_COMPONENT_SCENARIO_IDS, ScenarioHoopsImage.class)
                .inlineVariables(
                    getComponentId(), getScenarioId()
                );

        ResponseWrapper<ScenarioHoopsImage> response = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"8589"})
    @Description("Returns the scenario image containing a Base64 encoded SCS file for a scenario.")
    public void getWebImage() {
        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_SCENARIO_WEB_IMAGE_BY_COMPONENT_SCENARIO_IDS, ScenarioHoopsImage.class)
                .inlineVariables(
                    getComponentId(), getScenarioId()
                );

        ResponseWrapper<ScenarioHoopsImage> response = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"6926"})
    @Description("Returns the manifest for a scenario if the component type is a container.")
    public void getManifest() {
        final Item testingRollUp = postRollUp(new GenerateStringUtil().generateScenarioName(), "AutomationRollup");

        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_SCENARIO_MANIFEST_BY_COMPONENT_SCENARIO_IDS, ScenarioManifest.class)
                .inlineVariables(
                    testingRollUp.getComponentIdentity(), testingRollUp.getScenarioIdentity()
                );

        ResponseWrapper<ScenarioManifest> response = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = "8430")
    @Description("Copy a scenario.")
    public void testCopyScenario() {
        final String copiedScenarioName = new GenerateStringUtil().generateScenarioName();

        PostComponentRequest scenarioRequestBody = PostComponentRequest.builder()
            .scenarioName(copiedScenarioName)
            .createdBy(getTestingComponent().getComponentCreatedBy())
            .build();

        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.POST_COPY_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .inlineVariables(getComponentId(), getScenarioId())
                .body("scenario", scenarioRequestBody);

        ResponseWrapper<Scenario> responseWrapper = HTTPRequest.build(requestEntity).post();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, responseWrapper.getStatusCode());

        final Scenario copiedScenario = responseWrapper.getResponseEntity();

        assertEquals("Copied scenario should present for a component",
            copiedScenarioName, this.getReadyToWorkScenario(getComponentId(), copiedScenario.getIdentity()).getScenarioName());

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
        final Item scenarioForUpdate = postTestingComponentAndAddToRemoveList();

        PostComponentRequest scenarioRequestBody = PostComponentRequest.builder()
            .notes(updatedNotes)
            .description(updatedDescription)
            .updatedBy(scenarioForUpdate.getCreatedBy())
            .build();

        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.PATCH_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .inlineVariables(scenarioForUpdate.getComponentIdentity(), scenarioForUpdate.getScenarioIdentity())
                .body("scenario", scenarioRequestBody);

        ResponseWrapper<Scenario> responseWrapper = HTTPRequest.build(requestEntity).patch();
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

        Item publishedScenario = publishAndGetReadyToWorkScenario();

        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.POST_FORK_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .inlineVariables(publishedScenario.getComponentIdentity(), publishedScenario.getScenarioIdentity())
                .body("scenario", scenarioRequestBody);

        ResponseWrapper<Scenario> responseWrapper = HTTPRequest.build(requestEntity).post();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, responseWrapper.getStatusCode());

        final Scenario forkScenario = responseWrapper.getResponseEntity();


        assertEquals("Fork scenario should present for a component",
            forkScenarioName, this.getReadyToWorkScenario(publishedScenario.getComponentIdentity(), forkScenario.getIdentity()).getScenarioName()
        );

        scenariosToDelete.add(Item.builder()
            .componentIdentity(publishedScenario.getComponentIdentity())
            .scenarioIdentity(forkScenario.getIdentity())
            .build()
        );
    }

    @Test
    @TestRail(testCaseId = {"8590"})
    @Description("GET a completed watchpoint report for a scenario.")
    public void testGetWatchPoint() {
        Item scenarioWithCreatedWatchpoint = this.createWatchpoint();

        new CidAppTestUtil().getScenarioRepresentation("processing", scenarioWithCreatedWatchpoint.getComponentIdentity(),
            scenarioWithCreatedWatchpoint.getScenarioIdentity(), currentUser
        );

        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_WATCHPOINT_REPORT_SCENARIO_BY_COMPONENT_SCENARIO_IDs, null)
                .inlineVariables(
                    scenarioWithCreatedWatchpoint.getComponentIdentity(), scenarioWithCreatedWatchpoint.getScenarioIdentity()
                );

        ResponseWrapper<ScenarioHoopsImage> response = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = "8434")
    @Description("Create a watchpoint report.")
    public void testCreateWatchpointReport() {
        this.createWatchpoint();
    }

    @Test
    @TestRail(testCaseId = "7246")
    @Description("Delete an existing scenario.")
    public void deleteScenario() {
        final Item componentToDelete = postTestingComponentAndAddToRemoveList();

        removeTestingScenario(componentToDelete.getComponentIdentity(), componentToDelete.getScenarioIdentity());
        scenariosToDelete.remove(componentToDelete);
    }

    private Scenario getReadyToWorkScenario(final String componentIdentity, final String scenarioIdentity) {
        final int attemptsCount = 15;
        final int secondsToWait = 10;
        int currentCount = 0;
        Scenario scenario;

        do {
            this.doSleep(secondsToWait);
            scenario = this.getScenarioByCustomerScenarioIdentity(componentIdentity, scenarioIdentity);

            if (scenario.getScenarioState().toUpperCase().contains("FAILED")) {
                throw new IllegalStateException(String.format("Scenario failed state: %s. Scenario Id: %s",
                    scenario.getScenarioState(), scenario.getIdentity())
                );
            }

            if (isScenarioStateAsExpected(scenario)) {
                return scenario;
            }
        } while (currentCount++ < attemptsCount);

        throw new IllegalArgumentException(
            String.format("Failed to get scenario by identity: %s, after %d attempts with period in %d seconds.",
                scenarioIdentity, attemptsCount, secondsToWait)
        );
    }

    private Item createWatchpoint() {

        if (testingScenarioWithWatchpoint != null) {
            return testingScenarioWithWatchpoint;
        }

        final Item scenario = this.costAndGetReadyScenario();

        PostWatchpointReportRequest watchpointReportRequest = PostWatchpointReportRequest.builder()
            .watchpointTemplateName("CI_PartCost.watchpoints.xml")
            .outputFileName("BRACKET_BASIC-PartCostReport.xlsx")
            .build();

        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.POST_WATCHPOINT_REPORT_SCENARIO_BY_COMPONENT_SCENARIO_IDs, null)
                .inlineVariables(scenario.getComponentIdentity(), scenario.getScenarioIdentity())
                .body("scenario", watchpointReportRequest);

        ResponseWrapper<Scenario> responseWrapper = HTTPRequest.build(requestEntity).post();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, responseWrapper.getStatusCode());

        return testingScenarioWithWatchpoint = scenario;
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
        return getScenarioByCustomerScenarioIdentity(null, scenarioIdentity);
    }


    private Scenario getScenarioByCustomerScenarioIdentity(String componentIdentity, final String scenarioIdentity) {
        if (componentIdentity == null) {
            componentIdentity = getComponentId();
        }

        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_SCENARIO_SINGLE_BY_COMPONENT_SCENARIO_IDS, Scenario.class)
                .inlineVariables(
                    componentIdentity, scenarioIdentity
                );

        ResponseWrapper<Scenario> response = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());

        return response.getResponseEntity();
    }

    private List<Scenario> getScenarios() {
        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_SCENARIOS_BY_COMPONENT_IDS, ScenarioItemsResponse.class)
                .inlineVariables(
                    getComponentId()
                );

        ResponseWrapper<ScenarioItemsResponse> response = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());

        return response.getResponseEntity().getItems();
    }

    private Item publishAndGetReadyToWorkScenario() {
        final String publishScenarioName = new GenerateStringUtil().generateScenarioName();
        final Item testingComponent = postTestingComponentAndAddToRemoveList();

        PostComponentRequest scenarioRequestBody = PostComponentRequest.builder()
            .scenarioName(publishScenarioName)
            .override(false)
            .updatedBy(testingComponent.getCreatedBy())
            .build();

        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.POST_PUBLISH_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .inlineVariables(testingComponent.getComponentIdentity(), testingComponent.getScenarioIdentity())
                .body("scenario", scenarioRequestBody);

        ResponseWrapper<Scenario> responseWrapper = HTTPRequest.build(requestEntity).post();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, responseWrapper.getStatusCode());

        final Scenario publishedScenario = responseWrapper.getResponseEntity();

        assertEquals("Published scenario should present for a component",
            publishScenarioName, this.getReadyToWorkScenario(testingComponent.getComponentIdentity(), publishedScenario.getIdentity()).getScenarioName()
        );

        return testingComponent;
    }

    private Item costAndGetReadyScenario() {

        if (testingScenario != null) {
            return testingScenario;
        }

        String componentName = getTestingComponent().getComponentName();
        String scenarioName = getTestingComponent().getScenarioName();
        String componentId = getTestingComponent().getComponentIdentity();
        String scenarioId = getTestingComponent().getScenarioIdentity();

        ProcessGroupEnum pg = ProcessGroupEnum.SHEET_METAL;

        String mode = "manual";
        String materialName = "Use Default";

        List<Item> testingScenarios = new CidAppTestUtil().postCostScenario(
            CostComponentInfo.builder().componentName(componentName)
                .scenarioName(scenarioName)
                .componentId(componentId)
                .scenarioId(scenarioId)
                .processGroup(pg)
                .digitalFactory(DigitalFactoryEnum.APRIORI_USA)
                .mode(mode)
                .material(materialName)
                .user(currentUser)
                .build());

        assertNotEquals("Testing scenario should present.", testingScenarios.size(), 0);

        return testingScenario = testingScenarios.get(0);
    }
}
