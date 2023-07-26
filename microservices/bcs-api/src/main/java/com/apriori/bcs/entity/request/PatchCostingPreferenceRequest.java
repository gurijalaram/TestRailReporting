package com.apriori.bcs.entity.request;

import com.apriori.annotations.Schema;
import com.apriori.bcs.entity.response.UserPreference;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("response")
@Schema(location = "UserPreferenceSchema.json")
public class PatchCostingPreferenceRequest {
    private UserPreference costingPreferences;
    private Double cadToleranceReplacement;
    private Double minCadToleranceThreshold;
    private String processGroup;
    private String scenarioName;
    private String selectionColor;
    private Boolean useCadToleranceThreshold;
    private Boolean useVpeForAllProcesses;
    private String vpeName;

    public UserPreference getCostingPreferences() {
        return costingPreferences;
    }

    public PatchCostingPreferenceRequest setCostingPreferences(UserPreference userPreferences) {
        this.costingPreferences = userPreferences;
        return this;
    }

    public Double getCadToleranceReplacement() {
        return cadToleranceReplacement;
    }

    public PatchCostingPreferenceRequest setCadToleranceReplacement(Double cadToleranceReplacement) {
        this.cadToleranceReplacement = cadToleranceReplacement;
        return this;
    }

    public Double getMinCadToleranceThreshold() {
        return minCadToleranceThreshold;
    }

    public PatchCostingPreferenceRequest setMinCadToleranceThreshold(Double minCadToleranceThreshold) {
        this.minCadToleranceThreshold = minCadToleranceThreshold;
        return this;
    }

    public String getProcessGroup() {
        return processGroup;
    }

    public PatchCostingPreferenceRequest setProcessGroup(String processGroup) {
        this.processGroup = processGroup;
        return this;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public PatchCostingPreferenceRequest setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
        return this;
    }

    public String getSelectionColor() {
        return selectionColor;
    }

    public PatchCostingPreferenceRequest setSelectionColor(String selectionColor) {
        this.selectionColor = selectionColor;
        return this;
    }

    public Boolean getUseCadToleranceThreshold() {
        return useCadToleranceThreshold;
    }

    public PatchCostingPreferenceRequest setUseCadToleranceThreshold(Boolean useCadToleranceThreshold) {
        this.useCadToleranceThreshold = useCadToleranceThreshold;
        return this;
    }

    public Boolean getUseVpeForAllProcesses() {
        return useVpeForAllProcesses;
    }

    public PatchCostingPreferenceRequest setUseVpeForAllProcesses(Boolean useVpeForAllProcesses) {
        this.useVpeForAllProcesses = useVpeForAllProcesses;
        return this;
    }

    public String getVpeName() {
        return vpeName;
    }

    public PatchCostingPreferenceRequest setVpeName(String vpeName) {
        this.vpeName = vpeName;
        return this;
    }
}
