package com.apriori.apibase.services.sds;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "sds/ScenarioDigitalFactoryBean.json")
public class ScenarioDigitalFactoryBean {

    private String primaryVpeName;
    private String initialized;
    private ScenarioKey scenarioKey;
    private ScenarioCurrentProcessGroup currentPpgIdentifier;
    private String usePrimaryAsDefault;
    private ScenarioMaterialCatalogKeyData materialCatalogKeyData;
    private String primaryPgName;

    public String getPrimaryVpeName () {
        return primaryVpeName;
    }

    public void setPrimaryVpeName (String primaryVpeName) {
        this.primaryVpeName = primaryVpeName;
    }

    public String getInitialized () {
        return initialized;
    }

    public void setInitialized (String initialized) {
        this.initialized = initialized;
    }

    public ScenarioKey getScenarioKey () {
        return scenarioKey;
    }

    public void setScenarioKey (ScenarioKey scenarioKey) {
        this.scenarioKey = scenarioKey;
    }

    public ScenarioCurrentProcessGroup getCurrentPpgIdentifier () {
        return currentPpgIdentifier;
    }

    public void setCurrentPpgIdentifier (ScenarioCurrentProcessGroup currentPpgIdentifier) {
        this.currentPpgIdentifier = currentPpgIdentifier;
    }

    public String getUsePrimaryAsDefault () {
        return usePrimaryAsDefault;
    }

    public void setUsePrimaryAsDefault (String usePrimaryAsDefault) {
        this.usePrimaryAsDefault = usePrimaryAsDefault;
    }

    public ScenarioMaterialCatalogKeyData getMaterialCatalogKeyData () {
        return materialCatalogKeyData;
    }

    public void setMaterialCatalogKeyData (ScenarioMaterialCatalogKeyData materialCatalogKeyData) {
        this.materialCatalogKeyData = materialCatalogKeyData;
    }

    public String getPrimaryPgName () {
        return primaryPgName;
    }

    public void setPrimaryPgName (String primaryPgName) {
        this.primaryPgName = primaryPgName;
    }

}
