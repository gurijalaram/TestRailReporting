package com.apriori.cis.ui.utils;

public enum CisInsightsFieldsEnum {

    DETAILS("Details"),
    UTILIZATION_INFO("Utilization Info"),
    SELECTED_SHEET("Selected Sheet"),
    BLANK_SIZE("Blank Size"),
    PARTS_PER_SHEET("Parts Per Sheet"),
    CONFIGURATION("Configuration"),
    UTILIZATION_MODE("Utilization Mode"),
    MATERIAL_PROPERTIES("Material Properties"),
    CUT_COST("Cut Cost"),
    USA_NAME("USA Name"),
    DIN_NAME("DIN Name"),
    EN_NAME("EN Name"),
    UNIT_COST("Unit Cost"),
    DENSITY("Density"),
    YOUNG_MODULE("Young Module"),
    STRAIN_HARDENING_COEFFICIENT("K (strain-hardening coefficient)"),
    STRAIN_HARDENING_EXPONENT("N (strain-hardening exponent)"),
    LANKFORD_PARAMETER("R (Lankford parameter, avg.)"),
    MILLING_SPEED("Milling Speed"),
    HARDNESS("Hardness"),
    HARDNESS_SYSTEM("Hardness System"),
    POSSION_RATIO("Possion Ratio"),
    SHER_STRENGTH("Shear Strength"),
    BASIC_INFORMATION("Basic Information"),
    SELECTED_STOCK("Selected Stock"),
    SELECTED_METHOD("Selection Method"),
    STOCK_FORM("Stock Form"),
    VIRTUAL_STOCK("Virtual Stock"),
    DIMENSIONS("Dimensions"),
    PART("Part"),
    STOCK("Stock");

    private final String insightsFields;

    CisInsightsFieldsEnum(String insightsFields) {
        this.insightsFields = insightsFields;
    }

    public String getInsightsFields() {
        return this.insightsFields;
    }
}