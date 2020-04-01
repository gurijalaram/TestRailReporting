package com.apriori.utils.http.enums.common;

import com.apriori.utils.constants.Constants;

public interface EdcQaAPI extends ExternalEndpointEnum {

    @Override
    default String getEndpoint(Object... variables) {
        return Constants.getBaseUrl() + String.format(getEndpointString(), variables);
    }

    default String getSchemaLocation() {
        return null;
    }
}

