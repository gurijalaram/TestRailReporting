package com.apriori.css.api.enums;

public enum MaterialUtilizationMode {
    MACHINE_DEFAULT("MACHINE_DEFAULT"),
    RECTANGULAR("RECTANGULAR"),
    TRUE_PART("TRUE_PART");

    private final String materialUtilizationMode;

    MaterialUtilizationMode(String materialUtilMode) {
        this.materialUtilizationMode = materialUtilMode;
    }

    public String getMaterialUtilizationMode() {
        return materialUtilizationMode;
    }
}

