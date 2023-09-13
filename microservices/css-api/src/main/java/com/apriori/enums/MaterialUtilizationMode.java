package com.apriori.enums;

public enum MaterialUtilizationMode {
    MACHINE_DEFAULT("MACHINE_DEFAULT"),
    RECTANGULAR("RECTANGULAR"),
    TRUE_PART("TRUE_PART");

    private final String materialUtilizationMode;

    MaterialUtilizationMode(String mUtilMode) {
        this.materialUtilizationMode = mUtilMode;
    }

    public String getMaterialUtilizationMode() {
        return materialUtilizationMode;
    }
}

