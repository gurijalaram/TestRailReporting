package com.apriori.acs.entity.response.acs.getenabledcurrencyrateversions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CurrencyRateVersionItem {
    @JsonProperty("AUD")
    public double aud;
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
    @JsonProperty("SEK")
    public double sek;
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
    @JsonProperty("UAH")
    public double uah;
    @JsonProperty("USD")
    public double usd;
    @JsonProperty("ZAR")
    public double zar;
}
