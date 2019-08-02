package test.java.evaluate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.CostingLabelEnum;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class CostAllCadTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;

    public CostAllCadTests() {
        super();
    }

    @Test
    @Description("CAD file from all supported CAD formats - SLDPRT")
    @Severity(SeverityLevel.CRITICAL)
    public void testCADFormatSLDPRT() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.uploadFile("CostAllCAD Forging", new FileResourceUtil().getResourceFile("Machined Box AMERICAS.SLDPRT"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("CAD file from all supported CAD formats - par")
    @Severity(SeverityLevel.CRITICAL)
    public void testCADFormatPar() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.uploadFile("CostAllCAD par", new FileResourceUtil().getResourceFile("26136.par"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("CAD file from all supported CAD formats - CATPart")
    @Severity(SeverityLevel.CRITICAL)
    public void testCADFormatCATPart() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.uploadFile("CostAllCAD CATPart", new FileResourceUtil().getResourceFile("Plastic moulded cap DFM.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("CAD file from all supported CAD formats - prt.4")
    @Severity(SeverityLevel.CRITICAL)
    public void testCADFormatPRT4() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.uploadFile("CostAllCAD prt 4", new FileResourceUtil().getResourceFile("turning.prt.4"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("CAD file from all supported CAD formats - Creo")
    @Severity(SeverityLevel.CRITICAL)
    public void testCADFormatCreo() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.uploadFile("CostAllCAD Creo", new FileResourceUtil().getResourceFile("turning.prt.4"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("CAD file from all supported CAD formats - NX")
    @Severity(SeverityLevel.CRITICAL)
    public void testCADFormatNX() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.uploadFile("CostAllCAD NX", new FileResourceUtil().getResourceFile("Locker_bottom_panel.prt"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("CAD file from all supported CAD formats - Inventor")
    @Severity(SeverityLevel.CRITICAL)
    public void testCADFormatInventor() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.uploadFile("CostAllCAD Inventor", new FileResourceUtil().getResourceFile("VERTICAL PLATE.ipt"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("CAD file from all supported CAD formats - STEP")
    @Severity(SeverityLevel.CRITICAL)
    public void testCADFormatSTEP() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.uploadFile("CostAllCAD STEP", new FileResourceUtil().getResourceFile("partbody_2.stp"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("CAD file from all supported CAD formats - Parasolid")
    @Severity(SeverityLevel.CRITICAL)
    public void testCADFormatParasolid() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.uploadFile("CostAllCAD Parasolid", new FileResourceUtil().getResourceFile("bracket_basic_steel_PMI.x_t"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("CAD file from all supported CAD formats - ACIS")
    @Severity(SeverityLevel.CRITICAL)
    public void testCADFormatParaACIS() throws UnsupportedEncodingException {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.uploadFile("CostAllCAD ACIS", new FileResourceUtil().getResourceFile("Plastic moulded cap thinPart.SAT"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(evaluatePage.checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }
}