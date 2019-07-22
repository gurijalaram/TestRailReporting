package test.java.compare;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.UsersEnum;
import main.java.pages.compare.ComparePage;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import org.junit.Test;
import test.java.evaluate.DeletePrivateScenarioTests;

import java.util.Scanner;

public class EditPublicComparisonTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;

    private String filePath = new Scanner(DeletePrivateScenarioTests.class.getClassLoader()
        .getResourceAsStream("filepath.txt"), "UTF-8").useDelimiter("\\A").next();

    public EditPublicComparisonTests() {
        super();
    }

    @Test
    @Description("Test publishing a comparison shows the comparison in the comparison table")
    @Severity(SeverityLevel.NORMAL)
    public void testEditPublicComparisonPublish() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("DeletePrivateComparisonTests", filePath, "casting.prt")
            .publishScenario()
            .createNewComparison()
            .enterComparisonName("DeletePrivateComparison10")
            .save();

        new EvaluatePage(driver).publishScenario();

        assertThat(new ExplorePage(driver).findComparison("DeletePrivateComparison10").isDisplayed(), is(true));
    }

    @Test
    @Description("Test editing a published comparison shows the comparison view")
    @Severity(SeverityLevel.NORMAL)
    public void testEditPublicComparison() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile("DeletePrivateComparisonTests", filePath, "casting.prt")
            .publishScenario()
            .createNewComparison()
            .enterComparisonName("DeletePrivateComparison10")
            .save();

        new EvaluatePage(driver).publishScenario().openComparison("DeletePrivateComparison10");

        assertThat(new ComparePage(driver).getDescriptionText(), containsString("DeletePrivateComparison10"));
    }
}
