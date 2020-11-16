package com.compare;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.pageobjects.common.ScenarioTablePage;
import com.pageobjects.pages.compare.ComparePage;
import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.pages.evaluate.PublishPage;
import com.pageobjects.pages.explore.ExplorePage;
import com.pageobjects.pages.login.CidLoginPage;
import com.pageobjects.toolbars.GenericHeader;
import com.testsuites.suiteinterface.SmokeTests;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;


public class DeleteComparisonTests extends TestBase {

    private final String noComponentMessage = "You have no components that match the selected filter";
    private CidLoginPage loginPage;
    private ExplorePage explorePage;
    private ComparePage comparePage;
    private GenericHeader genericHeader;
    private ScenarioTablePage scenarioTablePage;
    private EvaluatePage evaluatePage;

    private File resourceFile;

    public DeleteComparisonTests() {
        super();
    }

    @Test
    @Issue("AP-61539")
    @TestRail(testCaseId = {"433", "2280", "2319"})
    @Description("Test a private comparison can be deleted from the explore page")
    public void testDeletePrivateComparisonExplore() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Machined Box AMERICAS.SLDPRT");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonName = new GenerateStringUtil().generateComparisonName();

        new CidLoginPage(driver).login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
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
            .setRowOne("Part Name", "Contains", "Machined Box AMERICAS")
            .apply(ScenarioTablePage.class);

        scenarioTablePage = new ScenarioTablePage(driver);

        new ScenarioTablePage(driver).selectComparisonScenario(testScenarioName, "Machined Box AMERICAS")
            .apply(GenericHeader.class)
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Set Children to Comparison", "okay")
            .closeJobQueue(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.selectExploreButton()
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
            .highlightComparison(testComparisonName)
            .delete()
            .deleteExploreComparison()
            .filter()
            .setWorkspace("Private")
            .setScenarioType("Comparison")
            .setRowOne("Scenario Name", "Contains", testComparisonName)
            .apply(ExplorePage.class);

        assertThat(new ExplorePage(driver).getNoComponentText(), is(containsString(noComponentMessage)));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"430"})
    @Description("Test a private comparison can be deleted from the comparison page")
    public void testDeletePrivateComparison() {

        String testComparisonName = new GenerateStringUtil().generateComparisonName();

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.delete()
            .deleteComparison()
            .filter()
            .setWorkspace("Private")
            .setScenarioType("Part")
            .setRowOne("Scenario Name", "Contains", testComparisonName)
            .apply(ExplorePage.class);

        assertThat(explorePage.getNoComponentText(), is(containsString(noComponentMessage)));
    }

    @Test
    @Issue("AP-60336")
    @Issue("AP-61539")
    @Category({SmokeTests.class})
    @TestRail(testCaseId = {"3838", "430", "432", "442", "448"})
    @Description("Test deleting a public comparison from explore tab")
    public void testPublicComparisonDeleteExplore() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Machined Box AMERICAS.SLDPRT");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonName = new GenerateStringUtil().generateComparisonName();

        loginPage = new CidLoginPage(driver);

        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isDFMRiskIcon("dtc-critical-risk-icon"), is(true));
        assertThat(evaluatePage.isDfmRisk("Critical"), is(true));

        explorePage = evaluatePage.publishScenario(PublishPage.class)
            .selectPublishButton()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Part")
            .setRowOne("Part Name", "Contains", "Machined Box AMERICAS")
            .apply(ScenarioTablePage.class)
            .selectComparisonScenario(testScenarioName, "MACHINED BOX AMERICAS")
            .apply(GenericHeader.class)
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
            .highlightComparison(testComparisonName)
            .delete()
            .deleteExploreComparison()
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Delete", "okay")
            .closeJobQueue(ExplorePage.class)
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Comparison")
            .setRowOne("Part Name", "Contains", testComparisonName)
            .apply(ExplorePage.class);

        assertThat(explorePage.getNoComponentText(), is(containsString(noComponentMessage)));
    }

    @Test
    @Issue("AP-60336")
    @Issue("AP-61539")
    @Category({SmokeTests.class})
    @TestRail(testCaseId = {"443"})
    @Description("Delete a public comparison from comparison page")
    public void deletePublicComparisonPage() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "testpart-4.prt");
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
            .setRowOne("Part Name", "Contains", "testpart-4")
            .apply(ScenarioTablePage.class)
            .selectComparisonScenario(testScenarioName, "testpart-4")
            .apply(GenericHeader.class)
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Publish", "okay")
            .closeJobQueue(ExplorePage.class)
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Comparison")
            .setRowOne("Part Name", "Contains", testComparisonName)
            .apply(ExplorePage.class)
            .openComparison(testComparisonName);

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.delete()
            .deleteComparison()
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Comparison")
            .setRowOne("Scenario Name", "Contains", testComparisonName)
            .apply(ExplorePage.class);

        assertThat(explorePage.getNoComponentText(), is(containsString(noComponentMessage)));
    }

    @Test
    @TestRail(testCaseId = {"431"})
    @Description("In comparison view, the user can delete the currently open comparison and any matching public or private comparisons")
    public void deletePublicPrivateComparison() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "testpart-4.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonName = new GenerateStringUtil().generateComparisonName();

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
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
            .setRowOne("Part Name", "Contains", "testpart-4")
            .apply(ScenarioTablePage.class)
            .selectComparisonScenario(testScenarioName, "testpart-4")
            .apply(GenericHeader.class)
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Publish", "okay")
            .closeJobQueue(ExplorePage.class)
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
            .highlightComparison(testComparisonName)
            .editScenario(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        explorePage = genericHeader.delete()
            .selectIterationsCheckbox()
            .deleteComparison()
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Delete", "okay")
            .closeJobQueue(ExplorePage.class)
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Comparison")
            .setRowOne("Part Name", "Contains", testComparisonName)
            .apply(ExplorePage.class);

        assertThat(explorePage.getNoComponentText(), containsString("You have no components that match the selected filter"));
    }
}