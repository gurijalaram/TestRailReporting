package com.apriori.sds.models.response;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Schema(location = "ScenarioMaterialCatalogKeyData.json")
@Data
@JsonRootName("response")
public class ScenarioMaterialCatalogKeyData {
    private String first;
    private String second;
}