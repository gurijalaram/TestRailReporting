package com.login;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.login.CasLoginPage;
import com.apriori.testsuites.categories.SanityTest;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;


public class LoginTests extends TestBase {

    private static final String loginPageErrorMessage = "We're sorry, something went wrong when attempting to log in.";
    private CasLoginPage loginPage;

    public LoginTests() {
        super();
    }

    @Before
    public void setup() {
        loginPage = new CasLoginPage(driver);
    }

    @Test
    @Category(SanityTest.class)
    @Description("Test successful login")
    public void testLogin() {

        loginPage.login(UserUtil.getUser());
        // If we get here, we passed.
    }

    @Test
    @Description("Test unsuccessful login with correct email, incorrect password")
    public void testIncorrectPwd() {

        loginPage = loginPage.failedLoginAs(UserUtil.getUser().getEmail(), "fakePassword");

        assertThat(loginPageErrorMessage.toUpperCase(), is(equalTo(loginPage.getLoginErrorMessage())));
    }

    @Test
    @Description("Test unsuccessful login with incorrect email, correct password")
    public void testIncorrectEmail() {

        loginPage = loginPage.failedLoginAs(new GenerateStringUtil().generateEmail(), UserUtil.getUser().getPassword());

        assertThat(loginPageErrorMessage.toUpperCase(), is(equalTo(loginPage.getLoginErrorMessage())));
    }

    @Test
    @Description("Test unsuccessful login with incorrect email, and incorrect password")
    public void testIncorrectEmailPassword() {

        loginPage = loginPage.failedLoginAs(new GenerateStringUtil().generateEmail(), "fakePassword");

        assertThat(loginPageErrorMessage.toUpperCase(), is(equalTo(loginPage.getLoginErrorMessage())));
    }
}
