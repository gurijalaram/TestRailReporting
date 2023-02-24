package com.evaluate;

import static org.assertj.core.api.Assertions.assertThat;

import com.apriori.cidappapi.entity.response.CostingTemplate;
import com.apriori.cidappapi.entity.response.scenarios.Routings;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.DataCreationUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.utils.CssComponent;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.io.File;

public class RoutingsTests {
    private final ScenariosUtil scenariosUtil = new ScenariosUtil();
    private SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @Description("Test routings")
    public void cadFormatSLDPRT() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "Machined Box AMERICAS";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(processGroupEnum.getProcessGroup()).build();
        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, costingTemplate, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);

        routings.getItems().forEach(routing -> {
            softAssertions.assertThat(routing.getDigitalFactoryName()).isNotEmpty();
            softAssertions.assertThat(routing.getDisplayName()).isNotEmpty();
            softAssertions.assertThat(routing.getName()).isNotEmpty();
            softAssertions.assertThat(routing.getProcessGroupName()).isNotEmpty();
        });

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"15821"})
    @Description("Verify save routing with costing template through API")
    public void testRecostWithAlternateRouting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_PLASTIC;
        final String componentName = "sheet_plastic";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".STEP");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();


        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);

        routings.getItems().forEach(routing -> {
            softAssertions.assertThat(routing.getDigitalFactoryName()).isNotEmpty();
            softAssertions.assertThat(routing.getDisplayName()).contains("[CTL]/Laser/[Bend]");
            softAssertions.assertThat(routing.getName()).isNotEmpty();
            softAssertions.assertThat(routing.getProcessGroupName()).isNotEmpty();
        });

        softAssertions.assertAll();

    }

    @Test
    @TestRail(testCaseId = {"14980"})
    @Description("Verify Get available routings API returns appropriate routings for Stock Machining")
    public void testAvailableRoutingForStockMachining() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;
        final String componentName = "bracket_basic";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("3 Axis Mill Routing", "4 Axis Mill Routing", "5 Axis Mill Routing",
            "2AL+3AM Routing", "2AL+4AM Routing", "2AL+5AM Routing", "3 Axis Lathe Routing", "MillTurn Routing",
            "3AM+Drill Press Routing", "3AM+4AM Routing", "3AM+5AM Routing", "2ABFL and 3AM routing", "3ABFL routing");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14980"})
    @Description("Verify Get available routings API returns appropriate routings for Sheet Plastic")
    public void testAvailableRoutingForSheetPlastic() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_PLASTIC;
        final String componentName = "5d51749fig01";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("Single Station Thermoforming", "Shuttle Station Thermoforming",
            "3 Station Rotary Thermoforming", "4 Station Rotary Thermoforming");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14978"})
    @Description("Verify Get available routings API returns appropriate routings for Sheet Metal - Transfer Die")
    public void testAvailableRoutingForSheetMetalTransferDie() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE;
        final String componentName = "bracket_basic";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        assertThat(routings.getItems().size()).isEqualTo(0);
    }

    @Test
    @TestRail(testCaseId = {"14977"})
    @Description("Verify Get available routings API returns appropriate routings for Sheet Metal - Stretch Forming")
    public void testAvailableRoutingForSheetMetalStretchForming() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING;
        final String componentName = "Hydroforming";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("Stretch Form Transverse", "Stretch Form Longitudinal");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14976"})
    @Description("Verify Get available routings API returns appropriate routings for Sheet Metal - Roll Forming")
    public void testAvailableRoutingForSheetMetalRollForming() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_ROLLFORMING;
        final String componentName = "angle_bar";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        assertThat(routings.getItems().size()).isEqualTo(0);
    }

    @Test
    @TestRail(testCaseId = {"14975"})
    @Description("Verify Get available routings API returns appropriate routings for Sheet Metal - Hydroforming")
    public void testAvailableRoutingForSheetMetalHydroforming() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_HYDROFORMING;
        final String componentName = "Hydroforming";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("Laser Cut - Fluid Cell Routing", "Router Cut - Fluid Cell Routing",
            "Offline Blank - Fluid Cell Routing", "Laser Cut - Deep Draw Routing", "Router Cut - Deep Draw Routing", "Offline Blank - Deep Draw Routing");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14974"})
    @Description("Verify Get available routings API returns appropriate routings for Sheet Metal")
    public void testAvailableRoutingForSheetMetal() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;
        final String componentName = "bracket_basic";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("[CTL]/Laser/[Bend]", "[CTL]/Fiber Laser/[Bend]", "[CTL]/Laser Punch/[Bend]",
            "[CTL]/Plasma/[Deslag]/[Bend]", "[CTL]/Plasma Punch/[Deslag]/[Bend]", "[CTL]/Oxyfuel/[Deslag]/[Bend]", "[CTL]/Waterjet/[Bend]",
            "[CTL]/Turret/[Bend]", "[CTL]/2 Axis Router/[Bend]", "Stage Tooling", "Prog Die", "[CTL]/Shear/Press", "[CTL]/Shear/Chemical Mill",
            "Tandem Die", "[CTL]/[Bend]");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14973"})
    @Description("Verify Get available routings API returns appropriate routings for Roto & Blow Molding")
    public void testAvailableRoutingForRotoAndBlowMolding() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ROTO_BLOW_MOLDING;
        final String componentName = "Rapid Prototyping";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("Blow Molding", "Rotational Molding");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14972"})
    @Description("Verify Get available routings API returns appropriate routings for Rapid Prototyping")
    public void testAvailableRoutingForRapidPrototyping() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.RAPID_PROTOTYPING;
        final String componentName = "Rapid Prototyping";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("3D Printing", "Selective Laser Sintering", "Stereolithography");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14971"})
    @Description("Verify Get available routings API returns appropriate routings for Powder Metal")
    public void testAvailableRoutingForPowderMetal() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;
        final String componentName = "Powder Metal";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        assertThat(routings.getItems().size()).isEqualTo(0);
    }

    @Test
    @TestRail(testCaseId = {"14970"})
    @Description("Verify Get available routings API returns appropriate routings for Plastic Molding")
    public void testAvailableRoutingForPlasticMolding() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentName = "titan charger lead";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("Injection Mold", "Reaction Injection Mold", "Structural Foam Mold", "Compression Mold");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14969"})
    @Description("Verify Get available routings API returns appropriate routings for Forging")
    public void testAvailableRoutingForForging() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;
        final String componentName = "small ring";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("Closed Die Forging", "Ring Rolled Forging");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14968"})
    @Description("Verify Get available routings API returns appropriate routings for Casting - Sand")
    public void testAvailableRoutingForCastingSand() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_SAND;
        final String componentName = "Casting";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("VerticalAutomatic", "HorizontalAutomatic", "ManualStd", "ManualFloor", "ManualPit");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14967"})
    @Description("Verify Get available routings API returns appropriate routings for Casting - Investment")
    public void testAvailableRoutingForCastingInvestment() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_INVESTMENT;
        final String componentName = "piston_model1";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("Band Saw", "Abrasive Wheel Cut");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14966"})
    @Description("Verify Get available routings API returns appropriate routings for Casting - Die")
    public void testAvailableRoutingForCastingDie() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;
        final String componentName = "DTCCastingIssues";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".catpart");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("High Pressure Die Cast", "Gravity Die Cast");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14965"})
    @Description("Verify Get available routings API returns appropriate routings for Casting")
    public void testAvailableRoutingForCasting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING;
        final String componentName = "CurvedWall";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        softAssertions.assertThat(routings.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(routings.getItems()).extracting("name").containsExactlyInAnyOrder("SandCasting", "DieCasting", "Permanent Mold");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14964"})
    @Description("Verify Get available routings API returns appropriate routings for Bar & Tube Fab")
    public void testAvailableRoutingForBarAndTubeFab() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.BAR_TUBE_FAB;
        final String componentName = "CurvedWall";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        assertThat(routings.getItems().size()).isEqualTo(0);
    }

    @Test
    @TestRail(testCaseId = {"14963"})
    @Description("Verify Get available routings API returns appropriate routings for Additive Manufacturing")
    public void testAvailableRoutingForAdditiveManufacturing() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ADDITIVE_MANUFACTURING;
        final String componentName = "testpart-4";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        final UserCredentials currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ScenarioResponse scenarioResponse = new DataCreationUtil(componentName, scenarioName, processGroupEnum, resourceFile, currentUser).createCostComponent();

        Routings routings = scenariosUtil.getRoutings(currentUser, new CssComponent().findFirst(componentName, scenarioName, currentUser).getComponentIdentity(),
            scenarioResponse.getIdentity()).getResponseEntity();

        assertThat(routings.getItems().size()).isEqualTo(0);
    }


}
