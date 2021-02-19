package com.apriori.sds.entity.response;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "sds/ScenarioCostingDefaults.json")
public class ScenarioCostingDefaultsResponse {

    private ScenarioCostingDefaultsResponse response;
    private String[] availablePgNames;
    private String machiningModeEnabled;
    private String computedBatchSize;
    private String manuallyCosted;
    private String batchSizeOverride;
    private String defaultToPrimaryVpe;
    private ScenarioKey scenarioKey;
    private String processGroupName;
    private String productionLife;
    private String compType;
    private String thicknessVisible;
    private String sourceModelVisible;
    private ScenarioDigitalFactoryBean vpeBean;
    private String initialized;
    private String pgEnabled;
    private String supportsMaterials;
    private String machiningMode;
    private String hasTargetFinishMass;
    private ScenarioMaterialBean materialBean;
    private String annualVolumeOverridden;
    private String hasTargetCost;
    private String manualCurrencyCode;
    private String[] availableCurrencyCodes;
    private String compositesFileVisible;
    private String batchSizeOverridden;
    private String manualCurrencyCodeVisible;
    private String stagesAndMaterialsVisible;
    private String[] availableCurrencyVersions;
    private String annualVolume;
    private String cadModelLoaded;
    private String manualCurrencyVersion;
    private String productionLifeOverridden;
    private String componentsPerProduct;

    public ScenarioCostingDefaultsResponse getResponse() {
        return response;
    }

    public void setResponse(ScenarioCostingDefaultsResponse response) {
        this.response = response;
    }

    private ScenarioAvailableProcessGroupSelection[] availableProcessGroupSelections;

    public String[] getAvailablePgNames() {
        return availablePgNames;
    }

    public void setAvailablePgNames(String[] availablePgNames) {
        this.availablePgNames = availablePgNames;
    }

    public String getMachiningModeEnabled() {
        return machiningModeEnabled;
    }

    public void setMachiningModeEnabled(String machiningModeEnabled) {
        this.machiningModeEnabled = machiningModeEnabled;
    }

    public String getComputedBatchSize() {
        return computedBatchSize;
    }

    public void setComputedBatchSize(String computedBatchSize) {
        this.computedBatchSize = computedBatchSize;
    }

    public String getManuallyCosted() {
        return manuallyCosted;
    }

    public void setManuallyCosted(String manuallyCosted) {
        this.manuallyCosted = manuallyCosted;
    }

    public String getBatchSizeOverride() {
        return batchSizeOverride;
    }

    public void setBatchSizeOverride(String batchSizeOverride) {
        this.batchSizeOverride = batchSizeOverride;
    }

    public String getDefaultToPrimaryVpe() {
        return defaultToPrimaryVpe;
    }

    public void setDefaultToPrimaryVpe(String defaultToPrimaryVpe) {
        this.defaultToPrimaryVpe = defaultToPrimaryVpe;
    }

    public ScenarioKey getScenarioKey() {
        return scenarioKey;
    }

    public void setScenarioKey(ScenarioKey scenarioKey) {
        this.scenarioKey = scenarioKey;
    }

    public String getProcessGroupName() {
        return processGroupName;
    }

    public void setProcessGroupName(String processGroupName) {
        this.processGroupName = processGroupName;
    }

    public String getProductionLife() {
        return productionLife;
    }

    public void setProductionLife(String productionLife) {
        this.productionLife = productionLife;
    }

    public String getCompType() {
        return compType;
    }

    public void setCompType(String compType) {
        this.compType = compType;
    }

    public String getThicknessVisible() {
        return thicknessVisible;
    }

    public void setThicknessVisible(String thicknessVisible) {
        this.thicknessVisible = thicknessVisible;
    }

    public String getSourceModelVisible() {
        return sourceModelVisible;
    }

    public void setSourceModelVisible(String sourceModelVisible) {
        this.sourceModelVisible = sourceModelVisible;
    }

    public ScenarioDigitalFactoryBean getVpeBean() {
        return vpeBean;
    }

    public void setVpeBean(ScenarioDigitalFactoryBean vpeBean) {
        this.vpeBean = vpeBean;
    }

    public String getInitialized() {
        return initialized;
    }

    public void setInitialized(String initialized) {
        this.initialized = initialized;
    }

    public String getPgEnabled() {
        return pgEnabled;
    }

    public void setPgEnabled(String pgEnabled) {
        this.pgEnabled = pgEnabled;
    }

    public String getSupportsMaterials() {
        return supportsMaterials;
    }

    public void setSupportsMaterials(String supportsMaterials) {
        this.supportsMaterials = supportsMaterials;
    }

    public String getMachiningMode() {
        return machiningMode;
    }

    public void setMachiningMode(String machiningMode) {
        this.machiningMode = machiningMode;
    }

    public String getHasTargetFinishMass() {
        return hasTargetFinishMass;
    }

    public void setHasTargetFinishMass(String hasTargetFinishMass) {
        this.hasTargetFinishMass = hasTargetFinishMass;
    }

    public ScenarioMaterialBean getMaterialBean() {
        return materialBean;
    }

    public void setMaterialBean(ScenarioMaterialBean materialBean) {
        this.materialBean = materialBean;
    }

    public String getAnnualVolumeOverridden() {
        return annualVolumeOverridden;
    }

    public void setAnnualVolumeOverridden(String annualVolumeOverridden) {
        this.annualVolumeOverridden = annualVolumeOverridden;
    }

    public String getHasTargetCost() {
        return hasTargetCost;
    }

    public void setHasTargetCost(String hasTargetCost) {
        this.hasTargetCost = hasTargetCost;
    }

    public String getManualCurrencyCode() {
        return manualCurrencyCode;
    }

    public void setManualCurrencyCode(String manualCurrencyCode) {
        this.manualCurrencyCode = manualCurrencyCode;
    }

    public String[] getAvailableCurrencyCodes() {
        return availableCurrencyCodes;
    }

    public void setAvailableCurrencyCodes(String[] availableCurrencyCodes) {
        this.availableCurrencyCodes = availableCurrencyCodes;
    }

    public String getCompositesFileVisible() {
        return compositesFileVisible;
    }

    public void setCompositesFileVisible(String compositesFileVisible) {
        this.compositesFileVisible = compositesFileVisible;
    }

    public String getBatchSizeOverridden() {
        return batchSizeOverridden;
    }

    public void setBatchSizeOverridden(String batchSizeOverridden) {
        this.batchSizeOverridden = batchSizeOverridden;
    }

    public String getManualCurrencyCodeVisible() {
        return manualCurrencyCodeVisible;
    }

    public void setManualCurrencyCodeVisible(String manualCurrencyCodeVisible) {
        this.manualCurrencyCodeVisible = manualCurrencyCodeVisible;
    }

    public String getStagesAndMaterialsVisible() {
        return stagesAndMaterialsVisible;
    }

    public void setStagesAndMaterialsVisible(String stagesAndMaterialsVisible) {
        this.stagesAndMaterialsVisible = stagesAndMaterialsVisible;
    }

    public String[] getAvailableCurrencyVersions() {
        return availableCurrencyVersions;
    }

    public void setAvailableCurrencyVersions(String[] availableCurrencyVersions) {
        this.availableCurrencyVersions = availableCurrencyVersions;
    }

    public String getAnnualVolume() {
        return annualVolume;
    }

    public void setAnnualVolume(String annualVolume) {
        this.annualVolume = annualVolume;
    }

    public String getCadModelLoaded() {
        return cadModelLoaded;
    }

    public void setCadModelLoaded(String cadModelLoaded) {
        this.cadModelLoaded = cadModelLoaded;
    }

    public String getManualCurrencyVersion() {
        return manualCurrencyVersion;
    }

    public void setManualCurrencyVersion(String manualCurrencyVersion) {
        this.manualCurrencyVersion = manualCurrencyVersion;
    }

    public String getProductionLifeOverridden() {
        return productionLifeOverridden;
    }

    public void setProductionLifeOverridden(String productionLifeOverridden) {
        this.productionLifeOverridden = productionLifeOverridden;
    }

    public String getComponentsPerProduct() {
        return componentsPerProduct;
    }

    public void setComponentsPerProduct(String componentsPerProduct) {
        this.componentsPerProduct = componentsPerProduct;
    }

    public ScenarioAvailableProcessGroupSelection[] getAvailableProcessGroupSelections() {
        return availableProcessGroupSelections;
    }

    public void setAvailableProcessGroupSelections(ScenarioAvailableProcessGroupSelection[] availableProcessGroupSelections) {
        this.availableProcessGroupSelections = availableProcessGroupSelections;
    }

}
