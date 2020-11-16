package com.apriori.utils.enums.reports;

public enum SortOrderEnum {
    MANUFACTURING_ISSUES("Manufacturing Issues"),
    DESIGN_STANDARDS("Design Standards"),
    TOLERANCES("Tolerances"),
    SLOW_OPERATIONS("Slow Operations"),
    ANNUAL_SPEND("Annual Spend"),
    DTC_RANK("DTC Rank"),
    CASTING_ISSUES("Manufacturing - Casting Issues"),
    MACHINING_ISSUES("Manufacturing - Machining Issues"),
    MATERIAL_SCRAP("Material Scrap"),
    SPECIAL_TOOLING("Special Tooling");

    private final String sortOrder;

    SortOrderEnum(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * Gets sort order option
     * @return String
     */
    public String getSortOrderEnum() {
        return this.sortOrder;
    }
}