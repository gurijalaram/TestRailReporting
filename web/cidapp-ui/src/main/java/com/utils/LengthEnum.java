package com.utils;

public enum LengthEnum {

    MILLIMETER("Millimeter"),
    CENTIMETER("Centimeter"),
    METER("Meter"),
    INCHES("Inch"),
    FEET("Foot");

    private final String length;

    LengthEnum(String length) {
        this.length = length;
    }

    public String getLength() {
        return this.length;
    }
}
