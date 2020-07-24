package com.apriori.apibase.services.cid.objects.cost.productioninfo;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "scenarioKey",
    "compType",
    "initialized",
    "availablePgNames",
    "processGroupName",
    "pgEnabled",
    "cadModelLoaded",
    "vpeBean",
    "supportsMaterials",
    "materialBean",
    "annualVolume",
    "annualVolumeOverridden",
    "productionLife",
    "productionLifeOverridden",
    "computedBatchSize",
    "batchSizeOverridden",
    "componentsPerProduct",
    "manuallyCosted",
    "availableCurrencyCodes",
    "manualCurrencyCode",
    "availableCurrencyVersions",
    "manualCurrencyVersion",
    "hasTargetCost",
    "hasTargetFinishMass",
    "machiningMode",
    "thicknessVisible",
    "compositesFileName"
})

@Schema(location = "ProductionCostSchema.json")
public class ProductionInfo {

    @JsonProperty("scenarioKey")
    private ProductionInfoScenario scenarioKey;
    @JsonProperty("compType")
    private String compType;
    @JsonProperty("initialized")
    private Boolean initialized;
    @JsonProperty("availablePgNames")
    private List<String> availablePgNames = null;
    @JsonProperty("processGroupName")
    private String processGroupName;
    @JsonProperty("pgEnabled")
    private Boolean pgEnabled;
    @JsonProperty("cadModelLoaded")
    private Boolean cadModelLoaded;
    @JsonProperty("vpeBean")
    private ProductionInfoVpe vpeBean;
    @JsonProperty("supportsMaterials")
    private Boolean supportsMaterials;
    @JsonProperty("materialBean")
    private ProductionInfoMaterial materialBean;
    @JsonProperty("annualVolume")
    private Integer annualVolume;
    @JsonProperty("annualVolumeOverridden")
    private Boolean annualVolumeOverridden;
    @JsonProperty("productionLife")
    private Integer productionLife;
    @JsonProperty("productionLifeOverridden")
    private Boolean productionLifeOverridden;
    @JsonProperty("computedBatchSize")
    private Integer computedBatchSize;
    @JsonProperty("batchSizeOverridden")
    private Boolean batchSizeOverridden;
    @JsonProperty("componentsPerProduct")
    private Integer componentsPerProduct;
    @JsonProperty("manuallyCosted")
    private Boolean manuallyCosted;
    @JsonProperty("availableCurrencyCodes")
    private List<String> availableCurrencyCodes = null;
    @JsonProperty("manualCurrencyCode")
    private String manualCurrencyCode;
    @JsonProperty("availableCurrencyVersions")
    private List<String> availableCurrencyVersions = null;
    @JsonProperty("manualCurrencyVersion")
    private String manualCurrencyVersion;
    @JsonProperty("hasTargetCost")
    private Boolean hasTargetCost;
    @JsonProperty("hasTargetFinishMass")
    private Boolean hasTargetFinishMass;
    @JsonProperty("machiningMode")
    private String machiningMode;
    @JsonProperty("thicknessVisible")
    private Boolean thicknessVisible;
    @JsonProperty("compositesFileName")
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