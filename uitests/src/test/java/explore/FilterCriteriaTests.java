package explore;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.header.GenericHeader;
import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.PublishPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Assert;
import org.junit.Test;

public class FilterCriteriaTests extends TestBase {

    private CIDLoginPage loginPage;
    private ExplorePage explorePage;
    private PublishPage publishPage;
    private GenericHeader genericHeader;

    public FilterCriteriaTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"2276"})
    @Issue("AP-56845")
    @Description("Test private criteria part")
    public void testPrivateCriteriaPart() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new CIDLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("SheetMetal.prt"))
            .selectExploreButton();

        explorePage = new ExplorePage(driver);
        explorePage.filterCriteria()
            .filterPrivateCriteria("Part", "Part Name", "Contains", "SheetMetal")
            .apply(ExplorePage.class);

        Assert.assertThat(explorePage.getListOfScenarios(testScenarioName, "SheetMetal"), is(equalTo(1)));
    }

    @Test
    @Issue("AP-56845")
    @Description("Test private criteria attribute")
    public void testPrivateCriteriaAttribute() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new CIDLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("Casting.prt"))
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .selectExploreButton();

        explorePage = new ExplorePage(driver);
        explorePage.filterCriteria()
            .filterPrivateCriteria("Part", "Process Group", "is", "Casting - Die")
            .apply(ExplorePage.class);

        Assert.assertThat(explorePage.getListOfScenarios(testScenarioName, "Casting"), is(equalTo(1)));
    }

    @Test
    @Issue("AP-56845")
    @Description("Test private criteria part contains")
    public void testPrivateCriteriaContains() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new CIDLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("CurvedWall.CATPart"))
            .selectExploreButton();

        explorePage = new ExplorePage(driver);
        explorePage.filterCriteria()
            .filterPrivateCriteria("Part", "Part Name", "Contains", "Wall")
            .apply(ExplorePage.class);

        Assert.assertThat(explorePage.getListOfScenarios(testScenarioName, "CurvedWall"), is(equalTo(1)));
    }

    @Test
    @Issue("AP-56845")
    @Description("Test private criteria assembly")
    public void testPrivateCriteriaAssembly() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new CIDLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("Piston_assembly.stp"))
            .selectExploreButton();

        explorePage = new ExplorePage(driver);
        explorePage.filterCriteria()
            .filterPrivateCriteria("Assembly", "Part Name", "Contains", "Piston_assembly")
            .apply(ExplorePage.class);

        Assert.assertThat(explorePage.getListOfScenarios(testScenarioName, "Piston_assembly"), is(equalTo(1)));
    }

    @Test
    @Issue("AP-56845")
    @Description("Test private criteria assembly status")
    public void testPrivateCriteriaAssemblyStatus() {
        loginPage = new CIDLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .filterCriteria()
            .filterPrivateCriteria("Assembly", "Status", "is", "clear selections")
            .apply(ExplorePage.class);
        //Assert.assertTrue();
    }

    @Test
    @TestRail(testCaseId = {"2277"})
    @Issue("AP-56845")
    @Description("Test public criteria part")
    public void testPublicCriteriaPart() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new CIDLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("Push Pin.stp"))
            .publishScenario(PublishPage.class)
            .selectPublishButton();

        explorePage = new ExplorePage(driver);
        explorePage.filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", "Push Pin")
            .apply(ExplorePage.class);

        Assert.assertThat(explorePage.getListOfScenarios(testScenarioName, "Push Pin"), is(equalTo(1)));
    }

    @Test
    @Issue("AP-56845")
    @Description("Test public criteria assembly description")
    public void testPublicCriteriaAssemblyDesc() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new CIDLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("Piston_assembly.stp"))
            .selectScenarioInfoNotes()
            .enterScenarioInfoNotes("Complete", "High", "Test Description", "Test Notes")
            .save(EvaluatePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus("piston_assembly", testScenarioName, "Update", "okay")
            .closeJobQueue(EvaluatePage.class)
            .publishScenario(PublishPage.class)
            .selectPublishButton();

        explorePage = new ExplorePage(driver);
        explorePage.filterCriteria()
            .filterPublicCriteria("Assembly", "Description", "Contains", "Test Description")
            .apply(ExplorePage.class);

        Assert.assertThat(explorePage.getListOfScenarios(testScenarioName, "Piston_assembly"), is(equalTo(1)));
    }

    @Test
    @Issue("AP-56845")
    @Description("Test public criteria comparison")
    public void testPublicCriteriaComparison() {

        String testComparisonName = new Util().getScenarioName();

        loginPage = new CIDLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        genericHeader.publishScenario(PublishPage.class)
            .selectPublishButton();

        explorePage = new ExplorePage(driver);
        explorePage.filterCriteria()
            .filterPublicCriteria("Comparison", "Part Name", "Contains", testComparisonName)
            .apply(ExplorePage.class);

        Assert.assertThat(explorePage.getListOfScenarios("Initial", testComparisonName), is(equalTo(1)));
    }
}