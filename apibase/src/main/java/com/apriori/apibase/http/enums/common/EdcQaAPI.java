package com.apriori.apibase.http.enums.common;

import com.apriori.utils.constants.Constants;

public interface EdcQaAPI extends ExternalEndpointEnum {

    @Override
    default String getEndpoint(Object... variables) {
        return Constants.url + String.format(getEndpointString(), ((Object[]) variables));
    }

    default String getSchemaLocation() {
        return null;
    }
}

