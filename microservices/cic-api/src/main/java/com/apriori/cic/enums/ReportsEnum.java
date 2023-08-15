package com.apriori.cic.enums;

public enum ReportsEnum {
    DTC_PART_SUMMARY("DTC Part Summary [CIR]"),
    PART_COST("Part Cost [SSR]"),
    DTC_COMPONENT_SUMMARY("DTC Component Summary [CIR]"),
    DTC_MULTIPLE_COMPONENT_SUMMARY("DFM Multiple Components Summary [CIR]");

    private String reportName;

    ReportsEnum(String name) {
        this.reportName = name;
    }

    /**Get report name
     *
     * @return String
     */
    public String getReportName() {
        return this.reportName;
    }
}
