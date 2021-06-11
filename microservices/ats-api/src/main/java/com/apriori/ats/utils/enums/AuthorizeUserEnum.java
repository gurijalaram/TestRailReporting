package com.apriori.ats.utils.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;

public enum AuthorizeUserEnum implements ExternalEndpointEnum {
    POST_MULTIPART_AUTHORIZE_BY_BASE_URL_SECRET("%s/authorize?key=%s");

    private final String endpoint;

    AuthorizeUserEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return String.format(getEndpointString(), variables);
    }
}
