package com.utils;

public enum LengthEnum {

    MILLIMETER("Millimeter"),
    CENTIMETER("Centimeter"),
    METER("Meter");

    private final String length;

    LengthEnum(String length) {
        this.length = length;
    }

    public String getLength() {
        return this.length;
    }
}
