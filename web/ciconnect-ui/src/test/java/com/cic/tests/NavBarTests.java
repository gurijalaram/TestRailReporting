package com.cic.tests;

import com.apriori.pages.connectors.ConnectorsPage;
import com.apriori.pages.home.help.cicuserguide.CicUserGuide;
import com.apriori.pages.home.settings.CostingServiceSettings;
import com.apriori.pages.login.CicLoginPage;
import com.apriori.pages.users.UsersPage;
import com.apriori.pages.workflows.WorkflowHome;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import org.junit.Assert;
import org.junit.Test;

public class NavBarTests extends TestBase {

    private UserCredentials currentUser = UserUtil.getUser();

    public NavBarTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"3653"})
    public void testNavigateToUsersTab() {
        UsersPage usersPage = new CicLoginPage(driver)
            .login(currentUser)
            .clickUsersMenu();
        Assert.assertEquals("Verify users menu", "Users", usersPage.getUsersText());
    }

    @Test
    @TestRail(testCaseId = {"3654"})
    public void testNavigateToConnectorsTab() {
        ConnectorsPage connectorsPage = new CicLoginPage(driver)
            .login(currentUser)
            .clickConnectorsMenu();

        Assert.assertEquals("Verify Connectors menu", "Connectors", connectorsPage.getConnectorText());

    }

    @Test
    @TestRail(testCaseId = {"3652"})
    public void testNavigateToWorkflowsTab() {
        WorkflowHome workflowHome = new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu();

        Assert.assertEquals("Verify Workflows menu", "Workflows", workflowHome.getWorkflowText());
    }

    @Test
    @TestRail(testCaseId = {"3659"})
    public void testUserDropDownInfo() {
        WorkflowHome workflowHome = new CicLoginPage(driver)
            .login(currentUser)
            .expandUserInfoDropdown();

        Assert.assertEquals("Verify Login ID", currentUser.getEmail(), workflowHome.getLoginID());
        Assert.assertEquals("Verify Company Name", "aPriori Internal", workflowHome.getCurrentCompany());
    }

    @Test
    @TestRail(testCaseId = {"3655"})
    public void testCicUserGuideNavigation() throws Exception {
        CicUserGuide cicUserGuide = new CicLoginPage(driver)
            .login(currentUser)
            .navigateToCicUserGuide()
            .switchTab();

        Assert.assertEquals("Verify Online help documentation guide", "https://www.apriori.com/Collateral/Documents/English-US/online_help/CIC/",
            cicUserGuide.getURL().split("2022")[0]);
    }

    @Test
    @TestRail(testCaseId = {"4002"})
    public void testNavigateToCostingServiceSettings() {
        CostingServiceSettings costingServiceSettings = new CicLoginPage(driver)
            .login(currentUser)
            .openCostingServiceSettings();

        Assert.assertEquals("Verify Settings model page", "Costing Service Settings", costingServiceSettings.getCostingServiceSettingsText());
    }
}


