package com.apriori.cir.ui.tests.login;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.CUSTOMER;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.ON_PREM;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REPORTS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.StringContains.containsString;

import com.apriori.cir.ui.pageobjects.header.ReportsPageHeader;
import com.apriori.cir.ui.pageobjects.login.ReportsLoginPage;
import com.apriori.cir.ui.utils.Constants;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.properties.PropertiesContext;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

public class LoginTests extends TestBaseUI {
    private Boolean isEnvOnPrem = PropertiesContext.get("${env}").equals("onprem");

    private ReportsPageHeader reportsPageHeader;
    private ReportsLoginPage loginPage;

    public LoginTests() {
        super();
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM),
        @Tag(CUSTOMER)
    })
    @TestRail(id = {2695})
    @Description("Successful login to CI Report")
    public void testLogin() {
        reportsPageHeader = new ReportsLoginPage(driver)
            .login();

        assertThat(reportsPageHeader.isCreateDashboardsButtonDisplayed(), is(true));
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {2696})
    @Description("Failed login to CI Report, wrong password")
    public void testFailedLogin() {
        loginPage = new ReportsLoginPage(driver)
            .failedLogin(UserUtil.getUserOnPrem(), "fakePassword");

        String assertValueToUse = isEnvOnPrem ? Constants.FAILED_LOGIN_MESSAGE_ONPREM : Constants.FAILED_LOGIN_MESSAGE_CLOUD;
        String actualErrorMessage = isEnvOnPrem ? loginPage.getInvalidEmailMessage() : loginPage.getInvalidPasswordMessage();
        assertThat(actualErrorMessage, is(containsString(assertValueToUse)));
    }

    @Test
    @Tag(REPORTS)
    @TestRail(id = {2697})
    @Description("Forgotten password functionality")
    public void testForgotPassword() {
        loginPage = new ReportsLoginPage(driver)
            .clickForgotPassword()
            .submitEmail("fakeEmail@apriori.comg");

        assertThat(loginPage.getBlankFieldsErrorMessage("email"), is(equalTo(Constants.FORGOT_PWD_MSG.toUpperCase())));
    }

    @Test
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {2698})
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
    @Tags({@Tag(REPORTS),
        @Tag(ON_PREM)})
    @TestRail(id = {2699})
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