package evaluate.designguidance.geometry;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.analysis.PropertiesDialogPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.GeometryPage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.UsersEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;

public class GeometryTests extends TestBase {

    private LoginPage loginPage;
    private EvaluatePage evaluatePage;
    private GeometryPage geometryPage;
    private PropertiesDialogPage propertiesDialogPage;

    public GeometryTests() {
        super();
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"1620", "1621"})
    @Description("Validate the user can open the Analysis Properties dialogue box for a specific GCD selected from the geometry tab")
    public void propertiesRouting() {
        loginPage = new LoginPage(driver);
        geometryPage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Case_001_-_Rockwell_2075-0243G.stp"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .openDesignGuidance()
            .openGeometryTab()
            .selectGCDAndGCDProperty("Volume", "Rings", "Ring:1");

        evaluatePage = new EvaluatePage(driver);
        propertiesDialogPage = evaluatePage.selectAnalysis()
            .selectProperties()
            .expandDropdown("Technique");
        assertThat(propertiesDialogPage.getProperties("Selected"), containsString("Plunging"));
        propertiesDialogPage.closeProperties();

        new GeometryPage(driver).selectGCDAndGCDProperty("Holes", "Ringed Holes", "RingedHole:1");
        evaluatePage = new EvaluatePage(driver);
        propertiesDialogPage = evaluatePage.selectAnalysis()
            .selectProperties()
            .expandDropdown("Technique");
        assertThat(propertiesDialogPage.getProperties("Selected"), containsString("Side Coring"));
    }
}
