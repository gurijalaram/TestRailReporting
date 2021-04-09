package com.apriori.sds.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Schema(location = "sds/ScenarioDigitalFactoryBean.json")
@Data
@JsonRootName("response")
@JsonIgnoreProperties(ignoreUnknown = true)
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
