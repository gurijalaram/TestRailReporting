import com.apriori.utils.web.driver.TestBase;

import header.GenericHeader;
import org.junit.Test;
import users.UserList;
import workflows.GenericWorkflow;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

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
        //System.out.println(userList.getUsersText());
        assertThat("Users", equalTo(userList.getUsersText()));
    }

}


