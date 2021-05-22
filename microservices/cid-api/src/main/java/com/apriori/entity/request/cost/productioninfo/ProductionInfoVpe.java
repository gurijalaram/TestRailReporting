package com.apriori.entity.request.cost.productioninfo;

import com.apriori.apibase.services.response.objects.AutoSelectedSecondaryVpes;
import com.apriori.apibase.services.response.objects.MaterialCatalogKeyData;

public class ProductionInfoVpe {
    private ProductionInfoScenarioKey scenarioKey;
    private String primaryPgName;
    private String primaryVpeName;
    private AutoSelectedSecondaryVpes autoSelectedSecondaryVpes;
    private Boolean usePrimaryAsDefault;
    private Boolean initialized;
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
