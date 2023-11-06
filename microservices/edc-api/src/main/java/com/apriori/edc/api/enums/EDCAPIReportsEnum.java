package com.apriori.edc.api.enums;

import com.apriori.shared.util.interfaces.ExternalEndpointEnum;
import com.apriori.shared.util.properties.PropertiesContext;

public enum EDCAPIReportsEnum implements ExternalEndpointEnum {

    //Reports
    REPORTS("reports");

    private final String endpoint;

    EDCAPIReportsEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("edc.api_url") + String.format(getEndpointString(), variables)
            + String.format(getEndpointString(), variables) + this.addQuery(getEndpointString());
    }
}
