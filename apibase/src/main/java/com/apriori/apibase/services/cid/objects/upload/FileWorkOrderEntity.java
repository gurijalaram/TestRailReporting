package com.apriori.apibase.services.cid.objects.upload;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FileWorkOrderEntity {

    @JsonProperty("action")
    private String action;

    @JsonProperty("orderIds")
    private List<String> orderIds;

    @JsonProperty("action")

    public String getAction() {
        return action;
    }

    @JsonProperty("action")
    public FileWorkOrderEntity setAction(String action) {
        this.action = action;
        return this;
    }

    @JsonProperty("orderIds")
    public List<String> getOrderIds() {
        return orderIds;
    }

    @JsonProperty("orderIds")
    public FileWorkOrderEntity setOrderIds(List<String> orderIds) {
        this.orderIds = orderIds;
        return this;
    }
}
