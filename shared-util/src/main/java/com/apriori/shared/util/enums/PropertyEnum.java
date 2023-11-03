package com.apriori.shared.util.enums;

import java.util.EnumSet;

public enum PropertyEnum {
    //INPUT GROUP
    ANNUAL_VOLUME("Annual Volume"),
    BATCH_SIZE("Batch Size"),
    MATERIAL("Material"),
    PRODUCTION_LIFE("Production Life"),
    COMPONENT_NAME("Component Name"),
    DESCRIPTION("Description"),
    NOTES("Notes"),
    SCENARIO_NAME("Scenario Name"),
    SCENARIO_TYPE("Scenario Type"),
    TOLERANCE_COUNT("Tolerance Count"),
    FULLY_BURDENED_COST("Fully Burdened Cost"),
    MATERIAL_COST("Material Cost"),
    PIECE_PART_COST("Piece Part Cost"),
    TOTAL_CAPITAL_INVESTMENT("Total Capital Investment"),
    CYCLE_TIME("Cycle Time"),
    FINISH_MASS("Finish Mass"),
    PROCESS_ROUTING("Process Routing"),
    UTILIZATION("Utilization"),

    //TOGGLE GROUP
    CAD_CONNECTED("Cad Connected"),
    LOCKED("Locked"),
    PUBLISH("Publish"),

    //DATE GROUP
    CREATED_AT("Created At"),
    LAST_UPDATED_AT("Last Updated At"),

    //DROPDOWN GROUP
    PROCESS_GROUP("Process Group"),
    DIGITAL_FACTORY("Digital Factory"),
    ASSIGNEE("Assignee"),
    COMPONENT_TYPE("Component Type"),
    COST_MATURITY("Cost Maturity"),
    CREATED_BY("Created By"),
    LAST_UPDATED_BY("Last Updated By"),
    STATE("State"),
    STATUS("Status"),
    DFM("Dfm"),
    DFM_RISK("DFM Risk");

    public static EnumSet<PropertyEnum> inputGroup = EnumSet.of(ANNUAL_VOLUME, BATCH_SIZE, MATERIAL, PRODUCTION_LIFE, COMPONENT_NAME, DESCRIPTION, NOTES, SCENARIO_NAME, TOLERANCE_COUNT,
        FULLY_BURDENED_COST, MATERIAL_COST, PIECE_PART_COST, TOTAL_CAPITAL_INVESTMENT, CYCLE_TIME, FINISH_MASS, PROCESS_ROUTING, UTILIZATION);
    public static EnumSet<PropertyEnum> toggleGroup = EnumSet.of(CAD_CONNECTED, LOCKED, PUBLISH);
    public static EnumSet<PropertyEnum> dateGroup = EnumSet.of(CREATED_AT, LAST_UPDATED_AT);
    public static EnumSet<PropertyEnum> dropdownGroup = EnumSet.of(PROCESS_GROUP, DIGITAL_FACTORY, ASSIGNEE, COMPONENT_TYPE, COST_MATURITY, CREATED_BY, LAST_UPDATED_BY, STATE, STATUS, DFM);
    private final String property;

    PropertyEnum(String property) {
        this.property = property;
    }

    public String getProperty() {
        return this.property;
    }
}
