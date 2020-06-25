package com.apriori.utils.enums;

public enum SystemEnum {

    ENGLISH("ENGLISH"),
    METRIC("Metric");

    private final String unit;

    SystemEnum(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return this.unit;
    }
}
