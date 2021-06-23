package com.apriori.utils.enums.reports;

public enum SortOrderItemsEnum {
    JEEP("JEEP WJ FRONT BRAKE DISC 99-04 (Init…"),
    CYLINDER("CYLINDER HEAD (Initial)"),
    DTC_SAND("DTCCASTINGISSUES (sand casting)"),
    DU_INITIAL("1205DU1017494_K (Initial)"),
    DTC_INITIAL("DTCCASTINGISSUES (Initial)"),
    OBSTRUCTED("OBSTRUCTED MACHINING (Initial)"),
    BARCO("BARCO_R8552931 (Initial)"),
    DU_TWO("DU600051458 (Initial)"),
    GEAR_HOUSING("GEAR HOUSING (Initial)"),
    E3("E3-241-4-N (Initial)");

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
