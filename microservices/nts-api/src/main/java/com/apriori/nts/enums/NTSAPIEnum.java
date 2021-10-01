package com.apriori.nts.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum NTSAPIEnum implements ExternalEndpointEnum {

    GET_EMAIL("emails"),
    GET_EMAIL_BY_ID("emails/%s"),
    GET_NOTIFICATIONS("notifications");

    private final String endpoint;

    NTSAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }


    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("${env}.nts.api_url") + String.format(getEndpointString(), variables) + "?key=" + PropertiesContext.get("${env}.secret_key");
    }
}
