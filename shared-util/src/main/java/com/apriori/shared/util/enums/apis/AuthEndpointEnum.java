package com.apriori.shared.util.enums.apis;

import com.apriori.shared.util.interfaces.ExternalEndpointEnum;

/**
 * @author kpatel
 */
public enum AuthEndpointEnum implements ExternalEndpointEnum {

    POST_AUTH("/auth/token");

    private final String endpoint;

    AuthEndpointEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }
}
