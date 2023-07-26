package com.login;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.TestBaseUI;
import com.apriori.pageobjects.header.ReportsPageHeader;
import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.OnPremTest;
import testsuites.suiteinterface.ReportsTest;
import utils.Constants;

public class LoginTests extends TestBaseUI {

    private ReportsPageHeader reportsPageHeader;
    private ReportsLoginPage loginPage;

    public LoginTests() {
        super();
    }

    @Test
    @Category( {ReportsTest.class, OnPremTest.class})
    @TestRail(id = {2695})
    @Description("Successful login to CI Report")
    public void testLogin() {
        reportsPageHeader = new ReportsLoginPage(driver)
            .login();

        assertThat(reportsPageHeader.isCreateDashboardsButtonDisplayed(), is(true));
    }

    @Test
    @Category( {ReportsTest.class, OnPremTest.class})
    @TestRail(id = {2696})
    @Description("Failed login to CI Report, wrong password")
    public void testFailedLogin() {
        loginPage = new ReportsLoginPage(driver)
            .failedLogin(UserUtil.getUserOnPrem(), "fakePassword");

        assertThat(loginPage.getLoginMessage(), is(equalTo(Constants.FAILED_LOGIN_MESSAGE_ONPREM)));
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(id = {2697})
    @Description("Forgotten password functionality")
    public void testForgotPassword() {
        loginPage = new ReportsLoginPage(driver)
            .clickForgotPassword()
            .submitEmail("fakeEmail@apriori.comg");

        assertThat(loginPage.getLoginMessage(), is(equalTo(Constants.FORGOT_PWD_MSG.toUpperCase())));
    }

    @Test
    @Category( {ReportsTest.class, OnPremTest.class})
    @TestRail(id = {2698})
    @Description("Empty email/password field message displayed")
    public void emptyFieldsMessage() {
        loginPage = new ReportsLoginPage(driver)
            .failedLoginEmptyFields();

        assertThat(loginPage.getLoginMessage(), is(equalTo(Constants.FAILED_LOGIN_MESSAGE_ONPREM)));
    }

    @Test
    @Category( {ReportsTest.class, OnPremTest.class})
    @TestRail(id = {2699})
    @Description("Invalid email address, wrong format")
    public void testInvalidEmail() {
        loginPage = new ReportsLoginPage(driver)
            .invalidEmailFailedLogin("a@b", "fakePassword");

        assertThat(loginPage.getInvalidEmailMessage(), is(equalTo(Constants.FAILED_LOGIN_MESSAGE_ONPREM)));
    }
}