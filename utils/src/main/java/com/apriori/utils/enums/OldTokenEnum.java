package com.apriori.utils.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

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
        return PropertiesContext.get("${env}.base_url") + String.format(getEndpointString(), variables)
            + "?key=" + PropertiesContext.get("${env}.secret_key");
    }
}
