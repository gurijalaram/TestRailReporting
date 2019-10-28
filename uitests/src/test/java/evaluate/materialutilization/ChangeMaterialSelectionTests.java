package evaluate.materialutilization;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.UsersEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

public class ChangeMaterialSelectionTests extends TestBase {

    private LoginPage loginPage;
    private EvaluatePage evaluatePage;

    public ChangeMaterialSelectionTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"864", "866", "867"})
    @Description("Test making changes to the Material for Additive Manufacturing, the change is respected and the scenario can be cost")
    public void changeMaterialSelectionTestAdditive() {
        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("BasicScenario_Additive.prt.1"))
            .selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Aluminum AlSi10Mg"), is(true));

            new EvaluatePage(driver).openMaterialCompositionTable()
            .selectMaterialComposition("Duraform ProX GF")
            .apply()
            .costScenario();

        assertThat(evaluatePage.isMaterialInfo("Duraform ProX GF"), is(true));
    }
}