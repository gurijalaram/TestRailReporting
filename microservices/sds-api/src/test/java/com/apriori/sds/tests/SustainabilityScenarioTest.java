package com.apriori.sds.tests;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.CostingTemplate;
import com.apriori.cidappapi.entity.response.componentiteration.AnalysisOfScenario;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.entity.response.componentiteration.ScenarioProcess;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.response.ScenarioIteration;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SustainabilityScenarioTest extends SDSTestUtil {
    private SoftAssertions soft = new SoftAssertions();

    @Test
    @TestRail(testCaseId = {"24068","24069"})
    @Description("Verify sustainability fields for scenario  after costing")
    public void verifySustainabilityFieldsForScenario() {

        List<ScenarioItem> testingScenarios =  this.costAndGetReadyScenarios();
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

        final RequestEntity requestEntity1 =
            RequestEntityUtil.init(SDSAPIEnum.GET_ITERATION_SINGLE_BY_COMPONENT_SCENARIO_IDENTITY_IDS, ScenarioIteration.class)
                .inlineVariables(testingScenarios.get(0).getComponentIdentity(), testingScenarios.get(0).getScenarioIdentity(),
                    testingScenarios.get(0).getIterationIdentity())
                .expectedResponseCode(HttpStatus.SC_OK);

        final RequestEntity requestEntity2 =
            RequestEntityUtil.init(SDSAPIEnum.GET_ITERATION_SINGLE_BY_COMPONENT_SCENARIO_IDENTITY_IDS, ScenarioIteration.class)
                .inlineVariables(testingScenarios.get(1).getComponentIdentity(), testingScenarios.get(1).getScenarioIdentity(),
                    testingScenarios.get(1).getIterationIdentity())
                .expectedResponseCode(HttpStatus.SC_OK);

        List<ScenarioIteration> scenarios = new ArrayList<>();
        scenarios.add((ScenarioIteration)HTTPRequest.build(requestEntity1).get().getResponseEntity());
        scenarios.add((ScenarioIteration)HTTPRequest.build(requestEntity2).get().getResponseEntity());

        return scenarios.stream()
            .filter(p -> (!p.getScenarioProcesses().isEmpty()))
            .findFirst()
            .orElse(null);
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
