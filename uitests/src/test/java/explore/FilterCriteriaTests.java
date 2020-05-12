package explore;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.PublishPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.pageobjects.toolbars.GenericHeader;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class FilterCriteriaTests extends TestBase {

    private CIDLoginPage loginPage;
    private ExplorePage explorePage;
    private GenericHeader genericHeader;

    private File resourceFile;

    public FilterCriteriaTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"2276"})
    @Description("Test private criteria part")
    public void testPrivateCriteriaPart() {

        resourceFile = new FileResourceUtil().getResourceFile("SheetMetal.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CIDLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, resourceFile)
            .selectExploreButton();

        explorePage = new ExplorePage(driver);
        explorePage.filterCriteria()
            .filterPrivateCriteria("Part", "Part Name", "Contains", "SheetMetal")
            .apply(ExplorePage.class);

        Assert.assertThat(explorePage.getListOfScenarios(testScenarioName, "SheetMetal"), is(equalTo(1)));
    }

    @Test
    @Description("Test private criteria attribute")
    public void testPrivateCriteriaAttribute() {

        resourceFile = new FileResourceUtil().getResourceFile("Casting.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CIDLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, resourceFile)
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
    @Description("Test private criteria part contains")
    public void testPrivateCriteriaContains() {

        resourceFile = new FileResourceUtil().getResourceFile("CurvedWall.CATPart");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CIDLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, resourceFile)
            .selectExploreButton();

        explorePage = new ExplorePage(driver);
        explorePage.filterCriteria()
            .filterPrivateCriteria("Part", "Part Name", "Contains", "Wall")
            .apply(ExplorePage.class);

        Assert.assertThat(explorePage.getListOfScenarios(testScenarioName, "CurvedWall"), is(equalTo(1)));
    }

    @Test
    @Description("Test private criteria assembly")
    public void testPrivateCriteriaAssembly() {

        resourceFile = new FileResourceUtil().getResourceFile("Piston_assembly.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CIDLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, resourceFile)
            .selectExploreButton();

        explorePage = new ExplorePage(driver);
        explorePage.filterCriteria()
            .filterPrivateCriteria("Assembly", "Part Name", "Contains", "Piston_assembly")
            .apply(ExplorePage.class);

        Assert.assertThat(explorePage.getListOfAssemblies(testScenarioName, "Piston_assembly"), is(equalTo(1)));
    }

    @Test
    @Description("Test private criteria assembly status")
    public void testPublicCriteriaAssemblyStatus() {

        resourceFile = new FileResourceUtil().getResourceFile("Piston_assembly.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CIDLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, resourceFile)
            .selectScenarioInfoNotes()
            .enterScenarioInfoNotes("Analysis", "High", "Test Description", "Test Notes")
            .save(EvaluatePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus("piston_assembly", testScenarioName, "Update", "okay")
            .closeJobQueue(EvaluatePage.class)
            .publishScenario(PublishPage.class)
            .selectPublishButton();

        explorePage = new ExplorePage(driver);
        explorePage.filterCriteria()
            .filterPublicCriteria("Assembly", "Status", "is", "Analysis")
            .apply(ExplorePage.class);

        Assert.assertThat(explorePage.getListOfAssemblies(testScenarioName, "Piston_assembly"), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"2277"})
    @Description("Test public criteria part")
    public void testPublicCriteriaPart() {

        resourceFile = new FileResourceUtil().getResourceFile("Push Pin.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CIDLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, resourceFile)
            .publishScenario(PublishPage.class)
            .selectPublishButton();

        explorePage = new ExplorePage(driver);
        explorePage.filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", "Push Pin")
            .apply(ExplorePage.class);

        Assert.assertThat(explorePage.getListOfScenarios(testScenarioName, "Push Pin"), is(equalTo(1)));
    }

    @Test
    @Description("Test public criteria assembly description")
    public void testPublicCriteriaAssemblyDesc() {

        resourceFile = new FileResourceUtil().getResourceFile("Piston_assembly.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CIDLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, resourceFile)
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

        Assert.assertThat(explorePage.getListOfAssemblies(testScenarioName, "Piston_assembly"), is(equalTo(1)));
    }

    @Test
    @Description("Test public criteria comparison")
    public void testPublicCriteriaComparison() {

        String testComparisonName = new GenerateStringUtil().generateScenarioName();

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

        Assert.assertThat(explorePage.getListOfComparisons(testComparisonName), is(equalTo(1)));
    }
}