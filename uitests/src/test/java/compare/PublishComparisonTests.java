package compare;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.header.GenericHeader;
import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.compare.ComparisonTablePage;
import com.apriori.pageobjects.pages.evaluate.PublishPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;

public class PublishComparisonTests extends TestBase {

    private LoginPage loginPage;
    private ComparePage comparePage;
    private ExplorePage explorePage;
    private GenericHeader genericHeader;

    public PublishComparisonTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"421"})
    @Issue("AP-56845")
    @Description("Test a private comparison can be published from comparison page")
    public void testPublishComparisonComparePage() {

        String testScenarioName = new Util().getScenarioName();
        String testComparisonName = new Util().getComparisonName();

        loginPage = new LoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("Casting.prt"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", "Casting")
            .apply(ComparisonTablePage.class)
            .selectScenario(testScenarioName, "Casting")
            .apply();

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.publishScenario(PublishPage.class)
            .selectPublishButton()
            .filterCriteria()
            .filterPublicCriteria("Comparison", "Part Name", "Contains", testComparisonName)
            .apply(ExplorePage.class);

        assertThat(explorePage.getListOfComparisons(testComparisonName), is(equalTo(1)));
    }


    @Test
    @TestRail(testCaseId = {"421"})
    @Issue("AP-56845")
    @Description("Test a private comparison can be published from explore page")
    public void testPublishComparisonExplorePage() {

        String testScenarioName = new Util().getScenarioName();
        String testComparisonName = new Util().getComparisonName();

        loginPage = new LoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("Casting.prt"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", "Casting")
            .apply(ComparisonTablePage.class)
            .selectScenario(testScenarioName, "CASTING")
            .apply();

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.selectExploreButton()
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
            .highlightComparison(testComparisonName)
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .openJobQueue()
            .checkJobQueueActionStatus("Initial", "Publish", "okay")
            .closeJobQueue(ExplorePage.class)
            .filterCriteria()
            .filterPublicCriteria("Comparison", "Part Name", "Contains", testComparisonName)
            .apply(ExplorePage.class);

        assertThat(explorePage.getListOfComparisons(testComparisonName), is(equalTo(1)));
    }
}