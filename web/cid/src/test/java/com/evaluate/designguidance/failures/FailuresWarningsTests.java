package com.evaluate.designguidance.failures;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.apibase.utils.AfterTestUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.pages.evaluate.designguidance.GuidancePage;
import com.pageobjects.pages.explore.ExplorePage;
import com.pageobjects.pages.login.CidLoginPage;
import com.pageobjects.pages.settings.SettingsPage;
import com.pageobjects.pages.settings.ToleranceSettingsPage;
import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class FailuresWarningsTests extends TestBase {

    private CidLoginPage loginPage;
    private SettingsPage settingsPage;
    private ToleranceSettingsPage toleranceSettingsPage;
    private UserCredentials currentUser;
    private GuidancePage guidancePage;
    private EvaluatePage evaluatePage;

    private File resourceFile;

    @After
    public void resetSettings() {
        if (currentUser != null) {
            new AfterTestUtil().resetAllSettings(currentUser.getUsername());
        }
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"1592", "1059", "1831", "1791"})
    @Description("Ensure that 'Failures/ Warnings tab includes: Messaging")
    public void failedCostingMessaging() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "DTCCastingIssues.catpart");
        currentUser = UserUtil.getUser();

        loginPage = new CidLoginPage(driver);
        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        guidancePage = settingsPage.save(ExplorePage.class)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario(5)
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Failed GCDs", "Curved Walls", "CurvedWall:100");

        assertThat(guidancePage.getGuidanceMessage(), containsString("High Pressure Die Casting is incapable of achieving [Diam Tolerance : 0.002 mm (0.0001 in)"));

        guidancePage.selectIssueTypeAndGCD("Not Supported GCDs", "Detached Solid", "Not Supported:1");
        assertThat(guidancePage.getGuidanceMessage(), containsString("Multiple bodies exist in the model. Only the largest body is used and the remainder are ignored"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"1592", "3830"})
    @Description("Ensure that 'Failures/ Warnings tab includes: - Issue type & count")
    public void failedCostingCount() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "DTCCastingIssues.catpart");
        currentUser = UserUtil.getUser();

        loginPage = new CidLoginPage(driver);
        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        guidancePage = settingsPage.save(ExplorePage.class)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab();

        assertThat(guidancePage.getGuidanceCell("Failed GCDs", "Count"), is(equalTo("3")));
        assertThat(guidancePage.getGuidanceCell("Not Supported GCDs", "Count"), is(equalTo("1")));

        evaluatePage = guidancePage.closePanel();
        assertThat(evaluatePage.isDFMRiskIcon("dtc-critical-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("Critical"), is(true));
    }
}
