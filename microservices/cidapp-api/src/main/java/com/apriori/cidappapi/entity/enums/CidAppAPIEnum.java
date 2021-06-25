package com.apriori.cidappapi.entity.enums;

import com.apriori.cidappapi.utils.Constants;
import com.apriori.utils.http.enums.common.ExternalEndpointEnum;

public enum CidAppAPIEnum implements ExternalEndpointEnum {

    // Component controller
    GET_COMPONENTS("components"),
    GET_COMPONENT_BY_COMPONENT_ID("components/%s"),
    GET_HOOPS_IMAGE_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s/hoops-image"),
    GET_SCENARIO_REPRESENTATION_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s"),
    GET_COMPONENT_ITERATION_LATEST_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s/iterations/latest"),

    POST_COMPONENTS("components"),
    POST_COMPONENT_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s/cost");

    private final String endpoint;

    CidAppAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }


    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return String.format(Constants.getApiUrl(), String.format(getEndpointString(), variables));
    }
}

