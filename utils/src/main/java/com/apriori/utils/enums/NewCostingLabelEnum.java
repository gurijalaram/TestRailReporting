package com.apriori.utils.enums;

public enum NewCostingLabelEnum {

    COSTING_IN_PROGRESS("Costing in Progress"),
    COST_UP_TO_DATE("Cost up To Date"),
    CREATED("Created"),
    FETCHING("Fetching"),
    NOT_COSTED("Not Costed"),
    UNCOSTED_CHANGES("Uncosted Changes");

    private final String costingText;

    NewCostingLabelEnum(String costingLabel) {
        this.costingText = costingLabel;
    }

    public String getCostingText() {
        return this.costingText;
    }
}
