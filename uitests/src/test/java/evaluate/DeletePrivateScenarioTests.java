package test.java.evaluate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.UsersEnum;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import org.junit.Test;

import java.util.Scanner;

public class DeletePrivateScenarioTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;

    private String filePath = new Scanner(DeletePrivateScenarioTests.class.getClassLoader()
        .getResourceAsStream("filepath.txt"), "UTF-8").useDelimiter("\\A").next();

    public DeletePrivateScenarioTests() {
        super();
    }

    @Test
    @Description("Test a private scenario can be deleted from the component table")
    @Severity(SeverityLevel.NORMAL)
    public void testDeletePrivateScenario() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("DeletePrivateScenario", filePath, "casting.prt")
            .selectExploreButton()
            .highlightScenario("DeletePrivateScenario", "casting")
            .delete()
            .deleteScenario();

        assertThat(new ExplorePage(driver).getListOfScenarios("DeletePrivateScenario", "casting") < 1, is(true));
    }
}