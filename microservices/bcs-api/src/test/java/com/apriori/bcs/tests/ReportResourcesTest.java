package com.apriori.bcs.tests;

import static org.junit.Assert.fail;

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
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReportResourcesTest extends TestUtil {
    private static Report report;
    private static ReportTemplates reportTemplates;
    private static Part part;
    private static Batch batch;
    private static NewReportRequest newReportRequest;

    @BeforeClass
    public static void testSetup() {
        batch = (Batch)BatchResources.createNewBatch();

        NewPartRequest newPartRequest =
                (NewPartRequest)JsonManager.deserializeJsonFromInputStream(
                        FileResourceUtil.getResourceFileStream("schemas/requests/CreatePartData.json"), NewPartRequest.class);
        newPartRequest.setFilename("bracket_form.prt");

        part = (Part)BatchPartResources.createNewBatchPart(newPartRequest, batch.getIdentity());

        int intervals = Constants.getPollingTimeout();
        int interval = 0;
        BcsUtils.State isPartCompleted;
        while (interval <= intervals) {
            part = (Part)BatchPartResources.getBatchPartRepresentation(batch.getIdentity(),
                            part.getIdentity()).getResponseEntity();
            isPartCompleted = BcsUtils.pollState(part, Part.class);
            if (isPartCompleted.equals(BcsUtils.State.COMPLETE)) {
                break;
            }
            interval++;
        }

        reportTemplates = (ReportTemplates) ReportResources.getReportTemplates("type[EQ]=PART_REPORT")
                .getResponseEntity();

        ReportParameters reportParameters = new ReportParameters();
        reportParameters.setCurrencyCode("USD");
        reportParameters.setRoundToDollar(true);

        newReportRequest =
                (NewReportRequest) JsonManager.deserializeJsonFromInputStream(
                        FileResourceUtil.getResourceFileStream("schemas/requests/CreateReportData.json"),
                        NewReportRequest.class);
        newReportRequest.setScopedIdentity(part.getIdentity());
        newReportRequest.setReportTemplateIdentity(reportTemplates.getItems().get(0).getIdentity());
        newReportRequest.setReportParameters(reportParameters);

        report  = ReportResources.createReport(newReportRequest);
    }

    private static final Logger logger = LoggerFactory.getLogger(ReportResourcesTest.class);

    @Test
    @Issue("AP-69406")
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
        ReportResources.getReportRepresentation(report.getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"4181"})
    @Description("Create a new report using the CIS API")
    public void createNewReport() {
        Assert.assertNotNull("No report was created", report.getIdentity());

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
                        report.getErrors()));
                return;

            } else if (reportState.equals(BcsUtils.State.COMPLETE)) {
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

        ReportResources.exportReport(report.getIdentity());
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
        report = (Report)ReportResources.getReportRepresentation(report.getIdentity()).getResponseEntity();
        return BcsUtils.pollState(report, Report.class);
    }
}
