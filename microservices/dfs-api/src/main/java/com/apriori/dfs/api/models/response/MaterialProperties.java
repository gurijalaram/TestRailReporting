package com.apriori.dfs.api.models.response;

import lombok.Data;

@Data
public class MaterialProperties {
    private Double costPerKG;
    private Integer ultimateTensileStrength;
    private Double k;
    private Double shearStrength;
    private Integer youngModulus;
    private Integer tensileYieldStrength;
    private Double n;
    private Double possionRatio;
    private Double r;
    private Double scrapCostPercent;
    private String costUnits;
    private String sourceName;
    private Integer millingSpeed;
    private String dataSource;
}

