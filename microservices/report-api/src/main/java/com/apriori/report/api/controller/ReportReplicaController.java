package com.apriori.report.api.controller;

import com.apriori.report.api.enums.ReportAPIEnum;
import com.apriori.report.api.models.Params;
import com.apriori.report.api.models.Report;
import com.apriori.report.api.models.ReportRequest;
import com.apriori.report.api.models.Settings;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.QueryParams;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpStatus;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ReportReplicaController {

    /**
     * Calls an API with GET verb
     *
     * @return report object
     */
    public Report getRequestStatus(String customerId, String executionId) {
        final long START_TIME = System.currentTimeMillis() / 1000;
        ResponseWrapper<Report> reportResponse;
        int WAIT_TIME = 20;

        do {
            try {
                int POLL_TIME = 500;
                TimeUnit.MILLISECONDS.sleep(POLL_TIME);

                final RequestEntity requestEntity = RequestEntityUtil_Old.init(ReportAPIEnum.get_REPORT_RESULTS, Report.class)
                    .inlineVariables(customerId, executionId)
                    .expectedResponseCode(HttpStatus.SC_OK)
                    .headers(new QueryParams().use("x-token", "eM1PPjIBYc23eKotQdvUy8ygrEWkz7KC7ATFEYDF")
                        .use("x-api-key", "eYxPlF5iwP2yZN3JajrbU7Ey2GR784ZL3lQhvyGK"));

                reportResponse = HTTPRequest.build(requestEntity).get();

                if (reportResponse != null && reportResponse.getResponseEntity().getStatus().equalsIgnoreCase("succeeded")) {
                    return reportResponse.getResponseEntity();
                }

            } catch (InterruptedException e) {
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
        while (((System.currentTimeMillis() / 1000) - START_TIME) < WAIT_TIME);

        throw new RuntimeException("Report status is not correct");
    }

// TODO: 29/02/2024 POST first then take id from response then GET then verify csv fields against some newly created object

    /**
     * Calls an API with POST verb
     *
     * @return report object
     */
    public Report postExecuteReport(String customerId) {
        final RequestEntity requestEntity = RequestEntityUtil_Old.init(ReportAPIEnum.post_CREATE_REPORT, Report.class)
            .inlineVariables(customerId)
            .headers(new QueryParams().use("x-token", "eM1PPjIBYc23eKotQdvUy8ygrEWkz7KC7ATFEYDF")
                .use("x-api-key", "eYxPlF5iwP2yZN3JajrbU7Ey2GR784ZL3lQhvyGK"))
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
}
