package com.apriori.sds.entity.response;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Schema(location = "ScenarioDigitalFactoryBean.json")
@Data
@JsonRootName("response")
public class ScenarioDigitalFactoryBean {
    private String primaryVpeName;
    private Boolean initialized;
    private ScenarioKey scenarioKey;
    private ScenarioCurrentProcessGroup currentPpgIdentifier;
    private Boolean usePrimaryAsDefault;
    private Boolean usePrimaryAsToolShopDefault;
    private ScenarioMaterialCatalogKeyData materialCatalogKeyData;
    private String primaryPgName;
}
