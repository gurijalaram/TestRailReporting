package com.apriori.apibase.services.cid.objects.response.publish.publishworkorderstatus;

public class PublishStatusScenarioIterationKey {
    private PublishStatusScenarioKey scenarioKey;
    private Integer iteration;

    public PublishStatusScenarioKey getScenarioKey() {
        return scenarioKey;
    }

    public PublishStatusScenarioIterationKey setScenarioKey(PublishStatusScenarioKey scenarioKey) {
        this.scenarioKey = scenarioKey;
        return this;
    }

    public Integer getIteration() {
        return iteration;
    }

    public PublishStatusScenarioIterationKey setIteration(Integer iteration) {
        this.iteration = iteration;
        return this;
    }
}