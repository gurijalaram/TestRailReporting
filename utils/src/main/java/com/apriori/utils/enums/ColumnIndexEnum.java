package com.apriori.utils.enums;

public enum ColumnIndexEnum {

    CID_PART_ONE("1"),
    CID_PART_TWO("2"),
    CID_PART_THREE("3"),
    CID_PART_FOUR("4"),
    CIR_PART_ONE("5"),
    CIR_PART_TWO("8"),
    CIR_PART_THREE("10"),
    CIR_PART_FOUR("13");

    private final String columnIndex;

    ColumnIndexEnum(String columnIndex) {
        this.columnIndex = columnIndex;
    }

    public String getColumnIndex() {
        return columnIndex;
    }
}
