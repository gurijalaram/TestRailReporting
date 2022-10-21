package com.apriori.css.entity.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum CssAPIEnum implements ExternalEndpointEnum {

    //SCENARIO
    SCENARIO_ITERATIONS("scenario-iterations"),
    SCENARIO_ITERATIONS_SEARCH("scenario-iterations/search"),
    COMPONENT_SCENARIO_NAME("scenario-iterations?componentName[EQ]=%s&scenarioName[EQ]=%s"),
    COMPONENT_ID("scenario-iterations?identity[EQ]=%s");

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
        return String.format(PropertiesContext.get("${env}.css.api_url").concat("%s"), String.format(getEndpointString(), variables));
    }
}

