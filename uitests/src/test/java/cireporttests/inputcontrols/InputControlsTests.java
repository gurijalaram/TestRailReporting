package cireporttests.inputcontrols;

import com.apriori.pageobjects.reports.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.reports.pages.view.reports.GenericReportPage;
import com.apriori.pageobjects.reports.pages.view.reports.PlasticDtcReportPage;
import com.apriori.utils.enums.PlasticDtcReportsEnum;
import com.apriori.utils.web.driver.TestBase;
import org.openqa.selenium.WebDriver;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class InputControlsTests extends TestBase {

    private GenericReportPage genericReportPage;

    public InputControlsTests() { super(); }

    public void testExportSetFilterUsingInputField(WebDriver driver, String reportName) {
        genericReportPage = new ReportsLoginPage(driver)
                .login("qa-automation-01", "qa-automation-01")
                .navigateToLibraryPage()
                .navigateToReport(reportName, GenericReportPage.class);

        Integer availableExportSetCount = Integer.parseInt(genericReportPage.getCountOfExportSets());

        genericReportPage.setEarliestExportDateToTodayInput()
                .setLatestExportDateToTwoDaysFutureInput()
                .ensureDatesAreCorrect(true, false)
                .waitForCorrectExportSetListCount("0");

        assertThat(Integer.parseInt(genericReportPage.getCountOfExportSets()), is(not(availableExportSetCount)));
    }

    public void testExportSetFilterUsingDatePicker(WebDriver driver, String reportName) {
        genericReportPage = new ReportsLoginPage(driver)
                .login("qa-automation-01", "qa-automation-01")
                .navigateToLibraryPage()
                .navigateToReport(reportName, GenericReportPage.class);

        Integer availableExportSetCount = Integer.parseInt(genericReportPage.getCountOfExportSets());

        genericReportPage.setEarliestExportDateToTodayPicker()
                .setLatestExportDateToTodayPlusTwoPicker()
                .ensureDatesAreCorrect(true, false)
                .waitForCorrectExportSetListCount("0");

        assertThat(Integer.parseInt(genericReportPage.getCountOfExportSets()), is(not(availableExportSetCount)));
    }

}
