package com.apriori.enums;

/*
 Enum for Column index for connector in GUI
 */
public enum ConnectorColumnFields {
    CI_CONNECT_FIELD("CI Connect Field", 0),
    USAGE("Usage", 2),
    PLM_FIELD("PLM Field", 3),
    DATA_TYPE("Data Type", 4);

    private final String connectorField;
    private final Integer fieldColIndex;

    ConnectorColumnFields(String field, Integer fieldIndex) {
        connectorField = field;
        fieldColIndex = fieldIndex;
    }

    public String getConnectorField() {
        return connectorField;
    }

    public Integer getColumnIndex() {
        return fieldColIndex;
    }
}
