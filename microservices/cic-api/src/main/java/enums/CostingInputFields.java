package enums;

public enum CostingInputFields {
    PROCESS_GROUP("Process Group"),
    MATERIAL("Material"),
    ANNUAL_VOLUME("Annual Volume"),
    BATCH_SIZE("Batch Size"),
    PRODUCTION_LIFE("Production Life"),
    DIGITAL_FACTORY("Digital Factory"),
    SCENARIO_NAME("Scenario Name"),
    CUSTOM_MULTI("Custom Multi"),
    DESCRIPTION("Description"),
    PART_NUMBER("partNumber");


    private final String costingInputField;

    CostingInputFields(String ciField) {
        costingInputField = ciField;
    }

    public String getCostingInputField() {
        return costingInputField;
    }
}
