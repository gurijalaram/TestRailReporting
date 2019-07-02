package main.java.enums;

public enum CostingLabelEnum {

    COSTING_FAILURE("Costing Failure"),
    COSTING_INCOMPLETE("Costing Incomplete"),
    COSTING_UP_TO_DATE("Costing up to\n" + "Date"),
    READY_TO_COST("Ready to Cost"),
    UNCOSTED_CHANGES("Uncosted\n" + "Changes");

    private final String costingLabel;

    CostingLabelEnum(String costingLabel) {
        this.costingLabel = costingLabel;
    }

    public String getCostingLabel() {
        return this.costingLabel;
    }
}
