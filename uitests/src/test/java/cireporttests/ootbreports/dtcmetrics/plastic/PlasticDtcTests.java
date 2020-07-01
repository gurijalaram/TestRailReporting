package cireporttests.ootbreports.dtcmetrics.plastic;

import com.apriori.pageobjects.reports.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.reports.pages.view.ViewRepositoryPage;
import com.apriori.pageobjects.reports.pages.view.enums.ExportSetEnum;
import com.apriori.pageobjects.reports.pages.view.reports.PlasticDtcReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.PlasticDtcReportsEnum;
import com.apriori.utils.web.driver.TestBase;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CiaCirTestDevTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PlasticDtcTests extends TestBase {

    private PlasticDtcReportPage plasticDtcReportPage;
    private ViewRepositoryPage repository;

    public PlasticDtcTests() { super(); }

    @Test
    @Category(CiaCirTestDevTest.class)
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

    @Test
    @Category(CiaCirTestDevTest.class)
    @TestRail(testCaseId = "1344")
    @Description("Test Plastic DTC Reports Export Set Availability")
    public void testPlasticDtcReportExportSetAvailability() {
        plasticDtcReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(PlasticDtcReportsEnum.PLASTIC_DTC_REPORT.getReportName(), PlasticDtcReportPage.class);

        String[] exportSetEnums = plasticDtcReportPage.getExportSetEnumValues();
        for (String exportSet : exportSetEnums) {
            assertThat(plasticDtcReportPage.getExportSetName(exportSet), is(equalTo(exportSet)));
        }
    }
}
