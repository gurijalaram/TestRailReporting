package com.apriori.acs.entity.response.getenabledcurrencyrateversions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CurrencyRateVersionItem {
    @JsonProperty("BRL")
    public double BRL;
    @JsonProperty("CAD")
    public double CAD;
    @JsonProperty("CNY")
    public double CNY;
    @JsonProperty("EUR")
    public double EUR;
    @JsonProperty("GBP")
    public double GBP;
    @JsonProperty("HKD")
    public double HKD;
    @JsonProperty("INR")
    public double INR;
    @JsonProperty("JPY")
    public double JPY;
    @JsonProperty("KRW")
    public double KRW;
    @JsonProperty("MXN")
    public double MXN;
    @JsonProperty("TWD")
    public double TWD;
    @JsonProperty("USD")
    public double USD;
    @JsonProperty("ZAR")
    public double ZAR;
}
