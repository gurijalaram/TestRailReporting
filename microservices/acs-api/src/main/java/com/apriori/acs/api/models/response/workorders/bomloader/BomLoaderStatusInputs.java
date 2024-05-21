package com.apriori.acs.api.models.response.workorders.bomloader;

import com.apriori.acs.api.models.response.workorders.genericclasses.ScenarioIterationKey;

public class BomLoaderStatusInputs {
    private String defaultScenarioProcessingRule;
    private ScenarioIterationKey scenarioIterationKey;
    private BomLoaderMapping mapping;

    public ScenarioIterationKey getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    public BomLoaderStatusInputs setScenarioIterationKey(ScenarioIterationKey scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
        return this;
    }

    public BomLoaderMapping getInputSetId() {
        return mapping;
    }

    public BomLoaderStatusInputs setmapping(BomLoaderMapping mapping) {
        this.mapping = mapping;
        return this;
    }
}
