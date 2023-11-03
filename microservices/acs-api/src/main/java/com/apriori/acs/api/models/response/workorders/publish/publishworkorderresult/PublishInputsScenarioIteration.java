package com.apriori.acs.api.models.response.workorders.publish.publishworkorderresult;

public class PublishInputsScenarioIteration {
    private PublishInputsScenarioKey scenarioKey;
    private Integer iteration;

    public PublishInputsScenarioKey getScenarioKey() {
        return scenarioKey;
    }

    public PublishInputsScenarioIteration setScenarioKey(PublishInputsScenarioKey scenarioKey) {
        this.scenarioKey = scenarioKey;
        return this;
    }

    public Integer getIteration() {
        return iteration;
    }

    public PublishInputsScenarioIteration setIteration(Integer iteration) {
        this.iteration = iteration;
        return this;
    }
}
