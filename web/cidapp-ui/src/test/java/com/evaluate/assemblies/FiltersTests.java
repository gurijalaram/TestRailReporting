package com.evaluate.assemblies;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
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

import com.utils.ColumnsEnum;
import com.utils.DirectionEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class FiltersTests extends TestBase {

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private ComponentsListPage componentsListPage;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private String filterName2 = generateStringUtil.generateFilterName();
    private FilterPage filterPage;
    private UserCredentials currentUser;
    private String assemblyName = "Hinge assembly";
    private final String assemblyExtension = ".SLDASM";
    private List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
    private final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.FORGING;
    private final String componentExtension = ".SLDPRT";
    private File assembly;

    @Test
    @TestRail(testCaseId = {"10538", "6168"})
    @Description("Verify that filter criteria can be deleted")
    public void filterCriteriaCanBeDeletedTest() {
        SoftAssertions soft = new SoftAssertions();
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

        soft.assertThat(filterPage.isElementDisplayed("No queries applied", "message")).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"10537", "6167", "6083"})
    @Description("Verify that newly created filter is displayed in filters dropdown in my filter section")
    public void newlyCreatedFilterIsDisplayedInFiltersTest() {
        SoftAssertions soft = new SoftAssertions();
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

        soft.assertThat(explorePage.isElementDisplayed(filterName2, "text-overflow")).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"10535", "6165"})
    @Description("Verify Cancel button closes the Scenario filter table")
    public void cancelBtnCloseFilterTableTest() {
        SoftAssertions soft = new SoftAssertions();
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

        soft.assertThat(explorePage.isFilterTablePresent()).isFalse();
    }

    @Test
    @TestRail(testCaseId = {"10534", "6164"})
    @Description("User can clear added criteria simultaneously by Clear button")
    public void canClearAddedCriteriaTest() {
        SoftAssertions soft = new SoftAssertions();
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

        soft.assertThat(filterPage.isElementDisplayed("No queries applied", "message")).isTrue();
    }

    @Test
    @TestRail(testCaseId = "10532")
    @Description("Validate that user can cancel action New before saving")
    public void canCancelBeforeSavingTest() {
        SoftAssertions soft = new SoftAssertions();
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

        soft.assertThat(filterPage.getAllFilters()).doesNotContain(filterName);
    }

    @Test
    @TestRail(testCaseId = {"10531", "6099"})
    @Description("User can filter scenarios from scenario filter modal box")
    public void canFilterScenariosFromModalBoxTest() {
        SoftAssertions soft = new SoftAssertions();
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

        soft.assertThat(explorePage.getAllScenarioComponentName()).containsExactly("BIG RING");
    }

    @Test
    @TestRail(testCaseId = {"10529", "6085"})
    @Description("User can create new filter from already existing one using Save As button")
    public void canCreateNewFilterBySaveAsTest() {
        SoftAssertions soft = new SoftAssertions();
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

        soft.assertThat(explorePage.isElementDisplayed(filterName, "text-overflow")).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"10528", "6084"})
    @Description("User is able to edit already created filters")
    public void ableToEditCreatedFilterTest() {
        SoftAssertions soft = new SoftAssertions();
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

        soft.assertThat(explorePage.isElementDisplayed(filterName2, "text-overflow")).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"10527", "6353"})
    @Description("Validate user can select custom filter")
    public void ableToSelectCustomFilterTest() {
        SoftAssertions soft = new SoftAssertions();
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

        soft.assertThat(componentsListPage.isElementDisplayed(filterName, "text-overflow")).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"10526", "6081"})
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

        SoftAssertions soft = new SoftAssertions();

        List<String> operations1 =
            filterPage.getListOfOperationsForCriteria((PropertyEnum.PROCESS_GROUP));
        soft.assertThat(Arrays.asList("In", "Is Not Defined", "Not In")).isEqualTo(operations1);

        List<String> operations2 =
            filterPage.getListOfOperationsForCriteria((PropertyEnum.SCENARIO_TYPE));
        soft.assertThat(Arrays.asList("In", "Is Not Defined", "Not In")).isEqualTo(operations2);

        List<String> operations3 =
            filterPage.getListOfOperationsForCriteria((PropertyEnum.CREATED_AT));
        soft.assertThat(Arrays.asList("Greater Than", "Less Than")).isEqualTo(operations3);

        List<String> operations4 =
            filterPage.getListOfOperationsForCriteria((PropertyEnum.DFM_RISK));
        soft.assertThat(Arrays.asList("In", "Is Not Defined", "Not In")).isEqualTo(operations4);

        List<String> operations5 =
            filterPage.getListOfOperationsForCriteria((PropertyEnum.MATERIAL_COST));
        soft.assertThat(Arrays.asList("Greater Than", "Less Than")).isEqualTo(operations5);

        List<String> operations6 =
            filterPage.getListOfOperationsForCriteria((PropertyEnum.FINISH_MASS));
        soft.assertThat(Arrays.asList("Equals", "Not Equal", "Greater Than", "Greater Than or Equal To",
            "Less Than", "Less Than or Equal To")).isEqualTo(operations6);
    }

    @Test
    @TestRail(testCaseId = "10525")
    @Description("Validate user can select Uncosted scenarios")
    public void ableToSelectUncostedScenarioTest() {
        SoftAssertions soft = new SoftAssertions();
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

        soft.assertThat(componentsListPage.isElementDisplayed("Uncosted", "text-overflow")).isTrue();
        soft.assertThat(stateListUncosted).containsExactly("Uncosted", "Uncosted", "Uncosted");
    }

    @Test
    @TestRail(testCaseId = {"10524", "6077"})
    @Description("Validate user can select Assigned to Me scenarios")
    public void ableToSelectAssignedToMeScenarioTest() {
        SoftAssertions soft = new SoftAssertions();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        assembly = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, assemblyName + ".SLDASM");

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
        .uploadComponentAndOpen(assemblyName, scenarioName, assembly, currentUser)
            .openComponents()
            .tableView()
            .selectFilter("Assigned To Me");

        soft.assertThat(componentsListPage.isElementDisplayed("Assigned To Me", "text-overflow")).isTrue();
        soft.assertThat(componentsListPage.getScenarioMessage()).contains("No scenarios found");
    }

    @Test
    @TestRail(testCaseId = "10523")
    @Description("Validate user can select Missing scenarios")
    public void ableToSelectMissingScenarioTest() {
        SoftAssertions soft = new SoftAssertions();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        assembly = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, assemblyName + ".SLDASM");

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(assemblyName, scenarioName, assembly, currentUser)
            .openComponents()
            .tableView()
            .selectFilter("Missing");

        soft.assertThat(componentsListPage.isElementDisplayed("Missing", "text-overflow")).isTrue();
        soft.assertThat(componentsListPage.getScenarioMessage()).contains("No scenarios found");
    }

    @Test
    @TestRail(testCaseId = {"10522", "6531"})
    @Description("Validate user can select All scenarios")
    public void ableToSelectAllScenarioTest() {
        SoftAssertions soft = new SoftAssertions();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        assembly = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, assemblyName + ".SLDASM");
        loginPage = new CidAppLoginPage(driver);

        componentsListPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(assemblyName, scenarioName, assembly, currentUser)
            .openComponents()
            .tableView()
            .selectFilter("All");

        soft.assertThat(componentsListPage.isElementDisplayed("All", "text-overflow")).isTrue();
        soft.assertThat(componentsListPage.getAllScenarioComponentName(3)).hasSize(3);
    }

    @Test
    @TestRail(testCaseId = {"6075", "6080"})
    @Description("Validate Private filter displays only Private Scenarios")
    public void verifyFilterContentTest() {
        SoftAssertions softAssert = new SoftAssertions();
        LocalDateTime filterStartTime = LocalDateTime.now().minusHours(24);
        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        explorePage = loginPage.login(currentUser);

        List<String> tableHeaders = explorePage.getTableHeaders();

        explorePage.selectFilter("Private")
            .addColumn(tableHeaders, ColumnsEnum.PUBLISHED)
            .sortColumn(ColumnsEnum.PUBLISHED, SortOrderEnum.ASCENDING);

        String[] topScenarioDetails = explorePage.getFirstScenarioDetails().split(",");
        String topComponentName = topScenarioDetails[0];
        String topScenarioName = topScenarioDetails[1];

        softAssert.assertThat(explorePage.getPublishedState(topComponentName, topScenarioName))
            .as("Published state of top scenario sorted ascending")
            .isEqualTo("Private");

        explorePage.sortColumn(ColumnsEnum.PUBLISHED, SortOrderEnum.DESCENDING);
        topScenarioDetails = explorePage.getFirstScenarioDetails().split(",");
        topComponentName = topScenarioDetails[0];
        topScenarioName = topScenarioDetails[1];

        softAssert.assertThat(explorePage.getPublishedState(topComponentName, topScenarioName))
            .as("Published state of top scenario sorted ascending")
            .isEqualTo("Private");

        explorePage.selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.ASCENDING);

        topScenarioDetails = explorePage.getFirstScenarioDetails().split(",");
        topComponentName = topScenarioDetails[0];
        topScenarioName = topScenarioDetails[1];

        softAssert.assertThat(explorePage.getCreatedAt(topComponentName, topScenarioName))
                .as("Created At date of oldest scenario in Recent")
                .isAfterOrEqualTo(filterStartTime);

        softAssert.assertAll();
    }
}
