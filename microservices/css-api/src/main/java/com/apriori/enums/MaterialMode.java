package com.apriori.enums;

public enum MaterialMode {
    CAD("CAD"),
    DIGITAL_FACTORY_DEFAULT("DIGITAL_FACTORY_DEFAULT"),
    MANUAL("MANUAL");

    private final String materialMode;

    MaterialMode(String mMode) {
        this.materialMode = mMode;
    }

    public String getMaterialMode() {
        return materialMode;
    }
}

