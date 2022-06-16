package com.partsandassemblies;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;

import com.apriori.pageobjects.navtoolbars.CisHeaderBar;
import com.apriori.pageobjects.navtoolbars.LeftHandNavigationBar;
import com.apriori.pageobjects.pages.login.CisLoginPage;
import com.apriori.pageobjects.pages.partsandassemblies.PartsAndAssembliesPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.CisColumnsEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.junit.Test;



public class PartsAndAssemblyTest extends TestBase {

    public PartsAndAssemblyTest() {
        super();
    }

    private CisLoginPage loginPage;
    private LeftHandNavigationBar leftHandNavigationBar;
    private PartsAndAssembliesPage partsAndAssembliesPage;
    private SoftAssertions softAssertions;

    @Test
    @TestRail(testCaseId = {"12058"})
    @Description("Verify the fields included in the table")
    public void testPartsAndAssemblyTableHeader() {
        loginPage = new CisLoginPage(driver);
        leftHandNavigationBar = loginPage.cisLogin(UserUtil.getUser());
        partsAndAssembliesPage = leftHandNavigationBar.clickPartsAndAssemblies();
        leftHandNavigationBar.collapseNavigationPanel();

        assertThat(partsAndAssembliesPage.getTableHeaders(), hasItems(CisColumnsEnum.COMPONENT_NAME.getColumns(), CisColumnsEnum.SCENARIO_NAME.getColumns(),
                CisColumnsEnum.COMPONENT_TYPE.getColumns(), CisColumnsEnum.STATE.getColumns(), CisColumnsEnum.PROCESS_GROUP.getColumns(), CisColumnsEnum.DIGITAL_FACTORY.getColumns(), CisColumnsEnum.CREATED_AT.getColumns(),
               CisColumnsEnum.CREATED_BY.getColumns(), CisColumnsEnum.ANNUAL_VOLUME.getColumns(), CisColumnsEnum.BATCH_SIZE.getColumns(), CisColumnsEnum.DFM_RISK.getColumns(), CisColumnsEnum.STOCK_FORM.getColumns(), CisColumnsEnum.FULLY_BURDENED_COST.getColumns()));

    }

    @Test
    @TestRail(testCaseId = {"12064"})
    @Description("Verify that user can check a table row")
    public void testCheckTableRow() {
        loginPage = new CisLoginPage(driver);
        leftHandNavigationBar = loginPage.cisLogin(UserUtil.getUser());
        partsAndAssembliesPage = leftHandNavigationBar.clickPartsAndAssemblies();
        partsAndAssembliesPage.waitForTableLoad();

        assertThat(partsAndAssembliesPage.getComponentCheckBoxStatus(), is(equalTo("true")));

    }

    @Test
    @TestRail(testCaseId = {"12188"})
    @Description("Verify that user can hide all the fields by selecting 'HIDE ALL' option")
    public void testHideAllFieldsOption() {
        loginPage = new CisLoginPage(driver);
        leftHandNavigationBar = loginPage.cisLogin(UserUtil.getUser());
        partsAndAssembliesPage = leftHandNavigationBar.clickPartsAndAssemblies();
        partsAndAssembliesPage.waitForTableLoad();
        partsAndAssembliesPage.clickOnShowHideOption();
        partsAndAssembliesPage.clickOnHideAllButton();

        assertThat(partsAndAssembliesPage.getTableHeaders(), hasItems(CisColumnsEnum.COMPONENT_NAME.getColumns()));

    }

    @Test
    @TestRail(testCaseId = {"12178","12185"})
    @Description("Verify that user can search a field by its name and hide the field")
    public void testSearchAFieldToShowHide() {
        loginPage = new CisLoginPage(driver);
        leftHandNavigationBar = loginPage.cisLogin(UserUtil.getUser());
        partsAndAssembliesPage = leftHandNavigationBar.clickPartsAndAssemblies();
        partsAndAssembliesPage.waitForTableLoad();
        partsAndAssembliesPage.clickOnShowHideOption();
        partsAndAssembliesPage.enterFieldName(CisColumnsEnum.COMPONENT_NAME.getColumns());

        assertThat(partsAndAssembliesPage.getFieldName(),is(equalTo(CisColumnsEnum.COMPONENT_NAME.getColumns())));

        partsAndAssembliesPage.clickOnToggleButton();

        assertThat(partsAndAssembliesPage.getTableHeaders(), not(hasItems(CisColumnsEnum.COMPONENT_NAME.getColumns())));

    }

    @Test
    @TestRail(testCaseId = {"12239","12241","12242","12243"})
    @Description("Verify the availability and functionality of the 'pin to left' option")
    public void testPintoLeftOption() {
        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
                .clickPartsAndAssemblies()
                .clickKebabMenuOnTableHeader();

        assertThat(partsAndAssembliesPage.isPinToLeftOptionDisplayed(), is(true));

        partsAndAssembliesPage.clickPinToLeft();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesPage.getPinnedTableHeaders()).contains(CisColumnsEnum.SCENARIO_NAME.getColumns());

        partsAndAssembliesPage.clickKebabMenuOnTableHeader();

        softAssertions.assertThat(partsAndAssembliesPage.isUnPinOptionDisplayed()).isEqualTo(true);

        partsAndAssembliesPage.clickOnUnpinOption();

        softAssertions.assertThat(partsAndAssembliesPage.getTableHeaders()).contains(CisColumnsEnum.SCENARIO_NAME.getColumns());


        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"12110","12111","12112","12113","12115"})
    @Description("Verify that Parts and Assemblies can search by Component Name")
    public void testSearchByComponentName() {
        String componentName = "Y_shape";
        String scenarioName = "YShape_AutomationSearch";
        loginPage = new CisLoginPage(driver);
        partsAndAssembliesPage = loginPage.cisLogin(UserUtil.getUser())
                .clickPartsAndAssemblies();

        assertThat(partsAndAssembliesPage.isSearchOptionDisplayed(), is(true));

        partsAndAssembliesPage.clickSearchOption();

        assertThat(partsAndAssembliesPage.isSearchFieldDisplayed(),is(true));

        partsAndAssembliesPage.clickOnSearchField();

        partsAndAssembliesPage.enterAComponentName(componentName);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(partsAndAssembliesPage.getAddedComponentName()).isEqualTo(componentName);
        softAssertions.assertThat(partsAndAssembliesPage.getListOfScenarios(componentName,scenarioName)).isEqualTo(1);

        softAssertions.assertAll();

        partsAndAssembliesPage.clickClearOption()
                .waitForTableLoad();

        assertThat(partsAndAssembliesPage.getListOfComponents(),is(not(equalTo(1))));


    }
}


