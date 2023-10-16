package com.apriori.enums;

/**
 * enum Job Details List table header field names
 */
public enum JobDetailsListHeaders {
    APRIORI_PART_NUMBER("aPriori Part Number"),
    PART_STATUS("Part Status"),
    STATUS_DETAILS("Status Details"),
    COSTING_RESULT("Costing Result"),
    DFM_RISK_RATING("DFM Risk Rating"),
    FULLY_BURDENED_COST("Fully Burdened Cost"),
    MATERIAL("Material"),
    PROCESS_GROUP("Process Group"),
    DIGITAL_FACTORY("Digital Factory"),
    PLM_PART_ID("PLM Part ID"),
    PLM_PART_NAME("PLM Part Name"),
    PLM_PART_NUMBER("PLM Part Number"),
    PLM_REVISION_NUMBER("PLM Revision Number"),
    CHECK_IN_USER_EMAIL("Check In User Email"),
    DESCRIPTION("Description"),
    SCENARIO_NAME("Scenario Name"),
    DFM_RISK_SCORE("DFM Risk Score"),
    PIECE_PART_COST("Piece Part Cost"),
    MATERIAL_COST("Material Cost"),
    CYCLE_TIME("Cycle Time"),
    LABOR_TIME("Labor Time"),
    MACHINING_MODE("Machining Mode"),
    TARGET_MASS("Target Mass"),
    TARGET_COST("Target Cost"),
    ANNUAL_MANUFACTURING_CARBON("Annual Manufacturing Carbon"),
    TOTAL_CORBON("Total Carbon"),
    LOGISTICS_CARBON("Logistics Carbon"),
    PROCESS_CARBON("Process Carbon"),
    MATERIAL_CARBON("Material Carbon"),
    TYPE("Type"),
    PROCESS_ROUTING("Process Routing"),
    UTILIZATION("Utilization"),
    ROUGH_MASS("Rough Mass"),
    FINISH_MASS("Finish Mass");

    private final String columnName;

    JobDetailsListHeaders(String colName) {
        columnName = colName;
    }

    public String getColumnName() {
        return columnName;
    }
}
