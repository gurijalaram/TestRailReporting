package com.login;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.header.ReportsPageHeader;
import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.OnPremTest;
import testsuites.suiteinterface.ReportsSmokeTest;
import testsuites.suiteinterface.ReportsTest;
import utils.Constants;

public class LoginTests extends TestBase {

    private ReportsPageHeader reportsPageHeader;
    private ReportsLoginPage loginPage;

    public LoginTests() {
        super();
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class, OnPremTest.class})
    @TestRail(testCaseId = "2695")
    @Description("Successful login to CI Report")
    public void testLogin() {
        reportsPageHeader = new ReportsLoginPage(driver)
            .login();

        assertThat(reportsPageHeader.isCreateDashboardsButtonDisplayed(), is(true));
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class, OnPremTest.class})
    @TestRail(testCaseId = "2696")
    @Description("Failed login to CI Report, wrong password")
    public void testFailedLogin() {
        loginPage = new ReportsLoginPage(driver)
                .failedLogin(UserUtil.getUser().getUsername(), "fakePassword");

        assertThat(loginPage.getLoginMessage(), is(equalTo(Constants.FAILED_LOGIN_MESSAGE)));
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class})
    @TestRail(testCaseId = "2697")
    @Description("Forgotten password functionality")
    public void testForgotPassword() {
        loginPage = new ReportsLoginPage(driver)
            .clickForgotPassword()
            .submitEmail("fakeEmail@apriori.com");

        String forgotPwdMsg = Constants.environment.equals("cir-qa")
                ? Constants.FORGOT_PWD_MSG_QA_ENV : Constants.FORGOT_PWD_MSG_STAGING_ENV;

        assertThat(loginPage.getLoginMessage(), is(equalTo(forgotPwdMsg)));
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class, OnPremTest.class})
    @TestRail(testCaseId = "2698")
    @Description("Empty email/password field message displayed")
    public void emptyFieldsMessage() {
        loginPage = new ReportsLoginPage(driver)
            .failedLogin("", "");

        assertThat(loginPage.getInputErrorMessagesLocalInstall(), is(equalTo(Constants.FAILED_LOGIN_MESSAGE)));
    }

    @Test
    @Category({ReportsTest.class, ReportsSmokeTest.class, OnPremTest.class})
    @TestRail(testCaseId = "2699")
    @Description("Invalid email address, wrong format")
    public void testInvalidEmail() {
        loginPage = new ReportsLoginPage(driver)
            .failedLogin("a@b", "fakePassword");

        assertThat(loginPage.getLoginMessage(), is(equalTo(Constants.FAILED_LOGIN_MESSAGE)));
    }
}