package com.apriori.apibase.services.response.objects;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Schema(location = "SubmitWorkorderResponseSchema.json")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubmitWorkOrder {

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

    public SubmitWorkOrder setAction(String action) {
        this.action = action;
        return this;
    }

    public List<String> getOrderIds() {
        return orderIds;
    }

    public SubmitWorkOrder setOrderIds(List<String> orderIds) {
        this.orderIds = orderIds;
        return this;
    }

    public String getId() {
        return id;
    }

    public SubmitWorkOrder setId(String id) {
        this.id = id;
        return this;
    }

    public boolean isResourceCreated() {
        return resourceCreated;
    }

    public SubmitWorkOrder setResourceCreated(boolean resourceCreated) {
        this.resourceCreated = resourceCreated;
        return this;
    }
}
