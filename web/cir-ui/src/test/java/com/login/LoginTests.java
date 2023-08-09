package com.login;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.header.ReportsPageHeader;
import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.OnPremTest;
import testsuites.suiteinterface.ReportsTest;
import utils.Constants;

public class LoginTests extends TestBase {
    private Boolean isEnvOnPrem = PropertiesContext.get("${env}").equals("onprem");

    private ReportsPageHeader reportsPageHeader;
    private ReportsLoginPage loginPage;

    public LoginTests() {
        super();
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(testCaseId = {"2695"})
    @Description("Successful login to CI Report")
    public void testLogin() {
        reportsPageHeader = new ReportsLoginPage(driver)
            .login();

        assertThat(reportsPageHeader.isCreateDashboardsButtonDisplayed(), is(true));
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(testCaseId = {"2696"})
    @Description("Failed login to CI Report, wrong password")
    public void testFailedLogin() {
        loginPage = new ReportsLoginPage(driver)
            .failedLogin(UserUtil.getUserOnPrem(), "fakePassword");

        String assertValueToUse = isEnvOnPrem ? Constants.FAILED_LOGIN_MESSAGE_ONPREM : Constants.FAILED_LOGIN_MESSAGE_CLOUD;
        String actualErrorMessage = isEnvOnPrem ? loginPage.getInvalidEmailMessage() : loginPage.getInvalidPasswordMessage();
        assertThat(actualErrorMessage, is(containsString(assertValueToUse)));
    }

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"2697"})
    @Description("Forgotten password functionality")
    public void testForgotPassword() {
        loginPage = new ReportsLoginPage(driver)
            .clickForgotPassword()
            .submitEmail("fakeEmail@apriori.comg");

        assertThat(loginPage.getBlankFieldsErrorMessage("email"), is(equalTo(Constants.FORGOT_PWD_MSG.toUpperCase())));
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(testCaseId = {"2698"})
    @Description("Empty email/password field message displayed")
    public void emptyFieldsMessage() {
        loginPage = new ReportsLoginPage(driver)
            .failedLoginEmptyFields();

        String emailKeyword = "Email";
        String passwordKeyword = "Password";

        String assertValueToUse = isEnvOnPrem
            ? Constants.FAILED_LOGIN_MESSAGE_ONPREM
            : String.format(Constants.FAILED_LOGIN_EMPTY_FIELDS_CLOUD, emailKeyword);
        assertThat(loginPage.getBlankFieldsErrorMessage(emailKeyword.replace("E", "e")),
            is(containsString(assertValueToUse)));

        if (!isEnvOnPrem) {
            assertValueToUse = assertValueToUse.contains("Email")
                ? String.format(Constants.FAILED_LOGIN_EMPTY_FIELDS_CLOUD, passwordKeyword)
                : assertValueToUse;
            assertThat(loginPage.getBlankFieldsErrorMessage(passwordKeyword.replace("P", "p")),
                is(containsString(assertValueToUse)));
        }
    }

    @Test
    @Category({ReportsTest.class, OnPremTest.class})
    @TestRail(testCaseId = {"2699"})
    @Description("Invalid email address, wrong format")
    public void testInvalidEmail() {
        loginPage = new ReportsLoginPage(driver)
            .invalidEmailFailedLogin("a@b", "fakePassword");

        String assertValueToUse = isEnvOnPrem
            ? Constants.FAILED_LOGIN_MESSAGE_ONPREM
            : Constants.FAILED_LOGIN_INVALID_EMAIL_CLOUD;
        assertThat(loginPage.getInvalidEmailMessage(), is(containsString(assertValueToUse)));
    }
}