package com.apriori.enums;

/**
 * enum Connector List table header field names
 */
public enum ConnectorListHeaders {
    NAME("Name", 4),
    DESCRIPTION("Description", 5),
    TYPE("Type", 6),
    CONNECTION_STATUS("Connection Status", 8);

    private final String columnName;
    private final Integer columnIndex;

    ConnectorListHeaders(String colName, Integer colIndex) {
        columnName = colName;
        columnIndex = colIndex;
    }

    public String getColumnName() {
        return columnName;
    }

    public Integer getColumnIndex() {
        return columnIndex;
    }
}
