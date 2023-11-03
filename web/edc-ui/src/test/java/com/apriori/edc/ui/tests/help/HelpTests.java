package com.apriori.edc.ui.tests.help;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import com.apriori.edc.ui.pageobjects.login.EdcAppLoginPage;
import com.apriori.edc.ui.pageobjects.navtoolbars.AboutUsPage;
import com.apriori.edc.ui.pageobjects.navtoolbars.ZendeskSignInPage;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;

public class HelpTests extends TestBaseUI {

    private EdcAppLoginPage loginPage;
    private AboutUsPage aboutUsPage;
    private ZendeskSignInPage zendeskSignInPage;
    private UserCredentials currentUser;

    public HelpTests() {
        super();
    }

    @Test
    @TestRail(id = 2272)
    @Description("Login page Help link takes user to Zendesk login page")
    public void onlineHelpTest() {
        currentUser = UserUtil.getUser();

        loginPage = new EdcAppLoginPage(driver);
        zendeskSignInPage = loginPage.login(currentUser)
            .clickHelp();

        assertThat(zendeskSignInPage.getCurrentUrl(), containsString("https://ap-cloud-prd-01.us.auth0.com/login"));
    }

    @Test
    @TestRail(id = 8941)
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

