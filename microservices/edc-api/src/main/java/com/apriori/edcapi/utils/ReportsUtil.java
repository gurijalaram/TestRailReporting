package com.apriori.edcapi.utils;

import com.apriori.TestUtil;
import com.apriori.edcapi.entity.enums.EDCAPIReportsEnum;
import com.apriori.edcapi.entity.request.ReportData;
import com.apriori.edcapi.entity.request.ReportsRequest;
import com.apriori.edcapi.entity.response.reports.ReportsResponse;
import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
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
