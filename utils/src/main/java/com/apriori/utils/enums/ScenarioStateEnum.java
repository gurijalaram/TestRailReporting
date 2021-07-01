package com.apriori.utils.enums;

public enum ScenarioStateEnum {
    PROCESSING("PROCESSING"),
    NOT_COSTED("NOT_COSTED");

    private final String state;

    ScenarioStateEnum(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }
}
