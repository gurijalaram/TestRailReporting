package evaluate.designguidance.dtc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.analysis.PropertiesDialogPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.GeometryPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.GuidancePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.tolerances.TolerancePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.pageobjects.pages.settings.SettingsPage;
import com.apriori.pageobjects.pages.settings.ToleranceSettingsPage;
import com.apriori.utils.AfterTestUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
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
    private GuidancePage guidancePage;
    private ToleranceSettingsPage toleranceSettingsPage;
    private SettingsPage settingsPage;
    private EvaluatePage evaluatePage;
    private GeometryPage geometryPage;
    private PropertiesDialogPage propertiesDialogPage;
    private TolerancePage tolerancePage;
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
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getDFMRiskIcon(), containsString("dtc-high-risk-icon"));
        assertThat(evaluatePage.isDfmRisk("High"), is(true));
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
        assertThat(evaluatePage.isDfmRisk("Medium"), is(true));
    }
}