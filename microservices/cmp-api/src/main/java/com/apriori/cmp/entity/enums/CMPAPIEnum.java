package com.apriori.cmp.entity.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum CMPAPIEnum implements ExternalEndpointEnum {

    COMPARISON("comparisons");

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
