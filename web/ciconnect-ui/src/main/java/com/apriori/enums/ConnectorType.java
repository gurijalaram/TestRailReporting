package com.apriori.enums;

/*
 Enum for types of connector in GUI
 */
public enum ConnectorType {
    WINDCHILL("Windchill"),
    TEAM_CENTER("Teamcenter"),
    FILE_SYSTEM("File System"),
    ENOVIA("Enovia");

    private final String connectorType;

    ConnectorType(String type) {
        connectorType = type;
    }

    public String getConnectorType() {
        return connectorType;
    }
}
