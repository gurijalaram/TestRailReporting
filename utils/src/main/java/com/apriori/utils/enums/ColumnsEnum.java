package com.apriori.utils.enums;

public enum ColumnsEnum {

    TYPE("Type"),
    FULLY_BURDENED_COST("Fully Burdened Cost (USD)"),
    LAST_SAVED_BY("Last Saved By"),
    DESCRIPTION("Description"),
    MATERIAL_COMPOSITION("Material Composition"),
    ASSIGNEE("Assignee"),
    COST_MATURITY("Cost Maturity"),
    ANNUAL_VOLUME("Annual Volume"),
    PIECE_PART_COST("Piece Part Cost (USD)"),
    TOTAL_CAPITAL_INVESTMENTS("Total Capital Investments (USD)"),
    FINISH_MASS("Finish Mass (kg)"),
    ROUGH_MASS("Rough Mass (kg)"),
    UTILIZATION("Utilization (%)"),
    MATERIAL_COST("Material Cost (USD)"),
    CYCLE_TIME("Cycle Time (s)"),
    TEST_DATE("Test Date"),
    TEST_STRING("Test String"),
    TEST_USER("Test User"),
    TEST_NUMBER("Test Number"),
    THUMBNAIL("Thumbnail"),
    NAME_SCENARIO("Name / Scenario"),
    STATUS("Status"),
    PROCESS_GROUP("Process Group"),
    VPE("VPE"),
    LOCKED_WORKSPACE("Locked / Workspace"),
    LAST_SAVED("Last Saved");

    private final String columns;

    ColumnsEnum(String columns) {
        this.columns = columns;
    }

    public String getColumns() {
        return this.columns;
    }
}
