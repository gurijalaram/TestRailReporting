package com.help;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.navtoolbars.help.AboutUsPage;
import com.apriori.pageobjects.navtoolbars.help.ZendeskSignInPage;
import com.apriori.pageobjects.pages.login.EdcAppLoginPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

public class HelpTests extends TestBase {

    private EdcAppLoginPage loginPage;
    private AboutUsPage aboutUsPage;
    private ZendeskSignInPage zendeskSignInPage;
    private UserCredentials currentUser;

    public HelpTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"2272"})
    @Description("Login page Help link takes user to Zendesk login page")
    public void onlineHelpTest() {
        currentUser = UserUtil.getUser();

        loginPage = new EdcAppLoginPage(driver);
        zendeskSignInPage = loginPage.login(currentUser)
            .clickHelp();

        assertThat(zendeskSignInPage.getCurrentUrl(), containsString("https://ap-cloud-prd-01.us.auth0.com/login"));
    }

    @Test
    @TestRail(testCaseId = "8941")
    @Description("User can navigate to About Us pager")
    public void testAboutUs() {
        currentUser = UserUtil.getUser();

        loginPage = new EdcAppLoginPage(driver);
        aboutUsPage = loginPage.login(currentUser)
            .clickAbout()
            .agreeTermsAndCondition();

        assertThat(aboutUsPage.getAboutUsPageUrl(), containsString("https://www.apriori.com/about"));
    }
}

