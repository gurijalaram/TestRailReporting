package com.apriori.cid.ui.utils;

public enum MassEnum {

    GRAM("Gram"),
    KILOGRAM("Kilogram"),
    OUNCE("Ounce"),
    POUND("Pound");

    private final String mass;

    MassEnum(String mass) {
        this.mass = mass;
    }

    public String getMass() {
        return this.mass;
    }

}