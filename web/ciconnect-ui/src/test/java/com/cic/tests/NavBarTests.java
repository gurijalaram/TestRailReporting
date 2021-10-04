package com.cic.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.connectors.ConnectorList;
import com.apriori.header.CostingServiceSettings;
import com.apriori.header.PageHeader;
import com.apriori.pageobjects.LoginPage;
import com.apriori.users.UserList;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;
import com.apriori.workflows.Schedule;

import cicuserguide.CicUserGuide;
import org.junit.Test;
import utils.Constants;

public class NavBarTests extends TestBase {

    private UserList userList;
    private ConnectorList connectorList;
    private Schedule schedule;
    private PageHeader pageHeader;
    private CicUserGuide cicUserGuide;
    private CostingServiceSettings costingServiceSettings;

    public NavBarTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"3653"})
    public void testNavigateToUsersTab() {
        userList = new LoginPage(driver)
            .login()
            .clickUsersMenu();

        assertThat(userList.getUsersText(), equalTo("Users"));
    }

    @Test
    @TestRail(testCaseId = {"3654"})
    public void testNavigateToConnectorsTab() {
        connectorList = new LoginPage(driver)
            .login()
            .clickConnectorsMenu();

        assertThat(connectorList.getConnectorText(), equalTo("Connectors"));
    }

    @Test
    @TestRail(testCaseId = {"3652"})
    public void testNavigateToWorkflowsTab() {
        schedule = new LoginPage(driver)
            .login()
            .clickConnectorsMenu()
            .clickWorkflowMenu();

        assertThat(schedule.getNewWorkflowBtnText(), equalTo("New"));
    }

    @Test
    @TestRail(testCaseId = {"3659"})
    public void testUserDropDownInfo() {
        pageHeader = new LoginPage(driver)
            .login()
            .expandUserInfoDropdown();

        assertThat(pageHeader.getLoginID(), equalTo(LoginPage.getUserName()));
        assertThat(pageHeader.getCurrentCompany(), equalTo("aPriori Internal"));
    }

    @Test
    @TestRail(testCaseId = {"3655"})
    public void testCicUserGuideNavigation() throws Exception {
        cicUserGuide = new LoginPage(driver)
            .login()
            .navigateToCicUserGuide()
            .switchTab()
            .switchToIFrameUserGuide("page_iframe");

        assertThat(cicUserGuide.getURL(), startsWith("https://www.apriori.com/Collateral/Documents/English-US/online_help/CIConnect"));
    }

    @Test
    @TestRail(testCaseId = {"3656"})
    public void testAboutAPrioriLinkNavigation() {
        pageHeader = new LoginPage(driver)
            .login()
            .navigateToAboutAPriori();

        assertThat(pageHeader.getCicVersionText(), startsWith("Cost Insight Connect"));
    }

    @Test
    @TestRail(testCaseId = {"4002"})
    public void testNavigateToCostingServiceSettings() {
        costingServiceSettings = new LoginPage(driver)
            .login()
            .openCostingServiceSettings();

        assertThat(costingServiceSettings.getCostingServiceSettingsText(), equalTo("Costing Service Settings"));
    }
}


