package com.evaluate.materialutilization;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.materialprocess.PartNestingPage;
import com.apriori.pageobjects.pages.help.HelpDocPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class BracketBasicTests extends TestBase {
    private PartNestingPage partNestingPage;
    private EvaluatePage evaluatePage;
    private HelpDocPage helpDocPage;

    public BracketBasicTests() {
        super();
    }

    @Test
    @Description("Create PO & methods for nesting tab where required")
    public void testLogin() {
        String componentName = "BRACKET_BASIC";
        String scenarioName = "Postman_f092d995-3354-4355-bc64-96cdf8699b73";
        String presetFilter = "All";

        UserCredentials currentUser = UserUtil.getUser();
        CidAppLoginPage loginPage = new CidAppLoginPage(driver);
        partNestingPage = loginPage
            .login(currentUser)
            .clickSearch(componentName)
            .selectFilter(presetFilter)
            .openScenario(componentName, scenarioName)
            .openMaterialProcess()
            .openPartNestingTab();

        assertThat(partNestingPage.getNestingInfo("Selected Sheet"), is(equalTo("4.00000mm x 1,250.00000mm x 2,500.00000mm")));
        assertThat(partNestingPage.getNestingInfo("Blank Size"), is(equalTo("470.78230mm x 400.00170mm")));
        assertThat(partNestingPage.getNestingInfo("Parts Per Sheet"), is(equalTo("15")));
        assertThat(partNestingPage.getNestingInfo("Part Nesting"), is(equalTo("True-Part Shape Nesting")));
        assertThat(partNestingPage.isUtilizationModeInfo("Utilization Mode"), is(true));
        assertThat(partNestingPage.getStockWidthInfo(), is(equalTo("1,250.00000mm")));
        assertThat(partNestingPage.getStockLengthInfo(), is(equalTo("2,500.00000mm")));

        partNestingPage = partNestingPage.closePanel()
            .openMaterialProcess()
            .openPartNestingTab();
        assertThat(partNestingPage.getNestingInfo("Parts Per Sheet"), is(equalTo("15")));

        helpDocPage = partNestingPage.openHelp();
        assertThat(helpDocPage.getChildPageTitle(), containsString("Process Details"));
    }
}
