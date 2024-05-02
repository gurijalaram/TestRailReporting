package com.apriori.cic.ui.enums;

/*
Enum for Usage Rule in GUI during connector creation
 */
public enum AprioriCostType {
    FULLY_BURDENED_COST("Fully Burdened Cost"),
    PIECE_PART_COST("Piece Part Cost"),
    MATERIAL_COST("Material Cost");

    private final String aprioriCostType;

    AprioriCostType(String mRule) {
        aprioriCostType = mRule;
    }

    public String getAprioriCostType() {
        return aprioriCostType;
    }
}
