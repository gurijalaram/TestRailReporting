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
    private UnitSystemSettingSchema unitVariantSettingsInfo;

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

    public UnitSystemSettingSchema getUnitVariantSettingsInfo() {
        return unitVariantSettingsInfo;
    }

    public DisplayPreferencesEntity setUnitVariantSettingsInfo(UnitSystemSettingSchema unitVariantSettingsInfo) {
        this.unitVariantSettingsInfo = unitVariantSettingsInfo;
        return this;
    }
}