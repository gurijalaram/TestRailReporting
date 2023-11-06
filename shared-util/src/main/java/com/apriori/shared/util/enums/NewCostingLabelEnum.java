package com.apriori.shared.util.enums;

public enum NewCostingLabelEnum {

    COSTING_IN_PROGRESS("Costing in Progress"),
    COST_UP_TO_DATE("Cost up to Date"),
    COST_INCOMPLETE("Cost Incomplete"),
    COST_COMPLETE("Cost Complete"),
    CREATED("Created"),
    FETCHING("Fetching"),
    NOT_COSTED("Not Costed"),
    UNCOSTED_CHANGES("Uncosted Changes"),
    PROCESSING_EDIT_ACTION("Processing Edit Action"),
    PROCESSING_PUBLISH_ACTION("Processing Publish Action"),
    PROCESSING_UPDATE_ACTION("Processing Update Action"),
    PROCESSING_UPDATE_CAD("Processing Update Cad File Action"),
    COSTING_FAILED("Costing Failed"),
    PROCESSING_CREATE_ACTION("Processing Create Action"),
    PROCESSING_REPORT_ACTION("Processing Report Action");

    private final String costingText;

    NewCostingLabelEnum(String costingLabel) {
        this.costingText = costingLabel;
    }

    public String getCostingText() {
        return this.costingText;
    }
}