package com.apriori.gcd.api.models.enums;

import com.apriori.shared.util.interfaces.ExternalEndpointEnum;
import com.apriori.shared.util.properties.PropertiesContext;

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
