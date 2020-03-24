package com.apriori.apibase.http.builder.common.response.common;

import com.apriori.apibase.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "DisplayPreferencesSchema.json")
public class DisplayPreferencesEntity {

    @JsonProperty
    private String currencyCode;

    @JsonProperty
    private Double currencyExchangeRate;

    @JsonProperty
    private Boolean isSystemUnits;

    @JsonProperty
    private String decimalPlaces;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public DisplayPreferencesEntity setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    public Double getCurrencyExchangeRate() {
        return currencyExchangeRate;
    }

    public DisplayPreferencesEntity setCurrencyExchangeRate(Double currencyExchangeRate) {
        this.currencyExchangeRate = currencyExchangeRate;
        return this;
    }

    public Boolean getSystemUnits() {
        return isSystemUnits;
    }

    public DisplayPreferencesEntity setSystemUnits(Boolean systemUnits) {
        isSystemUnits = systemUnits;
        return this;
    }

    public String getDecimalPlaces() {
        return decimalPlaces;
    }

    public DisplayPreferencesEntity setDecimalPlaces(String decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
        return this;
    }
}