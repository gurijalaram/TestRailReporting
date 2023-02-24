package com.apriori.ats.utils.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum AuthorizeUserEnum implements ExternalEndpointEnum {
    //AUTHORIZE_USER
    POST_AUTHORIZE_BY_BASE_URL_SECRET("authorize");

    private final String endpoint;

    AuthorizeUserEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("ats.api_url") + String.format(getEndpointString(), variables) + "?key=" + PropertiesContext.get("secret_key");
    }
}