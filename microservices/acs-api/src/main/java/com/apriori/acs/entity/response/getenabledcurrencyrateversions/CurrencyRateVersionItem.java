package com.apriori.acs.entity.response.getenabledcurrencyrateversions;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CurrencyRateVersionItem {
    @JsonProperty("BRL")
    public double brl;
    @JsonProperty("CAD")
    public double cad;
    @JsonProperty("CNY")
    public double cny;
    @JsonProperty("EUR")
    public double eur;
    @JsonProperty("GBP")
    public double gbp;
    @JsonProperty("HKD")
    public double hkd;
    @JsonProperty("INR")
    public double inr;
    @JsonProperty("JPY")
    public double jpy;
    @JsonProperty("KRW")
    public double krw;
    @JsonProperty("MXN")
    public double mxn;
    @JsonProperty("TWD")
    public double twd;
    @JsonProperty("USD")
    public double usd;
    @JsonProperty("ZAR")
    public double zar;
}
