package com.apriori.apibase.services.cid.objects.cost.productioninfo;

import com.apriori.apibase.services.response.objects.AutoSelectedSecondaryVpes;
import com.apriori.apibase.services.response.objects.MaterialCatalogKeyData;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "scenarioKey",
    "primaryPgName",
    "primaryVpeName",
    "autoSelectedSecondaryVpes",
    "usePrimaryAsDefault",
    "initialized",
    "materialCatalogKeyData"
})
public class ProductionInfoVpe {

    @JsonProperty("scenarioKey")
    private ProductionInfoScenarioKey scenarioKey;
    @JsonProperty("primaryPgName")
    private String primaryPgName;
    @JsonProperty("primaryVpeName")
    private String primaryVpeName;
    @JsonProperty("autoSelectedSecondaryVpes")
    private AutoSelectedSecondaryVpes autoSelectedSecondaryVpes;
    @JsonProperty("usePrimaryAsDefault")
    private Boolean usePrimaryAsDefault;
    @JsonProperty("initialized")
    private Boolean initialized;
    @JsonProperty("materialCatalogKeyData")
    private MaterialCatalogKeyData materialCatalogKeyData;

    public ProductionInfoScenarioKey getScenarioKey() {
        return scenarioKey;
    }

    public ProductionInfoVpe setScenarioKey(ProductionInfoScenarioKey scenarioKey) {
        this.scenarioKey = scenarioKey;
        return this;
    }

    public String getPrimaryPgName() {
        return primaryPgName;
    }

    public ProductionInfoVpe setPrimaryPgName(String primaryPgName) {
        this.primaryPgName = primaryPgName;
        return this;
    }

    public String getPrimaryVpeName() {
        return primaryVpeName;
    }

    public ProductionInfoVpe setPrimaryVpeName(String primaryVpeName) {
        this.primaryVpeName = primaryVpeName;
        return this;
    }

    public AutoSelectedSecondaryVpes getAutoSelectedSecondaryVpes() {
        return autoSelectedSecondaryVpes;
    }

    public ProductionInfoVpe setAutoSelectedSecondaryVpes(AutoSelectedSecondaryVpes autoSelectedSecondaryVpes) {
        this.autoSelectedSecondaryVpes = autoSelectedSecondaryVpes;
        return this;
    }

    public Boolean getUsePrimaryAsDefault() {
        return usePrimaryAsDefault;
    }

    public ProductionInfoVpe setUsePrimaryAsDefault(Boolean usePrimaryAsDefault) {
        this.usePrimaryAsDefault = usePrimaryAsDefault;
        return this;
    }

    public Boolean getInitialized() {
        return initialized;
    }

    public ProductionInfoVpe setInitialized(Boolean initialized) {
        this.initialized = initialized;
        return this;
    }

    public MaterialCatalogKeyData getMaterialCatalogKeyData() {
        return materialCatalogKeyData;
    }

    public ProductionInfoVpe setMaterialCatalogKeyData(MaterialCatalogKeyData materialCatalogKeyData) {
        this.materialCatalogKeyData = materialCatalogKeyData;
        return this;
    }
}
