package main.java.enums;

public enum CostingLabelEnum {

    COSTING_FAILURE("Costing\n" + "Failure"),
    COSTING_INCOMPLETE("Cost Incomplete"),
    COSTING_UP_TO_DATE("Cost up to\n" + "Date"),
    READY_TO_COST("Ready to Cost"),
    UNCOSTED_CHANGES("Uncosted\n" + "Changes"),
    COSTING_IN_PROGRESS("Costing in\n" + "Progress");

    private final String costingLabel;

    CostingLabelEnum(String costingLabel) {
        this.costingLabel = costingLabel;
    }

    public String getCostingLabel() {
        return this.costingLabel;
    }
}
