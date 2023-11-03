package com.apriori.cnh.entity.enums;




public enum CNHAPIEnum implements ExternalEndpointEnum {

    EXECUTE("dev/customers/%s/reports/%s/execute"),
    STATUS("dev/customers/%s/reports/%s/executions/%s/status");


    private final String endpoint;

    CNHAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }


    @Override
    public String getEndpointString() {
        return endpoint;
    }


    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("${env}.cnh.api_url") + String.format(getEndpointString(), variables);
    }

}
