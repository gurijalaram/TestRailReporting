package help;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.CostDetailsPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.DesignGuidancePage;
import com.apriori.pageobjects.pages.evaluate.inputs.MoreInputsPage;
import com.apriori.pageobjects.pages.evaluate.materialutilization.MaterialPage;
import com.apriori.pageobjects.pages.evaluate.process.ProcessRoutingPage;
import com.apriori.pageobjects.pages.help.HelpPage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

public class HelpTests extends TestBase {

    private CIDLoginPage loginPage;
    private HelpPage helpPage;
    private MoreInputsPage moreInputsPage;
    private MaterialPage materialPage;
    private DesignGuidancePage designGuidancePage;
    private ProcessRoutingPage processRoutingPage;
    private CostDetailsPage costDetailsPage;

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"263"})
    @Description("Be able to access help information in the application header")
    public void onlineHelpTest() {
        loginPage = new CIDLoginPage(driver);
        helpPage = loginPage.login(UserUtil.getUser())
            .openHelpMenu()
            .clickOnlineHelp();

        assertThat(helpPage.getChildPageTitle(), containsString("Cost Insight Design:User Guide"));
    }

    @Test
    @TestRail(testCaseId = {"264"})
    @Description("Have links to a detailed help pages in relevant areas of the UI")
    public void moreInputsHelp() {
        loginPage = new CIDLoginPage(driver);
        moreInputsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("PowderMetalShaft.stp"))
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .costScenario()
            .openMoreInputs()
            .clickHelp();

        assertThat(moreInputsPage.getChildPageTitle(), containsString("More Inputs"));
    }

    @Test
    @TestRail(testCaseId = {"264"})
    @Description("Have links to a detailed help pages in relevant areas of the UI")
    public void materialUtilHelp() {
        loginPage = new CIDLoginPage(driver);
        materialPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("PowderMetalShaft.stp"))
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .costScenario()
            .openMaterialComposition()
            .clickHelp();

        assertThat(materialPage.getChildPageTitle(), containsString("Material & Utilization Details"));
    }

    @Test
    @TestRail(testCaseId = {"264"})
    @Description("Have links to a detailed help pages in relevant areas of the UI")
    public void designGuidanceHelp() {
        loginPage = new CIDLoginPage(driver);
        designGuidancePage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("PowderMetalShaft.stp"))
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .clickHelp();

        assertThat(designGuidancePage.getChildPageTitle(), containsString("Design Guidance Details"));
    }

    @Test
    @TestRail(testCaseId = {"264"})
    @Description("Have links to a detailed help pages in relevant areas of the UI")
    public void processDetailsHelp() {
        loginPage = new CIDLoginPage(driver);
        processRoutingPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("PowderMetalShaft.stp"))
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .costScenario()
            .openProcessDetails()
            .clickHelp();

        assertThat(processRoutingPage.getChildPageTitle(), containsString("Process Details"));
    }

    @Test
    @TestRail(testCaseId = {"264"})
    @Description("Have links to a detailed help pages in relevant areas of the UI")
    public void costResultsHelp() {
        loginPage = new CIDLoginPage(driver);
        costDetailsPage = loginPage.login(UserUtil.getUser())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("PowderMetalShaft.stp"))
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .costScenario()
            .openCostDetails()
            .clickHelp();

        assertThat(costDetailsPage.getChildPageTitle(), containsString("Cost Details"));
    }
}
