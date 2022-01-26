package com.apriori.acs.entity.response.getsetproductiondefaults;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

@Data
@Schema(location = "GetProductionDefaultsResponse.json")
public class GetProductionDefaultsResponse {
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
