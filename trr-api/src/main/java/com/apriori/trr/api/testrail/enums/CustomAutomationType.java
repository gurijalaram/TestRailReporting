package com.apriori.trr.api.testrail.enums;

/*
 Enum for Column index for automation type
 */
public enum CustomAutomationType {
    MANUAL("MANUAL", 0),
    AUTOMATABLE("AUTOMATABLE", 2),
    AUTOMATED("AUTOMATED", 1);

    private final String customAutomationType;
    private final Integer typeIndex;

    CustomAutomationType(String automationType, Integer automationTypeIndex) {
        customAutomationType = automationType;
        typeIndex = automationTypeIndex;
    }

    public String getCustomAutomationType() {
        return customAutomationType;
    }

    public Integer getTypeIndex() {
        return typeIndex;
    }
}
