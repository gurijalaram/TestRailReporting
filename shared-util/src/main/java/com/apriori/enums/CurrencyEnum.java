package com.apriori.enums;

public enum CurrencyEnum {

    CAD("CAD"),
    EUR("EUR"),
    GBP("GBP"),
    HKD("HKD"),
    USD("USD"),
    BRL("BRL"),
    CNY("CNY"),
    INR("INR"),
    JPY("JPY"),
    KRW("KRW"),
    MXN("MXN"),
    TWD("TWD");

    private final String currency;

    CurrencyEnum(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return this.currency;
    }
}
