package com.apriori.edc.utils;

import com.apriori.TestUtil;
import com.apriori.edc.enums.EDCAPIReportsEnum;
import com.apriori.edc.models.request.ReportData;
import com.apriori.edc.models.request.ReportsRequest;
import com.apriori.edc.models.response.reports.ReportsResponse;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;

public class ReportsUtil extends TestUtil {

    /**
     * Create a new EDC Report
     *
     * @return the response object
     */
    public ResponseWrapper<ReportsResponse> createEDCReport(String reportType, String email, Integer numDays) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIReportsEnum.REPORTS, ReportsResponse.class)
                .body(ReportsRequest.builder()
                    .report(ReportData.builder()
                        .reportType(reportType)
                        .emailAddress(email)
                        .numberOfDaysToReport(numDays)
                        .build())
                    .build())
                .expectedResponseCode(HttpStatus.SC_CREATED);

        return HTTPRequest.build(requestEntity).post();
    }
}
