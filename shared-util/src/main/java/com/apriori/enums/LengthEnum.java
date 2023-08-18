package com.apriori.enums;

public enum LengthEnum {

    MILLIMETER("mm"),
    CENTIMETER("cm"),
    METER("m"),
    INCHES("in"),
    FEET("ft");

    private final String length;

    LengthEnum(String length) {
        this.length = length;
    }

    public String getLength() {
        return this.length;
    }
}
