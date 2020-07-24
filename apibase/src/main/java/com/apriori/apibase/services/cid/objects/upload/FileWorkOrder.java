package com.apriori.apibase.services.cid.objects.upload;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FileWorkOrder {

    @JsonProperty("action")
    private String action;

    @JsonProperty("orderIds")
    private List<String> orderIds;

    @JsonProperty("action")

    public String getAction() {
        return action;
    }

    @JsonProperty("action")
    public FileWorkOrder setAction(String action) {
        this.action = action;
        return this;
    }

    public List<String> getOrderIds() {
        return orderIds;
    }

    public FileWorkOrder setOrderIds(List<String> orderIds) {
        this.orderIds = orderIds;
        return this;
    }
}