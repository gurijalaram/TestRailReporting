package com.apriori.cisapi.entity.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum CisAPIEnum implements ExternalEndpointEnum {

    //Application Metadata
    APPLICATION_METADATA("application-metadata"),

    //People
    PEOPLE("people"),

    //User Preferences
    USERS_CURRENT_PREFERENCES("users/current/preferences?pageSize=100"),
    //USERS_CURRENT_PREFERENCES("users/current/preferences"),

    //Users
    USERS_CURRENT("users/current");


    private final String endpoint;

    CisAPIEnum(String endpoint) {
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