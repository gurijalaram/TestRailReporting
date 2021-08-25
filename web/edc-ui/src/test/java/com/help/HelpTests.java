package com.help;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.navtoolbars.help.AboutUsPage;
import com.apriori.pageobjects.pages.login.EdcAppLoginPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;


public class HelpTests extends TestBase {

    private EdcAppLoginPage loginPage;
    private AboutUsPage aboutUsPage;

    public HelpTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"8941"})
    @Description("Be able to access help information in the application header")
    public void onlineHelpTest() {

        loginPage = new EdcAppLoginPage(driver);
        aboutUsPage = loginPage.login(UserUtil.getUser())
            .clickHelpDropdown()
            .selectAbout()
            .switchTab()
            .agreeTermsAndCondition();

        assertThat(aboutUsPage.getAboutUsPageUrl(), containsString("https://www.apriori.com/about-us"));
        assertThat(aboutUsPage.getAboutUsMetaTag(), containsString("Improving Your Profitability through Digital Manufacturing"));
    }
}

