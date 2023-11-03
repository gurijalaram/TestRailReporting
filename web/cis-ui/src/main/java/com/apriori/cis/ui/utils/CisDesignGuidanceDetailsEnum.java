package com.apriori.cis.ui.utils;

public enum CisDesignGuidanceDetailsEnum {

    HOLE_ISSUE("Hole Issue"),
    PROXIMITY_WARNING("Proximity Warning"),
    BLANK_ISSUE("Blank Issue"),
    MATERIAL_ISSUE("Material Issue"),
    MACHINING_SETUPS("Machining Setups"),
    DISTINCT_SIZES_COUNT("Distinct Sizes Count"),
    MACHINED_GCD("Machined GCDs"),
    SIMPLE_HOLES("Simple Holes");

    private final String designGuidanceDetails;

    CisDesignGuidanceDetailsEnum(String fields) {
        this.designGuidanceDetails = fields;
    }

    public String getDesignGuidanceDetailsName() {
        return this.designGuidanceDetails;
    }
}
