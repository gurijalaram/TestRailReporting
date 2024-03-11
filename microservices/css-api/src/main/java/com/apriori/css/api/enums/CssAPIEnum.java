package com.apriori.css.api.enums;

import com.apriori.shared.util.interfaces.ExternalEndpointEnum;
import com.apriori.shared.util.properties.PropertiesContext;

public enum CssAPIEnum implements ExternalEndpointEnum {

    //SCENARIO
    SCENARIO_ITERATIONS("/scenario-iterations"),
    SCENARIO_ITERATIONS_SEARCH("/scenario-iterations/search"),
    SCENARIO_ITERATIONS_QUERY("/scenario-iterations/query");

    private final String endpoint;

    CssAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("css.api_url")
            + String.format(getEndpointString(), variables) + this.addQuery(getEndpointString());
    }
}

