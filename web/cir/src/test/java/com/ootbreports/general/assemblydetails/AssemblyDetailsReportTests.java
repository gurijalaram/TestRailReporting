package com.ootbreports.general.assemblydetails;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.AssemblyDetailsReportPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ColumnIndexEnum;
import com.apriori.utils.enums.ComponentInfoColumnEnum;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.reports.AssemblySetEnum;
import com.apriori.utils.enums.reports.AssemblyTypeEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.ListNameEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.web.driver.TestBase;

import com.inputcontrols.InputControlsTests;
import com.navigation.CommonReportTests;
import com.pageobjects.pages.evaluate.ComponentsPage;
import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.pages.explore.ExplorePage;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CiaCirTestDevTest;
import testsuites.suiteinterface.CustomerSmokeTests;
import testsuites.suiteinterface.ReportsTest;
import utils.Constants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssemblyDetailsReportTests extends TestBase {

    private AssemblyDetailsReportPage assemblyDetailsReportPage;
    private InputControlsTests inputControlsTests;
    private GenericReportPage genericReportPage;
    private CommonReportTests commonReportTests;
    private String assemblyType = "";

    public AssemblyDetailsReportTests() {
        super();
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "1915")
    @Description("Validate report is available by navigation")
    public void testReportAvailabilityByMenuAssemblyDetails() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
            Constants.GENERAL_FOLDER,
            ReportNamesEnum.ASSEMBLY_DETAILS.getReportName()
        );
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "3060")
    @Description("Validate report is available by library")
    public void testReportAvailabilityByLibraryAssemblyDetails() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName());
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "1916")
    @Description("Validate report is available by search")
    public void testReportAvailableBySearchAssemblyDetails() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName());
    }

    @Test
    @Category({ReportsTest.class, CustomerSmokeTests.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "1922")
    @Description("Verifies that the currency code works properly")
    public void testCurrencyCodeWorks() {
        assemblyType = AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType();
        BigDecimal gbpGrandTotal;
        BigDecimal usdGrandTotal;

        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        usdGrandTotal = assemblyDetailsReportPage.getValueFromTable(
                assemblyType,
                "Grand Total",
                "Capital Investments"
        );

        assemblyDetailsReportPage.clickInputControlsButton()
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class);

        gbpGrandTotal = assemblyDetailsReportPage.getValueFromTable(
                assemblyType,
                "Grand Total",
                "Capital Investments"
        );

        assertThat(assemblyDetailsReportPage.getCurrentCurrency(), is(equalTo(CurrencyEnum.GBP.getCurrency())));
        assertThat(gbpGrandTotal, is(not(usdGrandTotal)));
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "3205")
    @Description("Verifies that currency change and then reversion works")
    public void testCurrencyCodeReversion() {
        assemblyType = AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType();
        BigDecimal gbpGrandTotal;
        BigDecimal usdGrandTotal;

        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        assemblyDetailsReportPage.clickInputControlsButton()
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class);

        gbpGrandTotal = assemblyDetailsReportPage.getValueFromTable(
                assemblyType,
                "Grand Total",
                "Capital Investments"
        );
        assertThat(assemblyDetailsReportPage.getCurrentCurrency(), is(equalTo(CurrencyEnum.GBP.getCurrency())));

        assemblyDetailsReportPage.clickInputControlsButton()
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        usdGrandTotal = assemblyDetailsReportPage.getValueFromTable(
                assemblyType,
                "Grand Total",
                "Capital Investments"
        );

        assertThat(assemblyDetailsReportPage.getCurrentCurrency(), is(equalTo(CurrencyEnum.USD.getCurrency())));
        assertThat(usdGrandTotal, is(not(equalTo(gbpGrandTotal))));
    }

    @Test
    @Issue("AP-58059")
    @Issue("AP-53537")
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = {"3067", "1929"})
    @Description("Verify totals calculations for Sub Assembly")
    public void testTotalCalculationsForSubAssembly() {
        assemblyType = AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType();

        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .clickOk()
            .waitForCorrectAssembly(AssemblySetEnum.SUB_ASSEMBLY_LOWERCASE.getAssemblySetName())
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class);

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(assemblyType, "Grand Total", "Cycle Time"),
            assemblyDetailsReportPage.getExpectedCTGrandTotal(assemblyType, "Cycle Time")
        ), is(true));

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(
                    assemblyType, "Grand Total", "Piece Part Cost"),
            assemblyDetailsReportPage.getExpectedFbcPpcGrandTotal(assemblyType, "Piece Part Cost")
        ), is(true));

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(
                    assemblyType, "Grand Total", "Fully Burdened Cost"),
            assemblyDetailsReportPage.getExpectedFbcPpcGrandTotal(assemblyType, "Fully Burdened Cost")
        ), is(true));

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(
                    assemblyType, "Grand Total", "Capital Investments"),
            assemblyDetailsReportPage.getExpectedCIGrandTotal(assemblyType, "Capital Investments")
        ), is(true));
    }

    @Test
    @Issue("AP-58059")
    @Issue("AP-53537")
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = {"3068", "1929"})
    @Description("Verify totals calculations for Sub-Sub-ASM")
    public void testTotalCalculationsForSubSubASM() {
        assemblyType = AssemblyTypeEnum.SUB_SUB_ASM.getAssemblyType();

        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
                .waitForInputControlsLoad()
                .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName());

        genericReportPage.waitForCorrectAvailableSelectedCount(
                ListNameEnum.SCENARIO_NAME.getListName(),
                "Available: ",
                "1"
        );

        assemblyDetailsReportPage = new AssemblyDetailsReportPage(driver)
                .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
                .setAssembly(AssemblySetEnum.SUB_SUB_ASM.getAssemblySetName())
                .clickOk()
                .waitForCorrectAssembly(AssemblySetEnum.SUB_SUB_ASM_LOWERCASE.getAssemblySetName())
                .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class);

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(assemblyType, "Grand Total", "Cycle Time"),
            assemblyDetailsReportPage.getExpectedCTGrandTotal(assemblyType, "Cycle Time")
        ), is(true));

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(
                    assemblyType, "Grand Total", "Piece Part Cost"),
            assemblyDetailsReportPage.getExpectedFbcPpcGrandTotal(assemblyType, "Piece Part Cost")
        ), is(true));

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(
                    assemblyType, "Grand Total", "Fully Burdened Cost"),
            assemblyDetailsReportPage.getExpectedFbcPpcGrandTotal(assemblyType, "Fully Burdened Cost")
        ), is(true));

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(
                    assemblyType, "Grand Total", "Capital Investments"),
            assemblyDetailsReportPage.getExpectedCIGrandTotal(assemblyType, "Capital Investments")
        ), is(true));
    }

    @Test
    @Issue("AP-58059")
    @Issue("AP-53537")
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = {"1934", "1929"})
    @Description("Verify totals calculations for Top Level")
    public void testTotalCalculationsForTopLevel() {
        assemblyType = AssemblyTypeEnum.TOP_LEVEL.getAssemblyType();

        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName());

        genericReportPage.waitForCorrectAvailableSelectedCount(
                ListNameEnum.SCENARIO_NAME.getListName(),
                "Available: ",
                "1"
        );

        assemblyDetailsReportPage = new AssemblyDetailsReportPage(driver)
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .setAssembly(AssemblySetEnum.TOP_LEVEL.getAssemblySetName())
            .clickOk()
            .waitForCorrectAssembly(AssemblySetEnum.TOP_LEVEL.getAssemblySetName())
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class);

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(assemblyType, "Grand Total", "Cycle Time"),
            assemblyDetailsReportPage.getExpectedCTGrandTotal(assemblyType, "Cycle Time")
        ), is(true));

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(
                    assemblyType, "Grand Total", "Piece Part Cost"),
            assemblyDetailsReportPage.getExpectedFbcPpcGrandTotal(assemblyType, "Piece Part Cost")
        ), is(true));

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(
                    assemblyType, "Grand Total", "Fully Burdened Cost"),
            assemblyDetailsReportPage.getExpectedFbcPpcGrandTotal(assemblyType, "Fully Burdened Cost")
        ), is(true));

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(
                    assemblyType, "Grand Total", "Capital Investments"),
            assemblyDetailsReportPage.getExpectedCIGrandTotal(assemblyType, "Capital Investments")
        ), is(true));
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = {"3231", "1929"})
    @Description("Verify sub total calculations for Sub Assembly")
    public void testSubTotalCalculationsSubAssembly() {
        assemblyType = AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType();

        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .clickOk()
            .waitForCorrectAssembly(AssemblySetEnum.SUB_ASSEMBLY.getAssemblySetName())
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class);

        ArrayList<BigDecimal> ctValues = assemblyDetailsReportPage.getSubTotalAdditionValue(
                assemblyType,
                "Cycle Time"
        );
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(ctValues.get(0), ctValues.get(1)), is(true));

        ArrayList<BigDecimal> ppcValues = assemblyDetailsReportPage.getSubTotalAdditionValue(
                assemblyType,
                "Piece Part Cost"
        );
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(ppcValues.get(0), ppcValues.get(1)), is(true));

        ArrayList<BigDecimal> fbcValues = assemblyDetailsReportPage.getSubTotalAdditionValue(
                assemblyType,
                "Fully Burdened Cost"
        );
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(fbcValues.get(0), fbcValues.get(1)), is(true));

        ArrayList<BigDecimal> ciValues = assemblyDetailsReportPage.getSubTotalAdditionValue(
                assemblyType,
                "Capital Investments"
        );
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(ciValues.get(0), ciValues.get(1)), is(true));
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = {"3232", "1929"})
    @Description("Verify sub total calculations for Sub Sub ASM")
    public void testSubTotalCalculationsSubSubAsm() {
        assemblyType = AssemblyTypeEnum.SUB_SUB_ASM.getAssemblyType();

        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName());

        genericReportPage.waitForCorrectAvailableSelectedCount(
                ListNameEnum.SCENARIO_NAME.getListName(),
                "Available: ",
                "1"
        );

        assemblyDetailsReportPage = new AssemblyDetailsReportPage(driver)
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .setAssembly(AssemblySetEnum.SUB_SUB_ASM.getAssemblySetName())
            .clickOk()
            .waitForCorrectAssembly(AssemblySetEnum.SUB_SUB_ASM.getAssemblySetName())
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class);

        ArrayList<BigDecimal> ctValues = assemblyDetailsReportPage.getSubTotalAdditionValue(
                assemblyType,
                "Cycle Time"
        );
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(ctValues.get(0), ctValues.get(1)), is(true));

        ArrayList<BigDecimal> ppcValues = assemblyDetailsReportPage.getSubTotalAdditionValue(
                assemblyType,
                "Piece Part Cost"
        );
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(ppcValues.get(0), ppcValues.get(1)), is(true));

        ArrayList<BigDecimal> fbcValues = assemblyDetailsReportPage.getSubTotalAdditionValue(
                assemblyType,
                "Fully Burdened Cost"
        );
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(fbcValues.get(0), fbcValues.get(1)), is(true));

        ArrayList<BigDecimal> ciValues = assemblyDetailsReportPage.getSubTotalAdditionValue(
                assemblyType,
                "Capital Investments"
        );
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(ciValues.get(0), ciValues.get(1)), is(true));
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = {"3233", "1929"})
    @Description("Verify sub total calculations for Top Level")
    public void testSubTotalCalculationsTopLevel() {
        assemblyType = AssemblyTypeEnum.TOP_LEVEL.getAssemblyType();

        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName());

        genericReportPage.waitForCorrectAvailableSelectedCount(
                ListNameEnum.SCENARIO_NAME.getListName(),
                "Available: ",
                "1"
        );

        assemblyDetailsReportPage = new AssemblyDetailsReportPage(driver)
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .setAssembly(AssemblySetEnum.TOP_LEVEL.getAssemblySetName())
            .clickOk()
            .waitForCorrectAssembly(AssemblySetEnum.TOP_LEVEL.getAssemblySetName())
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class);

        ArrayList<BigDecimal> ctValues = assemblyDetailsReportPage.getSubTotalAdditionValue(
                assemblyType,
                "Cycle Time"
        );
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(ctValues.get(0), ctValues.get(1)), is(true));

        ArrayList<BigDecimal> ppcValues = assemblyDetailsReportPage.getSubTotalAdditionValue(
                assemblyType,
                "Piece Part Cost"
        );
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(ppcValues.get(0), ppcValues.get(1)), is(true));

        ArrayList<BigDecimal> fbcValues = assemblyDetailsReportPage.getSubTotalAdditionValue(
                assemblyType,
                "Fully Burdened Cost"
        );
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(fbcValues.get(0), fbcValues.get(1)), is(true));

        ArrayList<BigDecimal> ciValues = assemblyDetailsReportPage.getSubTotalAdditionValue(
                assemblyType,
                "Capital Investments"
        );
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(ciValues.get(0), ciValues.get(1)), is(true));
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "1919")
    @Description("Ensuring latest export date filter works properly (uses date input field)")
    public void testLatestExportDateFilterUsingInput() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName());
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "3244")
    @Description("Ensuring latest export date filter works properly (using date picker)")
    public void testLatestExportDateFilterUsingDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName());
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "1930")
    @Description("Test Export Set with costing failures costing incomplete")
    public void testExportSetWithCostingFailuresCostingIncomplete() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.PISTON_ASSEMBLY.getExportSetName())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), GenericReportPage.class)
            .openNewCidTabAndFocus(1);

        List<String> columnsToAdd = Arrays.asList(
            ComponentInfoColumnEnum.CYCLE_TIME.getColumnName(),
            ComponentInfoColumnEnum.PIECE_PART_COST.getColumnName(),
            ComponentInfoColumnEnum.FULLY_BURDENED_COST.getColumnName(),
            ComponentInfoColumnEnum.CAPITAL_INVESTMENT.getColumnName()
        );

        ComponentsPage componentsPage = new ExplorePage(driver)
            .filter()
            .setScenarioType(Constants.ASSEMBLY_SCENARIO_TYPE)
            .setWorkspace(Constants.PUBLIC_WORKSPACE)
            .setRowOne("Part Name", "Contains", Constants.PISTON_ASSEMBLY_CID_NAME)
            .setRowTwo("Scenario Name", "Contains", Constants.DEFAULT_SCENARIO_NAME)
            .apply(ExplorePage.class)
            .openFirstScenario()
            .openComponentsTable()
            .openColumnsTable()
            .checkColumnSettings(columnsToAdd)
            .selectSaveButton();

        ArrayList<BigDecimal> cidPartOneValues = componentsPage
            .getTableValsByRow(
                ColumnIndexEnum.CID_PART_ONE.getColumnIndex()
            );
        ArrayList<BigDecimal> cidPartTwoValues = componentsPage
            .getTableValsByRow(
                ColumnIndexEnum.CID_PART_TWO.getColumnIndex()
            );
        ArrayList<BigDecimal> cidPartThreeValues = componentsPage
            .getTableValsByRow(
                ColumnIndexEnum.CID_PART_THREE.getColumnIndex()
            );
        ArrayList<BigDecimal> cidPartFourValues = componentsPage
            .getTableValsByRow(
                ColumnIndexEnum.CID_PART_FOUR.getColumnIndex()
            );

        componentsPage.switchBackToTabOne();
        ArrayList<BigDecimal> reportsPartOneValues = genericReportPage
            .getValuesByRow(
                ColumnIndexEnum.CIR_PART_ONE.getColumnIndex()
            );
        ArrayList<BigDecimal> reportsPartTwoValues = genericReportPage
            .getValuesByRow(
                ColumnIndexEnum.CIR_PART_TWO.getColumnIndex()
            );
        ArrayList<BigDecimal> reportsPartThreeValues = genericReportPage
            .getValuesByRow(
                ColumnIndexEnum.CIR_PART_THREE.getColumnIndex()
            );
        ArrayList<BigDecimal> reportsPartFourValues = genericReportPage
            .getValuesByRow(
                ColumnIndexEnum.CIR_PART_FOUR.getColumnIndex()
            );

        assertThat(
            cidPartOneValues.equals(reportsPartFourValues),
            is(true)
        );
        assertThat(
            cidPartTwoValues.equals(reportsPartThreeValues),
            is(true)
        );
        assertThat(
            cidPartThreeValues.equals(reportsPartOneValues),
            is(true)
        );
        assertThat(
            cidPartFourValues.equals(reportsPartTwoValues),
            is(true)
        );
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "1918")
    @Description("Verify Export set of a part file is not available for selection")
    public void testAssemblySelectDropdown() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName());

        assertThat(
            genericReportPage.getAssemblyNameFromSetAssemblyDropdown(AssemblySetEnum.SUB_ASSEMBLY.getAssemblySetName()),
            containsString(Constants.ASSEMBLY_STRING)
        );

        assertThat(
            genericReportPage.getAssemblyNameFromSetAssemblyDropdown(AssemblySetEnum.SUB_SUB_ASM.getAssemblySetName()),
            containsString(Constants.ASSEMBLY_STRING)
        );

        assertThat(
            genericReportPage.getAssemblyNameFromSetAssemblyDropdown(AssemblySetEnum.TOP_LEVEL.getAssemblySetName()),
            containsString(Constants.ASSEMBLY_STRING)
        );
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "1920")
    @Description("Export set count is correct")
    public void testExportSetSelectionOptions() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSelection(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName());
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "1931")
    @Description("Validate links to component cost detail report (incl. headers etc.)")
    public void testLinksToComponentCostReport() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), GenericReportPage.class);

        String partNumberComponent = genericReportPage.getComponentLinkPartNumber();
        genericReportPage.clickComponentLinkAssemblyDetails();

        assertThat(genericReportPage.getReportTitle(),
                is(equalTo(ReportNamesEnum.COMPONENT_COST_INTERNAL_USE.getReportName())));
        assertThat(genericReportPage.getComponentCostPartNumber(), is(equalTo(partNumberComponent)));
        genericReportPage.closeTab();

        String partNumberAssembly = genericReportPage.getAssemblyLinkPartNumber();
        genericReportPage.clickAssemblyLinkAssemblyDetails();
        assertThat(genericReportPage.getReportTitle(),
                is(equalTo(ReportNamesEnum.COMPONENT_COST_INTERNAL_USE.getReportName())));
        assertThat(genericReportPage.getComponentCostPartNumber(), is(equalTo(partNumberAssembly)));
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "1921")
    @Description("Export Set search function works (plus other filters)")
    public void testExportSetSearch() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad();

        genericReportPage.searchForExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName());

        assertThat(genericReportPage.getExportSetOptionCount(), is(equalTo("2")));
        assertThat(genericReportPage.isExportSetVisible(ExportSetEnum.TOP_LEVEL.getExportSetName()), is(true));
        assertThat(genericReportPage.isExportSetVisible(ExportSetEnum.TOP_LEVEL_MULTI_VPE.getExportSetName()), is(true));
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "1921")
    @Description("Export set search function works (plus other filters)")
    public void testCreatedByFilterSearch() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testListFilterSearch(
            ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(),
            ListNameEnum.CREATED_BY.getListName()
        );
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "1921")
    @Description("Export set search function works (plus other filters)")
    public void testCreatedByFilterOperation() {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), GenericReportPage.class);

        String lastModifiedByAvailableCountPreSelection = genericReportPage.getCountOfListAvailableOrSelectedItems(
                ListNameEnum.LAST_MODIFIED_BY.getListName(), "Available");
        String scenarioNameAvailableCountPreSelection = genericReportPage.getCountOfListAvailableOrSelectedItems(
                ListNameEnum.SCENARIO_NAME.getListName(), "Available");

        String nameToSelect = "Ben Hegan";
        genericReportPage.selectListItem(ListNameEnum.CREATED_BY.getListName(), nameToSelect);

        genericReportPage.waitForCorrectAvailableSelectedCount(
                ListNameEnum.CREATED_BY.getListName(),
                "Selected: ",
                "1"
        );
        assertThat(genericReportPage.getCountOfListAvailableOrSelectedItems(
                ListNameEnum.CREATED_BY.getListName(),
                "Selected"
        ), is(equalTo("1")));

        String expectedLastModifiedCount = Constants.DEFAULT_ENVIRONMENT_VALUE.equals("cir-qa") ? "2" : "1";
        genericReportPage.waitForCorrectAvailableSelectedCount(
                ListNameEnum.LAST_MODIFIED_BY.getListName(), "Available: ", expectedLastModifiedCount);
        String lastModifiedByAvailableCountPostSelection = genericReportPage.getCountOfListAvailableOrSelectedItems(
                ListNameEnum.LAST_MODIFIED_BY.getListName(), "Available");
        String scenarioNameAvailableCountPostSelection = genericReportPage.getCountOfListAvailableOrSelectedItems(
                ListNameEnum.SCENARIO_NAME.getListName(), "Available");

        assertThat(lastModifiedByAvailableCountPreSelection,
                is(not(equalTo(lastModifiedByAvailableCountPostSelection))));
        assertThat(scenarioNameAvailableCountPreSelection,
                is(equalTo(scenarioNameAvailableCountPostSelection)));

        genericReportPage.waitForCorrectAssemblyInDropdown(AssemblySetEnum.SUB_ASSEMBLY.getAssemblySetName());
        assertThat(genericReportPage.getCurrentlySelectedAssembly(),
                is(startsWith(AssemblySetEnum.SUB_ASSEMBLY.getAssemblySetName())));
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "1921")
    @Description("Export set search function works (plus other filters)")
    public void testCreatedByFilterButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testListFilterButtons(
            ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(),
            "",
            ListNameEnum.CREATED_BY.getListName()
        );
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "1921")
    @Description("Export set search function works (plus other filters)")
    public void testLastModifiedFilter() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testListFilterSearch(
            ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(),
            ListNameEnum.LAST_MODIFIED_BY.getListName()
        );
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "1921")
    @Description("Export set search function works (plus other filters)")
    public void testLastModifiedFilterOperation() {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), GenericReportPage.class);

        String scenarioNameAvailableCountPreSelection = genericReportPage.getCountOfListAvailableOrSelectedItems(
                ListNameEnum.SCENARIO_NAME.getListName(), "Available");

        String nameToSelect = "Ben Hegan";
        genericReportPage.selectListItem(ListNameEnum.LAST_MODIFIED_BY.getListName(), nameToSelect);

        genericReportPage.waitForCorrectAvailableSelectedCount(
                ListNameEnum.LAST_MODIFIED_BY.getListName(),
                "Selected: ",
                "1"
        );
        assertThat(genericReportPage.getCountOfListAvailableOrSelectedItems(
                ListNameEnum.LAST_MODIFIED_BY.getListName(),
                "Selected"
        ), is(equalTo("1")));

        String scenarioNameAvailableCountPostSelection = genericReportPage.getCountOfListAvailableOrSelectedItems(
                ListNameEnum.SCENARIO_NAME.getListName(), "Available");

        assertThat(scenarioNameAvailableCountPreSelection,
                is(equalTo(scenarioNameAvailableCountPostSelection)));

        genericReportPage.waitForCorrectAssemblyInDropdown(AssemblySetEnum.SUB_ASSEMBLY.getAssemblySetName());
        assertThat(genericReportPage.getCurrentlySelectedAssembly(),
                is(startsWith(AssemblySetEnum.SUB_ASSEMBLY.getAssemblySetName())));
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "1921")
    @Description("Export set search function works (plus other filters)")
    public void testLastModifiedFilterButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testListFilterButtons(
            ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(),
            "",
            ListNameEnum.LAST_MODIFIED_BY.getListName()
        );
    }

    @Test
    @Category({CiaCirTestDevTest.class, ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "1921")
    @Description("Export set search function works (plus other filters)")
    public void testAssemblyNumberSearchCriteria() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testAssemblyNumberSearchCriteria(
            ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(),
            AssemblySetEnum.SUB_ASSEMBLY_SHORT.getAssemblySetName()
        );
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "1924")
    @Description("Verify report figures from CI Design")
    public void testDataIntegrity() {
        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        Map<String, String> reportsValues = new HashMap<>();
        reportsValues.put("Part Name", assemblyDetailsReportPage.getRowFivePartName());
        reportsValues.put("Cycle Time", assemblyDetailsReportPage.getFiguresFromTable("Cycle Time"));
        reportsValues.put("Piece Part Cost", assemblyDetailsReportPage.getFiguresFromTable("Piece Part Cost"));
        reportsValues.put("Fully Burdened Cost",
            assemblyDetailsReportPage.getFiguresFromTable("Fully Burdened Cost"));
        reportsValues.put("Capital Investments",
            assemblyDetailsReportPage.getFiguresFromTable("Capital Investments"));

        assemblyDetailsReportPage.openNewCidTabAndFocus(1);
        EvaluatePage evaluatePage = new ExplorePage(driver)
                .filter()
                .setScenarioType(Constants.PART_SCENARIO_TYPE)
                .setWorkspace(Constants.PUBLIC_WORKSPACE)
                .setRowOne("Part Name", "Contains", reportsValues.get("Part Name"))
                .setRowTwo("Scenario Name", "Contains", Constants.DEFAULT_SCENARIO_NAME)
                .apply(ExplorePage.class)
                .openFirstScenario();

        Map<String, String> cidValues = new HashMap<>();
        cidValues.put("Cycle Time", String.valueOf(evaluatePage.getCycleTimeCount()));
        cidValues.put("Piece Part Cost", String.valueOf(evaluatePage.getPartCost()));
        cidValues.put("Fully Burdened Cost", String.valueOf(evaluatePage.getBurdenedCost()));
        cidValues.put("Capital Investments", String.valueOf(evaluatePage.getCapitalInvestment()));

        assertThat(reportsValues.get("Cycle Time"), is(equalTo(cidValues.get("Cycle Time"))));
        assertThat(reportsValues.get("Piece Part Cost"), is(cidValues.get("Piece Part Cost")));
        assertThat(reportsValues.get("Fully Burdened Cost"), is(cidValues.get("Fully Burdened Cost")));
        assertThat(reportsValues.get("Capital Investments").substring(0, 3), is(cidValues.get("Capital Investments")));
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "1928")
    @Description("Validate report content aligns to aP desktop values (many levels inside BOM)")
    public void testLevelsInsideBOM() {
        assemblyDetailsReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), GenericReportPage.class)
                .waitForInputControlsLoad()
                .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
                .clickOk()
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        ArrayList<BigDecimal> levelValues =
                assemblyDetailsReportPage.getLevelValues(AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType());

        assertThat(levelValues.isEmpty(), is(false));
        for (int i = 0; i < 7; i++) {
            assertThat(levelValues.get(i).compareTo(new BigDecimal("1")), is(0));
        }

        for (int i = 7; i < 10; i++) {
            assertThat(levelValues.get(i).compareTo(new BigDecimal("2")), is(0));
        }
    }

    @Test
    @Category({CiaCirTestDevTest.class, ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "1933")
    @Description("Verify component subassembly report details")
    public void testComponentSubAssemblyReportDetails() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.COMPONENT_COST.getReportName(), GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.SUB_SUB_ASM.getExportSetName())
            .selectComponent(AssemblySetEnum.SUB_SUB_ASM.getAssemblySetName())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), GenericReportPage.class);

        BigDecimal actualVariance = genericReportPage.getComponentCostReportValue("Variance");
        BigDecimal actualLifetimeCost = genericReportPage.getComponentCostReportValue("Lifetime Cost");
        BigDecimal actualPercentageOfTarget = genericReportPage.getComponentCostReportValue("% of Target");
        BigDecimal actualLifetimeProjectedCostDifference =
            genericReportPage.getComponentCostReportValue("Lifetime Projected Cost");

        assertThat(actualVariance.compareTo(new BigDecimal("79.80")), is(equalTo(0)));
        assertThat(actualPercentageOfTarget.compareTo(new BigDecimal("1596.06")), is(equalTo(0)));
        assertThat(actualLifetimeCost.compareTo(new BigDecimal("2332078.82")), is(equalTo(0)));
        assertThat(actualLifetimeProjectedCostDifference.compareTo(new BigDecimal("2194578.82")),
            is(equalTo(0)));
    }

    @Test
    @Category({ReportsTest.class, CiaCirTestDevTest.class})
    @TestRail(testCaseId = "1927")
    @Description("Validate multiple VPE usage aligns to CID usage")
    public void testMultiVPEAgainstCID() {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), GenericReportPage.class)
                .waitForInputControlsLoad()
                .selectExportSet(ExportSetEnum.TOP_LEVEL_MULTI_VPE.getExportSetName());

        genericReportPage.waitForCorrectAvailableSelectedCount(ListNameEnum.SCENARIO_NAME.getListName(), "Available: ", "1");

        assemblyDetailsReportPage = new AssemblyDetailsReportPage(driver)
                .setAssembly(AssemblySetEnum.TOP_LEVEL_MULTI_VPE.getAssemblySetName())
                .clickOk()
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        ArrayList<String> reportsVpeValues = genericReportPage.getAllVpeValuesAssemblyDetailsReport();

        genericReportPage.openNewCidTabAndFocus(1);

        ComponentsPage componentsPage = new ExplorePage(driver)
                .filter()
                .setWorkspace(Constants.PUBLIC_WORKSPACE)
                .setScenarioType(Constants.ASSEMBLY_SCENARIO_TYPE)
                .setRowOne("Part Name", "Contains",
                        AssemblySetEnum.TOP_LEVEL_SHORT.getAssemblySetName())
                .setRowTwo("Scenario Name", "Contains", "Multi VPE")
                .apply(ExplorePage.class)
                .openFirstScenario()
                .openComponentsTable();

        ArrayList<String> cidVpeValues = componentsPage.getVpeValues();

        assertThat(reportsVpeValues.equals(cidVpeValues), is(equalTo(true)));
    }
}
