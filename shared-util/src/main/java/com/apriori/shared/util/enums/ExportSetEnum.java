package com.apriori.shared.util.enums;

public enum ExportSetEnum {
    TOP_LEVEL("- - - 0 0 0-top-level-0"),
    TOP_LEVEL_MULTI_VPE("- - - 0 0 0-top-level-multi-vpe-0"),
    PISTON_ASSEMBLY("- - - 0 0 0-piston-assembly-0"),
    MACHINING_DTC_DATASET("- - - 0 0 0-dtc-machiningdataset-0"),
    CASTING_DTC("- - - 0 0 0-dtc-casting-0"),
    ROLL_UP_A("- - - 0 0 0-roll-up-a-0"),
    SUB_SUB_ASM("- - - 0 0 0-sub-sub-asm-0"),
    SHEET_METAL_DTC("- - - 0 0 0-sheet-metal-dtc-0"),
    COST_OUTLIER_THRESHOLD_ROLLUP("- - - 0 0 0-cost-outlier-threshold-rollup-0"),
    ALL_PG_CURRENT("- - - 0 0 0-All-PG-Current-0"),
    ALL_PG_NEW("- - - 0 0 0-All-PG-New-0");

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