package cireporttests.ootbreports.dtcmetrics.machiningdtc;

import com.apriori.pageobjects.reports.pages.homepage.HomePage;
import com.apriori.pageobjects.reports.pages.library.LibraryPage;
import com.apriori.pageobjects.reports.pages.login.LoginPage;
import com.apriori.pageobjects.reports.pages.view.ViewRepositoryPage;
import com.apriori.pageobjects.reports.pages.view.ViewSearchResultsPage;
import com.apriori.pageobjects.reports.pages.view.enums.AssemblyReportsEnum;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;
import io.qameta.allure.Description;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MachiningDtcReportTests extends TestBase {

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
}
