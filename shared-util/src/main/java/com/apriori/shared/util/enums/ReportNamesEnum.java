package com.apriori.shared.util.enums;

public enum ReportNamesEnum {
    ASSEMBLY_COST_A4("Assembly Cost (A4)"),
    ASSEMBLY_COST_LETTER("Assembly Cost (Letter)"),
    ASSEMBLY_DETAILS("Assembly Details"),
    CASTING_DTC("Casting DTC"),
    CASTING_DTC_COMPARISON("Casting DTC Comparison"),
    CASTING_DTC_DETAILS("Casting DTC Details"),
    COMPONENT_COST("Component Cost"),
    COMPONENT_COST_INTERNAL_USE("Component Cost Internal Use"),
    COST_OUTLIER_IDENTIFICATION("Cost Outlier Identification"),
    COST_OUTLIER_IDENTIFICATION_DETAILS("Cost Outlier Identification Details"),
    CYCLE_TIME_VALUE_TRACKING("Cycle Time Value Tracking"),
    CYCLE_TIME_VALUE_TRACKING_DETAILS("Cycle Time Value Tracking Details"),
    DESIGN_OUTLIER_IDENTIFICATION("Design Outlier Identification"),
    DESIGN_OUTLIER_IDENTIFICATION_DETAILS("Design Outlier Identification Details"),
    DTC_PART_SUMMARY("DTC Part Summary"),
    MACHINING_DTC("Machining DTC"),
    MACHINING_DTC_DETAILS("Machining DTC Details"),
    MACHINING_DTC_COMPARISON("Machining DTC Comparison"),
    PLASTIC_DTC("Plastic DTC"),
    PLASTIC_DTC_COMPARISON("Plastic DTC Comparison"),
    PLASTIC_DTC_DETAILS("Plastic DTC Details"),
    SCENARIO_COMPARISON("Scenario Comparison"),
    SHEET_METAL_DTC("Sheet Metal DTC"),
    SHEET_METAL_DTC_COMPARISON("Sheet Metal DTC Comparison"),
    SHEET_METAL_DTC_DETAILS("Sheet Metal DTC Details"),
    TARGET_AND_QUOTED_COST_TREND("Target and Quoted Cost Trend"),
    TARGET_AND_QUOTED_COST_VALUE_TRACKING("Target and Quoted Cost Value Tracking"),
    TARGET_AND_QUOTED_COST_VALUE_TRACKING_DETAILS("Target and Quoted Cost Value Tracking Details");

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