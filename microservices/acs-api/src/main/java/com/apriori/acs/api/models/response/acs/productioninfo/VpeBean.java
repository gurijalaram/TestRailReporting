package com.apriori.acs.api.models.response.acs.productioninfo;

import com.apriori.acs.api.models.response.workorders.MaterialCatalogKeyData;
import com.apriori.acs.api.models.response.workorders.genericclasses.ScenarioKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VpeBean {
    private ScenarioKey scenarioKey;
    private String primaryPgName;
    private String primaryVpeName;
    private String usePrimaryAsDefault;
    private String usePrimaryAsToolShopDefault;
    private String initialized;
    private MaterialCatalogKeyData materialCatalogKeyData;
    private CurrentPgIdentifier currentPpgIdentifier;
}
