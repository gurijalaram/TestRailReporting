package explore;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Assert;
import org.junit.Test;
import pageobjects.pages.compare.ComparePage;
import pageobjects.pages.evaluate.EvaluatePage;
import pageobjects.pages.evaluate.PublishPage;
import pageobjects.pages.explore.ExplorePage;
import pageobjects.pages.login.CidLoginPage;
import pageobjects.toolbars.GenericHeader;

import java.io.File;

public class FilterCriteriaTests extends TestBase {

    private CidLoginPage loginPage;
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

        resourceFile = FileResourceUtil.getResourceAsFile("SheetMetal.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectExploreButton();

        explorePage = new ExplorePage(driver);
        explorePage.filter()
            .setWorkspace("Private")
            .setScenarioType("Part")
            .setRowOne("Part Name", "Contains", "SheetMetal")
            .apply(ExplorePage.class);

        Assert.assertThat(explorePage.getListOfScenarios(testScenarioName, "SheetMetal"), is(equalTo(1)));
    }

    @Test
    @Description("Test private criteria attribute")
    public void testPrivateCriteriaAttribute() {

        resourceFile = FileResourceUtil.getResourceAsFile("Casting.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .selectExploreButton();

        explorePage = new ExplorePage(driver);
        explorePage.filter()
            .setWorkspace("Private")
            .setScenarioType("Part")
            .setRowOne("Process Group", "is", "Casting - Die")
            .apply(ExplorePage.class);

        Assert.assertThat(explorePage.getListOfScenarios(testScenarioName, "Casting"), is(equalTo(1)));
    }

    @Test
    @Description("Test private criteria part contains")
    public void testPrivateCriteriaContains() {

        resourceFile = FileResourceUtil.getResourceAsFile("CurvedWall.CATPart");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectExploreButton();

        explorePage = new ExplorePage(driver);
        explorePage.filter()
            .setWorkspace("Private")
            .setScenarioType("Part")
            .setRowOne("Part Name", "Contains", "Wall")
            .apply(ExplorePage.class);

        Assert.assertThat(explorePage.getListOfScenarios(testScenarioName, "CurvedWall"), is(equalTo(1)));
    }

    @Test
    @Description("Test private criteria assembly")
    public void testPrivateCriteriaAssembly() {

        resourceFile = FileResourceUtil.getResourceAsFile("Piston_assembly.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectExploreButton();

        explorePage = new ExplorePage(driver);
        explorePage.filter()
            .setWorkspace("Private")
            .setScenarioType("Assembly")
            .setRowOne("Part Name", "Contains", "Piston_assembly")
            .apply(ExplorePage.class);

        Assert.assertThat(explorePage.getListOfAssemblies(testScenarioName, "Piston_assembly"), is(equalTo(1)));
    }

    @Test
    @Description("Test private criteria assembly status")
    public void testPublicCriteriaAssemblyStatus() {

        resourceFile = FileResourceUtil.getResourceAsFile("Piston_assembly.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectScenarioInfoNotes()
            .enterScenarioInfoNotes("Analysis", "High", "Test Description", "Test Notes")
            .save(EvaluatePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus("piston_assembly", testScenarioName, "Update", "okay")
            .closeJobQueue(EvaluatePage.class)
            .publishScenario(PublishPage.class)
            .selectPublishButton();

        explorePage = new ExplorePage(driver);
        explorePage.filter()
            .setWorkspace("Public")
            .setScenarioType("Assembly")
            .setRowOne("Status", "is", "Analysis")
            .apply(ExplorePage.class);

        Assert.assertThat(explorePage.getListOfAssemblies(testScenarioName, "Piston_assembly"), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"2277"})
    @Description("Test public criteria part")
    public void testPublicCriteriaPart() {

        resourceFile = FileResourceUtil.getResourceAsFile("Push Pin.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .publishScenario(PublishPage.class)
            .selectPublishButton();

        explorePage = new ExplorePage(driver);
        explorePage.filter()
            .setWorkspace("Public")
            .setScenarioType("Part")
            .setRowOne("Part Name", "Contains", "Push Pin")
            .apply(ExplorePage.class);

        Assert.assertThat(explorePage.getListOfScenarios(testScenarioName, "Push Pin"), is(equalTo(1)));
    }

    @Test
    @Description("Test public criteria assembly description")
    public void testPublicCriteriaAssemblyDesc() {

        resourceFile = FileResourceUtil.getResourceAsFile("Piston_assembly.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectScenarioInfoNotes()
            .enterScenarioInfoNotes("Complete", "High", "Test Description", "Test Notes")
            .save(EvaluatePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus("piston_assembly", testScenarioName, "Update", "okay")
            .closeJobQueue(EvaluatePage.class)
            .publishScenario(PublishPage.class)
            .selectPublishButton();

        explorePage = new ExplorePage(driver);
        explorePage.filter()
            .setWorkspace("Public")
            .setScenarioType("Assembly")
            .setRowOne("Description", "Contains", "Test Description")
            .apply(ExplorePage.class);

        Assert.assertThat(explorePage.getListOfAssemblies(testScenarioName, "Piston_assembly"), is(equalTo(1)));
    }

    @Test
    @Description("Test public criteria comparison")
    public void testPublicCriteriaComparison() {

        String testComparisonName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        genericHeader.publishScenario(PublishPage.class)
            .selectPublishButton();

        explorePage = new ExplorePage(driver);
        explorePage.filter()
            .setWorkspace("Public")
            .setScenarioType("Comparison")
            .setRowOne("Part Name", "Contains", testComparisonName)
            .apply(ExplorePage.class);

        Assert.assertThat(explorePage.getListOfComparisons(testComparisonName), is(equalTo(1)));
    }

    @Test
    @Description("Test public criteria assembly description")
    public void testFilterAttributes() {

        resourceFile = FileResourceUtil.getResourceAsFile("PowderMetalShaft.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .publishScenario("Analysis", "Initial", "Ciene Frith")
            .selectLock()
            .selectPublishButton();

        explorePage = new ExplorePage(driver);
        explorePage.filter()
            .setWorkspace("Public, Private")
            .setScenarioType("Part")
            .setRowOne("Status", "is", "Analysis")
            .setRowTwo("Cost Maturity", "is", "Initial")
            .setRowThree("Assignee", "is", "Ciene Frith")
            .apply(ExplorePage.class);

        Assert.assertThat(explorePage.getListOfScenarios(testScenarioName, "PowderMetalShaft"), is(equalTo(1)));
    }
}