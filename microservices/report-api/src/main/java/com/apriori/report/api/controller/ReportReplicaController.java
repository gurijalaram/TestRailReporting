package com.apriori.report.api.controller;

import com.apriori.report.api.enums.ReportAPIEnum;
import com.apriori.report.api.models.Params;
import com.apriori.report.api.models.Report;
import com.apriori.report.api.models.ReportRequest;
import com.apriori.report.api.models.Settings;
import com.apriori.shared.util.file.InitFileData;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.AwsParameterStoreUtil;
import com.apriori.shared.util.http.utils.QueryParams;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.URLFileUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.HttpStatus;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ReportReplicaController {

    final String TOKEN = AwsParameterStoreUtil.getSystemParameter("/qa-test-reporting-api/clients/6C1F8C1D4D75/token");
    final String API_KEY = AwsParameterStoreUtil.getSystemParameter("/qa-test-reporting-api/clients/6C1F8C1D4D75/api-key");

    /**
     * Calls an API with GET verb
     *
     * @return report object
     */
    public Report getReportStatus(String customerId, String executionId) {
        final long START_TIME = System.currentTimeMillis() / 1000;
        ResponseWrapper<Report> reportResponse;
        int WAIT_TIME = 20;

        do {
            try {
                int POLL_TIME = 500;
                TimeUnit.MILLISECONDS.sleep(POLL_TIME);

                final RequestEntity requestEntity = RequestEntityUtil_Old.init(ReportAPIEnum.REPORT_STATUS, Report.class)
                    .inlineVariables(customerId, executionId)
                    .expectedResponseCode(HttpStatus.SC_OK)
                    .headers(new QueryParams().use("x-token",TOKEN)
                        .use("x-api-key", API_KEY));

                reportResponse = HTTPRequest.build(requestEntity).get();

                if (reportResponse != null && reportResponse.getResponseEntity().getStatus().equalsIgnoreCase("succeeded")) {
                    return reportResponse.getResponseEntity();
                }

            } catch (InterruptedException e) {
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        } while (((System.currentTimeMillis() / 1000) - START_TIME) < WAIT_TIME);

        throw new RuntimeException("Report status is not correct");
    }

    /**
     * Calls an API with POST verb
     *
     * @return report object
     */
    public Report postExecuteReport(String customerId) {
        final RequestEntity requestEntity = RequestEntityUtil_Old.init(ReportAPIEnum.REPORT_EXECUTE, Report.class)
            .inlineVariables(customerId)
            .headers(new QueryParams().use("x-token", TOKEN)
                .use("x-api-key", API_KEY))
            .body(ReportRequest.builder()
                .params(Collections.singletonList(
                    Params.builder()
                        .name("last_up")
                        .value("2023-10-08T00:00:00Z")
                        .type("timestamp")
                        .build()))
                .settings(Settings.builder()
                    .decimalPrecision(6)
                    .build())
                .build())
            .expectedResponseCode(HttpStatus.SC_ACCEPTED);

        ResponseWrapper<Report> reportResponse = HTTPRequest.build(requestEntity).post();
        return reportResponse.getResponseEntity();
    }

    /**
     * Downloads and reads a report
     *
     * @param url       - the url
     * @param separator - the separator in the csv
     * @param klass     - the class bean
     */
    public <T> List<T> downloadReadReport(String url, char separator, Class<T> klass) {
        final String filename = StringUtils.substringBetween(url, "/reports/", "?AWSAccessKeyId");

        new URLFileUtil().downloadFileFromURL(url, filename);
        return new InitFileData().csvReader(new File(filename), separator, klass);
    }
}
