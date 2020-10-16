package com.apriori.utils.enums;

public enum CostingLabelEnum {

    COSTING_FAILURE("Costing\n" + "Failure"),
    COSTING_INCOMPLETE("Cost\n" + "Incomplete"),
    COSTING_UP_TO_DATE("Cost up to\n" + "Date"),
    COSTING_OUT_OF_DATE("Cost out of\n" + "Date"),
    READY_TO_COST("Ready to\n" + "Cost"),
    UNCOSTED_CHANGES("Uncosted\n" + "Changes"),
    COSTING_IN_PROGRESS("Costing in\n" + "Progress"),
    SAVING_NEW_SCENARIO("Saving New\n" + "Scenario"),
    TRANSLATING("Translating"),
    UP_TO_DATE("Up To Date"),
    FETCHING("Fetching");

    private final String costingText;

    CostingLabelEnum(String costingLabel) {
        this.costingText = costingLabel;
    }

    public String getCostingText() {
        return this.costingText;
    }
}
