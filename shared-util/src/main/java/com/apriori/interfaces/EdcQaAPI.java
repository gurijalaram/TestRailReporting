package com.apriori.interfaces;

import com.apriori.properties.PropertiesContext;

public interface EdcQaAPI extends ExternalEndpointEnum {

    @Override
    default String getEndpoint(Object... variables) {

        return PropertiesContext.get("edc.api_url") + String.format(getEndpointString(), variables) + "?key=" + PropertiesContext.get("secret_key");
    }

    default String getSchemaLocation() {
        return null;
    }
}

