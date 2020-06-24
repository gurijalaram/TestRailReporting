package evaluate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.CostDetailsPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.ReferenceComparePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.tolerances.WarningPage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.pageobjects.toolbars.EvaluateHeader;
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
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class CostAllCadTests extends TestBase {

    private CIDLoginPage loginPage;
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

        resourceFile = new FileResourceUtil().getResourceFile("Machined Box AMERICAS.SLDPRT");

        loginPage = new CIDLoginPage(driver);
        costDetailsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openCostDetails()
            .expandDropdown("Piece Part Cost")
            .expandDropdown("Total Variable Costs");

        assertThat(costDetailsPage.getCostContribution("Material Cost "), containsString("16.07"));
        assertThat(costDetailsPage.getCostContribution("Labor "), containsString("4.94"));
        assertThat(costDetailsPage.getCostContribution("Direct Overhead "), containsString("1.32"));
    }

    @Test
    @TestRail(testCaseId = {"566"})
    @Description("Be able to determine whether a decision has caused a cost increase or decrease")
    public void costIncreaseDecrease() {

        resourceFile = new FileResourceUtil().getResourceFile("powderMetal.stp");

        loginPage = new CIDLoginPage(driver);
        referenceComparePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openMaterialCompositionTable()
            .selectMaterialComposition("FN-0205")
            .apply()
            .costScenario()
            .openReferenceCompare();

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

        resourceFile = new FileResourceUtil().getResourceFile("26136.par");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - CATPart")
    public void testCADFormatCATPart() {

        resourceFile = new FileResourceUtil().getResourceFile("Plastic moulded cap DFM.CATPart");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - prt.4")
    public void testCADFormatPRT4() {

        resourceFile = new FileResourceUtil().getResourceFile("turning.prt.4");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - Creo")
    public void testCADFormatCreo() {

        resourceFile = new FileResourceUtil().getResourceFile("turning.prt.4");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - NX")
    public void testCADFormatNX() {

        resourceFile = new FileResourceUtil().getResourceFile("Locker_bottom_panel.prt");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - Inventor")
    public void testCADFormatInventor() {

        resourceFile = new FileResourceUtil().getResourceFile("VERTICAL PLATE.ipt");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - STEP")
    public void testCADFormatSTEP() {

        resourceFile = new FileResourceUtil().getResourceFile("partbody_2.stp");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - Parasolid")
    public void testCADFormatParasolid() {

        resourceFile = new FileResourceUtil().getResourceFile("bracket_basic_steel_PMI.x_t");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - ACIS")
    public void testCADFormatParaACIS() {

        resourceFile = new FileResourceUtil().getResourceFile("Plastic moulded cap thinPart.SAT");

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), (is(true)));
    }

    @Test
    @TestRail(testCaseId = {"1605"})
    @Description("Upload large GCD part. Part should be displayed in the viewer within 60 seconds")
    public void translationTest() {

        resourceFile = new FileResourceUtil().getResourceFile("LargePart.prt.1");

        loginPage = new CIDLoginPage(driver);
        evaluateHeader = loginPage.login(UserUtil.getUser())
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile);

        assertThat(new EvaluatePage(driver).getCostLabel(CostingLabelEnum.READY_TO_COST.getCostingText()), (is(true)));
    }

    @Test
    @TestRail(testCaseId = {"2316", "2317"})
    @Description("Ensure scripts cannot be entered into all available text input fields")
    public void failedUpload() {

        resourceFile = new FileResourceUtil().getResourceFile("LargePart.prt.1");

        loginPage = new CIDLoginPage(driver);
        warningPage = loginPage.login(UserUtil.getUser())
            .failedUploadFile("<script>alert(document.cookie)</script>", resourceFile);

        assertThat(warningPage.getWarningText(), Matchers.containsString("Some of the supplied inputs are invalid"));
    }

    @Test
    @TestRail(testCaseId = {"580"})
    @Description("Failure to create a new scenario that has a blank scenario name or is named using unsupported characters")
    public void failedBlankScenarioName() {

        resourceFile = new FileResourceUtil().getResourceFile("PowderMetalShaft.stp");

        loginPage = new CIDLoginPage(driver);
        warningPage = loginPage.login(UserUtil.getUser())
            .failedUploadFile("", resourceFile);

        assertThat(warningPage.getWarningText(), Matchers.containsString("Some of the supplied inputs are invalid"));
    }
}