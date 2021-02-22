package com.apriori.apibase.services.cid.objects.cost.productioninfo;

public class ProductionInfoMaterial {
    private Boolean initialized;
    private String vpeDefaultMaterialName;
    private String materialMode;
    private Boolean isUserMaterialNameValid;
    private Boolean isCadMaterialNameValid;
    private Integer userUtilizationOverride;

    public Boolean getInitialized() {
        return initialized;
    }

    public ProductionInfoMaterial setInitialized(Boolean initialized) {
        this.initialized = initialized;
        return this;
    }

    public String getVpeDefaultMaterialName() {
        return vpeDefaultMaterialName;
    }

    public ProductionInfoMaterial setVpeDefaultMaterialName(String vpeDefaultMaterialName) {
        this.vpeDefaultMaterialName = vpeDefaultMaterialName;
        return this;
    }

    public String getMaterialMode() {
        return materialMode;
    }

    public ProductionInfoMaterial setMaterialMode(String materialMode) {
        this.materialMode = materialMode;
        return this;
    }

    public Boolean getIsUserMaterialNameValid() {
        return isUserMaterialNameValid;
    }

    public ProductionInfoMaterial setIsUserMaterialNameValid(Boolean isUserMaterialNameValid) {
        this.isUserMaterialNameValid = isUserMaterialNameValid;
        return this;
    }

    public Boolean getIsCadMaterialNameValid() {
        return isCadMaterialNameValid;
    }

    public ProductionInfoMaterial setIsCadMaterialNameValid(Boolean isCadMaterialNameValid) {
        this.isCadMaterialNameValid = isCadMaterialNameValid;
        return this;
    }

    public Integer getUserUtilizationOverride() {
        return userUtilizationOverride;
    }

    public ProductionInfoMaterial setUserUtilizationOverride(Integer userUtilizationOverride) {
        this.userUtilizationOverride = userUtilizationOverride;
        return this;
    }
}