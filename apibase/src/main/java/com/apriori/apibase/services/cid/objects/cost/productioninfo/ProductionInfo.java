package com.apriori.apibase.services.cid.objects.cost.productioninfo;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private ScenarioKey scenarioKey;
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
    private ScenarioKey_ vpeBean;
    @JsonProperty("supportsMaterials")
    private Boolean supportsMaterials;
    @JsonProperty("materialBean")
    private MaterialBean materialBean;
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
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("scenarioKey")
    public ScenarioKey getScenarioKey() {
        return scenarioKey;
    }

    @JsonProperty("scenarioKey")
    public ProductionInfo setScenarioKey(ScenarioKey scenarioKey) {
        this.scenarioKey = scenarioKey;
        return this;
    }

    @JsonProperty("compType")
    public String getCompType() {
        return compType;
    }

    @JsonProperty("compType")
    public ProductionInfo setCompType(String compType) {
        this.compType = compType;
        return this;
    }

    @JsonProperty("initialized")
    public Boolean getInitialized() {
        return initialized;
    }

    @JsonProperty("initialized")
    public ProductionInfo setInitialized(Boolean initialized) {
        this.initialized = initialized;
        return this;
    }

    @JsonProperty("availablePgNames")
    public List<String> getAvailablePgNames() {
        return availablePgNames;
    }

    @JsonProperty("availablePgNames")
    public ProductionInfo setAvailablePgNames(List<String> availablePgNames) {
        this.availablePgNames = availablePgNames;
        return this;
    }

    @JsonProperty("processGroupName")
    public String getProcessGroupName() {
        return processGroupName;
    }

    @JsonProperty("processGroupName")
    public ProductionInfo setProcessGroupName(String processGroupName) {
        this.processGroupName = processGroupName;
        return this;
    }

    @JsonProperty("pgEnabled")
    public Boolean getPgEnabled() {
        return pgEnabled;
    }

    @JsonProperty("pgEnabled")
    public ProductionInfo setPgEnabled(Boolean pgEnabled) {
        this.pgEnabled = pgEnabled;
        return this;
    }

    @JsonProperty("cadModelLoaded")
    public Boolean getCadModelLoaded() {
        return cadModelLoaded;
    }

    @JsonProperty("cadModelLoaded")
    public ProductionInfo setCadModelLoaded(Boolean cadModelLoaded) {
        this.cadModelLoaded = cadModelLoaded;
        return this;
    }

    @JsonProperty("vpeBean")
    public ScenarioKey_ getVpeBean() {
        return vpeBean;
    }

    @JsonProperty("vpeBean")
    public ProductionInfo setVpeBean(ScenarioKey_ vpeBean) {
        this.vpeBean = vpeBean;
        return this;
    }

    @JsonProperty("supportsMaterials")
    public Boolean getSupportsMaterials() {
        return supportsMaterials;
    }

    @JsonProperty("supportsMaterials")
    public ProductionInfo setSupportsMaterials(Boolean supportsMaterials) {
        this.supportsMaterials = supportsMaterials;
        return this;
    }

    @JsonProperty("materialBean")
    public MaterialBean getMaterialBean() {
        return materialBean;
    }

    @JsonProperty("materialBean")
    public ProductionInfo setMaterialBean(MaterialBean materialBean) {
        this.materialBean = materialBean;
        return this;
    }

    @JsonProperty("annualVolume")
    public Integer getAnnualVolume() {
        return annualVolume;
    }

    @JsonProperty("annualVolume")
    public ProductionInfo setAnnualVolume(Integer annualVolume) {
        this.annualVolume = annualVolume;
        return this;
    }

    @JsonProperty("annualVolumeOverridden")
    public Boolean getAnnualVolumeOverridden() {
        return annualVolumeOverridden;
    }

    @JsonProperty("annualVolumeOverridden")
    public ProductionInfo setAnnualVolumeOverridden(Boolean annualVolumeOverridden) {
        this.annualVolumeOverridden = annualVolumeOverridden;
        return this;
    }

    @JsonProperty("productionLife")
    public Integer getProductionLife() {
        return productionLife;
    }

    @JsonProperty("productionLife")
    public ProductionInfo setProductionLife(Integer productionLife) {
        this.productionLife = productionLife;
        return this;
    }

    @JsonProperty("productionLifeOverridden")
    public Boolean getProductionLifeOverridden() {
        return productionLifeOverridden;
    }

    @JsonProperty("productionLifeOverridden")
    public ProductionInfo setProductionLifeOverridden(Boolean productionLifeOverridden) {
        this.productionLifeOverridden = productionLifeOverridden;
        return this;
    }

    @JsonProperty("computedBatchSize")
    public Integer getComputedBatchSize() {
        return computedBatchSize;
    }

    @JsonProperty("computedBatchSize")
    public ProductionInfo setComputedBatchSize(Integer computedBatchSize) {
        this.computedBatchSize = computedBatchSize;
        return this;
    }

    @JsonProperty("batchSizeOverridden")
    public Boolean getBatchSizeOverridden() {
        return batchSizeOverridden;
    }

    @JsonProperty("batchSizeOverridden")
    public ProductionInfo setBatchSizeOverridden(Boolean batchSizeOverridden) {
        this.batchSizeOverridden = batchSizeOverridden;
        return this;
    }

    @JsonProperty("componentsPerProduct")
    public Integer getComponentsPerProduct() {
        return componentsPerProduct;
    }

    @JsonProperty("componentsPerProduct")
    public ProductionInfo setComponentsPerProduct(Integer componentsPerProduct) {
        this.componentsPerProduct = componentsPerProduct;
        return this;
    }

    @JsonProperty("manuallyCosted")
    public Boolean getManuallyCosted() {
        return manuallyCosted;
    }

    @JsonProperty("manuallyCosted")
    public ProductionInfo setManuallyCosted(Boolean manuallyCosted) {
        this.manuallyCosted = manuallyCosted;
        return this;
    }

    @JsonProperty("availableCurrencyCodes")
    public List<String> getAvailableCurrencyCodes() {
        return availableCurrencyCodes;
    }

    @JsonProperty("availableCurrencyCodes")
    public ProductionInfo setAvailableCurrencyCodes(List<String> availableCurrencyCodes) {
        this.availableCurrencyCodes = availableCurrencyCodes;
        return this;
    }

    @JsonProperty("manualCurrencyCode")
    public String getManualCurrencyCode() {
        return manualCurrencyCode;
    }

    @JsonProperty("manualCurrencyCode")
    public ProductionInfo setManualCurrencyCode(String manualCurrencyCode) {
        this.manualCurrencyCode = manualCurrencyCode;
        return this;
    }

    @JsonProperty("availableCurrencyVersions")
    public List<String> getAvailableCurrencyVersions() {
        return availableCurrencyVersions;
    }

    @JsonProperty("availableCurrencyVersions")
    public ProductionInfo setAvailableCurrencyVersions(List<String> availableCurrencyVersions) {
        this.availableCurrencyVersions = availableCurrencyVersions;
        return this;
    }

    @JsonProperty("manualCurrencyVersion")
    public String getManualCurrencyVersion() {
        return manualCurrencyVersion;
    }

    @JsonProperty("manualCurrencyVersion")
    public ProductionInfo setManualCurrencyVersion(String manualCurrencyVersion) {
        this.manualCurrencyVersion = manualCurrencyVersion;
        return this;
    }

    @JsonProperty("hasTargetCost")
    public Boolean getHasTargetCost() {
        return hasTargetCost;
    }

    @JsonProperty("hasTargetCost")
    public ProductionInfo setHasTargetCost(Boolean hasTargetCost) {
        this.hasTargetCost = hasTargetCost;
        return this;
    }

    @JsonProperty("hasTargetFinishMass")
    public Boolean getHasTargetFinishMass() {
        return hasTargetFinishMass;
    }

    @JsonProperty("hasTargetFinishMass")
    public ProductionInfo setHasTargetFinishMass(Boolean hasTargetFinishMass) {
        this.hasTargetFinishMass = hasTargetFinishMass;
        return this;
    }

    @JsonProperty("machiningMode")
    public String getMachiningMode() {
        return machiningMode;
    }

    @JsonProperty("machiningMode")
    public ProductionInfo setMachiningMode(String machiningMode) {
        this.machiningMode = machiningMode;
        return this;
    }

    @JsonProperty("thicknessVisible")
    public Boolean getThicknessVisible() {
        return thicknessVisible;
    }

    @JsonProperty("thicknessVisible")
    public ProductionInfo setThicknessVisible(Boolean thicknessVisible) {
        this.thicknessVisible = thicknessVisible;
        return this;
    }

    @JsonProperty("compositesFileName")
    public String getCompositesFileName() {
        return compositesFileName;
    }

    @JsonProperty("compositesFileName")
    public ProductionInfo setCompositesFileName(String compositesFileName) {
        this.compositesFileName = compositesFileName;
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
