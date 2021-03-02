package com.apriori.entity.request.publish.createpublishworkorder;

public class PublishScenarioIterationKey {
    private PublishScenarioKey scenarioKey;
    private Integer iteration;

    public PublishScenarioKey getScenarioKey() {
        return scenarioKey;
    }

    public PublishScenarioIterationKey setScenarioKey(PublishScenarioKey scenarioKey) {
        this.scenarioKey = scenarioKey;
        return this;
    }

    public Integer getIteration() {
        return iteration;
    }

    public PublishScenarioIterationKey setIteration(Integer iteration) {
        this.iteration = iteration;
        return this;
    }
}