package evaluate.designguidance.dtc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.utils.AfterTestUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Test;

import java.io.File;

public class DFMRiskTests extends TestBase {

    private CIDLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private UserCredentials currentUser;

    private File resourceFile;

    public DFMRiskTests() {
        super();
    }

    @After
    public void resetSettings() {
        if (currentUser != null) {
            new AfterTestUtil().resetAllSettings(currentUser.getUsername());
        }
    }

    @Test
    @TestRail(testCaseId = {"3831"})
    @Description("Validate DFM Risk - High for Stock Machining")
    public void stockMachiningHighDFM() {

        resourceFile = new FileResourceUtil().getResourceFile("gs515625_gt077_high.prt.2");
        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        evaluatePage = loginPage.login(currentUser)
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
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
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
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
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
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
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
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
            .uploadFile(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getDFMRiskIcon(), containsString("dtc-medium-risk-icon"));
        assertThat(evaluatePage.getDfmRisk(), is("Medium"));
    }
}