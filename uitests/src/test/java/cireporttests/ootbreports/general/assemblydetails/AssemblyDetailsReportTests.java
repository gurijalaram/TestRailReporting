package cireporttests.ootbreports.general.assemblydetails;

import com.apriori.pageobjects.reports.pages.view.SearchResults;
import com.apriori.pageobjects.reports.pages.homepage.HomePage;
import com.apriori.pageobjects.reports.pages.library.Library;
import com.apriori.pageobjects.reports.pages.view.Repository;
import com.apriori.pageobjects.reports.pages.login.LoginPage;
import com.apriori.utils.web.driver.TestBase;
import com.apriori.utils.users.UserUtil;
import io.qameta.allure.Description;
import com.apriori.utils.TestRail;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class AssemblyDetailsReportTests extends TestBase {

    private SearchResults searchResults;
    private Repository repository;
    private HomePage homePage;
    private Library library;

    String[] reportNames = {
            "Assembly Cost (A4)",
            "Assembly Cost (Letter)",
            "Assembly Details",
            "Component Cost",
            "Scenario Comparison"
    };

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

        for(int i = 0; i < 5; i++) {
            assertThat(repository.getReportName(reportNames[i]), is(equalTo(reportNames[i])));
        }
    }

    @Test
    @TestRail(testCaseId = "3060")
    @Description("Validate report is available by library")
    public void testReportAvailabilityByLibrary() {
        library = new LoginPage(driver)
                .login(UserUtil.getUser())
                .navigateToLibraryPage();

        for (int i = 0; i < 5; i++) {
            assertThat(library.getReportName(reportNames[i]), is(equalTo(reportNames[i])));
        }
    }

    @Test
    @TestRail(testCaseId = "1916")
    @Description("Validate report is available by search")
    public void testReportAvailableBySearch() {
        homePage = new LoginPage(driver)
                .login(UserUtil.getUser());

        searchResults = new SearchResults(driver);

        for (int i = 0; i < 5; i++) {
            homePage.searchForReport(reportNames[i]);
            assertThat(searchResults.getReportName(reportNames[i]), is(equalTo(reportNames[i])));
        }
    }
}
