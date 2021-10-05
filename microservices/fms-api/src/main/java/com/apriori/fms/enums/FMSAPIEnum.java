package com.apriori.fms.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum FMSAPIEnum implements ExternalEndpointEnum {

    GET_FILES("files"),
    GET_FILE_BY_ID("files/%s"),
    POST_FILES("files");

    private final String endpoint;

    FMSAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("${env}.fms.api_url") + String.format(getEndpointString(), variables);
    }
}
