package com.apriori.cisapi.entity.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum CISAPIEnum implements ExternalEndpointEnum {

    //Application Metadata
    GET_APPLICATION_METADATA("application-metadata"),

    //People
    GET_PEOPLE("people"),

    //User Preferences
    GET_USERS_CURRENT_PREFERENCES("users/current/preferences?pageSize=100"),
    PATCH_USERS_CURRENT_PREFERENCES("users/current/preferences"),

    //Users
    GET_USERS_CURRENT("users/current");


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
        return PropertiesContext.get("${env}.cis.api_url") + String.format(getEndpointString(), variables);
    }
}