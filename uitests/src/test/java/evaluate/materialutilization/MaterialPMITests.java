package evaluate.materialutilization;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.pageobjects.pages.settings.SettingsPage;
import com.apriori.utils.AfterTestUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.CostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

import java.io.File;

public class MaterialPMITests extends TestBase {

    private CIDLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private UserCredentials currentUser;

    private File resourceFile;
    private File cadResourceFile;

    public MaterialPMITests() {
        super();
    }

    @After
    public void resetAllSettings() {
        if (currentUser != null) {
            new AfterTestUtil().resetAllSettings(currentUser.getUsername());
        }
    }

    @Test
    @TestRail(testCaseId = {"901"})
    @Description("Test setting a default material and ensure parts are costed in that material by default")
    public void materialTestProductionDefault() {

        resourceFile = new FileResourceUtil().getResourceFile("bracket_basic.prt");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        loginPage.login(currentUser)
            .openSettings()
            .openProdDefaultTab()
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_BRAZIL.getVpe())
            .selectMaterialCatalog(VPEEnum.APRIORI_BRAZIL.getVpe())
            .selectMaterial("Aluminum, Stock, ANSI 6061");
        new SettingsPage(driver).save(ExplorePage.class);

        new ExplorePage(driver).uploadFile(new Util().getScenarioName(), resourceFile)
            .costScenario(3);

        evaluatePage = new EvaluatePage(driver);
        assertThat(evaluatePage.isMaterialInfo("Aluminum, Stock, ANSI 6061"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"901"})
    @Description("Test to check file upload")
    public void testCadFile() {

        resourceFile = new FileResourceUtil().getResourceFile("bracket_basic.prt");
        cadResourceFile = new FileResourceUtil().getPathResourceFile("bracket_basic.prt");

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        loginPage.login(currentUser)
            .uploadFile(new Util().getScenarioName(), resourceFile)
            .uploadCadFile(cadResourceFile);

        assertThat(new EvaluatePage(driver).getCostLabel(CostingLabelEnum.TRANSLATING.getCostingText()), is(true));
        assertThat(new EvaluatePage(driver).getCostLabel(CostingLabelEnum.READY_TO_COST.getCostingText()), is(true));
    }
}
