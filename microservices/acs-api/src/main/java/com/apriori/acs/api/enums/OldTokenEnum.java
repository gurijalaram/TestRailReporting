package com.apriori.acs.api.enums;

import com.apriori.shared.util.interfaces.ExternalEndpointEnum;
import com.apriori.shared.util.properties.PropertiesContext;

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
        return PropertiesContext.get("base_api_sp_url")
            + String.format(getEndpointString(), variables) + this.addQuery(getEndpointString());
    }
}