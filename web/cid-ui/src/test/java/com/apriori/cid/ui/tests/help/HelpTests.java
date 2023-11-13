package com.apriori.cid.ui.tests.help;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.IGNORE;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.help.HelpDocPage;
import com.apriori.cid.ui.pageobjects.help.HelpPage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

public class HelpTests extends TestBaseUI {

    UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private HelpPage helpPage;
    private HelpDocPage helpDocPage;
    private File resourceFile;

    @Test
    @Tag(SMOKE)
    @TestRail(id = {6370, 6691, 6693})
    @Description("Be able to access help information in the application header")
    public void onlineHelpTest() {
        loginPage = new CidAppLoginPage(driver);
        helpPage = loginPage.login(UserUtil.getUser())
            .goToHelp()
            .clickUserGuide();

        assertThat(helpPage.getChildPageTitle(), is(equalTo("aP Design\nUser Guide")));
    }

    @Test
    @Disabled("Currently no help button for inputs")
    @Tag(IGNORE)
    @TestRail(id = {6371})
    @Description("Have links to a detailed help page in relevant areas of the UI")
    public void linksToHelpPage() {
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

        assertThat(helpDocPage.getChildPageTitle(), containsString("aP Design"));

        helpDocPage.closeHelpPage(EvaluatePage.class)
            .openDesignGuidance()
            .openHelp();

        assertThat(helpDocPage.getChildPageTitle(), containsString("Examine Design Issues"));

        helpDocPage.closeHelpPage(EvaluatePage.class)
            .openCostDetails()
            .openHelp();

        assertThat(helpDocPage.getChildPageTitle(), is(equalTo("Explore the Cost Results")));
    }

    @Test
    @TestRail(id = {6547})
    @Description("Have links to a detailed help page in relevant areas of the UI")
    public void assemblyHelp() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        String componentName = "Hinge assembly";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDASM");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        helpDocPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .openComponents()
            .selectTableView()
            .openHelp();

        assertThat(helpDocPage.getChildPageTitle(), containsString("Working in the Assembly Explorer"));
    }
}