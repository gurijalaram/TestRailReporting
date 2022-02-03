package com.apriori.utils.enums;

public enum ScenarioStateEnum {
    PROCESSING("PROCESSING"),
    PROCESSING_FAILED("PROCESSING_FAILED"),
    NOT_COSTED("NOT_COSTED"),
    COST_COMPLETE("COST_COMPLETE"),
    COST_INCOMPLETE("COST_INCOMPLETE"),
    COSTING_FAILED("COSTING_FAILED"),
    COST_UP_TO_DATE("COST_UP_TO_DATE"),
    COSTING("COSTING");

    private final String state;

    ScenarioStateEnum(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }
}
