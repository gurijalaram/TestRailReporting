package com.explore;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class FilterCriteriaTests extends TestBase {

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    //private ComparePage comparePage;

    private File resourceFile;

    public FilterCriteriaTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"6213"})
    @Description("Test private criteria part")
    public void testPrivateCriteriaPart() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "SheetMetal.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .clickExplore()
            .filter()
            .inputCurrentFilter("Private")
            .saveAs()
            .inputName("Automation")
            .addCriteria("Component Name", "Contains", "SheetMetal")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(testScenarioName, "SheetMetal"), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = "6214")
    @Description("Test private criteria attribute")
    public void testPrivateCriteriaAttribute() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Casting.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .clickExplore()
            .filter()
            .inputCurrentFilter("Private")
            .saveAs()
            .inputName("Automation")
            .addCriteria("Process Group", "is", ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(testScenarioName, "Casting"), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = "6215")
    @Description("Test private criteria part contains")
    public void testPrivateCriteriaContains() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "CurvedWall.CATPart");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .clickExplore()
            .filter()
            .inputCurrentFilter("Private")
            .saveAs()
            .inputName("Automation")
            .addCriteria("Part Name", "Contains", "Wall")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(testScenarioName, "CurvedWall"), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = "6216")
    @Description("Test private criteria assembly")
    public void testPrivateCriteriaAssembly() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Piston_assembly.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .clickExplore()
            .filter()
            .inputCurrentFilter("Private")
            .saveAs()
            .inputName("Automation")
            .addCriteria("Component Name", "Contains", "Piston_assembly")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(testScenarioName, "Piston_assembly"), is(equalTo(1)));
    }

    @Ignore
    @Test
    @TestRail(testCaseId = "6217")
    @Description("Test private criteria assembly status")
    public void testPublicCriteriaAssemblyStatus() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Piston_assembly.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        /*explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .selectScenarioInfoNotes()
            .enterScenarioInfoNotes("Analysis", "High", "Test Description", "Test Notes")
            .save(EvaluatePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus("piston_assembly", testScenarioName, "Update", "okay")
            .closeJobQueue(EvaluatePage.class)
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Assembly")
            .setRowOne("Status", "is", "Analysis")
            .apply(ExplorePage.class);

        assertThat(explorePage.getListOfAssemblies(testScenarioName, "Piston_assembly"), is(equalTo(1)));*/
    }

    @Test
    @TestRail(testCaseId = {"6218"})
    @Description("Test public criteria part")
    public void testPublicCriteriaPart() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Push Pin.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .publishScenario()
            .publish(ExplorePage.class)
            .filter()
            .inputCurrentFilter("Public")
            .saveAs()
            .inputName("Automation")
            .addCriteria("Component Name", "Contains", "Push Pin")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(testScenarioName, "Push Pin"), is(equalTo(1)));
    }

    @Ignore
    @Test
    @TestRail(testCaseId = "6219")
    @Description("Test public criteria assembly description")
    public void testPublicCriteriaAssemblyDesc() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Piston_assembly.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        /*loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .selectScenarioInfoNotes()
            .enterScenarioInfoNotes("Complete", "High", "Test Description", "Test Notes")
            .save(EvaluatePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus("piston_assembly", testScenarioName, "Update", "okay")
            .closeJobQueue(EvaluatePage.class)
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Assembly")
            .setRowOne("Description", "Contains", "Test Description")
            .apply(ExplorePage.class);

        assertThat(explorePage.getListOfAssemblies(testScenarioName, "Piston_assembly"), is(equalTo(1)));*/
    }

    @Ignore
    @Test
    @TestRail(testCaseId = "6220")
    @Description("Test public criteria comparison")
    public void testPublicCriteriaComparison() {
        String testComparisonName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        /*explorePage = loginPage.login(UserUtil.getUser())
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(GenericHeader.class)
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Comparison")
            .setRowOne("Part Name", "Contains", testComparisonName)
            .apply(ExplorePage.class);

        assertThat(explorePage.getListOfComparisons(testComparisonName), is(equalTo(1)));*/
    }

    @Ignore
    @Test
    @TestRail(testCaseId = "6221")
    @Description("Test public criteria assembly description")
    public void testFilterAttributes() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "PowderMetalShaft.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        /*explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .publishScenario("Analysis", "Initial", "Ciene Frith")
            .selectLock()
            .selectPublishButton()
            .filter()
            .setWorkspace("Public, Private")
            .setScenarioType("Part")
            .setRowOne("Status", "is", "Analysis")
            .setRowTwo("Cost Maturity", "is", "Initial")
            .setRowThree("Assignee", "is", "Ciene Frith")
            .apply(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(testScenarioName, "PowderMetalShaft"), is(equalTo(1)));*/
    }

    @Ignore
    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"6222"})
    @Description("Within the recent view, a type field is visible to the user.")
    public void recentComparisonsTest() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "1027312-101-A1333.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonNameA = new GenerateStringUtil().generateComparisonName();
        String testComparisonNameB = new GenerateStringUtil().generateComparisonName();
        String testAssemblyName = "1027312-101-A1333";
        String partName = "1027311-001";

        loginPage = new CidAppLoginPage(driver);
        /*comparePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(scenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario()
            .publish(PublishPage.class)
            .createNewComparison()
            .enterComparisonName(testComparisonNameA)
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
            .apply(ComparePage.class)
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .createNewComparison()
            .enterComparisonName(testComparisonNameB)
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
            .apply(GenericHeader.class)
            .selectExploreButton()
            .selectWorkSpace("Recent");

        assertThat(explorePage.isComparisonIconDisplayedInTypeCell(testComparisonNameA), is(true));
        assertThat(explorePage.isComparisonIconDisplayedInTypeCell(testComparisonNameB), is(true));
        assertThat(explorePage.isPartIconDisplayedInTypeCell(partName, scenarioName), is(true));
        assertThat(explorePage.isAssemblyIconDisplayedInTypeCell(testAssemblyName, scenarioName), is(true));*/
    }
}