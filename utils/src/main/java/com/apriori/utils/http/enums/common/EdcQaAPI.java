package com.apriori.utils.http.enums.common;

public interface EdcQaAPI extends ExternalEndpointEnum {

    @Override
    default String getEndpoint(Object... variables) {
        return System.getProperty("baseUrl") + String.format(getEndpointString(), variables);
    }

    default String getSchemaLocation() {
        return null;
    }
}

