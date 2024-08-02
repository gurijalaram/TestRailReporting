package com.apriori.bcs.api.enums;

public enum ReportTypeEnum {
    PART_REPORT("PART_REPORT", "DTC Component Summary"),
    BATCH_REPORT("BATCH_REPORT", "DFM Multiple Components Summary");

    private final String reportType;
    private final String reportName;

    ReportTypeEnum(String rType, String rName) {
        reportType = rType;
        reportName = rName;
    }

    public String getReportType() {
        return reportType;
    }

    public String getReportName() {
        return reportName;
    }
}
