package test.java.evaluate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.UsersEnum;
import main.java.enums.WorkspaceEnum;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import org.junit.Test;

import java.util.Scanner;

public class DeletePublicScenarioTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;

    private String filePath = new Scanner(DeletePublicScenarioTests.class.getClassLoader()
        .getResourceAsStream("filepath.txt"), "UTF-8").useDelimiter("\\A").next();

    public DeletePublicScenarioTests() {
        super();
    }

    @Test
    @Description("Test a public scenario can be deleted from the component table")
    @Severity(SeverityLevel.NORMAL)
    public void testDeletePrivateScenario() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("DeletePublicScenario", filePath, "casting.prt")
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario("DeletePublicScenario", "casting")
            .delete()
            .deleteScenario();

        assertThat(new ExplorePage(driver).getListOfScenarios("DeletePublicScenario", "casting") < 1, is(true));
    }
}
