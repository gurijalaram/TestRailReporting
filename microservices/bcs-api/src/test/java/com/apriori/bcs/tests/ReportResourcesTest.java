package com.apriori.bcs.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.apriori.TestUtil;
import com.apriori.bcs.controller.BatchPartResources;
import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.controller.ReportResources;
import com.apriori.bcs.entity.request.reports.ReportParameters;
import com.apriori.bcs.entity.request.reports.ReportRequest;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Part;
import com.apriori.bcs.entity.response.Report;
import com.apriori.bcs.entity.response.ReportError;
import com.apriori.bcs.entity.response.ReportExport;
import com.apriori.bcs.entity.response.ReportTemplates;
import com.apriori.bcs.entity.response.Reports;
import com.apriori.bcs.enums.BCSState;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReportResourcesTest extends TestUtil {

    private static Part part;
    private static Batch batch;
    private static Report report;

    @BeforeClass
    public static void testSetup() {
        batch = BatchResources.createBatch().getResponseEntity();
        part = BatchPartResources.createNewBatchPartByID(batch.getIdentity()).getResponseEntity();
        assertTrue("Verify Part is in completed state", BatchPartResources.waitUntilPartStateIsCompleted(batch.getIdentity(), part.getIdentity()));
        report = ReportResources.createReport().getResponseEntity();
        assertTrue("Verify Report is in completed state", ReportResources.waitUntilReportStateIsCompleted(report.getIdentity()));
    }

    @Test
    @TestRail(id = {4180})
    @Description("API returns a list of all the reports in the CIS DB")
    public void getReports() {
        Reports reports = ReportResources.getReports().getResponseEntity();
        assertNotEquals(reports.getItems().size(), 0);
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
        assertThat(report.getState(), is(equalTo(BCSState.CREATED.toString())));
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
        assertEquals("Test passed with string roundToDollar value",
            "Value 'hello' is not a 'BOOLEAN' parameter 'roundToDollar'",
            reportError.getMessage());
    }

    @Test
    @TestRail(id = {4183})
    @Description("Export a report using the CIS API")
    public void exportReport() {
        ResponseWrapper<ReportExport> reportExportResponse = ReportResources.exportReport(report.getIdentity());
        ReportExport reportExport = reportExportResponse.getResponseEntity();

        assertThat(report.getIdentity(), is(equalTo(reportExport.getReportIdentity())));
    }

    @Test
    @TestRail(id = {7957})
    @Description("Get a list of report templates")
    public void getReportTemplates() {
        ReportTemplates reportTemplates = ReportResources.getReportTemplates().getResponseEntity();
        assertNotEquals(reportTemplates.getItems().size(), 0);
    }

    @AfterClass
    public static void testCleanup() {
        BatchResources.checkAndCancelBatch(batch);
    }
}
