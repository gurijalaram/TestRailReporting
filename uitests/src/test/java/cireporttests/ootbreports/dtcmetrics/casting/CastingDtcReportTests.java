package cireporttests.ootbreports.dtcmetrics.casting;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.reports.pages.login.LoginPage;
import com.apriori.pageobjects.reports.pages.view.ViewRepositoryPage;
import com.apriori.pageobjects.reports.pages.view.enums.CastingReportsEnum;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

public class CastingDtcReportTests extends TestBase {

    private ViewRepositoryPage repository;

    public CastingDtcReportTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "1676")
    @Description("validate report available by navigation")
    public void testReportAvailabilityByMenu() {
        repository = new LoginPage(driver)
            .login(UserUtil.getUser())
            .navigateToViewRepositoryPage()
            .navigateToCastingFolder();

        CastingReportsEnum[] reportNames = CastingReportsEnum.values();
        for (CastingReportsEnum report : reportNames) {
            assertThat(repository.getReportName(report.getReportName()), is(equalTo(report.getReportName())));
        }
    }
}
