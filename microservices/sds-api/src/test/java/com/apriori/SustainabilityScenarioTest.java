package com.apriori;

import com.apriori.cidappapi.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.models.response.CostingTemplate;
import com.apriori.cidappapi.models.response.componentiteration.AnalysisOfScenario;
import com.apriori.enums.DigitalFactoryEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.models.response.ScenarioItem;
import com.apriori.models.response.componentiteration.ScenarioProcess;
import com.apriori.sds.enums.SDSAPIEnum;
import com.apriori.sds.models.response.ScenarioIteration;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class SustainabilityScenarioTest extends SDSTestUtil {
    private SoftAssertions soft = new SoftAssertions();

    @Test
    @TestRail(id = {24068, 24069})
    @Description("Verify sustainability fields for scenario  after costing")
    public void verifySustainabilityFieldsForScenario() {

        List<ScenarioItem> testingScenarios = this.costAndGetReadyScenarios();
        ScenarioIteration scenario = getScenarioViaSDS(testingScenarios);

        scenario.getScenarioProcesses().stream()
            .forEach(entry -> assertScenarioProcess(entry));
        assertScenarioAnalysis(scenario.getAnalysisOfScenario());
        soft.assertAll();
    }

    private void assertScenarioAnalysis(AnalysisOfScenario analysisOfScenario) {
        soft.assertThat(analysisOfScenario).hasFieldOrProperty("annualManufacturingCarbon");
        soft.assertThat(analysisOfScenario).hasFieldOrProperty("logisticsCarbon");
        soft.assertThat(analysisOfScenario).hasFieldOrProperty("materialCarbon");
        soft.assertThat(analysisOfScenario).hasFieldOrProperty("processCarbon");
        soft.assertThat(analysisOfScenario).hasFieldOrProperty("totalCarbon");
    }

    private void assertScenarioProcess(ScenarioProcess scenarioProcess) {
        soft.assertThat(scenarioProcess).hasFieldOrProperty("energyCarbonFactor");
        soft.assertThat(scenarioProcess).hasFieldOrProperty("primaryMaterialCarbonFactor");
        soft.assertThat(scenarioProcess).hasFieldOrProperty("logisticsCarbon");
        soft.assertThat(scenarioProcess).hasFieldOrProperty("materialCarbon");
        soft.assertThat(scenarioProcess).hasFieldOrProperty("supportsSustainabilityType");
        soft.assertThat(scenarioProcess).hasFieldOrProperty("totalCarbon");
    }

    private ScenarioIteration getScenarioViaSDS(List<ScenarioItem> testingScenarios) {

        List<ScenarioIteration> scenarios = new ArrayList<>();

        scenarios.add((ScenarioIteration) HTTPRequest.build(initRequestToGetIteration(0, testingScenarios)).get().getResponseEntity());
        scenarios.add((ScenarioIteration) HTTPRequest.build(initRequestToGetIteration(1, testingScenarios)).get().getResponseEntity());

        return scenarios.stream()
            .filter(p -> (!p.getScenarioProcesses().isEmpty()))
            .findFirst()
            .orElse(null);
    }

    public RequestEntity initRequestToGetIteration(Integer itemNumber, List<ScenarioItem> testingScenarios) {
        return RequestEntityUtil.init(SDSAPIEnum.GET_ITERATION_SINGLE_BY_COMPONENT_SCENARIO_IDENTITY_IDS, ScenarioIteration.class)
            .inlineVariables(testingScenarios.get(itemNumber).getComponentIdentity(), testingScenarios.get(itemNumber).getScenarioIdentity(),
                testingScenarios.get(itemNumber).getIterationIdentity())
            .expectedResponseCode(HttpStatus.SC_OK);
    }

    private List<ScenarioItem> costAndGetReadyScenarios() {

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

        return testingScenarios;
    }
}