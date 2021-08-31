package com.apriori.bcs.controller;

import com.apriori.bcs.entity.request.NewReportRequest;
import com.apriori.bcs.entity.response.Report;
import com.apriori.bcs.entity.response.ReportExport;
import com.apriori.bcs.entity.response.ReportTemplates;
import com.apriori.bcs.entity.response.Reports;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;

import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class ReportResources extends BcsBase {
    private static final String endpointReports = "reports";
    private static final String endpointReportRepresentation = "reports/%s";
    private static final String endpointExportReport = "reports/%s/export";
    private static final String endpointReportTemplate = "report-templates";

    public static <T> ResponseWrapper<T> getReports() {
        String url = String.format(getCisUrl(), endpointReports);
        return GenericRequestUtil.get(
                RequestEntity.init(url, Reports.class),
                new RequestAreaApi()
        );
    }

    public static <T> ResponseWrapper<T> getReportRepresentation(String identity) {
        String url = String.format(getCisUrl(), String.format(endpointReportRepresentation, identity));
        return GenericRequestUtil.get(
                RequestEntity.init(url, Report.class),
                new RequestAreaApi()
        );
    }

    public static <T> ResponseWrapper<T> exportReport(String identity) {
        String url = String.format(getCisUrl(), String.format(endpointExportReport,
                identity));
        return GenericRequestUtil.get(
                RequestEntity.init(url, ReportExport.class),
                new RequestAreaApi()
        );
    }

    public static <T> ResponseWrapper<T> createReport(NewReportRequest nrr, Integer status, Class klass) {
        String url = String.format(getCisUrl(), endpointReports);
        List<String> partsIdentities = new ArrayList<>();
        partsIdentities.add(PropertiesContext.get("${env}.bcs.part_identity"));
        NewReportRequest body = new NewReportRequest();
        body
                .setExternalId(String.format(nrr.getExternalId(), System.currentTimeMillis()))
                .setReportFormat(nrr.getReportFormat())
                .setReportType(nrr.getReportType())
                .setReportTemplateIdentity(nrr.getReportTemplateIdentity())
                .setScopedIdentity(nrr.getScopedIdentity())
                .setReportParameters(nrr.getReportParameters());

        return GenericRequestUtil.post(
                RequestEntity.init(url, klass)
                        .setBody(body)
                        .setStatusCode(status),
                new RequestAreaApi()
        );
    }

    public static <T> ResponseWrapper<T> createReport(NewReportRequest nrr) {
        return createReport(nrr, HttpStatus.SC_CREATED, Report.class);
    }

    public static <T> ResponseWrapper<T> getReportTemplates() {
        return getReportTemplates(null);
    }

    public static <T> ResponseWrapper<T> getReportTemplates(String queries) {
        if (queries != null) {
            queries = "&" + queries;
        } else {
            queries = "";
        }

        String url = String.format(getCisUrl(), endpointReportTemplate).concat(queries);
        return GenericRequestUtil.get(
                RequestEntity.init(url, ReportTemplates.class),
                new RequestAreaApi()
        );
    }
}
