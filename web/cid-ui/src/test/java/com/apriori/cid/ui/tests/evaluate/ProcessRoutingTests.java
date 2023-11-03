package com.apriori.cid.ui.tests.evaluate;

import static com.apriori.shared.util.enums.ProcessGroupEnum.ASSEMBLY;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.EXTENDED_REGRESSION;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cid.api.utils.UserPreferencesUtil;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.MaterialSelectorPage;
import com.apriori.cid.ui.pageobjects.evaluate.components.ComponentsTablePage;
import com.apriori.cid.ui.pageobjects.evaluate.components.inputs.ComponentBasicPage;
import com.apriori.cid.ui.pageobjects.evaluate.designguidance.GuidanceIssuesPage;
import com.apriori.cid.ui.pageobjects.evaluate.inputs.AdvancedPage;
import com.apriori.cid.ui.pageobjects.evaluate.inputs.RoutingSelectionPage;
import com.apriori.cid.ui.pageobjects.evaluate.materialprocess.MaterialProcessPage;
import com.apriori.cid.ui.pageobjects.explore.EditScenarioStatusPage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.utils.ColumnsEnum;
import com.apriori.cid.ui.utils.DecimalPlaceEnum;
import com.apriori.cid.ui.utils.SortOrderEnum;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.enums.NewCostingLabelEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.enums.ScenarioStateEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ProcessRoutingTests extends TestBaseUI {
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private ExplorePage explorePage;
    private ComponentsTablePage componentsTablePage;
    private ComponentInfoBuilder cidComponentItem;
    private ComponentInfoBuilder cidComponentItemA;
    private MaterialProcessPage materialProcessPage;
    private RoutingSelectionPage routingSelectionPage;
    private MaterialSelectorPage materialSelectorPage;
    private GuidanceIssuesPage guidanceIssuesPage;
    private AdvancedPage advancedPage;
    private CssComponent cssComponent = new CssComponent();

    private File resourceFile;
    private File twoModelFile;
    private File subComponentA;
    private File subComponentB;
    private File assembly;
    private UserCredentials currentUser;
    private SoftAssertions softAssertions = new SoftAssertions();

    public ProcessRoutingTests() {
        super();
    }

    @AfterEach
    public void resetAllSettings() {
        if (currentUser != null) {
            new UserPreferencesUtil().resetSettings(currentUser);
        }
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {14404, 15002, 15816, 14408})
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

        evaluatePage.enterAnnualVolume("9999")
            .enterAnnualYears("9")
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("3 Axis Mill");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14392, 14395, 14397, 14398, 14400, 14406, 7845, 16093})
    @Description("Validate routings UI features")
    public void testRoutingSelectionUI() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_form";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        loginPage = new CidAppLoginPage(driver);
        advancedPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .goToAdvancedTab();

        softAssertions.assertThat(advancedPage.isRoutingSelectionButtonEnabled()).isEqualTo(false);

        advancedPage.goToBasicTab()
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL)
            .costScenario()
            .goToAdvancedTab();

        softAssertions.assertThat(advancedPage.isRoutingSelectionButtonEnabled()).isEqualTo(true);
        softAssertions.assertThat(advancedPage.getRoutingSelectionSelected()).contains("Let aPriori Decide");

        advancedPage.openRoutingSelection()
            .selectRoutingPreferenceByName("[CTL]/Waterjet/[Bend]")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab();

        softAssertions.assertThat(advancedPage.getRoutingSelectionSelected()).contains("Waterjet");

        advancedPage.goToBasicTab()
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
            .goToAdvancedTab();

        softAssertions.assertThat(advancedPage.getRoutingSelectionSelected()).contains("Let aPriori Decide");

        advancedPage.goToBasicTab()
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
            .costScenario()
            .goToAdvancedTab();

        softAssertions.assertThat(advancedPage.getRoutingSelectionSelected()).contains("Let aPriori Decide");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {15817, 15817, 15820})
    @Description("Validate routings availability in regards to scenario cost status")
    public void costStatusAndRouting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isTrue();

        advancedPage = evaluatePage.goToAdvancedTab();

        softAssertions.assertThat(advancedPage.isRoutingSelectionButtonEnabled()).isEqualTo(true);

        advancedPage.openRoutingSelection()
            .selectRoutingPreferenceByName("[CTL]/[Bend]")
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_INCOMPLETE)).isTrue();

        evaluatePage.goToAdvancedTab();

        softAssertions.assertThat(advancedPage.isRoutingSelectionButtonEnabled()).isEqualTo(true);

        advancedPage.openRoutingSelection()
            .selectRoutingPreferenceByName("Stage Tooling")
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COSTING_FAILED)).isTrue();

        evaluatePage.goToAdvancedTab();

        softAssertions.assertThat(advancedPage.isRoutingSelectionButtonEnabled()).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14992, 15806, 7835})
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
            .selectMaterial(MaterialNameEnum.POLYURETHANE_POLYMERIC_MDI.getMaterialName())
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
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {7854, 12379, 12381, 12382})
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

        evaluatePage.openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.UNSATURATED_POLYESTER_CF50.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .openMaterialProcess()
            .selectBarChart("Compression Molding")
            .selectOptionsTab()
            .overrideWallThickness("0.4")
            .overrideInsertedComponents("5")
            .selectColorant("Carbon Black Pigment")
            .closePanel()
            .costScenario();

        materialProcessPage = evaluatePage.openMaterialProcess()
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Nominal Wall Thickness")).isEqualTo(0.40);
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Number of Inserted Components")).isEqualTo(5);
        softAssertions.assertThat(materialProcessPage.getColorant()).isEqualTo("Carbon Black Pigment");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {15012, 14401, 15050, 15988, 7851, 7852})
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

        softAssertions.assertThat(routingSelectionPage.getCostStatusValue("High Pressure Die Cast")).isEqualTo("Cost Complete");
        softAssertions.assertThat(routingSelectionPage.isCostDifference("High Pressure Die Cast", "$3.52")).isTrue();
        softAssertions.assertThat(routingSelectionPage.isAprioriLogoDisplayed("High Pressure Die Cast")).isEqualTo(true);

        routingSelectionPage.selectRoutingPreferenceByName("Gravity Die Cast");

        softAssertions.assertThat(routingSelectionPage.getSelectionStatus("Gravity Die Cast")).isEqualTo("Selected");

        routingSelectionPage.submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab()
            .openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.getCostStatusValue("Gravity Die Cast")).isEqualTo("Cost Complete");
        softAssertions.assertThat(routingSelectionPage.isCostDifference("Gravity Die Cast", "$4.75")).isTrue();
        softAssertions.assertThat(routingSelectionPage.isUserTileDisplayed("Gravity Die Cast")).isTrue();
        softAssertions.assertThat(routingSelectionPage.getSelectionStatus("Gravity Die Cast")).isEqualTo("Selected");
        softAssertions.assertAll();
    }

    @Test
    @Disabled("Due to update 14/10/22 routings can't be change for additive manufacturing")
    @TestRail(id = {7855, 14985, 15799})
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
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {7844, 7290, 7291, 7292})
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
            .selectOptionsTab();

        softAssertions.assertThat(materialProcessPage.getPartOrientation()).contains("Position Bend with Smallest Radius Parallel to Grain");
        softAssertions.assertThat(materialProcessPage.getGrainDirection()).contains("Parallel to Sheet Length");
        softAssertions.assertThat(materialProcessPage.getOverriddenPso("Minimum Recommended Hole Diameter")).isEqualTo(0.5);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {7846})
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
            .selectMaterial(MaterialNameEnum.ABS.getMaterialName());

        softAssertions.assertThat(materialSelectorPage.getListOfMaterialTypes())
            .containsExactlyInAnyOrder("All", "ABS", "Acetal", "Acrylic", "Thermoset", "Nylon", "PBT", "PET", "PPS", "Polycarbonate", "Polypropylene", "Polystyrene",
                "Polyurethane", "TPA", "TPE", "TPO", "TPS", "TPU", "TPV");
        softAssertions.assertAll();
    }

    @Test
    @Disabled("Due to update 14/10/22 routings can't be change for additive manufacturing")
    @TestRail(id = {7850})
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
            .selectMaterial(MaterialNameEnum.VISIJET_M3_BLACK.getMaterialName())
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("Material Jetting")
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Printing / Breakoff");

        evaluatePage.openMaterialSelectorTable()
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ALSI10MG.getMaterialName())
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
    @TestRail(id = {7848})
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
    @TestRail(id = {7859})
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
    @TestRail(id = {16132, 7841})
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

        softAssertions.assertThat(materialProcessPage.getProcessPercentage("Band Saw")).contains("52.51%");
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
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {14984, 15798})
    @Description("Validate routings 2-Model Machining")
    public void routings2mm() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String sourcePartName = "casting_BEFORE_machining";
        String twoModelPartName = "casting_AFTER_machining";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, sourcePartName + ".stp");
        twoModelFile = FileResourceUtil.getCloudFile(processGroupEnum, twoModelPartName + ".stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String twoModelScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(sourcePartName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .clickExplore()
            .uploadComponentAndOpen(twoModelPartName, twoModelScenarioName, twoModelFile, currentUser)
            .selectProcessGroup(ProcessGroupEnum.TWO_MODEL_MACHINING)
            .selectSourcePart()
            .selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
            .clickSearch(sourcePartName)
            .highlightScenario(sourcePartName, testScenarioName)
            .submit(EvaluatePage.class)
            .costScenario();

        routingSelectionPage = evaluatePage.goToAdvancedTab().openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.getAvailableRoutings()).contains("3 Axis Mill Routing", "4 Axis Mill Routing", "5 Axis Mill Routing", "2AL+3AM Routing",
            "2AL+4AM Routing", "2AL+5AM Routing", "3 Axis Lathe Routing", "MillTurn Routing", "Drill Press Routing");

        routingSelectionPage.selectRoutingPreferenceByName("3 Axis Lathe Routing")
            .submit(EvaluatePage.class)
            .costScenario();

        materialProcessPage = evaluatePage.openMaterialProcess()
            .selectProcessTab()
            .selectBarChart("3 Axis Lathe");

        softAssertions.assertThat(materialProcessPage.getProcessResult("Process Group Name")).contains("Machining");
        softAssertions.assertThat(materialProcessPage.getProcessResult("Process Name")).contains("3 Axis Lathe");
        softAssertions.assertAll();
    }

    @Test
    @Issue("BA-2757")
    @TestRail(id = {14987, 15801})
    @Description("Validate routings Casting")
    public void routingsCasting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        routingSelectionPage = evaluatePage.goToAdvancedTab().openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.getAvailableRoutings()).contains("SandCasting", "DieCasting", "Permanent Mold");

        routingSelectionPage.selectRoutingPreferenceByName("Permanent Mold")
            .submit(EvaluatePage.class)
            .costScenario();

        materialProcessPage = evaluatePage.openMaterialProcess()
            .selectProcessTab()
            .selectBarChart("PM Molding");

        softAssertions.assertThat(materialProcessPage.getProcessResult("Process Group Name")).contains("Casting");
        softAssertions.assertThat(materialProcessPage.getProcessResult("Process Name")).contains("PM Molding");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14988, 15802})
    @Description("Validate routings Die Cast")
    public void routingsDieCasting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "SandCast";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".x_t");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        routingSelectionPage = evaluatePage.goToAdvancedTab().openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.getAvailableRoutings()).contains("High Pressure Die Cast", "Gravity Die Cast");

        routingSelectionPage.selectRoutingPreferenceByName("Gravity Die Cast")
            .submit(EvaluatePage.class)
            .costScenario();

        materialProcessPage = evaluatePage.openMaterialProcess()
            .selectProcessTab()
            .selectBarChart("Gravity Die Casting");

        softAssertions.assertThat(materialProcessPage.getProcessResult("Process Group Name")).contains("Casting - Die");
        softAssertions.assertThat(materialProcessPage.getProcessResult("Process Name")).contains("Gravity Die Casting");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14989, 15803})
    @Description("Validate routings Investment Cast")
    public void routingsInvestmentCasting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_INVESTMENT;

        String componentName = "case_012_009-0020647_hinge_2";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        routingSelectionPage = evaluatePage.goToAdvancedTab().openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.getAvailableRoutings()).contains("Band Saw", "Abrasive Wheel Cut");

        routingSelectionPage.selectRoutingPreferenceByName("Abrasive Wheel Cut")
            .submit(EvaluatePage.class)
            .costScenario();

        guidanceIssuesPage = evaluatePage.openDesignGuidance()
            .selectIssueTypeGcd("Costing Failed", "Casting - Investment/Machining is infeasible", "Component:1");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Abrasive Wheel Cutting is not feasible because part mass (0.02 kg) is smaller then the acceptable value (4.0 kg).");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14990, 15804, 7843})
    @Description("Validate routings Sand Cast")
    public void routingsSandCasting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_SAND;

        String componentName = "SandCast";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".x_t");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Vertical Automatic");

        routingSelectionPage = evaluatePage.goToAdvancedTab().openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.getAvailableRoutings()).contains("VerticalAutomatic", "HorizontalAutomatic", "ManualStd", "ManualFloor", "ManualPit");

        routingSelectionPage.selectRoutingPreferenceByName("ManualStd")
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Manual Std Moldmaking");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14991, 15805})
    @Description("Validate routings Forging")
    public void routingsForging() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;

        String componentName = "ap_blow_molding_excerise_EL0000";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".STEP");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        routingSelectionPage = evaluatePage.goToAdvancedTab().openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.getAvailableRoutings()).contains("Ring Rolled Forging", "Closed Die Forging");

        routingSelectionPage.selectRoutingPreferenceByName("Ring Rolled Forging")
            .submit(EvaluatePage.class)
            .costScenario();

        guidanceIssuesPage = evaluatePage.openDesignGuidance()
            .selectIssueTypeGcd("Costing Failed", "Forging/Machining is infeasible", "Component:1");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("AP_BLOW_MOLDING_EXCERISE_EL0000 cannot be manufactured by the Ring Rolled Forging routing" +
            " because it has no central THROUGH hole");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14994, 15808})
    @Description("Validate routings Rapid Prototyping")
    public void routingsRapidPrototyping() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.RAPID_PROTOTYPING;

        String componentName = "Rapid Prototyping";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        routingSelectionPage = evaluatePage.goToAdvancedTab().openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.getAvailableRoutings()).contains("3D Printing", "Selective Laser Sintering", "Stereolithography");

        routingSelectionPage.selectRoutingPreferenceByName("3D Printing")
            .submit(EvaluatePage.class)
            .costScenario();

        materialProcessPage = evaluatePage.openMaterialProcess()
            .selectProcessTab()
            .selectBarChart("Printing");

        softAssertions.assertThat(materialProcessPage.getProcessResult("Process Group Name")).contains("Rapid Prototyping");
        softAssertions.assertThat(materialProcessPage.getProcessResult("Process Name")).contains("Printing");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14995, 15809})
    @Description("Validate routings Roto & Blow Molding")
    public void routingsRotoBlowMold() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ROTO_BLOW_MOLDING;

        String componentName = "Rapid Prototyping";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        routingSelectionPage = evaluatePage.goToAdvancedTab().openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.getAvailableRoutings()).contains("Blow Molding", "Rotational Molding");

        routingSelectionPage.selectRoutingPreferenceByName("Rotational Molding")
            .submit(EvaluatePage.class)
            .costScenario();

        materialProcessPage = evaluatePage.openMaterialProcess()
            .selectProcessTab()
            .selectBarChart("Rotational Mold");

        softAssertions.assertThat(materialProcessPage.getProcessResult("Process Group Name")).contains("Roto & Blow Molding");
        softAssertions.assertThat(materialProcessPage.getProcessResult("Process Name")).contains("Rotational Mold");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14996, 15810})
    @Description("Validate routings Sheet Metal")
    public void routingsSheetMetal() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "700-33770-01_A0";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        routingSelectionPage = evaluatePage.goToAdvancedTab().openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.getAvailableRoutings()).contains("Stage Tooling", "Prog Die", "[CTL]/Shear/Press", "[CTL]/Shear/Chemical Mill", "Tandem Die",
            "[CTL]/Laser/[Bend]", "[CTL]/Laser Punch/[Bend]", "[CTL]/Plasma/[Deslag]/[Bend]", "[CTL]/Plasma Punch/[Deslag]/[Bend]", "[CTL]/Oxyfuel/[Deslag]/[Bend]",
            "[CTL]/Waterjet/[Bend]", "[CTL]/Turret/[Bend]", "[CTL]/2 Axis Router/[Bend]", "[CTL]/[Bend]", "[CTL]/Fiber Laser/[Bend]");

        routingSelectionPage.selectRoutingPreferenceByName("[CTL]/Laser Punch/[Bend]")
            .submit(EvaluatePage.class)
            .costScenario();

        materialProcessPage = evaluatePage.openMaterialProcess()
            .selectProcessTab()
            .selectBarChart("Laser Punch");

        softAssertions.assertThat(materialProcessPage.getProcessResult("Process Group Name")).contains("Sheet Metal");
        softAssertions.assertThat(materialProcessPage.getProcessResult("Process Name")).contains("Laser Punch");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14997, 15811})
    @Description("Validate routings Sheet Metal - Hydroforming")
    public void routingsHydroforming() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_HYDROFORMING;

        String componentName = "Hydroforming";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        routingSelectionPage = evaluatePage.goToAdvancedTab().openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.getAvailableRoutings()).contains("Laser Cut - Fluid Cell Routing", "Router Cut - Fluid Cell Routing",
            "Offline Blank - Fluid Cell Routing", "Laser Cut - Deep Draw Routing", "Router Cut - Deep Draw Routing", "Offline Blank - Deep Draw Routing");

        routingSelectionPage.selectRoutingPreferenceByName("Laser Cut - Fluid Cell Routing")
            .submit(EvaluatePage.class)
            .costScenario();

        materialProcessPage = evaluatePage.openMaterialProcess()
            .selectProcessTab()
            .selectBarChart("Laser Cut");

        softAssertions.assertThat(materialProcessPage.getProcessResult("Process Group Name")).contains("Sheet Metal");
        softAssertions.assertThat(materialProcessPage.getProcessResult("Process Name")).contains("Laser Cut");

        materialProcessPage.selectBarChart("Hydroform");

        softAssertions.assertThat(materialProcessPage.getProcessResult("Process Group Name")).contains("Sheet Metal - Hydroforming");
        softAssertions.assertThat(materialProcessPage.getProcessResult("Process Name")).contains("Hydroform");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14999, 15813})
    @Description("Validate routings Sheet Metal - Stretchforming")
    public void routingsStretchforming() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING;

        String componentName = "Hydroforming";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(22.73), Offset.offset(3.0));

        routingSelectionPage = evaluatePage.goToAdvancedTab().openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.getAvailableRoutings()).contains("Stretch Form Transverse", "Stretch Form Longitudinal");

        routingSelectionPage.selectRoutingPreferenceByName("Stretch Form Longitudinal")
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(44.41), Offset.offset(3.0));
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {15001, 15815})
    @Description("Validate routings Sheet Plastic")
    public void routingsSheetPlastic() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_PLASTIC;

        String componentName = "5d51749fig01";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        routingSelectionPage = evaluatePage.goToAdvancedTab().openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.getAvailableRoutings()).contains("Single Station Thermoforming", "Shuttle Station Thermoforming", "3 Station Rotary Thermoforming", "4 Station Rotary Thermoforming");

        routingSelectionPage.selectRoutingPreferenceByName("Single Station Thermoforming")
            .submit(EvaluatePage.class)
            .costScenario();

        materialProcessPage = evaluatePage.openMaterialProcess()
            .selectProcessTab()
            .selectBarChart("Single Station Thermoforming");

        softAssertions.assertThat(materialProcessPage.getProcessResult("Process Group Name")).contains("Sheet Plastic");
        softAssertions.assertThat(materialProcessPage.getProcessResult("Process Name")).contains("Single Station Thermoforming");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {16383})
    @Description("Validate routings are disabled")
    public void routingsDisabled() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ADDITIVE_MANUFACTURING;

        String componentName = "116-5809";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        advancedPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .goToAdvancedTab();

        softAssertions.assertThat(advancedPage.isRoutingSelectionButtonEnabled()).isEqualTo(false);

        advancedPage.goToBasicTab()
            .selectProcessGroup(ProcessGroupEnum.BAR_TUBE_FAB)
            .costScenario()
            .goToAdvancedTab();

        softAssertions.assertThat(advancedPage.isRoutingSelectionButtonEnabled()).isEqualTo(false);

        advancedPage.goToBasicTab()
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL)
            .costScenario()
            .goToAdvancedTab();

        softAssertions.assertThat(advancedPage.isRoutingSelectionButtonEnabled()).isEqualTo(false);

        advancedPage.goToBasicTab()
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_ROLLFORMING)
            .costScenario()
            .goToAdvancedTab();

        softAssertions.assertThat(advancedPage.isRoutingSelectionButtonEnabled()).isEqualTo(false);

        advancedPage.goToBasicTab()
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE)
            .costScenario()
            .goToAdvancedTab();

        softAssertions.assertThat(advancedPage.isRoutingSelectionButtonEnabled()).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {7857})
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

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Melting / High Pressure Die Casting / Trim / 5 Axis Mill");

        evaluatePage.goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("Gravity Die Cast")
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("Melting", "Coremaking", "Gravity Die Casting", "Core Refractory Coat", "Refractory Coat Oven Dry",
            "Trim", "Cleaning", "Finishing", "Visual Inspection", "3 Axis Mill", "Drill Press");
        softAssertions.assertAll();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {16095, 16099})
    @Description("Validate group cost behaviour against routings")
    public void routingsAndGroupCost() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;
        final String componentName = "sheet metal custom 2";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        final ProcessGroupEnum processGroupEnum2 = ProcessGroupEnum.SHEET_METAL;
        final String componentName2 = "sheet metal custom 3";
        final File resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum2, componentName2 + ".SLDPRT");
        final String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);

        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        cidComponentItemA = new ExplorePage(driver).uploadComponent(componentName2, scenarioName2, resourceFile2, currentUser);

        explorePage = new ExplorePage(driver).refresh()
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .clickCostButton(ComponentBasicPage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_USA)
            .applyAndCost(EditScenarioStatusPage.class)
            .close(ExplorePage.class)
            .checkComponentStateRefresh(cidComponentItem, ScenarioStateEnum.COST_COMPLETE)
            .checkComponentStateRefresh(cidComponentItemA, ScenarioStateEnum.COST_COMPLETE);

        evaluatePage = explorePage.openScenario(componentName, scenarioName);

        routingSelectionPage = evaluatePage.goToAdvancedTab().openRoutingSelection();

        routingSelectionPage.selectRoutingPreferenceByName("[CTL]/Waterjet/[Bend]")
            .submit(EvaluatePage.class)
            .costScenario();

        evaluatePage.clickExplore()
            .selectFilter("Private")
            .addColumn(ColumnsEnum.PROCESS_ROUTING);

        softAssertions.assertThat(explorePage.getRowDetails(componentName, scenarioName)).contains("Material Stock / Waterjet Cut / Bend Brake");

        explorePage.multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .clickCostButton(ComponentBasicPage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_USA)
            .enterAnnualYears("9")
            .enterAnnualVolume("9999")
            .openMaterialSelectorTable()
            .search("1050A")
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_1050A.getMaterialName())
            .submit(ComponentBasicPage.class)
            .applyAndCost(EditScenarioStatusPage.class)
            .close(ExplorePage.class)
            .checkComponentStateRefresh(cidComponentItem, ScenarioStateEnum.COST_COMPLETE)
            .checkComponentStateRefresh(cidComponentItemA, ScenarioStateEnum.COST_COMPLETE);

        softAssertions.assertThat(explorePage.getRowDetails(componentName, scenarioName)).contains("Material Stock / Waterjet Cut / Bend Brake");

        explorePage.multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .clickCostButton(ComponentBasicPage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_PLASTIC)
            .applyAndCost(EditScenarioStatusPage.class)
            .close(ExplorePage.class)
            .checkComponentStateRefresh(cidComponentItem, ScenarioStateEnum.COST_COMPLETE)
            .checkComponentStateRefresh(cidComponentItemA, ScenarioStateEnum.COST_COMPLETE);

        softAssertions.assertThat(explorePage.getRowDetails(componentName, scenarioName)).contains("3 Station Rotary Thermoforming / Router");

        explorePage.openScenario(componentName, scenarioName)
            .goToAdvancedTab()
            .openRoutingSelection();

        routingSelectionPage.selectRoutingPreferenceByName("Shuttle Station Thermoforming")
            .submit(EvaluatePage.class)
            .costScenario();

        evaluatePage.clickExplore()
            .selectFilter("Private");

        softAssertions.assertThat(explorePage.getRowDetails(componentName, scenarioName)).contains("Shuttle Station Thermoforming / Router");

        explorePage.multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .clickCostButton(ComponentBasicPage.class)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_BRAZIL)
            .applyAndCost(EditScenarioStatusPage.class)
            .close(ExplorePage.class)
            .checkComponentStateRefresh(cidComponentItem, ScenarioStateEnum.COST_COMPLETE)
            .checkComponentStateRefresh(cidComponentItemA, ScenarioStateEnum.COST_COMPLETE);

        softAssertions.assertThat(explorePage.getRowDetails(componentName, scenarioName)).contains("3 Station Rotary Thermoforming / Router");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {16098})
    @Description("Validate sub-component can be costed with an alternate routing in an assembly")
    public void testRoutingsInAssembly() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        String subComponentAName = "piston";
        String subComponentBName = "piston_pin";
        String assemblyName = "piston_assembly";

        List<String> componentNames = Arrays.asList("piston", "piston_pin");

        subComponentA = FileResourceUtil.getCloudFile(processGroupEnum, subComponentAName + ".prt.5");
        subComponentB = FileResourceUtil.getCloudFile(processGroupEnum, subComponentBName + ".prt.1");
        assembly = FileResourceUtil.getCloudFile(ASSEMBLY, assemblyName + ".asm.1");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(subComponentAName, scenarioName, subComponentA, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        advancedPage = evaluatePage.goToAdvancedTab();

        advancedPage.openRoutingSelection()
            .selectRoutingPreferenceByName("Structural Foam Mold")
            .submit(EvaluatePage.class)
            .costScenario()
            .goToAdvancedTab();

        softAssertions.assertThat(advancedPage.getRoutingSelectionSelected()).contains("Structural Foam Mold");

        evaluatePage.uploadComponentAndOpen(subComponentBName, scenarioName, subComponentB, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario();

        evaluatePage.uploadComponentAndOpen(assemblyName, scenarioName, assembly, currentUser)
            .selectProcessGroup(ASSEMBLY)
            .costScenario();

        softAssertions.assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE)).isEqualTo(true);

        componentsTablePage = evaluatePage.openComponents().selectTableView();

        componentNames.forEach(component ->
            assertThat(componentsTablePage.isComponentNameDisplayedInTreeView(component.toUpperCase()), is(true)));
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14409})
    @Description("Validate routings and user preferences")
    public void routingsAndUserPreferences() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_PLASTIC;

        String componentName = "5d51749fig01";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        routingSelectionPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .goToAdvancedTab()
            .openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.isCostDifference("Single Station Thermoforming", "$9.84")).isTrue();

        routingSelectionPage.cancel(EvaluatePage.class)
            .openSettings()
            .selectDecimalPlaces(DecimalPlaceEnum.FIVE)
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openRoutingSelection();

        softAssertions.assertThat(routingSelectionPage.isCostDifference("Single Station Thermoforming", "$9.83841")).isTrue();
        softAssertions.assertAll();
    }
}