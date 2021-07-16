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
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.IgnoreTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class FilterCriteriaTests extends TestBase {

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;

    private File resourceFile;
    UserCredentials currentUser;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    public FilterCriteriaTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"6213"})
    @Description("Test private criteria part")
    public void testPrivateCriteriaPart() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL_TRANSFER_DIE;

        String componentName = "SheetMetal";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        String filterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteriaWithOption("Component Name", "Contains", "SheetMetal")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios("SheetMetal", scenarioName), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"6214"})
    @Description("Test private criteria attribute")
    public void testPrivateCriteriaAttribute() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "Casting";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        String filterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteriaWithOption("Process Group", "In", ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios("Casting", scenarioName), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"6215"})
    @Description("Test private criteria part contains")
    public void testPrivateCriteriaContains() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "CurvedWall";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".CATPart");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        String filterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteriaWithOption("Part Name", "Contains", "Wall")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios("CurvedWall", scenarioName), is(equalTo(1)));
    }

    @Test
    @Category(IgnoreTests.class)
    @Ignore("Cannot upload assemblies")
    @TestRail(testCaseId = {"6216"})
    @Description("Test private criteria assembly")
    public void testPrivateCriteriaAssembly() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        String componentName = "Piston_assembly";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        String filterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .clickExplore()
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteriaWithOption("Component Name", "Contains", "Piston_assembly")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios("Piston_assembly", scenarioName), is(equalTo(1)));
    }

    @Test
    @Category(IgnoreTests.class)
    @Ignore("Cannot upload assemblies")
    @TestRail(testCaseId = {"6217"})
    @Description("Test private criteria assembly status")
    public void testPublicCriteriaAssemblyStatus() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        String componentName = "Piston_assembly";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        String filterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .info()
            .selectStatus("Analysis")
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

        assertThat(explorePage.getListOfScenarios("Piston_assembly", scenarioName), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"6218"})
    @Description("Test public criteria part")
    public void testPublicCriteriaPart() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.PLASTIC_MOLDING;

        String componentName = "Push Pin";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        String filterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .publishScenario()
            .publish(ExplorePage.class)
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteriaWithOption("Component Name", "Contains", "Push Pin")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios("Push Pin", scenarioName), is(equalTo(1)));
    }

    @Test
    @Category(IgnoreTests.class)
    @Ignore("Cannot upload assemblies")
    @TestRail(testCaseId = {"6219"})
    @Description("Test public criteria assembly description")
    public void testPublicCriteriaAssemblyDesc() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        String componentName = "Piston_assembly";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        String filterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .info()
            .selectStatus("Complete")
            .inputCostMaturity("High")
            .inputDescription("Test Description")
            .inputNotes("Test Notes")
            .submit(ExplorePage.class)
            .filter()
            .saveAs()
            .inputName(filterName)
            .addCriteriaWithOption("Description", "Contains", "Test Description")
            .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios("Piston_assembly", scenarioName), is(equalTo(1)));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"6221"})
    @Description("Test public criteria assembly description")
    public void testFilterAttributes() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        String componentName = "PowderMetalShaft";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        String filterName = generateStringUtil.generateFilterName();
        String filterName2 = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .publishScenario()
            .selectStatus("Analysis")
            .selectCostMaturity("Initial")
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

        assertThat(explorePage.getListOfScenarios("PowderMetalShaft", scenarioName), is(equalTo(1)));
    }
}