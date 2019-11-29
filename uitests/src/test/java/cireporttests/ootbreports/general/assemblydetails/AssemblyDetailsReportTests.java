package cireporttests.ootbreports.general.assemblydetails;

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

    public AssemblyDetailsReportTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "2987")
    @Description("Ensure that the CI Reports User Guide Link works")
    public void testCIReportsUserGuideNavigation() {
        repository = new LoginPage(driver)
                .login(UserUtil.getUser())
                .navigateToViewRepositoryPage()
                .navigateToFolder("Organization")
                .navigateToFolder("aPriori")
                .navigateToFolder("Reports")
                .navigateToFolder("General");

        assertThat(repository.getCountOfGeneralReports(), is(equalTo("5")));

        assertThat(repository.getReportNameText("Assembly Cost (A4)"), is(equalTo("Assembly Cost (A4)")));
        assertThat(repository.getReportNameText("Assembly Cost (Letter)"), is(equalTo("Assembly Cost (Letter)")));
        assertThat(repository.getReportNameText("Assembly Details"), is(equalTo("Assembly Details")));
        assertThat(repository.getReportNameText("Component Cost"), is(equalTo("Component Cost")));
        assertThat(repository.getReportNameText("Scenario Comparison"), is(equalTo("Scenario Comparison")));
    }
}
