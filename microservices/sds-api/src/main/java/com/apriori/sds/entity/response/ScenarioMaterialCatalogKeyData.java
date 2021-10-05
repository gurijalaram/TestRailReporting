package com.apriori.sds.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Schema(location = "ScenarioMaterialCatalogKeyData.json")
@Data
@JsonRootName("response")
public class ScenarioMaterialCatalogKeyData {
    private String first;
    private String second;
}