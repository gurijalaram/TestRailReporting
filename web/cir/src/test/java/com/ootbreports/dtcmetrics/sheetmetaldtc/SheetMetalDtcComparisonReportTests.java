package com.ootbreports.dtcmetrics.sheetmetaldtc;

import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.pageobjects.pages.view.reports.SheetMetalDtcReportPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.CurrencyEnum;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.enums.reports.ReportNamesEnum;
import com.apriori.utils.web.driver.TestBase;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CiaCirTestDevTest;
import utils.Constants;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SheetMetalDtcComparisonReportTests extends TestBase {

    private SheetMetalDtcReportPage sheetMetalDtcReportPage;

    public SheetMetalDtcComparisonReportTests() {
        super();
    }

    @Test
    @Category(CiaCirTestDevTest.class)
    @TestRail(testCaseId = "3038")
    @Description("Verify Export Sets are available for selection")
    public void testExportSetAndRollupSelection() {
        String rollupName = String.format(
                "%s (%s)",
                ReportNamesEnum.SHEET_METAL_DTC.getReportName().toUpperCase(),
                Constants.DEFAULT_SCENARIO_NAME
        );
        sheetMetalDtcReportPage = new ReportsLoginPage(driver)
                .login()
                .navigateToLibraryPage()
                .navigateToReport(ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(), GenericReportPage.class)
                .selectExportSet(ExportSetEnum.SHEET_METAL_DTC.getExportSetName())
                .waitForCorrectRollupInDropdown(rollupName)
                .clickOk()
                .waitForCorrectCurrency(CurrencyEnum.USD.getCurrency(), SheetMetalDtcReportPage.class);

        assertThat(sheetMetalDtcReportPage.getDisplayedRollup(), is(equalTo(rollupName)));
        assertThat(sheetMetalDtcReportPage.getDisplayedExportSet(),
                is(equalTo(ExportSetEnum.SHEET_METAL_DTC.getExportSetName())));
    }
}
