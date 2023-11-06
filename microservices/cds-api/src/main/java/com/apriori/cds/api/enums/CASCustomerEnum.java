package com.apriori.cds.api.enums;

import com.apriori.shared.util.interfaces.ExternalEndpointEnum;
import com.apriori.shared.util.properties.PropertiesContext;

public enum CASCustomerEnum implements ExternalEndpointEnum {

    //CUSTOMERS
    CUSTOMERS("customers"),
    CUSTOMERS_BATCH("customers/%s/batches/");

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
        return PropertiesContext.get("cas.api_url") + String.format(getEndpointString(), variables);
    }
}