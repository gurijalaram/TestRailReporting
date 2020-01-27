package cireporttests.ootbreports.general.assemblydetails;

import com.apriori.pageobjects.reports.pages.view.enums.AssemblyReportsEnum;
import com.apriori.pageobjects.reports.pages.view.ViewSearchResultsPage;
import com.apriori.pageobjects.reports.pages.homepage.HomePage;
import com.apriori.pageobjects.reports.pages.library.LibraryPage;
import com.apriori.pageobjects.reports.pages.view.ViewRepositoryPage;
import com.apriori.pageobjects.reports.pages.login.LoginPage;
import com.apriori.pageobjects.reports.pages.view.enums.AssemblySetEnum;
import com.apriori.pageobjects.reports.pages.view.enums.ExportSetEnum;
import com.apriori.pageobjects.reports.pages.view.reports.AssemblyDetailsReportPage;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.web.driver.TestBase;
import com.apriori.utils.users.UserUtil;
import io.qameta.allure.Description;
import com.apriori.utils.TestRail;
import org.junit.Test;

import java.math.BigDecimal;

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
        assemblyType = "Sub-Assembly";
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

        usdGrandTotal = assemblyDetailsReport.getValueFromTable(
                assemblyType,
                "Grand Total Sub Assembly",
                "Capital Investments Total"
        );

        assemblyDetailsReport.clickInputControlsButton()
                .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
                .clickApplyAndOk()
                .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency());

        gbpGrandTotal = assemblyDetailsReport.getValueFromTable(
                assemblyType,
                "Grand Total Sub Assembly",
                "Capital Investments Total"
        );

        assertThat(assemblyDetailsReport.getCurrentCurrency(), is(equalTo(CurrencyEnum.GBP.getCurrency())));
        assertThat(gbpGrandTotal, is(not(usdGrandTotal)));
    }

    @Test
    @TestRail(testCaseId = "3205")
    @Description("Verifies that currency change and then reversion works")
    public void testCurrencyCodeReversion() {
        assemblyType = "Sub-Assembly";
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

        gbpGrandTotal = assemblyDetailsReport.getValueFromTable(
                assemblyType,
                "Grand Total Sub Assembly",
                "Capital Investments Total"
        );
        assertThat(assemblyDetailsReport.getCurrentCurrency(), is(equalTo(CurrencyEnum.GBP.getCurrency())));

        assemblyDetailsReport.clickInputControlsButton()
                .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
                .clickApplyAndOk()
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency());

        usdGrandTotal = assemblyDetailsReport.getValueFromTable(
                assemblyType,
                "Grand Total Sub Assembly",
                "Capital Investments Total"
        );

        assertThat(assemblyDetailsReport.getCurrentCurrency(), is(equalTo(CurrencyEnum.USD.getCurrency())));
        assertThat(usdGrandTotal, is(not(equalTo(gbpGrandTotal))));
    }

    @Test
    @TestRail(testCaseId = "3067")
    @Description("Verify totals calculations for Sub Assembly")
    public void testTotalCalculationsForSubAssembly() {
        assemblyType = "Sub-Assembly";

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
                assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total Sub Assembly", "Cycle Time Total"),
                assemblyDetailsReport.getExpectedCTGrandTotal(assemblyType, "Cycle Time")
        ), is(true));

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
                assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total Sub Assembly", "Piece Part Cost Total"),
                assemblyDetailsReport.getExpectedPPCGrandTotal(assemblyType, "Piece Part Cost")
        ), is(true));

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
                assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total Sub Assembly", "Fully Burdened Cost Total"),
                assemblyDetailsReport.getExpectedFBCGrandTotal(assemblyType, "Fully Burdened Cost")
        ), is(true));

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
                assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total Sub Assembly", "Capital Investments Total"),
                assemblyDetailsReport.getExpectedCIGrandTotal(assemblyType, "Capital Investments")
        ), is(true));
    }

    @Test
    @TestRail(testCaseId = "3068")
    @Description("Verify totals calculations for Sub-Sub-ASM")
    public void testTotalCalculationsForSubSubASM() {
        assemblyType = "Sub-Sub-ASM";

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
                assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total Sub Sub ASM", "Cycle Time Total"),
                assemblyDetailsReport.getExpectedCTGrandTotal(assemblyType, "Cycle Time")
        ), is(true));

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
                assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total Sub Sub ASM", "Piece Part Cost Total"),
                assemblyDetailsReport.getExpectedPPCGrandTotal(assemblyType, "Piece Part Cost")
        ), is(true));

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
                assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total Sub Sub ASM", "Fully Burdened Cost Total"),
                assemblyDetailsReport.getExpectedFBCGrandTotal(assemblyType, "Fully Burdened Cost")
        ), is(true));

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
                assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total Sub Sub ASM", "Capital Investments Total"),
                assemblyDetailsReport.getExpectedCIGrandTotal(assemblyType, "Capital Investments")
        ), is(true));
    }

    @Test
    @TestRail(testCaseId = "1934")
    @Description("Verify totals calculations for Top Level")
    public void testTotalCalculationsForTopLevel() {
        assemblyType = "Top Level";

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
                assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total Top Level", "Cycle Time Total"),
                assemblyDetailsReport.getExpectedCTGrandTotal(assemblyType, "Cycle Time")
        ), is(true));

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
                assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total Top Level", "Piece Part Cost Total"),
                assemblyDetailsReport.getExpectedPPCGrandTotal(assemblyType, "Piece Part Cost")
        ), is(true));

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
                assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total Top Level", "Fully Burdened Cost Total"),
                assemblyDetailsReport.getExpectedFBCGrandTotal(assemblyType, "Fully Burdened Cost")
        ), is(true));

        assertThat(assemblyDetailsReport.areValuesAlmostEqual(
                assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total Top Level", "Capital Investments Total"),
                assemblyDetailsReport.getExpectedCIGrandTotal(assemblyType, "Capital Investments")
        ), is(true));
    }

    @Test
    @TestRail(testCaseId = "3231")
    @Description("Verify sub total calculations for Sub Assembly")
    public void testSubTotalCalculationsSubAssembly() {
        assemblyType = "Sub-Assembly";

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

        BigDecimal ctComponentSubTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Component Subtotal Sub Assembly", "Cycle Time Sub Total");
        BigDecimal ctAssemblyProcesses = assemblyDetailsReport.getValueFromTable(assemblyType, "Assembly Processes Sub Assembly", "Cycle Time Total");
        BigDecimal ctExpectedTotal = ctComponentSubTotal.add(ctAssemblyProcesses);
        BigDecimal ctActualTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total Sub Assembly", "Cycle Time Total");
        assertThat(ctExpectedTotal, is(equalTo(ctActualTotal)));

        BigDecimal ppcComponentSubTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Component Subtotal Sub Assembly", "Piece Part Cost Sub Total");
        BigDecimal ppcAssemblyProcesses = assemblyDetailsReport.getValueFromTable(assemblyType, "Assembly Processes Sub Assembly", "Piece Part Cost Total");
        BigDecimal ppcExpectedTotal = ppcComponentSubTotal.add(ppcAssemblyProcesses);
        BigDecimal ppcActualTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total Sub Assembly", "Piece Part Cost Total");
        assertThat(
                assemblyDetailsReport.areValuesAlmostEqual(ppcExpectedTotal, ppcActualTotal),
                is(true)
        );

        BigDecimal fbcComponentSubTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Component Subtotal Sub Assembly", "Fully Burdened Cost Sub Total");
        BigDecimal fbcAssemblyProcesses = assemblyDetailsReport.getValueFromTable(assemblyType, "Assembly Processes Sub Assembly", "Fully Burdened Cost Total");
        BigDecimal fbcExpectedTotal = fbcComponentSubTotal.add(fbcAssemblyProcesses);
        BigDecimal fbcActualTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total Sub Assembly", "Fully Burdened Cost Total");
        assertThat(fbcExpectedTotal, is(equalTo(fbcActualTotal)));

        BigDecimal ciComponentSubTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Component Subtotal Sub Assembly", "Capital Investments Sub Total");
        BigDecimal ciAssemblyProcesses = assemblyDetailsReport.getValueFromTable(assemblyType, "Assembly Processes Sub Assembly", "Capital Investments Total");
        BigDecimal ciExpectedTotal = ciComponentSubTotal.add(ciAssemblyProcesses);
        BigDecimal ciActualTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total Sub Assembly", "Capital Investments Total");
        assertThat(ciExpectedTotal, is(equalTo(ciActualTotal)));
    }

    @Test
    @TestRail(testCaseId = "3232")
    @Description("Verify sub total calculations for Sub Sub ASM")
    public void testSubTotalCalculationsSubSubAsm() {
        assemblyType = "Sub-Sub-ASM";

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

        BigDecimal ctComponentSubTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Component Subtotal Sub Sub ASM", "Cycle Time Sub Total");
        BigDecimal ctAssemblyProcesses = assemblyDetailsReport.getValueFromTable(assemblyType, "Assembly Processes Sub Sub ASM", "Cycle Time Total");
        BigDecimal ctExpectedTotal = ctComponentSubTotal.add(ctAssemblyProcesses);
        BigDecimal ctActualTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total Sub Sub ASM", "Cycle Time Total");
        assertThat(ctExpectedTotal, is(equalTo(ctActualTotal)));

        BigDecimal ppcComponentSubTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Component Subtotal Sub Sub ASM", "Piece Part Cost Sub Total");
        BigDecimal ppcAssemblyProcesses = assemblyDetailsReport.getValueFromTable(assemblyType, "Assembly Processes Sub Sub ASM", "Piece Part Cost Total");
        BigDecimal ppcExpectedTotal = ppcComponentSubTotal.add(ppcAssemblyProcesses);
        BigDecimal ppcActualTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total Sub Sub ASM", "Piece Part Cost Total");
        assertThat(
                assemblyDetailsReport.areValuesAlmostEqual(ppcExpectedTotal, ppcActualTotal),
                is(true)
        );

        BigDecimal fbcComponentSubTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Component Subtotal Sub Sub ASM", "Fully Burdened Cost Sub Total");
        BigDecimal fbcAssemblyProcesses = assemblyDetailsReport.getValueFromTable(assemblyType, "Assembly Processes Sub Sub ASM", "Fully Burdened Cost Total");
        BigDecimal fbcExpectedTotal = fbcComponentSubTotal.add(fbcAssemblyProcesses);
        BigDecimal fbcActualTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total Sub Sub ASM", "Fully Burdened Cost Total");
        assertThat(fbcExpectedTotal, is(equalTo(fbcActualTotal)));

        BigDecimal ciComponentSubTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Component Subtotal Sub Sub ASM", "Capital Investments Sub Total");
        BigDecimal ciAssemblyProcesses = assemblyDetailsReport.getValueFromTable(assemblyType, "Assembly Processes Sub Sub ASM", "Capital Investments Total");
        BigDecimal ciExpectedTotal = ciComponentSubTotal.add(ciAssemblyProcesses);
        BigDecimal ciActualTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total Sub Sub ASM", "Capital Investments Total");
        assertThat(ciExpectedTotal, is(equalTo(ciActualTotal)));
    }

    @Test
    @TestRail(testCaseId = "3233")
    @Description("Verify sub total calculations for Top Level")
    public void testSubTotalCalculationsTopLevel() {
        assemblyType = "Top Level";

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

        BigDecimal ctComponentSubTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Component Subtotal Top Level", "Cycle Time Sub Total");
        BigDecimal ctAssemblyProcesses = assemblyDetailsReport.getValueFromTable(assemblyType, "Assembly Processes Top Level", "Cycle Time Total");
        BigDecimal ctExpectedTotal = ctComponentSubTotal.add(ctAssemblyProcesses);
        BigDecimal ctActualTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total Top Level", "Cycle Time Total");
        assertThat(ctExpectedTotal, is(equalTo(ctActualTotal)));

        BigDecimal ppcComponentSubTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Component Subtotal Top Level", "Piece Part Cost Sub Total");
        BigDecimal ppcAssemblyProcesses = assemblyDetailsReport.getValueFromTable(assemblyType, "Assembly Processes Top Level", "Piece Part Cost Total");
        BigDecimal ppcExpectedTotal = ppcComponentSubTotal.add(ppcAssemblyProcesses);
        BigDecimal ppcActualTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total Top Level", "Piece Part Cost Total");
        assertThat(
                assemblyDetailsReport.areValuesAlmostEqual(ppcExpectedTotal, ppcActualTotal),
                is(true)
        );

        BigDecimal fbcComponentSubTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Component Subtotal Top Level", "Fully Burdened Cost Sub Total");
        BigDecimal fbcAssemblyProcesses = assemblyDetailsReport.getValueFromTable(assemblyType, "Assembly Processes Top Level", "Fully Burdened Cost Total");
        BigDecimal fbcExpectedTotal = fbcComponentSubTotal.add(fbcAssemblyProcesses);
        BigDecimal fbcActualTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total Top Level", "Fully Burdened Cost Total");
        assertThat(fbcExpectedTotal, is(equalTo(fbcActualTotal)));

        BigDecimal ciComponentSubTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Component Subtotal Top Level", "Capital Investments Sub Total");
        BigDecimal ciAssemblyProcesses = assemblyDetailsReport.getValueFromTable(assemblyType, "Assembly Processes Top Level", "Capital Investments Total");
        BigDecimal ciExpectedTotal = ciComponentSubTotal.add(ciAssemblyProcesses);
        BigDecimal ciActualTotal = assemblyDetailsReport.getValueFromTable(assemblyType, "Grand Total Top Level", "Capital Investments Total");
        assertThat(ciExpectedTotal, is(equalTo(ciActualTotal)));
    }
}
