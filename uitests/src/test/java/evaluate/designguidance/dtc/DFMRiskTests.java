package evaluate.designguidance.dtc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.pageobjects.toolbars.GenericHeader;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.CostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

import java.io.File;

public class DFMRiskTests extends TestBase {

    private CIDLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private UserCredentials currentUser;
    private GenericHeader genericHeader;

    private File resourceFile;
    private File cadResourceFile;

    public DFMRiskTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"3831"})
    @Description("Validate DFM Risk - High for Stock Machining")
    public void stockMachiningHighDFM() {

        resourceFile = new FileResourceUtil().getResourceFile("gs515625_gt077_high.prt.2");
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getDFMRiskIcon(), containsString("dtc-high-risk-icon"));
        assertThat(evaluatePage.getDfmRisk(), is("High"));
    }

    @Test
    @TestRail(testCaseId = {"3832"})
    @Description("Validate DFM Risk - Medium for Stock Machining")
    public void stockMachiningMediumDFM() {

        resourceFile = new FileResourceUtil().getResourceFile("9856874Medium.prt.1");
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getDFMRiskIcon(), containsString("dtc-medium-risk-icon"));
        assertThat(evaluatePage.getDfmRisk(), is("Medium"));
    }

    @Test
    @TestRail(testCaseId = {"3834"})
    @Description("Validate DFM Risk - Critical for Sheet Metal")
    public void sheetMetalCriticalDFM() {

        resourceFile = new FileResourceUtil().getResourceFile("1271576_CRITICAL.prt.1");
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getDFMRiskIcon(), containsString("dtc-critical-risk-icon"));
        assertThat(evaluatePage.getDfmRisk(), is("Critical"));
    }

    @Test
    @TestRail(testCaseId = {"3835"})
    @Description("Validate DFM Risk - High for Sheet Metal")
    public void sheetMetalHighDFM() {

        resourceFile = new FileResourceUtil().getResourceFile("3571050_cad.prt.1");
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getDFMRiskIcon(), containsString("dtc-high-risk-icon"));
        assertThat(evaluatePage.getDfmRisk(), is("High"));
    }

    @Test
    @TestRail(testCaseId = {"3840"})
    @Description("Validate DFM Risk - High for Sheet Metal")
    public void plasticMouldedMediumDFM() {

        resourceFile = new FileResourceUtil().getResourceFile("PlasticMoulded-Special Tooling Sliders Lifters.CATPart");
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getDFMRiskIcon(), containsString("dtc-medium-risk-icon"));
        assertThat(evaluatePage.getDfmRisk(), is("Medium"));
    }

    @Test
    @TestRail(testCaseId = {"3849"})
    @Description("Validate DFM Risk - Low for Sand Casting")
    public void sandCastLowDFM() {

        resourceFile = new FileResourceUtil().getResourceFile("casting_q5_thinvalve.prt");
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getDFMRiskIcon(), containsString("dtc-low-risk-icon"));
        assertThat(evaluatePage.getDfmRisk(), is("Low"));
    }

    @Test
    @TestRail(testCaseId = {"3885", "4198"})
    @Description("Validate when switch PG from a group with dfm risk to a group without that the risk is removed")
    public void noRiskTransferDie() {

        resourceFile = new FileResourceUtil().getResourceFile("bracket_basic.prt");
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getDFMRiskIcon(), containsString("dtc-low-risk-icon"));
        assertThat(evaluatePage.getDfmRisk(), is("Low"));

        evaluatePage.selectProcessGroup(ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isDFMRiskIconDisplayed(), is(false));
        assertThat(evaluatePage.getDfmRisk(), is("―"));
    }

    @Test
    @TestRail(testCaseId = {"3862"})
    @Description("Validate DFM Risk can be REDUCED for STOCK MACHINING")
    public void dfmReducedStockMachining() {

        String file = "1379344.stp";
        resourceFile = new FileResourceUtil().getResourceFile(file);
        cadResourceFile = new FileResourceUtil().getResourceCadFile(file);
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getDFMRiskIcon(), containsString("dtc-high-risk-icon"));
        assertThat(evaluatePage.getDfmRisk(), is("High"));

        evaluatePage.uploadCadFile(cadResourceFile);
        assertThat(new EvaluatePage(driver).getCostLabel(CostingLabelEnum.TRANSLATING.getCostingText()), is(true));
        assertThat(new EvaluatePage(driver).getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));

        evaluatePage.costScenario();
        assertThat(evaluatePage.getDFMRiskIcon(), containsString("dtc-low-risk-icon"));
        assertThat(evaluatePage.getDfmRisk(), is("Low"));
    }

    @Test
    @TestRail(testCaseId = {"3864"})
    @Description("Validate DFM Risk can be REDUCED for SHEET METAL")
    public void dfmReducedSheetMetal() {

        String file = "bracketdfm.SLDPRT";
        resourceFile = new FileResourceUtil().getResourceFile(file);
        cadResourceFile = new FileResourceUtil().getResourceCadFile(file);
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getDFMRiskIcon(), containsString("dtc-medium-risk-icon"));
        assertThat(evaluatePage.getDfmRisk(), is("Medium"));

        evaluatePage.uploadCadFile(cadResourceFile);
        assertThat(new EvaluatePage(driver).getCostLabel(CostingLabelEnum.TRANSLATING.getCostingText()), is(true));
        assertThat(new EvaluatePage(driver).getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
        assertThat(evaluatePage.getDFMRiskIcon(), containsString("dtc-low-risk-icon"));
        assertThat(evaluatePage.getDfmRisk(), is("Low"));
    }

    @Test
    @TestRail(testCaseId = {"3872"})
    @Description("Validate DFM Risk can be REDUCED for DIE CAST")
    public void dfmReducedDieCast() {

        String file = "manifold.prt.1";
        resourceFile = new FileResourceUtil().getResourceFile(file);
        cadResourceFile = new FileResourceUtil().getResourceCadFile(file);
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getDFMRiskIcon(), containsString("dtc-medium-risk-icon"));
        assertThat(evaluatePage.getDfmRisk(), is("Medium"));

        evaluatePage.uploadCadFile(cadResourceFile);
        assertThat(new EvaluatePage(driver).getCostLabel(CostingLabelEnum.TRANSLATING.getCostingText()), is(true));
        assertThat(new EvaluatePage(driver).getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));

        evaluatePage.openDesignGuidance()
            .closePanel();
        assertThat(evaluatePage.getDFMRiskIcon(), containsString("dtc-low-risk-icon"));
        assertThat(evaluatePage.getDfmRisk(), is("Low"));
    }

    @Test
    @TestRail(testCaseId = {"3874"})
    @Description("Validate DFM Risk can be REDUCED for SAND CAST")
    public void dfmReducedSandCast() {

        String file = "SandCastBox.SLDPRT";
        resourceFile = new FileResourceUtil().getResourceFile(file);
        cadResourceFile = new FileResourceUtil().getResourceCadFile(file);
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getDFMRiskIcon(), containsString("dtc-high-risk-icon"));
        assertThat(evaluatePage.getDfmRisk(), is("High"));

        evaluatePage.uploadCadFile(cadResourceFile);
        assertThat(new EvaluatePage(driver).getCostLabel(CostingLabelEnum.TRANSLATING.getCostingText()), is(true));
        assertThat(new EvaluatePage(driver).getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));

        evaluatePage.openDesignGuidance()
            .closePanel();
        assertThat(evaluatePage.getDFMRiskIcon(), containsString("dtc-low-risk-icon"));
        assertThat(evaluatePage.getDfmRisk(), is("Low"));
    }
}