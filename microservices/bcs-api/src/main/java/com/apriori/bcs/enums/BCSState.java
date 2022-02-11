package com.apriori.bcs.enums;

public enum BCSState {
    CREATED("CREATED"),
    COSTING("COSTING"),
    READY_TO_COST("READY_TO_COST"),
    COSTED("COSTED"),
    PUBLISHING("PUBLISHING"),
    PUBLISHED("PUBLISHED"),
    EXPORTING("EXPORTING"),
    COMPLETED("COMPLETED"),
    CHECKED("CHECKED"),
    LOADING("LOADING"),
    ERRORED("ERRORED"),
    PROCESSING("PROCESSING"),
    REJECTED("REJECTED"),
    CANCELLED("CANCELLED");

    private final String state;

    BCSState(String st) {
        state = st;
    }

    public String getState() {
        return state;
    }
}
