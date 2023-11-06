package com.apriori.bcs.api.models.request;

public class UpdateCostingPreferencesRequest {
    private Double cadToleranceReplacement;
    private Double minCadToleranceThreshold;
    private String processGroup;
    private String scenarioName;
    private String selectionColor;
    private Boolean useCadToleranceThreshold;
    private Boolean useVpeForAllProcesses;
    private String vpeName;
    private UpdateCostingPreferencesRequest costingPreferences;

    public Double getCadToleranceReplacement() {
        return this.cadToleranceReplacement;
    }

    public UpdateCostingPreferencesRequest setCadToleranceReplacement(Double cadToleranceReplacement) {
        this.cadToleranceReplacement = cadToleranceReplacement;
        return this;
    }

    public Double getMinCadToleranceThreshold() {
        return this.minCadToleranceThreshold;
    }

    public UpdateCostingPreferencesRequest setMinCadToleranceThreshold(Double minCadToleranceThreshold) {
        this.minCadToleranceThreshold = minCadToleranceThreshold;
        return this;
    }

    public String getProcessGroup() {
        return this.processGroup;
    }

    public UpdateCostingPreferencesRequest setProcessGroup(String processGroup) {
        this.processGroup = processGroup;
        return this;
    }

    public String getScenarioName() {
        return this.scenarioName;
    }

    public UpdateCostingPreferencesRequest setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
        return this;
    }

    public String getSelectionColor() {
        return this.selectionColor;
    }

    public UpdateCostingPreferencesRequest setSelectionColor(String selectionColor) {
        this.selectionColor = selectionColor;
        return this;
    }

    public Boolean getUseCadToleranceThreshold() {
        return this.useCadToleranceThreshold;
    }

    public UpdateCostingPreferencesRequest setUseCadToleranceThreshold(Boolean useCadToleranceThreshold) {
        this.useCadToleranceThreshold = useCadToleranceThreshold;
        return this;
    }

    public Boolean getUseVpeForAllProcesses() {
        return this.useVpeForAllProcesses;
    }

    public UpdateCostingPreferencesRequest setUseVpeForAllProcesses(Boolean useVpeForAllProcesses) {
        this.useVpeForAllProcesses = useVpeForAllProcesses;
        return this;
    }

    public String getVpeName() {
        return this.vpeName;
    }

    public UpdateCostingPreferencesRequest setVpeName(String vpeName) {
        this.vpeName = vpeName;
        return this;
    }

    public UpdateCostingPreferencesRequest getCostingPreferences() {
        return this.costingPreferences;
    }

    public UpdateCostingPreferencesRequest setCostingPreferences(UpdateCostingPreferencesRequest costingPreferences) {
        this.costingPreferences = costingPreferences;
        return this;
    }
}
