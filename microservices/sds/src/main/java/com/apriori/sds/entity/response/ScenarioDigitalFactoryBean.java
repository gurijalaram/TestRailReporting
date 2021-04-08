package com.apriori.sds.entity.response;

import com.apriori.apibase.services.JacksonUtil;
import com.apriori.utils.http.enums.Schema;
import lombok.Data;

@Schema(location = "sds/ScenarioDigitalFactoryBean.json")
@Data
public class ScenarioDigitalFactoryBean extends JacksonUtil {
    private String primaryVpeName;
    private Boolean initialized;
    private ScenarioKey scenarioKey;
    private ScenarioCurrentProcessGroup currentPpgIdentifier;
    private Boolean usePrimaryAsDefault;
    private Boolean usePrimaryAsToolShopDefault;
    private ScenarioMaterialCatalogKeyData materialCatalogKeyData;
    private String primaryPgName;
}
