package com.apriori.cis.ui.utils;

public enum CisFilterOperationEnum {

    EQUALS("="),
    NOT_EQUALS("≠"),
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQUAL_TO("≥"),
    LESS_THAN("<"),
    LESS_THAN_OR_EQUAL_TO("≤"),
    CONTAINS("contains"),
    IS_BEFORE("is before"),
    IS_AFTER("is after"),
    IS_ANY_OF("is any of"),
    IS_NONE_OF("is none of"),
    IS_EMPTY("is empty");

    private String operation;

    CisFilterOperationEnum(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }
}
