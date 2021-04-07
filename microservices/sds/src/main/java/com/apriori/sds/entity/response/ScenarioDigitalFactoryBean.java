package com.apriori.sds.entity.response;

import com.apriori.apibase.services.LombokUtil;
import com.apriori.utils.http.enums.Schema;

@Schema(location = "sds/ScenarioDigitalFactoryBean.json")
public class ScenarioDigitalFactoryBean extends LombokUtil {
    private String primaryVpeName;
    private Boolean initialized;
    private ScenarioKey scenarioKey;
    private ScenarioCurrentProcessGroup currentPpgIdentifier;
    private Boolean usePrimaryAsDefault;
    private Boolean usePrimaryAsToolShopDefault;
    private ScenarioMaterialCatalogKeyData materialCatalogKeyData;
    private String primaryPgName;
}
