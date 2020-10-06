package evaluate.materialutilization;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.apibase.utils.AfterTestUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Test;
import pageobjects.pages.evaluate.EvaluatePage;
import pageobjects.pages.explore.ExplorePage;
import pageobjects.pages.login.CidLoginPage;
import pageobjects.pages.settings.SettingsPage;

import java.io.File;

public class MaterialPMITests extends TestBase {

    private CidLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private UserCredentials currentUser;

    private File resourceFile;


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
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic.prt");

        loginPage = new CidLoginPage(driver);
        currentUser = UserUtil.getUser();

        loginPage.login(currentUser)
                .openSettings()
                .openProdDefaultTab()
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .selectVPE(VPEEnum.APRIORI_BRAZIL.getVpe())
                .selectMaterialCatalog(VPEEnum.APRIORI_BRAZIL.getVpe())
                .selectMaterial("Aluminum, Stock, ANSI 6061");
        new SettingsPage(driver).save(ExplorePage.class);

        evaluatePage = new ExplorePage(driver).uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
                .costScenario(3);

        assertThat(evaluatePage.getMaterialInfo(), is("Aluminum, Stock, ANSI 6061"));
    }
}
