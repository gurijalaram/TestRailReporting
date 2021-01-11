package com.apriori.cis.tests;

import static org.junit.Assert.fail;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.cis.controller.ReportResources;
import com.apriori.cis.entity.request.NewReportRequest;
import com.apriori.cis.entity.response.Report;
import com.apriori.cis.utils.CisUtils;
import com.apriori.cis.utils.Constants;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReportResourcesTest extends TestUtil {

    private static final Logger logger = LoggerFactory.getLogger(ReportResourcesTest.class);

    @Test
    @TestRail(testCaseId = "4180")
    @Description("API returns a list of all the reports in the CIS DB")
    public void getReports() {
        ReportResources.getReports();
    }

    @Test
    @TestRail(testCaseId = "4182")
    @Description("API returns a representation of a single report in the CIS DB")
    public void getReport() {
        ReportResources.getReportRepresentation(Constants.getCisReportIdentity());
    }

    @Test
    @TestRail(testCaseId = "4184")
    @Description("API returns report types in the CIS DB")
    public void getReportTypes() {
        ReportResources.getReportTypes();
    }

    @Test
    @TestRail(testCaseId = "4185")
    @Description("API returns a single repport type in the CIS DB")
    public void getReportType() {
        ReportResources.getSpecificReportType();
    }


    @Test
    @TestRail(testCaseId = "4181")
    @Description("Create a new report using the CIS API")
    public void createNewReport() {
        Object obj = JsonManager.deserializeJsonFromStream(
                FileResourceUtil.getResourceFileStream("schemas/requests/CreateReportData.json"), NewReportRequest.class);

        Report report  = ReportResources.createReport(obj);

        try {
            String reportIdentity = CisUtils.getIdentity(report, Report.class);
            Constants.setCisReportIdentity(reportIdentity);
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Test
    @TestRail(testCaseId = "4183")
    @Description("Export a report using the CIS API")
    public void exportReport() {
        Integer count = 0;
        Object rptObj = JsonManager.deserializeJsonFromStream(
                FileResourceUtil.getResourceFileStream("schemas/requests/CreateReportData.json"), NewReportRequest.class);

        Report report = ReportResources.createReport(rptObj, Constants.getCisPartIdentity());
        String reportIdentity = report.getResponse().getIdentity();
        String reportState;

        while (count <= 15) {
            report = (Report)ReportResources.getReportRepresentation(reportIdentity).getResponseEntity();
            reportState = report.getResponse().getState();

            if (reportState.equals("ERRORED")) {
                fail("Report is state 'ERRORED'");
                return;
            }

            if (reportState.toUpperCase().equals("COMPLETED")) {
                break;
            } else {
                try {
                    logger.error("CURRENT STATE: " + reportState);
                    Thread.sleep(10000);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    logger.error(Arrays.toString(e.getStackTrace()));
                }
                count += 1;
            }
        }
        ReportResources.exportReport(reportIdentity);
    }
}
