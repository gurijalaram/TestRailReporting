package evaluate.designguidance.failures;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.designguidance.FailuresPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.pageobjects.pages.settings.SettingsPage;
import com.apriori.pageobjects.pages.settings.ToleranceSettingsPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.UsersEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

public class FailuresWarningsTests extends TestBase {

    private LoginPage loginPage;
    private SettingsPage settingsPage;
    private ToleranceSettingsPage toleranceSettingsPage;
    private FailuresPage failuresPage;

    @Test
    @Description("Ensure that 'Failures/ Warnings tab includes: - Issue type & count - Messaging")
    public void failedCostingCount() {
        loginPage = new LoginPage(driver);
        toleranceSettingsPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
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

        failuresPage.selectIssueTypeAndGCD("Not Supported GCDs", "Not Supported GCDs");
        assertThat(failuresPage.getUncostedMessage(), containsString("Multiple bodies exist in the model. Only the largest body is used and the remainder are ignored"));
    }
}
