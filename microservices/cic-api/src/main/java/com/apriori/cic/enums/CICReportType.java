package com.apriori.cic.enums;

public enum CICReportType {
    EMAIL("EMAIL"),
    PLM_WRITE("PLM_WRITE");

    private final String reportType;

    CICReportType(String type) {
        reportType = type;
    }

    public String getReportType() {
        return reportType;
    }
}
