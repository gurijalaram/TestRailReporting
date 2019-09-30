package evaluate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.CostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.UsersEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;

import org.junit.Test;

public class CostAllCadTests extends TestBase {

    private LoginPage loginPage;
    private EvaluatePage evaluatePage;

    public CostAllCadTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - SLDPRT")
    public void testCADFormatSLDPRT() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Machined Box AMERICAS.SLDPRT"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - par")
    public void testCADFormatPar() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("26136.par"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - CATPart")
    public void testCADFormatCATPart() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Plastic moulded cap DFM.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - prt.4")
    public void testCADFormatPRT4() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("turning.prt.4"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - Creo")
    public void testCADFormatCreo() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("turning.prt.4"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - NX")
    public void testCADFormatNX() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Locker_bottom_panel.prt"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - Inventor")
    public void testCADFormatInventor() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("VERTICAL PLATE.ipt"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - STEP")
    public void testCADFormatSTEP() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("partbody_2.stp"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - Parasolid")
    public void testCADFormatParasolid() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("bracket_basic_steel_PMI.x_t"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), is(true));
    }

    @Test
    @TestRail(testCaseId = {"574"})
    @Description("CAD file from all supported CAD formats - ACIS")
    public void testCADFormatParaACIS() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Plastic moulded cap thinPart.SAT"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingText()), (is(true)));
    }
}