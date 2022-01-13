package com.apriori.utils.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum AuthUserContextEnum implements ExternalEndpointEnum {
    //USER
    GET_AUTH_USER_CONTEXT("users/%s");

    private final String endpoint;

    AuthUserContextEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("${env}.cds.api_url") + String.format(getEndpointString(), variables) + "?key=" + PropertiesContext.get("${env}.secret_key");
    }
}
