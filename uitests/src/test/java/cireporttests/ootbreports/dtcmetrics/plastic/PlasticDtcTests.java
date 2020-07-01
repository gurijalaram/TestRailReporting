package cireporttests.ootbreports.dtcmetrics.plastic;

import com.apriori.pageobjects.reports.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.reports.pages.view.ViewRepositoryPage;
import com.apriori.pageobjects.reports.pages.view.enums.CastingReportsEnum;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.PlasticDtcReportsEnum;
import com.apriori.utils.web.driver.TestBase;
import io.qameta.allure.Description;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PlasticDtcTests extends TestBase {

    private ViewRepositoryPage repository;

    public PlasticDtcTests() { super(); }

    @Test
    @TestRail(testCaseId = "1343")
    @Description("Test Plastic DTC Reports Availability")
    public void testPlasticDtcReportAvailability() {
        repository = new ReportsLoginPage(driver)
            .login()
            .navigateToViewRepositoryPage()
            .navigateToPlasticFolder();

        PlasticDtcReportsEnum[] reportNames = PlasticDtcReportsEnum.values();
        for (PlasticDtcReportsEnum report : reportNames) {
            assertThat(repository.getReportName(report.getReportName()), is(equalTo(report.getReportName())));
        }
    }
}
