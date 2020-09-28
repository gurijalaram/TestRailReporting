package com.apriori.apibase.services.cid.objects.response.publish.publishworkorderresult;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "scenarioIterationKey"
})
public class PublishResultOutputs {

    @JsonProperty("scenarioIterationKey")
    private PublishOutputsScenarioIteration scenarioIterationKey;

    public PublishOutputsScenarioIteration getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    public PublishResultOutputs setScenarioIterationKey(PublishOutputsScenarioIteration scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
        return this;
    }
}
