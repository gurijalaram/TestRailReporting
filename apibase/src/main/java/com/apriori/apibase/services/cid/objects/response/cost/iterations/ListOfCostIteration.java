package com.apriori.apibase.services.cid.objects.response.cost.iterations;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "items"
})
public class ListOfCostIteration {

    @JsonProperty("items")
    private List<IterationScenario> commandType;

    public List<IterationScenario> getCommandType() {
        return commandType;
    }

    public void setCommandType(List<IterationScenario> commandType) {
        this.commandType = commandType;
    }

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
}
