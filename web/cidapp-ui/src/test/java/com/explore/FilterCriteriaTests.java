package com.explore;

import static com.apriori.utils.enums.ProcessGroupEnum.ASSEMBLY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsListPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.OperationEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.PropertyEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ColumnsEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FilterCriteriaTests extends TestBase {

    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private File resourceFile;
    private ComponentsListPage componentsListPage;
    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private ComponentInfoBuilder cidComponentItem;

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
                .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.CONTAINS, "SheetMetal")
                .submit(ExplorePage.class)
                .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        assertThat(explorePage.getListOfScenarios(componentName, scenarioName), is(equalTo(1)));
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
                .addCriteria(PropertyEnum.PROCESS_GROUP, OperationEnum.IN, ProcessGroupEnum.CASTING_DIE.getProcessGroup())
                .submit(ExplorePage.class)
                .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        assertThat(explorePage.getListOfScenarios(componentName, scenarioName), is(equalTo(1)));
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
                .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.CONTAINS, "Wall")
                .submit(ExplorePage.class)
                .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        assertThat(explorePage.getListOfScenarios(componentName, scenarioName), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"6216"})
    @Description("Test private criteria assembly")
    public void testPrivateCriteriaAssembly() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        String filename = "oldham.asm.1";
        String componentName = "OLDHAM";
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, filename);
        String filterName = generateStringUtil.generateFilterName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
                .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
                .clickExplore()
                .filter()
                .saveAs()
                .inputName(filterName)
                .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.CONTAINS, "oldham")
                .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios("oldham", scenarioName), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"6217"})
    @Description("Test public criteria assembly status")
    public void testPublicCriteriaAssemblyStatus() {
        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final String BIG_RING = "big ring";
        final String PIN = "Pin";
        final String SMALL_RING = "small ring";

        final List<String> subComponentNames = Arrays.asList(BIG_RING, PIN, SMALL_RING);
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;
        final String subComponentExtension = ".SLDPRT";
        String filterName = generateStringUtil.generateFilterName();

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
                assemblyExtension,
                ASSEMBLY,
                subComponentNames,
                subComponentExtension,
                subComponentProcessGroup,
                scenarioName,
                currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
                .uploadAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
                .navigateToScenario(componentAssembly)
                .clickActions()
                .info()
                .selectStatus("Analysis")
                .inputCostMaturity("High")
                .inputDescription("Test Description")
                .inputNotes("Test Notes")
                .submit(EvaluatePage.class)
                .publishScenario(PublishPage.class)
                .publish(cidComponentItem, ExplorePage.class)
                .filter()
                .saveAs()
                .inputName(filterName)
                .addCriteria(PropertyEnum.STATUS, OperationEnum.IN, "Analysis")
                .submit(ExplorePage.class);

        assertThat(explorePage.getListOfScenarios("Hinge assembly", scenarioName), is(greaterThanOrEqualTo(1)));
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
        cidComponentItem = loginPage.login(currentUser)
                .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        explorePage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
                .publishScenario(PublishPage.class)
                .publish(cidComponentItem, EvaluatePage.class)
                .clickExplore()
                .filter()
                .saveAs()
                .inputName(filterName)
                .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.CONTAINS, "Push Pin")
                .submit(ExplorePage.class)
                .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        assertThat(explorePage.getListOfScenarios("Push Pin", scenarioName), is(equalTo(1)));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"6221"})
    @Description("Test multiple attributes")
    public void testFilterAttributes() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        String componentName = "PowderMetalShaft";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        String filterName = generateStringUtil.generateFilterName();
        String filterName2 = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        cidComponentItem = loginPage.login(currentUser)
                .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        String scenarioCreatedByName = cidComponentItem.getScenarioItem().getScenarioCreatedByName();

        explorePage = new ExplorePage(driver).navigateToScenario(cidComponentItem)
                .publishScenario(PublishPage.class)
                .selectStatus("Analysis")
                .selectCostMaturity("Initial")
                .selectAssignee(currentUser)
                .publish(cidComponentItem, EvaluatePage.class)
                .clickExplore()
                .filter()
                .saveAs()
                .inputName(filterName)
                .addCriteria(PropertyEnum.ASSIGNEE, OperationEnum.IN, scenarioCreatedByName)
                .submit(ExplorePage.class)
                .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING)
                .highlightScenario(componentName, scenarioName)
                .clickActions()
                .lock(ExplorePage.class)
                .filter()
                .newFilter()
                .inputName(filterName2)
                .addCriteria(PropertyEnum.CREATED_AT, OperationEnum.GREATER_THAN, cidComponentItem.getScenarioItem().getScenarioCreatedAt())
                .addCriteria(PropertyEnum.STATUS, OperationEnum.IN, "Analysis")
                .addCriteria(PropertyEnum.COST_MATURITY, OperationEnum.IN, "Initial")
                .addCriteria(PropertyEnum.ASSIGNEE, OperationEnum.IN, scenarioCreatedByName)
                .submit(ExplorePage.class)
                .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        assertThat(explorePage.getListOfScenarios("PowderMetalShaft", scenarioName), is(equalTo(1)));
    }
}