package cireporttests.ootbreports.general.assemblydetails;

import com.apriori.pageobjects.reports.pages.login.LoginPage;
import com.apriori.pageobjects.reports.pages.view.Repository;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;
import io.qameta.allure.Description;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
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
                .navigateToGeneralFolder();

        // assert that five OOTB reports are there
        assertThat(repository.getCountOfGeneralReports(), is(equalTo("5")));
    }

}
