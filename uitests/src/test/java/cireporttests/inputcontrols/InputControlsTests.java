package cireporttests.inputcontrols;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.reports.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.reports.pages.view.reports.GenericReportPage;
import com.apriori.utils.web.driver.TestBase;

import org.openqa.selenium.WebDriver;

public class InputControlsTests extends TestBase {

    private GenericReportPage genericReportPage;
    private WebDriver driver;

    public InputControlsTests(WebDriver driver) {
        super();
        this.driver = driver;
    }

    /**
     * Generic export set filter using input field test
     * @param reportName String report to use
     */
    public void testExportSetFilterUsingInputField(String reportName) {
        genericReportPage = new ReportsLoginPage(driver)
                .login("qa-automation-01", "qa-automation-01")
                .navigateToLibraryPage()
                .navigateToReport(reportName, GenericReportPage.class);

        Integer availableExportSetCount = Integer.parseInt(genericReportPage.getCountOfExportSets());

        genericReportPage.setExportDateUsingInput(true)
                .setExportDateUsingInput(false)
                .ensureDatesAreCorrect(reportName)
                .waitForCorrectExportSetListCount("0");

        assertThat(Integer.parseInt(genericReportPage.getCountOfExportSets()), is(not(availableExportSetCount)));
        assertThat(Integer.parseInt(genericReportPage.getCountOfExportSets()), is(equalTo(0)));
    }

    /**
     * Generic export set filter using date picker test
     * @param reportName String report to use
     */
    public void testExportSetFilterUsingDatePicker(String reportName) {
        genericReportPage = new ReportsLoginPage(driver)
                .login("qa-automation-01", "qa-automation-01")
                .navigateToLibraryPage()
                .navigateToReport(reportName, GenericReportPage.class);

        Integer availableExportSetCount = Integer.parseInt(genericReportPage.getCountOfExportSets());

        genericReportPage.setExportDateUsingPicker(true)
                .setExportDateUsingPicker(false)
                .ensureDatesAreCorrect(reportName)
                .waitForCorrectExportSetListCount("0");

        assertThat(Integer.parseInt(genericReportPage.getCountOfExportSets()), is(not(availableExportSetCount)));
        assertThat(Integer.parseInt(genericReportPage.getCountOfExportSets()), is(equalTo(0)));
    }

}
