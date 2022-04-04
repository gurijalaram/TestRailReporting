package com.utils;

public enum ComponentStateEnum {

    NOT_COSTED("circle-minus"),
    PROCESSING_PUBLISH_ACTION("gear"),
    COST_INCOMPLETE("triangle-exclamation");

    private final String componentStatus;

    ComponentStateEnum(String componentStatus) {
        this.componentStatus = componentStatus;
    }

    public String getComponentStatus() {
        return this.componentStatus;
    }
}
