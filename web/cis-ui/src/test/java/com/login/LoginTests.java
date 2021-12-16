package com.login;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.navtoolbars.ExploreTabToolbar;
import com.apriori.pageobjects.pages.login.CisLoginPage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

public class LoginTests extends TestBase {

    public LoginTests() {
        super();
    }

    private static String loginPageErrorMessage = "We're sorry, something went wrong when attempting to log in.";

    private CisLoginPage loginPage;
    private ExploreTabToolbar exploreTabToolbar;

    @Test
    @TestRail(testCaseId = "9432")
    @Description("Successfully login with valid user")
    public void testLogin() {
        loginPage = new CisLoginPage(driver);
        exploreTabToolbar = loginPage.login(UserUtil.getUser());

        assertThat(exploreTabToolbar.getStartComparisonText(), is(equalTo("Start Comparison")));
    }

    @Test
    @TestRail(testCaseId = "9435")
    @Description("Unsuccessful login with invalid details")
    public void testIncorrectEmailAndPassword() {
        loginPage = new CisLoginPage(driver);
        loginPage.failedLoginAs(new GenerateStringUtil().generateEmail(), "fakePassword");

        assertThat(loginPageErrorMessage.toUpperCase(), is(loginPage.getLoginErrorMessage()));
    }

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

    @Test
    @TestRail(testCaseId = "9554")
    @Description("Verify deployment connection in sub header - Production")
    public void testDeploymentConnection() {
        loginPage = new CisLoginPage(driver);
        exploreTabToolbar = loginPage.login(UserUtil.getUser());

        assertThat(exploreTabToolbar.getDeploymentInfo(), is(equalTo("Production")));
    }
}



