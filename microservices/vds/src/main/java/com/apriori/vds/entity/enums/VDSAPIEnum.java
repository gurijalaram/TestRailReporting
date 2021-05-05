package com.apriori.vds.entity.enums;

import com.apriori.utils.http.enums.common.EdcQaAPI;
import com.apriori.vds.utils.Constants;

public enum VDSAPIEnum implements EdcQaAPI {

    // Access Controls
    GET_GROUPS("groups"),
    GET_PERMISSIONS("permissions"),

    POST_SYNCHRONIZE("synchronize");


    private final String endpoint;

    VDSAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }


    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return Constants.getApiUrl() + String.format(getEndpointString(), variables);
    }

}
