package com.apriori.utils.enums;

public enum CostingLabelEnum {

    COSTING_FAILURE("Costing\n" + "Failure"),
    COSTING_INCOMPLETE("Cost\n" + "Incomplete"),
    COSTING_UP_TO_DATE("Cost up to\n" + "Date"),
    READY_TO_COST("Ready to\n" + "Cost"),
    UNCOSTED_CHANGES("Uncosted\n" + "Changes"),
    COSTING_IN_PROGRESS("Costing in\n" + "Progress");

    private final String costingText;

    CostingLabelEnum(String costingLabel) {
        this.costingText = costingLabel;
    }

    public String getCostingText() {
        return this.costingText;
    }
}
