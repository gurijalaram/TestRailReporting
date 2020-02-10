package com.apriori.pageobjects.reports.pages.view.enums;

public enum ExportSetEnum {
    TOP_LEVEL("top-level"),
    PISTON_ASSEMBLY("Piston Assembly"),
    CASTING_DTC("DTC_Casting");

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
