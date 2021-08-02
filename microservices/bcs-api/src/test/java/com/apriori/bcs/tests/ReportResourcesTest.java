package com.apriori.bcs.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.bcs.controller.BatchPartResources;
import com.apriori.bcs.controller.BatchResources;
import com.apriori.bcs.controller.ReportResources;
import com.apriori.bcs.entity.request.NewPartRequest;
import com.apriori.bcs.entity.request.NewReportRequest;
import com.apriori.bcs.entity.request.ReportParameters;
import com.apriori.bcs.entity.response.Batch;
import com.apriori.bcs.entity.response.Part;
import com.apriori.bcs.entity.response.Report;
import com.apriori.bcs.entity.response.ReportTemplates;
import com.apriori.bcs.entity.response.Reports;
import com.apriori.bcs.utils.BcsUtils;
import com.apriori.bcs.utils.Constants;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReportResourcesTest extends TestUtil {
    private static ResponseWrapper<Report> report;
    private static ReportTemplates reportTemplates;
    private static Part part;
    private static Batch batch;

    @BeforeClass
    public static void testSetup() {
        batch = BatchResources.createNewBatch();

        NewPartRequest newPartRequest =
                (NewPartRequest)JsonManager.deserializeJsonFromInputStream(
                        FileResourceUtil.getResourceFileStream("schemas/requests/CreatePartData.json"), NewPartRequest.class);
        newPartRequest.setFilename("bracket_form.prt");

        part = (Part)BatchPartResources.createNewBatchPart(newPartRequest, batch.getIdentity()).getResponseEntity();

        int intervals = Constants.getPollingTimeout();
        int interval = 0;
        BcsUtils.State isPartCompleted = BcsUtils.State.PROCESSING;

        while (interval <= intervals) {
            part = (Part)BatchPartResources.getBatchPartRepresentation(batch.getIdentity(),
                            part.getIdentity()).getResponseEntity();
            try {
                isPartCompleted = BcsUtils.pollState(part, Part.class);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (isPartCompleted.equals(BcsUtils.State.COMPLETED)) {
                break;
            }
            interval++;
        }

        reportTemplates = (ReportTemplates) ReportResources.getReportTemplates("type[EQ]=PART_REPORT")
                .getResponseEntity();

        ReportParameters reportParameters = new ReportParameters();
        reportParameters.setCurrencyCode("USD");
        reportParameters.setRoundToDollar(true);

        NewReportRequest newReportRequest = generateReportRequest(reportParameters);

        report  = ReportResources.createReport(newReportRequest);
    }

    @AfterClass
    public static void testCleanup() {
        BcsUtils.checkAndCancelBatch(batch);
    }

    @Test
    @TestRail(testCaseId = {"4180"})
    @Description("API returns a list of all the reports in the CIS DB")
    public void getReports() {
        Reports reports = (Reports)ReportResources.getReports().getResponseEntity();
        Assert.assertNotEquals(reports.getItems().size(), 0);
    }

    @Test
    @TestRail(testCaseId = {"4182"})
    @Description("API returns a representation of a single report in the CIS DB")
    public void getReport() {
        ReportResources.getReportRepresentation(report.getResponseEntity().getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"4181"})
    @Description("Create a new report using the CIS API")
    public void createNewReport() {
        Assert.assertNotNull("No report was created", report.getResponseEntity().getIdentity());

        int intervals = Constants.getPollingTimeout();
        int interval = 0;
        BcsUtils.State reportState;
        while (interval <= intervals) {
            reportState = getReportState();
            if (reportState.equals(BcsUtils.State.ERRORED)) {
                Assert.fail(String.format("Report processing failed with error: '%s'",
                        BcsUtils.getErrors(report, Report.class)));
            }
            interval++;
        }
    }

    @Test
    @Issue("AP-69406")
    @TestRail(testCaseId = "8691")
    @Description("Create a report with unsupported roundToDollar values")
    public void createReportWithRoundToDollarSettings() {
        NewReportRequest newReportRequest;
        ReportParameters reportParameters = new ReportParameters();
        reportParameters.setCurrencyCode("USD");

        reportParameters.setRoundToDollar("true");
        newReportRequest = generateReportRequest(reportParameters);
        report  = ReportResources.createReport(newReportRequest, HttpStatus.SC_CONFLICT, null);
        Assert.assertEquals("Test passed with string 'true' roundToDollar value",
                report.getResponseEntity().getErrors(),
                "Value 'true' is not a 'BOOLEAN' parameter 'roundToDollar'");


        reportParameters.setRoundToDollar("hello");
        newReportRequest = generateReportRequest(reportParameters);
        report  = ReportResources.createReport(newReportRequest, HttpStatus.SC_CONFLICT, null);
        Assert.assertEquals("Test passed with string roundToDollar value",
                report.getResponseEntity().getErrors(),
                "Value 'hello' is not a 'BOOLEAN' parameter 'roundToDollar'");

        reportParameters.setRoundToDollar("");
        newReportRequest = generateReportRequest(reportParameters);
        report  = ReportResources.createReport(newReportRequest, HttpStatus.SC_CONFLICT, null);
        Assert.assertEquals("Test passed with empty string roundToDollar value",
                report.getResponseEntity().getErrors(),
                "Value '' is not a 'BOOLEAN' parameter 'roundToDollar'");

        reportParameters.setRoundToDollar(null);
        newReportRequest = generateReportRequest(reportParameters);
        report  = ReportResources.createReport(newReportRequest, HttpStatus.SC_NOT_FOUND, null);
        Assert.assertEquals("Test passed with NULL roundToDollar value",
                report.getResponseEntity().getErrors(),
                "Can't find mandatory report parameter 'roundToDollar' for report template 'DTC Part Summary'");
    }


    @Test
    @TestRail(testCaseId = {"4183"})
    @Description("Export a report using the CIS API")
    public void exportReport() {
        int intervals = Constants.getPollingTimeout();
        int interval = 0;
        BcsUtils.State reportState;
        boolean isReportReady = false;
        while (interval <= intervals) {
            reportState = getReportState();
            if (reportState.equals(BcsUtils.State.ERRORED)) {
                Assert.fail(String.format("Report processing failed with error: '%s'",
                        report.getResponseEntity().getErrors()));
                return;

            } else if (reportState.equals(BcsUtils.State.COMPLETED)) {
                isReportReady = true;
                break;
            }
            interval++;
        }

        if (!isReportReady) {
            Assert.fail(String.format("After %d seconds, the report hasn't completed processing (state = %s)",
                    Constants.getPollingTimeout() * 10,
                    getReportState().toString()));
        }

        ReportResources.exportReport(report.getResponseEntity().getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"7957"})
    @Description("Get a list of report templates")
    public void getReportTemplates() {
        ReportTemplates reportTemplates = (ReportTemplates)ReportResources.getReportTemplates().getResponseEntity();
        Assert.assertNotEquals(reportTemplates.getItems().size(), 0);
    }

    /**
     * Get current report state
     *
     * @return Report state
     */
    private BcsUtils.State getReportState() {
        Report rpt =
                (Report)ReportResources.getReportRepresentation(report.getResponseEntity().getIdentity()).getResponseEntity();
        try {
            return BcsUtils.pollState(rpt, Report.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Generate a new report request
     *
     * @param reportParameters Report parameter set
     * @return new report request
     */
    private static NewReportRequest generateReportRequest(ReportParameters reportParameters) {
        reportParameters.setCurrencyCode("USD");

        NewReportRequest newReportRequest =
                (NewReportRequest) JsonManager.deserializeJsonFromInputStream(
                        FileResourceUtil.getResourceFileStream("schemas/requests/CreateReportData.json"),
                        NewReportRequest.class);
        newReportRequest.setScopedIdentity(part.getIdentity());
        newReportRequest.setReportTemplateIdentity(reportTemplates.getItems().get(0).getIdentity());
        newReportRequest.setReportParameters(reportParameters);

        return newReportRequest;
    }
}
