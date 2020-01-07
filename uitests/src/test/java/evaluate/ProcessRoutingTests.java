package evaluate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.analysis.PropertiesDialogPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.FailuresPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.GeometryPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.investigation.InvestigationPage;
import com.apriori.pageobjects.pages.evaluate.materialutilization.MaterialCompositionPage;
import com.apriori.pageobjects.pages.evaluate.process.ProcessRoutingPage;
import com.apriori.pageobjects.pages.evaluate.process.RoutingsPage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.CostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Test;

public class ProcessRoutingTests extends TestBase {

    private CIDLoginPage loginPage;
    private ProcessRoutingPage processRoutingPage;
    private EvaluatePage evaluatePage;
    private RoutingsPage routingsPage;
    private MaterialCompositionPage materialCompositionPage;
    private GeometryPage geometryPage;
    private PropertiesDialogPage propertiesDialogPage;
    private FailuresPage failuresPage;

    public ProcessRoutingTests() {
        super();
    }

    @Test
    @Description("Validate the user can Change the process routing in CI Design")
    public void testAlternateRoutingSelection() {
        loginPage = new CIDLoginPage(driver);
        processRoutingPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("3 Axis Mill")
            .apply();

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.costScenario();

        assertThat(evaluatePage.isProcessRoutingDetails("3 Axis Mill"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"645", "269", "647", "649"})
    @Description("View detailed information about costed process")
    public void testViewProcessDetails() {
        loginPage = new CIDLoginPage(driver);
        processRoutingPage  = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("PlasticMoulding.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails();

        assertThat(processRoutingPage.getSelectionTableDetails(), containsString("Cycle Time (s): 54.05, Piece Part Cost (USD): 0.63, Fully Burdened Cost (USD): 1.05, Total Capital Investments (USD): 11,741.62"));
    }

    @Test
    @TestRail(testCaseId = {"646"})
    @Description("View individual process steps")
    public void testViewProcessSteps() {
        loginPage = new CIDLoginPage(driver);
        processRoutingPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("bracket_basic.prt"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails();

        assertThat(processRoutingPage.getRoutingLabels(), hasItems("Material Stock", "Turret Press", "Bend Brake"));
    }

    @Test
    @TestRail(testCaseId = {"1649", "1656"})
    @Description("Validate the user can Change the process routing")
    public void changeRouting() {
        loginPage = new CIDLoginPage(driver);
        processRoutingPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Push Pin.stp"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
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

        assertThat(evaluatePage.isProcessRoutingDetails("Reaction Injection Molding"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"1667", "1668"})
    @Description("Validate the Use selected for future costing checkbox works correctly")
    public void testRoutingCheckBox() {
        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("plasticLid.SLDPRT"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Structural Foam Mold")
            .apply()
            .closeProcessPanel()
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton()
            .checkRoutingBox()
            .apply()
            .closeProcessPanel()
            .costScenario();

        assertThat(evaluatePage.isProcessRoutingDetails("Injection Molding"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"1665", "1666"})
    @Description("Validate the information updates in the routing modal box")
    public void testlastRouting() {
        loginPage = new CIDLoginPage(driver);
        routingsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("CastedPart.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getCostedRouting("Die Casting"), is(true));

        routingsPage.selectRouting("Sand Casting")
            .apply()
            .closeProcessPanel();

        evaluatePage = new EvaluatePage(driver);
        routingsPage = evaluatePage.costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getCostedRouting("Sand Casting"), is(true));
        assertThat(routingsPage.getSelectedRouting("Sand Casting"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"1670", "568", "570", "571"})
    @Description("Validate behaviour when forcing a material that will fail costing within CID")
    public void failCostingRouting() {
        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("CastedPart.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Vat Photopolymerization")
            .apply()
            .closeProcessPanel()
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum AlSi10Mg")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_FAILURE.getCostingText()), is(true));
        assertThat(evaluatePage.isFailedIconPresent(), is(true));

        evaluatePage = new EvaluatePage(driver);
        failuresPage = evaluatePage.openDesignGuidance()
            .openFailuresTab()
            .selectIssueTypeAndGCD("Costing Failed", "Component:1");

        assertThat(failuresPage.getUncostedMessage(), containsString("This DMLS material is not compatible with Stereolithography."));
    }

    @Test
    @TestRail(testCaseId = {"1659"})
    @Description("Validate costing results update accordingly for a newly selected and costed routing")
    public void costUpdatedRouting() {
        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("HoleProximityTest.SLDPRT"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario();
        assertThat(evaluatePage.getBurdenedCost("1.56"), is(true));

        new EvaluatePage(driver).openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("[CTL]/Waterjet/[Bend]")
            .apply()
            .closeProcessPanel()
            .costScenario();

        assertThat(evaluatePage.getBurdenedCost("1.95"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"1661"})
    @Description("Validate materials selected are appropriate for selected routing.")
    public void routingMaterials() {
        loginPage = new CIDLoginPage(driver);
        materialCompositionPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("plasticLid.SLDPRT"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Structural Foam Mold")
            .apply()
            .closeProcessPanel()
            .costScenario()
            .openMaterialCompositionTable();

        assertThat(materialCompositionPage.getListOfMaterialTypes(), containsInAnyOrder("All", "ABS", "Acetal", "Acrylic", "Nylon", "PBT", "PET", "PPS", "Polycarbonate", "Polypropylene", "Polystyrene", "Polyurethane"));
    }

    @Test
    @TestRail(testCaseId = {"1664"})
    @Description("Validate behaviour when selecting a PG that auto triggers a secondary process")
    public void routingSecondaryPG() {
        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("AdditiveManuf.stp"))
            .selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Material Jetting")
            .apply()
            .closeProcessPanel()
            .openMaterialCompositionTable()
            .selectMaterialComposition("Visijet M3 Black")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isProcessRoutingDetails("Printing / Breakoff"), is(true));

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Powder Bed Fusion / Direct Metal Laser Sintering")
            .apply()
            .closeProcessPanel()
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum AlSi10Mg")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isProcessRoutingDetails("Stress Relief / Ultrasonic Cleaning"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"1663"})
    @Description("Validate a variety of secondary processes can be added for newly selected routings")
    public void secondaryProcessesRoutings() {
        loginPage = new CIDLoginPage(driver);
        processRoutingPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("PMI_AllTolTypesCatia.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Structural Foam Mold")
            .apply()
            .closeProcessPanel()
            .costScenario()
            .openSecondaryProcess()
            .selectSecondaryProcess("Other Secondary Processes", "Packaging")
            .apply()
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Injection Mold")
            .apply()
            .closeProcessPanel()
            .costScenario()
            .openProcessDetails();

        assertThat(processRoutingPage.getRoutingLabels(), hasItems("Carton Forming", "Pack & Load", "Carton Sealing"));
    }

    @Test
    @TestRail(testCaseId = {"1674", "1675"})
    @Description("Validate user cannot select a routing that does not belong to a certain Process Group")
    public void routingPGs() {
        loginPage = new CIDLoginPage(driver);
        routingsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("plasticLid.SLDPRT"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
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
        new CIDLoginPage(driver).login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("plasticLid.SLDPRT"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
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

        assertThat(evaluatePage.isProcessRoutingDetails("High Pressure Die Casting"), is(true));

        evaluatePage.openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Gravity Die Cast")
            .apply()
            .closeProcessPanel()
            .costScenario();

        assertThat(evaluatePage.isProcessRoutingDetails("Gravity Die Casting"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"1658", "1254"})
    @Description("Validate the properties dialogue box updates with a newly selected and costed routing.")
    public void propertiesRouting() {
        loginPage = new CIDLoginPage(driver);
        geometryPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("bracket_basic.prt"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openDesignGuidance()
            .openGeometryTab()
            .selectGCDAndGCDProperty("Holes", "Simple Holes", "SimpleHole:1");

        evaluatePage = new EvaluatePage(driver);
        propertiesDialogPage = evaluatePage.selectAnalysis()
            .selectProperties()
            .expandDropdown("Technique");
        assertThat(propertiesDialogPage.getProperties("Selected"), containsString("Punching"));
        new EvaluatePage(driver).selectAnalysis()
            .closeProperties();

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("[CTL]/Waterjet/[Bend]")
            .apply()
            .closeProcessPanel()
            .costScenario()
            .openDesignGuidance()
            .openGeometryTab()
            .selectGCDAndGCDProperty("Holes", "Simple Holes", "SimpleHole:1");

        evaluatePage = new EvaluatePage(driver);
        propertiesDialogPage = evaluatePage.selectAnalysis()
            .selectProperties()
            .expandDropdown("Technique");
        assertThat(propertiesDialogPage.getProperties("Selected"), containsString("Waterjet Cutting"));
    }

    @Test
    @TestRail(testCaseId = {"648"})
    @Description("Be able to see basic breakdown of cycle time by process for problem identification.")
    public void cycleTime() {
        loginPage = new CIDLoginPage(driver);
        processRoutingPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Push Pin.stp"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectProcessChart("Band Saw");

        assertThat(processRoutingPage.getProcessPercentage(), CoreMatchers.hasItem("16 (45%)"));
        assertThat(processRoutingPage.isMachineName("DoAll 3613-1 Vert"), is(true));

        processRoutingPage.selectProcessChart("2 Axis Lathe");
        assertThat(processRoutingPage.getSelectionTableDetails(), containsString("Cycle Time (s): 19.47"));
        assertThat(processRoutingPage.isMachineName("Virtual 2 Axis Lathe - Small"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"1657"})
    @Description("Validate routing out of date message appears")
    public void routingOutOfDate() {
        loginPage = new CIDLoginPage(driver);
        processRoutingPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("case_002_006-8611543_prt.stp"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
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
    public void routingsAddivite() {
        loginPage = new CIDLoginPage(driver);
        routingsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("case_002_006-8611543_prt.stp"))
            .selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("Material Jetting", "Powder Bed Fusion / Selective Laser Sintering", "Powder Bed Fusion / Direct Metal Laser Sintering", "Vat Photopolymerization"));
    }

    @Test
    @Description("Validate routings Bar and Tube")
    public void routingsBarTube() {
        loginPage = new CIDLoginPage(driver);
        routingsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("ap_blow_molding_excerise_EL0000.STEP"))
            .selectProcessGroup(ProcessGroupEnum.BAR_TUBE_FAB.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("Bent Part / CTL/[Punch]/[Bend]", "Bent Part / Tube Laser/[Punch]/[Bend]", "Unbent Part / CTL/[Punch]", "Unbent Part / Tube Laser/[Punch]", "Unbent Part / Roll Bending"));
    }

    @Test
    @Description("Validate routings Die Cast")
    public void routingsDieCasting() {
        loginPage = new CIDLoginPage(driver);
        routingsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("SandCast.x_t"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("High Pressure Die Cast", "Gravity Die Cast"));
    }

    @Test
    @Description("Validate routings Sand Cast")
    public void routingsSandCasting() {
        loginPage = new CIDLoginPage(driver);
        routingsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("SandCast.x_t"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("Vertical Automatic", "Horizontal Automatic", "Manual Std", "Manual Floor", "Manual Pit"));
    }

    @Test
    @Issue("BA-867")
    @Description("Validate routings Forging")
    public void routingsForging() {
        loginPage = new CIDLoginPage(driver);
        routingsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("ap_blow_molding_excerise_EL0000.STEP"))
            .selectProcessGroup(ProcessGroupEnum.FORGING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("2AL+3AM Routing", "4 Axis Mill Routing", "3 Axis Lathe Routing", "2AL+4AM Routing", "Drill Press Routing", "5 Axis Mill Routing", "3 Axis Mill Routing", "MillTurn Routing", "2AL+5AM Routing"));
    }

    @Test
    @Description("Validate routings Plastic Moulding")
    public void routingsPlasticMoulding() {
        loginPage = new CIDLoginPage(driver);
        routingsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("ap_blow_molding_excerise_EL0000.STEP"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("Injection Mold", "Reaction Injection Mold", "Structural Foam Mold"));
    }

    @Test
    @Issue("BA-867")
    @Description("Validate routings Powder Metal")
    public void routingsPowderMetal() {
        loginPage = new CIDLoginPage(driver);
        routingsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("PowderMetalShaft.stp"))
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("2AL+3AM Routing", "4 Axis Mill Routing", "3 Axis Lathe Routing", "2AL+4AM Routing", "Drill Press Routing", "5 Axis Mill Routing", "3 Axis Mill Routing", "MillTurn Routing", "2AL+5AM Routing"));
    }

    @Test
    @Description("Validate routings Rapid Prototyping")
    public void routingsRapidPrototyping() {
        loginPage = new CIDLoginPage(driver);
        routingsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Rapid Prototyping.stp"))
            .selectProcessGroup(ProcessGroupEnum.RAPID_PROTOTYPING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("3D Printing", "Selective Laser Sintering", "Stereolithography"));
    }

    @Test
    @Description("Validate routings Roto & Blow Moulding")
    public void routingsRotoBlowMould() {
        loginPage = new CIDLoginPage(driver);
        routingsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Rapid Prototyping.stp"))
            .selectProcessGroup(ProcessGroupEnum.ROTO_BLOW_MOLDING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("Blow Molding", "Rotational Molding"));
    }

    @Test
    @Issue("BA-867")
    @Description("Validate routings Sheet Metal")
    public void routingsSheetMetal() {
        loginPage = new CIDLoginPage(driver);
        routingsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("700-33770-01_A0.stp"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("Stage Tooling", "Prog Die", "[CTL]/Shear/Press", "[CTL]/Shear/Chemical Mill", "Tandem Die", "[CTL]/Laser/[Bend]", "[CTL]/Laser Punch/[Bend]", "[CTL]/Plasma/[Deslag]/[Bend]", "[CTL]/Plasma Punch/[Deslag]/[Bend]", "[CTL]/Oxyfuel/[Deslag]/[Bend]", "[CTL]/Waterjet/[Bend]", "[CTL]/Turret/[Bend]", "[CTL]/[Bend]"));
    }

    @Test
    @Description("Validate routings Sheet Metal - Hydroforming")
    public void routingsHydroforming() {
        loginPage = new CIDLoginPage(driver);
        routingsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Hydroforming.stp"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_HYDROFORMING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("Laser Cut - Fluid Cell Routing", "Router Cut - Fluid Cell Routing", "Offline Blank - Fluid Cell Routing", "Laser Cut - Deep Draw Routing", "Router Cut - Deep Draw Routing", "Offline Blank - Deep Draw Routing"));
    }

    @Test
    @Description("Validate routings Sheet Metal - Stretchforming")
    public void routingsStretchforming() {
        loginPage = new CIDLoginPage(driver);
        routingsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Hydroforming.stp"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL_STRETCH_FORMING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("Stretch Form Transverse", "Stretch Form Longitudinal"));
    }

    @Test
    @Description("Validate routings Sheet Plastic")
    public void routingsSheetPlastic() {
        loginPage = new CIDLoginPage(driver);
        routingsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("sheet_plastic.STEP"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_PLASTIC.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("Single Cavity Mold", "2x1-Cavity Mold", "2x2-Cavity Mold"));
    }

    @Test
    @Issue("BA-867")
    @Description("Validate routings Stock Machining")
    public void routingsStockMachining() {
        loginPage = new CIDLoginPage(driver);
        routingsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("225_gasket-1-solid1.prt.1"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton();

        assertThat(routingsPage.getRoutings(), containsInAnyOrder("3 Axis Lathe Routing", "2AL+3AM Routing", "3 Axis Mill Routing", "4 Axis Mill Routing", "5 Axis Mill Routing", "3AM+Drill Press Routing", "3AM+4AM Routing", "3AM+5AM Routing", "MillTurn Routing", "2AL+4AM Routing", "2AL+5AM Routing", "2ABFL and 3AM routing", "3ABFL routing"));
    }
}