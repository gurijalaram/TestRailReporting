package com.utils;

public enum CisCostDetailsEnum {

    PROCESS_GROUP_NAME("Process Group Name"),
    PROCESS_NAME("Process Name"),
    MACHINE_NAME("Machine Name"),
    CYCLE_TIME("Cycle Time"),
    FULLY_BURDENED_COST("Fully Burdened Cost"),
    PIECE_PART_COST("Piece Part Cost"),
    TOTAL_CAPITAL_INVESTMENT("Total Capital Investment");

    private final String costDetail;

    CisCostDetailsEnum(String fields) {
        this.costDetail = fields;
    }

    public String getProcessRoutingName() {
        return this.costDetail;
    }
}
