package com.apriori.cid.api.tests.evaluate;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cid.api.models.response.scenarios.ScenarioResponse;
import com.apriori.cid.api.utils.ComponentsUtil;
import com.apriori.cid.api.utils.IterationsUtil;
import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.NewCostingLabelEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.component.CostingTemplate;
import com.apriori.shared.util.models.response.component.componentiteration.AnalysisOfScenario;
import com.apriori.shared.util.models.response.component.componentiteration.ComponentIteration;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;

@ExtendWith(TestRulesAPI.class)
public class ReCostScenarioTests {

    private final ComponentsUtil componentsUtil = new ComponentsUtil();
    private final ScenariosUtil scenariosUtil = new ScenariosUtil();
    private final IterationsUtil iterationsUtil = new IterationsUtil();
    private SoftAssertions softAssertions;

    private ComponentInfoBuilder component;

    @Test
    @Tag(SMOKE)
    @TestRail(id = 6101)
    @Description("Test recosting a cad file - Gear Making")
    public void testRecostGearMaking() {
        ComponentInfoBuilder component = new ComponentRequestUtil().getComponent("Case_011_-_Team_350385");

        ComponentInfoBuilder componentResponse = componentsUtil.postComponent(component);

        postCostScenario(
            component.getProcessGroup(), component.getComponentName(), component.getScenarioName(),
            component.getUser(), componentResponse, DigitalFactoryEnum.APRIORI_USA);

        ResponseWrapper<ComponentIteration> componentIterationResponse = getComponentIterationResponseWrapper(componentResponse);

        AnalysisOfScenario analysisOfScenario = componentIterationResponse.getResponseEntity().getAnalysisOfScenario();

        softAssertions = new SoftAssertions();

        softAssertions.assertThat(analysisOfScenario.getProcessRoutingName()).contains("Material Stock");

        postCostScenario(
            component.getProcessGroup(), component.getComponentName(), component.getScenarioName(),
            component.getUser(), componentResponse, DigitalFactoryEnum.APRIORI_BRAZIL);

        ScenarioResponse scenarioRepresentation = getScenarioResponseResponseWrapper(componentResponse);

        softAssertions.assertThat(scenarioRepresentation.getScenarioState()).isEqualTo(NewCostingLabelEnum.COST_COMPLETE.name());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 6102)
    @Description("Test recosting a cad file - Machining Contouring")
    public void testRecostMachiningContouring() {
        component = new ComponentRequestUtil().getComponent("case_002_00400016-003M10_A");

        uploadCostScenarioAndAssert(component, "4 Axis Mill", DigitalFactoryEnum.APRIORI_CHINA);
    }

    @Test
    @TestRail(id = {6103})
    @Description("Test recosting a cad file - Partially Automated Machining")
    public void testRecostPartiallyAutomatedMachining() {
        component = new ComponentRequestUtil().getComponent("14100640");

        ComponentInfoBuilder componentResponse = componentsUtil.postComponent(component);

        postCostScenario(
            component.getProcessGroup(), component.getComponentName(), component.getScenarioName(), component.getUser(), componentResponse, DigitalFactoryEnum.APRIORI_USA);

        ResponseWrapper<ComponentIteration> componentIterationResponse = getComponentIterationResponseWrapper(componentResponse);

        AnalysisOfScenario analysisOfScenario = componentIterationResponse.getResponseEntity().getAnalysisOfScenario();

        softAssertions = new SoftAssertions();

        softAssertions.assertThat(analysisOfScenario.getProcessRoutingName()).contains("3 Axis Mill");

        postCostScenario(
            component.getProcessGroup(), component.getComponentName(), component.getScenarioName(), component.getUser(), componentResponse, DigitalFactoryEnum.APRIORI_BRAZIL);

        ScenarioResponse scenarioRepresentation = getScenarioResponseResponseWrapper(componentResponse);

        softAssertions.assertThat(scenarioRepresentation.getScenarioState()).isEqualTo(NewCostingLabelEnum.COST_COMPLETE.name());
    }

    @Test
    @TestRail(id = {6104})
    @Description("Test recosting a cad file - Pocket Recognition")
    public void testRecostPocketRecognition() {
        component = new ComponentRequestUtil().getComponent("case_010_lam_15-435508-00");

        uploadCostScenarioAndAssert(component, "Band Saw", DigitalFactoryEnum.APRIORI_BRAZIL);
    }

    @Test
    @TestRail(id = {6105})
    @Description("Test recosting a cad file - Shared Walls")
    public void testRecostSharedWalls() {
        component = new ComponentRequestUtil().getComponent("case_066_SpaceX_00128711-001_A");

        uploadCostScenarioAndAssert(component, "Band Saw", DigitalFactoryEnum.APRIORI_BRAZIL);
    }

    @Test
    @TestRail(id = {6106})
    @Description("Test recosting a cad file - Slot Examples")
    public void testRecostSlotExamples() {
        component = new ComponentRequestUtil().getComponent("case_007_SpaceX_00088481-001_C");

        uploadCostScenarioAndAssert(component, "Material Stock", DigitalFactoryEnum.APRIORI_BRAZIL);
    }

    private void uploadCostScenarioAndAssert(ComponentInfoBuilder component, final String processRoutingName, final DigitalFactoryEnum digitalFactoryEnum) {

        ComponentInfoBuilder componentResponse = componentsUtil.postComponent(component);

        scenariosUtil.postGroupCostScenarios(
            ComponentInfoBuilder.builder()
                .costingTemplate(CostingTemplate.builder()
                    .processGroupName(component.getProcessGroup().getProcessGroup())
                    .build())
                .componentName(component.getComponentName())
                .scenarioName(component.getScenarioName())
                .componentIdentity(componentResponse.getComponentIdentity())
                .scenarioIdentity(componentResponse.getScenarioIdentity())
                .processGroup(component.getProcessGroup())
                .user(component.getUser())
                .build());

        ResponseWrapper<ComponentIteration> componentIterationResponse = getComponentIterationResponseWrapper(componentResponse);

        AnalysisOfScenario analysisOfScenario = componentIterationResponse.getResponseEntity().getAnalysisOfScenario();

        softAssertions = new SoftAssertions();

        softAssertions.assertThat(analysisOfScenario.getProcessRoutingName()).contains(processRoutingName);

        postCostScenario(
            component.getProcessGroup(), component.getComponentName(), component.getScenarioName(), component.getUser(), componentResponse, digitalFactoryEnum);

        ScenarioResponse scenarioRepresentation = getScenarioResponseResponseWrapper(componentResponse);

        softAssertions.assertThat(scenarioRepresentation.getScenarioState()).isEqualTo(NewCostingLabelEnum.COST_COMPLETE.name());

        softAssertions.assertAll();
    }

    private void postCostScenario(ProcessGroupEnum processGroupEnum, String componentName, String scenarioName, UserCredentials currentUser, ComponentInfoBuilder componentInfo, DigitalFactoryEnum digitalFactory) {
        scenariosUtil.postGroupCostScenarios(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .componentIdentity(componentInfo.getComponentIdentity())
                .scenarioIdentity(componentInfo.getScenarioIdentity())
                .costingTemplate(CostingTemplate.builder()
                    .processGroupName(processGroupEnum.getProcessGroup())
                    .vpeName(digitalFactory.getDigitalFactory())
                    .build())
                .user(currentUser)
                .build());
    }

    private ScenarioResponse getScenarioResponseResponseWrapper(ComponentInfoBuilder componentInfo) {
        return scenariosUtil.getScenarioCompleted(componentInfo);
    }

    private ResponseWrapper<ComponentIteration> getComponentIterationResponseWrapper(ComponentInfoBuilder componentInfo) {
        return iterationsUtil.getComponentIterationLatest(componentInfo);
    }
}