package com.apriori.acs.entity.response.workorders.upload;

import java.util.List;

public class FileWorkorder {
    private String action;
    private List<String> orderIds;

    public String getAction() {
        return action;
    }

    public FileWorkorder setAction(String action) {
        this.action = action;
        return this;
    }

    public List<String> getOrderIds() {
        return orderIds;
    }

    public FileWorkorder setOrderIds(List<String> orderIds) {
        this.orderIds = orderIds;
        return this;
    }
}