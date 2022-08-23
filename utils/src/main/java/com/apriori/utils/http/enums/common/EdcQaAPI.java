package com.apriori.utils.http.enums.common;

import com.apriori.utils.properties.PropertiesContext;

public interface EdcQaAPI extends ExternalEndpointEnum {

    @Override
    default String getEndpoint(Object... variables) {

        return PropertiesContext.get("${env}.edc.api_url") + String.format(getEndpointString(), variables) + "?key=" + PropertiesContext.get("${env}.secret_key");
    }

    default String getSchemaLocation() {
        return null;
    }
}

