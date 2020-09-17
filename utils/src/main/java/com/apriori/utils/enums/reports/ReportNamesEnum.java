package com.apriori.utils.enums.reports;

public enum ReportNamesEnum {
    ASSEMBLY_COST_A4("Assembly Cost (A4)"),
    ASSEMBLY_COST_LETTER("Assembly Cost (Letter)"),
    ASSEMBLY_DETAILS("Assembly Details"),
    COMPONENT_COST("Component Cost"),
    SCENARIO_COMPARISON("Scenario Comparison"),
    CASTING_DTC("Casting DTC"),
    CASTING_DTC_COMPARISON("Casting DTC Comparison"),
    CASTING_DTC_DETAILS("Casting DTC Details"),
    DTC_PART_SUMMARY("DTC Part Summary"),
    MACHINING_DTC("Machining DTC"),
    MACHINING_DTC_DETAILS("Machining DTC Details"),
    MACHINING_DTC_COMPARISON("Machining DTC Comparison"),
    PLASTIC_DTC("Plastic DTC"),
    PLASTIC_DTC_COMPARISON("Plastic DTC Comparison"),
    PLASTIC_DTC_DETAILS("Plastic DTC Details");

    private final String reportName;

    ReportNamesEnum(String name) {
        this.reportName = name;
    }

    /**
     * Gets report name from Enum
     *
     * @return String
     */
    public String getReportName() {
        return this.reportName;
    }
}
