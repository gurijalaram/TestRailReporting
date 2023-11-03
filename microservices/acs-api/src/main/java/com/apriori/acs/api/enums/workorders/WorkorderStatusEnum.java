package com.apriori.acs.api.enums.workorders;

public enum WorkorderStatusEnum {

    FAILED("FAILED"),
    SUCCESS("SUCCESS");

    private final String workorderStatus;

    WorkorderStatusEnum(String workorderStatus) {
        this.workorderStatus = workorderStatus;
    }

    public String getWorkorderStatus() {
        return this.workorderStatus;
    }
}
