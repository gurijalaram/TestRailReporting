package com.apriori.edcapi.entity.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum EDCAPIEnum implements ExternalEndpointEnum {

    //Bill Of Materials
    GET_BILL_OF_MATERIALS("bill-of-materials"),
    POST_BILL_OF_MATERIALS("bill-of-materials"),
    GET_BILL_OF_MATERIALS_BY_IDENTITY("bill-of-materials/%s"),
    DELETE_BILL_OF_MATERIALS_BY_IDENTITY("bill-of-materials/%s"),
    POST_BILL_OF_MATERIALS_IDENTITY_TO_EXPORT("bill-of-materials/%s/export");

    private final String endpoint;

    EDCAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("${env}.edc.api_url") + String.format(getEndpointString(), variables);
    }
}
