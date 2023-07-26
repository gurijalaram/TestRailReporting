package com.apriori.acs.entity.enums;

import com.apriori.interfaces.ExternalEndpointEnum;
import com.apriori.properties.PropertiesContext;

public enum OldTokenEnum implements ExternalEndpointEnum {
    POST_TOKEN("ws/auth/token");

    private final String endpoint;

    OldTokenEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("base_url")
            + String.format(getEndpointString(), variables) + this.addQuery(getEndpointString());
    }
}