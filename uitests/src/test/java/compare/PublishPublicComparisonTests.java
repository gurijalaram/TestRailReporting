package test.java.compare;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.qameta.allure.Description;
import main.java.base.TestBase;
import main.java.constants.Constants;
import main.java.enums.ProcessGroupEnum;
import main.java.enums.UsersEnum;
import main.java.pages.compare.ComparePage;
import main.java.pages.compare.ComparisonTablePage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import org.junit.Test;

import java.util.Random;

public class PublishPublicComparisonTests extends TestBase {

    private LoginPage loginPage;
    private ComparePage comparePage;

    int random = new Random().nextInt(1000);

    public PublishPublicComparisonTests() {
        super();
    }

    @Test
    @Description("Test a public comparison can be published")
    public void testPublishPublicComparison() {

        String testScenarioName = Constants.scenarioName;
        int comparisonInt = random;

        loginPage = new LoginPage(driver);
        comparePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("Casting.prt"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .publishScenario()
            .createNewComparison()
            .enterComparisonName(testScenarioName + comparisonInt)
            .save(ComparePage.class)
            .addScenario()
            .filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", "Casting")
            .apply(ComparisonTablePage.class)
            .apply();

        assertThat(new ExplorePage(driver).findComparison(testScenarioName + comparisonInt).isDisplayed(), is(true));
    }
}