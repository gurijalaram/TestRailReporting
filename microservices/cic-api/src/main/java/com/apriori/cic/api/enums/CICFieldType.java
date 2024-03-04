package com.apriori.cic.api.enums;

public enum CICFieldType {
    TEXT_BOX("textbox"),
    DROP_DOWN("dropdown"),
    DATE_TIME_PICKER("datetimepicker"),
    MULTIPLE("multiple");

    private final String fieldType;

    CICFieldType(String type) {
        fieldType = type;
    }

    public String getFieldType() {
        return fieldType;
    }
}
