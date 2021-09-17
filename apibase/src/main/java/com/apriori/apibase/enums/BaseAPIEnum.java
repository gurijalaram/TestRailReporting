package com.apriori.apibase.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum BaseAPIEnum implements ExternalEndpointEnum {

    GET_TOLERANCE_VALUE("ws/workspace/users/me/tolerance-policy-defaults");

    private final String endpoint;

    BaseAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }


    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("${env}.base_url") + String.format(getEndpointString(), variables) + "?key=" + PropertiesContext.get("${env}.secret_key");
    }

}
