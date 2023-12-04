package com.apriori.bcs.api.tests;

import com.apriori.bcs.api.controller.BatchPartResources;
import com.apriori.bcs.api.controller.BatchResources;
import com.apriori.bcs.api.controller.ReportResources;
import com.apriori.bcs.api.enums.BCSState;
import com.apriori.bcs.api.models.request.reports.ReportParameters;
import com.apriori.bcs.api.models.request.reports.ReportRequest;
import com.apriori.bcs.api.models.response.Batch;
import com.apriori.bcs.api.models.response.Part;
import com.apriori.bcs.api.models.response.Report;
import com.apriori.bcs.api.models.response.ReportError;
import com.apriori.bcs.api.models.response.ReportExport;
import com.apriori.bcs.api.models.response.ReportTemplates;
import com.apriori.bcs.api.models.response.Reports;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class ReportResourcesTest extends TestUtil {

    private Part part;
    private Batch batch;
    private Report report;
    private SoftAssertions softAssertions;

    @BeforeAll
    public void beforeClass() {
        batch = BatchResources.createBatch().getResponseEntity();
        part = BatchPartResources.createNewBatchPartByID(batch.getIdentity()).getResponseEntity();
        report = ReportResources.createReport().getResponseEntity();
    }

    @BeforeEach
    public void setupTest() {
        softAssertions = new SoftAssertions();
    }

    @Test
    @TestRail(id = {4180})
    @Description("API returns a list of all the reports in the CIS DB")
    public void getReports() {
        Reports reports = ReportResources.getReports().getResponseEntity();
        softAssertions.assertThat(reports.getItems().size()).isEqualTo(0);
    }

    @Test
    @TestRail(id = {4182, 7958})
    @Description("API returns a representation of a single report in the CIS DB")
    public void getReport() {
        ReportResources.getReportRepresentation(report.getIdentity());
    }

    @Test
    @TestRail(id = {4181})
    @Description("Create a new report using the CIS API")
    public void createNewReport() {
        ReportRequest reportRequestTestData = ReportResources.getReportRequestData();

        reportRequestTestData.setExternalId(String.format(reportRequestTestData.getExternalId(), System.currentTimeMillis()));
        reportRequestTestData.setScopedIdentity(part.getIdentity());
        reportRequestTestData.setReportTemplateIdentity(ReportResources.getPartReportTemplateId());

        Report report = ReportResources.createReport(reportRequestTestData).getResponseEntity();
        softAssertions.assertThat(report.getState()).isEqualTo(BCSState.CREATED.toString());
    }

    @Test
    @Issue("AP-69406")
    @TestRail(id = 8691)
    @Description("Create a report with unsupported roundToDollar values")
    public void createReportWithRoundToDollarSettings() {
        ReportRequest setReportRequestTestData = ReportResources.getReportRequestData();
        ReportParameters reportParameters = ReportParameters.builder().roundToDollar("hello").currencyCode("USD").build();

        setReportRequestTestData.setExternalId(String.format(setReportRequestTestData.getExternalId(), System.currentTimeMillis()));
        setReportRequestTestData.setScopedIdentity(part.getIdentity());
        setReportRequestTestData.setReportTemplateIdentity(ReportResources.getPartReportTemplateId());
        setReportRequestTestData.setReportParameters(reportParameters);

        ReportError reportError = ReportResources.createReportWithInvalidData(setReportRequestTestData).getResponseEntity();
        softAssertions.assertThat(reportError.getMessage()).isEqualTo("Value 'hello' is not a 'BOOLEAN' parameter 'roundToDollar'");
    }

    @Test
    @TestRail(id = {4183})
    @Description("Export a report using the CIS API")
    public void exportReport() {
        ResponseWrapper<ReportExport> reportExportResponse = ReportResources.exportReport(report.getIdentity());
        ReportExport reportExport = reportExportResponse.getResponseEntity();
        softAssertions.assertThat(report.getIdentity()).isEqualTo(reportExport.getReportIdentity());
    }

    @Test
    @TestRail(id = {7957})
    @Description("Get a list of report templates")
    public void getReportTemplates() {
        ReportTemplates reportTemplates = ReportResources.getReportTemplates().getResponseEntity();
        softAssertions.assertThat(reportTemplates.getItems().size()).isNotEqualTo(0);
    }

    @AfterEach
    public void tearTest() {
        softAssertions.assertAll();
    }

    @AfterAll
    public void afterClass() {
        BatchResources.checkAndCancelBatch(batch);
    }
}
