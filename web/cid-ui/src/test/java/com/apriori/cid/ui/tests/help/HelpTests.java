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
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.AssemblyRequestUtil;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class HelpTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private HelpPage helpPage;
    private HelpDocPage helpDocPage;

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
        ComponentInfoBuilder component = new ComponentRequestUtil().getComponent();

        loginPage = new CidAppLoginPage(driver);
        helpDocPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
        ComponentInfoBuilder assembly = new AssemblyRequestUtil().getAssembly();

        loginPage = new CidAppLoginPage(driver);
        helpDocPage = loginPage.login(assembly.getUser())
            .uploadComponentAndOpen(assembly)
            .openComponents()
            .selectTableView()
            .openHelp();

        assertThat(helpDocPage.getChildPageTitle(), containsString("Working in the Assembly Explorer"));
    }
}