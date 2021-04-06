package com.apriori.entity.response.cost.iterations;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "cid/CostIterationResponseShort.json")
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

    public void setScenarioIterationKey(IterationScenario scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
    }

    public IterationScenario getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    public IterationScenarioKey getScenarioKey() {
        return scenarioKey;
    }

    public void setScenarioKey(IterationScenarioKey scenarioKey) {
        this.scenarioKey = scenarioKey;
    }


    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResultsUrl() {
        return resultsUrl;
    }

    public void setResultsUrl(String resultsUrl) {
        this.resultsUrl = resultsUrl;
    }

    public String getImageDataUrl() {
        return imageDataUrl;
    }

    public void setImageDataUrl(String imageDataUrl) {
        this.imageDataUrl = imageDataUrl;
    }

    public String getGcdInfoUrl() {
        return gcdInfoUrl;
    }

    public void setGcdInfoUrl(String gcdInfoUrl) {
        this.gcdInfoUrl = gcdInfoUrl;
    }

    public String getInputsUrl() {
        return inputsUrl;
    }

    public void setInputsUrl(String inputsUrl) {
        this.inputsUrl = inputsUrl;
    }

    public String getKeyState() {
        return keyState;
    }

    public void setKeyState(String keyState) {
        this.keyState = keyState;
    }
}
