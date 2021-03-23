package com.apriori.utils.enums;

public enum  CostingIconEnum {

    COSTED("check"),
    CREATED("hdd"),
    FAILED("times-circle"),
    FETCHING("redo"),
    READY_TO_COST("exclamation-circle"),
    SUBMITTED("cog"),
    UNCOSTED_CHANGES("UNCOSTED_CHANGES");

    private final String icon;

    CostingIconEnum(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return this.icon;
    }
}
