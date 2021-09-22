package com.apriori.bcs.controller;

import com.apriori.bcs.entity.request.NewReportRequest;
import com.apriori.bcs.entity.response.Report;
import com.apriori.bcs.entity.response.ReportExport;
import com.apriori.bcs.entity.response.ReportTemplates;
import com.apriori.bcs.entity.response.Reports;
import com.apriori.bcs.enums.BCSAPIEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;
import com.apriori.utils.properties.PropertiesContext;

import org.apache.http.HttpStatus;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

// TODO ALL: test it
public class ReportResources {

    public static <T> ResponseWrapper<T> getReports() {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.GET_REPORTS, Reports.class);

        return HTTP2Request.build(requestEntity).get();
    }

    public static <T> ResponseWrapper<T> getReportRepresentation(String identity) {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.GET_REPORT_BY_ID, Report.class)
            .inlineVariables(identity);

        return HTTP2Request.build(requestEntity).get();
    }

    public static <T> ResponseWrapper<T> exportReport(String identity) {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.GET_REPORT_EXPORT_BY_ID, ReportExport.class)
            .inlineVariables(identity);

        return HTTP2Request.build(requestEntity).get();
    }

    public static <T> ResponseWrapper<T> createReport(NewReportRequest nrr, int status, Class klass) {
        List<String> partsIdentities = new ArrayList<>();
        partsIdentities.add(PropertiesContext.get("${env}.bcs.part_identity"));
        NewReportRequest body = new NewReportRequest();
        body.setExternalId(String.format(nrr.getExternalId(), System.currentTimeMillis()))
                .setReportFormat(nrr.getReportFormat())
                .setReportType(nrr.getReportType())
                .setReportTemplateIdentity(nrr.getReportTemplateIdentity())
                .setScopedIdentity(nrr.getScopedIdentity())
                .setReportParameters(nrr.getReportParameters());

        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.POST_REPORTS, klass)
            .body(body);

        ResponseWrapper<T> responseWrapper = HTTP2Request.build(requestEntity).post();

        Assert.assertEquals(status, responseWrapper.getStatusCode());

        return responseWrapper;
    }

    public static <T> ResponseWrapper<T> createReport(NewReportRequest nrr) {
        return createReport(nrr, HttpStatus.SC_CREATED, Report.class);
    }

    public static <T> ResponseWrapper<T> getReportTemplates() {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.GET_REPORT_TEMPLATES, ReportTemplates.class);

        return HTTP2Request.build(requestEntity).get();
    }

    public static <T> ResponseWrapper<T> getReportTemplatesPartReport() {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.GET_REPORT_TEMPLATES_PART_REPORT, ReportTemplates.class)
            .urlEncodingEnabled(false);

        return HTTP2Request.build(requestEntity).get();
    }
}
