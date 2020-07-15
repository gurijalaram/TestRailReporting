package com.apriori.apibase.services.response.objects;

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
    public void setScenarioKey(ScenarioKey_ scenarioKey) {
        this.scenarioKey = scenarioKey;
    }

    @JsonProperty("primaryPgName")
    public String getPrimaryPgName() {
        return primaryPgName;
    }

    @JsonProperty("primaryPgName")
    public void setPrimaryPgName(String primaryPgName) {
        this.primaryPgName = primaryPgName;
    }

    @JsonProperty("primaryVpeName")
    public String getPrimaryVpeName() {
        return primaryVpeName;
    }

    @JsonProperty("primaryVpeName")
    public void setPrimaryVpeName(String primaryVpeName) {
        this.primaryVpeName = primaryVpeName;
    }

    @JsonProperty("autoSelectedSecondaryVpes")
    public AutoSelectedSecondaryVpes getAutoSelectedSecondaryVpes() {
        return autoSelectedSecondaryVpes;
    }

    @JsonProperty("autoSelectedSecondaryVpes")
    public void setAutoSelectedSecondaryVpes(AutoSelectedSecondaryVpes autoSelectedSecondaryVpes) {
        this.autoSelectedSecondaryVpes = autoSelectedSecondaryVpes;
    }

    @JsonProperty("usePrimaryAsDefault")
    public Boolean getUsePrimaryAsDefault() {
        return usePrimaryAsDefault;
    }

    @JsonProperty("usePrimaryAsDefault")
    public void setUsePrimaryAsDefault(Boolean usePrimaryAsDefault) {
        this.usePrimaryAsDefault = usePrimaryAsDefault;
    }

    @JsonProperty("initialized")
    public Boolean getInitialized() {
        return initialized;
    }

    @JsonProperty("initialized")
    public void setInitialized(Boolean initialized) {
        this.initialized = initialized;
    }

    @JsonProperty("materialCatalogKeyData")
    public MaterialCatalogKeyData getMaterialCatalogKeyData() {
        return materialCatalogKeyData;
    }

    @JsonProperty("materialCatalogKeyData")
    public void setMaterialCatalogKeyData(MaterialCatalogKeyData materialCatalogKeyData) {
        this.materialCatalogKeyData = materialCatalogKeyData;
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
