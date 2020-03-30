package com.apriori.apibase.http.builder.common.response.common;

import com.apriori.apibase.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Schema(location = "SubmitWorkOredrResponseSchema.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubmitWorkOredr {

    @JsonProperty
    private String id;

    @JsonProperty
    private boolean resourceCreated;

    @JsonProperty
    private String action;

    @JsonProperty
    private List<String> orderIds;

    public String getAction() {
        return action;
    }

    public SubmitWorkOredr setAction(String action) {
        this.action = action;
        return this;
    }

    public List<String> getOrderIds() {
        return orderIds;
    }

    public SubmitWorkOredr setOrderIds(List<String> orderIds) {
        this.orderIds = orderIds;
        return this;
    }

    public String getId() {
        return id;
    }

    public SubmitWorkOredr setId(String id) {
        this.id = id;
        return this;
    }

    public boolean isResourceCreated() {
        return resourceCreated;
    }

    public SubmitWorkOredr setResourceCreated(boolean resourceCreated) {
        this.resourceCreated = resourceCreated;
        return this;
    }
}
