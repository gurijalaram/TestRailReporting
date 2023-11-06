package com.apriori.cir.ui.enums;

public enum CostMetricEnum {
    PIECE_PART_COST("Piece Part Cost"),
    FULLY_BURDENED_COST("Fully Burdened Cost");

    private final String costMetricName;

    CostMetricEnum(String costMetricName) {
        this.costMetricName = costMetricName;
    }

    /**
     * Gets cost metric name
     *
     * @return String
     */
    public String getCostMetricName() {
        return this.costMetricName;
    }
}