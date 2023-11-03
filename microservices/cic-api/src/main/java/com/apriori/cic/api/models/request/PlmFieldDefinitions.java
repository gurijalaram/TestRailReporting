package com.apriori.cic.api.models.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlmFieldDefinitions {
    @JsonProperty("BatchSize")
    @Builder.Default
    private Integer batchSize = 0;
    @JsonProperty("FinishMass")
    @Builder.Default
    private Double finishMass = 0.0;
    @JsonProperty("MultiselectString")
    @Builder.Default
    private String multiselectString = "";
    @JsonProperty("MaterialCost")
    @Builder.Default
    private Double materialCost = 0.0;
    @JsonProperty("CapitalInvestment")
    @Builder.Default
    private Double capitalInvestment = 0.0;
    @JsonProperty("LaborTime")
    @Builder.Default
    private Double laborTime = 0.0;
    @JsonProperty("RoughMass")
    @Builder.Default
    private Double roughMass = 0.0;
    @JsonProperty("Utilization")
    @Builder.Default
    private Double utilization = 0.0;
    @JsonProperty("ApFBC")
    @Builder.Default
    private Double fullyBurdenedCost = 0.0;
    @JsonProperty("ApDFMRating")
    @Builder.Default
    private String dfmRating = "";
    @JsonProperty("CurrencyCode")
    @Builder.Default
    private String currencyCode = "";
    @JsonProperty("ApCycleTime")
    @Builder.Default
    private Double cycleTime = 0.0;
    @JsonProperty("ApDFMRiskScore")
    @Builder.Default
    private Integer dfmRiskScore = 0;
    @JsonProperty("ApPPC")
    @Builder.Default
    private Double ApPpc = 0.0;
    @JsonProperty("String1")
    @Builder.Default
    private String string1 = "";
    @JsonProperty("RealNumber1")
    @Builder.Default
    private Double realNumber1 = 0.0;
    @JsonProperty("DateTime1")
    private String dateTime1;
    @JsonProperty("ApPG")
    @Builder.Default
    private String processGroupName = "";
    @JsonProperty("ApMaterial")
    @Builder.Default
    private String materialName = "";
    @JsonProperty("AnnualVolume")
    @Builder.Default
    private Integer annualVolume = 0;
    @JsonProperty("ProductionLife")
    @Builder.Default
    private Double productionLife = 0.0;
    @Builder.Default
    @JsonProperty("VPE")
    private String digitalFactory = "";
}
