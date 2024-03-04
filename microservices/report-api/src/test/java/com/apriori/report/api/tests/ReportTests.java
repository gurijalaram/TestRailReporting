package com.apriori.report.api.tests;

import com.apriori.report.api.controller.ReportReplicaController;
import com.apriori.report.api.models.Report;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

public class ReportTests {

    SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @TestRail(id = 0)
    @Description("")
    public void testReportReplica() {
        String customerId = "6C1F8C1D4D75";

        ReportReplicaController reportReplicaController = new ReportReplicaController();
        Report postResponse = reportReplicaController.postExecuteReport(customerId);

        softAssertions.assertThat(postResponse.getStatus()).isEqualTo("RUNNING");

        Report reportResponse = reportReplicaController.getRequestStatus(customerId, postResponse.getExecutionId());

        softAssertions.assertThat(reportResponse.getExecutionId()).isNotEmpty();
        softAssertions.assertThat(reportResponse.getStatus()).isEqualTo("SUCCEEDED");
        softAssertions.assertThat(reportResponse.getUrl()).isNotEmpty();
        softAssertions.assertAll();
    }
}
