package com.apriori.edcapi.utils;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.edcapi.entity.enums.EDCAPIReportsEnum;
import com.apriori.edcapi.entity.request.ReportData;
import com.apriori.edcapi.entity.request.ReportsRequest;
import com.apriori.edcapi.entity.response.reports.ReportsResponse;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

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
