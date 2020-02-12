package com.apriori.pageobjects.reports.pages.view.enums;

public enum RollupEnum {
    CASTING_DTC_ALL("All Casting (Initial)"),
    UC_CASTING_DTC_ALL("ALL CASTING (Initial)");

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
