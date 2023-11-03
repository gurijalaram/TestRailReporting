package com.apriori.cir.ui.enums;

public enum SortOrderItemsEnum {
    JEEP_INITIAL("JEEP WJ FRONT BRAKE DISC 99-04 (Ini"),
    JEEP("JEEP WJ FRONT BRAKE DISC 99-04"),
    CYLINDER("CYLINDER HEAD (Initial)"),
    DTC_INITIAL("DTCCASTINGISSUES (Initial)"),
    DTC_SAND("DTCCASTINGISSUES (sand casting)"),
    DTC("DTCCASTINGISSUES"),
    DU_INITIAL("1205DU1017494_K (Initial)"),
    DU_TWO_INITIAL("DU600051458 (Initial)"),
    DU_TWO("DU600051458"),
    DU_THREE("DU200068073_B"),
    OBSTRUCTED_INITIAL("OBSTRUCTED MACHINING (Initial)"),
    OBSTRUCTED("OBSTRUCTED MACHINING"),
    BARCO("BARCO_R8552931"),
    BARCO_INITIAL("BARCO_R8552931 (Initial)"),
    BARCO_THREE("BARCO_R8761310"),
    GEAR_HOUSING_INITIAL("GEAR HOUSING (Initial)"),
    GEAR_HOUSING("GEAR HOUSING"),
    E3_INITIAL("E3-241-4-N (Initial)"),
    E3("E3-241-4-N"),
    MLDES("40137441.MLDES.0002"),
    B2("B2315"),
    MACHINING_DTC_INITIAL("MACHININGDESIGN_TO_COST (Initial)"),
    PARTBODY_INITIAL("PARTBODY_1 (Initial)"),
    DTC_MACHINING_TOLERANCED("DTCMACHINING_001 (Toleranced)"),
    PUNCH_INITIAL("PUNCH (Initial)"),
    PMI_ROUGH_INITIAL("PMI_ROUGHNESSCREO (Initial)"),
    PMI_PROFILE_INITIAL("PMI_PROFILEOFSURFACECREO (Initial)");

    private final String sortOrderItemName;

    SortOrderItemsEnum(String sortOrderItemName) {
        this.sortOrderItemName = sortOrderItemName;
    }

    /**
     * Gets sort order item name
     *
     * @return String
     */
    public String getSortOrderItemName() {
        return this.sortOrderItemName;
    }
}