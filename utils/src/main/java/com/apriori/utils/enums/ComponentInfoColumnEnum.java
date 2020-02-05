package com.apriori.utils.enums;

public enum ComponentInfoColumnEnum {

    QUANTITY("Qty"),
    PROCESS_GROUP("Process Group"),
    VPE("VPE"),
    LAST_SAVED("Last Saved"),
    LAST_COSTED("Last Costed"),
    CYCLE_TIME("Cycle Time (s)"),
    PER_PART_COST("Per Part Cost (USD)"),
    FULLY_BURDENED_COST("Fully Burdened Cost (USD)"),
    CAPITAL_INVESTMENT("Capital Investment");

    private final String columnName;

    ComponentInfoColumnEnum(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

}
