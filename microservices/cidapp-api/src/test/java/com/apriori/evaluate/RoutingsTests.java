package com.apriori.evaluate;

import static org.assertj.core.api.Assertions.assertThat;

import com.apriori.FileResourceUtil;
import com.apriori.GenerateStringUtil;
import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.CostingTemplate;
import com.apriori.cidappapi.entity.response.RoutingNodeOptions;
import com.apriori.cidappapi.entity.response.componentiteration.AnalysisOfScenario;
import com.apriori.cidappapi.entity.response.componentiteration.ComponentIteration;
import com.apriori.cidappapi.entity.response.scenarios.Routings;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.DataCreationUtil;
import com.apriori.cidappapi.utils.IterationsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.entity.request.ErrorRequestResponse;
import com.apriori.enums.NewCostingLabelEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;
import com.apriori.utils.CssComponent;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RoutingsTests {
    private final ScenariosUtil scenariosUtil = new ScenariosUtil();
    private final SoftAssertions softAssertions = new SoftAssertions();
    private final ComponentsUtil componentsUtil = new ComponentsUtil();
    private final IterationsUtil iterationsUtil = new IterationsUtil();

    @Test
    @TestRail(id = {14983})
    @Description("Verify Get available routings API does not return routings when scenario is in NOT_COSTED status")
    public void testAvailableRoutingForUncostedPart() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_PLASTIC;
        final String componentName = "sheet_plastic";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".STEP");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder scenarioItem = componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build());

        ErrorRequestResponse errorRequestResponse = scenariosUtil.getRoutings(currentUser, ErrorRequestResponse.class, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioItem.getScenarioIdentity()).getResponseEntity();

        String expectedMessage = String.format("Scenario '%s' was not costed, or was costed with invalid cost inputs, available routings not found", scenarioName);

        softAssertions.assertThat(errorRequestResponse.getMessage()).isEqualTo(expectedMessage);
        softAssertions.assertThat(errorRequestResponse.getStatus()).isEqualTo("409");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14982})
    @Description("Verify Get latest iteration API does not return scenarioRoutings upon costing to COSTING_FAILED")
    public void testLatestIterationDoesNotReturnScenarioRoutingsForCostingFailedState() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_ROLLFORMING;
        final String componentName = "Casting";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroupEnum.getProcessGroup()).build();

        ComponentInfoBuilder componentResponse = componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .resourceFile(resourceFile)
            .user(currentUser)
            .costingTemplate(costingTemplate)
            .build());

        scenariosUtil.postCostScenario(componentResponse);

        ScenarioResponse scenarioRepresentation = scenariosUtil.getScenarioCompleted(componentResponse);

        softAssertions.assertThat(scenarioRepresentation.getScenarioState()).isEqualTo(NewCostingLabelEnum.COSTING_FAILED.name());

        ResponseWrapper<ComponentIteration> componentIterationResponse = iterationsUtil.getComponentIterationLatest(componentResponse);

        softAssertions.assertThat(componentIterationResponse.getResponseEntity().getScenarioRoutings().size()).isEqualTo(0);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14981})
    @Description("Verify Get latest iteration API contains scenarioRoutings upon costing to COST_INCOMPLETE")
    public void testLatestIterationReturnsScenarioRoutingsForCostIncompleteState() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_SAND;
        final String componentName = "DTCCastingIssues";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".catpart");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroupEnum.getProcessGroup()).build();

        ComponentInfoBuilder componentResponse = componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .resourceFile(resourceFile)
            .user(currentUser)
            .costingTemplate(costingTemplate)
            .build());

        scenariosUtil.postCostScenario(componentResponse);

        ScenarioResponse scenarioRepresentation = scenariosUtil.getScenarioCompleted(componentResponse);

        softAssertions.assertThat(scenarioRepresentation.getScenarioState()).isEqualTo(NewCostingLabelEnum.COST_INCOMPLETE.name());

        ResponseWrapper<ComponentIteration> componentIterationResponse = iterationsUtil.getComponentIterationLatest(componentResponse);

        softAssertions.assertThat(componentIterationResponse.getResponseEntity().getScenarioRoutings().size()).isGreaterThan(0);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14961})
    @Description("Verify Get latest iteration API contains scenarioRoutings upon costing to COST_COMPLETE")
    public void testLatestIterationReturnsScenarioRoutingsForCostCompleteState() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_PLASTIC;
        final String componentName = "5d51749fig01";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroupEnum.getProcessGroup()).build();

        ComponentInfoBuilder componentResponse = componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .resourceFile(resourceFile)
            .user(currentUser)
            .costingTemplate(costingTemplate)
            .build());

        scenariosUtil.postCostScenario(componentResponse);

        ScenarioResponse scenarioRepresentation = scenariosUtil.getScenarioCompleted(componentResponse);

        softAssertions.assertThat(scenarioRepresentation.getScenarioState()).isEqualTo(NewCostingLabelEnum.COST_COMPLETE.name());

        ResponseWrapper<ComponentIteration> componentIterationResponse = iterationsUtil.getComponentIterationLatest(componentResponse);

        softAssertions.assertThat(componentIterationResponse.getResponseEntity().getScenarioRoutings().size()).isGreaterThan(0);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14980})
    @Description("Verify Get available routings API returns appropriate routings for Stock Machining")
    public void testAvailableRoutingForStockMachining() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "bracket_basic";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroupEnum.getProcessGroup()).build();
        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, costingTemplate, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, Routings.class, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

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
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_PLASTIC;
        final String componentName = "5d51749fig01";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroupEnum.getProcessGroup()).build();
        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, costingTemplate, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, Routings.class, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("Single Station Thermoforming", "Shuttle Station Thermoforming",
            "3 Station Rotary Thermoforming", "4 Station Rotary Thermoforming");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14978})
    @Description("Verify Get available routings API returns appropriate routings for Sheet Metal - Transfer Die")
    public void testAvailableRoutingForSheetMetalTransferDie() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE;
        final String componentName = "bracket_basic";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroupEnum.getProcessGroup()).build();
        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, costingTemplate, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, Routings.class, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        assertThat(routings.getItems().size()).isEqualTo(0);
    }

    @Test
    @TestRail(id = {14977})
    @Description("Verify Get available routings API returns appropriate routings for Sheet Metal - Stretch Forming")
    public void testAvailableRoutingForSheetMetalStretchForming() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING;
        final String componentName = "Hydroforming";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroupEnum.getProcessGroup()).build();
        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, costingTemplate, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, Routings.class, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("Stretch Form Transverse", "Stretch Form Longitudinal");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14976})
    @Description("Verify Get available routings API returns appropriate routings for Sheet Metal - Roll Forming")
    public void testAvailableRoutingForSheetMetalRollForming() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_ROLLFORMING;
        final String componentName = "angle_bar";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroupEnum.getProcessGroup()).build();
        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, costingTemplate, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, Routings.class, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        assertThat(routings.getItems().size()).isEqualTo(0);
    }

    @Test
    @TestRail(id = {14975})
    @Description("Verify Get available routings API returns appropriate routings for Sheet Metal - Hydroforming")
    public void testAvailableRoutingForSheetMetalHydroforming() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_HYDROFORMING;
        final String componentName = "Hydroforming";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroupEnum.getProcessGroup()).build();
        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, costingTemplate, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, Routings.class, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("Laser Cut - Fluid Cell Routing", "Router Cut - Fluid Cell Routing",
            "Offline Blank - Fluid Cell Routing", "Laser Cut - Deep Draw Routing", "Router Cut - Deep Draw Routing", "Offline Blank - Deep Draw Routing");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14974})
    @Description("Verify Get available routings API returns appropriate routings for Sheet Metal")
    public void testAvailableRoutingForSheetMetal() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;
        final String componentName = "bracket_basic";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroupEnum.getProcessGroup()).build();
        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, costingTemplate, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, Routings.class, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

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
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ROTO_BLOW_MOLDING;
        final String componentName = "Rapid Prototyping";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroupEnum.getProcessGroup()).build();
        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, costingTemplate, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, Routings.class, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("Blow Molding", "Rotational Molding");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14972})
    @Description("Verify Get available routings API returns appropriate routings for Rapid Prototyping")
    public void testAvailableRoutingForRapidPrototyping() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.RAPID_PROTOTYPING;
        final String componentName = "Rapid Prototyping";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroupEnum.getProcessGroup()).build();
        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, costingTemplate, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, Routings.class, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("3D Printing", "Selective Laser Sintering", "Stereolithography");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14971})
    @Description("Verify Get available routings API returns appropriate routings for Powder Metal")
    public void testAvailableRoutingForPowderMetal() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;
        final String componentName = "Powder Metal";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroupEnum.getProcessGroup()).build();
        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, costingTemplate, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, Routings.class, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        assertThat(routings.getItems().size()).isEqualTo(0);
    }

    @Test
    @TestRail(id = {14970})
    @Description("Verify Get available routings API returns appropriate routings for Plastic Molding")
    public void testAvailableRoutingForPlasticMolding() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentName = "titan charger lead";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroupEnum.getProcessGroup()).build();
        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, costingTemplate, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, Routings.class, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("Injection Mold", "Reaction Injection Mold", "Structural Foam Mold", "Compression Mold");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14969})
    @Description("Verify Get available routings API returns appropriate routings for Forging")
    public void testAvailableRoutingForForging() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;
        final String componentName = "small ring";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroupEnum.getProcessGroup()).build();
        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, costingTemplate, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, Routings.class, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("Closed Die Forging", "Ring Rolled Forging");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14968})
    @Description("Verify Get available routings API returns appropriate routings for Casting - Sand")
    public void testAvailableRoutingForCastingSand() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_SAND;
        final String componentName = "Casting";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroupEnum.getProcessGroup()).build();
        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, costingTemplate, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, Routings.class, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("VerticalAutomatic", "HorizontalAutomatic", "ManualStd", "ManualFloor", "ManualPit");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14967})
    @Description("Verify Get available routings API returns appropriate routings for Casting - Investment")
    public void testAvailableRoutingForCastingInvestment() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_INVESTMENT;
        final String componentName = "piston_model1";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroupEnum.getProcessGroup()).build();
        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, costingTemplate, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, Routings.class, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("Band Saw", "Abrasive Wheel Cut");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14966})
    @Description("Verify Get available routings API returns appropriate routings for Casting - Die")
    public void testAvailableRoutingForCastingDie() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;
        final String componentName = "DTCCastingIssues";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".catpart");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroupEnum.getProcessGroup()).build();
        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, costingTemplate, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, Routings.class, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("High Pressure Die Cast", "Gravity Die Cast");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14965})
    @Description("Verify Get available routings API returns appropriate routings for Casting")
    public void testAvailableRoutingForCasting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING;
        final String componentName = "CurvedWall";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroupEnum.getProcessGroup()).build();
        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, costingTemplate, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, Routings.class, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("SandCasting", "DieCasting", "Permanent Mold");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14964})
    @Description("Verify Get available routings API returns appropriate routings for Bar & Tube Fab")
    public void testAvailableRoutingForBarAndTubeFab() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.BAR_TUBE_FAB;
        final String componentName = "B&T-LOW-001";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroupEnum.getProcessGroup()).build();
        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, costingTemplate, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, Routings.class, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        assertThat(routings.getItems().size()).isEqualTo(0);
    }

    @Test
    @TestRail(id = {14963})
    @Description("Verify Get available routings API returns appropriate routings for Additive Manufacturing")
    public void testAvailableRoutingForAdditiveManufacturing() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ADDITIVE_MANUFACTURING;
        final String componentName = "testpart-4";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroupEnum.getProcessGroup()).build();
        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, costingTemplate, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, Routings.class, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        assertThat(routings.getItems().size()).isEqualTo(0);
    }

    @Test
    @TestRail(id = {14962})
    @Description("Verify Get available routings API returns appropriate routings for 2-Model Machining")
    public void testAvailableRoutingForTwoModelMachining() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;
        final String componentName = "casting_BEFORE_machining";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        final ProcessGroupEnum processGroupEnum2 = ProcessGroupEnum.TWO_MODEL_MACHINING;
        final String componentName2 = "casting_AFTER_machining";
        final File resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum2, componentName2 + ".stp");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate1 = CostingTemplate.builder().processGroupName(processGroupEnum.getProcessGroup()).build();
        ScenarioResponse scenarioResponse1 = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, costingTemplate1, currentUser).createCostComponent();

        CostingTemplate costingTemplate2 = CostingTemplate.builder().processGroupName(processGroupEnum2.getProcessGroup()).twoModelSourceScenarioIdentity(scenarioResponse1.getIdentity()).build();
        ScenarioResponse scenarioResponse2 = new DataCreationUtil(componentName2, scenarioName, processGroupEnum2, resourceFile2, costingTemplate2, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, Routings.class, new CssComponent().findFirst(componentName2, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse2.getIdentity()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("3 Axis Mill Routing", "4 Axis Mill Routing", "5 Axis Mill Routing", "2AL+3AM Routing",
            "2AL+4AM Routing", "2AL+5AM Routing", "3 Axis Lathe Routing", "MillTurn Routing", "Drill Press Routing");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {15821})
    @Description("Verify save routing with costing template through API")
    public void testSaveRouting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;
        final String componentName = "bracket_basic";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroupEnum.getProcessGroup()).build();

        ComponentInfoBuilder componentResponse = componentsUtil.postComponentQueryCID(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .resourceFile(resourceFile)
            .user(currentUser)
            .costingTemplate(costingTemplate)
            .build());

        scenariosUtil.postCostScenario(componentResponse);

        ResponseWrapper<ComponentIteration> componentIterationResponse = iterationsUtil.getComponentIterationLatest(componentResponse);

        AnalysisOfScenario analysisOfScenario = componentIterationResponse.getResponseEntity().getAnalysisOfScenario();

        softAssertions.assertThat(analysisOfScenario.getProcessRoutingName()).isEqualTo("Material Stock / Turret Press / Bend Brake");

        RoutingNodeOptions option = new RoutingNodeOptions(componentResponse.getScenarioIdentity(), "aPriori USA", "[CTL]/Laser Punch/[Bend]", "Sheet Metal");
        List<RoutingNodeOptions> routingNodeOptions = new ArrayList<>();
        routingNodeOptions.add(option);

        CostingTemplate costingTemplateWithRouting = CostingTemplate.builder().processGroupName(processGroupEnum.getProcessGroup()).routingNodeOptions(routingNodeOptions).build();

        componentResponse.setCostingTemplate(costingTemplateWithRouting);
        scenariosUtil.postCostScenario(componentResponse);

        ResponseWrapper<ComponentIteration> componentIterationResponseWithRouting = iterationsUtil.getComponentIterationLatest(componentResponse);

        AnalysisOfScenario analysisOfScenarioWithRouting = componentIterationResponseWithRouting.getResponseEntity().getAnalysisOfScenario();

        softAssertions.assertThat(analysisOfScenarioWithRouting.getProcessRoutingName()).isEqualTo("Material Stock / Laser Punch / Bend Brake");

        softAssertions.assertAll();
    }
}