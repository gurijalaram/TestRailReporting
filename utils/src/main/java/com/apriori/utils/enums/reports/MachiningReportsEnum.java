package com.apriori.utils.enums.reports;

public enum MachiningReportsEnum {
    MACHINING_DTC("Machining DTC"),
    MACHINING_DTC_DETAILS("Machining DTC Details"),
    MACHINING_DTC_COMPARISON("Machining DTC Comparison");

    private String reportName;

    MachiningReportsEnum(String name) {
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
