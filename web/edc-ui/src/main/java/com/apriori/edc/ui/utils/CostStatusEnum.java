package com.apriori.edc.ui.utils;

public enum CostStatusEnum {

    NONE("none"),
    NO_MATCH("No Parts Matched"),
    MATCH_ACTION_NEEDED("Incomplete"),
    MATCH_COMPLETE("Ready for Export");

    private final String costStatus;

    CostStatusEnum(String costStatus) {
        this.costStatus = costStatus;
    }

    public String getCostStatus() {
        return this.costStatus;
    }
}
