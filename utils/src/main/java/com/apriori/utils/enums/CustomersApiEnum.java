package com.apriori.utils.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum CustomersApiEnum implements ExternalEndpointEnum {

    // SITES
    SITES("sites"),
    SITES_BY_CUSTOMER_ID("customers/%s/sites"),


    //CUSTOMERS
    CUSTOMERS("customers"),
    CUSTOMER_BY_ID("customers/%s");

    private final String endpoint;

    CustomersApiEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("cds.api_url")
            + String.format(getEndpointString(), variables) + this.addQuery(getEndpointString());
    }
}
