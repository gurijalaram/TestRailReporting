package com.apriori.cis.ui.utils;

public enum CisProjectStatusEnum {

    OPEN("Open"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed");

    private final String status;

    CisProjectStatusEnum(String order) {
        this.status = order;
    }

    public String getStatus() {
        return this.status;
    }
}
