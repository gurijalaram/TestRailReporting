package evaluate.designguidance.failures;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.designguidance.FailuresPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.pageobjects.pages.settings.SettingsPage;
import com.apriori.pageobjects.pages.settings.ToleranceSettingsPage;
import com.apriori.pageobjects.utils.AfterTestUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserDataUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;

public class FailuresWarningsTests extends TestBase {

    private LoginPage loginPage;
    private SettingsPage settingsPage;
    private ToleranceSettingsPage toleranceSettingsPage;
    private FailuresPage failuresPage;

    @After
    public void resetToleranceSettings() {
        new AfterTestUtil(driver).resetToleranceSettings();
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"1592", "1059", "1831", "1791"})
    @Description("Ensure that 'Failures/ Warnings tab includes: Messaging")
    public void failedCostingMessaging() {
        loginPage = new LoginPage(driver);
        toleranceSettingsPage = loginPage.login(UserDataUtil.getGlobalUser().getUsername(), UserDataUtil.getGlobalUser().getPassword())
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        failuresPage = settingsPage.save(ExplorePage.class)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario(5)
            .openDesignGuidance()
            .openFailuresTab()
            .selectIssueTypeAndGCD("Failed GCDs", "CurvedWall:100");

        assertThat(failuresPage.getUncostedMessage(), containsString("High Pressure Die Casting is incapable of achieving [Diam Tolerance : 0.002 mm (0.0001 in)"));

        failuresPage.selectIssueTypeAndGCD("Not Supported GCDs", "Not Supported:1");
        assertThat(failuresPage.getUncostedMessage(), containsString("Multiple bodies exist in the model. Only the largest body is used and the remainder are ignored"));
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"1592"})
    @Description("Ensure that 'Failures/ Warnings tab includes: - Issue type & count")
    public void failedCostingCount() {
        loginPage = new LoginPage(driver);
        toleranceSettingsPage = loginPage.login(UserDataUtil.getGlobalUser().getUsername(), UserDataUtil.getGlobalUser().getPassword())
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        failuresPage = settingsPage.save(ExplorePage.class)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("DTCCastingIssues.catpart"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openFailuresTab();

        assertThat(failuresPage.getFailuresCell("Failed GCDs", "Count"), is(equalTo("3")));
        assertThat(failuresPage.getFailuresCell("Failed GCDs", "Reason"), is(equalTo("Failed to cost")));
        assertThat(failuresPage.getFailuresCell("Not Supported GCDs", "Count"), is(equalTo("1")));
        assertThat(failuresPage.getFailuresCell("Not Supported GCDs", "Reason"), is(equalTo("Detached Solid")));
    }
}
