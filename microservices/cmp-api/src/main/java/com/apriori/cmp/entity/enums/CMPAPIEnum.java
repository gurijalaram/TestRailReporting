package com.apriori.cmp.entity.enums;

import com.apriori.interfaces.ExternalEndpointEnum;
import com.apriori.properties.PropertiesContext;

public enum CMPAPIEnum implements ExternalEndpointEnum {

    COMPARISONS("comparisons"),
    COMPARISON_BY_IDENTITY("comparisons/%s");

    private final String endpoint;

    CMPAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("cmp.api_url")
            + String.format(getEndpointString(), variables) + this.addQuery(getEndpointString());
    }
}
