package com.apriori.cir.tests;

import com.apriori.cirapi.entity.JasperReportSummary;
import com.apriori.cirapi.entity.request.ParametersRequest;
import com.apriori.cirapi.entity.request.ReportRequest;
import com.apriori.cirapi.entity.response.ChartDataPoint;
import com.apriori.cirapi.utils.JasperReportUtil;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

public class JasperReportTest {

    @Test
    @Ignore
    public void getJasperReportSummaryTest() {

        // TODO z: usage example.

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
        ChartDataPoint chartDataPointToTest = jasperReportSummary.getChartDataPointByPartName("40137441.MLDES.0002 (Initial)");

        System.out.println(chartDataPointToTest.getAnnualSpend());
        System.out.println(chartDataPointToTest.getMassMetric());
        System.out.println(chartDataPointToTest.getCostMetric());
        System.out.println(chartDataPointToTest.getDTCScore());

        chartDataPointToTest.getPropertyByName("customPropertyName");

        // Get HTML data to validate
        jasperReportSummary.getReportHtmlPart();

    }
}
