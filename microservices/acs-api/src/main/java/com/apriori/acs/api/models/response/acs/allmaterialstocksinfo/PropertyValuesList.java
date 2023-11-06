package com.apriori.acs.api.models.response.acs.allmaterialstocksinfo;

import lombok.Data;

@Data
public class PropertyValuesList {
    private double costPerKG;
    private String hardnessSystem;
    private double thickness;
    private double length;
    private double materialStockCarbonEmissionsFactor;
    private double baseCostPerUnit;
    private double hardness;
    private String formName;
    private String name;
    private double width;
    private String costUnits;
    private double costPerUnit;
}
