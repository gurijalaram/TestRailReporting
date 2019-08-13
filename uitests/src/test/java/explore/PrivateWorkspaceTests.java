package test.java.explore;

import io.qameta.allure.Description;
import main.java.base.TestBase;
import main.java.enums.UsersEnum;
import main.java.enums.WorkspaceEnum;
import main.java.pages.login.LoginPage;
import org.junit.Test;

import java.time.LocalDateTime;

public class PrivateWorkspaceTests extends TestBase {

    private final String scenarioName = "AutoScenario" + LocalDateTime.now();

    private LoginPage loginPage;

    public PrivateWorkspaceTests() {
        super();
    }

    @Test
    @Description("Test opening scenario from private workspace")
    public void testSearchOpenScenario() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .openScenario(scenarioName, "Casting");
    }
}