import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.web.driver.TestBase;

import connectors.ConnectorList;
import header.PageHeader;
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

    public NavBarTests() {super();}

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
}


