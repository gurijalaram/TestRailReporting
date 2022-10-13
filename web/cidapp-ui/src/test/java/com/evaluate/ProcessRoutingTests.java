package com.evaluate;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.UserPreferencesUtil;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.MaterialSelectorPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.GuidanceIssuesPage;
import com.apriori.pageobjects.pages.evaluate.inputs.AdvancedPage;
import com.apriori.pageobjects.pages.evaluate.inputs.RoutingSelectionPage;
import com.apriori.pageobjects.pages.evaluate.inputs.SecondaryProcessesPage;
import com.apriori.pageobjects.pages.evaluate.materialprocess.MaterialProcessPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Offset;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class ProcessRoutingTests extends TestBase {
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private AdvancedPage advancedPage;
    private MaterialProcessPage materialProcessPage;
    private RoutingSelectionPage routingSelectionPage;
    private GuidanceIssuesPage guidanceIssuesPage;
    private MaterialSelectorPage materialSelectorPage;

    private File resourceFile;
    private UserCredentials currentUser;
    private SecondaryProcessesPage secondaryProcessPage;
    private ComponentInfoBuilder cidComponentItem;
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
    //@TestRail(testCaseId = {"}) TODO add testrail ID
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
            .selectRoutingPreferenceByName("3 Axis Mill")
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getProcessRoutingDetails()).contains("3 Axis Mill");
        softAssertions.assertAll();
    }

    @Test
    //@TestRail(testCaseId = {"}) TODO add testrail ID
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
        softAssertions.assertAll();
    }

    @Test
    //@TestRail(testCaseId = {"}) TODO add testrail ID
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
    //@TestRail(testCaseId = {"}) TODO add testrail ID
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

        softAssertions.assertThat(routingSelectionPage.getCostStatusValue("Gravity Die Cast")).isEqualTo(NewCostingLabelEnum.COST_COMPLETE);
        softAssertions.assertAll();
    }

    @Test
    //@TestRail(testCaseId = {"}) TODO add testrail ID
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

        /*TODO uncomment this section when dfm messaging appears for this failure
        evaluatePage.openDesignGuidance()
                .selectIssueTypeGcd("Costing Failed", "Additive Manufacturing/Surface Treatment is infeasible", "Component:1");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("This DMLS material is not compatible with Stereolithography.");*/
        softAssertions.assertAll();
    }

    @Test
    //@TestRail(testCaseId = {"}) TODO add testrail ID
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

        softAssertions.assertThat(materialProcessPage.getPartOrientation().contains("Position Bend with Smallest Radius Parallel to Grain"));
        softAssertions.assertThat(materialProcessPage.getGrainDirection().contains("Parallel to Sheet Length"));
        // TODO CN to fix this assertion softAssertions.assertThat(materialProcessPage.getOverriddenPso("Nominal Wall Thickness (Piece Part Cost Driver)")).isEqualTo(0.5);
        softAssertions.assertAll();
    }

    @Test
    //@TestRail(testCaseId = {"}) TODO add testrail ID
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
            .openMaterialSelectorTable();

        //TODO cn fix this softAssertions.assertThat(materialSelectorPage.getListOfMaterialTypes()).containsExactlyInAnyOrder("test", "All", "ABS", "Acetal", "Acrylic", "Nylon", "PBT", "PET", "PPS", "Polycarbonate", "Polypropylene", "Polystyrene", "Polyurethane", "TPA", "TPE", "TPO", "TPS", "TPU", "TPV");
        softAssertions.assertAll();
    }

    @Test
    //@TestRail(testCaseId = {"}) TODO add testrail ID
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
    //@TestRail(testCaseId = {"}) TODO add testrail ID
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

/*    @Test
    //@TestRail(testCaseId = {"}) TODO add testrail ID and un comment when BA-2646 is complete
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

        softAssertions.assertThat(routingSelectionPage.getAvailableRoutings()).doesNotContain("MillTurn Routing", "Die Casting");
        softAssertions.assertAll();
    }*/
}