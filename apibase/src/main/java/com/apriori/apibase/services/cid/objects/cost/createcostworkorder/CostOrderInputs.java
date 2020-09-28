package com.apriori.apibase.services.cid.objects.cost.createcostworkorder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "inputSetId",
    "scenarioIterationKey"
})
public class CostOrderInputs {

    @JsonProperty("inputSetId")
    private Integer inputSetId;
    @JsonProperty("scenarioIterationKey")
    private CostOrderScenarioIteration scenarioIterationKey;

    public Integer getInputSetId() {
        return inputSetId;
    }

    public CostOrderInputs setInputSetId(Integer inputSetId) {
        this.inputSetId = inputSetId;
        return this;
    }

    public CostOrderScenarioIteration getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    public CostOrderInputs setScenarioIterationKey(CostOrderScenarioIteration scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
        return this;
    }
}