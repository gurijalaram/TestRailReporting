package com.apriori.acs.entity.response.acs.getsetproductioninfo;

import com.apriori.acs.entity.response.acs.createmissingscenario.ScenarioKey;
import com.apriori.apibase.services.response.objects.MaterialCatalogKeyData;

public class VpeBean {
    private ScenarioKey scenarioKey;
    private String primaryPgName;
    private String primaryVpeName;
    private String autoSelectedSecondaryVpes;
    private String usePrimaryAsDefault;
    private String initialized;
    private MaterialCatalogKeyData materialCatalogKeyData;
}
