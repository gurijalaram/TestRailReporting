package com.apriori.trr.api.testrail.enums;

/*
 Enum for Column index for automation type
 */
public enum ReportType {
    COVERAGE_REPORT("TEST COVERAGE REPORT"),
    COVERAGE_PROJECT_TEAM("COVERAGE BY PROJECT TEAM");

    private final String reportName;

    ReportType(String reportTitle) {
        this.reportName = reportTitle;
    }

    public String getReportName() {
        return this.reportName;
    }
}
