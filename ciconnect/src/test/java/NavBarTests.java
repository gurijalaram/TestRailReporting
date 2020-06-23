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
        loginPage = new LoginPage(driver);
        userList = loginPage.login(driver)
            .clickUserMenu();

        assertThat("Users", equalTo(userList.getUsersText()));
    }

    @Test
    public void testNavigateToConnectorsTab() {
        loginPage = new LoginPage(driver);
        connectorList = loginPage.login(driver)
            .clickConnectorMenu();

        assertThat("Connectors", equalTo(connectorList.getConnectorText()));
    }

    @Test
    public void testNavigateToWorkflowsTab() {
        loginPage = new LoginPage(driver);
        schedule = loginPage.login(driver)
            .clickConnectorMenu()
            .clickWorkflowMenu();

        assertThat("New", equalTo(schedule.getNewWorkflowBtnText()));
    }

}


