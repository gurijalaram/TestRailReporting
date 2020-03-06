package cireporttests.ootbreports.general.assemblydetails;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.reports.pages.homepage.HomePage;
import com.apriori.pageobjects.reports.pages.library.LibraryPage;
import com.apriori.pageobjects.reports.pages.login.LoginPage;
import com.apriori.pageobjects.reports.pages.view.ViewRepositoryPage;
import com.apriori.pageobjects.reports.pages.view.ViewSearchResultsPage;
import com.apriori.pageobjects.reports.pages.view.enums.AssemblyReportsEnum;
import com.apriori.pageobjects.reports.pages.view.enums.AssemblySetEnum;
import com.apriori.pageobjects.reports.pages.view.enums.ExportSetEnum;
import com.apriori.pageobjects.reports.pages.view.reports.AssemblyDetailsReportPage;
import com.apriori.pageobjects.reports.pages.view.reports.GenericReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.AssemblyTypeEnum;
import com.apriori.utils.enums.ColumnIndexEnum;
import com.apriori.utils.enums.ComponentInfoColumnEnum;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class AssemblyDetailsReportTests extends TestBase {

    private AssemblyDetailsReportPage assemblyDetailsReport;
    private GenericReportPage genericReportPage;
    private ViewSearchResultsPage searchResults;
    private ViewRepositoryPage repository;
    private LibraryPage library;
    private HomePage homePage;

    String assemblyType = "";

    public AssemblyDetailsReportTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "1915")
    @Description("validate report is available by navigation")
    public void testReportAvailabilityByMenu() {
        repository = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToViewRepositoryPage()
            .navigateToGeneralFolder();

        assertThat(repository.getCountOfGeneralReports(), is(equalTo(5)));

        AssemblyReportsEnum[] reportNames = AssemblyReportsEnum.values();
        for (AssemblyReportsEnum report : reportNames) {
            assertThat(repository.getReportName(report.getReportName()), is(equalTo(report.getReportName())));
        }
    }

    @Test
    @TestRail(testCaseId = "3060")
    @Description("Validate report is available by library")
    public void testReportAvailabilityByLibrary() {
        library = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToLibraryPage();

        AssemblyReportsEnum[] reportNames = AssemblyReportsEnum.values();
        for (AssemblyReportsEnum report : reportNames) {
            assertThat(library.getReportName(report.getReportName()), is(equalTo(report.getReportName())));
        }
    }

    @Test
    @TestRail(testCaseId = "1916")
    @Description("Validate report is available by search")
    public void testReportAvailableBySearch() {
        homePage = new LoginPage(driver)
            .login(UserUtil.getUser());

        searchResults = new ViewSearchResultsPage(driver);

        AssemblyReportsEnum[] reportNames = AssemblyReportsEnum.values();
        for (AssemblyReportsEnum report : reportNames) {
            homePage.searchForReport(report.getReportName());
            assertThat(searchResults.getReportName(report.getReportName()), is(equalTo(report.getReportName())));
        }
    }

    @Test
    @TestRail(testCaseId = "1922")
    @Description("Verifies that the currency code works properly")
    public void testCurrencyCodeWorks() {
        assemblyType = AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType();
        BigDecimal gbpGrandTotal;
        BigDecimal usdGrandTotal;

        assemblyDetailsReport = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToLibraryPage()
            .navigateToReport(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName())
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .scrollDownInputControls()
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
            .clickApplyAndOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        usdGrandTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Capital Investments");

        assemblyDetailsReport.clickInputControlsButton()
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .clickApplyAndOk()
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

        assemblyDetailsReport = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToLibraryPage()
            .navigateToReport(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName())
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .scrollDownInputControls()
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
            .clickApplyAndOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        assemblyDetailsReport.clickInputControlsButton()
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .clickApplyAndOk()
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class);

        gbpGrandTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Capital Investments");
        assertThat(assemblyDetailsReport.getCurrentCurrency(), is(equalTo(CurrencyEnum.GBP.getCurrency())));

        assemblyDetailsReport.clickInputControlsButton()
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
            .clickApplyAndOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class);

        usdGrandTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Capital Investments");

        assertThat(assemblyDetailsReport.getCurrentCurrency(), is(equalTo(CurrencyEnum.USD.getCurrency())));
        assertThat(usdGrandTotal, is(not(equalTo(gbpGrandTotal))));
    }

    @Test
    @TestRail(testCaseId = {"3067", "1929"})
    @Description("Verify totals calculations for Sub Assembly")
    public void testTotalCalculationsForSubAssembly() {
        assemblyType = AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType();

        assemblyDetailsReport = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToLibraryPage()
            .navigateToReport(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName())
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .scrollDownInputControls()
            .setAssembly(AssemblySetEnum.SUB_ASSEMBLY.getAssemblySetName())
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .clickApplyAndOk()
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class)
            .waitForCorrectAssembly(assemblyType, AssemblyDetailsReportPage.class);

        /*
            The reason for the range check in areValuesAlmostEqual is that there is a rounding bug.
            Initial rounding bug (similar issue, in a different report): https://jira.apriori.com/browse/AP-53537
            Bug for this issue: https://jira.apriori.com/browse/AP-58059
         */

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
            assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Cycle Time"),
            assemblyDetailsReport.getExpectedCTGrandTotal(assemblyType, "Cycle Time")
        ), is(true));

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
            assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Piece Part Cost"),
            assemblyDetailsReport.getExpectedPPCGrandTotal(assemblyType, "Piece Part Cost")
        ), is(true));

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
            assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Fully Burdened Cost"),
            assemblyDetailsReport.getExpectedFBCGrandTotal(assemblyType, "Fully Burdened Cost")
        ), is(true));

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
            assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Capital Investments"),
            assemblyDetailsReport.getExpectedCIGrandTotal(assemblyType, "Capital Investments")
        ), is(true));
    }

    @Test
    @TestRail(testCaseId = {"3068", "1929"})
    @Description("Verify totals calculations for Sub-Sub-ASM")
    public void testTotalCalculationsForSubSubASM() {
        assemblyType = AssemblyTypeEnum.SUB_SUB_ASM.getAssemblyType();

        assemblyDetailsReport = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToLibraryPage()
            .navigateToReport(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName())
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .scrollDownInputControls()
            .setAssembly(AssemblySetEnum.SUB_SUB_ASM.getAssemblySetName())
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .clickApplyAndOk()
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class)
            .waitForCorrectAssembly(assemblyType, AssemblyDetailsReportPage.class);

        /*
            The reason for the range check in areValuesAlmostEqual is that there is a rounding bug.
            Initial rounding bug (similar issue, in a different report): https://jira.apriori.com/browse/AP-53537
            Bug for this issue: https://jira.apriori.com/browse/AP-58059
         */

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
            assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Cycle Time"),
            assemblyDetailsReport.getExpectedCTGrandTotal(assemblyType, "Cycle Time")
        ), is(true));

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
            assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Piece Part Cost"),
            assemblyDetailsReport.getExpectedPPCGrandTotal(assemblyType, "Piece Part Cost")
        ), is(true));

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
            assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Fully Burdened Cost"),
            assemblyDetailsReport.getExpectedFBCGrandTotal(assemblyType, "Fully Burdened Cost")
        ), is(true));

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
            assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Capital Investments"),
            assemblyDetailsReport.getExpectedCIGrandTotal(assemblyType, "Capital Investments")
        ), is(true));
    }

    @Test
    @TestRail(testCaseId = {"1934", "1929"})
    @Description("Verify totals calculations for Top Level")
    public void testTotalCalculationsForTopLevel() {
        assemblyType = AssemblyTypeEnum.TOP_LEVEL.getAssemblyType();

        assemblyDetailsReport = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToLibraryPage()
            .navigateToReport(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName())
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .scrollDownInputControls()
            .setAssembly(AssemblySetEnum.TOP_LEVEL.getAssemblySetName())
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .clickApplyAndOk()
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class)
            .waitForCorrectAssembly(assemblyType, AssemblyDetailsReportPage.class);

        /*
            The reason for the range check in areValuesAlmostEqual is that there is a rounding bug.
            Initial rounding bug (similar issue, in a different report): https://jira.apriori.com/browse/AP-53537
            Bug for this issue: https://jira.apriori.com/browse/AP-58059
         */

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
            assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Cycle Time"),
            assemblyDetailsReport.getExpectedCTGrandTotal(assemblyType, "Cycle Time")
        ), is(true));

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
            assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Piece Part Cost"),
            assemblyDetailsReport.getExpectedPPCGrandTotal(assemblyType, "Piece Part Cost")
        ), is(true));

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
            assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Fully Burdened Cost"),
            assemblyDetailsReport.getExpectedFBCGrandTotal(assemblyType, "Fully Burdened Cost")
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

        assemblyDetailsReport = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToLibraryPage()
            .navigateToReport(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName())
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .scrollDownInputControls()
            .setAssembly(AssemblySetEnum.SUB_ASSEMBLY.getAssemblySetName())
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .clickApplyAndOk()
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class)
            .waitForCorrectAssembly(assemblyType, AssemblyDetailsReportPage.class);

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

        assemblyDetailsReport = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToLibraryPage()
            .navigateToReport(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName())
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .scrollDownInputControls()
            .setAssembly(AssemblySetEnum.SUB_SUB_ASM.getAssemblySetName())
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .clickApplyAndOk()
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class)
            .waitForCorrectAssembly(assemblyType, AssemblyDetailsReportPage.class);

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

        assemblyDetailsReport = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToLibraryPage()
            .navigateToReport(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName())
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
            .scrollDownInputControls()
            .setAssembly(AssemblySetEnum.TOP_LEVEL.getAssemblySetName())
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .clickApplyAndOk()
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class)
            .waitForCorrectAssembly(assemblyType, AssemblyDetailsReportPage.class);

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
    @Issue("AP-54036")
    @Description("Ensuring latest export date filter works properly (uses date input field)")
    public void testLatestExportDateFilterUsingInput() {
        genericReportPage = new LoginPage(driver)
                .login(UserUtil.getUser())
                .navigateToLibraryPage()
                .navigateToReport(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName())
                .waitForInputControlsLoad()
                .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
                .setLatestExportDateToTodayInput()
                .ensureDatesAreCorrect(false, true);
        //.waitForCorrectExportSetListCount("0");

        // If this assertion fails, test fails as the export set is there because bug is not yet fixed
        // TODO: Bring last method above back in once bug fixed
        assertThat(genericReportPage.getAmountOfTopLevelExportSets(), is(0));
    }

    @Test
    @TestRail(testCaseId = "3244")
    @Issue("AP-54036")
    @Description("Ensuring latest export date filter works properly (using date picker)")
    public void testLatestExportDateFilterUsingDatePicker() {
        genericReportPage = new LoginPage(driver)
                .login(UserUtil.getUser())
                .navigateToLibraryPage()
                .navigateToReport(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName())
                .waitForInputControlsLoad()
                .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
                .setLatestExportDateToTodayPlusTwoPicker()
                .ensureDatesAreCorrect(false, false);
        //.waitForCorrectExportSetListCount("0");

        // If this assertion fails, test fails as the export set is there because bug is not yet fixed
        // TODO: Bring last method above back in once bug fixed
        assertThat(genericReportPage.getAmountOfTopLevelExportSets(), is(0));
    }

    @Test
    @TestRail(testCaseId = "1930")
    @Description("Test Export Set with costing failures costing incomplete")
    public void testExportSetWithCostingFailuresCostingIncomplete() {
        assemblyDetailsReport = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToLibraryPage()
            .navigateToReport(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName())
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.PISTON_ASSEMBLY.getExportSetName())
            .clickApplyAndOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), AssemblyDetailsReportPage.class)
            .openNewTabAndFocus();

        List<String> columnsToRemove = Arrays.asList(
            ComponentInfoColumnEnum.QUANTITY.getColumnName(),
            ComponentInfoColumnEnum.PROCESS_GROUP.getColumnName(),
            ComponentInfoColumnEnum.VPE.getColumnName(),
            ComponentInfoColumnEnum.LAST_SAVED.getColumnName(),
            ComponentInfoColumnEnum.LAST_COSTED.getColumnName()
        );

        List<String> columnsToAdd = Arrays.asList(
            ComponentInfoColumnEnum.CYCLE_TIME.getColumnName(),
            ComponentInfoColumnEnum.PER_PART_COST.getColumnName(),
            ComponentInfoColumnEnum.FULLY_BURDENED_COST.getColumnName(),
            ComponentInfoColumnEnum.CAPITAL_INVESTMENT.getColumnName()
        );

        EvaluatePage evaluatePage = new ExplorePage(driver)
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .openAssembly("Initial", "PISTON_ASSEMBLY")
            .openComponentsTable()
            .openColumnsTable()
            .checkColumnSettings(columnsToAdd, columnsToRemove)
            .selectSaveButton();

        ArrayList<BigDecimal> cidPartOneValues = evaluatePage
            .getTableValsByRow(
                ColumnIndexEnum.CID_PART_ONE.getColumnIndex()
            );
        ArrayList<BigDecimal> cidPartTwoValues = evaluatePage
            .getTableValsByRow(
                ColumnIndexEnum.CID_PART_TWO.getColumnIndex()
            );
        ArrayList<BigDecimal> cidPartThreeValues = evaluatePage
            .getTableValsByRow(
                ColumnIndexEnum.CID_PART_THREE.getColumnIndex()
            );
        ArrayList<BigDecimal> cidPartFourValues = evaluatePage
            .getTableValsByRow(
                ColumnIndexEnum.CID_PART_FOUR.getColumnIndex()
            );

        evaluatePage.switchBackToTabOne();
        ArrayList<BigDecimal> reportsPartOneValues = assemblyDetailsReport
            .getValuesByRow(
                ColumnIndexEnum.CIR_PART_ONE.getColumnIndex()
            );
        ArrayList<BigDecimal> reportsPartTwoValues = assemblyDetailsReport
            .getValuesByRow(
                ColumnIndexEnum.CIR_PART_TWO.getColumnIndex()
            );
        ArrayList<BigDecimal> reportsPartThreeValues = assemblyDetailsReport
            .getValuesByRow(
                ColumnIndexEnum.CIR_PART_THREE.getColumnIndex()
            );
        ArrayList<BigDecimal> reportsPartFourValues = assemblyDetailsReport
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
