package com.apriori.acs.entity.response.getsetproductiondefaults;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SetProductionDefaultsInputs {
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
