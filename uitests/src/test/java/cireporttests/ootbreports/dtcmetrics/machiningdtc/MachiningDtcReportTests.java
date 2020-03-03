package cireporttests.ootbreports.dtcmetrics.machiningdtc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.reports.pages.homepage.HomePage;
import com.apriori.pageobjects.reports.pages.library.LibraryPage;
import com.apriori.pageobjects.reports.pages.login.LoginPage;
import com.apriori.pageobjects.reports.pages.view.ViewRepositoryPage;
import com.apriori.pageobjects.reports.pages.view.ViewSearchResultsPage;
import com.apriori.pageobjects.reports.pages.view.enums.ExportSetEnum;
import com.apriori.pageobjects.reports.pages.view.reports.MachiningDTCReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

import java.math.BigDecimal;

public class MachiningDtcReportTests extends TestBase {

    private MachiningDTCReportPage machiningDTCReportPage;
    private ViewSearchResultsPage searchResults;
    private ViewRepositoryPage repository;
    private LibraryPage library;
    private HomePage homePage;

    private String reportName = "Machining DTC";
    private int reportCount = 3;

    public MachiningDtcReportTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "2024")
    @Description("Verify report availability by navigation")
    public void testReportAvailabilityByNavigation() {
        repository = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToViewRepositoryPage()
            .navigateToMachiningDTCFolder()
            .waitForMachiningDTCReportsToAppear();

        assertThat(repository.getCountOfGeneralReports(), is(equalTo(reportCount)));

        assertThat(reportName, is(equalTo(repository.getReportName(reportName))));
    }

    @Test
    @TestRail(testCaseId = "3415")
    @Description("Verify report availability by library")
    public void testReportAvailabilityByLibrary() {
        library = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToLibraryPage();

        assertThat(reportName, is(equalTo(library.getReportName(reportName))));
    }

    @Test
    @TestRail(testCaseId = "3416")
    @Description("Verify report availability by search")
    public void testReportAvailabilityBySearch() {
        homePage = new LoginPage(driver)
            .login(UserUtil.getUser());

        searchResults = new ViewSearchResultsPage(driver);
        homePage.searchForReport(reportName);

        assertThat(searchResults.getReportName(reportName), is(equalTo(reportName)));
    }

    @Test
    @TestRail(testCaseId = "3026")
    @Description("Verify currency code input control functions correctly")
    public void testCurrencyChange() {
        BigDecimal gbpGrandTotal;
        BigDecimal usdGrandTotal;

        machiningDTCReportPage = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToLibraryPage()
            .navigateToReport(reportName)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName())
            .scrollDownInputControls()
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
            .clickApplyAndOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), MachiningDTCReportPage.class);

        usdGrandTotal = machiningDTCReportPage.getValueFromCentralCircleInChart();

        machiningDTCReportPage.clickInputControlsButton()
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .clickApplyAndOk()
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), MachiningDTCReportPage.class);

        gbpGrandTotal = machiningDTCReportPage.getValueFromCentralCircleInChart();

        assertThat(machiningDTCReportPage.getCurrentCurrency(), is(equalTo(CurrencyEnum.GBP.getCurrency())));
        assertThat(gbpGrandTotal, is(not(usdGrandTotal)));
    }
}
