package com.apriori.enums;

public enum ExportSetEnum {
    TOP_LEVEL("- - - 0 0 0-top-level"),
    TOP_LEVEL_MULTI_VPE("- - - 0 0 0-top-level-multi-vpe"),
    PISTON_ASSEMBLY("- - - 0 0 0-piston-assembly"),
    MACHINING_DTC_DATASET("- - - 0 0 0-dtc-machiningdataset"),
    CASTING_DTC("- - - 0 0 0-dtc-casting"),
    ROLL_UP_A("- - - 0 0 0-roll-up-a"),
    SUB_SUB_ASM("- - - 0 0 0-sub-sub-asm"),
    SHEET_METAL_DTC("- - - 0 0 0-sheet-metal-dtc"),
    COST_OUTLIER_THRESHOLD_ROLLUP("- - - 0 0 0-cost-outlier-threshold-rollup");

    private final String exportSetName;

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