package com.apriori.enums;

public enum OperationEnum {
    EQUALS("Equals"),
    NOT_EQUALS("Not Equal"),
    GREATER_THAN("Greater Than"),
    GREATER_THAN_OR_EQUAL_TO("Greater Than or Equal To"),
    LESS_THAN("Less Than"),
    LESS_THAN_OR_EQUAL_TO("Less Than or Equal To"),
    IN("In"),
    IS_NOT_DEFINED("Is Not Defined"),
    NOT_IN("Not In"),
    CONTAINS("Contains"),
    ENDS_WITH("Ends With"),
    STARTS_WITH("Starts With");

    private String operation;

    OperationEnum(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }
}
