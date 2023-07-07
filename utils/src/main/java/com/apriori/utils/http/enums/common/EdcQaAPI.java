package com.apriori.utils.http.enums.common;

import com.apriori.utils.properties.PropertiesContext;

public interface EdcQaAPI extends ExternalEndpointEnum {

    @Override
    default String getEndpoint(Object... variables) {

        return PropertiesContext.get("edc.api_url")
            + String.format(getEndpointString(), variables) + this.addQuery(getEndpointString());
    }

    default String getSchemaLocation() {
        return null;
    }
}

