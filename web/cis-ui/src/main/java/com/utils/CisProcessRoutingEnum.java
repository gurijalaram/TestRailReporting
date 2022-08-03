package com.utils;

public enum CisProcessRoutingEnum {

    PROCESS_GROUP_NAME("Process Group Name"),
    PROCESS_NAME("Process Name"),
    MACHINE_NAME("Machine Name"),
    CYCLE_TIME("Cycle Time"),
    FULLY_BURDENED_COST("Fully Burdened Cost"),
    PIECE_PART_COST("Piece Part Cost"),
    TOTAL_CAPITAL_INVESTMENT("Total Capital Investment");

    private final String processName;

    CisProcessRoutingEnum(String fields) {
        this.processName = fields;
    }

    public String getProcessRoutingName() {
        return this.processName;
    }
}
