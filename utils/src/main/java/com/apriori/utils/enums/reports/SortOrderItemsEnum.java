package com.apriori.utils.enums.reports;

public enum SortOrderItemsEnum {
    JEEP_INITIAL("JEEP WJ FRONT BRAKE DISC 99-04 (Init…"),
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
    E3("E3-241-4-N (Initial)"),
    MLDES("40137441.MLDES.0002"),
    B2("B2315");

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
