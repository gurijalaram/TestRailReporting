package com.utils;

public enum CostStatusEnum {

    NONE("None"),
    NO_MATCH("No Match"),
    MATCH_ACTION_NEEDED("Match - Action Needed"),
    MATCH_COMPLETE("Match - Complete");

    private final String costStatus;

    CostStatusEnum(String costStatus) {
        this.costStatus = costStatus;
    }
}
