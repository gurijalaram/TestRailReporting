package com.apriori.cusapi.entity.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum CusAppAPIEnum implements ExternalEndpointEnum {

    //PREFERENCES
    PREFERENCES("users/current/preferences"),

    //USER
    CURRENT_USER("users/current");

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
        return String.format((PropertiesContext.get("${env}.cus.api_url")).concat("%s"), String.format(getEndpointString(), variables));
    }
}

