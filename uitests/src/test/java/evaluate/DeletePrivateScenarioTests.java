package evaluate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;

import io.qameta.allure.Issue;
import org.junit.Test;

public class DeletePrivateScenarioTests extends TestBase {

    private CIDLoginPage loginPage;
    private ExplorePage explorePage;

    private final String noComponentMessage = "You have no components that match the selected filter";

    public DeletePrivateScenarioTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"393", "587"})
    @Issue("AP-56845")
    @Description("Test a private scenario can be deleted from the component table")
    public void testDeletePrivateScenario() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new CIDLoginPage(driver);
        loginPage.login(UserUtil.getUser());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("casting.prt"))
            .selectExploreButton()
            .filterCriteria()
            .filterPrivateCriteria("Part", "Scenario Name", "Contains", testScenarioName)
            .apply(ExplorePage.class)
            .highlightScenario(testScenarioName, "casting");

        explorePage = new ExplorePage(driver);
        explorePage.delete()
            .deleteScenario()
            .filterCriteria()
            .filterPrivateCriteria("Part", "Scenario Name", "Contains", testScenarioName)
            .apply(ExplorePage.class);

        assertThat(explorePage.getNoComponentText(), is(containsString(noComponentMessage)));
    }
}