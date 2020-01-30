package cireporttests.ootbreports.general.assemblydetails;

import ch.qos.logback.core.net.AbstractSSLSocketAppender;
import com.apriori.pageobjects.reports.pages.view.enums.AssemblyReportsEnum;
import com.apriori.pageobjects.reports.pages.view.ViewSearchResultsPage;
import com.apriori.pageobjects.reports.pages.homepage.HomePage;
import com.apriori.pageobjects.reports.pages.library.LibraryPage;
import com.apriori.pageobjects.reports.pages.view.ViewRepositoryPage;
import com.apriori.pageobjects.reports.pages.login.LoginPage;
import com.apriori.pageobjects.reports.pages.view.enums.AssemblySetEnum;
import com.apriori.pageobjects.reports.pages.view.enums.ExportSetEnum;
import com.apriori.pageobjects.reports.pages.view.reports.AssemblyDetailsReportPage;
import com.apriori.utils.enums.AssemblyTypeEnum;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.web.driver.TestBase;
import com.apriori.utils.users.UserUtil;
import io.qameta.allure.Description;
import com.apriori.utils.TestRail;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class AssemblyDetailsReportTests extends TestBase {

    private AssemblyDetailsReportPage assemblyDetailsReport;
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
        for(AssemblyReportsEnum report : reportNames) {
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
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency());

        usdGrandTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Capital Investments");

        assemblyDetailsReport.clickInputControlsButton()
                .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
                .clickApplyAndOk()
                .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency());

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
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency());

        assemblyDetailsReport.clickInputControlsButton()
                .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
                .clickApplyAndOk()
                .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency());

        gbpGrandTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total", "Capital Investments");
        assertThat(assemblyDetailsReport.getCurrentCurrency(), is(equalTo(CurrencyEnum.GBP.getCurrency())));

        assemblyDetailsReport.clickInputControlsButton()
                .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
                .clickApplyAndOk()
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency());

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
                .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency())
                .waitForCorrectAssembly(assemblyType);

        /*
            The reason for the range check in areValuesAlmostEqual is that there is a rounding bug.
            Initial rounding bug (similar issue, in a different report): https://jira.apriori.com/browse/AP-53537
            Bug for this issue: https://jira.apriori.com/browse/AP-58059
         */

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
                assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total","Cycle Time"),
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
                assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total","Capital Investments"),
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
                .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency())
                .waitForCorrectAssembly(assemblyType);

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
                .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency())
                .waitForCorrectAssembly(assemblyType);

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
                .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency())
                .waitForCorrectAssembly(assemblyType);

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
                .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency())
                .waitForCorrectAssembly(assemblyType);

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
                .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency())
                .waitForCorrectAssembly(assemblyType);

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
    @Description("Ensuring latest export date filter works properly")
    public void testLatestExportDateFilter() {
        assemblyDetailsReport = new LoginPage(driver)
                .login(UserUtil.getUser())
                .navigateToLibraryPage()
                .navigateToReport(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName())
                .waitForInputControlsLoad()
                .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
                .ensureDateIsToday()
                .setExportDateToTwoMonthsAgo()
                .ensureExportSetHasChanged();

        assertThat(assemblyDetailsReport.getAmountOfTopLevelExportSets(), is(0));
    }

}
