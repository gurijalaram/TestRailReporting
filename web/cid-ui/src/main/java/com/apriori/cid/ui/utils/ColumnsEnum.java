package com.apriori.cid.ui.utils;

public enum ColumnsEnum {

    CAD_CONNECTED("CAD Connected"),
    ASSIGNEE("Assignee"),
    ASSIGNED_AT("Assigned At"),
    ASSIGNED_BY("Assigned By"),
    COST_MATURITY("Cost Maturity"),
    DESCRIPTION("Description"),
    LOCKED("Locked"),
    LOCKED_AT("Locked At"),
    LOCKED_BY("Locked By"),
    NOTES("Notes"),
    PUBLISHED("Published"),
    PUBLISHED_AT("Published At"),
    PUBLISHED_BY("Published By"),
    STATUS("Status"),
    SCENARIO_TYPE("Scenario Type"),
    LAST_UPDATED_AT("Last Updated At"),
    LAST_UPDATED_BY("Last Updated By"),
    MATERIAL_NAME("Last Updated At"),
    TOTAL_CAPITAL_INVESTMENT("Total Capital Investment"),
    CYCLE_TIME("Cycle Time"),
    FINISH_MASS("Finish Mass"),
    LABOR_COST("Labor Cost"),
    MATERIAL_COST("Material Cost"),
    PROCESS_ROUTING("Process Routing"),
    PIECE_PART_COST("Piece Part Cost"),
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
    FULLY_BURDENED_COST("Fully Burdened Cost"),
    EXCLUDED("Excluded"),
    QUANTITY("Quantity"),
    MATERIAL_CARBON("Material Carbon"),
    PROCESS_CARBON("Process Carbon"),
    LOGISTICS_CARBON("Logistics Carbon");

    private final String columns;

    ColumnsEnum(String columns) {
        this.columns = columns;
    }

    public String getColumns() {
        return this.columns;
    }
}
