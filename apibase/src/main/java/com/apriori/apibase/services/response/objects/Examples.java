package com.apriori.apibase.services.response.objects;

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
public class Examples {

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
    private VpeBean vpeBean;
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
    public void setScenarioKey(ScenarioKey scenarioKey) {
        this.scenarioKey = scenarioKey;
    }

    @JsonProperty("compType")
    public String getCompType() {
        return compType;
    }

    @JsonProperty("compType")
    public void setCompType(String compType) {
        this.compType = compType;
    }

    @JsonProperty("initialized")
    public Boolean getInitialized() {
        return initialized;
    }

    @JsonProperty("initialized")
    public void setInitialized(Boolean initialized) {
        this.initialized = initialized;
    }

    @JsonProperty("availablePgNames")
    public List<String> getAvailablePgNames() {
        return availablePgNames;
    }

    @JsonProperty("availablePgNames")
    public void setAvailablePgNames(List<String> availablePgNames) {
        this.availablePgNames = availablePgNames;
    }

    @JsonProperty("processGroupName")
    public String getProcessGroupName() {
        return processGroupName;
    }

    @JsonProperty("processGroupName")
    public void setProcessGroupName(String processGroupName) {
        this.processGroupName = processGroupName;
    }

    @JsonProperty("pgEnabled")
    public Boolean getPgEnabled() {
        return pgEnabled;
    }

    @JsonProperty("pgEnabled")
    public void setPgEnabled(Boolean pgEnabled) {
        this.pgEnabled = pgEnabled;
    }

    @JsonProperty("cadModelLoaded")
    public Boolean getCadModelLoaded() {
        return cadModelLoaded;
    }

    @JsonProperty("cadModelLoaded")
    public void setCadModelLoaded(Boolean cadModelLoaded) {
        this.cadModelLoaded = cadModelLoaded;
    }

    @JsonProperty("vpeBean")
    public VpeBean getVpeBean() {
        return vpeBean;
    }

    @JsonProperty("vpeBean")
    public void setVpeBean(VpeBean vpeBean) {
        this.vpeBean = vpeBean;
    }

    @JsonProperty("supportsMaterials")
    public Boolean getSupportsMaterials() {
        return supportsMaterials;
    }

    @JsonProperty("supportsMaterials")
    public void setSupportsMaterials(Boolean supportsMaterials) {
        this.supportsMaterials = supportsMaterials;
    }

    @JsonProperty("materialBean")
    public MaterialBean getMaterialBean() {
        return materialBean;
    }

    @JsonProperty("materialBean")
    public void setMaterialBean(MaterialBean materialBean) {
        this.materialBean = materialBean;
    }

    @JsonProperty("annualVolume")
    public Integer getAnnualVolume() {
        return annualVolume;
    }

    @JsonProperty("annualVolume")
    public void setAnnualVolume(Integer annualVolume) {
        this.annualVolume = annualVolume;
    }

    @JsonProperty("annualVolumeOverridden")
    public Boolean getAnnualVolumeOverridden() {
        return annualVolumeOverridden;
    }

    @JsonProperty("annualVolumeOverridden")
    public void setAnnualVolumeOverridden(Boolean annualVolumeOverridden) {
        this.annualVolumeOverridden = annualVolumeOverridden;
    }

    @JsonProperty("productionLife")
    public Integer getProductionLife() {
        return productionLife;
    }

    @JsonProperty("productionLife")
    public void setProductionLife(Integer productionLife) {
        this.productionLife = productionLife;
    }

    @JsonProperty("productionLifeOverridden")
    public Boolean getProductionLifeOverridden() {
        return productionLifeOverridden;
    }

    @JsonProperty("productionLifeOverridden")
    public void setProductionLifeOverridden(Boolean productionLifeOverridden) {
        this.productionLifeOverridden = productionLifeOverridden;
    }

    @JsonProperty("computedBatchSize")
    public Integer getComputedBatchSize() {
        return computedBatchSize;
    }

    @JsonProperty("computedBatchSize")
    public void setComputedBatchSize(Integer computedBatchSize) {
        this.computedBatchSize = computedBatchSize;
    }

    @JsonProperty("batchSizeOverridden")
    public Boolean getBatchSizeOverridden() {
        return batchSizeOverridden;
    }

    @JsonProperty("batchSizeOverridden")
    public void setBatchSizeOverridden(Boolean batchSizeOverridden) {
        this.batchSizeOverridden = batchSizeOverridden;
    }

    @JsonProperty("componentsPerProduct")
    public Integer getComponentsPerProduct() {
        return componentsPerProduct;
    }

    @JsonProperty("componentsPerProduct")
    public void setComponentsPerProduct(Integer componentsPerProduct) {
        this.componentsPerProduct = componentsPerProduct;
    }

    @JsonProperty("manuallyCosted")
    public Boolean getManuallyCosted() {
        return manuallyCosted;
    }

    @JsonProperty("manuallyCosted")
    public void setManuallyCosted(Boolean manuallyCosted) {
        this.manuallyCosted = manuallyCosted;
    }

    @JsonProperty("availableCurrencyCodes")
    public List<String> getAvailableCurrencyCodes() {
        return availableCurrencyCodes;
    }

    @JsonProperty("availableCurrencyCodes")
    public void setAvailableCurrencyCodes(List<String> availableCurrencyCodes) {
        this.availableCurrencyCodes = availableCurrencyCodes;
    }

    @JsonProperty("manualCurrencyCode")
    public String getManualCurrencyCode() {
        return manualCurrencyCode;
    }

    @JsonProperty("manualCurrencyCode")
    public void setManualCurrencyCode(String manualCurrencyCode) {
        this.manualCurrencyCode = manualCurrencyCode;
    }

    @JsonProperty("availableCurrencyVersions")
    public List<String> getAvailableCurrencyVersions() {
        return availableCurrencyVersions;
    }

    @JsonProperty("availableCurrencyVersions")
    public void setAvailableCurrencyVersions(List<String> availableCurrencyVersions) {
        this.availableCurrencyVersions = availableCurrencyVersions;
    }

    @JsonProperty("manualCurrencyVersion")
    public String getManualCurrencyVersion() {
        return manualCurrencyVersion;
    }

    @JsonProperty("manualCurrencyVersion")
    public void setManualCurrencyVersion(String manualCurrencyVersion) {
        this.manualCurrencyVersion = manualCurrencyVersion;
    }

    @JsonProperty("hasTargetCost")
    public Boolean getHasTargetCost() {
        return hasTargetCost;
    }

    @JsonProperty("hasTargetCost")
    public void setHasTargetCost(Boolean hasTargetCost) {
        this.hasTargetCost = hasTargetCost;
    }

    @JsonProperty("hasTargetFinishMass")
    public Boolean getHasTargetFinishMass() {
        return hasTargetFinishMass;
    }

    @JsonProperty("hasTargetFinishMass")
    public void setHasTargetFinishMass(Boolean hasTargetFinishMass) {
        this.hasTargetFinishMass = hasTargetFinishMass;
    }

    @JsonProperty("machiningMode")
    public String getMachiningMode() {
        return machiningMode;
    }

    @JsonProperty("machiningMode")
    public void setMachiningMode(String machiningMode) {
        this.machiningMode = machiningMode;
    }

    @JsonProperty("thicknessVisible")
    public Boolean getThicknessVisible() {
        return thicknessVisible;
    }

    @JsonProperty("thicknessVisible")
    public void setThicknessVisible(Boolean thicknessVisible) {
        this.thicknessVisible = thicknessVisible;
    }

    @JsonProperty("compositesFileName")
    public String getCompositesFileName() {
        return compositesFileName;
    }

    @JsonProperty("compositesFileName")
    public void setCompositesFileName(String compositesFileName) {
        this.compositesFileName = compositesFileName;
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
