package com.help;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.help.HelpDocPage;
import com.apriori.pageobjects.pages.help.HelpPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
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

    private CidAppLoginPage loginPage;
    private HelpPage helpPage;
    private HelpDocPage helpDocPage;

    private File resourceFile;

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"263"})
    @Description("Be able to access help information in the application header")
    public void onlineHelpTest() {
        loginPage = new CidAppLoginPage(driver);
        helpPage = loginPage.login(UserUtil.getUser())
            .goToHelp()
            .clickOnlineHelp();

        assertThat(helpPage.getChildPageTitle(), containsString("Cost Insight Design:User Guide"));
    }

    @Test
    @TestRail(testCaseId = {"264"})
    @Description("Have links to a detailed help pages in relevant areas of the UI")
    public void moreInputsHelp() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum,"PowderMetalShaft.stp");

        loginPage = new CidAppLoginPage(driver);
        helpDocPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openInputDetails()
            .openHelp();

        assertThat(helpDocPage.getChildPageTitle(), containsString("More Inputs"));
    }

    @Test
    @TestRail(testCaseId = {"264"})
    @Description("Have links to a detailed help pages in relevant areas of the UI")
    public void materialUtilHelp() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum,"PowderMetalShaft.stp");

        loginPage = new CidAppLoginPage(driver);
        helpDocPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openMaterialUtilization()
            .openHelp();

        assertThat(helpDocPage.getChildPageTitle(), containsString("Material & Utilization Details"));
    }

    @Test
    @TestRail(testCaseId = {"264"})
    @Description("Have links to a detailed help pages in relevant areas of the UI")
    public void designGuidanceHelp() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum,"PowderMetalShaft.stp");

        loginPage = new CidAppLoginPage(driver);
        helpDocPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openHelp();

        assertThat(helpDocPage.getChildPageTitle(), containsString("Design Guidance Details"));
    }

    @Test
    @TestRail(testCaseId = {"264"})
    @Description("Have links to a detailed help pages in relevant areas of the UI")
    public void processDetailsHelp() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum,"PowderMetalShaft.stp");

        loginPage = new CidAppLoginPage(driver);
        helpDocPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openProcesses()
            .openHelp();

        assertThat(helpDocPage.getChildPageTitle(), containsString("Process Details"));
    }

    @Test
    @TestRail(testCaseId = {"264"})
    @Description("Have links to a detailed help pages in relevant areas of the UI")
    public void costResultsHelp() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum,"PowderMetalShaft.stp");

        loginPage = new CidAppLoginPage(driver);
        helpDocPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openCostDetails()
            .openHelp();

        assertThat(helpDocPage.getChildPageTitle(), containsString("Cost Details"));
    }
}