package com.apriori.cis.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.login.CisLoginPage;
import com.apriori.pageobjects.pages.login.ExplorePage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import org.junit.Test;

public class LoginTests extends TestBase {

    public LoginTests() {
        super();
    }

    private static String loginPageErrorMessage = "We're sorry, something went wrong when attempting to log in.";

    private ExplorePage explorePage;
    private CisLoginPage loginPage;

    @Test
    public void testLogin() {
        loginPage = new CisLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser());
    }

    @Test
    public void testIncorrectEmailAndPassword() {
        loginPage = new CisLoginPage(driver);
        loginPage.failedLoginAs(new GenerateStringUtil().generateEmail(), "fakePassword");

        assertThat(loginPageErrorMessage.toUpperCase(), is(loginPage.getLoginErrorMessage()));
    }
}



