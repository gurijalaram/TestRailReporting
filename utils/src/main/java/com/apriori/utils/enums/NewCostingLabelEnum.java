package com.apriori.utils.enums;

public enum NewCostingLabelEnum {

    COSTING_IN_PROGRESS("Costing in Progress"),
    UP_TO_DATE("Up To Date"),
    CREATED("Created"),
    FETCHING("Fetching"),
    UNCOSTED_SCENARIO("Uncosted Scenario"),
    UNCOSTED_CHANGES("Uncosted Changes");

    private final String costingText;

    NewCostingLabelEnum(String costingLabel) {
        this.costingText = costingLabel;
    }

    public String getCostingText() {
        return this.costingText;
    }
}
