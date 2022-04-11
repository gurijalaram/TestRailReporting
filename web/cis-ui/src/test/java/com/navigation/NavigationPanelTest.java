package com.navigation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.navtoolbars.CisHeaderBar;
import com.apriori.pageobjects.navtoolbars.LeftHandNavigationBar;
import com.apriori.pageobjects.pages.login.CisLoginPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;

import org.junit.Test;

public class NavigationPanelTest extends TestBase {

    public NavigationPanelTest() {
        super();
    }

    private CisLoginPage loginPage;
    private LeftHandNavigationBar leftHandNavigationBar;
    private CisHeaderBar cisHeaderBar;

    @Test
    @TestRail(testCaseId = {"11992","12014"})
    @Description("Verify the navigation bar default state and Welcome text on the home page")
    public void testNavigationBarDefaultStateAndWelcomeText() {
        loginPage = new CisLoginPage(driver);
        leftHandNavigationBar = loginPage.cisLogin(UserUtil.getUser());

        assertThat(leftHandNavigationBar.getNavigationPanelDefaultState(), is(equalTo("non-collapsed")));

        cisHeaderBar = new CisHeaderBar(driver);

        assertThat(cisHeaderBar.getWelcomeText(), is(equalTo("Welcome Back!")));

    }
}
