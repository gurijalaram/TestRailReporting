package com.apriori.apibase.services.cid.objects.cost.productioninfo;

import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "ProductionCostResponse.json")
public class ProductionInfo {
    private ProductionInfoScenario scenarioKey;
    private String compType;
    private Boolean initialized;
    private List<String> availablePgNames = null;
    private String processGroupName;
    private Boolean pgEnabled;
    private Boolean cadModelLoaded;
    private ProductionInfoVpe vpeBean;
    private Boolean supportsMaterials;
    private ProductionInfoMaterial materialBean;
    private Integer annualVolume;
    private Boolean annualVolumeOverridden;
    private Integer productionLife;
    private Boolean productionLifeOverridden;
    private Integer computedBatchSize;
    private Boolean batchSizeOverridden;
    private Integer componentsPerProduct;
    private Boolean manuallyCosted;
    private List<String> availableCurrencyCodes = null;
    private String manualCurrencyCode;
    private List<String> availableCurrencyVersions = null;
    private String manualCurrencyVersion;
    private Boolean hasTargetCost;
    private Boolean hasTargetFinishMass;
    private String machiningMode;
    private Boolean thicknessVisible;
    private String compositesFileName;

    public ProductionInfoScenario getScenarioKey() {
        return scenarioKey;
    }

    public ProductionInfo setScenarioKey(ProductionInfoScenario scenarioKey) {
        this.scenarioKey = scenarioKey;
        return this;
    }

    public String getCompType() {
        return compType;
    }

    public ProductionInfo setCompType(String compType) {
        this.compType = compType;
        return this;
    }

    public Boolean getInitialized() {
        return initialized;
    }

    public ProductionInfo setInitialized(Boolean initialized) {
        this.initialized = initialized;
        return this;
    }

    public List<String> getAvailablePgNames() {
        return availablePgNames;
    }

    public ProductionInfo setAvailablePgNames(List<String> availablePgNames) {
        this.availablePgNames = availablePgNames;
        return this;
    }

    public String getProcessGroupName() {
        return processGroupName;
    }

    public ProductionInfo setProcessGroupName(String processGroupName) {
        this.processGroupName = processGroupName;
        return this;
    }

    public Boolean getPgEnabled() {
        return pgEnabled;
    }

    public ProductionInfo setPgEnabled(Boolean pgEnabled) {
        this.pgEnabled = pgEnabled;
        return this;
    }

    public Boolean getCadModelLoaded() {
        return cadModelLoaded;
    }

    public ProductionInfo setCadModelLoaded(Boolean cadModelLoaded) {
        this.cadModelLoaded = cadModelLoaded;
        return this;
    }

    public ProductionInfoVpe getVpeBean() {
        return vpeBean;
    }

    public ProductionInfo setVpeBean(ProductionInfoVpe vpeBean) {
        this.vpeBean = vpeBean;
        return this;
    }

    public Boolean getSupportsMaterials() {
        return supportsMaterials;
    }

    public ProductionInfo setSupportsMaterials(Boolean supportsMaterials) {
        this.supportsMaterials = supportsMaterials;
        return this;
    }

    public ProductionInfoMaterial getMaterialBean() {
        return materialBean;
    }

    public ProductionInfo setMaterialBean(ProductionInfoMaterial materialBean) {
        this.materialBean = materialBean;
        return this;
    }

    public Integer getAnnualVolume() {
        return annualVolume;
    }

    public ProductionInfo setAnnualVolume(Integer annualVolume) {
        this.annualVolume = annualVolume;
        return this;
    }

    public Boolean getAnnualVolumeOverridden() {
        return annualVolumeOverridden;
    }

    public ProductionInfo setAnnualVolumeOverridden(Boolean annualVolumeOverridden) {
        this.annualVolumeOverridden = annualVolumeOverridden;
        return this;
    }

    public Integer getProductionLife() {
        return productionLife;
    }

    public ProductionInfo setProductionLife(Integer productionLife) {
        this.productionLife = productionLife;
        return this;
    }

    public Boolean getProductionLifeOverridden() {
        return productionLifeOverridden;
    }

    public ProductionInfo setProductionLifeOverridden(Boolean productionLifeOverridden) {
        this.productionLifeOverridden = productionLifeOverridden;
        return this;
    }

    public Integer getComputedBatchSize() {
        return computedBatchSize;
    }

    public ProductionInfo setComputedBatchSize(Integer computedBatchSize) {
        this.computedBatchSize = computedBatchSize;
        return this;
    }

    public Boolean getBatchSizeOverridden() {
        return batchSizeOverridden;
    }

    public ProductionInfo setBatchSizeOverridden(Boolean batchSizeOverridden) {
        this.batchSizeOverridden = batchSizeOverridden;
        return this;
    }

    public Integer getComponentsPerProduct() {
        return componentsPerProduct;
    }

    public ProductionInfo setComponentsPerProduct(Integer componentsPerProduct) {
        this.componentsPerProduct = componentsPerProduct;
        return this;
    }

    public Boolean getManuallyCosted() {
        return manuallyCosted;
    }

    public ProductionInfo setManuallyCosted(Boolean manuallyCosted) {
        this.manuallyCosted = manuallyCosted;
        return this;
    }

    public List<String> getAvailableCurrencyCodes() {
        return availableCurrencyCodes;
    }

    public ProductionInfo setAvailableCurrencyCodes(List<String> availableCurrencyCodes) {
        this.availableCurrencyCodes = availableCurrencyCodes;
        return this;
    }

    public String getManualCurrencyCode() {
        return manualCurrencyCode;
    }

    public ProductionInfo setManualCurrencyCode(String manualCurrencyCode) {
        this.manualCurrencyCode = manualCurrencyCode;
        return this;
    }

    public List<String> getAvailableCurrencyVersions() {
        return availableCurrencyVersions;
    }

    public ProductionInfo setAvailableCurrencyVersions(List<String> availableCurrencyVersions) {
        this.availableCurrencyVersions = availableCurrencyVersions;
        return this;
    }

    public String getManualCurrencyVersion() {
        return manualCurrencyVersion;
    }

    public ProductionInfo setManualCurrencyVersion(String manualCurrencyVersion) {
        this.manualCurrencyVersion = manualCurrencyVersion;
        return this;
    }

    public Boolean getHasTargetCost() {
        return hasTargetCost;
    }

    public ProductionInfo setHasTargetCost(Boolean hasTargetCost) {
        this.hasTargetCost = hasTargetCost;
        return this;
    }

    public Boolean getHasTargetFinishMass() {
        return hasTargetFinishMass;
    }

    public ProductionInfo setHasTargetFinishMass(Boolean hasTargetFinishMass) {
        this.hasTargetFinishMass = hasTargetFinishMass;
        return this;
    }

    public String getMachiningMode() {
        return machiningMode;
    }

    public ProductionInfo setMachiningMode(String machiningMode) {
        this.machiningMode = machiningMode;
        return this;
    }

    public Boolean getThicknessVisible() {
        return thicknessVisible;
    }

    public ProductionInfo setThicknessVisible(Boolean thicknessVisible) {
        this.thicknessVisible = thicknessVisible;
        return this;
    }

    public String getCompositesFileName() {
        return compositesFileName;
    }

    public ProductionInfo setCompositesFileName(String compositesFileName) {
        this.compositesFileName = compositesFileName;
        return this;
    }
}