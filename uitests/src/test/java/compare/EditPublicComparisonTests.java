package test.java.compare;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import main.java.base.TestBase;
import main.java.enums.UsersEnum;
import main.java.pages.compare.ComparePage;
import main.java.pages.evaluate.EvaluatePage;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import main.java.utils.Util;
import org.junit.Test;

public class EditPublicComparisonTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private ComparePage comparePage;

    public EditPublicComparisonTests() {
        super();
    }

    @Test
    @Description("Test publishing a comparison shows the comparison in the comparison table")
    public void testEditPublicComparisonPublish() {

        String testComparisonName = new Util().getComparisonName();

        loginPage = new LoginPage(driver);
        comparePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Casting.prt"))
            .publishScenario()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class);

        evaluatePage = new EvaluatePage(driver);
        explorePage = evaluatePage.publishScenario();

        assertThat(explorePage.getListOfComparisons(testComparisonName) > 0, is(true));
    }

    @Test
    @Description("Test editing a published comparison shows the comparison view")
    public void testEditPublicComparison() {

        String testComparisonName = new Util().getComparisonName();

        loginPage = new LoginPage(driver);
        comparePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("Casting.prt"))
            .publishScenario()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class);

        evaluatePage = new EvaluatePage(driver);
        comparePage = evaluatePage.publishScenario()
            .openComparison(testComparisonName);

        assertThat(comparePage.getDescriptionText(), containsString(testComparisonName));
    }
}
