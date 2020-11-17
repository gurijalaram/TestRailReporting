package compare;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageobjects.common.ScenarioTablePage;
import pageobjects.pages.compare.ComparePage;
import pageobjects.pages.evaluate.EditWarningPage;
import pageobjects.pages.evaluate.EvaluatePage;
import pageobjects.pages.evaluate.PublishPage;
import pageobjects.pages.evaluate.PublishWarningPage;
import pageobjects.pages.explore.ComparisonPage;
import pageobjects.pages.explore.ExplorePage;
import pageobjects.pages.explore.PreviewPanelPage;
import pageobjects.pages.login.CidLoginPage;
import pageobjects.toolbars.GenericHeader;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class EditPublicComparisonTests extends TestBase {

    private CidLoginPage loginPage;
    private ExplorePage explorePage;
    private ComparePage comparePage;
    private GenericHeader genericHeader;
    private EvaluatePage evaluatePage;
    private EditWarningPage editWarningPage;

    private File resourceFile;

    public EditPublicComparisonTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"421"})
    @Description("Test publishing a comparison shows the comparison in the comparison table")
    public void testPublishComparison() {

        String testComparisonName = new GenerateStringUtil().generateComparisonName();

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
                .createNewComparison()
                .enterComparisonName(testComparisonName)
                .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);

        explorePage = genericHeader.publishScenario(PublishPage.class)
                .selectPublishButton()
                .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace());

        assertThat(explorePage.findComparison(testComparisonName).isDisplayed(), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"427"})
    @Description("Test editing a published comparison shows the comparison view")
    public void testEditPublicComparison() {

        String testComparisonName = new GenerateStringUtil().generateComparisonName();

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
                .createNewComparison()
                .enterComparisonName(testComparisonName)
                .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);

        explorePage = genericHeader.publishScenario(PublishPage.class)
                .selectPublishButton()
                .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
                .highlightComparison(testComparisonName);

        genericHeader = new GenericHeader(driver);
        comparePage = genericHeader.editScenario(ComparePage.class);

        assertThat(comparePage.getComparisonName(), is(equalTo(testComparisonName.toUpperCase())));
    }

    @Test
    @TestRail(testCaseId = {"428"})
    @Description("Private comparison can be overwritten by public comparison with the same name")
    public void testOverwritePrivateComparison() {

        String testComparisonName = new GenerateStringUtil().generateComparisonName();
        String testComparisonDescription = "Test comparison description";

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
                .createNewComparison()
                .enterComparisonName(testComparisonName)
                .enterComparisonDescription(testComparisonDescription)
                .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        comparePage = genericHeader.publishScenario(PublishPage.class)
                .selectPublishButton()
                .createNewComparison()
                .enterComparisonName(testComparisonName)
                .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.selectExploreButton()
                .filter()
                .setWorkspace("Public")
                .setScenarioType("Comparison")
                .apply(ScenarioTablePage.class)
                .highlightComparison(testComparisonName);

        genericHeader = new GenericHeader(driver);
        comparePage = genericHeader.editScenario(EditWarningPage.class)
                .selectOverwrite()
                .selectContinue(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.selectExploreButton()
                .filter()
                .setWorkspace("Private")
                .setScenarioType("Comparison")
                .apply(ScenarioTablePage.class)
                .highlightComparison(testComparisonName)
                .openPreviewPanel(ExplorePage.class);

        assertThat(explorePage.getDescriptionText(), is(equalTo(testComparisonDescription)));
    }

    @Test
    @Issue("AP-61539")
    @TestRail(testCaseId = {"458"})
    @Description("Delete private scenarios included in comparison from private workspace")
    public void testRemoveFromComparison() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "PowderMetalShaft.stp");
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
                .setRowOne("Part Name", "Contains", "PowderMetalShaft")
                .apply(ScenarioTablePage.class)
                .selectComparisonScenario(testScenarioName, "PowderMetalShaft")
                .apply(ComparePage.class)
                .removeScenarioFromCompareView("PowderMetalShaft", testScenarioName);

        assertThat(comparePage.scenarioIsNotInComparisonView(testScenarioName, "PowderMetalShaft"), is(true));
    }

    @Test
    @Issue("BA-999")
    @Issue("AP-61539")
    @TestRail(testCaseId = {"2279"})
    @Description("Test you can change the basis of your comparison")
    public void testChangeComparisonBasis() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Assembly2.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonName = new GenerateStringUtil().generateComparisonName();
        String testAssemblyName = "Assembly2";
        String partName = "PART0001";

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
                .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
                .selectProcessGroup(processGroupEnum.getProcessGroup())
                .costScenario()
                .selectExploreButton()
                .selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace())
                .openScenario(scenarioName, partName)
                .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
                .costScenario()
                .createNewComparison()
                .enterComparisonName(testComparisonName)
                .save(ComparePage.class)
                .addScenario()
                .filter()
                .setWorkspace("Private")
                .setScenarioType("Assembly")
                .setRowOne("Part Name", "Contains", testAssemblyName)
                .apply(ScenarioTablePage.class)
                .selectComparisonScenario(scenarioName, testAssemblyName)
                .apply(ComparePage.class);

        new ComparePage(driver).addScenario()
                .filter()
                .setWorkspace("Private")
                .setScenarioType("Part")
                .setRowOne("Part Name", "Contains", partName)
                .apply(ScenarioTablePage.class)
                .selectComparisonScenario(scenarioName, partName)
                .apply(ComparePage.class);

        new ComparePage(driver).setBasis(partName, scenarioName);

        assertThat(new ComparePage(driver).isBasis(partName, scenarioName), is(true));
        assertThat(new ComparePage(driver).isBasisButtonPresent(testAssemblyName, scenarioName), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"435"})
    @Description("In explore view, the user can lock and unlock the currently open public comparison")
    public void testLockUnlockPublishComparison() {

        String testComparisonName = new GenerateStringUtil().generateComparisonName();

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
                .createNewComparison()
                .enterComparisonName(testComparisonName)
                .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.publishScenario(PublishPage.class)
                .selectPublishButton()
                .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
                .highlightComparison(testComparisonName)
                .toggleLock()
                .openJobQueue()
                .checkJobQueueActionStatus(testComparisonName, "Initial", "Update", "okay")
                .closeJobQueue(ExplorePage.class);

        new ScenarioTablePage(driver).openComparison(testComparisonName);

        assertThat(comparePage.isComparisonLockStatus("lock"), is(true));

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.selectExploreButton()
                .filter()
                .setWorkspace("Public")
                .setScenarioType("Comparison")
                .apply(ScenarioTablePage.class)
                .highlightComparison(testComparisonName)
                .toggleLock()
                .openJobQueue()
                .checkJobQueueRow("okay")
                .closeJobQueue(ExplorePage.class);

        new ScenarioTablePage(driver).openComparison(testComparisonName);

        assertThat(comparePage.isComparisonLockStatus("unlock"), is(true));
    }

    @Test
    @TestRail(testCaseId = {"429"})
    @Description("Public comparison can be edited and given a new unique name if a private comparison of the same name already exists")
    public void testEditComparisonAsNewScenario() {

        String testComparisonName = new GenerateStringUtil().generateComparisonName();
        String testComparisonDescription = "Test comparison description";
        String newScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
                .createNewComparison()
                .enterComparisonName(testComparisonName)
                .enterComparisonDescription(testComparisonDescription)
                .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        comparePage = genericHeader.publishScenario(PublishPage.class)
                .selectPublishButton()
                .createNewComparison()
                .enterComparisonName(testComparisonName)
                .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.selectExploreButton()
                .filter()
                .setWorkspace("Public")
                .setScenarioType("Comparison")
                .apply(ScenarioTablePage.class)
                .highlightComparison(testComparisonName);

        genericHeader = new GenericHeader(driver);
        comparePage = genericHeader.editScenario(EditWarningPage.class)
                .selectSaveAsNew()
                .inputNewScenarioName(newScenarioName)
                .selectContinue(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.selectExploreButton()
                .filter()
                .setWorkspace("Private")
                .setScenarioType("Comparison")
                .setRowOne("Scenario Name", "Contains", newScenarioName)
                .apply(ScenarioTablePage.class)
                .highlightComparison(testComparisonName)
                .openPreviewPanel(ExplorePage.class);

        assertThat(explorePage.getDescriptionText(), is(equalTo(testComparisonDescription)));
    }
}