package com.apriori.apibase.services.cid.objects.response.cost.costworkorderstatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "scenarioIterationKey",
    "inputSetId"
})
public class CostOrderStatusInputs {

    @JsonProperty("scenarioIterationKey")
    private CostOrderStatusScenarioIterationKey scenarioIterationKey;
    @JsonProperty("inputSetId")
    private Integer inputSetId;

    public CostOrderStatusScenarioIterationKey getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    public CostOrderStatusInputs setScenarioIterationKey(CostOrderStatusScenarioIterationKey scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
        return this;
    }

    public Integer getInputSetId() {
        return inputSetId;
    }

    public CostOrderStatusInputs setInputSetId(Integer inputSetId) {
        this.inputSetId = inputSetId;
        return this;
    }
}
