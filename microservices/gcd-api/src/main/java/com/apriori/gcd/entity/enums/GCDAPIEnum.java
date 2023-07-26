package com.apriori.gcd.entity.enums;

import com.apriori.interfaces.ExternalEndpointEnum;
import com.apriori.properties.PropertiesContext;

public enum GCDAPIEnum implements ExternalEndpointEnum {

    TREE_DIFF("gcd-tree/diff");

    private final String endpoint;

    GCDAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("gcd.api_url")
            + String.format(getEndpointString(), variables) + this.addQuery(getEndpointString());
    }
}
