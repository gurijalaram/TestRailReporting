package com.apriori.utils.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum DeploymentsAPIEnum implements ExternalEndpointEnum {

    DEPLOYMENTS("customers/%s/deployments?key=%s");

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
        return PropertiesContext.get("${env}.cds.api_url") + String.format(getEndpointString(), variables);
    }
}