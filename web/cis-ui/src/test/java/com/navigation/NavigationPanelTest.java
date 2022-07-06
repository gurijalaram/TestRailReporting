package com.navigation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.navtoolbars.CisHeaderBar;
import com.apriori.pageobjects.navtoolbars.LeftHandNavigationBar;
import com.apriori.pageobjects.pages.login.CisLoginPage;
import com.apriori.pageobjects.pages.myuser.MyUserPage;
import com.apriori.pageobjects.pages.myuser.TermsOfUsePage;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.CisNavBarItemsEnum;
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
    private TermsOfUsePage termsOfUsePage;

    @Test
    @TestRail(testCaseId = {"11992","12014","12007"})
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

    @Test
    @TestRail(testCaseId = {"13233"})
    @Description("Verify that user can access the Terms of Use Page")
    public void testUserCanAccessTheTermsOfUsePage() {
        loginPage = new CisLoginPage(driver);
        leftHandNavigationBar = loginPage.cisLogin(UserUtil.getUser())
                .clickUserIcon();

        termsOfUsePage = new MyUserPage(driver)
                .clickTermsOfUse();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(termsOfUsePage.getTermsOfUseUrl()).contains("/terms-of-use");
        softAssertions.assertThat(termsOfUsePage.getTermsOfUseText()).contains("This Cost Insight Source application (“Application”), including the intellectual property rights and trade secrets contained therein, is the property of aPriori Technologies, Inc. and/or its suppliers. Use of this Application in any manner is governed by the terms and conditions of a signed subscription agreement between You and aPriori. In the absence of a signed subscription agreement, the use of this software is governed solely by the aPriori Subscription and Professional Services Agreement availablehere.");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11997","11998","11999","12000","12001","12003","12008"})
    @Description("Verify that user can view the left navigation bar items and aPriori logo")
    public void testNavBarItems() {
        loginPage = new CisLoginPage(driver);
        leftHandNavigationBar = loginPage.cisLogin(UserUtil.getUser());

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(leftHandNavigationBar.isaPrioriLogoDisplayed()).isEqualTo(true);
        softAssertions.assertThat(leftHandNavigationBar.isDashboardBtnDisplayed()).isEqualTo(true);
        softAssertions.assertThat(leftHandNavigationBar.getDashBoardBtnDefaultState()).contains("Mui-selected");
        softAssertions.assertThat(leftHandNavigationBar.getItemsOfSections(CisNavBarItemsEnum.COLLABORATION.getNavBarItems())).contains(CisNavBarItemsEnum.SOURCING_PROJECT.getNavBarItems());
        softAssertions.assertThat(leftHandNavigationBar.getItemsOfSections(CisNavBarItemsEnum.DATA.getNavBarItems())).contains(CisNavBarItemsEnum.PARTS_N_ASSEMBLIES.getNavBarItems(),CisNavBarItemsEnum.BID_PACKAGES.getNavBarItems());
        softAssertions.assertThat(leftHandNavigationBar.getItemsOfSections(CisNavBarItemsEnum.ACCESS_CONTROL.getNavBarItems())).contains(CisNavBarItemsEnum.SUPPLIERS.getNavBarItems(),CisNavBarItemsEnum.USERS.getNavBarItems());

        leftHandNavigationBar.collapseNavigationPanel();
        softAssertions.assertThat(leftHandNavigationBar.isCollapsedAprioriLogoDisplayed()).isEqualTo(true);

        softAssertions.assertAll();
    }
}
