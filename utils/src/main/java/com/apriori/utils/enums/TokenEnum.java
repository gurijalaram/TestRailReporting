package com.apriori.utils.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum TokenEnum implements ExternalEndpointEnum {

    POST_TOKEN("tokens"),
    PATCH_USER_PASSWORD_BY_USERNAME("users/%s/password");

    private final String endpoint;

    TokenEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("ats.api_url")
            + String.format(getEndpointString(), variables) + this.addQuery(getEndpointString());
    }
}
