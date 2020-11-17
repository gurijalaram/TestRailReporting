package compare;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.AssemblyProcessGroupEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageobjects.common.ScenarioTablePage;
import pageobjects.pages.compare.ComparePage;
import pageobjects.pages.evaluate.EvaluatePage;
import pageobjects.pages.evaluate.PublishPage;
import pageobjects.pages.evaluate.PublishWarningPage;
import pageobjects.pages.explore.ExplorePage;
import pageobjects.pages.login.CidLoginPage;
import pageobjects.toolbars.GenericHeader;
import testsuites.suiteinterface.SanityTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class PublishComparisonTests extends TestBase {

    private final String noComponentMessage = "You have no components that match the selected filter";
    private final String cannotPublishPrivate = "To publish a comparison, all referenced scenarios must be public. Remove or replace any private scenarios.";
    private final String cannotPublishLock = "The published version of this scenario is locked. You cannot publish using the same scenario name. Enter a new name to continue.";
    private CidLoginPage loginPage;
    private ComparePage comparePage;
    private ExplorePage explorePage;
    private GenericHeader genericHeader;
    private PublishWarningPage publishWarningPage;

    private File resourceFile;

    public PublishComparisonTests() {
        super();
    }

    @Test
    @Issue("AP-61539")
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"421", "434"})
    @Description("Test a private comparison can be published from comparison page")
    public void testPublishComparisonComparePage() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Casting.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonName = new GenerateStringUtil().generateComparisonName();

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Part")
            .setRowOne("Part Name", "Contains", "Casting")
            .apply(ScenarioTablePage.class)
            .selectComparisonScenario(testScenarioName, "Casting")
            .apply(GenericHeader.class)
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Publish", "okay")
            .closeJobQueue(ExplorePage.class)
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
            .openComparison(testComparisonName);

        genericHeader = new GenericHeader(driver);
        comparePage = genericHeader.toggleLock()
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Update", "okay")
            .closeJobQueue(ComparePage.class);

        assertThat(comparePage.isComparisonLockStatus("lock"), is(true));

        genericHeader = new GenericHeader(driver);
        comparePage = genericHeader.toggleLock()
            .openJobQueue()
            .checkJobQueueRow("okay")
            .closeJobQueue(ComparePage.class);

        assertThat(comparePage.isComparisonLockStatus("unlock"), is(true));
    }


    @Test
    @Issue("AP-61539")
    @Category({SanityTests.class})
    @TestRail(testCaseId = {"421"})
    @Description("Test a private comparison can be published from explore page")
    public void testPublishComparisonExplorePage() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Casting.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonName = new GenerateStringUtil().generateComparisonName();

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Part")
            .setRowOne("Part Name", "Contains", "Casting")
            .apply(ScenarioTablePage.class)
            .selectComparisonScenario(testScenarioName, "CASTING")
            .apply(GenericHeader.class)
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Set Children to Comparison", "okay")
            .closeJobQueue(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.selectExploreButton()
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
            .highlightComparison(testComparisonName)
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Publish", "okay")
            .closeJobQueue(ExplorePage.class)
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Comparison")
            .setRowOne("Part Name", "Contains", testComparisonName)
            .apply(ExplorePage.class);

        assertThat(explorePage.getListOfComparisons(testComparisonName), is(equalTo(1)));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"422"})
    @Description("In comparison view, user can overwrite an existing public comparison via a publish of a private comparison of the same name")
    public void testOverwritePublicComparison() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum,"1027312-101-A1333.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonName = new GenerateStringUtil().generateComparisonName();
        String testComparisonDescription = "Test comparison description";
        String testAssemblyName = "1027312-101-A1333";
        String partName = "1027311-001";

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
                .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .costScenario()
                .publishScenario(PublishPage.class)
                .selectPublishButton()
                .createNewComparison()
                .enterComparisonName(testComparisonName)
                .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        comparePage = genericHeader.publishScenario(PublishPage.class)
                .selectPublishButton()
                .createNewComparison()
                .enterComparisonName(testComparisonName)
                .enterComparisonDescription(testComparisonDescription)
                .save(ComparePage.class)
                .addScenario()
                .filter()
                .setWorkspace("Public")
                .setScenarioType("Assembly")
                .setRowOne("Part Name", "Contains", testAssemblyName)
                .apply(ScenarioTablePage.class)
                .selectComparisonScenario(scenarioName, testAssemblyName)
                .apply(ComparePage.class)
                .addScenario()
                .filter()
                .setWorkspace("Public")
                .setScenarioType("Part")
                .setRowOne("Part Name", "Contains", partName)
                .apply(ScenarioTablePage.class)
                .selectComparisonScenario(scenarioName, partName)
                .apply(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.publishScenario(PublishWarningPage.class)
                .selectOverwriteOption()
                .selectContinueButton()
                .selectPublishButton()
                .filter()
                .setWorkspace("Public")
                .setScenarioType("Comparison")
                .apply(ScenarioTablePage.class)
                .highlightComparison(testComparisonName);

        new ExplorePage(driver).openPreviewPanel(ExplorePage.class);

        assertThat(explorePage.getDescriptionText(), is(equalTo(testComparisonDescription)));

        new ExplorePage(driver).filter()
                .setWorkspace("Private")
                .setScenarioType("Comparison")
                .setRowOne("Scenario Name", "Contains", testComparisonName)
                .apply(ExplorePage.class);

        assertThat(new ExplorePage(driver).getNoComponentText(), is(containsString(noComponentMessage)));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"423"})
    @Description("User has option to change the name of the comparison scenario to be published")
    public void testPublishComparisonWithNewScenario() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum,"1027312-101-A1333.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonName = new GenerateStringUtil().generateComparisonName();
        String newScenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonDescription = "Test comparison description";
        String testAssemblyName = "1027312-101-A1333";
        String partName = "1027311-001";

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
                .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .costScenario()
                .publishScenario(PublishPage.class)
                .selectPublishButton()
                .createNewComparison()
                .enterComparisonName(testComparisonName)
                .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        comparePage = genericHeader.publishScenario(PublishPage.class)
                .selectPublishButton()
                .createNewComparison()
                .enterComparisonName(testComparisonName)
                .enterComparisonDescription(testComparisonDescription)
                .save(ComparePage.class)
                .addScenario()
                .filter()
                .setWorkspace("Public")
                .setScenarioType("Assembly")
                .setRowOne("Part Name", "Contains", testAssemblyName)
                .apply(ScenarioTablePage.class)
                .selectComparisonScenario(scenarioName, testAssemblyName)
                .apply(ComparePage.class)
                .addScenario()
                .filter()
                .setWorkspace("Public")
                .setScenarioType("Part")
                .setRowOne("Part Name", "Contains", partName)
                .apply(ScenarioTablePage.class)
                .selectComparisonScenario(scenarioName, partName)
                .apply(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        comparePage = genericHeader.publishScenario(PublishWarningPage.class)
                .selectPublishAsNew()
                .enterNewScenarioName(newScenarioName)
                .selectContinueButton()
                .selectPublishButton()
                .filter()
                .setWorkspace("Public")
                .setScenarioType("Comparison")
                .setRowOne("Scenario Name","Contains", newScenarioName)
                .apply(ScenarioTablePage.class)
                .openComparison(testComparisonName);

        assertThat(comparePage.getComparisonName(), is(equalTo(testComparisonName.toUpperCase())));

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.selectExploreButton()
                .filter()
                .setWorkspace("Private")
                .setScenarioType("Comparison")
                .setRowOne("Scenario Name", "Contains", testComparisonName)
                .apply(ExplorePage.class);

        assertThat(new ExplorePage(driver).getNoComponentText(), is(containsString(noComponentMessage)));
    }

    @Test
    @TestRail(testCaseId = {"424"})
    @Description("Test all available characters in a comparison scenario name via publish dialog box")
    public void testPublishComparisonAllCharacters() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum,"1027312-101-A1333.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonName = new GenerateStringUtil().generateComparisonName();
        String newScenarioName = (new GenerateStringUtil().generateScenarioName() + "!£$%^&()_+{}~`1-=[]#';@");
        String testComparisonDescription = "Test comparison description";
        String testAssemblyName = "1027312-101-A1333";
        String partName = "1027311-001";

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
                .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .costScenario()
                .publishScenario(PublishPage.class)
                .selectPublishButton()
                .createNewComparison()
                .enterComparisonName(testComparisonName)
                .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        comparePage = genericHeader.publishScenario(PublishPage.class)
                .selectPublishButton()
                .createNewComparison()
                .enterComparisonName(testComparisonName)
                .enterComparisonDescription(testComparisonDescription)
                .save(ComparePage.class)
                .addScenario()
                .filter()
                .setWorkspace("Public")
                .setScenarioType("Assembly")
                .setRowOne("Part Name", "Contains", testAssemblyName)
                .apply(ScenarioTablePage.class)
                .selectComparisonScenario(scenarioName, testAssemblyName)
                .apply(ComparePage.class)
                .addScenario()
                .filter()
                .setWorkspace("Public")
                .setScenarioType("Part")
                .setRowOne("Part Name", "Contains", partName)
                .apply(ScenarioTablePage.class)
                .selectComparisonScenario(scenarioName, partName)
                .apply(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        comparePage = genericHeader.publishScenario(PublishWarningPage.class)
                .selectPublishAsNew()
                .enterNewScenarioName(newScenarioName)
                .selectContinueButton()
                .selectPublishButton()
                .filter()
                .setWorkspace("Public")
                .setScenarioType("Comparison")
                .setRowOne("Scenario Name","Contains", newScenarioName)
                .apply(ScenarioTablePage.class)
                .openComparison(testComparisonName);

        assertThat(comparePage.getComparisonName(), is(equalTo(testComparisonName.toUpperCase())));

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.selectExploreButton()
                .filter()
                .setWorkspace("Private")
                .setScenarioType("Comparison")
                .setRowOne("Scenario Name", "Contains", testComparisonName)
                .apply(ExplorePage.class);

        assertThat(new ExplorePage(driver).getNoComponentText(), is(containsString(noComponentMessage)));
    }

    @Test
    @TestRail(testCaseId = {"426"})
    @Description("Attempt to publish a comparison containing private scenarios")
    public void testAttemptPublishComparisonPrivateScenario() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "testpart-4.prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonName = new GenerateStringUtil().generateComparisonName();
        String partName = "testpart-4";

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
                .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .costScenario()
                .selectExploreButton()
                .createNewComparison()
                .enterComparisonName(testComparisonName)
                .save(ComparePage.class)
                .addScenario()
                .filter()
                .setWorkspace("Private")
                .setScenarioType("Part")
                .setRowOne("Part Name", "Contains", partName)
                .apply(ScenarioTablePage.class)
                .selectComparisonScenario(scenarioName, partName)
                .apply(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        publishWarningPage = genericHeader.publishScenario(PublishWarningPage.class);

        assertThat(new PublishWarningPage(driver).getCannotPublishDuePrivateScenarioText(), is(containsString(cannotPublishPrivate)));
    }

    @Test
    @TestRail(testCaseId = {"425"})
    @Description("Attempt to publish overwrite a locked public comparison")
    public void testPublishOverwriteLockedComparison() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum,"1027312-101-A1333.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonName = new GenerateStringUtil().generateComparisonName();
        String testAssemblyName = "1027312-101-A1333";
        String partName = "1027311-001";

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
                .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .costScenario()
                .publishScenario(PublishPage.class)
                .selectPublishButton()
                .createNewComparison()
                .enterComparisonName(testComparisonName)
                .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        comparePage = genericHeader.publishScenario(PublishPage.class)
                .selectPublishButton()
                .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
                .highlightComparison(testComparisonName)
                .toggleLock()
                .openJobQueue()
                .checkJobQueueActionStatus(testComparisonName, "Initial", "Update", "okay")
                .closeJobQueue(ExplorePage.class)
                .createNewComparison()
                .enterComparisonName(testComparisonName)
                .save(ComparePage.class)
                .addScenario()
                .filter()
                .setWorkspace("Public")
                .setScenarioType("Assembly")
                .setRowOne("Part Name", "Contains", testAssemblyName)
                .apply(ScenarioTablePage.class)
                .selectComparisonScenario(scenarioName, testAssemblyName)
                .apply(ComparePage.class)
                .addScenario()
                .filter()
                .setWorkspace("Public")
                .setScenarioType("Part")
                .setRowOne("Part Name", "Contains", partName)
                .apply(ScenarioTablePage.class)
                .selectComparisonScenario(scenarioName, partName)
                .apply(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        publishWarningPage = genericHeader.publishScenario(PublishWarningPage.class);

        assertThat(new PublishWarningPage(driver).getCannotPublishDueLockedComparisonText(), is(containsString(cannotPublishLock)));
    }
}