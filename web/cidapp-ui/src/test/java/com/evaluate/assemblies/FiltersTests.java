package com.evaluate.assemblies;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.apriori.pageobjects.common.FilterPage;
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

import io.qameta.allure.Description;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FiltersTests extends TestBase {

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private ComponentsListPage componentsListPage;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    String filterName2 = generateStringUtil.generateFilterName();
    private FilterPage filterPage;
    private UserCredentials currentUser;
    String assemblyName = "Hinge assembly";
    final String assemblyExtension = ".SLDASM";
    List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
    final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;
    final String componentExtension = ".SLDPRT";
    private File assembly;

    @Test
    @TestRail(testCaseId = "10538")
    @Description("Verify that filter criteria can be deleted")
    public void filterCriteriaCanBeDeletedTest() {
        String filterName = generateStringUtil.generateFilterName();
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        filterPage = loginPage.login(currentUser)
            .uploadsAndOpenAssembly(
                assemblyName,
                assemblyExtension,
                ProcessGroupEnum.ASSEMBLY,
                subComponentNames,
                componentExtension,
                processGroupEnum,
                scenarioName,
                currentUser)
            .openComponents()
            .tableView()
            .filter()
            .newFilter()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COST_MATURITY, OperationEnum.IN, "Medium")
            .clear();

        assertTrue(filterPage.isElementDisplayed("No queries applied", "message"));
    }

    @Test
    @TestRail(testCaseId = "10537")
    @Description("Verify that newly created filter is displayed in filters dropdown in my filter section")
    public void newlyCreatedFilterIsDisplayedInFiltersTest() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadsAndOpenAssembly(
                assemblyName,
                assemblyExtension,
                ProcessGroupEnum.ASSEMBLY,
                subComponentNames,
                componentExtension,
                processGroupEnum,
                scenarioName,
                currentUser)
            .openComponents()
            .tableView()
            .filter()
            .newFilter()
            .inputName(filterName2)
            .addCriteria(PropertyEnum.COST_MATURITY, OperationEnum.IN, "Medium")
            .submit(ExplorePage.class);

        assertTrue(filterPage.isElementDisplayed(filterName2, "text-overflow"));
    }

    @Test
    @TestRail(testCaseId = "10535")
    @Description("Verify Cancel button closes the Scenario filter table")
    public void cancelBtnCloseFilterTableTest() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadsAndOpenAssembly(
                assemblyName,
                assemblyExtension,
                ProcessGroupEnum.ASSEMBLY,
                subComponentNames,
                componentExtension,
                processGroupEnum,
                scenarioName,
                currentUser)
            .openComponents()
            .tableView()
            .filter()
            .cancel(ExplorePage.class);

        assertFalse(explorePage.isFilterTablePresent());
    }

    @Test
    @TestRail(testCaseId = "10534")
    @Description("User can clear added criteria simultaneously by Clear button")
    public void canClearAddedCriteriaTest() {
        String filterName = generateStringUtil.generateFilterName();
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        filterPage = loginPage.login(currentUser)
            .uploadsAndOpenAssembly(
                assemblyName,
                assemblyExtension,
                ProcessGroupEnum.ASSEMBLY,
                subComponentNames,
                componentExtension,
                processGroupEnum,
                scenarioName,
                currentUser)
            .openComponents()
            .tableView()
            .filter()
            .newFilter()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COST_MATURITY, OperationEnum.IN, "Medium")
            .addCriteria(PropertyEnum.COST_MATURITY, OperationEnum.IN, "Low")
            .deleteAllCriteria();

        assertTrue(filterPage.isElementDisplayed("No queries applied", "message"));
    }

    @Test
    @TestRail(testCaseId = "10532")
    @Description("Validate that user can cancel action New before saving")
    public void canCancelBeforeSavingTest() {
        String filterName = generateStringUtil.generateFilterName();
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        filterPage = loginPage.login(currentUser)
            .uploadsAndOpenAssembly(
                assemblyName,
                assemblyExtension,
                ProcessGroupEnum.ASSEMBLY,
                subComponentNames,
                componentExtension,
                processGroupEnum,
                scenarioName,
                currentUser)
            .openComponents()
            .tableView()
            .filter()
            .newFilter()
            .inputName(filterName)
            .cancel(FilterPage.class);

        assertThat(filterPage.getAllFilters()).doesNotContain(filterName);
    }

    @Test
    @TestRail(testCaseId = "10531")
    @Description("User can filter scenarios from scenario filter modal box")
    public void canFilterScenariosFromModalBoxTest() {
        String filterName = generateStringUtil.generateFilterName();
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadsAndOpenAssembly(
                assemblyName,
                assemblyExtension,
                ProcessGroupEnum.ASSEMBLY,
                subComponentNames,
                componentExtension,
                processGroupEnum,
                scenarioName,
                currentUser)
            .openComponents()
            .tableView()
            .filter()
            .newFilter()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.EQUALS, "BIG RING")
            .submit(ExplorePage.class);

        assertThat(explorePage.getAllScenarioComponentName()).containsExactly("BIG RING");
    }

    @Test
    @TestRail(testCaseId = "10529")
    @Description("User can create new filter from already existing one using Save As button")
    public void canCreateNewFilterBySaveAsTest() {
        String filterName = generateStringUtil.generateFilterName();
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadsAndOpenAssembly(
                assemblyName,
                assemblyExtension,
                ProcessGroupEnum.ASSEMBLY,
                subComponentNames,
                componentExtension,
                processGroupEnum,
                scenarioName,
                currentUser)
            .openComponents()
            .tableView()
            .filter()
            .selectFilter("Missing")
            .saveAs()
            .inputName(filterName)
            .submit(ExplorePage.class);

        assertTrue(explorePage.isElementDisplayed(filterName, "text-overflow"));
    }

    @Test
    @TestRail(testCaseId = "10528")
    @Description("User is able to edit already created filters")
    public void ableToEditCreatedFilterTest() {
        String filterName = generateStringUtil.generateFilterName();
        String filterName2 = generateStringUtil.generateFilterName();
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadsAndOpenAssembly(
                assemblyName,
                assemblyExtension,
                ProcessGroupEnum.ASSEMBLY,
                subComponentNames,
                componentExtension,
                processGroupEnum,
                scenarioName,
                currentUser)
            .openComponents()
            .tableView()
            .filter()
            .newFilter()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.EQUALS, "BIG RING")
            .submit(ExplorePage.class);

        explorePage
            .filterOnTableView()
            .rename()
            .inputName(filterName2)
            .save(FilterPage.class)
            .submit(ExplorePage.class);

        assertTrue(explorePage.isElementDisplayed(filterName2, "text-overflow"));
    }

    @Test
    @TestRail(testCaseId = "10527")
    @Description("Validate user can select custom filter")
    public void ableToSelectCustomFilterTest() {
        String filterName = generateStringUtil.generateFilterName();
        String filterName2 = generateStringUtil.generateFilterName();
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .uploadsAndOpenAssembly(
                assemblyName,
                assemblyExtension,
                ProcessGroupEnum.ASSEMBLY,
                subComponentNames,
                componentExtension,
                processGroupEnum,
                scenarioName,
                currentUser)
            .openComponents()
            .tableView()
            .filter()
            .newFilter()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.EQUALS, "BIG RING")
            .save(FilterPage.class)
            .newFilter()
            .inputName(filterName2)
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.EQUALS, "PIN")
            .submit(EvaluatePage.class)
            .treeView()
            .openComponents()
            .tableView()
            .selectFilter(filterName);

        assertTrue(componentsListPage.isElementDisplayed(filterName, "text-overflow"));
    }

    @Test
    @TestRail(testCaseId = "10526")
    @Description("Validate user can create custom filter with all available attributes")
    public void ableToCreateCustomFilterWithAllAttributesTest() {
        String filterName = generateStringUtil.generateFilterName();
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        filterPage = loginPage.login(currentUser)
            .uploadsAndOpenAssembly(
                assemblyName,
                assemblyExtension,
                ProcessGroupEnum.ASSEMBLY,
                subComponentNames,
                componentExtension,
                processGroupEnum,
                scenarioName,
                currentUser)
            .openComponents()
            .tableView()
            .filter()
            .newFilter()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.EQUALS, "BIG RING");

        List<String> operations1 =
            filterPage.getListOfOperationsForCriteria((PropertyEnum.PROCESS_GROUP));
        assertEquals(Arrays.asList("In", "Is Not Defined", "Not In"), operations1);

        List<String> operations2 =
            filterPage.getListOfOperationsForCriteria((PropertyEnum.SCENARIO_TYPE));
        assertEquals(Arrays.asList("In", "Is Not Defined", "Not In"), operations2);

        List<String> operations3 =
            filterPage.getListOfOperationsForCriteria((PropertyEnum.CREATED_AT));
        assertEquals(Arrays.asList("Greater Than", "Less Than"), operations3);

        List<String> operations4 =
            filterPage.getListOfOperationsForCriteria((PropertyEnum.DFM_RISK));
        assertEquals(Arrays.asList("In", "Is Not Defined", "Not In"), operations4);

        List<String> operations5 =
            filterPage.getListOfOperationsForCriteria((PropertyEnum.MATERIAL_COST));
        assertEquals(Arrays.asList("Greater Than", "Less Than"), operations5);

        List<String> operations6 =
            filterPage.getListOfOperationsForCriteria((PropertyEnum.FINISH_MASS));
        assertEquals(Arrays.asList("Equals", "Not Equal", "Greater Than", "Greater Than or Equal To",
            "Less Than", "Less Than or Equal To"), operations6);
    }

    @Test
    @TestRail(testCaseId = "10525")
    @Description("Validate user can select Uncosted scenarios")
    public void ableToSelectUncostedScenarioTest() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        assembly = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, assemblyName + ".SLDASM");

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .uploadsAndOpenAssembly(
                assemblyName,
                assemblyExtension,
                ProcessGroupEnum.ASSEMBLY,
                subComponentNames,
                componentExtension,
                processGroupEnum,
                scenarioName,
                currentUser)
            .openComponents()
            .tableView()
            .selectFilter("Uncosted");

        List<String> stateListUncosted = componentsListPage.getAllScenarioState();
        assertTrue(componentsListPage.isElementDisplayed("Uncosted", "text-overflow"));
        assertThat(stateListUncosted).containsExactly("Uncosted", "Uncosted", "Uncosted");
    }

    @Test
    @TestRail(testCaseId = "10524")
    @Description("Validate user can select Assigned to Me scenarios")
    public void ableToSelectAssignedToMeScenarioTest() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        assembly = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, assemblyName + ".SLDASM");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(assemblyName, scenarioName, assembly, currentUser);

        componentsListPage = new ComponentsListPage(driver);

        componentsListPage
            .tableView()
            .selectFilter("Assigned To Me");
        assertTrue(componentsListPage.isElementDisplayed("Assigned To Me", "text-overflow"));
        MatcherAssert.assertThat(componentsListPage.getScenarioMessage(), containsString("No scenarios found"));
    }

    @Test
    @TestRail(testCaseId = "10523")
    @Description("Validate user can select Missing scenarios")
    public void ableToSelectMissingScenarioTest() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        assembly = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, assemblyName + ".SLDASM");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(assemblyName, scenarioName, assembly, currentUser);

        componentsListPage = new ComponentsListPage(driver);

        componentsListPage
            .tableView()
            .selectFilter("Missing");
        assertTrue(componentsListPage.isElementDisplayed("Missing", "text-overflow"));
        MatcherAssert.assertThat(componentsListPage.getScenarioMessage(), containsString("No scenarios found"));
    }

    @Test
    @TestRail(testCaseId = "10522")
    @Description("Validate user can select All scenarios")
    public void ableToSelectAllScenarioTest() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        assembly = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, assemblyName + ".SLDASM");

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(assemblyName, scenarioName, assembly, currentUser);

        componentsListPage = new ComponentsListPage(driver);

        componentsListPage
            .tableView()
            .selectFilter("All");
        assertTrue(componentsListPage.isElementDisplayed("All", "text-overflow"));
        assertThat(componentsListPage.getAllScenarioComponentName()).hasSize(3);
    }

}
