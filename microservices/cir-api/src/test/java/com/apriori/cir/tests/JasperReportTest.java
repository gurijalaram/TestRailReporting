package com.apriori.cir.tests;

import com.apriori.cirapi.entity.JasperReportSummary;
import com.apriori.cirapi.entity.request.ParametersRequest;
import com.apriori.cirapi.entity.request.ReportCastingDTCRequest;
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
        ReportCastingDTCRequest reportCastingDTCRequest = ReportCastingDTCRequest.initFromJsonFile(); // will map ReportCastingDTCRequest.json to object

        // to update request data, update object fields
        reportCastingDTCRequest.setReportUnitUri("newReportUnitURI");
        reportCastingDTCRequest.setPages(5);

        // to update specific parameter
        reportCastingDTCRequest.getParameters().getReportParameterByName("currencyCode").setValue(Arrays.asList("Fully Burdened Cost",
            "\"Fully Burdened Cost\"", "sdfjhsdkjfh"));

        // to insert a new request parameters
        reportCastingDTCRequest.setParameters(ParametersRequest.builder().build());
        // ===============================================



        // Generate DTS Report
        JasperReportSummary jasperReportSummary =
            JasperReportUtil.init("0FBF6B3526703FC5C14FBD889D9F16F8")
                .generateJasperReportSummary(ReportCastingDTCRequest.initFromJsonFile());



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
