package com.apriori.cid.ui.tests.evaluate.assemblies;

import static org.assertj.core.api.Assertions.assertThat;

import com.apriori.cid.api.utils.AssemblyUtils;
import com.apriori.cid.ui.pageobjects.common.FilterPage;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.SetInputStatusPage;
import com.apriori.cid.ui.pageobjects.evaluate.components.ComponentsTablePage;
import com.apriori.cid.ui.pageobjects.evaluate.components.ComponentsTreePage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.utils.ColumnsEnum;
import com.apriori.cid.ui.utils.SortOrderEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.AssemblyRequestUtil;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.NewCostingLabelEnum;
import com.apriori.shared.util.enums.OperationEnum;
import com.apriori.shared.util.enums.PropertyEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FiltersTests extends TestBaseUI {

    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private ComponentInfoBuilder componentAssembly;
    private SoftAssertions softAssertions = new SoftAssertions();
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private ComponentsTablePage componentsTablePage;
    private ComponentsTreePage componentsTreePage;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private String filterName2 = generateStringUtil.generateFilterName();
    private FilterPage filterPage;
    private UserCredentials currentUser;

    @Test
    @TestRail(id = {10538, 6168})
    @Description("Verify that filter criteria can be deleted")
    public void filterCriteriaCanBeDeletedTest() {

        String filterName = generateStringUtil.generateFilterName();

        componentAssembly = new AssemblyRequestUtil().getAssembly();

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        filterPage = new CidAppLoginPage(driver)
            .login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .filter()
            .newFilter()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COST_MATURITY, OperationEnum.IN, "Medium")
            .clear();

        softAssertions.assertThat(filterPage.isElementDisplayed("No queries applied", "message")).isTrue();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {10537, 6167, 6083})
    @Description("Verify that newly created filter is displayed in filters dropdown in my filter section")
    public void newlyCreatedFilterIsDisplayedInFiltersTest() {

        componentAssembly = new AssemblyRequestUtil().getAssembly();

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        explorePage = new CidAppLoginPage(driver)
            .login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .filter()
            .newFilter()
            .inputName(filterName2)
            .addCriteria(PropertyEnum.COST_MATURITY, OperationEnum.IN, "Medium")
            .submit(ExplorePage.class);

        softAssertions.assertThat(explorePage.isElementDisplayed(filterName2, "text-overflow")).isTrue();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {10535, 6165})
    @Description("Verify Cancel button closes the Scenario filter table")
    public void cancelBtnCloseFilterTableTest() {

        componentAssembly = new AssemblyRequestUtil().getAssembly();

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        explorePage = new CidAppLoginPage(driver)
            .login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .filter()
            .cancel(ExplorePage.class);

        softAssertions.assertThat(explorePage.isFilterTablePresent()).isFalse();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {10534, 6164})
    @Description("User can clear added criteria simultaneously by Clear button")
    public void canClearAddedCriteriaTest() {

        String filterName = generateStringUtil.generateFilterName();
        componentAssembly = new AssemblyRequestUtil().getAssembly();

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        filterPage = new CidAppLoginPage(driver)
            .login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .filter()
            .newFilter()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COST_MATURITY, OperationEnum.IN, "Medium")
            .addCriteria(PropertyEnum.COST_MATURITY, OperationEnum.IN, "Low")
            .deleteAllCriteria();

        softAssertions.assertThat(filterPage.isElementDisplayed("No queries applied", "message")).isTrue();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 10532)
    @Description("Validate that user can cancel action New before saving")
    public void canCancelBeforeSavingTest() {

        String filterName = generateStringUtil.generateFilterName();

        componentAssembly = new AssemblyRequestUtil().getAssembly();

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        filterPage = new CidAppLoginPage(driver)
            .login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .filter()
            .newFilter()
            .inputName(filterName)
            .cancel(FilterPage.class);

        softAssertions.assertThat(filterPage.getAllFilters()).doesNotContain(filterName);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {10531, 6099})
    @Description("User can filter scenarios from scenario filter modal box")
    public void canFilterScenariosFromModalBoxTest() {

        String filterName = generateStringUtil.generateFilterName();

        componentAssembly = new AssemblyRequestUtil().getAssembly();

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        explorePage = new CidAppLoginPage(driver)
            .login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .filter()
            .newFilter()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.EQUALS, "BIG RING")
            .submit(ExplorePage.class);

        softAssertions.assertThat(explorePage.getAllScenarioComponentName()).containsExactly("BIG RING");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {10529, 6085})
    @Description("User can create new filter from already existing one using Save As button")
    public void canCreateNewFilterBySaveAsTest() {

        String filterName = generateStringUtil.generateFilterName();

        componentAssembly = new AssemblyRequestUtil().getAssembly();

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        explorePage = new CidAppLoginPage(driver)
            .login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .filter()
            .selectFilter("Missing")
            .saveAs()
            .inputName(filterName)
            .submit(ExplorePage.class);

        softAssertions.assertThat(explorePage.isElementDisplayed(filterName, "text-overflow")).isTrue();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {10528, 6084})
    @Description("User is able to edit already created filters")
    public void ableToEditCreatedFilterTest() {

        String filterName = generateStringUtil.generateFilterName();
        String filterName2 = generateStringUtil.generateFilterName();

        componentAssembly = new AssemblyRequestUtil().getAssembly();

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        explorePage = new CidAppLoginPage(driver)
            .login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .filter()
            .newFilter()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.EQUALS, "BIG RING")
            .submit(ExplorePage.class)
            .filterOnTableView()
            .rename()
            .inputName(filterName2)
            .save(FilterPage.class)
            .submit(ExplorePage.class);

        softAssertions.assertThat(explorePage.isElementDisplayed(filterName2, "text-overflow")).isTrue();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {10527, 6353})
    @Description("Validate user can select custom filter")
    public void ableToSelectCustomFilterTest() {

        String filterName = generateStringUtil.generateFilterName();
        String filterName2 = generateStringUtil.generateFilterName();

        componentAssembly = new AssemblyRequestUtil().getAssembly();

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        componentsTablePage = new CidAppLoginPage(driver)
            .login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
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
            .selectTableView()
            .selectFilter(filterName);

        softAssertions.assertThat(componentsTablePage.isElementDisplayed(filterName, "text-overflow")).isTrue();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {10526, 6081})
    @Description("Validate user can create custom filter with all available attributes")
    public void ableToCreateCustomFilterWithAllAttributesTest() {

        String filterName = generateStringUtil.generateFilterName();

        componentAssembly = new AssemblyRequestUtil().getAssembly();

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        filterPage = new CidAppLoginPage(driver)
            .login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .filter()
            .newFilter()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.EQUALS, "BIG RING");

        List<String> operations1 =
            filterPage.getListOfOperationsForCriteria((PropertyEnum.PROCESS_GROUP));
        softAssertions.assertThat(Arrays.asList("In", "Is Not Defined", "Not In")).isEqualTo(operations1);

        List<String> operations2 =
            filterPage.getListOfOperationsForCriteria((PropertyEnum.SCENARIO_TYPE));
        softAssertions.assertThat(Arrays.asList("In", "Is Not Defined", "Not In")).isEqualTo(operations2);

        List<String> operations3 =
            filterPage.getListOfOperationsForCriteria((PropertyEnum.CREATED_AT));
        softAssertions.assertThat(Arrays.asList("Greater Than", "Less Than")).isEqualTo(operations3);

        List<String> operations4 =
            filterPage.getListOfOperationsForCriteria((PropertyEnum.DFM_RISK));
        softAssertions.assertThat(Arrays.asList("In", "Is Not Defined", "Not In")).isEqualTo(operations4);

        List<String> operations5 =
            filterPage.getListOfOperationsForCriteria((PropertyEnum.MATERIAL_COST));
        softAssertions.assertThat(Arrays.asList("Greater Than", "Less Than")).isEqualTo(operations5);

        List<String> operations6 =
            filterPage.getListOfOperationsForCriteria((PropertyEnum.FINISH_MASS));
        softAssertions.assertThat(Arrays.asList("Equals", "Not Equal", "Greater Than", "Greater Than or Equal To",
            "Less Than", "Less Than or Equal To")).isEqualTo(operations6);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 10525)
    @Description("Validate user can select Uncosted scenarios")
    public void ableToSelectUncostedScenarioTest() {

        componentAssembly = new AssemblyRequestUtil().getAssembly();

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        componentsTablePage = new CidAppLoginPage(driver)
            .login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .selectFilter("Uncosted");

        List<String> stateListUncosted = componentsTablePage.getAllScenarioState();

        softAssertions.assertThat(componentsTablePage.isElementDisplayed("Uncosted", "text-overflow")).isTrue();
        softAssertions.assertThat(stateListUncosted).containsExactly("Uncosted", "Uncosted", "Uncosted");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {10524, 6077})
    @Description("Validate user can select Assigned to Me scenarios")
    public void ableToSelectAssignedToMeScenarioTest() {

        componentAssembly = new AssemblyRequestUtil().getAssembly();

        loginPage = new CidAppLoginPage(driver);
        componentsTablePage = loginPage.login(componentAssembly.getUser())
            .uploadComponentAndOpen(componentAssembly)
            .openComponents()
            .selectTableView()
            .selectFilter("Assigned To Me");

        softAssertions.assertThat(componentsTablePage.isElementDisplayed("Assigned To Me", "text-overflow")).isTrue();
        softAssertions.assertThat(componentsTablePage.getScenarioMessage()).contains("No scenarios found");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 10523)
    @Description("Validate user can select Missing scenarios")
    public void ableToSelectMissingScenarioTest() {

        componentAssembly = new AssemblyRequestUtil().getAssembly();

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTablePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .selectFilter("Missing");

        softAssertions.assertThat(componentsTablePage.isElementDisplayed("Missing", "text-overflow")).isTrue();
        softAssertions.assertThat(componentsTablePage.getScenarioMessage()).contains("No scenarios found");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {10522, 6531})
    @Description("Validate user can select All scenarios")
    public void ableToSelectAllScenarioTest() {

        componentAssembly = new AssemblyRequestUtil().getAssembly();

        loginPage = new CidAppLoginPage(driver);
        componentsTablePage = loginPage.login(componentAssembly.getUser())
            .uploadComponentAndOpen(componentAssembly)
            .openComponents()
            .selectTableView()
            .selectFilter("All");

        softAssertions.assertThat(componentsTablePage.isElementDisplayed("All", "text-overflow")).isTrue();
        softAssertions.assertThat(componentsTablePage.getAllScenarioComponentName(componentAssembly.getSubComponents().size()))
            .containsAll(componentAssembly.getSubComponents().stream().map(o -> o.getComponentName().toUpperCase()).collect(Collectors.toList()));

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6075, 6080})
    @Description("Validate Private filter displays only Private Scenarios")
    public void verifyFilterContentTest() {

        LocalDateTime filterStartTime = LocalDateTime.now().minusHours(24);
        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        explorePage = loginPage.login(currentUser);

        explorePage.selectFilter("Private")
            .addColumn(ColumnsEnum.PUBLISHED)
            .sortColumn(ColumnsEnum.PUBLISHED, SortOrderEnum.ASCENDING);

        String[] topScenarioDetails = explorePage.getFirstScenarioDetails().split(",");
        String topComponentName = topScenarioDetails[0];
        String topScenarioName = topScenarioDetails[1];

        softAssertions.assertThat(explorePage.getPublishedState(topComponentName, topScenarioName))
            .as("Published state of top scenario sorted ascending")
            .isEqualTo("Private");

        explorePage.sortColumn(ColumnsEnum.PUBLISHED, SortOrderEnum.DESCENDING);
        topScenarioDetails = explorePage.getFirstScenarioDetails().split(",");
        topComponentName = topScenarioDetails[0];
        topScenarioName = topScenarioDetails[1];

        softAssertions.assertThat(explorePage.getPublishedState(topComponentName, topScenarioName))
            .as("Published state of top scenario sorted ascending")
            .isEqualTo("Private");

        explorePage.selectFilter("Recent")
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        topScenarioDetails = explorePage.getFirstScenarioDetails().split(",");
        topComponentName = topScenarioDetails[0];
        topScenarioName = topScenarioDetails[1];

        softAssertions.assertThat(explorePage.getCreatedAt(topComponentName, topScenarioName))
            .as("Created At date of oldest scenario in Recent")
            .isAfterOrEqualTo(filterStartTime);

        softAssertions.assertAll();
    }

    @Test
    @Disabled("This test has never worked on Jenkins because the time cannot be changed, should be a manual test")
    @Issue("BA-2610")
    @TestRail(id = 6094)
    @Description("Validate Private filter displays only Private Scenarios")
    public void verifyFilterPersistenceTest() {

        currentUser = UserUtil.getUser();
        String filterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .filter()
            .newFilter()
            .inputName(filterName)
            .addCriteria(PropertyEnum.CREATED_AT, OperationEnum.LESS_THAN, LocalDateTime.now().minusHours(1))
            .submit(ExplorePage.class)
            .selectFilter(filterName)
            .logout()
            .login(currentUser)
            .selectFilter(filterName);

        assertThat(explorePage.getCurrentFilter()).isEqualTo(filterName);
    }

    @Test
    @TestRail(id = 6100)
    @Description("Validate that user can cancel action New, Rename, Save As before saving")
    public void cancelFilterTest() {

        currentUser = UserUtil.getUser();
        String filterName = generateStringUtil.generateFilterName();
        String cancelledFilterName = generateStringUtil.generateFilterName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .filter()
            .newFilter()
            .inputName(filterName)
            .submit(ExplorePage.class);

        filterPage = explorePage.filter()
            .newFilter()
            .inputName(cancelledFilterName);

        softAssertions.assertThat(filterPage.isSaveEnabled()).as("Verify Save button is enabled (New)").isTrue();
        filterPage.cancelInput();
        softAssertions.assertThat(filterPage.isSaveEnabled()).as("Verify Save button is disabled (New)").isFalse();

        filterPage.saveAs()
            .inputName(cancelledFilterName);
        softAssertions.assertThat(filterPage.isSaveEnabled()).as("Verify Save button is enabled (Save As)").isTrue();
        filterPage.cancelInput();
        softAssertions.assertThat(filterPage.isSaveEnabled()).as("Verify Save button is disabled (Save As)").isFalse();
        explorePage = filterPage.cancel(ExplorePage.class);

        filterPage = explorePage.filter()
            .selectFilter(filterName)
            .rename()
            .inputName(cancelledFilterName);
        softAssertions.assertThat(filterPage.isSaveEnabled()).as("Verify Save button is enabled (Rename)").isTrue();
        filterPage.cancelInput();
        softAssertions.assertThat(filterPage.isSaveEnabled()).as("Verify Save button is disabled (Rename)").isFalse();
        softAssertions.assertThat(filterPage.getAllFilters()).as("Cancelled filter name not present in list").doesNotContain(cancelledFilterName);

        softAssertions.assertAll();
    }

    @Test
    @Issue("BA-2610")
    @TestRail(id = 6532)
    @Description("User can perform complex searches and be able to find the desired assembly scenario")
    public void advancedFilterTest() {

        final LocalDateTime testStart = LocalDateTime.now();

        String filterName = new GenerateStringUtil().generateFilterName();

        componentAssembly = new AssemblyRequestUtil().getAssembly();

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.costSubComponents(componentAssembly)
            .costAssembly(componentAssembly);

        ComponentInfoBuilder componentAssembly2 = new AssemblyRequestUtil().getAssembly();

        assemblyUtils.uploadSubComponents(componentAssembly2)
            .uploadAssembly(componentAssembly2);

        ComponentInfoBuilder componentAssembly3 = new AssemblyRequestUtil().getAssembly();

        assemblyUtils.uploadSubComponents(componentAssembly3)
            .uploadAssembly(componentAssembly3);

        componentsTreePage = new CidAppLoginPage(driver).login(componentAssembly.getUser())
            .openScenario(componentAssembly2.getComponentName(), componentAssembly2.getScenarioName())
            .openComponents()
            .selectCheckAllBox()
            .setInputs()
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_UNITED_KINGDOM)
            .selectProcessGroup(componentAssembly.getProcessGroup())
            .clickApplyAndCost(SetInputStatusPage.class)
            .close(ComponentsTreePage.class)
            .closePanel()
            .clickExplore()
            .openScenario(componentAssembly3.getComponentName(), componentAssembly3.getScenarioName())
            .openComponents()
            .selectCheckAllBox()
            .setInputs()
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_UNITED_KINGDOM)
            .selectProcessGroup(componentAssembly.getProcessGroup())
            .clickApplyAndCost(SetInputStatusPage.class)
            .close(ComponentsTreePage.class)
            .closePanel()
            .clickExplore()
            .openScenario(componentAssembly2.getComponentName(), componentAssembly2.getScenarioName())
            .openComponents();

        componentAssembly2.getSubComponents().forEach(subcomponent -> componentsTreePage.checkSubcomponentState(componentAssembly2, subcomponent.getComponentName()));

        componentsTreePage = componentsTreePage.closePanel()
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_UNITED_KINGDOM)
            .clickCostButton()
            .waitForCostLabelNotContain(NewCostingLabelEnum.COSTING_IN_PROGRESS, 2)
            .clickExplore()
            .openScenario(componentAssembly3.getComponentName(), componentAssembly3.getScenarioName())
            .openComponents();

        componentAssembly3.getSubComponents().forEach(subcomponent -> componentsTreePage.checkSubcomponentState(componentAssembly3, subcomponent.getComponentName()));

        explorePage = componentsTreePage.closePanel()
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_UNITED_KINGDOM)
            .goToAdvancedTab()
            .openSecondaryProcesses()
            .goToMachiningTab()
            .expandSecondaryProcessTree("Deburr")
            .selectSecondaryProcess("Automated Deburr")
            .submit(EvaluatePage.class)
            .clickCostButton()
            .waitForCostLabelNotContain(NewCostingLabelEnum.COSTING_IN_PROGRESS, 2)
            .clickExplore()
            .filter()
            .newFilter()
            .inputName(filterName)
            .addCriteria(PropertyEnum.CREATED_AT, OperationEnum.GREATER_THAN, testStart)
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.CONTAINS, componentAssembly.getComponentName().substring(0, 5))
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.CONTAINS, "AutoScenario")
            .addCriteria(PropertyEnum.DIGITAL_FACTORY, OperationEnum.IN, DigitalFactoryEnum.APRIORI_UNITED_KINGDOM.getDigitalFactory())
            .addCriteria(PropertyEnum.FULLY_BURDENED_COST, OperationEnum.GREATER_THAN, "0.4")
            .save(FilterPage.class)
            .submit(ExplorePage.class)
            .selectFilter(filterName);

        softAssertions.assertThat(explorePage.getAllScenarioComponentName().size()).as("Number of scenarios displayed").isEqualTo(1);
        softAssertions.assertAll();
    }
}
