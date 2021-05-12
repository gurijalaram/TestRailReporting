package com.apriori.apibase.services.bcs.objects;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "bcs/CisCostingPreferencesSchema.json")
public class CostingPreferences extends Pagination {
    private List<Tolerance> tolerances;
    private CostingPreferences response;
    private Boolean cadTolerances;
    private Double cadToleranceReplacement;
    private String processGroup;
    private String scenarioName;
    private Boolean useVpeForAllProcesses;
    private String vpeName;
    private Integer annualVolume;
    private Integer batchSize;
    private String materialCatalogName;
    private Double productionLife;
    private String batchMode;
    private Double minCadToleranceThreshold;
    private Boolean useCadToleranceThreshold;
    private String materialName;

    public String getMaterialName() {
        return materialName;
    }

    public CostingPreferences setMaterialName(String materialName) {
        this.materialName = materialName;
        return this;
    }

    public Boolean getUseCadToleranceThreshold() {
        return useCadToleranceThreshold;
    }

    public CostingPreferences setUseCadToleranceThreshold(Boolean useCadToleranceThreshold) {
        this.useCadToleranceThreshold = useCadToleranceThreshold;
        return this;
    }

    public Double getMinCadToleranceThreshold() {
        return minCadToleranceThreshold;
    }

    public CostingPreferences setMinCadToleranceThreshold(Double minCadToleranceThreshold) {
        this.minCadToleranceThreshold = minCadToleranceThreshold;
        return this;
    }

    public String getBatchMode() {
        return batchMode;
    }

    public CostingPreferences setBatchMode(String batchMode) {
        this.batchMode = batchMode;
        return this;
    }

    public String getMaterialCatalogName() {
        return materialCatalogName;
    }

    public CostingPreferences setMaterialCatalogName(String materialCatalogName) {
        this.materialCatalogName = materialCatalogName;
        return this;
    }

    public Double getProductionLife() {
        return productionLife;
    }

    public CostingPreferences setProductionLife(Double productionLife) {
        this.productionLife = productionLife;
        return this;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public CostingPreferences setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    public Integer getAnnualVolume() {
        return annualVolume;
    }

    public CostingPreferences setAnnualVolume(Integer annualVolume) {
        this.annualVolume = annualVolume;
        return this;
    }

    public Boolean getCadTolerances() {
        return this.cadTolerances;
    }

    public CostingPreferences setCadTolerances(Boolean cadTolerances) {
        this.cadTolerances = cadTolerances;
        return this;
    }

    public Boolean getUseVpeForAllProcesses() {
        return this.useVpeForAllProcesses;
    }

    public CostingPreferences setUseVpeForAllProcesses(Boolean useVpeForAllProcesses) {
        this.useVpeForAllProcesses = useVpeForAllProcesses;
        return this;
    }

    public String getVpeName() {
        return this.vpeName;
    }

    public CostingPreferences setVpeName(String vpeName) {
        this.vpeName = vpeName;
        return this;
    }

    public Double getCadToleranceReplacement() {
        return this.cadToleranceReplacement;
    }

    public CostingPreferences setCadToleranceReplacement(Double cadToleranceReplacement) {
        this.cadToleranceReplacement = cadToleranceReplacement;
        return this;
    }

    public String getProcessGroup() {
        return this.processGroup;
    }

    public CostingPreferences setProcessGroup(String processGroup) {
        this.processGroup = processGroup;
        return this;
    }

    public String getScenarioName() {
        return this.scenarioName;
    }

    public CostingPreferences setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
        return this;
    }

    public List<Tolerance> getTolerances() {
        return this.tolerances;
    }

    public CostingPreferences setTolerances(List<Tolerance> tolerances) {
        this.tolerances = tolerances;
        return this;
    }

    public CostingPreferences getResponse() {
        return this.response;
    }

    public CostingPreferences setResponse(CostingPreferences response) {
        this.response = response;
        return this;
    }
}