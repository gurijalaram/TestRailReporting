package com.apriori.enums;

public enum ReportsEnum {
    DTC_PART_SUMMARY("DTC Part Summary [CIR]"),
    PART_COST("Part Cost [SSR]");
    private String reportName;

    ReportsEnum(String name) {
        this.reportName = name;
    }

    /**Get report name
     *
     * @return String
     */
    public String getConnectorName() {
        return this.reportName;
    }
}
