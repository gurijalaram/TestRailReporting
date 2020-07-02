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

        assertThat(userList.getUsersText(), equalTo("Users"));
    }

    @Test
    public void testNavigateToConnectorsTab() {
        connectorList = new LoginPage(driver)
            .login(driver)
            .clickConnectorsMenu();

        assertThat(connectorList.getConnectorText(), equalTo("Connectors"));
    }

    @Test
    public void testNavigateToWorkflowsTab() {
        schedule = new LoginPage(driver)
            .login(driver)
            .clickConnectorsMenu()
            .clickWorkflowMenu();

        assertThat(schedule.getNewWorkflowBtnText(), equalTo("New"));
    }

    @Test
    public void testUserDropDownInfo() {
        pageHeader = new LoginPage(driver)
            .login(driver)
            .expandUserInfoDropdown();

        assertThat(pageHeader.getCurrentUser(), equalTo("Kunal Patel"));
        assertThat(pageHeader.getLoginID(), equalTo("kpatel@apriori.com"));
        assertThat(pageHeader.getCurrentCompany(), equalTo("aPriori Internal"));
    }

    @Test
    public void testCicUserGuideNavigation() throws Exception {
        cicUserGuide = new LoginPage(driver)
            .login(driver)
            .navigateToCicUserGuide()
            .switchTab()
            .switchToIFrameUserGuide("page_iframe");

        assertThat(cicUserGuide.getURL(), startsWith("https://www.apriori.com/Collateral/Documents/English-US/online_help/CIConnect"));
    }

    @Test
    public void testAboutAPrioriLinkNavigation() {
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


