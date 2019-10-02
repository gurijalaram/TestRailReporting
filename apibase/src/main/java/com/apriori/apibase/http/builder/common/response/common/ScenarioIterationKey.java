package com.apriori.apibase.http.builder.common.response.common;


import com.fasterxml.jackson.annotation.JsonProperty;

public class ScenarioIterationKey {

    @JsonProperty
    private Integer iteration;

    @JsonProperty
    private ScenarioKey scenarioKey;

    public Integer getIteration() {
        return iteration;
    }

    public ScenarioIterationKey setIteration(Integer iteration) {
        this.iteration = iteration;
        return this;
    }

    public ScenarioKey getScenarioKey() {
        return scenarioKey;
    }

    public ScenarioIterationKey setScenarioKey(ScenarioKey scenarioKey) {
        this.scenarioKey = scenarioKey;
        return this;
    }
}
