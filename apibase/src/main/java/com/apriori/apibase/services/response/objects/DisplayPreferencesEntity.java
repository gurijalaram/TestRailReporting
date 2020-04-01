package com.apriori.apibase.services.response.objects;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "DisplayPreferencesSchema.json")
public class DisplayPreferencesEntity {

    @JsonProperty
    private String currencyCode;

    @JsonProperty
    private Double currencyExchangeRate;

    @JsonProperty
    private Boolean isSystemUnits;

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
}