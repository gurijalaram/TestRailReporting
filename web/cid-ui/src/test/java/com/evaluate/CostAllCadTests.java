package com.evaluate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CostingLabelEnum;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.pageobjects.pages.evaluate.CostDetailsPage;
import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.pages.evaluate.ReferenceComparePage;
import com.pageobjects.pages.evaluate.designguidance.tolerances.WarningPage;
import com.pageobjects.pages.login.CidLoginPage;
import com.pageobjects.toolbars.EvaluateHeader;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.experimental.categories.Category;
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
    @TestRail(testCaseId = {"574", "565", "567"})
    @Description("CAD file from all supported CAD formats - SLDPRT")
    public void testCADFormatSLDPRT() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Machined Box AMERICAS.SLDPRT");

        loginPage = new CidLoginPage(driver);
        costDetailsPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .selectVPE(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
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
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "powderMetal.stp");

        loginPage = new CidLoginPage(driver);
        referenceComparePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .selectVPE(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
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
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - par")
    public void testCADFormatPar() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "26136.par");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - CATPart")
    public void testCADFormatCATPart() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Plastic moulded cap DFM.CATPart");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - prt.4")
    public void testCADFormatPRT4() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "turning.prt.4");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - Creo")
    public void testCADFormatCreo() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "turning.prt.4");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @Issue("AP-64577")
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - Creo 7")
    public void testCADFormatCreo7() {

        resourceFile = FileResourceUtil.getResourceAsFile("creo7test.prt.1");

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
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Locker_bottom_panel.prt");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - Inventor")
    public void testCADFormatInventor() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "VERTICAL PLATE.ipt");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @Issue("AP-64578")
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - Inventor 2020")
    public void testCADFormatInventor2020() {

        resourceFile = FileResourceUtil.getResourceAsFile("Inventor2020test.ipt");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - STEP")
    public void testCADFormatSTEP() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "partbody_2.stp");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - Parasolid")
    public void testCADFormatParasolid() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic_steel_PMI.x_t");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - ACIS")
    public void testCADFormatParaACIS() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Plastic moulded cap thinPart.SAT");

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), (is(true)));
    }

    @Test
    @TestRail(testCaseId = {"1605"})
    @Description("Upload large GCD part. Part should be displayed in the viewer within 60 seconds")
    public void translationTest() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.WITHOUT_PG;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "LargePart.prt.1");

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

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.WITHOUT_PG;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "LargePart.prt.1");

        loginPage = new CidLoginPage(driver);
        warningPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk("<script>alert(document.cookie)</script>", resourceFile, WarningPage.class);

        assertThat(warningPage.getWarningText(), Matchers.containsString("Some of the supplied inputs are invalid"));
    }

    @Test
    @TestRail(testCaseId = {"580"})
    @Description("Failure to create a new scenario that has a blank scenario name or is named using unsupported characters")
    public void failedBlankScenarioName() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "PowderMetalShaft.stp");

        loginPage = new CidLoginPage(driver);
        warningPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk("", resourceFile, WarningPage.class);

        assertThat(warningPage.getWarningText(), Matchers.containsString("Some of the supplied inputs are invalid"));
    }
}