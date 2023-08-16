package com.apriori.acs.models.response.acs.productiondefaults;

import com.apriori.annotations.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/ProductionDefaultsResponse.json")
public class ProductionDefaultsResponse {
    private String pg;
    private String vpe;
    private String material;
    private String materialCatalogName;
    private String annualVolume;
    private double productionLife;
    private int batchSize;
    private boolean useVpeForAllProcesses;
    private ProductionDefaultsList defaults;
    private boolean batchSizeMode;
}
