package com.utils;

public enum CisDisplayPreferenceEnum {

    UNITS("Units"),
    LENGTH("Length"),
    MASS("Mass"),
    TIME("Time"),
    DECIMAL_PLACE("Decimal Places"),
    LANGUAGE("Language"),
    CURRENCY("Currency"),
    EXCHANGE_RATE_TABLE("Exchange Rate Table");

    private final String fields;

    CisDisplayPreferenceEnum(String columns) {
        this.fields = columns;
    }

    public String getFields() {
        return this.fields;
    }
}