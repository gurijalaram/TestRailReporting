package com.apriori.pageobjects.reports.pages.view.enums;

public enum AssemblyReportsEnum {
    ASSEMBLY_COST_A4("Assembly Cost (A4)"),
    ASSEMBLY_COST_LETTER("Assembly Cost (Letter)"),
    ASSEMBLY_DETAILS("Assembly Details"),
    COMPONENT_COST("Component Cost"),
    SCENARIO_COMPARISON("Scenario Comparison");

    private String reportName;

    AssemblyReportsEnum(String name) {
        this.reportName = name;
    }

    /**
     * Gets report name from Enum
     * @return String
     */
    public String getReportName() {
        return this.reportName;
    }
}
