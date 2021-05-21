package com.apriori.bcs.entity.response;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "ReportExportSchema.json")
public class ReportExport { 
    private String reportIdentity;
    private String fileName;
    private String contentType;
    private String encodedContent;
    private ReportExport response;

    public String getReportIdentity() {
        return this.reportIdentity;
    }

    public ReportExport  setReportIdentity(String reportIdentity) {
        this.reportIdentity = reportIdentity;
        return this;
    }

    public String getFileName() {
        return this.fileName;
    }

    public ReportExport  setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getContentType() {
        return this.contentType;
    }

    public ReportExport  setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public String getEncodedContent() {
        return this.encodedContent;
    }

    public ReportExport  setEncodedContent(String encodedContent) {
        this.encodedContent = encodedContent;
        return this;
    }

    public ReportExport getResponse() {
        return this.response;
    }

    public ReportExport  setResponse(ReportExport response) {
        this.response = response;
        return this;
    }
}
