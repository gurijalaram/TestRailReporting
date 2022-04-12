package com.apriori.acs.entity.request.workorders.cost.productioninfo;

import com.apriori.apibase.services.response.objects.AutoSelectedSecondaryVpes;
import com.apriori.apibase.services.response.objects.MaterialCatalogKeyData;

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
