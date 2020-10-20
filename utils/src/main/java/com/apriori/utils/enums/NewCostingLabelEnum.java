package com.apriori.utils.enums;

public enum NewCostingLabelEnum {

    COSTING_IN_PROGRESS("Costing in Progress"),
    UP_TO_DATE("Up To Date"),
    FETCHING("Fetching");

    private final String costingText;

    NewCostingLabelEnum(String costingLabel) {
        this.costingText = costingLabel;
    }

    public String getCostingText() {
        return this.costingText;
    }
}
