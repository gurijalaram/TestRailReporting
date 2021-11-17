package com.utils;

public enum CisColumnsEnum {

    THUMBNAIL("Thumbnail"),
    COMPONENT_NAME("Component Name"),
    SCENARIO_NAME("Scenario Name"),
    COMPONENT_TYPE("Component Type"),
    STATE("State"),
    PROCESS_GROUP("Process Group"),
    DIGITAL_FACTORY("Digital Factory"),
    CREATED_AT("Created At"),
    CREATED_BY("Created By"),
    ANNUAL_VOLUME("Annual Volume"),
    BATCH_SIZE("Batch Size"),
    DFM_RISK("DFM Risk"),
    FULLY_BURDENED_COST("Fully Burdened Cost");

    private final String columns;

    CisColumnsEnum(String columns) {
        this.columns = columns;
    }

    public String getColumns() {
        return this.columns;
    }
}
