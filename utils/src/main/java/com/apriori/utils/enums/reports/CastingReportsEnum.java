package com.apriori.utils.enums.reports;

public enum CastingReportsEnum {
    CASTING_DTC("Casting DTC"),
    CASTING_DTC_COMPARISON("Casting DTC Comparison"),
    CASTING_DTC_DETAILS("Casting DTC Details");

    private String reportName;

    CastingReportsEnum(String name) {
        this.reportName = name;
    }

    /**
     * Get report name from enum
     *
     * @return String
     */
    public String getReportName() {
        return this.reportName;
    }
}
