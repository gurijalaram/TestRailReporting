package navigation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.web.driver.TestBase;

import org.openqa.selenium.WebDriver;
import pageobjects.header.ReportsPageHeader;
import pageobjects.pages.library.LibraryPage;
import pageobjects.pages.login.ReportsLoginPage;
import pageobjects.pages.view.ViewSearchResultsPage;
import pageobjects.pages.view.reports.GenericReportPage;

public class ReportAvailabilityTests extends TestBase {

    private ViewSearchResultsPage viewSearchResultsPage;
    private GenericReportPage genericReportPage;
    private ReportsPageHeader reportsPageHeader;
    private LibraryPage libraryPage;
    private WebDriver driver;

    public ReportAvailabilityTests(WebDriver driver) {
        super();
        this.driver = driver;
    }

    /**
     * Generic test for report availability by navigation
     * @param reportName - String
     */
    public void testReportAvailabilityByNavigation(String firstFolder, String reportName) {
        genericReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToViewRepositoryPage()
                .navigateToReportFolder(firstFolder, reportName);

        assertThat(reportName, is(equalTo(genericReportPage.getReportName(reportName))));
    }

    /**
     * Generic test for report availability by library
     * @param reportName - String
     */
    public void testReportAvailabilityByLibrary(String reportName) {
        libraryPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage();

        assertThat(reportName, is(equalTo(libraryPage.getReportName(reportName))));
    }

    /**
     * Generic test for report availability by search
     * @param reportName - String
     */
    public void testReportAvailabilityBySearch(String reportName) {
        reportsPageHeader = new ReportsLoginPage(driver)
                .login();

        viewSearchResultsPage = reportsPageHeader.searchForReport(reportName);

        assertThat(viewSearchResultsPage.getReportName(reportName),
                is(equalTo(reportName))
        );
    }
}
