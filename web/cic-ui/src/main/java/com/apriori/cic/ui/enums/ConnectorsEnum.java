package com.apriori.cic.ui.enums;

public enum ConnectorsEnum {
    WINDCHILL_CONNECTOR("aP Internal - WC - windows - 3"),
    TEAMCENTER_CONNECTOR("aP Internal - TC - windows - 2");
    private String connectorName;

    ConnectorsEnum(String name) {
        this.connectorName = name;
    }

    /**Get connector name
     *
     * @return String
     */
    public String getConnectorName() {
        return this.connectorName;
    }
}
