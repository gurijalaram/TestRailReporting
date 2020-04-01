package com.apriori.utils.http.enums.common.api;

import com.apriori.utils.http.enums.common.InternalEndpointEnum;

/**
 * @author kpatel
 */
public enum CommonEndpointEnum implements InternalEndpointEnum {

    POST_SESSIONID("/login");

    private final String endpoint;

    CommonEndpointEnum(String endpoint) {
        this.endpoint = endpoint;
    }


    @Override
    public String getEndpointString() {
        return endpoint;
    }

}
