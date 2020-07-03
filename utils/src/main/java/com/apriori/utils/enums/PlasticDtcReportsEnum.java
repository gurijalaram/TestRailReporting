package com.apriori.utils.enums;

public enum PlasticDtcReportsEnum {
    PLASTIC_DTC_REPORT("Plastic DTC"),
    PLASTIC_DTC_COMPARISON("Plastic DTC Comparison"),
    PLASTIC_DTC_DETAILS("Plastic DTC Details");

    private String reportName;

    PlasticDtcReportsEnum(String name) {
        this.reportName = name;
    }

    public String getReportName() {
        return this.reportName;
    }
}
