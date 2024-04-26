package com.apriori.shared.util.enums;

import com.apriori.shared.util.interfaces.ExternalEndpointEnum;
import com.apriori.shared.util.properties.PropertiesContext;

public enum UsersApiEnum implements ExternalEndpointEnum {

    //USERS
    USERS("/users");

    private final String endpoint;

    UsersApiEnum(String endpoint) {
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
