package com.apriori.bcs.enums;

public enum BCSState {
    COMPLETED("COMPLETED"),
    READY_TO_COST("READY_TO_COST"),
    CHECKED("CHECKED"),
    LOADING("LOADING"),
    PUBLISHING("PUBLISHING"),
    COSTED("COSTED"),
    CREATED("CREATED"),
    COSTING("COSTING"),
    ERRORED("ERRORED"),
    PROCESSING("PROCESSING"),
    REJECTED("REJECTED"),
    CANCELED("CANCELLED");

    private final String state;

    BCSState(String st) {
        state = st;
    }

    public String getState() {
        return state;
    }
}
