package com.evaluate.bracketbasic;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.materialprocess.PartNestingPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class BracketBasicTests extends TestBase {
    public BracketBasicTests() {
        super();
    }

    @Test
    @Description("Create PO & methods for nesting tab where required")
    public void testLogin() {
        String componentName = "BRACKET_BASIC";
        String scenarioName = "David_Bracket";
        String presetFilter = "Private";

        UserCredentials currentUser = UserUtil.getUser();
        CidAppLoginPage loginPage = new CidAppLoginPage(driver);
        PartNestingPage partNestingPage = loginPage
            .login(currentUser)
            .clickSearch(componentName)
            .selectFilter(presetFilter)
            .openScenario(componentName, scenarioName)
            .openMaterialProcess()
            .openPartNestingTab()
            .SelectUtilizationModeDropDown("True-Part Shape Nesting");

        assertThat(partNestingPage.getNestingInfo("Selected Sheet"), is(equalTo("4.000mm x 1,250.000mm x 2,500.000mm")));
        assertThat(partNestingPage.getNestingInfo("Blank Size"), is(equalTo("470.782mm x 400.002mm")));
        assertThat(partNestingPage.getNestingInfo("Parts Per Sheet"), is(equalTo("15")));
        assertThat(partNestingPage.getNestingInfo("Part Nesting"), is(equalTo("True-Part Shape Nesting")));
        assertThat(partNestingPage.isUtUtilizationModeInfo("Utilization Mode"), is(true));
        assertThat(partNestingPage.getStockWidthInfo(), is(equalTo("1,250.000mm")));
        assertThat(partNestingPage.getStockLengthInfo(), is(equalTo("2,500.000mm")));
    }
}
