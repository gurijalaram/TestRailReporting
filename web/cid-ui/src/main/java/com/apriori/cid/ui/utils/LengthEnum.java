package com.apriori.cid.ui.utils;

public enum LengthEnum {

    MILLIMETER("Millimeter"),
    CENTIMETER("Centimeter"),
    METER("Meter"),
    INCHES("Inch"),
    FOOT("Foot");

    private final String length;

    LengthEnum(String length) {
        this.length = length;
    }

    public String getLength() {
        return this.length;
    }
}
