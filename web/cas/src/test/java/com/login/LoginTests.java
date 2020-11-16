package com.login;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.customeradmin.CustomerAdminPage;
import com.apriori.login.CasLoginPage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

public class LoginTests extends TestBase {

    private static String loginPageErrorMessage = "We're sorry, something went wrong when attempting to log in.";
    private CasLoginPage loginPage;
    private CustomerAdminPage casPage;

    public LoginTests() {
        super();
    }

    @Test
    @Description("Test successful login")
    public void testLogin() {

        loginPage = new CasLoginPage(driver);
        casPage = loginPage.login(UserUtil.getUser());

        assertThat(casPage.isNewCustomerButtonPresent(), is(true));
    }

    @Test
    @Description("Test unsuccessful login with correct email, incorrect password")
    public void testIncorrectPwd() {

        loginPage = new CasLoginPage(driver);
        loginPage = loginPage.failedLoginAs(UserUtil.getUser().getUsername(), "fakePassword");

        assertThat(loginPageErrorMessage.toUpperCase(), is(equalTo(loginPage.getLoginErrorMessage())));
    }

    @Test
    @Description("Test unsuccessful login with incorrect email, correct password")
    public void testIncorrectEmail() {

        loginPage = new CasLoginPage(driver);
        loginPage = loginPage.failedLoginAs(new GenerateStringUtil().generateEmail(), UserUtil.getUser().getPassword());

        assertThat(loginPageErrorMessage.toUpperCase(), is(equalTo(loginPage.getLoginErrorMessage())));
    }

    @Test
    @Description("Test unsuccessful login with incorrect email, and incorrect password")
    public void testIncorrectEmailPassword() {

        loginPage = new CasLoginPage(driver);
        loginPage = loginPage.failedLoginAs(new GenerateStringUtil().generateEmail(), "fakePassword");

        assertThat(loginPageErrorMessage.toUpperCase(), is(equalTo(loginPage.getLoginErrorMessage())));
    }

    @Test
    @Description("Validate Login Dialog")
    public void loginDialog() {

        loginPage = new CasLoginPage(driver);

        assertThat(loginPage.getMarketingText(), containsString("COST INSIGHT GENERATE:\n" +
            "SOLUTION FOR A NEW NORMAL\n" +
            "Proactively notify your team of manufacturability issues and enable them to optimize their designs faster."));
        assertThat(loginPage.isLogoDisplayed(), is(true));
    }
}