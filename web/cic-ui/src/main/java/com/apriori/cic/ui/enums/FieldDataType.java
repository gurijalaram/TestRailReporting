package com.apriori.cic.ui.enums;

/*
Enum for Usage Rule in GUI during connector creation
 */
public enum FieldDataType {
    DT_INTEGER("Integer"),
    DT_STRING("String"),
    DT_EMAIL("Email"),
    DT_REAL("Real"),
    DT_DATE_TIME("Date Time");

    private final String dataType;

    FieldDataType(String mRule) {
        dataType = mRule;
    }

    public String getDataType() {
        return dataType;
    }
}
