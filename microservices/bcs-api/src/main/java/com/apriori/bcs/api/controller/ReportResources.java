package com.apriori.bcs.api.controller;

import com.apriori.bcs.api.enums.BCSAPIEnum;
import com.apriori.bcs.api.enums.BCSState;
import com.apriori.bcs.api.models.request.reports.ReportRequest;
import com.apriori.bcs.api.models.response.Batch;
import com.apriori.bcs.api.models.response.Part;
import com.apriori.bcs.api.models.response.Report;
import com.apriori.bcs.api.models.response.ReportError;
import com.apriori.bcs.api.models.response.ReportExport;
import com.apriori.bcs.api.models.response.ReportTemplates;
import com.apriori.bcs.api.models.response.Reports;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.json.JsonManager;
import com.apriori.shared.util.properties.PropertiesContext;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

// TODO ALL: test it
public class ReportResources {
    private static final Logger logger = LoggerFactory.getLogger(ReportResources.class);
    private static final long WAIT_TIME = 300;

    /**
     * Create report with default parameters
     *
     * @return response - Object of ResponseWrapper
     */
    public static ResponseWrapper<Report> createReport() {
        ReportRequest reportRequestTestData = getReportRequestData();

        reportRequestTestData.setExternalId(String.format(reportRequestTestData.getExternalId(), System.currentTimeMillis()));
        reportRequestTestData.setScopedIdentity(getPartInCompletedState().getIdentity());
        reportRequestTestData.setReportTemplateIdentity(getPartReportTemplateId());

        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.REPORTS, Report.class)
            .inlineVariables(PropertiesContext.get("customer_identity"))
            .body(reportRequestTestData);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Create a report with custom data sent as request data
     *
     * @param reportRequestData - ReportRequest data as POJO
     * @return - response - object of ResponseWrapper object.
     */
    public static ResponseWrapper<Report> createReport(ReportRequest reportRequestData) {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.REPORTS, Report.class)
            .inlineVariables(PropertiesContext.get("customer_identity"))
            .body(reportRequestData);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Create a report with invalid custom data sent as request data
     *
     * @param reportRequestData - ReportRequest data as POJO
     * @return response - response object of ReportError POJO Object
     */
    public static ResponseWrapper<ReportError> createReportWithInvalidData(ReportRequest reportRequestData) {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.REPORTS, ReportError.class)
            .inlineVariables(PropertiesContext.get("customer_identity"))
            .body(reportRequestData);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Submit get Reports API
     *
     * @return - response - ResponseWrapper object of Reports POJO
     */
    public static ResponseWrapper<Reports> getReports() {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.REPORTS, Reports.class)
            .inlineVariables(PropertiesContext.get("customer_identity"));
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * submit get report with report ID
     *
     * @param reportIdentity - report ID
     * @return response object of ResponseWrapper with Report POJO
     */
    public static ResponseWrapper<Report> getReportRepresentation(String reportIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.REPORT_BY_ID, Report.class)
            .inlineVariables(PropertiesContext.get("customer_identity"), reportIdentity)
            .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Submit export report api
     *
     * @param reportIdentity - report ID
     * @return response - object ResponseWrapper with ReportExport POJO
     */
    public static ResponseWrapper<ReportExport> exportReport(String reportIdentity) {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.REPORT_EXPORT_BY_ID, ReportExport.class)
            .inlineVariables(PropertiesContext.get("customer_identity"), reportIdentity)
            .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Submit report template API
     *
     * @return - response  object of ResponseWrapper.
     */
    public static ResponseWrapper<ReportTemplates> getReportTemplates() {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.REPORT_TEMPLATES, ReportTemplates.class)
            .inlineVariables(PropertiesContext.get("customer_identity"));

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Generate a new report request
     *
     * @return new report request
     */
    public static ReportRequest getReportRequestData() {
        ReportRequest newReportRequest =
            JsonManager.deserializeJsonFromInputStream(
                FileResourceUtil.getResourceFileStream("schemas/testdata/CreateReportData.json"),
                ReportRequest.class);
        return newReportRequest;
    }

    /**
     * submit get part report template and returns template id
     *
     * @return report Template ID
     */
    public static String getPartReportTemplateId() {
        ReportTemplates reportTemplates = getReportTemplates().getResponseEntity();
        return reportTemplates.getItems().get(0).getIdentity();
    }

    /**
     * Creates Batch , part with default parameters and returns completed part
     *
     * @return part POJO
     */
    public static Part getPartInCompletedState() {
        Batch batch = BatchResources.createBatch().getResponseEntity();
        Part part = BatchPartResources.createNewBatchPartByID(batch.getIdentity()).getResponseEntity();
        if (BatchPartResources.waitUntilPartStateIsCompleted(batch.getIdentity(), part.getIdentity())) {
            return part;
        }
        return null;
    }

    /**
     * Checks and wait until the report status is completed
     *
     * @param reportIdentity - report ID to send
     * @return boolean true or false
     */
    public static boolean waitUntilReportStateIsCompleted(String reportIdentity) {
        long initialTime = System.currentTimeMillis() / 1000;
        Report report;
        RequestEntity requestEntity;
        do {
            requestEntity = RequestEntityUtil.init(BCSAPIEnum.REPORT_BY_ID, Report.class)
                .inlineVariables(PropertiesContext.get("customer_identity"), reportIdentity);
            report = (Report) HTTPRequest.build(requestEntity).get().getResponseEntity();
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        } while (!report.getState().equals(BCSState.COMPLETED.toString())
            && ((System.currentTimeMillis() / 1000) - initialTime) < WAIT_TIME);

        return (report.getState().equals(BCSState.COMPLETED.toString())) ? true : false;
    }
}
