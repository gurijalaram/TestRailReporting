package com.apriori.enums;

public enum MassEnum {

    GRAM("g"),
    KILOGRAM("kg"),
    OUNCE("oz"),
    POUND("lb");

    private final String mass;

    MassEnum(String mass) {
        this.mass = mass;
    }

    public String getMass() {
        return this.mass;
    }

}
