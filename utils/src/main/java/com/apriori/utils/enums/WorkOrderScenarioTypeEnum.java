package com.apriori.utils.enums;

public enum WorkOrderScenarioTypeEnum {

    PART("PART"),
    ASSEMBLY("ASSEMBLY");


    private final String type;

    WorkOrderScenarioTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
