package evaluate.designguidance.dtc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.analysis.PropertiesDialogPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.GeometryPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.GuidancePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.tolerances.TolerancePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.pageobjects.pages.settings.SettingsPage;
import com.apriori.pageobjects.pages.settings.ToleranceSettingsPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.UsersEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

public class SheetMetalDTC extends TestBase {

    private LoginPage loginPage;
    private GuidancePage guidancePage;
    private ToleranceSettingsPage toleranceSettingsPage;
    private SettingsPage settingsPage;
    private EvaluatePage evaluatePage;
    private GeometryPage geometryPage;
    private PropertiesDialogPage propertiesDialogPage;
    private TolerancePage tolerancePage;

    public SheetMetalDTC() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"1839", "1842", "1843"})
    @Description("Testing DTC Sheet Metal")
    public void sheetMetalDTCHoles() {
        loginPage = new LoginPage(driver);
        toleranceSettingsPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        guidancePage = settingsPage.save(ExplorePage.class)
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("SheMetDTC.SLDPRT"))
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeAndGCD("Hole Issue", "Diameter", "SimpleHole:2");

        assertThat(guidancePage.getGuidanceMessage(), containsString("Hole is too small to be cut with Plasma Cutting"));

        guidancePage.selectIssueTypeAndGCD("Hole Issue", "Property", "SimpleHole:4");
        assertThat(guidancePage.getGuidanceMessage(), containsString("This is a blind hole and cannot be cut with Plasma Cutting"));

        guidancePage.selectIssueTypeAndGCD("Machined GCDs", "Facing", "PlanarFace:13");
        assertThat(guidancePage.getGuidanceCell("Facing", "Count"), is(equalTo("1")));

        guidancePage.selectIssueTypeAndGCD("Machined GCDs", "Center Drilling / Pecking", "SimpleHole:2");
        assertThat(guidancePage.getGuidanceCell("Center Drilling / Pecking", "Count"), is(equalTo("1")));

        guidancePage.selectIssueTypeAndGCD("Machined GCDs", "Center Drilling / Drilling", "SimpleHole:3");
        assertThat(guidancePage.getGuidanceCell("Center Drilling / Drilling", "Count"), is(equalTo("3")));
    }

}
