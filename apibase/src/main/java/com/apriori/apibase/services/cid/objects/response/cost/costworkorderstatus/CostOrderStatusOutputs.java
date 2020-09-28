package com.apriori.apibase.services.cid.objects.response.cost.costworkorderstatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "scenarioIterationKey",
    "costStatus"
})
public class CostOrderStatusOutputs {

    @JsonProperty("scenarioIterationKey")
    private CostOrderStatusScenarioIterationKey scenarioIterationKey;
    @JsonProperty("costStatus")
    private String costStatus;

    public CostOrderStatusScenarioIterationKey getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    public CostOrderStatusOutputs setScenarioIterationKey(CostOrderStatusScenarioIterationKey scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
        return this;
    }

    public String getCostStatus() {
        return costStatus;
    }

    public CostOrderStatusOutputs setCostStatus(String costStatus) {
        this.costStatus = costStatus;
        return this;
    }
}
