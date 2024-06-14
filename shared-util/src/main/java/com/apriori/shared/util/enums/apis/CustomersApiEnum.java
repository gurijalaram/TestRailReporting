package com.apriori.shared.util.enums.apis;

import com.apriori.shared.util.interfaces.ExternalEndpointEnum;
import com.apriori.shared.util.properties.PropertiesContext;

public enum CustomersApiEnum implements ExternalEndpointEnum {

    // SITES
    SITES_BY_CUSTOMER_ID("/customers/%s/sites"),

    // APPLICATION
    APPLICATIONS("/applications"),

    //CUSTOMERS
    CUSTOMERS("/customers");

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
