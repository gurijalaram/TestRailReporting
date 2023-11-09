package com.apriori.bcm.api.enums;

import com.apriori.shared.util.interfaces.ExternalEndpointEnum;
import com.apriori.shared.util.properties.PropertiesContext;

public enum CusAppAPIEnum implements ExternalEndpointEnum {

    //PREFERENCES
    PREFERENCES("users/current/preferences"),

    //USER
    CURRENT_USER("user/current");

    private final String endpoint;

    CusAppAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("cus.api_url")
            + String.format(getEndpointString(), variables) + this.addQuery(getEndpointString());
    }
}

