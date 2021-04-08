package com.apriori.sds.entity.response;

import com.apriori.apibase.services.JacksonUtil;
import com.apriori.utils.http.enums.Schema;
import lombok.Data;

@Schema(location = "sds/ScenarioMaterialCatalogKeyData.json")
@Data
public class ScenarioMaterialCatalogKeyData extends JacksonUtil {
    private String first;
    private String second;
}