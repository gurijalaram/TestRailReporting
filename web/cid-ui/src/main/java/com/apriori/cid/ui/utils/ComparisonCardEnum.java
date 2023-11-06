package com.apriori.cid.ui.utils;

public enum ComparisonCardEnum {

    INFO_DESCRIPTION(1, Constants.INFO_INPUTS),
    INFO_MODIFIED_BY(2, Constants.INFO_INPUTS),
    INFO_LAST_MODIFIED(3, Constants.INFO_INPUTS),
    INFO_LOCKED(4, Constants.INFO_INPUTS),
    INFO_PROCESS_GROUP(5, Constants.INFO_INPUTS),
    INFO_ANNUAL_VOLUME(6, Constants.INFO_INPUTS),
    INFO_VPE(7, Constants.INFO_INPUTS),
    INFO_BATCH_SIZE(8, Constants.INFO_INPUTS),
    MATERIAL_MATERIAL(1, Constants.MATERIAL_AND_UTILIZATION),
    MATERIAL_FINISH_MASS(2, Constants.MATERIAL_AND_UTILIZATION),
    MATERIAL_UTILIZATION(3, Constants.MATERIAL_AND_UTILIZATION),
    DESIGN_DFM_RISK(1, Constants.DESIGN_GUIDANCE),
    DESIGN_DESIGN_FAILURES(2, Constants.DESIGN_GUIDANCE),
    DESIGN_DESIGN_WARNINGS(3, Constants.DESIGN_GUIDANCE),
    DESIGN_TOLERANCES(4, Constants.DESIGN_GUIDANCE),
    PROCESS_ROUTING(1, Constants.PROCESS),
    PROCESS_TOTAL_CYCLE_TIME(2, Constants.PROCESS),
    COST_MATERIAL(1, Constants.COST_RESULT),
    COST_LABOR(2, Constants.COST_RESULT),
    COST_DIRECT_OVERHEAD(3, Constants.COST_RESULT),
    COST_INDIRECT_OVERHEAD(4, Constants.COST_RESULT),
    COST_AMORTIZED_BATCH_SETUP(5, Constants.COST_RESULT),
    COST_INVESTMENT(6, Constants.COST_RESULT),
    COST_OTHER(7, Constants.COST_RESULT),
    COST_FULLY_BURDENED_COST(8, Constants.COST_RESULT),
    COST_PIECE_PART_COST(9, Constants.COST_RESULT),
    COST_TARGET_COST(10, Constants.COST_RESULT),
    COST_TOTAL_CAPITAL_INVESTMENT(11, Constants.COST_RESULT);

    private final int position;
    private final String cardHeader;

    ComparisonCardEnum(int position, String cardHeader) {
        this.position = position;
        this.cardHeader = cardHeader;

    }

    public int getCardPosition() {
        return this.position - 1;
    }

    public String getCardHeader() {
        return this.cardHeader;
    }

    private static class Constants {
        private static final String INFO_INPUTS = "Info & Inputs";
        private static final String MATERIAL_AND_UTILIZATION = "Material & Utilization";
        private static final String DESIGN_GUIDANCE = "Design Guidance";
        private static final String PROCESS = "Process";
        private static final String COST_RESULT = "Cost Result";
    }
}
