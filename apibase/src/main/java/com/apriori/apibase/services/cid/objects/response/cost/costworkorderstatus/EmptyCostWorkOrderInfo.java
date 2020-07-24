package com.apriori.apibase.services.cid.objects.response.cost.costworkorderstatus;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "items"
})

@Schema(location = "cid/CostWorkOrderStatusSchema.json")
public class EmptyCostWorkOrderInfo extends ArrayList<EmptyCostWorkOrderInfo> {

    @JsonProperty("items")
    private List<EmptyEmptyCostWorkOrderInfo> commandType;

    public List<EmptyEmptyCostWorkOrderInfo> getCommandType() {
        return commandType;
    }

    public void setCommandType(List<EmptyEmptyCostWorkOrderInfo> commandType) {
        this.commandType = commandType;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}