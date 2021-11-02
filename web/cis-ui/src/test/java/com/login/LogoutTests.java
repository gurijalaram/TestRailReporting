package com.login;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.login.CisLoginPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

public class LogoutTests extends TestBase {

    public LogoutTests() {
        super();
    }

    private CisLoginPage loginPage;

    @Test
    @TestRail(testCaseId = "9445")
    @Description("Be able to log out from aPriori Web client")
    public void testLogout() {
        String pageTitle = "aPriori Single Sign-On";
        loginPage = new CisLoginPage(driver);
        loginPage = loginPage.login(UserUtil.getUser())
            .clickUserDropdown()
            .logout();

        assertThat(loginPage.verifyPageTitle(pageTitle), is(true));
    }
}
