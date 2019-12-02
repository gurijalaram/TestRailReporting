package cireporttests.ootbreports.general.assemblydetails;

import com.apriori.pageobjects.reports.pages.library.Library;
import com.apriori.pageobjects.reports.pages.view.Repository;
import com.apriori.pageobjects.reports.pages.login.LoginPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;
import io.qameta.allure.Description;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AssemblyDetailsReportTests extends TestBase {

    private Repository repository;
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

        assertThat(repository.getAssemblyA4ReportName(), is(equalTo("Assembly Cost (A4)")));
        assertThat(repository.getAssemblyLetterReportName(), is(equalTo("Assembly Cost (Letter)")));
        assertThat(repository.getAssemblyDetailsReportName(), is(equalTo("Assembly Details")));
        assertThat(repository.getComponentCostReportName(), is(equalTo("Component Cost")));
        assertThat(repository.getScenarioComparisonReportName(), is(equalTo("Scenario Comparison")));
    }

    @Test
    @TestRail(testCaseId = "3060")
    @Description("Validate report is available by library")
    public void testReportAvailabilityByLibrary() {
        library = new LoginPage(driver)
                .login(UserUtil.getUser())
                .navigateToLibraryPage();

        assertThat(library.getAssemblyA4ReportName(), is(equalTo("Assembly Cost (A4)")));
        assertThat(library.getAssemblyLetterReportName(), is(equalTo("Assembly Cost (Letter)")));
        assertThat(library.getAssemblyDetailsReportName(), is(equalTo("Assembly Details")));
        assertThat(library.getComponentCostReportName(), is(equalTo("Component Cost")));
        assertThat(library.getScenarioComparisonReportName(), is(equalTo("Scenario Comparison")));
    }
}
