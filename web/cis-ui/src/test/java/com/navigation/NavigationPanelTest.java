package com.navigation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.navtoolbars.CisHeaderBar;
import com.apriori.pageobjects.navtoolbars.LeftHandNavigationBar;
import com.apriori.pageobjects.pages.login.CisLoginPage;
import com.apriori.pageobjects.pages.myuser.MyUserPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

public class NavigationPanelTest extends TestBase {

    public NavigationPanelTest() {
        super();
    }

    private CisLoginPage loginPage;
    private LeftHandNavigationBar leftHandNavigationBar;
    private CisHeaderBar cisHeaderBar;
    private MyUserPage myUserPage;

    @Test
    @TestRail(testCaseId = {"11992","12014"})
    @Description("Verify the navigation bar default state and Header text on the home page")
    public void testNavigationBarDefaultStateAndWelcomeText() {
        loginPage = new CisLoginPage(driver);
        leftHandNavigationBar = loginPage.cisLogin(UserUtil.getUser());

        assertThat(leftHandNavigationBar.getNavigationPanelDefaultState(), is(equalTo("non-collapsed")));

        cisHeaderBar = new CisHeaderBar(driver);

        assertThat(cisHeaderBar.getHeaderText(), is(equalTo("Dashboard")));

    }

    @Test
    @TestRail(testCaseId = {"13194","13051"})
    @Description("Verify that user can logout from the CIS application")
    public void testUserCanLogOutFromTheCISApplication() {
        loginPage = new CisLoginPage(driver);
        leftHandNavigationBar = loginPage.cisLogin(UserUtil.getUser())
                        .clickUserIcon();

        myUserPage = new MyUserPage(driver);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(myUserPage.isSettingsOptionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(myUserPage.isTermsOfUseOptionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(myUserPage.isSupportOptionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(myUserPage.isLogoutOptionDisplayed()).isEqualTo(true);

        myUserPage.clickLogOut();

        softAssertions.assertThat(loginPage.isLogoDisplayed()).isEqualTo(true);

        softAssertions.assertAll();
    }
}
