package com.apriori.apibase.services.cid.objects.cost.productioninfo;

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
    "initialized",
    "vpeDefaultMaterialName",
    "materialMode",
    "isUserMaterialNameValid",
    "isCadMaterialNameValid",
    "userUtilizationOverride"
})
public class MaterialBean {

    @JsonProperty("initialized")
    private Boolean initialized;
    @JsonProperty("vpeDefaultMaterialName")
    private String vpeDefaultMaterialName;
    @JsonProperty("materialMode")
    private String materialMode;
    @JsonProperty("isUserMaterialNameValid")
    private Boolean isUserMaterialNameValid;
    @JsonProperty("isCadMaterialNameValid")
    private Boolean isCadMaterialNameValid;
    @JsonProperty("userUtilizationOverride")
    private Integer userUtilizationOverride;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("initialized")
    public Boolean getInitialized() {
        return initialized;
    }

    @JsonProperty("initialized")
    public MaterialBean setInitialized(Boolean initialized) {
        this.initialized = initialized;
        return this;
    }

    @JsonProperty("vpeDefaultMaterialName")
    public String getVpeDefaultMaterialName() {
        return vpeDefaultMaterialName;
    }

    @JsonProperty("vpeDefaultMaterialName")
    public MaterialBean setVpeDefaultMaterialName(String vpeDefaultMaterialName) {
        this.vpeDefaultMaterialName = vpeDefaultMaterialName;
        return this;
    }

    @JsonProperty("materialMode")
    public String getMaterialMode() {
        return materialMode;
    }

    @JsonProperty("materialMode")
    public MaterialBean setMaterialMode(String materialMode) {
        this.materialMode = materialMode;
        return this;
    }

    @JsonProperty("isUserMaterialNameValid")
    public Boolean getIsUserMaterialNameValid() {
        return isUserMaterialNameValid;
    }

    @JsonProperty("isUserMaterialNameValid")
    public MaterialBean setIsUserMaterialNameValid(Boolean isUserMaterialNameValid) {
        this.isUserMaterialNameValid = isUserMaterialNameValid;
        return this;
    }

    @JsonProperty("isCadMaterialNameValid")
    public Boolean getIsCadMaterialNameValid() {
        return isCadMaterialNameValid;
    }

    @JsonProperty("isCadMaterialNameValid")
    public MaterialBean setIsCadMaterialNameValid(Boolean isCadMaterialNameValid) {
        this.isCadMaterialNameValid = isCadMaterialNameValid;
        return this;
    }

    @JsonProperty("userUtilizationOverride")
    public Integer getUserUtilizationOverride() {
        return userUtilizationOverride;
    }

    @JsonProperty("userUtilizationOverride")
    public MaterialBean setUserUtilizationOverride(Integer userUtilizationOverride) {
        this.userUtilizationOverride = userUtilizationOverride;
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
