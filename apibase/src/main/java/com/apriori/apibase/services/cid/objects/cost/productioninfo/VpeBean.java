package com.apriori.apibase.services.cid.objects.cost.productioninfo;

import com.apriori.apibase.services.response.objects.AutoSelectedSecondaryVpes;
import com.apriori.apibase.services.response.objects.MaterialCatalogKeyData;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

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
public class VpeBean {

    @JsonProperty("scenarioKey")
    private ScenarioKey_ scenarioKey;
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
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("scenarioKey")
    public ScenarioKey_ getScenarioKey() {
        return scenarioKey;
    }

    @JsonProperty("scenarioKey")
    public VpeBean setScenarioKey(ScenarioKey_ scenarioKey) {
        this.scenarioKey = scenarioKey;
        return this;
    }

    @JsonProperty("primaryPgName")
    public String getPrimaryPgName() {
        return primaryPgName;
    }

    @JsonProperty("primaryPgName")
    public VpeBean setPrimaryPgName(String primaryPgName) {
        this.primaryPgName = primaryPgName;
        return this;
    }

    @JsonProperty("primaryVpeName")
    public String getPrimaryVpeName() {
        return primaryVpeName;
    }

    @JsonProperty("primaryVpeName")
    public VpeBean setPrimaryVpeName(String primaryVpeName) {
        this.primaryVpeName = primaryVpeName;
        return this;
    }

    @JsonProperty("autoSelectedSecondaryVpes")
    public AutoSelectedSecondaryVpes getAutoSelectedSecondaryVpes() {
        return autoSelectedSecondaryVpes;
    }

    @JsonProperty("autoSelectedSecondaryVpes")
    public VpeBean setAutoSelectedSecondaryVpes(AutoSelectedSecondaryVpes autoSelectedSecondaryVpes) {
        this.autoSelectedSecondaryVpes = autoSelectedSecondaryVpes;
        return this;
    }

    @JsonProperty("usePrimaryAsDefault")
    public Boolean getUsePrimaryAsDefault() {
        return usePrimaryAsDefault;
    }

    @JsonProperty("usePrimaryAsDefault")
    public VpeBean setUsePrimaryAsDefault(Boolean usePrimaryAsDefault) {
        this.usePrimaryAsDefault = usePrimaryAsDefault;
        return this;
    }

    @JsonProperty("initialized")
    public Boolean getInitialized() {
        return initialized;
    }

    @JsonProperty("initialized")
    public VpeBean setInitialized(Boolean initialized) {
        this.initialized = initialized;
        return this;
    }

    @JsonProperty("materialCatalogKeyData")
    public MaterialCatalogKeyData getMaterialCatalogKeyData() {
        return materialCatalogKeyData;
    }

    @JsonProperty("materialCatalogKeyData")
    public VpeBean setMaterialCatalogKeyData(MaterialCatalogKeyData materialCatalogKeyData) {
        this.materialCatalogKeyData = materialCatalogKeyData;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
