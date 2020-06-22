import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.web.driver.TestBase;

import header.GenericHeader;
import org.junit.Test;
import users.UserList;
import workflows.GenericWorkflow;

public class NavBarTests extends TestBase {

    private GenericWorkflow genericWorkflow;
    private UserList userList;
    private GenericHeader genericHeader;
    private LoginPage loginPage;

    public NavBarTests() {super();}

    @Test
    public void testNavigateToUsersTab() {
        loginPage = new LoginPage(driver);
        userList = loginPage.login(driver)
            .clickUsersMenu();

        assertThat("Username", equalTo(userList.getUsersText()));
    }

}


