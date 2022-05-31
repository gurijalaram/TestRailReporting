package com.apriori.cds.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum CASCustomerEnum implements ExternalEndpointEnum {

    //CUSTOMERS
    CUSTOMERS("customers");

    private final String endpoint;

    CASCustomerEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("${env}.cas.api_url") + String.format(getEndpointString(), variables);
    }
}