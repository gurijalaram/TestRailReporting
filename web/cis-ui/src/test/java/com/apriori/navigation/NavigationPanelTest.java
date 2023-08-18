package com.apriori.navigation;

import com.apriori.navtoolbars.CisHeaderBar;
import com.apriori.navtoolbars.LeftHandNavigationBar;
import com.apriori.pageobjects.help.ZendeskSignInPage;
import com.apriori.pageobjects.login.CisLoginPage;
import com.apriori.pageobjects.myuser.MyUserPage;
import com.apriori.pageobjects.myuser.TermsOfUsePage;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import com.utils.CisNavBarItemsEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

public class NavigationPanelTest extends TestBaseUI {

    private CisLoginPage loginPage;
    private LeftHandNavigationBar leftHandNavigationBar;
    private CisHeaderBar cisHeaderBar;
    private MyUserPage myUserPage;
    private TermsOfUsePage termsOfUsePage;
    private ZendeskSignInPage zendeskSignInPage;

    public NavigationPanelTest() {
        super();
    }

    @Test
    @TestRail(id = {11992, 12014, 12007})
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
    @TestRail(id = {13194, 13051, 12013})
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
    @TestRail(id = {13233})
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
    @TestRail(id = {11997, 11999, 12000, 12003, 12008})
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
    @TestRail(id = {13553})
    @Description("Verify that user can access the 'Support' page")
    public void testUserCanAccessTheSupportPage() {
        loginPage = new CisLoginPage(driver);
        zendeskSignInPage = loginPage.cisLogin(UserUtil.getUser())
            .clickUserIcon()
            .clickSupport()
            .switchTab();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(zendeskSignInPage.isZendeskLabelDisplayed()).isEqualTo(true);

        softAssertions.assertAll();
    }
}
