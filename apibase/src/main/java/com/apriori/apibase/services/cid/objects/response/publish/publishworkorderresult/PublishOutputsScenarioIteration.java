package com.apriori.apibase.services.cid.objects.response.publish.publishworkorderresult;

public class PublishOutputsScenarioIteration {
    private PublishOutputsScenarioKey scenarioKey;
    private Integer iteration;

    public PublishOutputsScenarioKey getScenarioKey() {
        return scenarioKey;
    }

    public PublishOutputsScenarioIteration setScenarioKey(PublishOutputsScenarioKey scenarioKey) {
        this.scenarioKey = scenarioKey;
        return this;
    }

    public Integer getIteration() {
        return iteration;
    }

    public PublishOutputsScenarioIteration setIteration(Integer iteration) {
        this.iteration = iteration;
        return this;
    }
}