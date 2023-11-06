package com.apriori.cmp.api.models.enums;

import com.apriori.shared.util.interfaces.ExternalEndpointEnum;
import com.apriori.shared.util.properties.PropertiesContext;

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
