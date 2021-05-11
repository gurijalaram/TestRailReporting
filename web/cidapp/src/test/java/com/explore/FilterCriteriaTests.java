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
import org.junit.Test;

import java.io.File;

public class FilterCriteriaTests extends TestBase {

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;

    private File resourceFile;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    public FilterCriteriaTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"6213"})
    @Description("Test private criteria part")
    public void testPrivateCriteriaPart() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "SheetMetal.prt");
        String testScenarioName = generateStringUtil.generateScenarioName();
        String filterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteriaWithOption("Component Name", "Contains", "SheetMetal")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios("SheetMetal", testScenarioName), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"6214"})
    @Description("Test private criteria attribute")
    public void testPrivateCriteriaAttribute() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Casting.prt");
        String testScenarioName = generateStringUtil.generateScenarioName();
        String filterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteriaWithOption("Process Group", "is", ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios("Casting", testScenarioName), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"6215"})
    @Description("Test private criteria part contains")
    public void testPrivateCriteriaContains() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "CurvedWall.CATPart");
        String testScenarioName = generateStringUtil.generateScenarioName();
        String filterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteriaWithOption("Part Name", "Contains", "Wall")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios("CurvedWall", testScenarioName), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"6216"})
    @Description("Test private criteria assembly")
    public void testPrivateCriteriaAssembly() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Piston_assembly.stp");
        String testScenarioName = generateStringUtil.generateScenarioName();
        String filterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteriaWithOption("Component Name", "Contains", "Piston_assembly")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios("Piston_assembly", testScenarioName), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"6217"})
    @Description("Test private criteria assembly status")
    public void testPublicCriteriaAssemblyStatus() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Piston_assembly.stp");
        String testScenarioName = generateStringUtil.generateScenarioName();
        String filterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .info()
            .inputStatus("Analysis")
            .inputCostMaturity("High")
            .inputDescription("Test Description")
            .inputNotes("Test Notes")
            .submit(EvaluatePage.class)
            .publishScenario()
            .publish(ExplorePage.class)
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteriaWithOption("Status", "is", "Analysis")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios("Piston_assembly", testScenarioName), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"6218"})
    @Description("Test public criteria part")
    public void testPublicCriteriaPart() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Push Pin.stp");
        String testScenarioName = generateStringUtil.generateScenarioName();
        String filterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .publishScenario()
            .publish(ExplorePage.class)
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteriaWithOption("Component Name", "Contains", "Push Pin")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios("Push Pin", testScenarioName), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"6219"})
    @Description("Test public criteria assembly description")
    public void testPublicCriteriaAssemblyDesc() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Piston_assembly.stp");
        String testScenarioName = generateStringUtil.generateScenarioName();
        String filterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .info()
            .inputStatus("Complete")
            .inputCostMaturity("High")
            .inputDescription("Test Description")
            .inputNotes("Test Notes")
            .submit(ExplorePage.class)
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteriaWithOption("Description", "Contains", "Test Description")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios("Piston_assembly", testScenarioName), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"6221"})
    @Description("Test public criteria assembly description")
    public void testFilterAttributes() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "PowderMetalShaft.stp");
        String testScenarioName = generateStringUtil.generateScenarioName();
        String filterName = generateStringUtil.generateFilterName();
        String filterName2 = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .publishScenario()
            .inputStatus("Analysis")
            .inputCostMaturity("Initial")
            .publish(ExplorePage.class)
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteriaWithOption("Assignee", "In", "Ciene Frith")
            .submit(ExplorePage.class)
            .lock(ExplorePage.class)
            .publishScenario()
            .publish(ExplorePage.class)
            .filter()
            .saveAs()
            .inputName(filterName2)
            .addCriteriaWithOption("Status", "In", "Analysis")
            .addCriteriaWithOption("Cost Maturity", "In", "Initial")
            .addCriteriaWithOption("Assignee", "In", "Ciene Frith")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios("PowderMetalShaft", testScenarioName), is(equalTo(1)));
    }
}