package cireporttests.ootbreports.dtcmetrics.plastic;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsArrayContainingInAnyOrder.arrayContainingInAnyOrder;

import com.apriori.pageobjects.reports.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.reports.pages.view.ViewRepositoryPage;
import com.apriori.pageobjects.reports.pages.view.enums.AssemblyReportsEnum;
import com.apriori.pageobjects.reports.pages.view.enums.CastingReportsEnum;
import com.apriori.pageobjects.reports.pages.view.enums.ExportSetEnum;
import com.apriori.pageobjects.reports.pages.view.enums.RollupEnum;
import com.apriori.pageobjects.reports.pages.view.reports.AssemblyDetailsReportPage;
import com.apriori.pageobjects.reports.pages.view.reports.CastingDtcReportHeader;
import com.apriori.pageobjects.reports.pages.view.reports.PlasticDtcReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.AssemblyTypeEnum;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.PlasticDtcReportsEnum;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CiaCirTestDevTest;

import java.math.BigDecimal;

public class PlasticDtcTests extends TestBase {

    private PlasticDtcReportPage plasticDtcReportPage;
    private ViewRepositoryPage repository;
    private String assemblyType = "";

    public PlasticDtcTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = "1343")
    @Description("Test Plastic DTC Reports Availability")
    public void testPlasticDtcReportAvailability() {
        repository = new ReportsLoginPage(driver)
            .login()
            .navigateToViewRepositoryPage()
            .navigateToPlasticFolder();

        String[] expectedReportNames = repository.getReportNamesValues();

        assertThat(expectedReportNames, arrayContainingInAnyOrder(repository.getActualReportNames()));
    }

    @Test
    @TestRail(testCaseId = "1344")
    @Description("Test Plastic DTC Reports Export Set Availability")
    public void testPlasticDtcReportExportSetAvailability() {
        plasticDtcReportPage = new ReportsLoginPage(driver)
            .login()
            .navigateToLibraryPage()
            .navigateToReport(PlasticDtcReportsEnum.PLASTIC_DTC_REPORT.getReportName(), PlasticDtcReportPage.class);

        String[] expectedExportSetValues = plasticDtcReportPage.getExportSetEnumValues();

        assertThat(expectedExportSetValues, arrayContainingInAnyOrder(plasticDtcReportPage.getActualExportSetValues()));
    }

    @Test
    @Category(CiaCirTestDevTest.class)
    @TestRail(testCaseId = "1365")
    @Description("Verify rollup dropdown input control functions correctly")
    public void testRollupDropdownInputControlsFunctionsProperly() {
        plasticDtcReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(PlasticDtcReportsEnum.PLASTIC_DTC_REPORT.getReportName(), PlasticDtcReportPage.class)
                .waitForInputControlsLoad()
                .selectRollup(RollupEnum.ROLL_UP_A.getRollupName())
                .clickOk()
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), PlasticDtcReportPage.class);

        assertThat(plasticDtcReportPage.getDisplayedRollup(PlasticDtcReportsEnum.PLASTIC_DTC_REPORT.getReportName()),
                is(equalTo(RollupEnum.ROLL_UP_A.getRollupName())));
    }

    @Test
    @Category(CiaCirTestDevTest.class)
    @TestRail(testCaseId = "1370")
    @Description("Verify currency code functionality works correctly")
    public void testCurrencyCodeFunctionality() {
        assemblyType = AssemblyTypeEnum.SUB_ASSEMBLY.getAssemblyType();
        BigDecimal gbpAnnualSpend;
        BigDecimal usdAnnualSpend;

        plasticDtcReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(PlasticDtcReportsEnum.PLASTIC_DTC_REPORT.getReportName(), PlasticDtcReportPage.class)
                .waitForInputControlsLoad()
                .selectRollup(RollupEnum.ROLL_UP_A.getRollupName())
                .checkCurrencySelected(CurrencyEnum.USD.getCurrency())
                .clickOk()
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), PlasticDtcReportPage.class);

        //usdAnnualSpend = plasticDtcReportPage.getFBCValueFromBubbleTooltip();

        plasticDtcReportPage.clickInputControlsButton()
                .checkCurrencySelected(CurrencyEnum.GBP.getCurrency())
                .clickOk()
                .waitForCorrectCurrency(CurrencyEnum.GBP.getCurrency(), AssemblyDetailsReportPage.class);

        //gbpAnnualSpend = plasticDtcReportPage.getValueFromTable(assemblyType, "Grand Total", "Capital Investments");

        assertThat(plasticDtcReportPage.getCurrentCurrency(), is(equalTo(CurrencyEnum.GBP.getCurrency())));
        //assertThat(gbpAnnualSpend, is(not(usdAnnualSpend)));
    }
}
