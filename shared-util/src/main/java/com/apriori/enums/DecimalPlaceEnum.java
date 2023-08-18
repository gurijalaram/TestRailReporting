package com.apriori.enums;

public enum DecimalPlaceEnum {

    ZERO("0"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6");

    private final String decimalPlaces;

    DecimalPlaceEnum(String decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public String getDecimalPlaces() {
        return this.decimalPlaces;
    }
}
