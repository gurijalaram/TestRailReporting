package com.apriori.dfs.api.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MaterialProperties {
    private Double costPerKG;
    private Integer ultimateTensileStrength;
    @JsonProperty("k")
    private Double thermalConductivity;
    private Double shearStrength;
    private Integer youngModulus;
    private Integer tensileYieldStrength;
    @JsonProperty("n")
    private Double strainHardeningExponent;
    private Double possionRatio;
    @JsonProperty("r")
    private Double plasticStrainSatio;
    private Double scrapCostPercent;
    private String costUnits;
    private String sourceName;
    private Integer millingSpeed;
    private String dataSource;
}

