package com.ootbreports.newreportstests.dtcmetrics.castingdtc;

import java.util.Arrays;

import com.apriori.cirapi.entity.JasperReportSummary;
import com.apriori.cirapi.entity.request.ReportRequest;
import com.apriori.cirapi.entity.response.ChartDataPoint;
import com.apriori.cirapi.utils.JasperReportUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.reports.ExportSetEnum;
import com.apriori.utils.http.utils.WebDriverUtils;
import com.apriori.utils.web.driver.DriverFactory;
import com.apriori.utils.web.driver.WebDriverService;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ReportsTest;

public class CastingDtcReportTests {

    @Test
    @Category(ReportsTest.class)
    @TestRail(testCaseId = {"1699"})
    @Description("Verify Currency Code input control functions correctly")
    public void testCurrencyCode() {
        // use login endpoint to get authorisation

        ReportRequest reportRequest = ReportRequest.initFromJsonFile("ReportCastingDTCRequest");
        reportRequest.getParameters().getReportParameterByName("exportSet")
            .setValue(Arrays.asList("Export Set", "\"Export Set\"", ExportSetEnum.CASTING_DTC.getExportSetName()));
        reportRequest.getParameters().getReportParameterByName("currencyCode")
            .setValue(Arrays.asList("Currency Code", "\"Currency Code\"", "GBP"));

        // get jsessionid and pass into the init call below
        // ask Vlad how to get jsession id
        /*JasperReportSummary jasperReportSummary =
            JasperReportUtil.init("")
                .generateJasperReportSummary(reportRequest);*/

        /*ChartDataPoint chartDataPointToTest = jasperReportSummary.getChartDataPointByPartName("40137441.MLDES.0002 (Initial)");

        System.out.println(chartDataPointToTest.getAnnualSpend());
        System.out.println(chartDataPointToTest.getMassMetric());
        System.out.println(chartDataPointToTest.getCostMetric());
        System.out.println(chartDataPointToTest.getDTCScore());

        chartDataPointToTest.getPropertyByName("customPropertyName");

        // Get HTML data to validate
        jasperReportSummary.getReportHtmlPart();*/
    }

}
