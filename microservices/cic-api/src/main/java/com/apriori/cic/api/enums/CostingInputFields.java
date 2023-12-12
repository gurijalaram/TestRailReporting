package com.apriori.cic.api.enums;

public enum CostingInputFields {
    PROCESS_GROUP("Process Group"),
    MATERIAL("Material"),
    ANNUAL_VOLUME("Annual Volume"),
    BATCH_SIZE("Batch Size"),
    PRODUCTION_LIFE("Production Life"),
    DIGITAL_FACTORY("Digital Factory"),
    SCENARIO_NAME("Scenario Name"),
    DESCRIPTION("Description"),
    PART_NUMBER("partNumber"),
    TARGET_COST("targetCost"),
    TARGET_MASS("targetMass"),
    MACHINING_MODE("machiningMode"),
    CUSTOM_STRING("Custom String"),  //twxAttributeName : UDA1
    CUSTOM_NUMBER("Custom Number"), //twxAttributeName : UDA2
    CUSTOM_DATE("Custom Date"), //twxAttributeName : UDA3
    CUSTOM_MULTI("Custom Multi"); //twxAttributeName : UDA4

    private final String costingInputField;

    CostingInputFields(String ciField) {
        costingInputField = ciField;
    }

    public String getCostingInputField() {
        return costingInputField;
    }
}
