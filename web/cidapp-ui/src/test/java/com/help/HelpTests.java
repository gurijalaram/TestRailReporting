package com.help;

import static com.apriori.utils.enums.ProcessGroupEnum.PLASTIC_MOLDING;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.help.HelpDocPage;
import com.apriori.pageobjects.pages.help.HelpPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.IgnoreTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class HelpTests extends TestBase {

    UserCredentials currentUser;
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
            .clickUserGuide();

        assertThat(helpPage.getChildPageTitle(), containsString("Cost Insight DesignUser Guide"));
    }

    @Test
    @Ignore("Currently no help button for inputs")
    @Category(IgnoreTests.class)
    @TestRail(testCaseId = {"264"})
    @Description("Have links to a detailed help pages in relevant areas of the UI")
    public void moreInputsHelp() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        String componentName = "PowderMetalShaft";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        helpDocPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
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

        String componentName = "PowderMetalShaft";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        helpDocPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .openMaterialProcess()
            .openHelp();

        assertThat(helpDocPage.getChildPageTitle(), containsString("Examine Material and Process Inputs and Results"));
    }

    @Test
    @TestRail(testCaseId = {"264"})
    @Description("Have links to a detailed help pages in relevant areas of the UI")
    public void designGuidanceHelp() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        String componentName = "PowderMetalShaft";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        helpDocPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(PLASTIC_MOLDING)
            .costScenario()
            .openDesignGuidance()
            .openHelp();

        assertThat(helpDocPage.getChildPageTitle(), containsString("Examine the Design Guidance Results"));
    }

    @Test
    @TestRail(testCaseId = {"264"})
    @Description("Have links to a detailed help pages in relevant areas of the UI")
    public void processDetailsHelp() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        String componentName = "PowderMetalShaft";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        helpDocPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .openMaterialProcess()
            .openHelp();

        assertThat(helpDocPage.getChildPageTitle(), containsString("Process Inputs and Results"));
    }

    @Test
    @TestRail(testCaseId = {"264"})
    @Description("Have links to a detailed help pages in relevant areas of the UI")
    public void costResultsHelp() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        String componentName = "PowderMetalShaft";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        helpDocPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .openCostDetails()
            .openHelp();

        assertThat(helpDocPage.getChildPageTitle(), containsString("Cost Results Details"));
    }
}