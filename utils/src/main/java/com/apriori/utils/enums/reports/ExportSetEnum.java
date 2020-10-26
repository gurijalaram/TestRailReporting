package com.apriori.utils.enums.reports;

public enum ExportSetEnum {
    TOP_LEVEL("--01-top-level"),
    PISTON_ASSEMBLY("---01-piston-assembly"),
    MACHINING_DTC_DATASET("---01-dtc-machiningdataset"),
    CASTING_DTC("---01-dtc-casting"),
    ROLL_UP_A("---01-roll-up-a");

    private String exportSetName;

    ExportSetEnum(String exportSetName) {
        this.exportSetName = exportSetName;
    }

    /**
     * Gets export set name
     *
     * @return String
     */
    public String getExportSetName() {
        return this.exportSetName;
    }
}
