package com.apriori.utils.enums;

public enum CostingLabelEnum {

    COSTING_FAILURE("Costing" + "Failure"),
    COSTING_INCOMPLETE("Cost" + "Incomplete"),
    COSTING_UP_TO_DATE("Cost up to" + "Date"),
    COSTING_OUT_OF_DATE("Cost out of" + "Date"),
    READY_TO_COST("Ready to" + "Cost"),
    UNCOSTED_CHANGES("Uncosted" + "Changes"),
    COSTING_IN_PROGRESS("Costing in" + "Progress"),
    SAVING_NEW_SCENARIO("Saving New" + "Scenario"),
    TRANSLATING("Translating");

    private final String costingText;

    CostingLabelEnum(String costingLabel) {
        this.costingText = costingLabel;
    }

    public String getCostingText() {
        return this.costingText;
    }
}
