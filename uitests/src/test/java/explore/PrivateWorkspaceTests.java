package test.java.explore;

import main.java.base.TestBase;
import main.java.enums.UsersEnum;
import main.java.enums.WorkspaceEnum;
import main.java.pages.login.LoginPage;
import org.junit.Test;

public class PrivateWorkspaceTests extends TestBase {

    private LoginPage loginPage;

    public PrivateWorkspaceTests() {
        super();
    }

    /**
     * Test opening scenario from private workspace
     */
    @Test
    public void testSearchOpenScenario() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .openScenario("Casting", "Scenario A");
    }
}
