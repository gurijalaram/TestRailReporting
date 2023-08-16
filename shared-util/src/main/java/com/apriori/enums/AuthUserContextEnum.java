package com.apriori.enums;

import com.apriori.interfaces.ExternalEndpointEnum;
import com.apriori.properties.PropertiesContext;

public enum AuthUserContextEnum implements ExternalEndpointEnum {
    //USER
    GET_AUTH_USER_CONTEXT_BY_USERID("users/%s"),
    GET_AUTH_USER_CONTEXT("users/"),
    ;

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
        return PropertiesContext.get("cds.api_url") + String.format(getEndpointString(), variables) + "?key=" + PropertiesContext.get("secret_key");
    }
}
