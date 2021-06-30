package com.apriori.utils.enums;

public enum ScenarioStateEnum {
    PROCESSING("PROCESSING"),
    NO_COSTED("NO_COSTED");

    private final String state;

    ScenarioStateEnum(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }
}
