package com.apriori;

import com.apriori.builder.ComponentInfoBuilder;
import com.apriori.enums.DigitalFactoryEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.enums.ScenarioStateEnum;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.response.component.CostingTemplate;
import com.apriori.models.response.component.ScenarioItem;
import com.apriori.rules.TestRulesAPI;
import com.apriori.sds.enums.SDSAPIEnum;
import com.apriori.sds.models.request.PostComponentRequest;
import com.apriori.sds.models.request.PostWatchpointReportRequest;
import com.apriori.sds.models.response.Scenario;
import com.apriori.sds.models.response.ScenarioCostingDefaultsResponse;
import com.apriori.sds.models.response.ScenarioHoopsImage;
import com.apriori.sds.models.response.ScenarioItemsResponse;
import com.apriori.sds.models.response.ScenarioManifest;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class ScenariosTest extends SDSTestUtil {

    private static ScenarioItem testingScenario;
    private static ScenarioItem testingScenarioWithWatchpoint;

    @Test
    @TestRail(id = 6922)
    @Description("Find scenarios for a given component matching a specified query.")
    public void testGetScenarios() {
        this.getScenarios();
    }

    @Test
    @TestRail(id = 6923)
    @Description("Get the current representation of a scenario.")
    public void testGetScenarioByIdentity() {
        this.getScenarioByIdentity(getScenarioId());
    }

    @Test
    @TestRail(id = 6924)
    @Description("Get production defaults for a scenario.")
    public void getCostingDefaults() {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.GET_SCENARIO_COSTING_DEFAULTS_BY_COMPONENT_SCENARIO_IDS, ScenarioCostingDefaultsResponse.class)
                .inlineVariables(
                    getComponentId(), getScenarioId()
                )
                .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }

    @Test
    //TODO should be resolved after adding an API to create a custom image for CID.
    @Disabled("API that allow to create a custom image, doesn't exist for CID. Custom image is user guided.")
    @TestRail(id = 6925)
    @Description("Returns the scenario image containing a Base64 encoded SCS file for a scenario.")
    public void getCustomImage() {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.GET_SCENARIO_CUSTOM_IMAGE_BY_COMPONENT_SCENARIO_IDS, ScenarioHoopsImage.class)
                .inlineVariables(
                    getComponentId(), getScenarioId()
                )
                .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }

    @Test
    @TestRail(id = 8589)
    @Description("Returns the scenario image containing a Base64 encoded SCS file for a scenario.")
    public void getWebImage() {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.GET_SCENARIO_WEB_IMAGE_BY_COMPONENT_SCENARIO_IDS, ScenarioHoopsImage.class)
                .inlineVariables(
                    getComponentId(), getScenarioId()
                )
                .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }

    @Test
    @TestRail(id = 6926)
    @Description("Returns the manifest for a scenario if the component type is a container.")
    public void getManifest() {
        final ScenarioItem testingRollUp = postRollUp(new GenerateStringUtil().generateScenarioName(), "AutomationRollup");

        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.GET_SCENARIO_MANIFEST_BY_COMPONENT_SCENARIO_IDS, ScenarioManifest.class)
                .inlineVariables(
                    testingRollUp.getComponentIdentity(), testingRollUp.getScenarioIdentity()
                )
                .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }

    @Test
    @TestRail(id = 8430)
    @Description("Copy a scenario.")
    public void testCopyScenario() {
        final String copiedScenarioName = new GenerateStringUtil().generateScenarioName();

        PostComponentRequest scenarioRequestBody = PostComponentRequest.builder()
            .scenarioName(copiedScenarioName)
            .createdBy(getTestingComponent().getComponentCreatedBy())
            .build();

        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.POST_COPY_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .inlineVariables(getComponentId(), getScenarioId())
                .body("scenario", scenarioRequestBody)
                .expectedResponseCode(HttpStatus.SC_CREATED);

        ResponseWrapper<Scenario> responseWrapper = HTTPRequest.build(requestEntity).post();

        final Scenario copiedScenario = responseWrapper.getResponseEntity();

        final SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(copiedScenarioName).isEqualTo(this.getReadyToWorkScenario(getComponentId(), copiedScenario.getIdentity()).getScenarioName());
        softAssertions.assertAll();

        this.addScenarioToDelete(copiedScenario.getIdentity());
    }

    @Test
    @TestRail(id = 8431)
    @Description("Cost a scenario.")
    public void testCostScenario() {
        this.costAndGetReadyScenario();
    }

    @Test
    @TestRail(id = 8429)
    @Description("Update an existing scenario. ")
    public void testUpdateScenario() {
        final String updatedNotes = "Automation Notes";
        final String updatedDescription = "Automation Description";
        final ScenarioItem scenarioForUpdate = postTestingComponentAndAddToRemoveList();

        PostComponentRequest scenarioRequestBody = PostComponentRequest.builder()
            .notes(updatedNotes)
            .description(updatedDescription)
            .updatedBy(scenarioForUpdate.getCreatedBy())
            .build();

        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.PATCH_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .inlineVariables(scenarioForUpdate.getComponentIdentity(), scenarioForUpdate.getScenarioIdentity())
                .body("scenario", scenarioRequestBody)
                .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<Scenario> responseWrapper = HTTPRequest.build(requestEntity).patch();
        final Scenario scenario = responseWrapper.getResponseEntity();

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(scenario.getNotes()).isEqualTo(updatedNotes);
        softAssertions.assertThat(scenario.getDescription()).isEqualTo(updatedDescription);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 8433)
    @Description("Publish a scenario.")
    public void testPublishScenario() {
        this.publishAndGetReadyToWorkScenario();
    }

    @Test
    @TestRail(id = 8432)
    @Description("Fork a scenario.")
    public void testForkScenario() {
        final String forkScenarioName = new GenerateStringUtil().generateScenarioName();

        PostComponentRequest scenarioRequestBody = PostComponentRequest.builder()
            .scenarioName(forkScenarioName)
            .override(false)
            .updatedBy(getTestingComponent().getCreatedBy())
            .build();

        ScenarioItem publishedScenario = publishAndGetReadyToWorkScenario();

        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.POST_FORK_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .inlineVariables(publishedScenario.getComponentIdentity(), publishedScenario.getScenarioIdentity())
                .body("scenario", scenarioRequestBody)
                .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<Scenario> responseWrapper = HTTPRequest.build(requestEntity).post();

        final Scenario forkScenario = responseWrapper.getResponseEntity();

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(this.getReadyToWorkScenario(publishedScenario.getComponentIdentity(), forkScenario.getIdentity())
            .getScenarioName()).isEqualTo(forkScenarioName);

        softAssertions.assertAll();

        scenariosToDelete.add(ScenarioItem.builder()
            .componentIdentity(publishedScenario.getComponentIdentity())
            .scenarioIdentity(forkScenario.getIdentity())
            .build()
        );
    }

    @Test
    @TestRail(id = 8590)
    @Description("GET a completed watchpoint report for a scenario.")
    public void testGetWatchPoint() {
        ScenarioItem scenarioWithCreatedWatchpoint = this.createWatchpoint();

        getScenarioRepresentation(ScenarioItem.builder()
                .componentIdentity(scenarioWithCreatedWatchpoint.getComponentIdentity())
                .scenarioIdentity(scenarioWithCreatedWatchpoint.getScenarioIdentity())
                .scenarioState(ScenarioStateEnum.NOT_COSTED.getState())
                .build(),
            testingUser);

        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.GET_WATCHPOINT_REPORT_SCENARIO_BY_COMPONENT_SCENARIO_IDs, null)
                .inlineVariables(
                    scenarioWithCreatedWatchpoint.getComponentIdentity(), scenarioWithCreatedWatchpoint.getScenarioIdentity())
                .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }

    @Test
    @TestRail(id = 8434)
    @Description("Create a watchpoint report.")
    public void testCreateWatchpointReport() {
        this.createWatchpoint();
    }

    @Test
    @TestRail(id = 7246)
    @Description("Delete an existing scenario.")
    public void deleteScenario() {
        final ScenarioItem componentToDelete = postTestingComponentAndAddToRemoveList();

        removeTestingScenario(componentToDelete.getComponentIdentity(), componentToDelete.getScenarioIdentity());
        scenariosToDelete.remove(componentToDelete);
    }

    private ScenarioItem createWatchpoint() {

        if (testingScenarioWithWatchpoint != null) {
            return testingScenarioWithWatchpoint;
        }

        final ScenarioItem scenario = this.costAndGetReadyScenario();

        PostWatchpointReportRequest watchpointReportRequest = PostWatchpointReportRequest.builder()
            .watchpointTemplateName("CI_PartCost.watchpoints.xml")
            .outputFileName("BRACKET_BASIC-PartCostReport.xlsx")
            .build();

        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.POST_WATCHPOINT_REPORT_SCENARIO_BY_COMPONENT_SCENARIO_IDs, null)
                .inlineVariables(scenario.getComponentIdentity(), scenario.getScenarioIdentity())
                .body("scenario", watchpointReportRequest)
                .expectedResponseCode(HttpStatus.SC_CREATED);

        HTTPRequest.build(requestEntity).post();

        return testingScenarioWithWatchpoint = scenario;
    }

    private Scenario getScenarioByIdentity(final String scenarioIdentity) {
        return getScenarioByCustomerScenarioIdentity(null, scenarioIdentity);
    }

    private List<Scenario> getScenarios() {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.GET_SCENARIOS_BY_COMPONENT_IDS, ScenarioItemsResponse.class)
                .inlineVariables(
                    getComponentId()
                )
                .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<ScenarioItemsResponse> response = HTTPRequest.build(requestEntity).get();

        return response.getResponseEntity().getItems();
    }

    private ScenarioItem publishAndGetReadyToWorkScenario() {
        final String publishScenarioName = new GenerateStringUtil().generateScenarioName();
        final ScenarioItem testingComponent = postTestingComponentAndAddToRemoveList();

        PostComponentRequest scenarioRequestBody = PostComponentRequest.builder()
            .scenarioName(publishScenarioName)
            .override(false)
            .updatedBy(testingComponent.getCreatedBy())
            .build();

        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.POST_PUBLISH_SCENARIO_BY_COMPONENT_SCENARIO_IDs, Scenario.class)
                .inlineVariables(testingComponent.getComponentIdentity(), testingComponent.getScenarioIdentity())
                .body("scenario", scenarioRequestBody)
                .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<Scenario> responseWrapper = HTTPRequest.build(requestEntity).post();

        final Scenario publishedScenario = responseWrapper.getResponseEntity();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(this.getReadyToWorkScenario(testingComponent.getComponentIdentity(), publishedScenario.getIdentity())
            .getScenarioName()).isEqualTo(publishScenarioName);
        softAssertions.assertAll();

        return testingComponent;
    }

    private ScenarioItem costAndGetReadyScenario() {

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

        List<ScenarioItem> testingScenarios = postCostScenario(
            ComponentInfoBuilder.builder().componentName(componentName)
                .scenarioName(scenarioName)
                .componentIdentity(componentId)
                .scenarioIdentity(scenarioId)
                .costingTemplate(CostingTemplate.builder()
                    .processGroupName(pg.getProcessGroup())
                    .vpeName(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
                    .materialMode(mode.toUpperCase())
                    .materialName(materialName)
                    .build())
                .user(testingUser)
                .build());

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(testingScenarios.size()).isNotZero();
        softAssertions.assertAll();

        return testingScenario = testingScenarios.get(0);
    }
}