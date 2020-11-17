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

public class LoginTests extends TestBase {

    private ReportsPageHeader reportsPageHeader;
    private ReportsLoginPage loginPage;

    public LoginTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "2695")
    @Description("Successful login to CI Report")
    public void testLogin() {
        reportsPageHeader = new ReportsLoginPage(driver)
            .login();

        assertThat(reportsPageHeader.isCreateDashboardsButtonDisplayed(), is(true));
    }

    @Test
    @TestRail(testCaseId = "2696")
    @Description("Failed login to CI Report, wrong password")
    public void testFailedLogin() {
        loginPage = new ReportsLoginPage(driver)
            .failedLogin(UserUtil.getUser().getUsername(), "fakePassword");

        assertThat(loginPage.getLoginMessage(), is(equalTo(Constants.FAILED_LOGIN_MESSAGE.toUpperCase())));
    }

    @Test
    @TestRail(testCaseId = "2697")
    @Description("Forgotten password functionality")
    public void testForgotPassword() {
        loginPage = new ReportsLoginPage(driver)
            .clickForgotPassword()
            .submitEmail("fakeEmail@apriori.com");

        assertThat(loginPage.getLoginMessage(), is(equalTo(Constants.FORGOT_PWD_MESSAGE.toUpperCase())));
    }

    @Test
    @TestRail(testCaseId = "2698")
    @Description("Empty email/password field message displayed")
    public void testEmptyFieldsMessage() {
        loginPage = new ReportsLoginPage(driver)
            .failedLogin("", "");

        assertThat(loginPage.getInputErrorMsg(), is(equalTo(Constants.EMPTY_FIELDS_MESSAGE)));
    }

    @Test
    @TestRail(testCaseId = "2699")
    @Description("Invalid email address, wrong format")
    public void testInvalidEmail() {
        loginPage = new ReportsLoginPage(driver)
            .failedLogin("a@b", "fakePassword");

        assertThat(loginPage.getInputErrorMsg(), is(equalTo(Constants.INVALID_ERROR_MESSAGE)));
    }
}