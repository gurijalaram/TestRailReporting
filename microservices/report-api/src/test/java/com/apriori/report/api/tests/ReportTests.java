package com.apriori.report.api.tests;

import com.apriori.report.api.controller.ReportReplicaController;
import com.apriori.report.api.models.Report;
import com.apriori.report.api.models.ReportBean;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ReportTests {

    SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @TestRail(id = 0)
    @Description("Test to check report is populated")
    public void testReportReplica() {
        String customerId = "6C1F8C1D4D75";

        ReportReplicaController reportReplicaController = new ReportReplicaController();
        Report postResponse = reportReplicaController.postExecuteReport(customerId);

        Report reportResponse = reportReplicaController.getReportStatus(customerId, postResponse.getExecutionId());

        softAssertions.assertThat(reportResponse.getExecutionId()).isNotEmpty();
        softAssertions.assertThat(reportResponse.getStatus()).isEqualTo("SUCCEEDED");
        softAssertions.assertThat(reportResponse.getUrl()).isNotEmpty();

        List<ReportBean> reportCsv = reportReplicaController.downloadReadReport(reportResponse.getUrl(), '|', ReportBean.class);

        if (reportCsv.size() > 5) {
            reportCsv = reportCsv.subList(0, 5);
        }
        reportCsv.forEach(row -> {
            softAssertions.assertThat(row).isNotNull();
            softAssertions.assertThat(row.getId()).isNotNull();
            softAssertions.assertThat(row.getCostingMessage()).isNotEmpty();
            softAssertions.assertThat(row.getProcessGroupId()).isNotNull();
        });

        softAssertions.assertAll();
    }
}
