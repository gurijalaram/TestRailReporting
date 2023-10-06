package com.apriori;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.apriori.cic.utils.WorkflowTestUtil;
import com.apriori.pageobjects.connectors.ConnectorsPage;
import com.apriori.pageobjects.home.CIConnectHome;
import com.apriori.pageobjects.home.help.CicAbout;
import com.apriori.pageobjects.home.help.cicuserguide.CicUserGuide;
import com.apriori.pageobjects.home.settings.CostingServiceSettings;
import com.apriori.pageobjects.login.CicLoginPage;
import com.apriori.pageobjects.users.UsersPage;
import com.apriori.pageobjects.workflows.WorkflowHome;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testconfig.TestSuiteType;
import com.apriori.testrail.TestRail;

import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.ExcludeTags;

import java.time.Year;

@Tag(TestSuiteType.TestSuite.CUSTOMER)
public class NavBarTests extends WorkflowTestUtil {

    private SoftAssertions softAssertions;

    @BeforeEach
    public void setup() {
        currentUser = UserUtil.getUser();
        softAssertions = new SoftAssertions();
    }

    @Test
    @TestRail(id = {3653})
    public void testNavigateToUsersTab() {
        UsersPage usersPage = new CicLoginPage(driver)
            .login(currentUser)
            .clickUsersMenu();
        assertEquals("Verify users menu", "Users", usersPage.getUsersText());
    }

    @Test
    @TestRail(id = {3654, 3660})
    public void testNavigateToConnectorsTab() {
        ConnectorsPage connectorsPage = new CicLoginPage(driver)
            .login(currentUser)
            .clickConnectorsMenu();

        assertEquals("Verify Connectors menu", "Connectors", connectorsPage.getConnectorText());

    }

    @Test
    @TestRail(id = {3652})
    public void testNavigateToWorkflowsTab() {
        CIConnectHome ciConnectHome = new CicLoginPage(driver).login(currentUser);
        WorkflowHome workflowHome = ciConnectHome.clickWorkflowMenu();

        assertEquals("Verify Workflows menu", "Workflows", workflowHome.getWorkflowText());

        CicLoginPage cicLoginPage = ciConnectHome.clickLogout();
        softAssertions.assertThat(cicLoginPage.getEmailInputCloud().isDisplayed()).isTrue();
    }

    @Test
    @TestRail(id = {3659})
    public void testUserDropDownInfo() {
        WorkflowHome workflowHome = new CicLoginPage(driver)
            .login(currentUser)
            .expandUserInfoDropdown();

        assertEquals("Verify Login ID", currentUser.getEmail(), workflowHome.getLoginID());
        assertEquals("Verify Company Name", "aPriori Internal", workflowHome.getCurrentCompany());
    }

    @Test
    @TestRail(id = {3655})
    public void testCicUserGuideNavigation() throws Exception {
        CicUserGuide cicUserGuide = new CicLoginPage(driver)
            .login(currentUser)
            .navigateToCicUserGuide()
            .switchTab();

        assertEquals("Verify Online help documentation guide", "https://www.apriori.com/Collateral/Documents/English-US/online_help/CIC/",
            cicUserGuide.getURL().split("2022")[0]);
    }

    @Test
    @TestRail(id = {4002})
    public void testNavigateToCostingServiceSettings() {
        CostingServiceSettings costingServiceSettings = new CicLoginPage(driver)
            .login(currentUser)
            .clickCostingServiceSettings();

        assertEquals("Verify Settings model page", "Costing Service Settings", costingServiceSettings.getCostingServiceSettingsText());
    }

    @Test
    @TestRail(id = {24486, 24487, 24488})
    public void testStmtAndTermsOfUse() {
        CicAbout cicAbout = new CicLoginPage(driver)
            .login(currentUser)
            .navigateToAboutApConnect();

        softAssertions.assertThat(cicAbout.getCicAboutStatement().getText()).isEqualTo("aP Connect is Powered by ThingWorx.");
        softAssertions.assertThat(cicAbout.getCicTermsOfUse().getText()).isEqualTo("This aP Connect application (“Application”), including the intellectual property rights and trade secrets contained therein, is the property of aPriori Technologies, Inc. and/or its suppliers. Use of this Application in any manner is governed by the terms and conditions of a signed subscription agreement between You and aPriori. In the absence of a signed subscription agreement, the use of this software is governed solely by the aPriori Subscription and Professional Services Agreement available at https://resources.apriori.com/i/1136035-apriori-saas-subscription-agreement-060119.");
        softAssertions.assertThat(cicAbout.getCopyRightElement().getText()).isEqualTo(String.format("aPriori Technologies Inc. © 2003-%s. All Rights Reserved.", Year.now().getValue()));
    }

    @After
    public void cleanup() {
        softAssertions.assertAll();
    }
}


