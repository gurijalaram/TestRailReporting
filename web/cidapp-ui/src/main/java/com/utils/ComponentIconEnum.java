package com.utils;

public enum ComponentIconEnum {

    ASSEMBLY("cubes"),
    COMPARISON("balance-scale"),
    PART("cube"),
    ROLLUP("paint-roller"),
    UNKNOWN("question-square");

    private final String icon;

    ComponentIconEnum(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return this.icon;
    }
}