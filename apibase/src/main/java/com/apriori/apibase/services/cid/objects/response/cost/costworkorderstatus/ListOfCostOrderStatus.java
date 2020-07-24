package com.apriori.apibase.services.cid.objects.response.cost.costworkorderstatus;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListOfCostOrderStatus {

    @JsonProperty("items")
    private List<CostOrderStatusInfo> commandType;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    public List<CostOrderStatusInfo> getCommandType() {
        return commandType;
    }

    public void setCommandType(List<CostOrderStatusInfo> commandType) {
        this.commandType = commandType;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
