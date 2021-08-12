package com.apriori.css.entity.enums;

import com.apriori.utils.Constants;
import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum CssAPIEnum implements ExternalEndpointEnum {
    GET_COMPONENT_BY_COMPONENT_SCENARIO_NAMES("scenario-iterations?componentName[EQ]=%s&scenarioName[EQ]=%s");

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
        return String.format((PropertiesContext.getStr("${env}.css.css_url")).concat("/%s"), String.format(getEndpointString(), variables));
    }
}

