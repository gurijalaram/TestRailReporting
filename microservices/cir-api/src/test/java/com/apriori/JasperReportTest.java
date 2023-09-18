package com.apriori;

import com.apriori.cir.JasperReportSummary;
import com.apriori.cir.models.request.ParametersRequest;
import com.apriori.cir.models.request.ReportRequest;
import com.apriori.cir.models.response.ChartDataPoint;
import com.apriori.cir.utils.JasperReportUtil;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class JasperReportTest {

    @Test
    @Disabled
    public void getJasperReportSummaryTest() {

        // usage example.

        // How to init a request data
        ReportRequest reportRequest = ReportRequest.initFromJsonFile("ReportCastingDTCRequest"); // will map CastingDtcReportRequest.json to object

        // to update request data, update object fields
        reportRequest.setReportUnitUri("newReportUnitURI");
        reportRequest.setPages(5);

        // to update specific parameter
        reportRequest.getParameters().getReportParameterByName("currencyCode").setValue(Arrays.asList("Fully Burdened Cost",
            "\"Fully Burdened Cost\"", "sdfjhsdkjfh"));

        // to insert a new request parameters
        reportRequest.setParameters(ParametersRequest.builder().build());
        // ===============================================



        // Generate DTC Report
        JasperReportSummary jasperReportSummary =
            JasperReportUtil.init("0FBF6B3526703FC5C14FBD889D9F16F8")
                .generateJasperReportSummary(ReportRequest.initFromJsonFile("ReportCastingDTCRequest"));



        // Get chart data point element
        ChartDataPoint chartDataPointToTest = jasperReportSummary.getFirstChartData().getChartDataPointByPartName("40137441.MLDES.0002 (Initial)");

        System.out.println(chartDataPointToTest.getAnnualSpend());
        System.out.println(chartDataPointToTest.getMassMetric());
        System.out.println(chartDataPointToTest.getCostMetric());
        System.out.println(chartDataPointToTest.getDTCScore());

        chartDataPointToTest.getPropertyByName("customPropertyName");

        // Get HTML data to validate
        jasperReportSummary.getReportHtmlPart();

    }
}
