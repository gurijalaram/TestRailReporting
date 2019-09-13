package test.java.evaluate;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.containsInAnyOrder;

import io.qameta.allure.Description;
import main.java.base.TestBase;
import main.java.enums.CostingLabelEnum;
import main.java.enums.CurrencyEnum;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.enums.VPEEnum;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.evaluate.materialutilization.MaterialCompositionPage;
import main.java.pages.evaluate.process.ProcessPage;
import main.java.pages.evaluate.process.RoutingsPage;
import main.java.pages.login.LoginPage;
import main.java.pages.settings.SettingsPage;
import main.java.pages.settings.ToleranceSettingsPage;
import main.java.utils.FileResourceUtil;
import main.java.utils.TestRail;
import main.java.utils.Util;
import org.junit.Test;

public class ProcessRoutingTests extends TestBase {

    private LoginPage loginPage;
    private ProcessPage processPage;
    private EvaluatePage evaluatePage;
    private SettingsPage settingsPage;
    private ToleranceSettingsPage toleranceSettingsPage;
    private RoutingsPage routingsPage;
    private MaterialCompositionPage materialCompositionPage;

    public ProcessRoutingTests() {
        super();
    }

    @Test
    @Description("Validate the user can Change the process routing in CI Design")
    public void testAlternateRoutingSelection() {
        loginPage = new LoginPage(driver);
        processPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("3 Axis Mill")
            .apply();

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails("3 Axis Mill"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"645"})
    @Description("View detailed information about costed process")
    public void testViewProcessDetails() {
        loginPage = new LoginPage(driver);
        toleranceSettingsPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("PlasticMoulding.CATPart"))
            .openSettings()
            .changeCurrency(CurrencyEnum.USD.getCurrency())
            .openTolerancesTab()
            .selectAssumeTolerance();

        settingsPage = new SettingsPage(driver);
        processPage = settingsPage.save(EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails();

        assertThat(processPage.getSelectionTableDetails(), arrayContaining("Cycle Time (s): 29.67", "Piece Part Cost (USD): 0.43",
            "Fully Burdened Cost (USD): 0.82", "Total Capital Investments (USD): 10,732.01"));
    }

    @Test
    @TestRail(testCaseId = {"646"})
    @Description("View individual process steps")
    public void testViewProcessSteps() {
        loginPage = new LoginPage(driver);
        toleranceSettingsPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("bracket_basic.prt"))
            .openSettings()
            .openTolerancesTab()
            .selectAssumeTolerance();

        settingsPage = new SettingsPage(driver);
        processPage = settingsPage.save(EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openProcessDetails();

        assertThat(processPage.getRoutingLabels(), hasItems("Material Stock", "Turret Press", "Bend Brake"));
    }

    @Test
    @TestRail(testCaseId = {"1649", "1656"})
    @Description("Validate the user can Change the process routing")
    public void changeRouting() {
        loginPage = new LoginPage(driver);
        processPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
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

        assertThat(evaluatePage.getProcessRoutingDetails("Reaction Injection Molding"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"1667", "1668"})
    @Description("Validate the Use selected for future costing checkbox works correctly")
    public void testRoutingCheckBox() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
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

        assertThat(evaluatePage.getProcessRoutingDetails("Injection Molding"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"1665", "1666"})
    @Description("Validate the information updates in the routing modal box")
    public void testlastRouting() {
        loginPage = new LoginPage(driver);
        routingsPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
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
    @TestRail(testCaseId = {"1670"})
    @Description("Validate behaviour when forcing a material that will fail costing within CID")
    public void failCostingRouting() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
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
    }

    @Test
    @TestRail(testCaseId = {"1659"})
    @Description("Validate costing results update accordingly for a newly selected and costed routing")
    public void costUpdatedRouting() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("HoleProximityTest.SLDPRT"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getBurdenedCost("1.63"), is(true));

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("[CTL]/Waterjet/[Bend]")
            .apply()
            .closeProcessPanel()
            .costScenario();

        assertThat(evaluatePage.getBurdenedCost("2.02"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"1661"})
    @Description("Validate materials selected are appropriate for selected routing.")
    public void routingMaterials() {
        loginPage = new LoginPage(driver);
        materialCompositionPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
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

        assertThat(materialCompositionPage.getListOfMaterialTypes(), containsInAnyOrder("All", "ABS", "Acetal", "Nylon", "PET", "Polycarbonate", "Polypropylene", "Polystyrene", "Polyurethane"));
    }

    @Test
    @TestRail(testCaseId = {"1664"})
    @Description("Validate behaviour when selecting a PG that auto triggers a secondary process")
    public void routingSecondaryPG() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("plasticLid.SLDPRT"))
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

        assertThat(evaluatePage.getProcessRoutingDetails("Printing / Breakoff"), is(true));

        evaluatePage = new EvaluatePage(driver);
        evaluatePage.openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Powder Bed Fusion")
            .apply()
            .closeProcessPanel()
            .openMaterialCompositionTable()
            .selectMaterialComposition("Aluminum AlSi10Mg")
            .apply()
            .costScenario();

        assertThat(evaluatePage.getProcessRoutingDetails("Stress Relief / Ultrasonic Cleaning"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"1663"})
    @Description("Validate a variety of secondary processes can be added for newly selected routings")
    public void secondaryProcessesRoutings() {
        loginPage = new LoginPage(driver);
        processPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
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

        assertThat(processPage.getRoutingLabels(), hasItems("Carton Forming", "Pack & Load", "Carton Sealing"));
    }
}