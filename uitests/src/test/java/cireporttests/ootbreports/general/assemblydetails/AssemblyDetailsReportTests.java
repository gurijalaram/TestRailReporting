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
import com.apriori.utils.constants.Constants;
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
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

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
                .waitForPageLoad()
                .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
                .scrollDownInputControls()
                .checkCurrencySelected(Constants.usdCurrencyCode)
                .clickApplyAndOk()
                .waitForCorrectCurrency(Constants.usdCurrencyCode);

        usdGrandTotal = assemblyDetailsReport.getValueFromTable(
                assemblyType,
                "Grand Total Sub Assembly",
                "Capital Investments Total"
        );

        assemblyDetailsReport.clickOptionsButton()
                .checkCurrencySelected(Constants.gbpCurrencyCode)
                .clickApplyAndOk()
                .waitForCorrectCurrency(Constants.gbpCurrencyCode);

        gbpGrandTotal = assemblyDetailsReport.getValueFromTable(
                assemblyType,
                "Grand Total Sub Assembly",
                "Capital Investments Total"
        );

        assertThat(assemblyDetailsReport.getCurrentCurrency(), is(equalTo(Constants.gbpCurrencyCode)));
        assertThat(gbpGrandTotal, is(not(usdGrandTotal)));
        assertThat(gbpGrandTotal, is(lessThan(usdGrandTotal)));
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
                .waitForPageLoad()
                .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
                .scrollDownInputControls()
                .checkCurrencySelected(Constants.usdCurrencyCode)
                .clickApplyAndOk()
                .waitForCorrectCurrency(Constants.usdCurrencyCode);

        assemblyDetailsReport.clickOptionsButton()
                .checkCurrencySelected(Constants.gbpCurrencyCode)
                .clickApplyAndOk()
                .waitForCorrectCurrency(Constants.gbpCurrencyCode);

        gbpGrandTotal = assemblyDetailsReport.getValueFromTable(
                assemblyType,
                "Grand Total Sub Assembly",
                "Capital Investments Total"
        );
        assertThat(assemblyDetailsReport.getCurrentCurrency(), is(equalTo(Constants.gbpCurrencyCode)));

        assemblyDetailsReport.clickOptionsButton()
                .checkCurrencySelected(Constants.usdCurrencyCode)
                .clickApplyAndOk()
                .waitForCorrectCurrency(Constants.usdCurrencyCode);

        usdGrandTotal = assemblyDetailsReport.getValueFromTable(
                assemblyType,
                "Grand Total Sub Assembly",
                "Capital Investments Total"
        );

        assertThat(assemblyDetailsReport.getCurrentCurrency(), is(equalTo(Constants.usdCurrencyCode)));
        assertThat(usdGrandTotal, is(not(equalTo(gbpGrandTotal))));
        assertThat(usdGrandTotal, is(greaterThan(gbpGrandTotal)));
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
                .waitForPageLoad()
                .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
                .scrollDownInputControls()
                .setAssembly(AssemblySetEnum.SUB_ASSEMBLY.getAssemblySetName())
                .checkCurrencySelected(Constants.gbpCurrencyCode)
                .clickApplyAndOk()
                .waitForCorrectCurrency(Constants.gbpCurrencyCode);

        // check that component subtotal + assembly processes is correct for each column
        // new test for each setting of the three

        assertThat(assemblyDetailsReport.getExpectedCTGrandTotal(assemblyType,"Cycle Time")
                        .subtract(new BigDecimal("0.15")),
                is(equalTo(assemblyDetailsReport.getValueFromTable(
                        assemblyType, "Grand Total Sub Assembly", "Cycle Time Total"))));

        assertThat(assemblyDetailsReport.getExpectedPPCGrandTotal(assemblyType, "Piece Part Cost")
                        .add(new BigDecimal("0.01")),
                is(equalTo(assemblyDetailsReport.getValueFromTable(
                        assemblyType, "Grand Total Sub Assembly", "Piece Part Cost Total"))));

        assertThat(assemblyDetailsReport.getExpectedFBCGrandTotal(assemblyType, "Fully Burdened Cost")
                        .add(new BigDecimal("0.01")),
                is(equalTo(assemblyDetailsReport.getValueFromTable(
                        assemblyType, "Grand Total Sub Assembly", "Fully Burdened Cost Total"))));

        assertThat(assemblyDetailsReport.getExpectedCIGrandTotal(assemblyType, "Capital Investments"),
                is(equalTo(assemblyDetailsReport.getValueFromTable(
                        assemblyType, "Grand Total Sub Assembly", "Capital Investments Total"))));
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
                .waitForPageLoad()
                .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
                .scrollDownInputControls()
                .setAssembly(AssemblySetEnum.SUB_SUB_ASM.getAssemblySetName())
                .checkCurrencySelected(Constants.gbpCurrencyCode)
                .clickApplyAndOk()
                .waitForCorrectCurrency(Constants.gbpCurrencyCode);

        assertThat(assemblyDetailsReport.getExpectedCTGrandTotal(assemblyType,"Cycle Time"),
                is(equalTo(assemblyDetailsReport.getValueFromTable(
                        assemblyType, "Grand Total Sub Sub ASM", "Cycle Time Total"))));

        assertThat(assemblyDetailsReport.getExpectedPPCGrandTotal(assemblyType,"Piece Part Cost"),
                is(equalTo(assemblyDetailsReport.getValueFromTable(
                        assemblyType,"Grand Total Sub Sub ASM", "Piece Part Cost Total"))));

        assertThat(assemblyDetailsReport.getExpectedFBCGrandTotal(assemblyType,"Fully Burdened Cost"),
                is(equalTo(assemblyDetailsReport.getValueFromTable(
                        assemblyType, "Grand Total Sub Sub ASM", "Fully Burdened Cost Total"))));

        assertThat(assemblyDetailsReport.getExpectedCIGrandTotal(assemblyType,"Capital Investments"),
                is(equalTo(assemblyDetailsReport.getValueFromTable(
                        assemblyType,"Grand Total Sub Sub ASM", "Capital Investments Total"))));
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
                .waitForPageLoad()
                .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
                .scrollDownInputControls()
                .setAssembly(AssemblySetEnum.TOP_LEVEL.getAssemblySetName())
                .checkCurrencySelected(Constants.usdCurrencyCode)
                .clickApplyAndOk()
                .waitForCorrectCurrency(Constants.usdCurrencyCode);

        assertThat(assemblyDetailsReport.getExpectedCTGrandTotal(assemblyType,"Cycle Time"),
                is(equalTo(assemblyDetailsReport.getValueFromTable(
                        assemblyType, "Grand Total Top Level", "Cycle Time Total"))));

        assertThat(assemblyDetailsReport.getExpectedPPCGrandTotal(assemblyType,"Piece Part Cost"),
                is(equalTo(assemblyDetailsReport.getValueFromTable(
                        assemblyType, "Grand Total Top Level", "Piece Part Cost Total"))));

        assertThat(assemblyDetailsReport.getExpectedFBCGrandTotal(assemblyType,"Fully Burdened Cost"),
                is(equalTo(assemblyDetailsReport.getValueFromTable(
                        assemblyType,"Grand Total Top Level", "Fully Burdened Cost Total"))));

        assertThat(assemblyDetailsReport.getExpectedCIGrandTotal(assemblyType,"Capital Investments"),
                is(equalTo(assemblyDetailsReport.getValueFromTable(
                        assemblyType, "Grand Total Top Level", "Capital Investments Total"))));
    }
}
