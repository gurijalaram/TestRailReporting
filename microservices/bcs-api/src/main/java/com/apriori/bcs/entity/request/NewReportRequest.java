package com.apriori.bcs.entity.request;

public class NewReportRequest {
    private String externalId;
    private String reportFormat;
    private String reportType;
    private String reportTemplateIdentity;
    private String scopedIdentity;
    private ReportParameters reportParameters;

    public String getExternalId() {
        return this.externalId;
    }

    public NewReportRequest setExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public String getReportFormat() {
        return this.reportFormat;
    }

    public NewReportRequest setReportFormat(String reportFormat) {
        this.reportFormat = reportFormat;
        return this;
    }

    public String getReportTemplateIdentity() {
        return this.reportTemplateIdentity;
    }

    public NewReportRequest setReportTemplateIdentity(String reportTemplateIdentity) {
        this.reportTemplateIdentity = reportTemplateIdentity;
        return this;
    }

    public String getScopedIdentity() {
        return this.scopedIdentity;
    }

    public NewReportRequest setScopedIdentity(String scopedIdentity) {
        this.scopedIdentity = scopedIdentity;
        return this;
    }

    public String getReportType() {
        return this.reportType;
    }

    public NewReportRequest setReportType(String reportType) {
        this.reportType = reportType;
        return this;
    }

    public ReportParameters getReportParameters() {
        return this.reportParameters;
    }

    public NewReportRequest setReportParameters(ReportParameters reportParameters) {
        this.reportParameters = reportParameters;
        return this;
    }

}
