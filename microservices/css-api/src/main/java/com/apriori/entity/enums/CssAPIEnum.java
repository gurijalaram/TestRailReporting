package com.apriori.entity.enums;

import com.apriori.interfaces.ExternalEndpointEnum;
import com.apriori.properties.PropertiesContext;

public enum CssAPIEnum implements ExternalEndpointEnum {

    //SCENARIO
    SCENARIO_ITERATIONS("scenario-iterations"),
    SCENARIO_ITERATIONS_SEARCH("scenario-iterations/search"),
    SCENARIO_ITERATIONS_QUERY("scenario-iterations/query");

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
        return String.format(PropertiesContext.get("css.api_url").concat("%s"), String.format(getEndpointString(), variables));
    }
}

