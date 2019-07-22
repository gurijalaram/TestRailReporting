package test.java.evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.UsersEnum;
import main.java.enums.WorkspaceEnum;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import org.junit.Test;
import test.java.compare.AddPublicScenarioTests;

import java.util.Scanner;

public class NewScenarioNameTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private String filePath = new Scanner(AddPublicScenarioTests.class.getClassLoader()
        .getResourceAsStream("filepath.txt"), "UTF-8").useDelimiter("\\A").next();

    public NewScenarioNameTests() {
        super();
    }

    @Test
    @Description("Test entering a new scenario name shows the correct name on the evaluate page")
    @Severity(SeverityLevel.NORMAL)
    public void testEnterNewScenarioName() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        new ExplorePage(driver).uploadFile("scenario name", filePath, "Lug.SLDPRT");

        explorePage = new ExplorePage(driver);
        explorePage.createNewScenario()
            .enterScenarioName("new scenario name")
            .save();

        assertThat(new EvaluatePage(driver).getCurrentScenarioName(), is(equalTo("new scenario name")));
    }

    @Test
    @Description("Test entering a new scenario name shows the correct name on the evaluate page after the scenario is published")
    @Severity(SeverityLevel.NORMAL)
    public void testPublishEnterNewScenarioName() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("publish scenario name", filePath, "Lug.SLDPRT")
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace())
            .createNewScenario()
            .enterScenarioName("publish new scenario name")
            .save();

        assertThat(new EvaluatePage(driver).getCurrentScenarioName(), is(equalTo("publish new scenario name")));
    }
}
