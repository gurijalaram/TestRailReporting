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
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class DeletePrivateScenarioTests extends TestBase {

    private CIDLoginPage loginPage;
    private ExplorePage explorePage;

    private File resourceFile;
    private final String noComponentMessage = "You have no components that match the selected filter";

    public DeletePrivateScenarioTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"393", "587"})
    @Description("Test a private scenario can be deleted from the component table")
    public void testDeletePrivateScenario() {

        resourceFile = new FileResourceUtil().getResourceFile("casting.prt");
        String testScenarioName = new Util().generateScenarioName();

        loginPage = new CIDLoginPage(driver);
        loginPage.login(UserUtil.getUser());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFile(testScenarioName, resourceFile)
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