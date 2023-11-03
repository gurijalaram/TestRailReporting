package com.apriori.acs.api.models.request.workorders.cost.productioninfo;

import com.apriori.acs.api.models.response.workorders.AutoSelectedSecondaryVpes;
import com.apriori.acs.api.models.response.workorders.MaterialCatalogKeyData;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductionInfoVpe {
    private ProductionInfoScenarioKey scenarioKey;
    private String primaryPgName;
    private String primaryVpeName;
    private AutoSelectedSecondaryVpes autoSelectedSecondaryVpes;
    private Boolean usePrimaryAsDefault;
    private Boolean initialized;
    private MaterialCatalogKeyData materialCatalogKeyData;
}
