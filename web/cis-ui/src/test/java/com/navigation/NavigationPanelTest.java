package com.navigation;

import com.apriori.pageobjects.navtoolbars.CisHeaderBar;
import com.apriori.pageobjects.navtoolbars.LeftHandNavigationBar;
import com.apriori.pageobjects.pages.login.CisLoginPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NavigationPanelTest extends TestBase {

    public NavigationPanelTest() {
        super();
    }

    private CisLoginPage loginPage;
    private LeftHandNavigationBar leftHandNavigationBar;
    private CisHeaderBar cisHeaderBar;
    private SoftAssertions softAssertions;

    @Before
    public void setUp() {
        softAssertions = new SoftAssertions();
    }

    @After
    public void tearDown() {
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "11992")
    @Description("Verify the navigation bar position and default state on the home page")
    public void testNavigationBarDefaultState() {
        loginPage = new CisLoginPage(driver);
        leftHandNavigationBar = loginPage.cisLogin(UserUtil.getUser());
        softAssertions.assertThat(leftHandNavigationBar.getNavigationPanelDefaultState()).isEqualTo("non-collapsed");

    }

    @Test
    @TestRail(testCaseId = "12014")
    @Description("Verify the Welcome text on the header")
    public void testWelcomeTextOnHeader() {
        loginPage = new CisLoginPage(driver);
        cisHeaderBar = loginPage.cisLogin(UserUtil.getUser());
        softAssertions.assertThat(cisHeaderBar.getWelcomeText()).isEqualTo("Welcome Back!");

    }
}
