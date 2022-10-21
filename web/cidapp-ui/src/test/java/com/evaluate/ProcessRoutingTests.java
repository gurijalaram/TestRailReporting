package com.evaluate;

import com.apriori.cidappapi.utils.UserPreferencesUtil;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.MaterialSelectorPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.GuidanceIssuesPage;
import com.apriori.pageobjects.pages.evaluate.inputs.RoutingSelectionPage;
import com.apriori.pageobjects.pages.evaluate.materialprocess.MaterialProcessPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Offset;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class ProcessRoutingTests extends TestBase {
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private MaterialProcessPage materialProcessPage;
    private RoutingSelectionPage routingSelectionPage;
    private MaterialSelectorPage materialSelectorPage;
    private GuidanceIssuesPage guidanceIssuesPage;

    private File resourceFile;
    private UserCredentials currentUser;
    private SoftAssertions softAssertions = new SoftAssertions();

    public ProcessRoutingTests() {
        super();
    }

    @After
    public void resetAllSettings() {
        if (currentUser != null) {
            new UserPreferencesUtil().resetSettings(currentUser);
        }
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"14404", "15002", "15816"})
    @Description("Validate the user can Change the process routing in CI Design")
    public void testAlternateRoutingSelection() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("3 Axis Mill Routing")
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("3 Axis Mill");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14992", "15806"})
    @Description("Validate the user can Change the process routing")
    public void changeRouting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "Push Pin";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .openMaterialSelectorTable()
            .selectMaterial("Polyurethane, Polymeric MDI")
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("Reaction Injection Mold")
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Reaction Injection Molding");

        routingSelectionPage = evaluatePage.goToAdvancedTab()
            .openRoutingSelection();
        softAssertions.assertThat(routingSelectionPage.getAvailableRoutings()).contains("Injection Mold", "Reaction Injection Mold", "Structural Foam Mold");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"7854"})
    @Description("Validate the Use selected for future costing checkbox works correctly")
    public void testLetAprioriDecide() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "plasticLid";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("Structural Foam Mold")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectLetAprioriDecide()
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Injection Molding");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"15012"})
    @Description("Validate the information updates in the routing modal box")
    public void testLastRouting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "CastedPart";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        routingSelectionPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .goToAdvancedTab()
            .openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.isAprioriLogoDisplayed("High Pressure Die Cast")).isEqualTo(true);

        routingSelectionPage = new RoutingSelectionPage(driver);
        routingSelectionPage = routingSelectionPage
            .selectRoutingPreferenceByName("Gravity Die Cast")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.getCostStatusValue("Gravity Die Cast")).isEqualTo("Cost Complete");
        softAssertions.assertAll();
    }

    @Test
    @Ignore("Due to update 14/10/22 routings can't be change for additive manufacturing")
    @TestRail(testCaseId = {"7855", "14985", "15799"})
    @Description("Validate behaviour when forcing a material that will fail costing within CID")
    public void failCostingRouting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ADDITIVE_MANUFACTURING;

        String componentName = "CastedPart";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("Vat Photopolymerization")
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COSTING_FAILED)).isEqualTo(true);

        evaluatePage.openDesignGuidance()
            .selectIssueTypeGcd("Costing Failed", "Additive Manufacturing/Surface Treatment is infeasible", "Component:1");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("This DMLS material is not compatible with Stereolithography.");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"7844"})
    @Description("Validate costing results update accordingly for a newly selected and costed routing")
    public void costUpdatedRouting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "HoleProximityTest";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(1.56), Offset.offset(5.0));

        evaluatePage.goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("[CTL]/Waterjet/[Bend]")
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(1.96), Offset.offset(5.0));

        materialProcessPage = evaluatePage.openMaterialProcess()
            .selectBarChart("Waterjet Cut")
            .selectOptionsTab()
            .selectPartOrientation("Position Bend with Smallest Radius Parallel to Grain")
            .selecGrainDirection("Parallel to Sheet Length")
            .inputMinimumRecommendedHoleDiameter("0.5")
            .closePanel()
            .costScenario()
            .openMaterialProcess()
            .selectBarChart("Waterjet Cut")
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.getPartOrientation()).contains("Position Bend with Smallest Radius Parallel to Grain");
        softAssertions.assertThat(materialProcessPage.getGrainDirection()).contains("Parallel to Sheet Length");
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Minimum Recommended Hole Diameter")).isEqualTo(0.5);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"7846"})
    @Description("Validate materials selected are appropriate for selected routing.")
    public void routingMaterials() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "plasticLid";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        materialSelectorPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("Structural Foam Mold")
            .submit(EvaluatePage.class)
            .costScenario()
            .openMaterialSelectorTable()
            .selectMaterial("ABS");

        softAssertions.assertThat(materialSelectorPage.getListOfMaterialTypes())
            .containsExactlyInAnyOrder("All", "ABS", "Acetal", "Acrylic", "Nylon", "PBT", "PET", "PPS", "Polycarbonate", "Polypropylene", "Polystyrene",
                "Polyurethane", "TPA", "TPE", "TPO", "TPS", "TPU", "TPV");
        softAssertions.assertAll();
    }

    @Test
    @Ignore("Due to update 14/10/22 routings can't be change for additive manufacturing")
    @TestRail(testCaseId = {"7850"})
    @Description("Validate behaviour when selecting a PG that auto triggers a secondary process")
    public void routingSecondaryPG() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ADDITIVE_MANUFACTURING;

        String componentName = "CastedPart";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .openMaterialSelectorTable()
            .selectMaterial("Visijet M3 Black")
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("Material Jetting")
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Printing / Breakoff");

        evaluatePage.openMaterialSelectorTable()
            .selectMaterial("Aluminum AlSi10Mg")
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("Powder Bed Fusion / Direct Metal Laser Sintering")
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Stress Relief / Ultrasonic Cleaning");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"7848"})
    @Description("Validate a variety of secondary processes can be added for newly selected routings")
    public void secondaryProcessesRoutings() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "PMI_AllTolTypesCatia";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("Structural Foam Mold")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToOtherSecProcessesTab()
            .selectSecondaryProcess("Packaging")
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("Injection Mold")
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Carton Forming", "Pack & Load", "Carton Sealing");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"7859"})
    @Description("Validate user cannot select a routing that does not belong to a certain Process Group")
    public void routingPGs() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "plasticLid";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        routingSelectionPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .goToAdvancedTab()
            .openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.getRoutingStates("Injection Mold")).doesNotContain("MillTurn Routing", "Die Casting");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"16132"})
    @Description("Be able to see basic breakdown of cycle time by process for problem identification.")
    public void routingCycleTime() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        String componentName = "Push Pin";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        materialProcessPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .openMaterialProcess()
            .selectBarChart("Band Saw");

        softAssertions.assertThat(materialProcessPage.getProcessPercentage("Band Saw")).contains("16.23s (52.51%)");
        materialProcessPage.selectProcessTab();

        softAssertions.assertThat(materialProcessPage.getProcessResult("Machine Name")).contains("DoAll 3613-1 Vert");
        materialProcessPage.selectBarChart("2 Axis Lathe");

        softAssertions.assertThat(materialProcessPage.getTotalResult("Cycle Time")).isCloseTo(Double.valueOf(15.99), Offset.offset(5.0));
        softAssertions.assertThat(materialProcessPage.getProcessResult("Machine Name")).contains("Virtual 2 Axis Lathe - Small");

        routingSelectionPage = materialProcessPage.closePanel()
            .goToAdvancedTab()
            .openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.getAvailableRoutings()).contains("3 Axis Lathe Routing", "2AL+3AM Routing", "3 Axis Mill Routing", "4 Axis Mill Routing",
            "5 Axis Mill Routing", "3AM+Drill Press Routing", "3AM+4AM Routing", "3AM+5AM Routing", "MillTurn Routing", "2AL+4AM Routing", "2AL+5AM Routing",
            "2ABFL and 3AM routing", "3ABFL routing");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14988", "15802"})
    @Description("Validate routings Die Cast")
    public void routingsDieCasting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "SandCast";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".x_t");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        routingSelectionPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .goToAdvancedTab()
            .openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.getAvailableRoutings()).contains("High Pressure Die Cast", "Gravity Die Cast");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14990", "15804"})
    @Description("Validate routings Sand Cast")
    public void routingsSandCasting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_SAND;

        String componentName = "SandCast";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".x_t");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        routingSelectionPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .goToAdvancedTab()
            .openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.getAvailableRoutings()).contains("VerticalAutomatic", "HorizontalAutomatic", "ManualStd", "ManualFloor", "ManualPit");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14991", "15805"})
    @Description("Validate routings Forging")
    public void routingsForging() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;

        String componentName = "ap_blow_molding_excerise_EL0000";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".STEP");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        routingSelectionPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .goToAdvancedTab()
            .openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.getAvailableRoutings()).contains("Ring Rolled Forging", "Closed Die Forging");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14994", "15808"})
    @Description("Validate routings Rapid Prototyping")
    public void routingsRapidPrototyping() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.RAPID_PROTOTYPING;

        String componentName = "Rapid Prototyping";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        routingSelectionPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .goToAdvancedTab()
            .openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.getAvailableRoutings()).contains("3D Printing", "Selective Laser Sintering", "Stereolithography");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14995", "15809"})
    @Description("Validate routings Roto & Blow Moulding")
    public void routingsRotoBlowMould() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ROTO_BLOW_MOLDING;

        String componentName = "Rapid Prototyping";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        routingSelectionPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .goToAdvancedTab()
            .openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.getAvailableRoutings()).contains("Blow Molding", "Rotational Molding");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14996", "15810"})
    @Description("Validate routings Sheet Metal")
    public void routingsSheetMetal() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "700-33770-01_A0";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        routingSelectionPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .goToAdvancedTab()
            .openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.getAvailableRoutings()).contains("Stage Tooling", "Prog Die", "[CTL]/Shear/Press", "[CTL]/Shear/Chemical Mill", "Tandem Die",
            "[CTL]/Laser/[Bend]", "[CTL]/Laser Punch/[Bend]", "[CTL]/Plasma/[Deslag]/[Bend]", "[CTL]/Plasma Punch/[Deslag]/[Bend]", "[CTL]/Oxyfuel/[Deslag]/[Bend]",
            "[CTL]/Waterjet/[Bend]", "[CTL]/Turret/[Bend]", "[CTL]/2 Axis Router/[Bend]", "[CTL]/[Bend]", "[CTL]/Fiber Laser/[Bend]");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14997", "15811"})
    @Description("Validate routings Sheet Metal - Hydroforming")
    public void routingsHydroforming() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_HYDROFORMING;

        String componentName = "Hydroforming";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        routingSelectionPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .goToAdvancedTab()
            .openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.getAvailableRoutings()).contains("Laser Cut - Fluid Cell Routing", "Router Cut - Fluid Cell Routing",
            "Offline Blank - Fluid Cell Routing", "Laser Cut - Deep Draw Routing", "Router Cut - Deep Draw Routing", "Offline Blank - Deep Draw Routing");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14999", "15813"})
    @Description("Validate routings Sheet Metal - Stretchforming")
    public void routingsStretchforming() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING;

        String componentName = "Hydroforming";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        routingSelectionPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .goToAdvancedTab()
            .openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.getAvailableRoutings()).contains("Stretch Form Transverse", "Stretch Form Longitudinal");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"15001", "15815"})
    @Description("Validate routings Sheet Plastic")
    public void routingsSheetPlastic() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_PLASTIC;

        String componentName = "sheet_plastic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".STEP");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        routingSelectionPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .goToAdvancedTab()
            .openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.getAvailableRoutings()).contains("Single Cavity Mold", "2x1-Cavity Mold", "2x2-Cavity Mold");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"7857"})
    @Description("Validate behaviour when Adding/Editing tolerances that may require additional machining.")
    public void routingTolerances() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "DTCCastingIssues";
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".catpart");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .openSettings()
            .goToToleranceTab()
            .selectCad()
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Melting / High Pressure Die Casting / Trim / 5 Axis Mill / Cylindrical Grinder / Reciprocating Surface Grinder");

        evaluatePage.goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("Gravity Die Cast")
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Melting", "Coremaking", "Gravity Die Casting", "Core Refractory Coat", "Refractory Coat Oven Dry",
            "Trim", "Cleaning", "Finishing", "Visual Inspection", "3 Axis Mill", "Drill Press", "Cylindrical Grinder", "Reciprocating Surface Grinder");
        softAssertions.assertAll();
    }
}