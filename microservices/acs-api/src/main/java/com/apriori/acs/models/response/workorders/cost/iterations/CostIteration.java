package com.apriori.acs.models.response.workorders.cost.iterations;

import com.apriori.annotations.Schema;

@Schema(location = "workorders/CostIterationResponseShort.json")
public class CostIteration {
    private IterationScenario scenarioIterationKey;
    private IterationScenarioKey scenarioKey;
    private boolean locked;
    private String url;
    private String resultsUrl;
    private String imageDataUrl;
    private String gcdInfoUrl;
    private String inputsUrl;
    private String keyState;

    public CostIteration setScenarioIterationKey(IterationScenario scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
        return this;
    }

    public IterationScenario getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    public IterationScenarioKey getScenarioKey() {
        return scenarioKey;
    }

    public CostIteration setScenarioKey(IterationScenarioKey scenarioKey) {
        this.scenarioKey = scenarioKey;
        return this;
    }

    public boolean isLocked() {
        return locked;
    }

    public CostIteration setLocked(boolean locked) {
        this.locked = locked;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public CostIteration setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getResultsUrl() {
        return resultsUrl;
    }

    public CostIteration setResultsUrl(String resultsUrl) {
        this.resultsUrl = resultsUrl;
        return this;
    }

    public String getImageDataUrl() {
        return imageDataUrl;
    }

    public CostIteration setImageDataUrl(String imageDataUrl) {
        this.imageDataUrl = imageDataUrl;
        return this;
    }

    public String getGcdInfoUrl() {
        return gcdInfoUrl;
    }

    public CostIteration setGcdInfoUrl(String gcdInfoUrl) {
        this.gcdInfoUrl = gcdInfoUrl;
        return this;
    }

    public String getInputsUrl() {
        return inputsUrl;
    }

    public CostIteration setInputsUrl(String inputsUrl) {
        this.inputsUrl = inputsUrl;
        return this;
    }

    public String getKeyState() {
        return keyState;
    }

    public CostIteration setKeyState(String keyState) {
        this.keyState = keyState;
        return this;
    }
}
