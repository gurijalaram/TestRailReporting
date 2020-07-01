package help;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.help.HelpDocPage;
import com.apriori.pageobjects.pages.help.HelpPage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class HelpTests extends TestBase {

    private CIDLoginPage loginPage;
    private HelpPage helpPage;
    private HelpDocPage helpDocPage;

    private File resourceFile;

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

        resourceFile = new FileResourceUtil().getResourceFile("PowderMetalShaft.stp");

        loginPage = new CIDLoginPage(driver);
        helpDocPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .costScenario()
            .openMoreInputs()
            .clickHelp();

        assertThat(helpDocPage.getChildPageTitle(), containsString("More Inputs"));
    }

    @Test
    @TestRail(testCaseId = {"264"})
    @Description("Have links to a detailed help pages in relevant areas of the UI")
    public void materialUtilHelp() {

        resourceFile = new FileResourceUtil().getResourceFile("PowderMetalShaft.stp");

        loginPage = new CIDLoginPage(driver);
        helpDocPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .costScenario()
            .openMaterialUtilization()
            .clickHelp();

        assertThat(helpDocPage.getChildPageTitle(), containsString("Material & Utilization Details"));
    }

    @Test
    @TestRail(testCaseId = {"264"})
    @Description("Have links to a detailed help pages in relevant areas of the UI")
    public void designGuidanceHelp() {

        resourceFile = new FileResourceUtil().getResourceFile("PowderMetalShaft.stp");

        loginPage = new CIDLoginPage(driver);
        helpDocPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .clickHelp();

        assertThat(helpDocPage.getChildPageTitle(), containsString("Design Guidance Details"));
    }

    @Test
    @TestRail(testCaseId = {"264"})
    @Description("Have links to a detailed help pages in relevant areas of the UI")
    public void processDetailsHelp() {

        resourceFile = new FileResourceUtil().getResourceFile("PowderMetalShaft.stp");

        loginPage = new CIDLoginPage(driver);
        helpDocPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .costScenario()
            .openProcessDetails()
            .clickHelp();

        assertThat(helpDocPage.getChildPageTitle(), containsString("Process Details"));
    }

    @Test
    @TestRail(testCaseId = {"264"})
    @Description("Have links to a detailed help pages in relevant areas of the UI")
    public void costResultsHelp() {

        resourceFile = new FileResourceUtil().getResourceFile("PowderMetalShaft.stp");

        loginPage = new CIDLoginPage(driver);
        helpDocPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile)
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .costScenario()
            .openCostDetails()
            .clickHelp();

        assertThat(helpDocPage.getChildPageTitle(), containsString("Cost Details"));
    }
}