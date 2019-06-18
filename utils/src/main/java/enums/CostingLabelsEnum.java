package main.java.enums;

public enum CostingLabelsEnum {

    COSTING_FAILURE("Costing Failure"),
    COSTING_INCOMPLETE("Costing Incomplete"),
    COSTING_UP_TO_DATE("Costing up to Date"),
    READY_TO_COST("Ready to Cost"),
    UNCOSTED_CHANGES("Uncosted Changes");

    private final String costingLabel;

    CostingLabelsEnum(String costingLabel) {
        this.costingLabel = costingLabel;
    }

    public String getCostingLabel() {
        return this.costingLabel;
    }
}
