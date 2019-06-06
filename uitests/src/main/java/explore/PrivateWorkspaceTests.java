package main.java.explore;

import main.java.base.TestBase;
import main.java.enums.UsersEnum;
import main.java.pages.login.LoginPage;
import org.junit.Test;

public class PrivateWorkspaceTests extends TestBase {

    private LoginPage loginPage;

    public PrivateWorkspaceTests() {
        super();
    }

    @Test
    public void testSearchForScenario() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CIE_TE_USER.getUsername(), UsersEnum.CIE_TE_USER.getPassword())
            .selectWorkSpace("Public Workspace")
            .findScenario();
    }
}
