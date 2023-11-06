package com.apriori.edc.ui.utils;

public enum RightClickOptionEnum {
    DELETE("Delete"),
    EXPORT("Export");

    private final String option;

    RightClickOptionEnum(String option) {
        this.option = option;
    }

    public String getOption() {
        return this.option;
    }
}
