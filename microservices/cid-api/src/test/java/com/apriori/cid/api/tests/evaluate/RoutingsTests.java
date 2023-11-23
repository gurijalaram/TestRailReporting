package com.apriori.cid.api.tests.evaluate;

import static org.assertj.core.api.Assertions.assertThat;

import com.apriori.cid.api.models.response.scenarios.Routings;
import com.apriori.cid.api.models.response.scenarios.ScenarioResponse;
import com.apriori.cid.api.utils.ComponentsUtil;
import com.apriori.cid.api.utils.DataCreationUtil;
import com.apriori.cid.api.utils.IterationsUtil;
import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dto.ComponentRequestUtil;
import com.apriori.shared.util.enums.NewCostingLabelEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.request.component.RoutingNodeOptions;
import com.apriori.shared.util.models.response.component.CostingTemplate;
import com.apriori.shared.util.models.response.component.componentiteration.AnalysisOfScenario;
import com.apriori.shared.util.models.response.component.componentiteration.ComponentIteration;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class RoutingsTests {
    private final ScenariosUtil scenariosUtil = new ScenariosUtil();
    private final SoftAssertions softAssertions = new SoftAssertions();
    private final ComponentsUtil componentsUtil = new ComponentsUtil();
    private final IterationsUtil iterationsUtil = new IterationsUtil();
    private ComponentInfoBuilder component;
    private Routings routings;

    @Test
    @TestRail(id = {14982})
    @Description("Verify Get latest iteration API does not return scenarioRoutings upon costing to COSTING_FAILED")
    public void testLatestIterationDoesNotReturnScenarioRoutingsForCostingFailedState() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL_ROLLFORMING);

        softAssertions.assertThat(getIterationLatest(component, NewCostingLabelEnum.COSTING_FAILED).getResponseEntity().getScenarioRoutings().size()).isEqualTo(0);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14981})
    @Description("Verify Get latest iteration API contains scenarioRoutings upon costing to COST_INCOMPLETE")
    public void testLatestIterationReturnsScenarioRoutingsForCostIncompleteState() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING_SAND);

        softAssertions.assertThat(getIterationLatest(component, NewCostingLabelEnum.COST_INCOMPLETE).getResponseEntity().getScenarioRoutings().size()).isGreaterThan(0);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14961})
    @Description("Verify Get latest iteration API contains scenarioRoutings upon costing to COST_COMPLETE")
    public void testLatestIterationReturnsScenarioRoutingsForCostCompleteState() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_PLASTIC);

        softAssertions.assertThat(getIterationLatest(component, NewCostingLabelEnum.COST_COMPLETE).getResponseEntity().getScenarioRoutings().size()).isGreaterThan(0);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14980})
    @Description("Verify Get available routings API returns appropriate routings for Stock Machining")
    public void testAvailableRoutingForStockMachining() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.STOCK_MACHINING);

        routings = getRoutings(component);

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("3 Axis Mill Routing", "4 Axis Mill Routing", "5 Axis Mill Routing",
            "2AL+3AM Routing", "2AL+4AM Routing", "2AL+5AM Routing", "3 Axis Lathe Routing", "MillTurn Routing",
            "3AM+Drill Press Routing", "3AM+4AM Routing", "3AM+5AM Routing", "2ABFL and 3AM routing", "3ABFL routing");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14979})
    @Description("Verify Get available routings API returns appropriate routings for Sheet Plastic")
    public void testAvailableRoutingForSheetPlastic() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_PLASTIC);

        routings = getRoutings(component);

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("Single Station Thermoforming", "Shuttle Station Thermoforming",
            "3 Station Rotary Thermoforming", "4 Station Rotary Thermoforming");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14978})
    @Description("Verify Get available routings API returns appropriate routings for Sheet Metal - Transfer Die")
    public void testAvailableRoutingForSheetMetalTransferDie() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE);

        routings = getRoutings(component);

        assertThat(routings.getItems().size()).isEqualTo(0);
    }

    @Test
    @TestRail(id = {14977})
    @Description("Verify Get available routings API returns appropriate routings for Sheet Metal - Stretch Forming")
    public void testAvailableRoutingForSheetMetalStretchForming() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING);

        routings = getRoutings(component);

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("Stretch Form Transverse", "Stretch Form Longitudinal");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14976})
    @Description("Verify Get available routings API returns appropriate routings for Sheet Metal - Roll Forming")
    public void testAvailableRoutingForSheetMetalRollForming() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL_HYDROFORMING);

        routings = getRoutings(component);

        assertThat(routings.getItems().size()).isEqualTo(0);
    }

    @Test
    @TestRail(id = {14975})
    @Description("Verify Get available routings API returns appropriate routings for Sheet Metal - Hydroforming")
    public void testAvailableRoutingForSheetMetalHydroforming() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL_HYDROFORMING);

        routings = getRoutings(component);

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("Laser Cut - Fluid Cell Routing", "Router Cut - Fluid Cell Routing",
            "Offline Blank - Fluid Cell Routing", "Laser Cut - Deep Draw Routing", "Router Cut - Deep Draw Routing", "Offline Blank - Deep Draw Routing");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14974})
    @Description("Verify Get available routings API returns appropriate routings for Sheet Metal")
    public void testAvailableRoutingForSheetMetal() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL);

        routings = getRoutings(component);

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("[CTL]/Laser/[Bend]", "[CTL]/Fiber Laser/[Bend]", "[CTL]/Laser Punch/[Bend]",
            "[CTL]/Plasma/[Deslag]/[Bend]", "[CTL]/Plasma Punch/[Deslag]/[Bend]", "[CTL]/Oxyfuel/[Deslag]/[Bend]", "[CTL]/Waterjet/[Bend]",
            "[CTL]/Turret/[Bend]", "[CTL]/2 Axis Router/[Bend]", "Stage Tooling", "Prog Die", "[CTL]/Shear/Press", "[CTL]/Shear/Chemical Mill",
            "Tandem Die", "[CTL]/[Bend]");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14973})
    @Description("Verify Get available routings API returns appropriate routings for Roto & Blow Molding")
    public void testAvailableRoutingForRotoAndBlowMolding() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.ROTO_BLOW_MOLDING);

        routings = getRoutings(component);

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("Blow Molding", "Rotational Molding");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14972})
    @Description("Verify Get available routings API returns appropriate routings for Rapid Prototyping")
    public void testAvailableRoutingForRapidPrototyping() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.RAPID_PROTOTYPING);

        routings = getRoutings(component);

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("3D Printing", "Selective Laser Sintering", "Stereolithography");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14971})
    @Description("Verify Get available routings API returns appropriate routings for Powder Metal")
    public void testAvailableRoutingForPowderMetal() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.POWDER_METAL);

        routings = getRoutings(component);

        assertThat(routings.getItems().size()).isEqualTo(0);
    }

    @Test
    @TestRail(id = {14970})
    @Description("Verify Get available routings API returns appropriate routings for Plastic Molding")
    public void testAvailableRoutingForPlasticMolding() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING);

        routings = getRoutings(component);

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("Injection Mold", "Reaction Injection Mold", "Structural Foam Mold", "Compression Mold");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14969})
    @Description("Verify Get available routings API returns appropriate routings for Forging")
    public void testAvailableRoutingForForging() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.FORGING);

        routings = getRoutings(component);

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("Closed Die Forging", "Ring Rolled Forging");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14968})
    @Description("Verify Get available routings API returns appropriate routings for Casting - Sand")
    public void testAvailableRoutingForCastingSand() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING_SAND);

        routings = getRoutings(component);

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("VerticalAutomatic", "HorizontalAutomatic", "ManualStd", "ManualFloor", "ManualPit");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14967})
    @Description("Verify Get available routings API returns appropriate routings for Casting - Investment")
    public void testAvailableRoutingForCastingInvestment() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING_INVESTMENT);

        routings = getRoutings(component);

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("Band Saw", "Abrasive Wheel Cut");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14966})
    @Description("Verify Get available routings API returns appropriate routings for Casting - Die")
    public void testAvailableRoutingForCastingDie() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING_DIE);

        routings = getRoutings(component);

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("High Pressure Die Cast", "Gravity Die Cast");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14965})
    @Description("Verify Get available routings API returns appropriate routings for Casting")
    public void testAvailableRoutingForCasting() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING);

        routings = getRoutings(component);

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("SandCasting", "DieCasting", "Permanent Mold");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14964})
    @Description("Verify Get available routings API returns appropriate routings for Bar & Tube Fab")
    public void testAvailableRoutingForBarAndTubeFab() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.BAR_TUBE_FAB);

        routings = getRoutings(component);

        assertThat(routings.getItems().size()).isEqualTo(0);
    }

    @Test
    @TestRail(id = {14963})
    @Description("Verify Get available routings API returns appropriate routings for Additive Manufacturing")
    public void testAvailableRoutingForAdditiveManufacturing() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING);

        routings = getRoutings(component);

        assertThat(routings.getItems().size()).isEqualTo(0);
    }

    @Test
    @TestRail(id = {14962})
    @Description("Verify Get available routings API returns appropriate routings for 2-Model Machining")
    public void testAvailableRoutingForTwoModelMachining() {
        component = new ComponentRequestUtil().getComponent("casting_BEFORE_machining");
        ComponentInfoBuilder component2 = new ComponentRequestUtil().getComponent("casting_AFTER_machining");

        CostingTemplate costingTemplate1 = CostingTemplate.builder().processGroupName(component.getProcessGroup().getProcessGroup()).build();
        ScenarioResponse scenarioResponse1 = new DataCreationUtil(component.getComponentName(), component.getScenarioName(), component.getProcessGroup(),
            component.getResourceFile(), costingTemplate1, component.getUser()).createCostComponent();

        CostingTemplate costingTemplate2 = CostingTemplate.builder().processGroupName(component2.getProcessGroup().getProcessGroup()).twoModelSourceScenarioIdentity(scenarioResponse1.getIdentity()).build();
        ScenarioResponse scenarioResponse2 = new DataCreationUtil(component2.getComponentName(), component.getScenarioName(), component2.getProcessGroup(), component2.getResourceFile(), costingTemplate2,
            component.getUser()).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(component.getUser(), Routings.class, new CssComponent().findFirst(component2.getComponentName(), component.getScenarioName(), component.getUser()).getComponentIdentity(),
            scenarioResponse2.getIdentity(), costingTemplate2.getVpeName(), ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("3 Axis Mill Routing", "4 Axis Mill Routing", "5 Axis Mill Routing", "2AL+3AM Routing",
            "2AL+4AM Routing", "2AL+5AM Routing", "3 Axis Lathe Routing", "MillTurn Routing", "Drill Press Routing");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {15821})
    @Description("Verify save routing with costing template through API")
    public void testSaveRouting() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL);

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(component.getProcessGroup().getProcessGroup()).build();

        ComponentInfoBuilder componentResponse = componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
            .componentName(component.getComponentName())
            .scenarioName(component.getScenarioName())
            .resourceFile(component.getResourceFile())
            .user(component.getUser())
            .costingTemplate(costingTemplate)
            .build());

        scenariosUtil.postCostScenario(componentResponse);

        ResponseWrapper<ComponentIteration> componentIterationResponse = iterationsUtil.getComponentIterationLatest(componentResponse);

        AnalysisOfScenario analysisOfScenario = componentIterationResponse.getResponseEntity().getAnalysisOfScenario();

        softAssertions.assertThat(analysisOfScenario.getProcessRoutingName()).isEqualTo("Material Stock / Turret Press / Bend Brake");

        RoutingNodeOptions option = new RoutingNodeOptions(componentResponse.getScenarioIdentity(), "aPriori USA", "[CTL]/Laser Punch/[Bend]", "Sheet Metal");
        List<RoutingNodeOptions> routingNodeOptions = new ArrayList<>();
        routingNodeOptions.add(option);

        CostingTemplate costingTemplateWithRouting = CostingTemplate.builder().processGroupName(component.getProcessGroup().getProcessGroup()).routingNodeOptions(routingNodeOptions).build();

        componentResponse.setCostingTemplate(costingTemplateWithRouting);
        scenariosUtil.postCostScenario(componentResponse);

        ResponseWrapper<ComponentIteration> componentIterationResponseWithRouting = iterationsUtil.getComponentIterationLatest(componentResponse);

        AnalysisOfScenario analysisOfScenarioWithRouting = componentIterationResponseWithRouting.getResponseEntity().getAnalysisOfScenario();

        softAssertions.assertThat(analysisOfScenarioWithRouting.getProcessRoutingName()).isEqualTo("Material Stock / Laser Punch / Bend Brake");

        softAssertions.assertAll();
    }

    private Routings getRoutings(ComponentInfoBuilder component) {
        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(component.getProcessGroup().getProcessGroup()).build();
        ScenarioResponse scenarioResponse = new DataCreationUtil(component.getComponentName(), component.getScenarioName(), component.getProcessGroup(),
            component.getResourceFile(), costingTemplate, component.getUser())
            .createCostComponent();

        return scenariosUtil.getRoutings(component.getUser(), Routings.class, new CssComponent().findFirst(component.getComponentName(), component.getScenarioName(),
                    component.getUser())
                .getComponentIdentity(),
            scenarioResponse.getIdentity(),
            component.getCostingTemplate().getVpeName(),
            component.getProcessGroup().getProcessGroup()).getResponseEntity();
    }

    private ResponseWrapper<ComponentIteration> getIterationLatest(ComponentInfoBuilder component, NewCostingLabelEnum costingLabel) {

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(component.getProcessGroup().getProcessGroup()).build();

        ComponentInfoBuilder componentResponse = componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
            .componentName(component.getComponentName())
            .scenarioName(component.getScenarioName())
            .resourceFile(component.getResourceFile())
            .user(component.getUser())
            .costingTemplate(costingTemplate)
            .build());

        scenariosUtil.postCostScenario(componentResponse);

        ScenarioResponse scenarioRepresentation = scenariosUtil.getScenarioCompleted(componentResponse);

        softAssertions.assertThat(scenarioRepresentation.getScenarioState()).isEqualTo(costingLabel.name());

        return iterationsUtil.getComponentIterationLatest(componentResponse);
    }
}