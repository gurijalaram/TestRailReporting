package com.apriori.cis.ui.tests.partsandassemblies;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

import com.apriori.cis.ui.navtoolbars.LeftHandNavigationBar;
import com.apriori.cis.ui.pageobjects.login.CisLoginPage;
import com.apriori.cis.ui.pageobjects.partsandassemblies.PartsAndAssembliesPage;
import com.apriori.cis.ui.utils.CisColumnsEnum;
import com.apriori.cis.ui.utils.CisFilterOperationEnum;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class PartsAndAssemblyTest extends TestBaseUI {

    private CisLoginPage loginPage;
    private LeftHandNavigationBar leftHandNavigationBar;
    private PartsAndAssembliesPage partsAndAssembliesPage;
    private SoftAssertions softAssertions;
    private File resourceFile;
    private UserCredentials currentUser;

    public PartsAndAssemblyTest() {
        super();
    }

    @Test
    @TestRail(id = {12058, 12056, 12057, 12055})
    @Description("Verify the fields included in the table and page title")
    public void testPartsAndAssemblyTableHeader() {
        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
            .clickPartsAndAssemblies();

        leftHandNavigationBar = new LeftHandNavigationBar(driver);

        leftHandNavigationBar.collapseNavigationPanel();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesPage.getHeaderText()).isEqualTo("Parts & Assemblies");

        softAssertions.assertThat(partsAndAssembliesPage.isPartAndAssembliesTableDisplayed()).isEqualTo(true);

        softAssertions.assertThat(partsAndAssembliesPage.getTableHeaders()).contains(CisColumnsEnum.COMPONENT_NAME.getColumns(), CisColumnsEnum.SCENARIO_NAME.getColumns(),
            CisColumnsEnum.COMPONENT_TYPE.getColumns(), CisColumnsEnum.STATE.getColumns(), CisColumnsEnum.PROCESS_GROUP.getColumns(), CisColumnsEnum.DIGITAL_FACTORY.getColumns(), CisColumnsEnum.CREATED_AT.getColumns(),
            CisColumnsEnum.CREATED_BY.getColumns(), CisColumnsEnum.ANNUAL_VOLUME.getColumns(), CisColumnsEnum.BATCH_SIZE.getColumns(), CisColumnsEnum.DFM_RISK.getColumns(), CisColumnsEnum.STOCK_FORM.getColumns());

        softAssertions.assertAll();

    }

    @Test
    @TestRail(id = {12064})
    @Description("Verify that user can check a table row")
    public void testCheckTableRow() {
        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
            .clickPartsAndAssemblies();

        assertThat(partsAndAssembliesPage.getComponentCheckBoxStatus(), is(equalTo("true")));

    }

    @Test
    @TestRail(id = {12188, 12174, 12175, 12189})
    @Description("Verify that user can hide all and Show all the fields by selecting 'HIDE ALL' and 'SHOW ALL' options")
    public void testShowHideFieldsOption() {
        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
            .clickPartsAndAssemblies();

        leftHandNavigationBar = new LeftHandNavigationBar(driver);

        leftHandNavigationBar.collapseNavigationPanel();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesPage.isShowHideOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesPage.clickOnShowHideOption();

        softAssertions.assertThat(partsAndAssembliesPage.isShowHideModalDisplayed()).isEqualTo(true);

        partsAndAssembliesPage.clickOnHideAllButton();

        softAssertions.assertThat(partsAndAssembliesPage.getTableHeaders()).contains(CisColumnsEnum.COMPONENT_NAME.getColumns());

        partsAndAssembliesPage.clickOnShowAllOption();

        softAssertions.assertThat(partsAndAssembliesPage.getTableHeaders()).contains(CisColumnsEnum.COMPONENT_NAME.getColumns(), CisColumnsEnum.SCENARIO_NAME.getColumns(),
            CisColumnsEnum.COMPONENT_TYPE.getColumns(), CisColumnsEnum.STATE.getColumns(), CisColumnsEnum.PROCESS_GROUP.getColumns(), CisColumnsEnum.DIGITAL_FACTORY.getColumns(), CisColumnsEnum.CREATED_AT.getColumns(),
            CisColumnsEnum.CREATED_BY.getColumns(), CisColumnsEnum.ANNUAL_VOLUME.getColumns(), CisColumnsEnum.BATCH_SIZE.getColumns(), CisColumnsEnum.DFM_RISK.getColumns(), CisColumnsEnum.STOCK_FORM.getColumns());

        softAssertions.assertAll();

    }

    @Test
    @TestRail(id = {12178, 12185, 12186, 12176})
    @Description("Verify that user can search a field by its name and hide/show the field")
    public void testSearchAFieldToShowHide() {
        loginPage = new CisLoginPage(driver);
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

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {12239, 12241, 12242, 12243})
    @Description("Verify the availability and functionality of the 'pin to left' option")
    public void testPintoLeftOption() {
        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
            .clickPartsAndAssemblies()
            .clickKebabMenuOnTableHeader();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesPage.isPinToLeftOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesPage.clickPinToLeft();

        softAssertions.assertThat(partsAndAssembliesPage.getPinnedTableHeaders()).contains(CisColumnsEnum.COMPONENT_TYPE.getColumns());

        partsAndAssembliesPage.clickKebabMenuOnTableHeader();

        softAssertions.assertThat(partsAndAssembliesPage.isUnPinOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesPage.clickOnUnpinOption();

        softAssertions.assertThat(partsAndAssembliesPage.getTableHeaders()).contains(CisColumnsEnum.COMPONENT_TYPE.getColumns());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {12110, 12111, 12112, 12113, 12115})
    @Description("Verify that Parts and Assemblies can search by Component Name")
    public void testSearchByComponentName() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField();

        assertThat(partsAndAssembliesPage.isSearchOptionDisplayed(), is(true));

        partsAndAssembliesPage.clickSearchOption();

        assertThat(partsAndAssembliesPage.isSearchFieldDisplayed(), is(true));

        partsAndAssembliesPage.clickOnSearchField();

        partsAndAssembliesPage.enterAComponentName(componentName);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesPage.getAddedComponentName()).isEqualTo(componentName);
        softAssertions.assertThat(partsAndAssembliesPage.getListOfScenarios(componentName, scenarioName)).isEqualTo(1);

        softAssertions.assertAll();

        partsAndAssembliesPage.clickClearOption();
        assertThat(partsAndAssembliesPage.getListOfComponents(), is(not(equalTo(1))));

    }

    @Test
    @TestRail(id = {12066, 13263, 12063, 12065, 12067})
    @Description("Verify that user can check all rows and uncheck all rows")
    public void testCheckAllOption() {
        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
            .clickPartsAndAssemblies();

        SoftAssertions softAssertions = new SoftAssertions();

        partsAndAssembliesPage.clickCheckAll();
        softAssertions.assertThat(partsAndAssembliesPage.getCheckAllStatus()).contains("Mui-checked");

        partsAndAssembliesPage.clickMessages()
            .clickPartsAndAssemblies();
        softAssertions.assertThat(partsAndAssembliesPage.getCheckAllStatus()).doesNotContain("Mui-checked");

        partsAndAssembliesPage.clickCheckAll();
        softAssertions.assertThat(partsAndAssembliesPage.getCheckAllStatus()).contains("Mui-checked");

        partsAndAssembliesPage.clickCheckAll();
        softAssertions.assertThat(partsAndAssembliesPage.getCheckAllStatus()).doesNotContain("Mui-checked");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {12221, 12224, 12226, 12227, 12233, 13200, 12230})
    @Description("Verify that user can filter results in parts and assemblies page")
    public void testFilterAComponent() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .clickFilter();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesPage.isFilterModalDisplayed()).isEqualTo(true);

        partsAndAssembliesPage.clickAddCondition();

        softAssertions.assertThat(partsAndAssembliesPage.isFilterFieldDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesPage.isFilterTypeDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesPage.isFilterValueDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesPage.isFilterClearIconDisplayed()).isEqualTo(true);
        softAssertions.assertThat(partsAndAssembliesPage.selectComponentNameField()).isEqualTo("componentName");

        partsAndAssembliesPage.addFilterValue(componentName, scenarioName);

        softAssertions.assertThat(partsAndAssembliesPage.getFilteredComponentName()).isEqualTo(scenarioName);
        softAssertions.assertThat(partsAndAssembliesPage.getListOfScenarios(componentName, scenarioName)).isEqualTo(1);

        partsAndAssembliesPage.clickToRemoveAddedFilter();

        softAssertions.assertThat(partsAndAssembliesPage.getListOfComponents()).isNotEqualTo(1);

        softAssertions.assertAll();

    }

    @Test
    @TestRail(id = {12247, 12248, 12249})
    @Description("Verify that user can set a single sorting rule as ascending or descending order")
    public void testSortPartAndAssemblyTable() {
        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
            .clickPartsAndAssemblies();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesPage.getSortingRule()).isEqualTo("ascending");
        softAssertions.assertThat(partsAndAssembliesPage.getSortingStatus()).isEqualTo("sort-up");
        softAssertions.assertThat(partsAndAssembliesPage.getSortingRule()).isEqualTo("descending");
        softAssertions.assertThat(partsAndAssembliesPage.getSortingStatus()).isEqualTo("sort-down");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {12191, 12465, 12467, 12468, 12469, 12470, 12476})
    @Description("Verify that user can Save the parts and assemblies page configuration")
    public void testSaveConfigurations() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickPartsAndAssemblies()
            .sortDownDigitalFactoryField()
            .pinToLeftProcessGroupColumn()
            .clickOnShowHideOption()
            .enterFieldName(CisColumnsEnum.STATE.getColumns())
            .disableStateField();

        SoftAssertions softAssertions = new SoftAssertions();

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

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {12225, 12228, 12229, 12349, 12350})
    @Description("Verify that user can see filter operations in parts and assemblies filter modal")
    public void testFilterOperations() {
        loginPage = new CisLoginPage(driver);
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

        softAssertions.assertAll();
    }
}


