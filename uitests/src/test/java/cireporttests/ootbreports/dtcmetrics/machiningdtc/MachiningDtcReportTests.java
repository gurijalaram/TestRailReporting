package cireporttests.ootbreports.dtcmetrics.machiningdtc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.reports.pages.homepage.ReportsHomePage;
import com.apriori.pageobjects.reports.pages.library.LibraryPage;
import com.apriori.pageobjects.reports.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.reports.pages.view.ViewRepositoryPage;
import com.apriori.pageobjects.reports.pages.view.ViewSearchResultsPage;
import com.apriori.pageobjects.reports.pages.view.reports.GenericReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.MachiningReportsEnum;
import com.apriori.utils.web.driver.TestBase;

import cireporttests.inputcontrols.InputControlsTests;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.MsSQLOracleLocalInstallTest;
import testsuites.suiteinterface.CIARStagingSmokeTest;

import java.math.BigDecimal;

public class MachiningDtcReportTests extends TestBase {

    private InputControlsTests inputControlsTests;
    private GenericReportPage genericReportPage;
    private ViewSearchResultsPage searchResults;
    private ViewRepositoryPage repository;
    private LibraryPage library;
    private ReportsHomePage homePage;

    private int reportCount = 3;

    public MachiningDtcReportTests() {
        super();
    }

    @Test
    @Category({MsSQLOracleLocalInstallTest.class, CIARStagingSmokeTest.class})
    @TestRail(testCaseId = "2024")
    @Description("Verify report availability by navigation")
    public void testReportAvailabilityByNavigation() {
        repository = new ReportsLoginPage(driver)
            .login()
            .navigateToViewRepositoryPage()
            .navigateToMachiningDTCFolder();

        assertThat(MachiningReportsEnum.MACHINING_DTC.getReportName(),
                is(equalTo(repository.getReportName(MachiningReportsEnum.MACHINING_DTC.getReportName()))));
        assertThat(repository.getCountOfGeneralReports(), is(equalTo(reportCount)));
    }

    @Test
    @Category(MsSQLOracleLocalInstallTest.class)
    @TestRail(testCaseId = "3415")
    @Description("Verify report availability by library")
    public void testReportAvailabilityByLibrary() {
        library = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage();

        assertThat(MachiningReportsEnum.MACHINING_DTC.getReportName(), is(equalTo(library.getReportName(MachiningReportsEnum.MACHINING_DTC.getReportName()))));
    }

    @Test
    @Category(MsSQLOracleLocalInstallTest.class)
    @TestRail(testCaseId = "3416")
    @Description("Verify report availability by search")
    public void testReportAvailabilityBySearch() {
        homePage = new ReportsLoginPage(driver)
            .login();

        searchResults = new ViewSearchResultsPage(driver);
        homePage.searchForReport(MachiningReportsEnum.MACHINING_DTC.getReportName());

        assertThat(searchResults.getReportName(MachiningReportsEnum.MACHINING_DTC.getReportName()),
                is(equalTo(MachiningReportsEnum.MACHINING_DTC.getReportName())));
    }

    @Test
    @Category({MsSQLOracleLocalInstallTest.class, CIARStagingSmokeTest.class})
    @TestRail(testCaseId = "3026")
    @Description("Verify currency code input control functions correctly")
    public void testCurrencyChange() {
        BigDecimal gbpGrandTotal;
        BigDecimal usdGrandTotal;

        genericReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(MachiningReportsEnum.MACHINING_DTC.getReportName(), GenericReportPage.class)
            .waitForInputControlsLoad()
            .selectExportSet(ExportSetEnum.MACHINING_DTC_DATASET.getExportSetName())
            .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), GenericReportPage.class);

        genericReportPage.setReportName(MachiningReportsEnum.MACHINING_DTC.getReportName());
        genericReportPage.hoverPartNameBubbleDtcReports();
        usdGrandTotal = genericReportPage.getFBCValueFromBubbleTooltip();

        genericReportPage.clickInputControlsButton()
            .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
            .clickOk()
            .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), GenericReportPage.class);

        genericReportPage.setReportName(MachiningReportsEnum.MACHINING_DTC.getReportName());
        genericReportPage.hoverPartNameBubbleDtcReports();
        gbpGrandTotal = genericReportPage.getFBCValueFromBubbleTooltip();

        assertThat(genericReportPage.getCurrentCurrency(), is(equalTo(CurrencyEnum.GBP.getCurrency())));
        assertThat(gbpGrandTotal, is(not(usdGrandTotal)));
    }

    @Test
    @Category(MsSQLOracleLocalInstallTest.class)
    @TestRail(testCaseId = "3565")
    @Description("Verify that earliest and latest export date fields function correctly using input field")
    public void testBothExportDatesUsingInputField() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingInputField(MachiningReportsEnum.MACHINING_DTC.getReportName());
    }

    @Test
    @Category({MsSQLOracleLocalInstallTest.class, CIARStagingSmokeTest.class})
    @TestRail(testCaseId = "3566")
    @Description("Verify that earliest and latest export date fields function correctly using date picker")
    public void testBothExportDatesUsingDatePicker() {
        inputControlsTests = new InputControlsTests(driver);
        inputControlsTests.testExportSetFilterUsingDatePicker(MachiningReportsEnum.MACHINING_DTC.getReportName());
    }
}
