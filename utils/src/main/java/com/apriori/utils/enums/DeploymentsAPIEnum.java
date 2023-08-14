package com.apriori.utils.enums;




public enum DeploymentsAPIEnum implements ExternalEndpointEnum {

    DEPLOYMENTS("customers/%s/deployments");

    private final String endpoint;

    DeploymentsAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("cds.api_url")
            + String.format(getEndpointString(), variables) + this.addQuery(getEndpointString());
    }
}
