package com.apriori.utils.enums;

public enum ScenarioStateEnum {
    PROCESSING("PROCESSING"),
    NOT_COSTED("NOT_COSTED"),
    COST_COMPLETE("COST_COMPLETE");

    private final String state;

    ScenarioStateEnum(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }
}
