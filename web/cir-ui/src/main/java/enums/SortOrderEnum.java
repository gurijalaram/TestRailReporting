package enums;

public enum SortOrderEnum {
    ANNUAL_SPEND("Annual Spend"),
    BENDS("Bends"),
    CASTING_ISSUES("Manufacturing - Casting Issues"),
    DESIGN_STANDARDS("Design Standards"),
    DTC_RANK("DTC Rank"),
    MACHINING_ISSUES("Manufacturing - Machining Issues"),
    MACHINING_TIME("Machining Time"),
    MANUFACTURING_ISSUES("Manufacturing Issues"),
    MATERIAL_SCRAP("Material Scrap"),
    SLOW_OPERATIONS("Slow Operations"),
    SPECIAL_TOOLING("Special Tooling"),
    TOLERANCES("Tolerances");

    private final String sortOrder;

    SortOrderEnum(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * Gets sort order option
     *
     * @return String
     */
    public String getSortOrderEnum() {
        return this.sortOrder;
    }
}