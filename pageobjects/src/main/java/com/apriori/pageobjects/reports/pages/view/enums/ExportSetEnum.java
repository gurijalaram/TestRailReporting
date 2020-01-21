package com.apriori.pageobjects.reports.pages.view.enums;

public enum ExportSetEnum {
    ALL_PLASTIC("All Plastic"),
    ALL_DEFAULT_SCENARIOS("All.Default Scenarios"),
    DESIGN_OUTLIER("design outlier"),
    DESIGN_OUTLIER_2("Design Outlier 2"),
    DTC_MACHINING_DATA("DTC Machining Data"),
    QA_TEST_1("qa test 1"),
    SHEET_METAL_DTC("Sheet Metal DTC"),
    TEST_DTC("Test DTC"),
    TOP_LEVEL("top-level");

    private String exportSetName;

    ExportSetEnum(String exportSetName) {
        this.exportSetName = exportSetName;
    }

    /**
     * Gets export set name
     * @return String
     */
    public String getExportSetName() {
        return this.exportSetName;
    }
}
