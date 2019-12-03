package cireporttests.ootbreports.general.assemblydetails;

import com.apriori.pageobjects.reports.pages.view.AssemblyReports;
import com.apriori.pageobjects.reports.pages.view.SearchResults;
import com.apriori.pageobjects.reports.pages.homepage.HomePage;
import com.apriori.pageobjects.reports.pages.library.Library;
import com.apriori.pageobjects.reports.pages.view.Repository;
import com.apriori.pageobjects.reports.pages.login.LoginPage;
import com.apriori.pageobjects.reports.pages.view.reports.AssemblyDetailsReport;
import com.apriori.pageobjects.reports.pages.view.reports.InputControls;
import com.apriori.utils.web.driver.TestBase;
import com.apriori.utils.users.UserUtil;
import io.qameta.allure.Description;
import com.apriori.utils.TestRail;
import org.junit.Test;
import sun.rmi.runtime.Log;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class AssemblyDetailsReportTests extends TestBase {

    private AssemblyDetailsReport assemblyDetailsReport;
    private InputControls inputControls;
    private SearchResults searchResults;
    private Repository repository;
    private HomePage homePage;
    private Library library;

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

        AssemblyReports[] reportNames = AssemblyReports.values();
        for(AssemblyReports report : reportNames) {
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

        AssemblyReports[] reportNames = AssemblyReports.values();
        for (AssemblyReports report : reportNames) {
            assertThat(library.getReportName(report.getReportName()), is(equalTo(report.getReportName())));
        }
    }

    @Test
    @TestRail(testCaseId = "1916")
    @Description("Validate report is available by search")
    public void testReportAvailableBySearch() {
        homePage = new LoginPage(driver)
                .login(UserUtil.getUser());

        searchResults = new SearchResults(driver);

        AssemblyReports[] reportNames = AssemblyReports.values();
        for (AssemblyReports report : reportNames) {
            homePage.searchForReport(report.getReportName());
            assertThat(searchResults.getReportName(report.getReportName()), is(equalTo(report.getReportName())));
        }
    }

    @Test
    @TestRail(testCaseId = "1922")
    @Description("Currency Code works")
    public void testCurrencyCodeWorks() throws InterruptedException {
        assemblyDetailsReport = new LoginPage(driver)
                .login(UserUtil.getUser())
                .navigateToLibraryPage()
                .navigateToReport(AssemblyReports.ASSEMBLY_DETAILS.getReportName())
                .waitForPageLoad()
                .checkUsdSelected()
                .clickApplyAndOk()
                .storeCapitalInvGrandTotalUSD()
                .clickOptionsButton()
                .checkGbpSelected()
                .clickApplyAndOk();

        // assert that values are different (not equal + less than)

    }
}
