package com.apriori.css.api.enums;

public enum MaterialMode {
    CAD("CAD"),
    DIGITAL_FACTORY_DEFAULT("DIGITAL_FACTORY_DEFAULT"),
    MANUAL("MANUAL");

    private final String materialMode;

    MaterialMode(String materialMode) {
        this.materialMode = materialMode;
    }

    public String getMaterialMode() {
        return materialMode;
    }
}

