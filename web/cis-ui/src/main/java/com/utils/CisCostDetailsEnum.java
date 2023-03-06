package com.utils;

public enum CisCostDetailsEnum {

    PROCESS_GROUP_NAME("Process Group Name"),
    PROCESS_NAME("Process Name"),
    MACHINE_NAME("Machine Name"),
    CYCLE_TIME("Cycle Time"),
    FULLY_BURDENED_COST("Fully Burdened Cost"),
    PIECE_PART_COST("Piece Part Cost"),
    TOTAL_CAPITAL_INVESTMENT("Total Capital Investment"),
    ASSEMBLY_PROCESS_COST("Assembly Process Cost"),
    COMPONENT_COST_FULLY_BURDENED("Components Cost (Fully Burdened)"),
    COMPONENT_COST_PIECE_PART("Components Cost (Piece Part)"),
    TOTAL_COST("Total Cost"),
    TOTAL_MACHINE_COST("Total Machine Cost"),
    LABOR_TIME("Labor Time");

    private final String costDetail;

    CisCostDetailsEnum(String fields) {
        this.costDetail = fields;
    }

    public String getProcessRoutingName() {
        return this.costDetail;
    }
}
