package com.navigation;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import com.apriori.pageobjects.navtoolbars.CisHeaderBar;
import com.apriori.pageobjects.navtoolbars.LeftHandNavigationBar;
import com.apriori.pageobjects.pages.help.ZendeskSignInPage;
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
    private ZendeskSignInPage zendeskSignInPage;

    @Test
    @TestRail(testCaseId = {"11992", "12014", "12007"})
    @Description("Verify the navigation bar default state and Header text on the home page")
    public void testNavigationBarDefaultStateAndWelcomeText() {
        loginPage = new CisLoginPage(driver);
        leftHandNavigationBar = loginPage.cisLogin(UserUtil.getUser());

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(leftHandNavigationBar.getNavigationPanelDefaultState()).isEqualTo("non-collapsed");

        cisHeaderBar = new CisHeaderBar(driver);

        softAssertions.assertThat(cisHeaderBar.getHeaderText()).isEqualTo("All Messages");

    }

    @Test
    @TestRail(testCaseId = {"13194", "13051", "12013"})
    @Description("Verify that user can logout from the CIS application")
    public void testUserCanLogOutFromTheCISApplication() {
        loginPage = new CisLoginPage(driver);
        myUserPage = loginPage.cisLogin(UserUtil.getUser())
            .clickUserIcon();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(myUserPage.getLoggedUsername()).contains("QA");
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
        myUserPage = loginPage.cisLogin(UserUtil.getUser())
            .clickUserIcon();

        termsOfUsePage = new MyUserPage(driver)
            .clickTermsOfUse();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(termsOfUsePage.getTermsOfUseUrl()).contains("/terms-of-use");
        softAssertions.assertThat(termsOfUsePage.getTermsOfUseText()).contains("This aP Workspace application (“Application”), including the intellectual property rights and trade secrets contained therein");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11997", "11999", "12000", "12003", "12008"})
    @Description("Verify that user can view the left navigation bar items and aPriori logo")
    public void testNavBarItems() {
        loginPage = new CisLoginPage(driver);
        leftHandNavigationBar = loginPage.cisLogin(UserUtil.getUser());

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(leftHandNavigationBar.isaPrioriLogoDisplayed()).isEqualTo(true);
        softAssertions.assertThat(leftHandNavigationBar.getMessageBtnDefaultState()).contains("Mui-selected");
        softAssertions.assertThat(leftHandNavigationBar.getItemsOfSections(CisNavBarItemsEnum.COLLABORATION.getNavBarItems())).contains(CisNavBarItemsEnum.MESSAGES.getNavBarItems());
        softAssertions.assertThat(leftHandNavigationBar.getItemsOfSections(CisNavBarItemsEnum.DATA.getNavBarItems())).contains(CisNavBarItemsEnum.PARTS_N_ASSEMBLIES.getNavBarItems());

        leftHandNavigationBar.collapseNavigationPanel();
        softAssertions.assertThat(leftHandNavigationBar.isCollapsedAprioriLogoDisplayed()).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"13553"})
    @Description("Verify that user can access the 'Support' page")
    public void testUserCanAccessTheSupportPage() {
        loginPage = new CisLoginPage(driver);
        zendeskSignInPage = loginPage.cisLogin(UserUtil.getUser())
            .clickUserIcon()
            .clickSupport()
            .switchTab();

        assertThat(zendeskSignInPage.getCurrentUrl(), containsString("https://support.apriori.com/hc/en-us"));
    }
}
