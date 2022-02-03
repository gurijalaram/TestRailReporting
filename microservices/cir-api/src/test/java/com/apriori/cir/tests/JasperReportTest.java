package com.apriori.cir.tests;

import com.apriori.cirapi.entity.JasperReportSummary;
import com.apriori.cirapi.entity.request.ReportCastingDTCRequest;
import com.apriori.cirapi.utils.JasperReportUtil;

import org.junit.Ignore;
import org.junit.Test;

public class JasperReportTest {

    @Test
    @Ignore
    public void getJasperReportSummaryTest() {

        // TODO z: usage example.
        JasperReportSummary jasperReportSummary =
            JasperReportUtil.init("2BF833B03BD6C330473EE56AD892D53F")
                .generateJasperReportSummary(ReportCastingDTCRequest.initFromJsonFile());

        ReportCastingDTCRequest.initFromJsonFile().getParameters()
            .getReportParameterByName("latestExportDate").getValue();
    }
}
