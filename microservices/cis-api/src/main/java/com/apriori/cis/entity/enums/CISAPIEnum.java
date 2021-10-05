package com.apriori.cis.entity.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public class CISAPIEnum implements ExternalEndpointEnum {

    private final String endpoint;

    CISAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("${env}.cis.api_url") + String.format(getEndpointString(), variables)
            + "?key=" + PropertiesContext.get("${env}.secret_key");
    }
}