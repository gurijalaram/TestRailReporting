package com.apriori.cas.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum CASAPIEnum implements ExternalEndpointEnum {

    //CUSTOMERS
    GET_CUSTOMERS("customers/"),

    //SITES
    VALIDATE_SITES("/sites/validate"),
    POST_SITES("/sites"),

    //USERS
    POST_USERS("/users/"),

    //BATCHES
    POST_BATCHES("/batches/"),
    POST_BATCH_ITEMS("/items/");

    private final String endpoint;

    CASAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("${env}.cas.api_url") + String.format(getEndpointString(), variables) + "?key=" + PropertiesContext.get("${env}.secret_key");
    }
}