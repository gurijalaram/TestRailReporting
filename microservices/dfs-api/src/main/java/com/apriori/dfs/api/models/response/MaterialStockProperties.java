package com.apriori.dfs.api.models.response;

import lombok.Data;

@Data
public class MaterialStockProperties {
    private Double costPerKG;
    private Integer outsideDia;
    private Integer length;
    private Double width;
    private Double wallThickness;
    private Double materialStockCarbonEmissionsFactor;
    private String costUnits;
    private Double insideDia;
    private Double massPerMeter;
    private Double height;
}

