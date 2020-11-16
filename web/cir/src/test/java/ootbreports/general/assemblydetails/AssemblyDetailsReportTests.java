package ootbreports.general.assemblydetails;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.enums.ColumnIndexEnum;
import com.apriori.utils.enums.ComponentInfoColumnEnum;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.reports.AssemblySetEnum;
import com.apriori.utils.enums.reports.AssemblyTypeEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.ListNameEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.web.driver.TestBase;

import inputcontrols.InputControlsTests;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import navigation.CommonReportTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageobjects.pages.evaluate.ComponentsPage;
import pageobjects.pages.evaluate.EvaluatePage;
import pageobjects.pages.explore.ExplorePage;
import pageobjects.pages.login.ReportsLoginPage;
import pageobjects.pages.view.reports.AssemblyDetailsReportPage;
import pageobjects.pages.view.reports.GenericReportPage;
import testsuites.suiteinterface.CIARStagingSmokeTest;
import testsuites.suiteinterface.CustomerSmokeTests;

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
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = "1915")
    @Description("Validate report is available by navigation")
    public void testReportAvailabilityByMenuAssemblyCostA4() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                Constants.GENERAL_FOLDER,
                ReportNamesEnum.ASSEMBLY_COST_A4.getReportName()
        );
    }

    @Test
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = "1915")
    @Description("Validate report is available by navigation")
    public void testReportAvailabilityByMenuAssemblyCostLetter() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                Constants.GENERAL_FOLDER,
                ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName()
        );
    }

    @Test
    @Category(CIARStagingSmokeTest.class)
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
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = "1915")
    @Description("Validate report is available by navigation")
    public void testReportAvailabilityByMenuComponentCost() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                Constants.GENERAL_FOLDER,
                ReportNamesEnum.COMPONENT_COST.getReportName()
        );
    }

    @Test
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = "1915")
    @Description("Validate report is available by navigation")
    public void testReportAvailabilityByMenuScenarioComparison() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByNavigation(
                Constants.GENERAL_FOLDER,
                ReportNamesEnum.SCENARIO_COMPARISON.getReportName()
        );
    }

    @Test
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = "3060")
    @Description("Validate report is available by library")
    public void testReportAvailabilityByLibraryAssemblyCostA4() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.ASSEMBLY_COST_A4.getReportName());
    }

    @Test
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = "3060")
    @Description("Validate report is available by library")
    public void testReportAvailabilityByLibraryAssemblyCostLetter() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName());
    }

    @Test
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = "3060")
    @Description("Validate report is available by library")
    public void testReportAvailabilityByLibraryAssemblyDetails() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName());
    }

    @Test
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = "3060")
    @Description("Validate report is available by library")
    public void testReportAvailabilityByLibraryComponentCost() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.COMPONENT_COST.getReportName());
    }

    @Test
    @Category(CIARStagingSmokeTest.class)
    @TestRail(testCaseId = "3060")
    @Description("Validate report is available by library")
    public void testReportAvailabilityByLibraryScenarioComparison() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityByLibrary(ReportNamesEnum.SCENARIO_COMPARISON.getReportName());
    }

    @Test
    @TestRail(testCaseId = "1916")
    @Description("Validate report is available by search")
    public void testReportAvailableBySearchAssemblyCostA4() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.ASSEMBLY_COST_A4.getReportName());
    }

    @Test
    @TestRail(testCaseId = "1916")
    @Description("Validate report is available by search")
    public void testReportAvailableBySearchAssemblyCostLetter() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName());
    }

    @Test
    @TestRail(testCaseId = "1916")
    @Description("Validate report is available by search")
    public void testReportAvailableBySearchAssemblyDetails() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName());
    }

    @Test
    @TestRail(testCaseId = "1916")
    @Description("Validate report is available by search")
    public void testReportAvailableBySearchComponentCost() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.COMPONENT_COST.getReportName());
    }

    @Test
    @TestRail(testCaseId = "1916")
    @Description("Validate report is available by search")
    public void testReportAvailableBySearchScenarioComparison() {
        commonReportTests = new CommonReportTests(driver);
        commonReportTests.testReportAvailabilityBySearch(ReportNamesEnum.SCENARIO_COMPARISON.getReportName());
    }

    @Test
    @Category({CustomerSmokeTests.class, CIARStagingSmokeTest.class})
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

        usdGrandTotal = assemblyDetailsReportPage.getValueFromTable(assemblyType, "Grand Total", "Capital Investments");

        assemblyDetailsReportPage.clickInputControlsButton()
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class);

        gbpGrandTotal = assemblyDetailsReportPage.getValueFromTable(assemblyType, "Grand Total", "Capital Investments");

        assertThat(assemblyDetailsReportPage.getCurrentCurrency(), is(equalTo(CurrencyEnum.GBP.getCurrency())));
        assertThat(gbpGrandTotal, is(not(usdGrandTotal)));
    }

    @Test
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

        gbpGrandTotal = assemblyDetailsReportPage.getValueFromTable(assemblyType, "Grand Total", "Capital Investments");
        assertThat(assemblyDetailsReportPage.getCurrentCurrency(), is(equalTo(CurrencyEnum.GBP.getCurrency())));

        assemblyDetailsReportPage.clickInputControlsButton()
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        usdGrandTotal = assemblyDetailsReportPage.getValueFromTable(assemblyType, "Grand Total", "Capital Investments");

        assertThat(assemblyDetailsReportPage.getCurrentCurrency(), is(equalTo(CurrencyEnum.USD.getCurrency())));
        assertThat(usdGrandTotal, is(not(equalTo(gbpGrandTotal))));
    }

    @Test
    @Issue("AP-58059")
    @Issue("AP-53537")
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
            .waitForCorrectAssembly(AssemblySetEnum.SUB_ASSEMBLY.getAssemblySetName())
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class);

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(assemblyType, "Grand Total", "Cycle Time"),
            assemblyDetailsReportPage.getExpectedCTGrandTotal(assemblyType, "Cycle Time")
        ), is(true));

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(assemblyType, "Grand Total", "Piece Part Cost"),
            assemblyDetailsReportPage.getExpectedFbcPpcGrandTotal(assemblyType, "Piece Part Cost")
        ), is(true));

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(assemblyType, "Grand Total", "Fully Burdened Cost"),
            assemblyDetailsReportPage.getExpectedFbcPpcGrandTotal(assemblyType, "Fully Burdened Cost")
        ), is(true));

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(assemblyType, "Grand Total", "Capital Investments"),
            assemblyDetailsReportPage.getExpectedCIGrandTotal(assemblyType, "Capital Investments")
        ), is(true));
    }

    @Test
    @Issue("AP-58059")
    @Issue("AP-53537")
    @TestRail(testCaseId = {"3068", "1929"})
    @Description("Verify totals calculations for Sub-Sub-ASM")
    public void testTotalCalculationsForSubSubASM() {
        assemblyType = AssemblyTypeEnum.SUB_SUB_ASM.getAssemblyType();

        assemblyDetailsReportPage = new ReportsLoginPage(driver)
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

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(assemblyType, "Grand Total", "Cycle Time"),
            assemblyDetailsReportPage.getExpectedCTGrandTotal(assemblyType, "Cycle Time")
        ), is(true));

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(assemblyType, "Grand Total", "Piece Part Cost"),
            assemblyDetailsReportPage.getExpectedFbcPpcGrandTotal(assemblyType, "Piece Part Cost")
        ), is(true));

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(assemblyType, "Grand Total", "Fully Burdened Cost"),
            assemblyDetailsReportPage.getExpectedFbcPpcGrandTotal(assemblyType, "Fully Burdened Cost")
        ), is(true));

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(assemblyType, "Grand Total", "Capital Investments"),
            assemblyDetailsReportPage.getExpectedCIGrandTotal(assemblyType, "Capital Investments")
        ), is(true));
    }

    @Test
    @Issue("AP-58059")
    @Issue("AP-53537")
    @TestRail(testCaseId = {"1934", "1929"})
    @Description("Verify totals calculations for Top Level")
    public void testTotalCalculationsForTopLevel() {
        assemblyType = AssemblyTypeEnum.TOP_LEVEL.getAssemblyType();

        assemblyDetailsReportPage = new ReportsLoginPage(driver)
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

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(assemblyType, "Grand Total", "Cycle Time"),
            assemblyDetailsReportPage.getExpectedCTGrandTotal(assemblyType, "Cycle Time")
        ), is(true));

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(assemblyType, "Grand Total", "Piece Part Cost"),
            assemblyDetailsReportPage.getExpectedFbcPpcGrandTotal(assemblyType, "Piece Part Cost")
        ), is(true));

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(assemblyType, "Grand Total", "Fully Burdened Cost"),
            assemblyDetailsReportPage.getExpectedFbcPpcGrandTotal(assemblyType, "Fully Burdened Cost")
        ), is(true));

        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(
            assemblyDetailsReportPage.getValueFromTable(assemblyType, "Grand Total", "Capital Investments"),
            assemblyDetailsReportPage.getExpectedCIGrandTotal(assemblyType, "Capital Investments")
        ), is(true));
    }

    @Test
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

        ArrayList<BigDecimal> ctValues = assemblyDetailsReportPage.getSubTotalAdditionValue(assemblyType, "Cycle Time");
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(ctValues.get(0), ctValues.get(1)), is(true));

        ArrayList<BigDecimal> ppcValues = assemblyDetailsReportPage.getSubTotalAdditionValue(assemblyType, "Piece Part Cost");
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(ppcValues.get(0), ppcValues.get(1)), is(true));

        ArrayList<BigDecimal> fbcValues = assemblyDetailsReportPage.getSubTotalAdditionValue(assemblyType, "Fully Burdened Cost");
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(fbcValues.get(0), fbcValues.get(1)), is(true));

        ArrayList<BigDecimal> ciValues = assemblyDetailsReportPage.getSubTotalAdditionValue(assemblyType, "Capital Investments");
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(ciValues.get(0), ciValues.get(1)), is(true));
    }

    @Test
    @TestRail(testCaseId = {"3232", "1929"})
    @Description("Verify sub total calculations for Sub Sub ASM")
    public void testSubTotalCalculationsSubSubAsm() {
        assemblyType = AssemblyTypeEnum.SUB_SUB_ASM.getAssemblyType();

        assemblyDetailsReportPage = new ReportsLoginPage(driver)
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

        ArrayList<BigDecimal> ctValues = assemblyDetailsReportPage.getSubTotalAdditionValue(assemblyType, "Cycle Time");
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(ctValues.get(0), ctValues.get(1)), is(true));

        ArrayList<BigDecimal> ppcValues = assemblyDetailsReportPage.getSubTotalAdditionValue(assemblyType, "Piece Part Cost");
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(ppcValues.get(0), ppcValues.get(1)), is(true));

        ArrayList<BigDecimal> fbcValues = assemblyDetailsReportPage.getSubTotalAdditionValue(assemblyType, "Fully Burdened Cost");
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(fbcValues.get(0), fbcValues.get(1)), is(true));

        ArrayList<BigDecimal> ciValues = assemblyDetailsReportPage.getSubTotalAdditionValue(assemblyType, "Capital Investments");
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(ciValues.get(0), ciValues.get(1)), is(true));
    }

    @Test
    @TestRail(testCaseId = {"3233", "1929"})
    @Description("Verify sub total calculations for Top Level")
    public void testSubTotalCalculationsTopLevel() {
        assemblyType = AssemblyTypeEnum.TOP_LEVEL.getAssemblyType();

        assemblyDetailsReportPage = new ReportsLoginPage(driver)
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

        ArrayList<BigDecimal> ctValues = assemblyDetailsReportPage.getSubTotalAdditionValue(assemblyType, "Cycle Time");
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(ctValues.get(0), ctValues.get(1)), is(true));

        ArrayList<BigDecimal> ppcValues = assemblyDetailsReportPage.getSubTotalAdditionValue(assemblyType, "Piece Part Cost");
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(ppcValues.get(0), ppcValues.get(1)), is(true));

        ArrayList<BigDecimal> fbcValues = assemblyDetailsReportPage.getSubTotalAdditionValue(assemblyType, "Fully Burdened Cost");
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(fbcValues.get(0), fbcValues.get(1)), is(true));

        ArrayList<BigDecimal> ciValues = assemblyDetailsReportPage.getSubTotalAdditionValue(assemblyType, "Capital Investments");
        assertThat(assemblyDetailsReportPage.areValuesAlmostEqual(ciValues.get(0), ciValues.get(1)), is(true));
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
    @TestRail(testCaseId = "1920")
    @Description("Export set count is correct")
    public void testExportSetSelectionOptions() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetSelection(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName());
    }

    @Test
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
        assertThat(genericReportPage.getReportTitle(), is(equalTo(ReportNamesEnum.COMPONENT_COST_INTERNAL_USE.getReportName())));
        assertThat(genericReportPage.getComponentCostPartNumber(), is(equalTo(partNumberComponent)));
        genericReportPage.closeTab();

        String partNumberAssembly = genericReportPage.getAssemblyLinkPartNumber();
        genericReportPage.clickAssemblyLinkAssemblyDetails();
        assertThat(genericReportPage.getReportTitle(), is(equalTo(ReportNamesEnum.COMPONENT_COST_INTERNAL_USE.getReportName())));
        assertThat(genericReportPage.getComponentCostPartNumber(), is(equalTo(partNumberAssembly)));
    }

    @Test
    @TestRail(testCaseId = "1921")
    @Description("Export Set search function works (plus other filters)")
    public void testExportSetSearch() {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
                .waitForInputControlsLoad();

        genericReportPage.searchForExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName());

        assertThat(genericReportPage.getExportSetOptionCount(), is(equalTo("1")));
        assertThat(genericReportPage.isExportSetVisible(ExportSetEnum.TOP_LEVEL.getExportSetName()), is(true));
    }

    @Test
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
    @TestRail(testCaseId = "1921")
    @Description("Export set search function works (plus other filters)")
    public void testCreatedByFilterOperation() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testListFilterOperation(
                ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(),
                ListNameEnum.CREATED_BY.getListName()
        );
    }

    @Test
    @TestRail(testCaseId = "1921")
    @Description("Export set search function works (plus other filters)")
    public void testCreatedByFilterButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testListFilterButtons(
                ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(),
                ListNameEnum.CREATED_BY.getListName()
        );
    }

    @Test
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
    @TestRail(testCaseId = "1921")
    @Description("Export set search function works (plus other filters)")
    public void testLastModifiedFilterOperation() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testListFilterOperation(
                ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(),
                ListNameEnum.LAST_MODIFIED_BY.getListName()
        );
    }

    @Test
    @TestRail(testCaseId = "1921")
    @Description("Export set search function works (plus other filters)")
    public void testLastModifiedFilterButtons() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testListFilterButtons(
                ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(),
                ListNameEnum.LAST_MODIFIED_BY.getListName()
        );
    }

    @Test
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
}