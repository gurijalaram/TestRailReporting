package com.apriori.pageobjects.reports.pages.view.enums;

public enum ExportSetEnum {
    TOP_LEVEL("top-level"),
    PISTON_ASSEMBLY("Piston Assembly"),
    MACHINING_DTC_DATASET("DTC_MachiningDataset");

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
