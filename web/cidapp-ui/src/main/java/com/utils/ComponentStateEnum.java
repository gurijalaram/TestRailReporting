package com.utils;

public enum ComponentStateEnum {

    NOT_COSTED("circle-minus"),
    PROCESSING_PUBLISH_ACTION("gear"),
    COST_INCOMPLETE("triangle-exclamation"),
    COSTING_FAILED("circle-xmark");

    private final String componentState;

    ComponentStateEnum(String componentStatus) {
        this.componentState = componentStatus;
    }

    public String getComponentState() {
        return this.componentState;
    }
}
