package com.apriori.bcs.utils;

import com.apriori.apibase.services.bcs.objects.Report;
import com.apriori.apibase.services.bcs.objects.ReportExport;
import com.apriori.apibase.services.bcs.objects.ReportType;
import com.apriori.apibase.services.bcs.objects.ReportTypes;
import com.apriori.apibase.services.bcs.objects.Reports;
import com.apriori.apibase.services.bcs.objects.requests.NewReportRequest;
import com.apriori.utils.constants.CommonConstants;
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

    public static <T> ResponseWrapper<T> getSpecificReportType(String reportTypeIdentity) {
        String url = String.format(getReportTypesUrl(), "/" + reportTypeIdentity);
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
            nrr.addPart(identity);
        } else {
            nrr.addPart(CommonConstants.getCisPartIdentity());
        }

        String url = String.format(getCisUrl(), endpointReports);
        List<String> partsIdentities = new ArrayList<>();
        partsIdentities.add(CommonConstants.getCisPartIdentity());
        NewReportRequest body = new NewReportRequest();
        body
                .setExternalId(String.format(nrr.getExternalId(), System.currentTimeMillis()))
                .setReportFormat(nrr.getReportFormat())
                .setReportType(nrr.getReportType())
                .setRoundToNearest(nrr.getRoundToNearest())
                .setPartIdentities(nrr.getPartIdentities());

        return (Report) GenericRequestUtil.post(
                RequestEntity.init(url, Report.class)
                        .setBody(body)
                        .setStatusCode(HttpStatus.SC_CREATED),
                new RequestAreaApi()
        ).getResponseEntity();
    }
}