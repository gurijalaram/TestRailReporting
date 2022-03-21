package com.utils;

public enum ButtonTypeEnum {

    INCLUDE("include"),
    EXCLUDE("exclude");

    private final String buttonType;

    ButtonTypeEnum(String buttonType) {
        this.buttonType = buttonType;
    }

    public String getButtonType() {
        return this.buttonType;
    }
}
