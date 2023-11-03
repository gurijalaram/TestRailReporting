package com.apriori.fms.api.enums;

import com.apriori.shared.util.interfaces.ExternalEndpointEnum;
import com.apriori.shared.util.properties.PropertiesContext;

public enum FMSAPIEnum implements ExternalEndpointEnum {

    FILES("files"),
    FILE_BY_ID("files/%s");

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
        return PropertiesContext.get("fms.api_url")
            + String.format(getEndpointString(), variables) + this.addQuery(getEndpointString());
    }
}
