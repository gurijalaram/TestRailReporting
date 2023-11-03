package com.apriori.cid.ui.utils;

public enum ButtonTypeEnum {

    INCLUDE("include"),
    EXCLUDE("exclude"),
    EDIT("edit"),
    PUBLISH("publish"),
    DELETE("delete"),
    SET_INPUTS("set_inputs");

    private final String buttonType;

    ButtonTypeEnum(String buttonType) {
        this.buttonType = buttonType;
    }

    public String getButtonType() {
        return this.buttonType;
    }
}
