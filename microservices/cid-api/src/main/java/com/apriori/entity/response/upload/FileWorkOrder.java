package com.apriori.entity.response.upload;

import java.util.List;

public class FileWorkOrder {
    private String action;
    private List<String> orderIds;

    public String getAction() {
        return action;
    }

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