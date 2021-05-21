package com.apriori.bcs.controller;

import com.apriori.bcs.entity.request.NewReportRequest;
import com.apriori.bcs.entity.response.Report;
import com.apriori.bcs.entity.response.ReportExport;
import com.apriori.bcs.entity.response.ReportTemplates;
import com.apriori.bcs.entity.response.ReportType;
import com.apriori.bcs.entity.response.ReportTypes;
import com.apriori.bcs.entity.response.Reports;
import com.apriori.bcs.utils.Constants;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class ReportResources extends CisBase {
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

    public static <T> ResponseWrapper<T> getReportRepresentation() {
        return getReportRepresentation(null);
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

    public static <T> ResponseWrapper<T> getReportTypes() {
        String url = String.format(getReportTypesUrl(), "");
        return GenericRequestUtil.get(
                RequestEntity.init(url, ReportTypes.class),
                new RequestAreaApi()
        );
    }

    public static <T> ResponseWrapper<T> getSpecificReportType() {
        String url = String.format(getReportTypesUrl(), "/" + Constants.getCisReportTypeIdentity());
        return GenericRequestUtil.get(
                RequestEntity.init(url, ReportType.class),
                new RequestAreaApi()
        );
    }

    public static Report createReport(Object obj) {
        return createReport(obj, null);
    }

    public static Report createReport(Object obj, String identity) {
        NewReportRequest nrr = (NewReportRequest)obj;

        if (identity != null) {
            nrr.setScopedIdentity(identity);
        } else {
            nrr.setScopedIdentity(Constants.getCisPartIdentity());
        }

        String url = String.format(getCisUrl(), endpointReports);
        List<String> partsIdentities = new ArrayList<>();
        partsIdentities.add(Constants.getCisPartIdentity());
        NewReportRequest body = new NewReportRequest();
        body
                .setExternalId(String.format(nrr.getExternalId(), System.currentTimeMillis()))
                .setReportFormat(nrr.getReportFormat())
                .setReportType(nrr.getReportType())
                .setReportTemplateIdentity(nrr.getReportTemplateIdentity())
                .setScopedIdentity(nrr.getScopedIdentity())
                .setReportParameters(nrr.getReportParameters());

        return (Report) GenericRequestUtil.post(
                RequestEntity.init(url, Report.class)
                        .setBody(body)
                        .setStatusCode(HttpStatus.SC_CREATED),
                new RequestAreaApi()
        ).getResponseEntity();
    }

    public static <T> ResponseWrapper<T> getReportTemplates() {
        String url = String.format(getCisUrl(), endpointReportTemplate);
        return GenericRequestUtil.get(
                RequestEntity.init(url, ReportTemplates.class),
                new RequestAreaApi()
        );
    }
}
