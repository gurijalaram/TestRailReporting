package evaluate.designguidance.geometry;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.analysis.PropertiesDialogPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.GeometryPage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class GeometryTests extends TestBase {

    private CIDLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private GeometryPage geometryPage;
    private PropertiesDialogPage propertiesDialogPage;

    private File resourceFile;

    public GeometryTests() {
        super();
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"1620", "1621", "1255", "1259", "1256"})
    @Description("Validate the user can open the Analysis Properties dialogue box for a specific GCD selected from the geometry tab")
    public void propertiesRouting() {

        resourceFile = new FileResourceUtil().getResourceFile("Case_001_-_Rockwell_2075-0243G.stp");

        loginPage = new CIDLoginPage(driver);
        geometryPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile)
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
        propertiesDialogPage.minimizeDropdown("Technique")
            .closeProperties();

        new GeometryPage(driver).selectGCDAndGCDProperty("Holes", "Ringed Holes", "RingedHole:1");
        evaluatePage = new EvaluatePage(driver);
        propertiesDialogPage = evaluatePage.selectAnalysis()
            .selectProperties()
            .expandDropdown("Technique");
        assertThat(propertiesDialogPage.getProperties("Selected"), containsString("Side Coring"));
    }
}
