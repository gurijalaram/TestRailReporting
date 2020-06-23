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
import com.apriori.pageobjects.reports.pages.view.reports.GenericReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.MsSQLOracleLocalInstallTest;
import testsuites.suiteinterface.CIARStagingSmokeTest;

import java.math.BigDecimal;

public class MachiningDtcReportTests extends TestBase {

    private GenericReportPage genericReportPage;
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
    @Category({MsSQLOracleLocalInstallTest.class, CIARStagingSmokeTest.class})
    @TestRail(testCaseId = "2024")
    @Description("Verify report availability by navigation")
    public void testReportAvailabilityByNavigation() {
        repository = new LoginPage(driver)
            .login()
            .navigateToViewRepositoryPage()
            .navigateToMachiningDTCFolder();

        assertThat(reportName, is(equalTo(repository.getReportName(reportName))));
        assertThat(repository.getCountOfGeneralReports(), is(equalTo(reportCount)));
    }

    @Test
    @Category(MsSQLOracleLocalInstallTest.class)
    @TestRail(testCaseId = "3415")
    @Description("Verify report availability by library")
    public void testReportAvailabilityByLibrary() {
        library = new LoginPage(driver)
            .login()
            .navigateToLibraryPage();

        assertThat(reportName, is(equalTo(library.getReportName(reportName))));
    }

    @Test
    @Category(MsSQLOracleLocalInstallTest.class)
    @TestRail(testCaseId = "3416")
    @Description("Verify report availability by search")
    public void testReportAvailabilityBySearch() {
        homePage = new LoginPage(driver)
            .login();

        searchResults = new ViewSearchResultsPage(driver);
        homePage.searchForReport(reportName);

        assertThat(searchResults.getReportName(reportName), is(equalTo(reportName)));
    }

    @Test
    @Category({MsSQLOracleLocalInstallTest.class, CIARStagingSmokeTest.class})
    @TestRail(testCaseId = "3026")
    @Description("Verify currency code input control functions correctly")
    public void testCurrencyChange() {
        BigDecimal gbpGrandTotal;
        BigDecimal usdGrandTotal;

        genericReportPage = new LoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(reportName)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName())
            .scrollDownInputControls()
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), GenericReportPage.class);

        usdGrandTotal = genericReportPage.getFBCValueFromBubbleTooltip(false);

        genericReportPage.clickInputControlsButton()
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), GenericReportPage.class);

        gbpGrandTotal = genericReportPage.getFBCValueFromBubbleTooltip(false);

        assertThat(genericReportPage.getCurrentCurrency(), is(equalTo(CurrencyEnum.GBP.getCurrency())));
        assertThat(gbpGrandTotal, is(not(usdGrandTotal)));
    }

    @Test
    @Category(MsSQLOracleLocalInstallTest.class)
    @TestRail(testCaseId = "3565")
    @Description("Verify that earliest and latest export date fields function correctly using input field")
    public void testBothExportDatesUsingInputField() {
        genericReportPage = new LoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName)
                .waitForInputControlsLoad();

        Integer availableExportSetCount = Integer.parseInt(genericReportPage.getCountOfExportSets());

        genericReportPage.setEarliestExportDateToTodayInput()
                .setLatestExportDateToTwoDaysFutureInput()
                .ensureDatesAreCorrect(true, false)
                .waitForCorrectExportSetListCount("0");

        assertThat(Integer.parseInt(genericReportPage.getCountOfExportSets()), is(not(availableExportSetCount)));
    }

    @Test
    @Category({MsSQLOracleLocalInstallTest.class, CIARStagingSmokeTest.class})
    @TestRail(testCaseId = "3566")
    @Description("Verify that earliest and latest export date fields function correctly using date picker")
    public void testBothExportDatesUsingDatePicker() {
        genericReportPage = new LoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(reportName)
                .waitForInputControlsLoad();

        Integer availableExportSetCount = Integer.parseInt(genericReportPage.getCountOfExportSets());

        genericReportPage.setEarliestExportDateToTodayPicker()
                .setLatestExportDateToTodayPlusTwoPicker()
                .ensureDatesAreCorrect(true, false)
                .waitForCorrectExportSetListCount("0");

        assertThat(Integer.parseInt(genericReportPage.getCountOfExportSets()), is(not(availableExportSetCount)));
    }
}
