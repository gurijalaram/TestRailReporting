package cireporttests.ootbreports.general.assemblydetails;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.ComponentsPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.reports.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.reports.pages.view.reports.AssemblyDetailsReportPage;
import com.apriori.pageobjects.reports.pages.view.reports.GenericReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.enums.ColumnIndexEnum;
import com.apriori.utils.enums.ComponentInfoColumnEnum;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.reports.AssemblySetEnum;
import com.apriori.utils.enums.reports.AssemblyTypeEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.web.driver.TestBase;

import cireporttests.inputcontrols.InputControlsTests;
import cireporttests.navigation.ReportAvailabilityTests;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CIARStagingSmokeTest;
import testsuites.suiteinterface.CustomerSmokeTests;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssemblyDetailsReportTests extends TestBase {

    private ReportAvailabilityTests reportAvailabilityTests;
    private AssemblyDetailsReportPage assemblyDetailsReport;
    private InputControlsTests inputControlsTests;
    private GenericReportPage genericReportPage;
    private String assemblyType = "";

    public AssemblyDetailsReportTests() {
        super();
    }

    @Test
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = "1915")
    @Description("validate report is available by navigation")
    public void testReportAvailabilityByMenuAssemblyCostA4() {
        reportAvailabilityTests = new ReportAvailabilityTests(driver);
        reportAvailabilityTests.testReportAvailabilityByNavigation(
                Constants.GENERAL_FOLDER,
                ReportNamesEnum.ASSEMBLY_COST_A4.getReportName()
        );
    }

    @Test
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = "1915")
    @Description("validate report is available by navigation")
    public void testReportAvailabilityByMenuAssemblyCostLetter() {
        reportAvailabilityTests = new ReportAvailabilityTests(driver);
        reportAvailabilityTests.testReportAvailabilityByNavigation(
                Constants.GENERAL_FOLDER,
                ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName()
        );
    }

    @Test
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = "1915")
    @Description("validate report is available by navigation")
    public void testReportAvailabilityByMenuAssemblyDetails() {
        reportAvailabilityTests = new ReportAvailabilityTests(driver);
        reportAvailabilityTests.testReportAvailabilityByNavigation(
                Constants.GENERAL_FOLDER,
                ReportNamesEnum.ASSEMBLY_DETAILS.getReportName()
        );
    }

    @Test
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = "1915")
    @Description("validate report is available by navigation")
    public void testReportAvailabilityByMenuComponentCost() {
        reportAvailabilityTests = new ReportAvailabilityTests(driver);
        reportAvailabilityTests.testReportAvailabilityByNavigation(
                Constants.GENERAL_FOLDER,
                ReportNamesEnum.COMPONENT_COST.getReportName()
        );
    }

    @Test
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = "1915")
    @Description("validate report is available by navigation")
    public void testReportAvailabilityByMenuScenarioComparison() {
        reportAvailabilityTests = new ReportAvailabilityTests(driver);
        reportAvailabilityTests.testReportAvailabilityByNavigation(
                Constants.GENERAL_FOLDER,
                ReportNamesEnum.SCENARIO_COMPARISON.getReportName()
        );
    }

    @Test
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = "3060")
    @Description("Validate report is available by library")
    public void testReportAvailabilityByLibraryAssemblyCostA4() {
        reportAvailabilityTests = new ReportAvailabilityTests(driver);
        reportAvailabilityTests.testReportAvailabilityByLibrary(ReportNamesEnum.ASSEMBLY_COST_A4.getReportName());
    }

    @Test
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = "3060")
    @Description("Validate report is available by library")
    public void testReportAvailabilityByLibraryAssemblyCostLetter() {
        reportAvailabilityTests = new ReportAvailabilityTests(driver);
        reportAvailabilityTests.testReportAvailabilityByLibrary(ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName());
    }

    @Test
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = "3060")
    @Description("Validate report is available by library")
    public void testReportAvailabilityByLibraryAssemblyDetails() {
        reportAvailabilityTests = new ReportAvailabilityTests(driver);
        reportAvailabilityTests.testReportAvailabilityByLibrary(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName());
    }

    @Test
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = "3060")
    @Description("Validate report is available by library")
    public void testReportAvailabilityByLibraryComponentCost() {
        reportAvailabilityTests = new ReportAvailabilityTests(driver);
        reportAvailabilityTests.testReportAvailabilityByLibrary(ReportNamesEnum.COMPONENT_COST.getReportName());
    }

    @Test
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = "3060")
    @Description("Validate report is available by library")
    public void testReportAvailabilityByLibraryScenarioComparison() {
        reportAvailabilityTests = new ReportAvailabilityTests(driver);
        reportAvailabilityTests.testReportAvailabilityByLibrary(ReportNamesEnum.SCENARIO_COMPARISON.getReportName());
    }

    @Test
    @TestRail(testCaseId = "1916")
    @Description("Validate report is available by search")
    public void testReportAvailableBySearchAssemblyCostA4() {
        reportAvailabilityTests = new ReportAvailabilityTests(driver);
        reportAvailabilityTests.testReportAvailabilityBySearch(ReportNamesEnum.ASSEMBLY_COST_A4.getReportName());
    }

    @Test
    @TestRail(testCaseId = "1916")
    @Description("Validate report is available by search")
    public void testReportAvailableBySearchAssemblyCostLetter() {
        reportAvailabilityTests = new ReportAvailabilityTests(driver);
        reportAvailabilityTests.testReportAvailabilityBySearch(ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName());
    }

    @Test
    @TestRail(testCaseId = "1916")
    @Description("Validate report is available by search")
    public void testReportAvailableBySearchAssemblyDetails() {
        reportAvailabilityTests = new ReportAvailabilityTests(driver);
        reportAvailabilityTests.testReportAvailabilityBySearch(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName());
    }

    @Test
    @TestRail(testCaseId = "1916")
    @Description("Validate report is available by search")
    public void testReportAvailableBySearchComponentCost() {
        reportAvailabilityTests = new ReportAvailabilityTests(driver);
        reportAvailabilityTests.testReportAvailabilityBySearch(ReportNamesEnum.COMPONENT_COST.getReportName());
    }

    @Test
    @TestRail(testCaseId = "1916")
    @Description("Validate report is available by search")
    public void testReportAvailableBySearchScenarioComparison() {
        reportAvailabilityTests = new ReportAvailabilityTests(driver);
        reportAvailabilityTests.testReportAvailabilityBySearch(ReportNamesEnum.SCENARIO_COMPARISON.getReportName());
    }

    @Test
    @Category({CustomerSmokeTests.class, CIARStagingSmokeTest.class})
    @TestRail(testCaseId = "1922")
    @Description("Verifies that the currency code works properly")
    public void testCurrencyCodeWorks() {
        assemblyType = AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType();
        BigDecimal gbpGrandTotal;
        BigDecimal usdGrandTotal;

        assemblyDetailsReport = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        usdGrandTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Capital Investments");

        assemblyDetailsReport.clickInputControlsButton()
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class);

        gbpGrandTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Capital Investments");

        assertThat(assemblyDetailsReport.getCurrentCurrency(), is(equalTo(CurrencyEnum.GBP.getCurrency())));
        assertThat(gbpGrandTotal, is(not(usdGrandTotal)));
    }

    @Test
    @TestRail(testCaseId = "3205")
    @Description("Verifies that currency change and then reversion works")
    public void testCurrencyCodeReversion() {
        assemblyType = AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType();
        BigDecimal gbpGrandTotal;
        BigDecimal usdGrandTotal;

        assemblyDetailsReport = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        assemblyDetailsReport.clickInputControlsButton()
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class);

        gbpGrandTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Capital Investments");
        assertThat(assemblyDetailsReport.getCurrentCurrency(), is(equalTo(CurrencyEnum.GBP.getCurrency())));

        assemblyDetailsReport.clickInputControlsButton()
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        usdGrandTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Capital Investments");

        assertThat(assemblyDetailsReport.getCurrentCurrency(), is(equalTo(CurrencyEnum.USD.getCurrency())));
        assertThat(usdGrandTotal, is(not(equalTo(gbpGrandTotal))));
    }

    @Test
    @Issue("AP-58059")
    @Issue("AP-53537")
    @TestRail(testCaseId = {"3067", "1929"})
    @Description("Verify totals calculations for Sub Assembly")
    public void testTotalCalculationsForSubAssembly() {
        assemblyType = AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType();

        assemblyDetailsReport = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .clickOk()
            .waitForCorrectAssembly(AssemblySetEnum.SUB_ASSEMBLY.getAssemblySetName())
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class);

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
            assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Cycle Time"),
            assemblyDetailsReport.getExpectedCTGrandTotal(assemblyType, "Cycle Time")
        ), is(true));

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
            assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Piece Part Cost"),
            assemblyDetailsReport.getExpectedFbcPpcGrandTotal(assemblyType, "Piece Part Cost")
        ), is(true));

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
            assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Fully Burdened Cost"),
            assemblyDetailsReport.getExpectedFbcPpcGrandTotal(assemblyType, "Fully Burdened Cost")
        ), is(true));

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
            assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Capital Investments"),
            assemblyDetailsReport.getExpectedCIGrandTotal(assemblyType, "Capital Investments")
        ), is(true));
    }

    @Test
    @Issue("AP-58059")
    @Issue("AP-53537")
    @TestRail(testCaseId = {"3068", "1929"})
    @Description("Verify totals calculations for Sub-Sub-ASM")
    public void testTotalCalculationsForSubSubASM() {
        assemblyType = AssemblyTypeEnum.SUB_SUB_ASM.getAssemblyType();

        assemblyDetailsReport = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .setAssembly(AssemblySetEnum.SUB_SUB_ASM.getAssemblySetName())
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .clickOk()
            .waitForCorrectAssembly(AssemblySetEnum.SUB_SUB_ASM.getAssemblySetName())
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class);

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
            assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Cycle Time"),
            assemblyDetailsReport.getExpectedCTGrandTotal(assemblyType, "Cycle Time")
        ), is(true));

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
            assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Piece Part Cost"),
            assemblyDetailsReport.getExpectedFbcPpcGrandTotal(assemblyType, "Piece Part Cost")
        ), is(true));

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
            assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Fully Burdened Cost"),
            assemblyDetailsReport.getExpectedFbcPpcGrandTotal(assemblyType, "Fully Burdened Cost")
        ), is(true));

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
            assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Capital Investments"),
            assemblyDetailsReport.getExpectedCIGrandTotal(assemblyType, "Capital Investments")
        ), is(true));
    }

    @Test
    @Issue("AP-58059")
    @Issue("AP-53537")
    @TestRail(testCaseId = {"1934", "1929"})
    @Description("Verify totals calculations for Top Level")
    public void testTotalCalculationsForTopLevel() {
        assemblyType = AssemblyTypeEnum.TOP_LEVEL.getAssemblyType();

        assemblyDetailsReport = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .setAssembly(AssemblySetEnum.TOP_LEVEL.getAssemblySetName())
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .clickOk()
            .waitForCorrectAssembly(AssemblySetEnum.TOP_LEVEL.getAssemblySetName())
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class);

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
            assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Cycle Time"),
            assemblyDetailsReport.getExpectedCTGrandTotal(assemblyType, "Cycle Time")
        ), is(true));

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
            assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Piece Part Cost"),
            assemblyDetailsReport.getExpectedFbcPpcGrandTotal(assemblyType, "Piece Part Cost")
        ), is(true));

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
            assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Fully Burdened Cost"),
            assemblyDetailsReport.getExpectedFbcPpcGrandTotal(assemblyType, "Fully Burdened Cost")
        ), is(true));

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
            assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Capital Investments"),
            assemblyDetailsReport.getExpectedCIGrandTotal(assemblyType, "Capital Investments")
        ), is(true));
    }

    @Test
    @TestRail(testCaseId = {"3231", "1929"})
    @Description("Verify sub total calculations for Sub Assembly")
    public void testSubTotalCalculationsSubAssembly() {
        assemblyType = AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType();

        assemblyDetailsReport = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .clickOk()
            .waitForCorrectAssembly(AssemblySetEnum.SUB_ASSEMBLY.getAssemblySetName())
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class);

        ArrayList<BigDecimal> ctValues = assemblyDetailsReport.getSubTotalAdditionValue(assemblyType, "Cycle Time");
        assertThat(assemblyDetailsReport.areValuesAlmostEqual(ctValues.get(0), ctValues.get(1)), is(true));

        ArrayList<BigDecimal> ppcValues = assemblyDetailsReport.getSubTotalAdditionValue(assemblyType, "Piece Part Cost");
        assertThat(assemblyDetailsReport.areValuesAlmostEqual(ppcValues.get(0), ppcValues.get(1)), is(true));

        ArrayList<BigDecimal> fbcValues = assemblyDetailsReport.getSubTotalAdditionValue(assemblyType, "Fully Burdened Cost");
        assertThat(assemblyDetailsReport.areValuesAlmostEqual(fbcValues.get(0), fbcValues.get(1)), is(true));

        ArrayList<BigDecimal> ciValues = assemblyDetailsReport.getSubTotalAdditionValue(assemblyType, "Capital Investments");
        assertThat(assemblyDetailsReport.areValuesAlmostEqual(ciValues.get(0), ciValues.get(1)), is(true));
    }

    @Test
    @TestRail(testCaseId = {"3232", "1929"})
    @Description("Verify sub total calculations for Sub Sub ASM")
    public void testSubTotalCalculationsSubSubAsm() {
        assemblyType = AssemblyTypeEnum.SUB_SUB_ASM.getAssemblyType();

        assemblyDetailsReport = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .setAssembly(AssemblySetEnum.SUB_SUB_ASM.getAssemblySetName())
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .clickOk()
            .waitForCorrectAssembly(AssemblySetEnum.SUB_SUB_ASM.getAssemblySetName())
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class);

        ArrayList<BigDecimal> ctValues = assemblyDetailsReport.getSubTotalAdditionValue(assemblyType, "Cycle Time");
        assertThat(assemblyDetailsReport.areValuesAlmostEqual(ctValues.get(0), ctValues.get(1)), is(true));

        ArrayList<BigDecimal> ppcValues = assemblyDetailsReport.getSubTotalAdditionValue(assemblyType, "Piece Part Cost");
        assertThat(assemblyDetailsReport.areValuesAlmostEqual(ppcValues.get(0), ppcValues.get(1)), is(true));

        ArrayList<BigDecimal> fbcValues = assemblyDetailsReport.getSubTotalAdditionValue(assemblyType, "Fully Burdened Cost");
        assertThat(assemblyDetailsReport.areValuesAlmostEqual(fbcValues.get(0), fbcValues.get(1)), is(true));

        ArrayList<BigDecimal> ciValues = assemblyDetailsReport.getSubTotalAdditionValue(assemblyType, "Capital Investments");
        assertThat(assemblyDetailsReport.areValuesAlmostEqual(ciValues.get(0), ciValues.get(1)), is(true));
    }

    @Test
    @TestRail(testCaseId = {"3233", "1929"})
    @Description("Verify sub total calculations for Top Level")
    public void testSubTotalCalculationsTopLevel() {
        assemblyType = AssemblyTypeEnum.TOP_LEVEL.getAssemblyType();

        assemblyDetailsReport = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .setAssembly(AssemblySetEnum.TOP_LEVEL.getAssemblySetName())
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .clickOk()
            .waitForCorrectAssembly(AssemblySetEnum.TOP_LEVEL.getAssemblySetName())
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class);

        ArrayList<BigDecimal> ctValues = assemblyDetailsReport.getSubTotalAdditionValue(assemblyType, "Cycle Time");
        assertThat(assemblyDetailsReport.areValuesAlmostEqual(ctValues.get(0), ctValues.get(1)), is(true));

        ArrayList<BigDecimal> ppcValues = assemblyDetailsReport.getSubTotalAdditionValue(assemblyType, "Piece Part Cost");
        assertThat(assemblyDetailsReport.areValuesAlmostEqual(ppcValues.get(0), ppcValues.get(1)), is(true));

        ArrayList<BigDecimal> fbcValues = assemblyDetailsReport.getSubTotalAdditionValue(assemblyType, "Fully Burdened Cost");
        assertThat(assemblyDetailsReport.areValuesAlmostEqual(fbcValues.get(0), fbcValues.get(1)), is(true));

        ArrayList<BigDecimal> ciValues = assemblyDetailsReport.getSubTotalAdditionValue(assemblyType, "Capital Investments");
        assertThat(assemblyDetailsReport.areValuesAlmostEqual(ciValues.get(0), ciValues.get(1)), is(true));
    }

    @Test
    @TestRail(testCaseId = "1919")
    @Description("Ensuring latest export date filter works properly (uses date input field)")
    public void testLatestExportDateFilterUsingInput() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName());
    }

    @Test
    @TestRail(testCaseId = "3244")
    @Description("Ensuring latest export date filter works properly (using date picker)")
    public void testLatestExportDateFilterUsingDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName());
    }

    @Test
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
            .openNewTabAndFocus(1);

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
}
