package com.apriori.utils.enums;

public enum CurrencyEnum {

    CAD("CAD"),
    EUR("EUR"),
    GBP("GBP"),
    HKD("HKD"),
    USD("USD");

    private final String currency;

    CurrencyEnum(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return this.currency;
    }
}
