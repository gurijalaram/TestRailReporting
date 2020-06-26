package navbar;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.web.driver.TestBase;

import cicuserguide.CicUserGuide;
import connectors.ConnectorList;
import header.CostingServiceSettings;
import header.PageHeader;
import login.LoginPage;
import org.junit.Test;
import users.UserList;
import workflows.GenericWorkflow;
import workflows.Schedule;

public class NavBarTests extends TestBase {

    private GenericWorkflow genericWorkflow;
    private UserList userList;
    private LoginPage loginPage;
    private ConnectorList connectorList;
    private Schedule schedule;
    private PageHeader pageHeader;
    private CicUserGuide cicUserGuide;
    private CostingServiceSettings costingServiceSettings;

    public NavBarTests() {
        super();
    }

    @Test
    public void testNavigateToUsersTab() {
        userList = new LoginPage(driver)
            .login(driver)
            .clickUsersMenu();

        assertThat("Users", equalTo(userList.getUsersText()));
    }

    @Test
    public void testNavigateToConnectorsTab() {
        connectorList = new LoginPage(driver)
            .login(driver)
            .clickConnectorsMenu();

        assertThat("Connectors", equalTo(connectorList.getConnectorText()));
    }

    @Test
    public void testNavigateToWorkflowsTab() {
        schedule = new LoginPage(driver)
            .login(driver)
            .clickConnectorsMenu()
            .clickWorkflowMenu();

        assertThat("New", equalTo(schedule.getNewWorkflowBtnText()));
    }

    @Test
    public void testUserDropDownInfo() {
        pageHeader = new LoginPage(driver)
            .login(driver)
            .expandUserInfoDropdown();

        assertThat("Kunal Patel", equalTo(pageHeader.getCurrentUser()));
        assertThat("kpatel@apriori.com", equalTo(pageHeader.getLoginID()));
        assertThat("aPriori Internal", equalTo(pageHeader.getCurrentCompany()));
    }

    @Test
    public void testCicUserGuideNavigation() throws Exception {
        cicUserGuide = new LoginPage(driver)
            .login(driver)
            .navigateToCicUserGuide()
            .switchTab()
            .switchToIFrameUserGuide("page_iframe");

        //assertThat("aPriori Cost Insight Connect", equalTo(cicUserGuide.getUserGuideTitle()));
        assertThat(cicUserGuide.getURL(), startsWith("https://www.apriori.com/Collateral/Documents/English-US/online_help/CIConnect"));
    }

    @Test
    public void testAboutAPrioriLinkNavigation() throws Exception {
        cicUserGuide = new LoginPage(driver)
            .login(driver)
            .navigateToAboutAPriori()
            .switchTab();

        assertThat(cicUserGuide.getURL(), startsWith("https://www.apriori.com/about-us/"));
    }

    @Test
    public void testNavigateToCostingServiceSettings() {
        costingServiceSettings = new LoginPage(driver)
            .login(driver)
            .openCostingServiceSettings();

        assertThat(costingServiceSettings.getCostingServiceSettingsText(), equalTo("Costing Service Settings"));

    }


}


