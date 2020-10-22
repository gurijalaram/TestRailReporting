package evaluate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageobjects.pages.evaluate.CostDetailsPage;
import pageobjects.pages.evaluate.EvaluatePage;
import pageobjects.pages.evaluate.ReferenceComparePage;
import pageobjects.pages.evaluate.designguidance.tolerances.WarningPage;
import pageobjects.pages.login.CidLoginPage;
import pageobjects.toolbars.EvaluateHeader;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class CostAllCadTests extends TestBase {

    private CidLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private CostDetailsPage costDetailsPage;
    private EvaluateHeader evaluateHeader;
    private WarningPage warningPage;
    private ReferenceComparePage referenceComparePage;

    private File resourceFile;

    public CostAllCadTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"574", "565", "567"})
    @Description("CAD file from all supported CAD formats - SLDPRT")
    public void testCADFormatSLDPRT() {

        resourceFile = FileResourceUtil.getResourceAsFile("Machined Box AMERICAS.SLDPRT");

        loginPage = new CidLoginPage(driver);
        costDetailsPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openCostDetails()
            .expandDropdown("Piece Part Cost")
            .expandDropdown("Total Variable Costs");

        assertThat(costDetailsPage.getCostContribution("Material Cost "), containsString("17.13"));
        assertThat(costDetailsPage.getCostContribution("Labor "), containsString("8.15"));
        assertThat(costDetailsPage.getCostContribution("Direct Overhead "), containsString("2.28"));
    }

    @Test
    @TestRail(testCaseId = {"566"})
    @Description("Be able to determine whether a decision has caused a cost increase or decrease")
    public void costIncreaseDecrease() {

        resourceFile = FileResourceUtil.getResourceAsFile("powderMetal.stp");

        loginPage = new CidLoginPage(driver);
        referenceComparePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openMaterialCompositionTable()
            .selectMaterialComposition("FN-0205")
            .apply()
            .costScenario()
            .expandReferencePanel();

        assertThat(referenceComparePage.getMaterialCostDelta(), containsString("up"));
        assertThat(referenceComparePage.getPiecePartCostDelta(), containsString("down"));
        assertThat(referenceComparePage.getFullyBurdenedCostDelta(), containsString("down"));
        assertThat(referenceComparePage.getTotalCapitalInvestmentsDelta(), containsString("up"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - par")
    public void testCADFormatPar() {

        resourceFile = FileResourceUtil.getResourceAsFile("26136.par");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - CATPart")
    public void testCADFormatCATPart() {

        resourceFile = FileResourceUtil.getResourceAsFile("Plastic moulded cap DFM.CATPart");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - prt.4")
    public void testCADFormatPRT4() {

        resourceFile = FileResourceUtil.getResourceAsFile("turning.prt.4");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - Creo")
    public void testCADFormatCreo() {

        resourceFile = FileResourceUtil.getResourceAsFile("turning.prt.4");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - NX")
    public void testCADFormatNX() {

        resourceFile = FileResourceUtil.getResourceAsFile("Locker_bottom_panel.prt");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - Inventor")
    public void testCADFormatInventor() {

        resourceFile = FileResourceUtil.getResourceAsFile("VERTICAL PLATE.ipt");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - STEP")
    public void testCADFormatSTEP() {

        resourceFile = FileResourceUtil.getResourceAsFile("partbody_2.stp");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - Parasolid")
    public void testCADFormatParasolid() {

        resourceFile = FileResourceUtil.getResourceAsFile("bracket_basic_steel_PMI.x_t");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - ACIS")
    public void testCADFormatParaACIS() {

        resourceFile = FileResourceUtil.getResourceAsFile("Plastic moulded cap thinPart.SAT");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), (is(true)));
    }

    @Test
    @TestRail(testCaseId = {"1605"})
    @Description("Upload large GCD part. Part should be displayed in the viewer within 60 seconds")
    public void translationTest() {

        resourceFile = FileResourceUtil.getResourceAsFile("LargePart.prt.1");

        loginPage = new CidLoginPage(driver);
        evaluateHeader = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .checkForImage(1);

        assertThat(new EvaluatePage(driver).isCostLabel(CostingLabelEnum.READY_TO_COST.getCostingText()), (is(true)));
    }

    @Test
    @TestRail(testCaseId = {"2316", "2317"})
    @Description("Ensure scripts cannot be entered into all available text input fields")
    public void failedUpload() {

        resourceFile = FileResourceUtil.getResourceAsFile("LargePart.prt.1");

        loginPage = new CidLoginPage(driver);
        warningPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk("<script>alert(document.cookie)</script>", resourceFile, WarningPage.class);

        assertThat(warningPage.getWarningText(), Matchers.containsString("Some of the supplied inputs are invalid"));
    }

    @Test
    @TestRail(testCaseId = {"580"})
    @Description("Failure to create a new scenario that has a blank scenario name or is named using unsupported characters")
    public void failedBlankScenarioName() {

        resourceFile = FileResourceUtil.getResourceAsFile("PowderMetalShaft.stp");

        loginPage = new CidLoginPage(driver);
        warningPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk("", resourceFile, WarningPage.class);

        assertThat(warningPage.getWarningText(), Matchers.containsString("Some of the supplied inputs are invalid"));
    }
}