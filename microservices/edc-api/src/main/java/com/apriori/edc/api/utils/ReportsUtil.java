package com.apriori.edc.api.utils;

import com.apriori.edc.api.enums.EDCAPIReportsEnum;
import com.apriori.edc.api.models.request.ReportData;
import com.apriori.edc.api.models.request.ReportsRequest;
import com.apriori.edc.api.models.response.reports.ReportsResponse;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestUtil;

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
