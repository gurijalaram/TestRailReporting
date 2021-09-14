package com.apriori.edc.entity.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum EDCAPIEnum implements ExternalEndpointEnum {

    //Bill Of Materials
    DELETE_BILL_OF_MATERIALS_BY_IDENTITY("bill-of-materials/%s")
    ;

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
        return PropertiesContext.get("${env}.edc.api_url") + String.format(getEndpointString(), variables)
            + "?key=" + PropertiesContext.get("${env}.secret_key");
    }
}
