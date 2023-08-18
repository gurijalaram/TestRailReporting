package com.apriori;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.apriori.cic.utils.WorkflowTestUtil;
import com.apriori.pageobjects.connectors.ConnectorsPage;
import com.apriori.pageobjects.home.CIConnectHome;
import com.apriori.pageobjects.home.help.cicuserguide.CicUserGuide;
import com.apriori.pageobjects.home.settings.CostingServiceSettings;
import com.apriori.pageobjects.login.CicLoginPage;
import com.apriori.pageobjects.users.UsersPage;
import com.apriori.pageobjects.workflows.WorkflowHome;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NavBarTests extends WorkflowTestUtil {

    private SoftAssertions softAssertions;
    private CIConnectHome ciConnectHome;

    @BeforeEach
    public void setup() {
        currentUser = UserUtil.getUser();
        softAssertions = new SoftAssertions();
        ciConnectHome = new CicLoginPage(driver).login(currentUser);

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
    @TestRail(id = {3654})
    public void testNavigateToConnectorsTab() {
        ConnectorsPage connectorsPage = new CicLoginPage(driver)
            .login(currentUser)
            .clickConnectorsMenu();

        assertEquals("Verify Connectors menu", "Connectors", connectorsPage.getConnectorText());

    }

    @Test
    @TestRail(id = {3652})
    public void testNavigateToWorkflowsTab() {
        WorkflowHome workflowHome = new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu();

        assertEquals("Verify Workflows menu", "Workflows", workflowHome.getWorkflowText());
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
}

