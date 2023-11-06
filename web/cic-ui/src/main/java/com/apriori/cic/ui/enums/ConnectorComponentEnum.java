package com.apriori.cic.ui.enums;

public enum ConnectorComponentEnum {
    QUERY_DEFINITION("Query Definition"),
    REST_API_REQUEST("REST API Request");
    private String connectorComponentName;

    ConnectorComponentEnum(String connectorComponentName) {
        this.connectorComponentName = connectorComponentName;
    }

    /**Get connector component name
     *
     * @return String
     */
    public String getConnectorComponentName() {
        return this.connectorComponentName;
    }
}
