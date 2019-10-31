package evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.GuidancePage;
import com.apriori.pageobjects.pages.evaluate.process.ProcessOptionsPage;
import com.apriori.pageobjects.pages.evaluate.process.ProcessRoutingPage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.pageobjects.pages.settings.SettingsPage;
import com.apriori.pageobjects.pages.settings.ToleranceSettingsPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

public class PsoEditTests extends TestBase {

    private LoginPage loginPage;
    private GuidancePage guidancePage;
    private ToleranceSettingsPage toleranceSettingsPage;
    private SettingsPage settingsPage;
    private EvaluatePage evaluatePage;
    private ProcessOptionsPage processOptionsPage;
    private ProcessRoutingPage processRoutingPage;

    @Test
    @TestRail(testCaseId = {"761"})
    @Description("Plastic Moulding- Validate the user can edit the number of cavities")
    public void plasticMouldPSO() {
        loginPage = new LoginPage(driver);
        processOptionsPage = loginPage.login(UserUtil.getUser().getUsername(), UserUtil.getUser().getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Plastic moulded cap DFM.CATPart"))
            .selectProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .costScenario(3)
            .openProcessDetails()
            .selectProcessChart("Injection Molding")
            .selectOptions()
            .selectDefinedValueDropdown("8")
            .selectOverrideNominalButton()
            .setOverride("0.4")
            .selectAddColorantButton()
            .selectMaterialDefinedButton()
            .setMaterialRegrindInput("0.3");

        processRoutingPage = new ProcessRoutingPage(driver);
        processRoutingPage.closeProcessPanel();

        evaluatePage = new EvaluatePage(driver);
        processOptionsPage = evaluatePage.costScenario()
            .openProcessDetails()
            .selectProcessChart("Injection Molding")
            .selectOptions();

        assertThat(processOptionsPage.isNominalOverride("0.4"), is(true));
        assertThat(processOptionsPage.isAddColorantSelected("checked"), is("true"));
        assertThat(processOptionsPage.isMaterialRegrind("0.3"), is(true));
    }
}
