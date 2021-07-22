package com.apriori.acs.entity.response.getenabledcurrencyrateversions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonRootName("abaairabiriririqiraajraiyrabiriqirbaizqbirjriyzrajzrizyq")
public class CurrencyRateVersionItemTwo {
    @JsonProperty("BRL")
    private double BRL;
    @JsonProperty("CAD")
    private double CAD;
    @JsonProperty("CNY")
    private double CNY;
    @JsonProperty("EUR")
    private double EUR;
    @JsonProperty("GBP")
    private double GBP;
    @JsonProperty("HKD")
    private double HKD;
    @JsonProperty("INR")
    private double INR;
    @JsonProperty("JPY")
    private double JPY;
    @JsonProperty("KRW")
    private double KRW;
    @JsonProperty("MXN")
    private double MXN;
    @JsonProperty("TWD")
    private double TWD;
    @JsonProperty("USD")
    private double USD;
    @JsonProperty("ZAR")
    private double ZAR;
}
