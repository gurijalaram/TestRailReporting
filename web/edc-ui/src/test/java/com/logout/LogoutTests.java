package com.logout;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.login.EdcAppLoginPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

public class LogoutTests extends TestBase {

    private EdcAppLoginPage loginPage;
    private EdcAppLoginPage edcPage;

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"8922"})
    @Description("Test Logout successfully")
    public void testLogout() {
        String pageTitle = "aPriori Single Sign-On";
        loginPage = new EdcAppLoginPage(driver);
        edcPage = loginPage.login(UserUtil.getUser())
            .clickUserDropdown()
            .logout();

        assertThat(edcPage.verifyPageTitle(pageTitle), is(true));
    }
}
