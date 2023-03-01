package com.apriori.ats.utils.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum ATSAPIEnum implements ExternalEndpointEnum {
    POST_TOKEN("tokens"),
    PATCH_USER_PASSWORD_BY_USERNAME("users/%s/password");

    private final String endpoint;

    ATSAPIEnum(String endpoint) {
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
