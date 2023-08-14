package com.apriori.utils.http.enums.common.api;



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
