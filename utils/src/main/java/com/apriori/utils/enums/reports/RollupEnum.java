package com.apriori.utils.enums.reports;

public enum RollupEnum {
    UC_CASTING_DTC_ALL("ALL CASTING (Initial)"),
    ROLL_UP_A("ROLL-UP A (Initial)");

    private String rollupName;

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
