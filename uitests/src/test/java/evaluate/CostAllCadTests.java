package evaluate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.header.EvaluateHeader;
import com.apriori.pageobjects.pages.evaluate.CostDetailsPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
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

import org.junit.Test;

public class CostAllCadTests extends TestBase {

    private CIDLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private CostDetailsPage costDetailsPage;
    private EvaluateHeader evaluateHeader;

    public CostAllCadTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"574", "565", "567"})
    @Description("CAD file from all supported CAD formats - SLDPRT")
    public void testCADFormatSLDPRT() {
        loginPage = new CIDLoginPage(driver);
        costDetailsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Machined Box AMERICAS.SLDPRT"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openCostDetails()
            .expandDropdown("Piece Part Cost")
            .expandDropdown("Total Variable Costs");

        assertThat(costDetailsPage.getCostContribution("Material Cost "), containsString("14.51"));
        assertThat(costDetailsPage.getCostContribution("Labor "), containsString("4.94"));
        assertThat(costDetailsPage.getCostContribution("Direct Overhead "), containsString("1.31"));
    }

    @Test
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - par")
    public void testCADFormatPar() {
        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("26136.par"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - CATPart")
    public void testCADFormatCATPart() {
        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Plastic moulded cap DFM.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - prt.4")
    public void testCADFormatPRT4() {
        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("turning.prt.4"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - Creo")
    public void testCADFormatCreo() {
        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("turning.prt.4"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - NX")
    public void testCADFormatNX() {
        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Locker_bottom_panel.prt"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - Inventor")
    public void testCADFormatInventor() {
        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("VERTICAL PLATE.ipt"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - STEP")
    public void testCADFormatSTEP() {
        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("partbody_2.stp"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - Parasolid")
    public void testCADFormatParasolid() {
        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("bracket_basic_steel_PMI.x_t"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - ACIS")
    public void testCADFormatParaACIS() {
        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Plastic moulded cap thinPart.SAT"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), (is(true)));
    }

    @Test
    @TestRail(testCaseId = {"1605"})
    @Description("Upload large GCD part. Part should be displayed in the viewer within 60 seconds")
    public void translationTest() {
        loginPage = new CIDLoginPage(driver);
        evaluateHeader = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("LargePart.prt.1"))
            .checkForImage(0.1);

        assertThat(new EvaluatePage(driver).getCostLabel(CostingLabelEnum.READY_TO_COST.getCostingText()), (is(true)));
    }
}