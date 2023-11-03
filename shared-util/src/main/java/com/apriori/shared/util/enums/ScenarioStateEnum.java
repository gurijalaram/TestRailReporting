package com.apriori.shared.util.enums;

import java.util.EnumSet;

public enum ScenarioStateEnum {
    PROCESSING("PROCESSING"),
    PROCESSING_FAILED("PROCESSING_FAILED"),
    NOT_COSTED("NOT_COSTED"),
    COST_COMPLETE("COST_COMPLETE"),
    COST_INCOMPLETE("COST_INCOMPLETE"),
    COSTING_FAILED("COSTING_FAILED"),
    COST_UP_TO_DATE("COST_UP_TO_DATE"),
    COSTING("COSTING");

    public static EnumSet<ScenarioStateEnum> transientState = EnumSet.of(PROCESSING, COSTING);
    public static EnumSet<ScenarioStateEnum> terminalState = EnumSet.of(PROCESSING_FAILED, NOT_COSTED, COST_COMPLETE, COST_INCOMPLETE, COSTING_FAILED, COST_UP_TO_DATE);
    private final String state;

    ScenarioStateEnum(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }
}
