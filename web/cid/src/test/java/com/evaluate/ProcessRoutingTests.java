package com.evaluate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;

import com.apriori.apibase.utils.AfterTestUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CostingLabelEnum;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.pages.evaluate.MaterialCompositionPage;
import com.pageobjects.pages.evaluate.analysis.PropertiesDialogPage;
import com.pageobjects.pages.evaluate.designguidance.GeometryPage;
import com.pageobjects.pages.evaluate.designguidance.GuidancePage;
import com.pageobjects.pages.evaluate.designguidance.investigation.InvestigationPage;
import com.pageobjects.pages.evaluate.process.ProcessRoutingPage;
import com.pageobjects.pages.evaluate.process.ProcessSetupOptionsPage;
import com.pageobjects.pages.evaluate.process.RoutingsPage;
import com.pageobjects.pages.explore.ExplorePage;
import com.pageobjects.pages.login.CidLoginPage;
import com.pageobjects.pages.settings.SettingsPage;
import com.pageobjects.pages.settings.ToleranceSettingsPage;
import io.qameta.allure.Description;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class ProcessRoutingTests extends TestBase {

    private CidLoginPage loginPage;
    private ProcessRoutingPage processRoutingPage;
    private EvaluatePage evaluatePage;
    private RoutingsPage routingsPage;
    private MaterialCompositionPage materialCompositionPage;
    private GeometryPage geometryPage;
    private PropertiesDialogPage propertiesDialogPage;
    private GuidancePage guidancePage;
    private ToleranceSettingsPage toleranceSettingsPage;
    private SettingsPage settingsPage;
    private UserCredentials currentUser;
    private ProcessSetupOptionsPage processSetupOptionsPage;

    private File resourceFile;

    public ProcessRoutingTests() {
        super();
    }

    @After
    public void resetSettings() {
        if (currentUser != null) {
            new AfterTestUtil().resetAllSettings(currentUser.getUsername());
        }
    }

    @Test
    @Description("Validate the user can Change the process routing in CI Design")
    public void testAlternateRoutingSelection() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface.CATPart");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        processRoutingPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("3 Axis Mill")
            .apply();

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("3 Axis Mill"));
    }

    @Test
    @TestRail(testCaseId = {"645", "269", "647", "649"})
    @Description("View detailed information about costed process")
    public void testViewProcessDetails() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "PlasticMoulding.CATPart");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        processRoutingPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails();

        assertThat(processRoutingPage.getSelectionTableDetails(), containsString("Cycle Time (s): 54.14, Piece Part Cost (USD): 0.62, Fully Burdened Cost (USD): 1.06, Total Capital Investments (USD): 11,934.97"));
    }

    @Test
    @TestRail(testCaseId = {"646"})
    @Description("View individual process steps")
    public void testViewProcessSteps() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic.prt");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        processRoutingPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails();

        assertThat(processRoutingPage.getRoutingLabels(), hasItems("Material Stock", "Turret Press", "Bend Brake"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"1649", "1656"})
    @Description("Validate the user can Change the process routing")
    public void changeRouting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Push Pin.stp");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        processRoutingPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openMaterialCompositionTable()
            .selectMaterialComposition("Polyurethane, Polymeric MDI")
            .apply()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Reaction Injection Mold")
            .apply();

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), is("Reaction Injection Molding"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"1667", "1668"})
    @Description("Validate the Use selected for future costing checkbox works correctly")
    public void testRoutingCheckBox() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "plasticLid.SLDPRT");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Structural Foam Mold")
            .apply()
            .closePanel()
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton()
            .checkRoutingBox()
            .apply()
            .closePanel()
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), is("Injection Molding"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"1665", "1666"})
    @Description("Validate the information updates in the routing modal box")
    public void testLastRouting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "CastedPart.CATPart");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        routingsPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getCostedRouting("Die Casting"), is(true));

        routingsPage.selectRouting("Sand Casting")
            .apply()
            .closePanel()
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getCostedRouting("Sand Casting"), is(true));
        assertThat(routingsPage.getSelectedRouting("Sand Casting"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"1670", "568", "570", "571"})
    @Description("Validate behaviour when forcing a material that will fail costing within CID")
    public void failCostingRouting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ADDITIVE_MANUFACTURING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "CastedPart.CATPart");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Vat Photopolymerization")
            .apply()
            .closePanel()
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum AlSi10Mg")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_FAILURE.getCostingText()), is(true));
        assertThat(evaluatePage.isFailedIconPresent(), is(true));

        guidancePage = evaluatePage.openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Costing Failed", "Additive Manufacturing/Surface Treatment is infeasible", "Component:1");

        assertThat(guidancePage.getGuidanceMessage(), containsString("This DMLS material is not compatible with Stereolithography."));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"1659", "765", "766", "767"})
    @Description("Validate costing results update accordingly for a newly selected and costed routing")
    public void costUpdatedRouting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "HoleProximityTest.SLDPRT");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario();
        assertThat(evaluatePage.getBurdenedCost(), is(closeTo(1.56, 1)));

        evaluatePage.openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("[CTL]/Waterjet/[Bend]")
            .apply()
            .closePanel()
            .costScenario();

        assertThat(evaluatePage.getBurdenedCost(), is(closeTo(1.96, 1)));

        processSetupOptionsPage = evaluatePage.openProcessDetails()
            .selectProcessChart("Waterjet Cut")
            .selectOptions()
            .selectPartOrientationDropdown("Position Bend with Smallest Radius Parallel to Grain")
            .selectGrainDirectionDropdown("Parallel to Sheet Length")
            .setMinHoleDiameter("0.5")
            .closePanel()
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Waterjet Cut")
            .selectOptions();

        assertThat(processSetupOptionsPage.getPartOrientation("Position Bend with Smallest Radius Parallel to Grain"), is(true));
        assertThat(processSetupOptionsPage.getGrainDirection("Parallel to Sheet Length"), is(true));
        assertThat(processSetupOptionsPage.getMinHoleDiameter(), is("0.500"));
    }

    @Test
    @TestRail(testCaseId = {"1661"})
    @Description("Validate materials selected are appropriate for selected routing.")
    public void routingMaterials() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "plasticLid.SLDPRT");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        materialCompositionPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Structural Foam Mold")
            .apply()
            .closePanel()
            .costScenario()
            .openMaterialCompositionTable();

        assertThat(materialCompositionPage.getListOfMaterialTypes(), containsInAnyOrder("All", "ABS", "Acetal", "Acrylic", "Nylon", "PBT", "PET", "PPS", "Polycarbonate", "Polypropylene", "Polystyrene", "Polyurethane", "TPA", "TPE", "TPO", "TPS", "TPU", "TPV"));
    }

    @Test
    @TestRail(testCaseId = {"1664"})
    @Description("Validate behaviour when selecting a PG that auto triggers a secondary process")
    public void routingSecondaryPG() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ADDITIVE_MANUFACTURING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "AdditiveManuf.stp");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Material Jetting")
            .apply()
            .closePanel()
            .openMaterialCompositionTable()
            .selectMaterialComposition("Visijet M3 Black")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), is("Printing / Breakoff"));

        evaluatePage.openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Powder Bed Fusion / Direct Metal Laser Sintering")
            .apply()
            .closePanel()
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum AlSi10Mg")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Stress Relief / Ultrasonic Cleaning"));
    }

    @Test
    @TestRail(testCaseId = {"1663"})
    @Description("Validate a variety of secondary processes can be added for newly selected routings")
    public void secondaryProcessesRoutings() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "PMI_AllTolTypesCatia.CATPart");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        processRoutingPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Structural Foam Mold")
            .apply()
            .closePanel()
            .costScenario()
            .openSecondaryProcess()
            .selectSecondaryProcess("Other Secondary Processes", "Packaging")
            .apply()
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Injection Mold")
            .apply()
            .closePanel()
            .costScenario()
            .openProcessDetails();

        assertThat(processRoutingPage.getRoutingLabels(), hasItems("Carton Forming", "Pack & Load", "Carton Sealing"));
    }

    @Test
    @TestRail(testCaseId = {"1674", "1675"})
    @Description("Validate user cannot select a routing that does not belong to a certain Process Group")
    public void routingPGs() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "plasticLid.SLDPRT");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        routingsPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), Matchers.not(hasItem("Die Casting")));
        assertThat(routingsPage.getRoutings(), Matchers.not(hasItem("MillTurn Routing")));
    }

    @Test
    @TestRail(testCaseId = {"1673"})
    @Description("Validate behaviour when Adding/Editing threads that may require additional machining.")
    public void threadsRouting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "plasticLid.SLDPRT");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Threading")
            .editThread("Simple Holes", "SimpleHole:1")
            .selectThreadDropdown("Yes")
            .enterThreadLength("3.00")
            .apply(InvestigationPage.class);

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("High Pressure Die Casting"));

        evaluatePage.openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Gravity Die Cast")
            .apply()
            .closePanel()
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), containsString("Gravity Die Casting"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"1658", "1254"})
    @Description("Validate the properties dialogue box updates with a newly selected and costed routing.")
    public void propertiesRouting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic.prt");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        geometryPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openDesignGuidance()
            .openGeometryTab()
            .selectGCDAndGCDProperty("Holes", "Simple Holes", "SimpleHole:1");

        propertiesDialogPage = new EvaluatePage(driver).selectAnalysis()
            .selectProperties()
            .expandDropdown("Technique");
        assertThat(propertiesDialogPage.getProperties("Selected"), containsString("Punching"));

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.selectAnalysis()
            .closeProperties();

        evaluatePage.openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("[CTL]/Waterjet/[Bend]")
            .apply()
            .closePanel()
            .costScenario()
            .openDesignGuidance()
            .openGeometryTab()
            .selectGCDAndGCDProperty("Holes", "Simple Holes", "SimpleHole:1");

        propertiesDialogPage = evaluatePage.selectAnalysis()
            .selectProperties()
            .expandDropdown("Technique");
        assertThat(propertiesDialogPage.getProperties("Selected"), containsString("Waterjet Cutting"));
    }

    @Test
    @TestRail(testCaseId = {"648"})
    @Description("Be able to see basic breakdown of cycle time by process for problem identification.")
    public void cycleTime() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Push Pin.stp");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        processRoutingPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Band Saw");

        assertThat(processRoutingPage.getProcessPercentage(), CoreMatchers.hasItem("16 (50%)"));
        assertThat(processRoutingPage.getMachineName(), is("DoAll 3613-1 Vert"));

        processRoutingPage.selectProcessChart("2 Axis Lathe");
        assertThat(processRoutingPage.getSelectionTableDetails(), containsString("Cycle Time (s): 15.99"));
        assertThat(processRoutingPage.getMachineName(), is("Virtual 2 Axis Lathe - Small"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"1657"})
    @Description("Validate routing out of date message appears")
    public void routingOutOfDate() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "case_002_006-8611543_prt.stp");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        processRoutingPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Structural Foam Mold")
            .apply();

        assertThat(processRoutingPage.isRoutingOutOfDateDisplayed(), is(true));
    }

    @Test
    @Description("Validate routings Additive")
    public void routingsAdditive() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ADDITIVE_MANUFACTURING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "case_002_006-8611543_prt.stp");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        routingsPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("Material Jetting", "Powder Bed Fusion / Selective Laser Sintering", "Powder Bed Fusion / Direct Metal Laser Sintering", "Vat Photopolymerization"));
    }

    @Test
    @Description("Validate routings Bar and Tube")
    public void routingsBarTube() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.BAR_TUBE_FAB;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "ap_blow_molding_excerise_EL0000.STEP");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        routingsPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("Bent Part / CTL/[Punch]/[Bend]", "Bent Part / Tube Laser/[Punch]/[Bend]", "Unbent Part / CTL/[Punch]", "Unbent Part / Tube Laser/[Punch]", "Unbent Part / Roll Bending"));
    }

    @Test
    @Description("Validate routings Die Cast")
    public void routingsDieCasting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "SandCast.x_t");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        routingsPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("High Pressure Die Cast", "Gravity Die Cast"));
    }

    @Test
    @Description("Validate routings Sand Cast")
    public void routingsSandCasting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_SAND;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "SandCast.x_t");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        routingsPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("Vertical Automatic", "Horizontal Automatic", "Manual Std", "Manual Floor", "Manual Pit"));
    }

    @Test
    @Description("Validate routings Forging")
    public void routingsForging() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "ap_blow_molding_excerise_EL0000.STEP");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        routingsPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("Ring Rolled Forging", "Closed Die Forging"));
    }

    @Test
    @Description("Validate routings Plastic Moulding")
    public void routingsPlasticMoulding() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "ap_blow_molding_excerise_EL0000.STEP");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        routingsPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("Injection Mold", "Reaction Injection Mold", "Structural Foam Mold"));
    }

    @Test
    @Description("Validate routings Powder Metal")
    public void routingsPowderMetal() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "PowderMetalShaft.stp");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        routingsPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("2AL+3AM Routing", "4 Axis Mill Routing", "3 Axis Lathe Routing", "2AL+4AM Routing", "Drill Press Routing", "5 Axis Mill Routing", "3 Axis Mill Routing", "MillTurn Routing", "2AL+5AM Routing"));
    }

    @Test
    @Description("Validate routings Rapid Prototyping")
    public void routingsRapidPrototyping() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.RAPID_PROTOTYPING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Rapid Prototyping.stp");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        routingsPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("3D Printing", "Selective Laser Sintering", "Stereolithography"));
    }

    @Test
    @Description("Validate routings Roto & Blow Moulding")
    public void routingsRotoBlowMould() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ROTO_BLOW_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Rapid Prototyping.stp");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        routingsPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("Blow Molding", "Rotational Molding"));
    }

    @Test
    @Description("Validate routings Sheet Metal")
    public void routingsSheetMetal() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "700-33770-01_A0.stp");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        routingsPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("Stage Tooling", "Prog Die", "[CTL]/Shear/Press", "[CTL]/Shear/Chemical Mill", "Tandem Die",
            "[CTL]/Laser/[Bend]", "[CTL]/Laser Punch/[Bend]", "[CTL]/Plasma/[Deslag]/[Bend]", "[CTL]/Plasma Punch/[Deslag]/[Bend]", "[CTL]/Oxyfuel/[Deslag]/[Bend]",
            "[CTL]/Waterjet/[Bend]", "[CTL]/Turret/[Bend]", "[CTL]/2 Axis Router/[Bend]", "[CTL]/[Bend]", "[CTL]/Fiber Laser/[Bend]"));
    }

    @Test
    @Description("Validate routings Sheet Metal - Hydroforming")
    public void routingsHydroforming() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_HYDROFORMING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Hydroforming.stp");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        routingsPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("Laser Cut - Fluid Cell Routing", "Router Cut - Fluid Cell Routing",
            "Offline Blank - Fluid Cell Routing", "Laser Cut - Deep Draw Routing", "Router Cut - Deep Draw Routing", "Offline Blank - Deep Draw Routing"));
    }

    @Test
    @Description("Validate routings Sheet Metal - Stretchforming")
    public void routingsStretchforming() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Hydroforming.stp");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        routingsPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("Stretch Form Transverse", "Stretch Form Longitudinal"));
    }

    @Test
    @Description("Validate routings Sheet Plastic")
    public void routingsSheetPlastic() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_PLASTIC;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "sheet_plastic.STEP");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        routingsPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("Single Cavity Mold", "2x1-Cavity Mold", "2x2-Cavity Mold"));
    }

    @Test
    @Description("Validate routings Stock Machining")
    public void routingsStockMachining() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "225_gasket-1-solid1.prt.1");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        routingsPage = loginPage.login(currentUser)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("3 Axis Lathe Routing", "2AL+3AM Routing", "3 Axis Mill Routing", "4 Axis Mill Routing",
            "5 Axis Mill Routing", "3AM+Drill Press Routing", "3AM+4AM Routing", "3AM+5AM Routing", "MillTurn Routing", "2AL+4AM Routing", "2AL+5AM Routing",
            "2ABFL and 3AM routing", "3ABFL routing"));
    }

    @Test
    @TestRail(testCaseId = {"1672"})
    @Description("Validate behaviour when Adding/Editing tolerances that may require additional machining.")
    public void routingTolerances() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "DTCCastingIssues.catpart");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .changeCurrency(CurrencyEnum.USD.getCurrency())
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        evaluatePage = settingsPage.save(ExplorePage.class)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails(), is("Melting / High Pressure Die Casting / Trim / 5 Axis Mill / Drill Press / Cylindrical Grinder / " +
            "Reciprocating Surface Grinder"));

        processRoutingPage = evaluatePage.openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Gravity Die Cast")
            .apply()
            .closePanel()
            .costScenario()
            .openProcessDetails();

        assertThat(processRoutingPage.getRoutingLabels(), containsInAnyOrder("Melting", "Coremaking", "Gravity Die Casting", "Core Refractory Coat", "Refractory Coat Oven Dry",
            "Trim", "Cleaning", "Finishing", "Visual Inspection", "3 Axis Mill", "Drill Press", "Cylindrical Grinder", "Reciprocating Surface Grinder"));
    }
}