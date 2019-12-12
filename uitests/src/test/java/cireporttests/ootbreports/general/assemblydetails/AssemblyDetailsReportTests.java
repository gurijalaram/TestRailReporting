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
import static org.hamcrest.Matchers.lessThan;

public class AssemblyDetailsReportTests extends TestBase {

    private AssemblyDetailsReportPage assemblyDetailsReport;
    private ViewSearchResultsPage searchResults;
    private ViewRepositoryPage repository;
    private HomePage homePage;
    private LibraryPage library;

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

        assertThat(repository.getCountOfGeneralReports(), is(equalTo("5")));

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
    @Description("Currency Code works")
    public void testCurrencyCodeWorks() {
        BigDecimal gbpGrandTotal;
        BigDecimal usdGrandTotal;

        assemblyDetailsReport = new LoginPage(driver)
                .login(UserUtil.getUser())
                .navigateToLibraryPage()
                .navigateToReport(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName())
                .waitForPageLoad()
                .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
                .scrollDownInputControls()
                .checkCurrencySelected("USD")
                .clickApplyAndOk()
                .waitForCorrectCurrency("USD");

        usdGrandTotal = assemblyDetailsReport.getTableCellText("28", "34");
        assemblyDetailsReport.clickOptionsButton()
                .checkCurrencySelected("GBP")
                .clickApplyAndOk()
                .waitForCorrectCurrency("GBP");

        gbpGrandTotal = assemblyDetailsReport.getTableCellText("28", "34");
        assertThat(assemblyDetailsReport.getCurrentCurrency(), is(equalTo("GBP")));
        assertThat(gbpGrandTotal, is(not(usdGrandTotal)));
        assertThat(gbpGrandTotal, is(lessThan(usdGrandTotal)));
    }

    @Test
    @TestRail(testCaseId = "3067")
    @Description("Verify totals calculations for Sub Assembly")
    public void testTotalCalculationsForSubAssembly() {
        assemblyDetailsReport = new LoginPage(driver)
                .login(UserUtil.getUser())
                .navigateToLibraryPage()
                .navigateToReport(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName())
                .waitForPageLoad()
                .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
                .scrollDownInputControls()
                .setAssembly(AssemblySetEnum.SUB_ASSEMBLY.getAssemblySetName())
                .checkCurrencySelected("GBP")
                .clickApplyAndOk()
                .waitForCorrectCurrency("GBP");

        assertThat(assemblyDetailsReport.getExpectedColumnGrandTotal("Cycle Time (s)"),
                is(equalTo(assemblyDetailsReport.getActualColumnGrandTotal("Cycle Time"))));

        assertThat(assemblyDetailsReport.getExpectedColumnGrandTotal("Piece Part Cost")
                        .add(new BigDecimal("5.72")),
                is(equalTo(assemblyDetailsReport.getActualColumnGrandTotal("Piece Part Cost"))));

        assertThat(assemblyDetailsReport.getExpectedColumnGrandTotal("Fully Burdened Cost")
                        .add(new BigDecimal("5.72")),
                is(equalTo(assemblyDetailsReport.getActualColumnGrandTotal("Fully Burdened Cost"))));

        assertThat(assemblyDetailsReport.getExpectedColumnGrandTotal("Capital Investments"),
                is(equalTo(assemblyDetailsReport.getActualColumnGrandTotal("Capital Investments"))));
    }

    @Test
    @TestRail(testCaseId = "3068")
    @Description("Verify totals calculations for Sub-Sub-ASM")
    public void testTotalCalculationsForSubSubASM() {
        assemblyDetailsReport = new LoginPage(driver)
                .login(UserUtil.getUser())
                .navigateToLibraryPage()
                .navigateToReport(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName())
                .waitForPageLoad()
                .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
                .scrollDownInputControls()
                .setAssembly(AssemblySetEnum.SUB_SUB_ASM.getAssemblySetName())
                .checkCurrencySelected("USD")
                .clickApplyAndOk()
                .waitForCorrectCurrency("USD");

        assertThat(assemblyDetailsReport.getExpectedColumnGrandTotal("Cycle Time (s)"),
                is(equalTo(assemblyDetailsReport.getActualColumnGrandTotal("Cycle Time (s)"))));

        assertThat(assemblyDetailsReport.getExpectedColumnGrandTotal("Piece Part Cost"),
                is(equalTo(assemblyDetailsReport.getActualColumnGrandTotal("Piece Part Cost"))));

        assertThat(assemblyDetailsReport.getExpectedColumnGrandTotal("Fully Burdened Cost"),
                is(equalTo(assemblyDetailsReport.getActualColumnGrandTotal("Fully Burdened Cost"))));

        assertThat(assemblyDetailsReport.getExpectedColumnGrandTotal("Capital Investments"),
                is(equalTo(assemblyDetailsReport.getActualColumnGrandTotal("Capital Investments"))));
    }

    @Test
    @TestRail(testCaseId = "1934")
    @Description("Verify totals calculations for Top Level")
    public void testTotalCalculationsForTopLevel() {
        assemblyDetailsReport = new LoginPage(driver)
                .login(UserUtil.getUser())
                .navigateToLibraryPage()
                .navigateToReport(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName())
                .waitForPageLoad()
                .selectExportSet(ExportSetEnum.TOP_LEVEL.getExportSetName())
                .scrollDownInputControls()
                .setAssembly(AssemblySetEnum.TOP_LEVEL.getAssemblySetName())
                .checkCurrencySelected("USD")
                .clickApplyAndOk()
                .waitForCorrectCurrency("USD");

        assertThat(assemblyDetailsReport.getExpectedColumnGrandTotal("Cycle Time (s)"),
                is(equalTo(assemblyDetailsReport.getActualColumnGrandTotal("Cycle Time (s)"))));

        assertThat(assemblyDetailsReport.getExpectedColumnGrandTotal("Piece Part Cost"),
                is(equalTo(assemblyDetailsReport.getActualColumnGrandTotal("Piece Part Cost"))));

        assertThat(assemblyDetailsReport.getExpectedColumnGrandTotal("Fully Burdened Cost"),
                is(equalTo(assemblyDetailsReport.getActualColumnGrandTotal("Fully Burdened Cost"))));

        assertThat(assemblyDetailsReport.getExpectedColumnGrandTotal("Capital Investments"),
                is(equalTo(assemblyDetailsReport.getActualColumnGrandTotal("Capital Investments"))));
    }
}
