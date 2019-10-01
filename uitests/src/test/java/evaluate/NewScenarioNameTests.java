package evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.CostingLabelEnum;
import com.apriori.utils.enums.UsersEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;

public class NewScenarioNameTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;

    public NewScenarioNameTests() {
        super();
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"577"})
    @Description("Test entering a new scenario name shows the correct name on the evaluate page")
    public void testEnterNewScenarioName() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        evaluatePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile("partbody_2.stp"))
            .createNewScenario()
            .enterScenarioName(testScenarioName)
            .save();

        assertThat(evaluatePage.getCurrentScenarioName(testScenarioName), is(true));
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"1576", "1586", "1587", "1589"})
    @Description("Test entering a new scenario name shows the correct name on the evaluate page after the scenario is published")
    public void testPublishEnterNewScenarioName() {

        String testScenarioName = new Util().getScenarioName();
        String testNewScenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("partbody_2.stp"));

        assertThat(evaluatePage.getCostLabel(CostingLabelEnum.READY_TO_COST.getCostingText()), CoreMatchers.is(true));

        evaluatePage.costScenario()
            .publishScenario()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "partbody_2");

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.createNewScenario()
            .enterScenarioName(testNewScenarioName)
            .save();

        assertThat(evaluatePage.getCurrentScenarioName(testNewScenarioName), is(true));
    }
}