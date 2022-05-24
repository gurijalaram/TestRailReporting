package com.partsandassemblies;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

import com.apriori.pageobjects.navtoolbars.CisHeaderBar;
import com.apriori.pageobjects.navtoolbars.LeftHandNavigationBar;
import com.apriori.pageobjects.pages.login.CisLoginPage;
import com.apriori.pageobjects.pages.partsandassemblies.PartsAndAssembliesPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.CisColumnsEnum;
import io.qameta.allure.Description;
import org.junit.Test;



public class PartsAndAssemblyTest extends TestBase {

    public PartsAndAssemblyTest() {
        super();
    }

    private CisLoginPage loginPage;
    private LeftHandNavigationBar leftHandNavigationBar;
    private PartsAndAssembliesPage partsAndAssembliesPage;

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

}


