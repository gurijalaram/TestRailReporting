package com.apriori.cid.ui.utils;

public enum CurrencyEnum {

    BRL("BRL (BRL Brazilain Real)"),
    CAD("CAD (Canadian Dollar)"),
    CNY("CNY (Chinese Renminbi Yuan)"),
    EUR("EUR (Euro)"),
    GBP("GBP (British Pound Sterling)"),
    HKD("HKD (Hong Kong Dollar)"),
    INR("INR (Indian Rupee)"),
    JPY("JPY (Japanese Yen)"),
    KRW("KRW (South Korean Won)"),
    MXN("MXN (Mexican Peso)"),
    TWD("TWD (Taiwanese NT Dollar)"),
    USD("USD (United States Dollar)");

    private final String currency;

    CurrencyEnum(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return this.currency;
    }
}
