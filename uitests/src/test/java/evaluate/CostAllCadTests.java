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
import org.junit.Test;

import java.util.Scanner;

public class CostAllCadTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;

    private String filePath = new Scanner(ProcessGroupsTests.class.getClassLoader()
        .getResourceAsStream("filepath.txt"), "UTF-8").useDelimiter("\\A").next();

    public CostAllCadTests() {
        super();
    }

    @Test
    @Description("CAD file from all supported CAD formats - SLDPRT")
    @Severity(SeverityLevel.CRITICAL)
    public void testCADFormatSLDPRT() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("CostAllCAD Forging", filePath, "Machined Box AMERICAS.SLDPRT")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(new EvaluatePage(driver).checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("CAD file from all supported CAD formats - par")
    @Severity(SeverityLevel.CRITICAL)
    public void testCADFormatPar() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("CostAllCAD par", filePath, "26136.par")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(new EvaluatePage(driver).checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("CAD file from all supported CAD formats - CATPart")
    @Severity(SeverityLevel.CRITICAL)
    public void testCADFormatCATPart() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("CostAllCAD CATPart", filePath, "Plastic moulded cap DFM.CATPart")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(new EvaluatePage(driver).checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("CAD file from all supported CAD formats - prt.4")
    @Severity(SeverityLevel.CRITICAL)
    public void testCADFormatPRT4() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("CostAllCAD prt 4", filePath, "turning.prt.4")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(new EvaluatePage(driver).checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("CAD file from all supported CAD formats - Creo")
    @Severity(SeverityLevel.CRITICAL)
    public void testCADFormatCreo() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("CostAllCAD Creo", filePath, "turning.prt.4")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(new EvaluatePage(driver).checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("CAD file from all supported CAD formats - NX")
    @Severity(SeverityLevel.CRITICAL)
    public void testCADFormatNX() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("CostAllCAD NX", filePath, "Locker_bottom_panel.prt")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(new EvaluatePage(driver).checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("CAD file from all supported CAD formats - Inventor")
    @Severity(SeverityLevel.CRITICAL)
    public void testCADFormatInventor() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("CostAllCAD Inventor", filePath, "VERTICAL PLATE.ipt")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(new EvaluatePage(driver).checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("CAD file from all supported CAD formats - STEP")
    @Severity(SeverityLevel.CRITICAL)
    public void testCADFormatSTEP() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("CostAllCAD STEP", filePath, "partbody_2.stp")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(new EvaluatePage(driver).checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("CAD file from all supported CAD formats - Parasolid")
    @Severity(SeverityLevel.CRITICAL)
    public void testCADFormatParasolid() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("CostAllCAD Parasolid", filePath, "bracket_basic_steel_PMI.x_t")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(new EvaluatePage(driver).checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }

    @Test
    @Description("CAD file from all supported CAD formats - ACIS")
    @Severity(SeverityLevel.CRITICAL)
    public void testCADFormatParaACIS() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("CostAllCAD ACIS", filePath, "Plastic moulded cap thinPart.SAT")
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel());

        assertThat(new EvaluatePage(driver).checkCostLabel(CostingLabelEnum.COSTING_UP_TO_DATE.getCostingLabel()), is(true));
    }
}
