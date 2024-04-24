package com.apriori.cis.ui.tests.partsandassemblies;

import com.apriori.cis.ui.navtoolbars.LeftHandNavigationBar;
import com.apriori.cis.ui.pageobjects.login.CisLoginPage;
import com.apriori.cis.ui.pageobjects.partsandassemblies.PartsAndAssembliesPage;
import com.apriori.cis.ui.pageobjects.partsandassembliesdetails.PartsAndAssembliesDetailsPage;
import com.apriori.cis.ui.pageobjects.projectsdetails.ProjectsDetailsPage;
import com.apriori.cis.ui.utils.CisColumnsEnum;
import com.apriori.cis.ui.utils.CisCostDetailsEnum;
import com.apriori.cis.ui.utils.CisFilterOperationEnum;
import com.apriori.serialization.util.DateFormattingUtils;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.AssemblyRequestUtil;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.DateUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class PartsAndAssemblyTest extends TestBaseUI {

    private CisLoginPage loginPage;
    private LeftHandNavigationBar leftHandNavigationBar;
    private PartsAndAssembliesPage partsAndAssembliesPage;
    private PartsAndAssembliesDetailsPage partsAndAssembliesDetailsPage;
    private File resourceFile;
    private UserCredentials currentUser;
    private ProjectsDetailsPage projectsDetailsPage;
    private String projectParticipant;
    private SoftAssertions softAssertions;
    private static final String assemblyName = "Hinge assembly";
    private static final String componentName = "ChampferOut";

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        loginPage = new CisLoginPage(driver);
    }

    @Test
    @TestRail(id = {12058, 12056, 12057, 12055})
    @Description("Verify the fields included in the table and page title")
    public void testPartsAndAssemblyTableHeader() {
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
            .clickPartsAndAssemblies();

        leftHandNavigationBar = new LeftHandNavigationBar(driver);

        leftHandNavigationBar.collapseNavigationPanel();

        softAssertions.assertThat(partsAndAssembliesPage.getHeaderText()).isEqualTo("Parts & Assemblies");

        softAssertions.assertThat(partsAndAssembliesPage.isPartAndAssembliesTableDisplayed()).isEqualTo(true);

        softAssertions.assertThat(partsAndAssembliesPage.getTableHeaders()).contains(CisColumnsEnum.COMPONENT_NAME.getColumns(), CisColumnsEnum.SCENARIO_NAME.getColumns(),
            CisColumnsEnum.COMPONENT_TYPE.getColumns(), CisColumnsEnum.STATE.getColumns(), CisColumnsEnum.PROCESS_GROUP.getColumns(), CisColumnsEnum.DIGITAL_FACTORY.getColumns(), CisColumnsEnum.CREATED_AT.getColumns(),
            CisColumnsEnum.CREATED_BY.getColumns(), CisColumnsEnum.ANNUAL_VOLUME.getColumns(), CisColumnsEnum.BATCH_SIZE.getColumns(), CisColumnsEnum.DFM_RISK.getColumns(), CisColumnsEnum.STOCK_FORM.getColumns());
    }

    @Test
    @TestRail(id = {12064})
    @Description("Verify that user can check a table row")
    public void testCheckTableRow() {
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
            .clickPartsAndAssemblies();

        softAssertions.assertThat(partsAndAssembliesPage.getComponentCheckBoxStatus()).isEqualTo("true");
    }

    @Test
    @TestRail(id = {12188, 12174, 12175, 12189})
    @Description("Verify that user can hide all and Show all the fields by selecting 'HIDE ALL' and 'SHOW ALL' options")
    public void testShowHideFieldsOption() {
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
            .clickPartsAndAssemblies();

        leftHandNavigationBar = new LeftHandNavigationBar(driver);

        leftHandNavigationBar.collapseNavigationPanel();
        softAssertions.assertThat(partsAndAssembliesPage.isShowHideOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesPage.clickOnShowHideOption();
        softAssertions.assertThat(partsAndAssembliesPage.isShowHideModalDisplayed()).isEqualTo(true);

        partsAndAssembliesPage.clickOnHideAllButton();
        softAssertions.assertThat(partsAndAssembliesPage.getTableHeaders()).contains(CisColumnsEnum.COMPONENT_NAME.getColumns());

        partsAndAssembliesPage.clickOnShowAllOption();
        softAssertions.assertThat(partsAndAssembliesPage.getTableHeaders()).contains(CisColumnsEnum.COMPONENT_NAME.getColumns(), CisColumnsEnum.SCENARIO_NAME.getColumns(),
            CisColumnsEnum.COMPONENT_TYPE.getColumns(), CisColumnsEnum.STATE.getColumns(), CisColumnsEnum.PROCESS_GROUP.getColumns(), CisColumnsEnum.DIGITAL_FACTORY.getColumns(), CisColumnsEnum.CREATED_AT.getColumns(),
            CisColumnsEnum.CREATED_BY.getColumns(), CisColumnsEnum.ANNUAL_VOLUME.getColumns(), CisColumnsEnum.BATCH_SIZE.getColumns(), CisColumnsEnum.DFM_RISK.getColumns(), CisColumnsEnum.STOCK_FORM.getColumns());
    }

    @Test
    @TestRail(id = {12178, 12185, 12186, 12176})
    @Description("Verify that user can search a field by its name and hide/show the field")
    public void testSearchAFieldToShowHide() {
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
            .clickPartsAndAssemblies()
            .clickOnShowHideOption()
            .enterFieldName(CisColumnsEnum.COMPONENT_NAME.getColumns());

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(partsAndAssembliesPage.getFieldName()).isEqualTo(CisColumnsEnum.COMPONENT_NAME.getColumns());

        partsAndAssembliesPage.clickOnToggleButton();
        softAssertions.assertThat(partsAndAssembliesPage.getTableHeaders()).doesNotContain(CisColumnsEnum.COMPONENT_NAME.getColumns());

        partsAndAssembliesPage.clickOnToggleButton();
        softAssertions.assertThat(partsAndAssembliesPage.getTableHeaders()).contains(CisColumnsEnum.COMPONENT_NAME.getColumns());

        partsAndAssembliesPage.clickOnShowHideOption();
        softAssertions.assertThat(partsAndAssembliesPage.isShowHideModalDisplayed()).isEqualTo(false);
    }

    @Test
    @TestRail(id = {12239, 12241, 12242, 12243})
    @Description("Verify the availability and functionality of the 'pin to left' option")
    public void testPintoLeftOption() {
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
            .clickPartsAndAssemblies()
            .clickKebabMenuOnTableHeader();

        softAssertions.assertThat(partsAndAssembliesPage.isPinToLeftOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesPage.clickPinToLeft();

        softAssertions.assertThat(partsAndAssembliesPage.getPinnedTableHeaders()).contains(CisColumnsEnum.COMPONENT_TYPE.getColumns());

        partsAndAssembliesPage.clickKebabMenuOnTableHeader();

        softAssertions.assertThat(partsAndAssembliesPage.isUnPinOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesPage.clickOnUnpinOption();

        softAssertions.assertThat(partsAndAssembliesPage.getTableHeaders()).contains(CisColumnsEnum.COMPONENT_TYPE.getColumns());
    }

    @Test
    @TestRail(id = {12110, 12111, 12112, 12113, 12115})
    @Description("Verify that Parts and Assemblies can search by Component Name")
    public void testSearchByComponentName() {
        ComponentInfoBuilder componentInfoBuilder = new ComponentRequestUtil().getComponentWithProcessGroup(componentName, ProcessGroupEnum.SHEET_METAL);
        currentUser = componentInfoBuilder.getUser();
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentInfoBuilder)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField();
        softAssertions.assertThat(partsAndAssembliesPage.isSearchOptionDisplayed()).isEqualTo("true");

        partsAndAssembliesPage.clickSearchOption();

        softAssertions.assertThat(partsAndAssembliesPage.isSearchFieldDisplayed()).isEqualTo("true");

        partsAndAssembliesPage.clickOnSearchField();

        partsAndAssembliesPage.enterAComponentName(componentName);

        softAssertions.assertThat(partsAndAssembliesPage.getAddedComponentName()).isEqualTo(componentName);
        softAssertions.assertThat(partsAndAssembliesPage.getListOfScenarios(componentName, componentInfoBuilder.getScenarioName())).isEqualTo(1);

        partsAndAssembliesPage.clickClearOption();
        softAssertions.assertThat(partsAndAssembliesPage.getListOfComponents()).isNotEqualTo(1);
    }

    @Test
    @TestRail(id = {12066, 13263, 12063, 12065, 12067})
    @Description("Verify that user can check all rows and uncheck all rows")
    public void testCheckAllOption() {
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
            .clickPartsAndAssemblies();

        partsAndAssembliesPage.clickCheckAll();
        softAssertions.assertThat(partsAndAssembliesPage.getCheckAllStatus()).contains("Mui-checked");

        partsAndAssembliesPage.clickMessages()
            .clickPartsAndAssemblies();
        softAssertions.assertThat(partsAndAssembliesPage.getCheckAllStatus()).doesNotContain("Mui-checked");

        partsAndAssembliesPage.clickCheckAll();
        softAssertions.assertThat(partsAndAssembliesPage.getCheckAllStatus()).contains("Mui-checked");

        partsAndAssembliesPage.clickCheckAll();
        softAssertions.assertThat(partsAndAssembliesPage.getCheckAllStatus()).doesNotContain("Mui-checked");
    }

    @Test
    @TestRail(id = {12221, 12224, 12226, 12227, 12233, 13200, 12230})
    @Description("Verify that user can filter results in parts and assemblies page")
    public void testFilterAComponent() {
        ComponentInfoBuilder componentInfo = new ComponentRequestUtil().getComponentWithProcessGroup(componentName, ProcessGroupEnum.SHEET_METAL);
        currentUser = componentInfo.getUser();
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentInfo)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField();

        softAssertions.assertThat(partsAndAssembliesPage.isFilterModalDisplayed()).isEqualTo(true);

        partsAndAssembliesPage.clickAddCondition();

        softAssertions.assertThat(partsAndAssembliesPage.isFilterFieldDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesPage.isFilterTypeDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesPage.isFilterValueDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesPage.isFilterClearIconDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesPage.selectComponentNameField()).isEqualTo("componentName");

        partsAndAssembliesPage.addFilterValue(componentInfo.getComponentName(), componentInfo.getScenarioName());

        softAssertions.assertThat(partsAndAssembliesPage.getFilteredComponentName()).isEqualTo(componentInfo.getScenarioName());
        softAssertions.assertThat(partsAndAssembliesPage.getListOfScenarios(componentInfo.getComponentName(), componentInfo.getScenarioName())).isEqualTo(1);

        partsAndAssembliesPage.clickToRemoveAddedFilter();

        softAssertions.assertThat(partsAndAssembliesPage.getListOfComponents()).isNotEqualTo(1);
    }

    @Test
    @TestRail(id = {12247, 12248, 12249})
    @Description("Verify that user can set a single sorting rule as ascending or descending order")
    public void testSortPartAndAssemblyTable() {
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
            .clickPartsAndAssemblies();
        softAssertions.assertThat(partsAndAssembliesPage.getSortingRule()).isEqualTo("ascending");
        softAssertions.assertThat(partsAndAssembliesPage.getSortingStatus()).isEqualTo("sort-up");
        softAssertions.assertThat(partsAndAssembliesPage.getSortingRule()).isEqualTo("descending");
        softAssertions.assertThat(partsAndAssembliesPage.getSortingStatus()).isEqualTo("sort-down");
    }

    @Test
    @TestRail(id = {12191, 12465, 12467, 12468, 12469, 12470, 12476})
    @Description("Verify that user can Save the parts and assemblies page configuration")
    public void testSaveConfigurations() {
        ComponentInfoBuilder componentInfo = new ComponentRequestUtil().getComponentWithProcessGroup(componentName, ProcessGroupEnum.SHEET_METAL);
        currentUser = componentInfo.getUser();
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentInfo)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .pinToLeftProcessGroupColumn()
            .clickOnShowHideOption()
            .enterFieldName(CisColumnsEnum.STATE.getColumns())
            .disableStateField();

        softAssertions.assertThat(partsAndAssembliesPage.getPinnedTableHeaders()).contains(CisColumnsEnum.PROCESS_GROUP.getColumns());
        softAssertions.assertThat(partsAndAssembliesPage.getTableHeaders()).doesNotContain(CisColumnsEnum.STATE.getColumns());
        softAssertions.assertThat(partsAndAssembliesPage.getDigitalFactorySortingRule()).isEqualTo("sort-up");

        partsAndAssembliesPage.clickMessages()
            .clickPartsAndAssemblies();

        softAssertions.assertThat(partsAndAssembliesPage.getPinnedTableHeaders()).contains(CisColumnsEnum.PROCESS_GROUP.getColumns());
        softAssertions.assertThat(partsAndAssembliesPage.getTableHeaders()).doesNotContain(CisColumnsEnum.STATE.getColumns());
        softAssertions.assertThat(partsAndAssembliesPage.getDigitalFactorySortingRule()).isEqualTo("sort-up");

        partsAndAssembliesPage.clickOnShowHideOption()
            .enterFieldName(CisColumnsEnum.STATE.getColumns())
            .enableStateField()
            .sortUpDigitalFactoryField()
            .pinToRightProcessGroupColumn();

        softAssertions.assertThat(partsAndAssembliesPage.getListOfComponents()).isNotEqualTo(1);
        softAssertions.assertThat(partsAndAssembliesPage.getTableHeaders()).contains(CisColumnsEnum.PROCESS_GROUP.getColumns());
        softAssertions.assertThat(partsAndAssembliesPage.getTableHeaders()).contains(CisColumnsEnum.STATE.getColumns());
        softAssertions.assertThat(partsAndAssembliesPage.getDigitalFactorySortingRule()).isEqualTo("sort-down");
    }

    @Test
    @TestRail(id = {12225, 12228, 12229, 12349, 12350})
    @Description("Verify that user can see filter operations in parts and assemblies filter modal")
    public void testFilterOperations() {
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
            .clickPartsAndAssemblies()
            .clickFilterOption()
            .clickAddCondition()
            .selectFilterField(CisColumnsEnum.COMPONENT_NAME.getColumns());

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(partsAndAssembliesPage.getOperationList()).contains(CisFilterOperationEnum.CONTAINS.getOperation());

        partsAndAssembliesPage.clickFilterOption()
            .selectFilterField(CisColumnsEnum.BATCH_SIZE.getColumns());
        softAssertions.assertThat(partsAndAssembliesPage.getOperationList()).contains(CisFilterOperationEnum.EQUALS.getOperation(),
            CisFilterOperationEnum.NOT_EQUALS.getOperation(),
            CisFilterOperationEnum.GREATER_THAN.getOperation(),
            CisFilterOperationEnum.GREATER_THAN_OR_EQUAL_TO.getOperation(),
            CisFilterOperationEnum.LESS_THAN.getOperation(),
            CisFilterOperationEnum.LESS_THAN_OR_EQUAL_TO.getOperation());

        partsAndAssembliesPage.clickFilterOption()
            .selectFilterField(CisColumnsEnum.STATE.getColumns());
        softAssertions.assertThat(partsAndAssembliesPage.getOperationList()).contains(CisFilterOperationEnum.IS_ANY_OF.getOperation(), CisFilterOperationEnum.IS_NONE_OF.getOperation(), CisFilterOperationEnum.IS_EMPTY.getOperation());

        partsAndAssembliesPage.clickFilterOption()
            .selectFilterField(CisColumnsEnum.CREATED_AT.getColumns());
        softAssertions.assertThat(partsAndAssembliesPage.getOperationList()).contains(CisFilterOperationEnum.IS_BEFORE.getOperation(), CisFilterOperationEnum.IS_AFTER.getOperation());

        partsAndAssembliesPage.removeFilterModal();
        softAssertions.assertThat(partsAndAssembliesPage.isFilterModalDisplayed()).isEqualTo(false);
    }

    @Test
    @TestRail(id = {26123, 26124, 26125, 26126, 26128})
    @Description("Verify that user can add new parts & assemblies after a project creation")
    public void testAddNewPartsAndAssembliesAfterCreation() {
        SoftAssertions softAssertions = new SoftAssertions();
        ComponentInfoBuilder assemblyInfo = new AssemblyRequestUtil().getAssembly(assemblyName);
        String dateTime = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);
        String assemblyExtension = assemblyInfo.getExtension();
        ProcessGroupEnum assemblyProcessGroup = assemblyInfo.getProcessGroup();
        List<String> subComponentNames = assemblyInfo.getSubComponents().stream()
            .map(ComponentInfoBuilder::getComponentName)
            .collect(Collectors.toList());
        String subComponentExtension = assemblyInfo.getSubComponents().get(0).getExtension();
        ProcessGroupEnum subComponentProcessGroup = assemblyInfo.getSubComponents().get(0).getProcessGroup();
        String scenarioName = assemblyInfo.getScenarioName();
        currentUser = UserUtil.getUser();
        projectParticipant = currentUser.getEmail();

        projectsDetailsPage = new CisLoginPage(driver).cisLogin(currentUser)
            .uploadAndCostAssembly(assemblyName,
                assemblyExtension,
                assemblyProcessGroup,
                subComponentNames,
                subComponentExtension,
                subComponentProcessGroup,
                scenarioName,
                currentUser)
            .clickProjects()
            .clickOnCreateNewProject()
            .createANewProjectAndOpen("Automation Project " + dateTime, "This Project is created by Automation User " + currentUser.getEmail(),
                scenarioName, assemblyName, projectParticipant, "2028", "15", "Details");

        softAssertions.assertThat(projectsDetailsPage.isProjectDetailsDisplayed("Owner")).isNotEmpty();
        softAssertions.assertThat(projectsDetailsPage.isProjectDetailsDisplayed("Due Date")).isNotEmpty();
        softAssertions.assertThat(projectsDetailsPage.getProjectDetailsTabTitle()).contains("Details");
        softAssertions.assertThat(projectsDetailsPage.isProjectDetailsDisplayed("Name")).contains("Automation Project " + dateTime);
        softAssertions.assertThat(projectsDetailsPage.isProjectDetailsDisplayed("Description")).contains("This Project is created by Automation User " + currentUser.getEmail());

        projectsDetailsPage.clickDetailsPageTab("Parts & Assemblies");

        softAssertions.assertThat(projectsDetailsPage.isAddPartsOptionDisplayed()).isEqualTo(true);

        projectsDetailsPage.clickOnAddParts();

        softAssertions.assertThat(projectsDetailsPage.isAddPartsModalDisplayed()).isEqualTo(true);
        softAssertions.assertThat(projectsDetailsPage.isShowHideOptionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(projectsDetailsPage.isSearchOptionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(projectsDetailsPage.getPinnedTableHeaders()).contains(CisColumnsEnum.COMPONENT_NAME.getColumns(), CisColumnsEnum.SCENARIO_NAME.getColumns());
        softAssertions.assertThat(projectsDetailsPage.getTableHeaders()).contains(CisColumnsEnum.COMPONENT_TYPE.getColumns(), CisColumnsEnum.STATE.getColumns(), CisColumnsEnum.PROCESS_GROUP.getColumns(), CisColumnsEnum.DIGITAL_FACTORY.getColumns(), CisColumnsEnum.CREATED_AT.getColumns(),
            CisColumnsEnum.CREATED_BY.getColumns(), CisColumnsEnum.ANNUAL_VOLUME.getColumns(), CisColumnsEnum.BATCH_SIZE.getColumns());

        projectsDetailsPage.selectAPart(scenarioName, subComponentNames.get(0)).clickAdd();

        softAssertions.assertThat(projectsDetailsPage.getListOfScenarios(subComponentNames.get(0), scenarioName)).isEqualTo(1);

        projectsDetailsPage.clickOnAllProjects().clickOnCreatedProject().clickDeleteProject().clickModalDeleteProject();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {24448, 26135, 26136, 26137})
    @Description("Verify that user can remove parts & assemblies after a project creation")
    public void testRemovePartsAndAssembliesAfterCreation() {
        SoftAssertions softAssertions = new SoftAssertions();
        ComponentInfoBuilder assemblyInfo = new AssemblyRequestUtil().getAssembly(assemblyName);
        String dateTime = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);
        String assemblyExtension = assemblyInfo.getExtension();
        ProcessGroupEnum assemblyProcessGroup = assemblyInfo.getProcessGroup();
        List<String> subComponentNames = assemblyInfo.getSubComponents().stream()
            .map(ComponentInfoBuilder::getComponentName)
            .collect(Collectors.toList());
        String subComponentExtension = assemblyInfo.getSubComponents().get(0).getExtension();
        ProcessGroupEnum subComponentProcessGroup = assemblyInfo.getSubComponents().get(0).getProcessGroup();
        String scenarioName = assemblyInfo.getScenarioName();
        currentUser = UserUtil.getUser();
        projectParticipant = currentUser.getEmail();

        projectsDetailsPage = new CisLoginPage(driver).cisLogin(currentUser)
            .uploadAndCostAssembly(assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser)
            .clickProjects()
            .clickOnCreateNewProject()
            .createANewProjectAndOpen("Automation Project " + dateTime, "This Project is created by Automation User " + currentUser.getEmail(),
                scenarioName, assemblyName, projectParticipant, "2028", "15", "Details");

        projectsDetailsPage.clickDetailsPageTab("Parts & Assemblies");

        projectsDetailsPage.checkAllComponents();

        softAssertions.assertThat(projectsDetailsPage.getCheckAllStatus()).contains("Mui-checked");
        softAssertions.assertThat(projectsDetailsPage.getStatusOfRemoveOption()).contains("Mui-disabled");

        projectsDetailsPage.checkAllComponents();

        softAssertions.assertThat(projectsDetailsPage.isAddPartsOptionDisplayed()).isEqualTo(true);

        projectsDetailsPage.clickOnAddParts()
            .selectAPart(scenarioName, subComponentNames.get(0))
            .selectAPart(scenarioName, subComponentNames.get(1))
            .clickAdd();

        softAssertions.assertThat(projectsDetailsPage.getListOfScenarios(subComponentNames.get(0), scenarioName)).isEqualTo(1);

        projectsDetailsPage.selectAPart(scenarioName, subComponentNames.get(0));

        softAssertions.assertThat(projectsDetailsPage.isRemoveSelectedPartOptionDisplayed()).isEqualTo(true);

        projectsDetailsPage.clickRemoveOption();

        softAssertions.assertThat(projectsDetailsPage.isRemovePartConfirmationModalDisplayed()).isEqualTo(true);
        softAssertions.assertThat(projectsDetailsPage.getRemovePartConfirmationMessage()).contains("Are you sure you want to remove selected part from the project?");

        projectsDetailsPage.clickOnRemove();
        softAssertions.assertThat(projectsDetailsPage.getListOfScenarios(subComponentNames.get(0), scenarioName)).isEqualTo(1);
        projectsDetailsPage.clickOnAllProjects().clickOnCreatedProject().clickDeleteProject().clickModalDeleteProject();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14040, 14041, 14042, 14043, 14044, 14045, 14046, 14047})
    @Description("Verify assembly cost details breakdowns")
    public void testAssemblyCostDetails() {
        ComponentInfoBuilder assemblyInfo = new AssemblyRequestUtil().getAssembly(assemblyName);
        String assemblyExtension = assemblyInfo.getExtension();
        ProcessGroupEnum assemblyProcessGroup = assemblyInfo.getProcessGroup();
        List<String> subComponentNames = assemblyInfo.getSubComponents().stream()
            .map(ComponentInfoBuilder::getComponentName)
            .collect(Collectors.toList());
        String subComponentExtension = assemblyInfo.getSubComponents().get(0).getExtension();
        ProcessGroupEnum subComponentProcessGroup = assemblyInfo.getSubComponents().get(0).getProcessGroup();
        String scenarioName = assemblyInfo.getScenarioName();
        currentUser = UserUtil.getUser();
        partsAndAssembliesPage = new CisLoginPage(driver)
            .cisLogin(currentUser)
            .uploadAndCostAssembly(assemblyName,
                assemblyExtension,
                assemblyProcessGroup,
                subComponentNames,
                subComponentExtension,
                subComponentProcessGroup,
                scenarioName,
                currentUser)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(assemblyName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponent(assemblyName, scenarioName);

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isAssemblyCostsOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.clickCostsOption();
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isCostSectionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getCostTitle()).isEqualTo("Cost");

        partsAndAssembliesDetailsPage.selectCostDropDownOption(CisCostDetailsEnum.ASSEMBLY_PROCESS_COST.getProcessRoutingName());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isAssemblyProcessCostCostGraphDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.selectCostDropDownOption(CisCostDetailsEnum.COMPONENT_COST_FULLY_BURDENED.getProcessRoutingName());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isComponentCostFullyBurdenedCostGraphDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.selectCostDropDownOption(CisCostDetailsEnum.COMPONENT_COST_PIECE_PART.getProcessRoutingName());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isComponentCostPiecePartCostGraphDisplayed()).isEqualTo(true);

        partsAndAssembliesDetailsPage.selectCostDropDownOption(CisCostDetailsEnum.TOTAL_COST.getProcessRoutingName());
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isTotalCostGraphDisplayed()).isEqualTo(true);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {13243, 13244, 13245, 13248, 13485, 13488})
    @Description("Verify assembly tree view")
    public void testAssemblyTreeView() {
        SoftAssertions softAssertions = new SoftAssertions();
        ComponentInfoBuilder assemblyInfo = new AssemblyRequestUtil().getAssembly(assemblyName);
        String assemblyExtension = assemblyInfo.getExtension();
        ProcessGroupEnum assemblyProcessGroup = assemblyInfo.getProcessGroup();
        List<String> subComponentNames = assemblyInfo.getSubComponents().stream()
            .map(ComponentInfoBuilder::getComponentName)
            .collect(Collectors.toList());
        String subComponentExtension = assemblyInfo.getSubComponents().get(0).getExtension();
        ProcessGroupEnum subComponentProcessGroup = assemblyInfo.getSubComponents().get(0).getProcessGroup();
        String scenarioName = assemblyInfo.getScenarioName();
        currentUser = UserUtil.getUser();
        projectParticipant = currentUser.getEmail();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostAssembly(assemblyName,
                assemblyExtension,
                assemblyProcessGroup,
                subComponentNames,
                subComponentExtension,
                subComponentProcessGroup,
                scenarioName,
                currentUser)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .clickSearchOption()
            .clickOnSearchField()
            .enterAComponentName(assemblyName);

        partsAndAssembliesDetailsPage = partsAndAssembliesPage.clickOnComponent(assemblyName, scenarioName)
            .clickAssemblyTree();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.isAssemblyTreeIconDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.isAssemblyTreeViewDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getTableHeaders()).contains(CisColumnsEnum.COMPONENT_NAME.getColumns(), CisColumnsEnum.SCENARIO_NAME.getColumns(),
            CisColumnsEnum.COMPONENT_TYPE.getColumns(), CisColumnsEnum.STATE.getColumns(), CisColumnsEnum.PROCESS_GROUP.getColumns());

        partsAndAssembliesDetailsPage.clickShowHideOption()
            .hideField("State");

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getTableHeaders()).doesNotContain(CisColumnsEnum.STATE.getColumns());

        partsAndAssembliesDetailsPage.openAssembly("Pin", scenarioName);

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getSubComponentName()).isEqualTo("Pin");
        softAssertions.assertAll();
    }

    @AfterEach
    public void afterTest() {
        softAssertions.assertAll();
    }
}


