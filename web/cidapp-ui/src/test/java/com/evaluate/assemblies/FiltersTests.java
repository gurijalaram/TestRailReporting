package com.evaluate.assemblies;

import com.apriori.FileResourceUtil;
import com.apriori.GenerateStringUtil;
import com.apriori.TestBaseUI;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.pageobjects.common.FilterPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsTablePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsTreePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;
import com.apriori.utils.enums.OperationEnum;
import com.apriori.utils.enums.PropertyEnum;

import com.utils.ColumnsEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class FiltersTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private ComponentsTablePage componentsTablePage;
    private ComponentsTreePage componentsTreePage;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
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
    @TestRail(id = {10538", "6168"})
        @Description("Verify that filter criteria can be deleted")
        public void filterCriteriaCanBeDeletedTest(){
        SoftAssertions soft=new SoftAssertions();
        String filterName=generateStringUtil.generateFilterName();
        currentUser = UserUtil.getUser();
        String scenarioName=new GenerateStringUtil().generateScenarioName();

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
            .selectTableView()
            .filter()
            .newFilter()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COST_MATURITY, OperationEnum.IN, "Medium")
            .clear();

        soft.assertThat(filterPage.isElementDisplayed("No queries applied", "message")).isTrue();

        soft.assertAll();
        }

        @Test
        @TestRail(id = {10537", "6167", "6083"})
        @Description("Verify that newly created filter is displayed in filters dropdown in my filter section")
        public void newlyCreatedFilterIsDisplayedInFiltersTest(){
        SoftAssertions soft=new SoftAssertions();
        currentUser = UserUtil.getUser();
        String scenarioName=new GenerateStringUtil().generateScenarioName();

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
            .selectTableView()
            .filter()
            .newFilter()
            .inputName(filterName2)
            .addCriteria(PropertyEnum.COST_MATURITY, OperationEnum.IN, "Medium")
            .submit(ExplorePage.class);

        soft.assertThat(explorePage.isElementDisplayed(filterName2, "text-overflow")).isTrue();

        soft.assertAll();
        }

        @Test
        @TestRail(id = {10535", "6165"})
        @Description("Verify Cancel button closes the Scenario filter table")
        public void cancelBtnCloseFilterTableTest(){
        SoftAssertions soft=new SoftAssertions();
        currentUser = UserUtil.getUser();
        String scenarioName=new GenerateStringUtil().generateScenarioName();

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
            .selectTableView()
            .filter()
            .cancel(ExplorePage.class);

        soft.assertThat(explorePage.isFilterTablePresent()).isFalse();

        soft.assertAll();
        }

        @Test
        @TestRail(id = {10534", "6164"})
        @Description("User can clear added criteria simultaneously by Clear button")
        public void canClearAddedCriteriaTest(){
        SoftAssertions soft=new SoftAssertions();
        String filterName=generateStringUtil.generateFilterName();
        currentUser = UserUtil.getUser();
        String scenarioName=new GenerateStringUtil().generateScenarioName();

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
            .selectTableView()
            .filter()
            .newFilter()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COST_MATURITY, OperationEnum.IN, "Medium")
            .addCriteria(PropertyEnum.COST_MATURITY, OperationEnum.IN, "Low")
            .deleteAllCriteria();

        soft.assertThat(filterPage.isElementDisplayed("No queries applied", "message")).isTrue();

        soft.assertAll();
        }

        @Test
        @TestRail(id = 10532")
        @Description("Validate that user can cancel action New before saving")
        public void canCancelBeforeSavingTest(){
        SoftAssertions soft=new SoftAssertions();
        String filterName=generateStringUtil.generateFilterName();
        currentUser = UserUtil.getUser();
        String scenarioName=new GenerateStringUtil().generateScenarioName();

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
            .selectTableView()
            .filter()
            .newFilter()
            .inputName(filterName)
            .cancel(FilterPage.class);

        soft.assertThat(filterPage.getAllFilters()).doesNotContain(filterName);

        soft.assertAll();
        }

        @Test
        @TestRail(id = {10531", "6099"})
        @Description("User can filter scenarios from scenario filter modal box")
        public void canFilterScenariosFromModalBoxTest(){
        SoftAssertions soft=new SoftAssertions();
        String filterName=generateStringUtil.generateFilterName();
        currentUser = UserUtil.getUser();
        String scenarioName=new GenerateStringUtil().generateScenarioName();

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
            .selectTableView()
            .filter()
            .newFilter()
            .inputName(filterName)
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.EQUALS, "BIG RING")
            .submit(ExplorePage.class);

        soft.assertThat(explorePage.getAllScenarioComponentName()).containsExactly("BIG RING");

        soft.assertAll();
        }

        @Test
        @TestRail(id = {10529", "6085"})
        @Description("User can create new filter from already existing one using Save As button")
        public void canCreateNewFilterBySaveAsTest(){
        SoftAssertions soft=new SoftAssertions();
        String filterName=generateStringUtil.generateFilterName();
        currentUser = UserUtil.getUser();
        String scenarioName=new GenerateStringUtil().generateScenarioName();

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
            .selectTableView()
            .filter()
            .selectFilter("Missing")
            .saveAs()
            .inputName(filterName)
            .submit(ExplorePage.class);

        soft.assertThat(explorePage.isElementDisplayed(filterName, "text-overflow")).isTrue();

        soft.assertAll();
        }

        @Test
        @TestRail(id = {10528", "6084"})
        @Description("User is able to edit already created filters")
        public void ableToEditCreatedFilterTest(){
        SoftAssertions soft=new SoftAssertions();
        String filterName=generateStringUtil.generateFilterName();
        String filterName2=generateStringUtil.generateFilterName();
        currentUser = UserUtil.getUser();
        String scenarioName=new GenerateStringUtil().generateScenarioName();

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
            .selectTableView()
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

        soft.assertAll();
        }

        @Test
        @TestRail(id = {10527", "6353"})
        @Description("Validate user can select custom filter")
        public void ableToSelectCustomFilterTest(){
        SoftAssertions soft=new SoftAssertions();
        String filterName=generateStringUtil.generateFilterName();
        String filterName2=generateStringUtil.generateFilterName();
        currentUser = UserUtil.getUser();
        String scenarioName=new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        componentsTablePage = loginPage.login(currentUser)
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

        soft.assertThat(componentsTablePage.isElementDisplayed(filterName, "text-overflow")).isTrue();

        soft.assertAll();
        }

        @Test
        @TestRail(id = {10526", "6081"})
        @Description("Validate user can create custom filter with all available attributes")
        public void ableToCreateCustomFilterWithAllAttributesTest(){
        String filterName=generateStringUtil.generateFilterName();
        currentUser = UserUtil.getUser();
        String scenarioName=new GenerateStringUtil().generateScenarioName();

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
            .selectTableView()
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

        List<String>operations6 =
        filterPage.getListOfOperationsForCriteria((PropertyEnum.FINISH_MASS));
        soft.assertThat(Arrays.asList("Equals", "Not Equal", "Greater Than", "Greater Than or Equal To",
        "Less Than", "Less Than or Equal To")).isEqualTo(operations6);

        soft.assertAll();
        }

        @Test
        @TestRail(id = 10525")
        @Description("Validate user can select Uncosted scenarios")
        public void ableToSelectUncostedScenarioTest(){
        SoftAssertions soft=new SoftAssertions();
        String scenarioName=new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        assembly = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, assemblyName + ".SLDASM");

        loginPage = new CidAppLoginPage(driver);
        componentsTablePage = loginPage.login(currentUser)
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
            .selectTableView()
            .selectFilter("Uncosted");

        List<String>stateListUncosted = componentsTablePage.getAllScenarioState();

        soft.assertThat(componentsTablePage.isElementDisplayed("Uncosted", "text-overflow")).isTrue();
        soft.assertThat(stateListUncosted).containsExactly("Uncosted", "Uncosted", "Uncosted");

        soft.assertAll();
        }

        @Test
        @TestRail(id = {10524", "6077"})
        @Description("Validate user can select Assigned to Me scenarios")
        public void ableToSelectAssignedToMeScenarioTest(){
        SoftAssertions soft=new SoftAssertions();
        String scenarioName=new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();
        assembly = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, assemblyName + ".SLDASM");

        loginPage = new CidAppLoginPage(driver);
        componentsTablePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(assemblyName, scenarioName, assembly, currentUser)
            .openComponents()
            .selectTableView()
            .selectFilter("Assigned To Me");

        soft.assertThat(componentsTablePage.isElementDisplayed("Assigned To Me", "text-overflow")).isTrue();
        soft.assertThat(componentsTablePage.getScenarioMessage()).contains("No scenarios found");

        soft.assertAll();
        }

        @Test
        @TestRail(id = 10523")
        @Description("Validate user can select Missing scenarios")
        public void ableToSelectMissingScenarioTest(){
        SoftAssertions soft=new SoftAssertions();
        String scenarioName=new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        ComponentInfoBuilder componentAssembly=assemblyUtils.associateAssemblyAndSubComponents(
        assemblyName,
        assemblyExtension,
        ProcessGroupEnum.ASSEMBLY,
            subComponentNames,
            componentExtension,
            ProcessGroupEnum.FORGING,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTablePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .selectFilter("Missing");

        soft.assertThat(componentsTablePage.isElementDisplayed("Missing", "text-overflow")).isTrue();
        soft.assertThat(componentsTablePage.getScenarioMessage()).contains("No scenarios found");

        soft.assertAll();
    }

@Test
@TestRail(id = {10522", "6531"})
    @Description("Validate user can select All scenarios")
    public void ableToSelectAllScenarioTest(){
    SoftAssertions soft=new SoftAssertions();
    String scenarioName=new GenerateStringUtil().generateScenarioName();
    currentUser = UserUtil.getUser();
    assembly = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, assemblyName + ".SLDASM");
    loginPage = new CidAppLoginPage(driver);

    componentsTablePage = loginPage.login(currentUser)
        .uploadComponentAndOpen(assemblyName, scenarioName, assembly, currentUser)
            .openComponents()
        .selectTableView()
        .selectFilter("All");

    soft.assertThat(componentsTablePage.isElementDisplayed("All", "text-overflow")).isTrue();
    soft.assertThat(componentsTablePage.getAllScenarioComponentName(3)).hasSize(3);

    soft.assertAll();
    }

    @Test
    @TestRail(id = {6075", "6080"})
    @Description("Validate Private filter displays only Private Scenarios")
    public void verifyFilterContentTest(){
    SoftAssertions softAssert=new SoftAssertions();
    LocalDateTime filterStartTime=LocalDateTime.now().minusHours(24);
    loginPage = new CidAppLoginPage(driver);
    currentUser = UserUtil.getUser();

    explorePage = loginPage.login(currentUser);

    explorePage.selectFilter("Private")
            .addColumn(ColumnsEnum.PUBLISHED)
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

    @Test
    @Issue("BA-2610")
    @TestRail(id = 6094")
        @Description("Validate Private filter displays only Private Scenarios")
        public void verifyFilterPersistenceTest(){
        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();
        String filterName=generateStringUtil.generateFilterName();

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
        @TestRail(id = 6100")
        @Description("Validate that user can cancel action New, Rename, Save As before saving")
        public void cancelFilterTest(){
        SoftAssertions soft=new SoftAssertions();

        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();
        String filterName=generateStringUtil.generateFilterName();
        String cancelledFilterName=generateStringUtil.generateFilterName();

        explorePage = loginPage.login(currentUser)
            .filter()
            .newFilter()
            .inputName(filterName)
            .submit(ExplorePage.class);

        filterPage = explorePage.filter()
            .newFilter()
            .inputName(cancelledFilterName);

        soft.assertThat(filterPage.isSaveEnabled()).as("Verify Save button is enabled (New)").isTrue();
        filterPage.cancelInput();
        soft.assertThat(filterPage.isSaveEnabled()).as("Verify Save button is disabled (New)").isFalse();

        filterPage.saveAs()
            .inputName(cancelledFilterName);
        soft.assertThat(filterPage.isSaveEnabled()).as("Verify Save button is enabled (Save As)").isTrue();
        filterPage.cancelInput();
        soft.assertThat(filterPage.isSaveEnabled()).as("Verify Save button is disabled (Save As)").isFalse();
        explorePage = filterPage.cancel(ExplorePage.class);

        filterPage = explorePage.filter()
            .selectFilter(filterName)
            .rename()
            .inputName(cancelledFilterName);
        soft.assertThat(filterPage.isSaveEnabled()).as("Verify Save button is enabled (Rename)").isTrue();
        filterPage.cancelInput();
        soft.assertThat(filterPage.isSaveEnabled()).as("Verify Save button is disabled (Rename)").isFalse();
        soft.assertThat(filterPage.getAllFilters()).as("Cancelled filter name not present in list").doesNotContain(cancelledFilterName);

        soft.assertAll();
        }

        @Test
        @Issue("BA-2610")
        @TestRail(id = 6532")
        @Description("User can perform complex searches and be able to find the desired assembly scenario")
        public void advancedFilterTest(){
        final ProcessGroupEnum subComponentProcessGroup=ProcessGroupEnum.FORGING;
        final ProcessGroupEnum assemblyProcessGroup=ProcessGroupEnum.ASSEMBLY;

        final LocalDateTime testStart=LocalDateTime.now();

        SoftAssertions soft=new SoftAssertions();

        currentUser = UserUtil.getUser();
        String scenarioName1 = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();
        String scenarioName3 = new GenerateStringUtil().generateScenarioName();
        String filterName = new GenerateStringUtil().generateFilterName();

        ComponentInfoBuilder componentAssembly1 = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            componentExtension,
            subComponentProcessGroup,
            scenarioName1,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly1)
            .uploadAssembly(componentAssembly1);
        assemblyUtils.costSubComponents(componentAssembly1)
            .costAssembly(componentAssembly1);

        ComponentInfoBuilder componentAssembly2 = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            componentExtension,
            subComponentProcessGroup,
            scenarioName2,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly2)
            .uploadAssembly(componentAssembly2);

        ComponentInfoBuilder componentAssembly3 = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            componentExtension,
            subComponentProcessGroup,
            scenarioName3,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly3)
            .uploadAssembly(componentAssembly3);

        componentsTreePage = new CidAppLoginPage(driver).login(currentUser)
            .openScenario(assemblyName, scenarioName2)
            .openComponents()
            .selectCheckAllBox()
            .setInputs()
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_UNITED_KINGDOM)
            .selectProcessGroup(subComponentProcessGroup)
            .clickApplyAndCost(SetInputStatusPage.class)
            .close(ComponentsTreePage.class)
            .closePanel()
            .clickExplore()
            .openScenario(assemblyName, scenarioName3)
            .openComponents()
            .selectCheckAllBox()
            .setInputs()
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_UNITED_KINGDOM)
            .selectProcessGroup(subComponentProcessGroup)
            .clickApplyAndCost(SetInputStatusPage.class)
            .close(ComponentsTreePage.class)
            .closePanel()
            .clickExplore()
            .openScenario(assemblyName, scenarioName2)
            .openComponents();

        subComponentNames.forEach(subcomponent -> componentsTreePage.checkSubcomponentState(componentAssembly2, subcomponent));

        componentsTreePage = componentsTreePage.closePanel()
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_UNITED_KINGDOM)
            .clickCostButton()
            .waitForCostLabelNotContain(NewCostingLabelEnum.COSTING_IN_PROGRESS, 2)
            .clickExplore()
            .openScenario(assemblyName, scenarioName3)
            .openComponents();

        subComponentNames.forEach(subcomponent -> componentsTreePage.checkSubcomponentState(componentAssembly3, subcomponent));

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
            .addCriteria(PropertyEnum.COMPONENT_NAME, OperationEnum.CONTAINS, assemblyName.substring(0, 5))
            .addCriteria(PropertyEnum.SCENARIO_NAME, OperationEnum.CONTAINS, "AutoScenario")
            .addCriteria(PropertyEnum.DIGITAL_FACTORY, OperationEnum.IN, DigitalFactoryEnum.APRIORI_UNITED_KINGDOM.getDigitalFactory())
            .addCriteria(PropertyEnum.FULLY_BURDENED_COST, OperationEnum.GREATER_THAN, "0.4")
            .save(FilterPage.class)
            .submit(ExplorePage.class)
            .selectFilter(filterName);

        soft.assertThat(explorePage.getAllScenarioComponentName().size()).as("Number of scenarios displayed").isEqualTo(1);
        soft.assertAll();
    }
}
