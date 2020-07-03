package cireporttests.ootbreports.general.assemblydetails;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.ComponentsPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.reports.pages.homepage.ReportsHomePage;
import com.apriori.pageobjects.reports.pages.library.LibraryPage;
import com.apriori.pageobjects.reports.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.reports.pages.view.ViewRepositoryPage;
import com.apriori.pageobjects.reports.pages.view.ViewSearchResultsPage;
import com.apriori.pageobjects.reports.pages.view.enums.AssemblyReportsEnum;
import com.apriori.pageobjects.reports.pages.view.enums.AssemblySetEnum;
import com.apriori.pageobjects.reports.pages.view.enums.ExportSetEnum;
import com.apriori.pageobjects.reports.pages.view.reports.AssemblyDetailsReportPage;
import com.apriori.pageobjects.reports.pages.view.reports.GenericReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.enums.AssemblyTypeEnum;
import com.apriori.utils.enums.ColumnIndexEnum;
import com.apriori.utils.enums.ComponentInfoColumnEnum;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.MsSQLOracleLocalInstallTest;
import testsuites.suiteinterface.MySQLTest;
import testsuites.suiteinterface.CIARStagingSmokeTest;
import testsuites.suiteinterface.CustomerSmokeTests;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssemblyDetailsReportTests extends TestBase {

    String assemblyType = "";
    private AssemblyDetailsReportPage assemblyDetailsReportPage;
    private GenericReportPage genericReportPage;
    private ViewSearchResultsPage searchResults;
    private ViewRepositoryPage repository;
    private LibraryPage library;
    private ReportsHomePage homePage;

    public AssemblyDetailsReportTests() {
        super();
    }

    @Test
    @Category(MsSQLOracleLocalInstallTest.class)
    @TestRail(testCaseId = "1915")
    @Description("validate report is available by navigation")
    public void testReportAvailabilityByMenu() {
        repository = new ReportsLoginPage(driver)
            .login()
            .navigateToViewRepositoryPage()
            .navigateToGeneralFolder();

        assertThat(repository.getCountOfGeneralReports(), is(equalTo(5)));

        AssemblyReportsEnum[] reportNames = AssemblyReportsEnum.values();
        for (AssemblyReportsEnum report : reportNames) {
            assertThat(repository.getReportName(report.getReportName()), is(equalTo(report.getReportName())));
        }
    }

    @Test
    @Category({MsSQLOracleLocalInstallTest.class, CIARStagingSmokeTest.class})
    @TestRail(testCaseId = "3060")
    @Description("Validate report is available by library")
    public void testReportAvailabilityByLibrary() {
        library = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage();

        AssemblyReportsEnum[] reportNames = AssemblyReportsEnum.values();
        for (AssemblyReportsEnum report : reportNames) {
            assertThat(library.getReportName(report.getReportName()), is(equalTo(report.getReportName())));
        }
    }

    @Test
    @Category(MsSQLOracleLocalInstallTest.class)
    @TestRail(testCaseId = "1916")
    @Description("Validate report is available by search")
    public void testReportAvailableBySearch() {
        homePage = new ReportsLoginPage(driver)
            .login();

        searchResults = new ViewSearchResultsPage(driver);

        AssemblyReportsEnum[] reportNames = AssemblyReportsEnum.values();
        for (AssemblyReportsEnum report : reportNames) {
            homePage.searchForReport(report.getReportName());
            assertThat(searchResults.getReportName(report.getReportName()), is(equalTo(report.getReportName())));
        }
    }

    @Test
    @Category({CustomerSmokeTests.class, CIARStagingSmokeTest.class, MsSQLOracleLocalInstallTest.class})
    @TestRail(testCaseId = "1922")
    @Description("Verifies that the currency code works properly")
    public void testCurrencyCodeWorks() {
        assemblyType = AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType();
        BigDecimal gbpGrandTotal;
        BigDecimal usdGrandTotal;

        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .scrollDownInputControls()
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
    @Category(MsSQLOracleLocalInstallTest.class)
    @TestRail(testCaseId = "3205")
    @Description("Verifies that currency change and then reversion works")
    public void testCurrencyCodeReversion() {
        assemblyType = AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType();
        BigDecimal gbpGrandTotal;
        BigDecimal usdGrandTotal;

        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .scrollDownInputControls()
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
    @Category(MsSQLOracleLocalInstallTest.class)
    @Issue("AP-58059")
    @Issue("AP-53537")
    @TestRail(testCaseId = {"3067", "1929"})
    @Description("Verify totals calculations for Sub Assembly")
    public void testTotalCalculationsForSubAssembly() {
        assemblyType = AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType();

        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .scrollDownInputControls()
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
    @Category(MsSQLOracleLocalInstallTest.class)
    @Issue("AP-58059")
    @Issue("AP-53537")
    @TestRail(testCaseId = {"3068", "1929"})
    @Description("Verify totals calculations for Sub-Sub-ASM")
    public void testTotalCalculationsForSubSubASM() {
        assemblyType = AssemblyTypeEnum.SUB_SUB_ASM.getAssemblyType();

        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .scrollDownInputControls()
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
    @Category({MsSQLOracleLocalInstallTest.class, CIARStagingSmokeTest.class})
    @Issue("AP-58059")
    @Issue("AP-53537")
    @TestRail(testCaseId = {"1934", "1929"})
    @Description("Verify totals calculations for Top Level")
    public void testTotalCalculationsForTopLevel() {
        assemblyType = AssemblyTypeEnum.TOP_LEVEL.getAssemblyType();

        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .scrollDownInputControls()
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
    @Category(MsSQLOracleLocalInstallTest.class)
    @TestRail(testCaseId = {"3231", "1929"})
    @Description("Verify sub total calculations for Sub Assembly")
    public void testSubTotalCalculationsSubAssembly() {
        assemblyType = AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType();

        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .scrollDownInputControls()
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
    @Category(MsSQLOracleLocalInstallTest.class)
    @TestRail(testCaseId = {"3232", "1929"})
    @Description("Verify sub total calculations for Sub Sub ASM")
    public void testSubTotalCalculationsSubSubAsm() {
        assemblyType = AssemblyTypeEnum.SUB_SUB_ASM.getAssemblyType();

        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .scrollDownInputControls()
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
    @Category(MsSQLOracleLocalInstallTest.class)
    @TestRail(testCaseId = {"3233", "1929"})
    @Description("Verify sub total calculations for Top Level")
    public void testSubTotalCalculationsTopLevel() {
        assemblyType = AssemblyTypeEnum.TOP_LEVEL.getAssemblyType();

        assemblyDetailsReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .scrollDownInputControls()
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
    @Category(MsSQLOracleLocalInstallTest.class)
    @TestRail(testCaseId = "1919")
    @Issue("AP-54036")
    @Description("Ensuring latest export date filter works properly (uses date input field)")
    public void testLatestExportDateFilterUsingInput() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .setLatestExportDateToTodayInput()
            .ensureDatesAreCorrect(false, true);

        // TODO: Re-add waitForCorrectExportSetListCount("0") if is ever bug fixed
        assertThat(genericReportPage.getAmountOfTopLevelExportSets(), is(0));
    }

    @Test
    @Category(MsSQLOracleLocalInstallTest.class)
    @TestRail(testCaseId = "3244")
    @Issue("AP-54036")
    @Description("Ensuring latest export date filter works properly (using date picker)")
    public void testLatestExportDateFilterUsingDatePicker() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .setLatestExportDateToTodayPlusTwoPicker()
            .ensureDatesAreCorrect(false, false);

        // TODO: Re-add waitForCorrectExportSetListCount("0") if is ever bug fixed
        assertThat(genericReportPage.getAmountOfTopLevelExportSets(), is(0));
    }

    @Test
    @Category(MsSQLOracleLocalInstallTest.class)
    @TestRail(testCaseId = "1930")
    @Description("Test Export Set with costing failures costing incomplete")
    public void testExportSetWithCostingFailuresCostingIncomplete() {
        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName(), AssemblyDetailsReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.PISTON_ASSEMBLY.getExportSetName())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), GenericReportPage.class)
            .openNewTabAndFocus(1);

        List<String> columnsToRemove = Arrays.asList(
            ComponentInfoColumnEnum.QUANTITY.getColumnName(),
            ComponentInfoColumnEnum.PROCESS_GROUP.getColumnName(),
            ComponentInfoColumnEnum.VPE.getColumnName(),
            ComponentInfoColumnEnum.LAST_SAVED.getColumnName(),
            ComponentInfoColumnEnum.LAST_COSTED.getColumnName()
        );

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
            .checkColumnSettings(columnsToAdd, columnsToRemove)
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
