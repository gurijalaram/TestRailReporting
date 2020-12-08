package com.apriori.utils.enums.reports;

public enum RollupEnum {
    UC_CASTING_DTC_ALL("ALL CASTING (Initial)"),
    DTC_MACHINING_DATASET("DTC_MACHININGDATASET (Initial)"),
    ROLL_UP_A("ROLL-UP A (Initial)"),
    SHEET_METAL_DTC("SHEET METAL DTC (Initial)");

    private final String rollupName;

    RollupEnum(String name) {
        this.rollupName = name;
    }

    /**
     * Get rollup name from enum
     */
    public String getRollupName() {
        return this.rollupName;
    }
}
