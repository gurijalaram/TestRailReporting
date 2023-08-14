package com.apriori.apibase.services.response.objects;



import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "DisplayPreferencesResponse.json")
public class DisplayPreferencesEntity {

    @JsonProperty
    private String currencyCode;

    @JsonProperty
    private Double currencyExchangeRate;

    @JsonProperty
    private UnitSystemSettingEntity unitVariantSettingsInfo;

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

    public UnitSystemSettingEntity getUnitSystemSetting() {
        return unitVariantSettingsInfo;
    }

    public DisplayPreferencesEntity setUnitSystemSetting(UnitSystemSettingEntity unitVariantSettingsInfo) {
        this.unitVariantSettingsInfo = unitVariantSettingsInfo;
        return this;
    }
}