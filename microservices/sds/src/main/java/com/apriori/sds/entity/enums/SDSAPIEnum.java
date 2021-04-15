package com.apriori.sds.entity.enums;

import com.apriori.sds.utils.Constants;
import com.apriori.utils.http.enums.common.EdcQaAPI;

public enum SDSAPIEnum implements EdcQaAPI {

    // ITERATIONS
    GET_ITERATIONS_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s/iterations"),
    GET_ITERATION_LATEST_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s/iterations/latest"),
    GET_ITERATION_SINGLE_BY_COMPONENT_SCENARIO_IDENTITY_IDS("components/%s/scenarios/%s/iterations/%s"),

    // SCENARIO
    GET_SCENARIOS_BY_COMPONENT_IDS("components/%s/scenarios"),
    GET_SCENARIO_SINGLE_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s"),
    GET_SCENARIO_COSTING_DEFAULTS_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s/costing-defaults"),
    GET_SCENARIO_HOOPS_IMAGE_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s/hoops-image"),
    GET_SCENARIO_MANIFEST_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s/manifest"),
    GET_SCENARIO_SECONDARY_PROCESSES_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s/secondary-processes"),

    DELETE_SCENARIO_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s"),


    // SCENARIO ASSOCIATIONS
    GET_ASSOCIATIONS_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s/associations/occurrences=1"),
    GET_ASSOCIATIONS_SINGLE_BY_COMPONENT_SCENARIO_IDENTITY_IDS("components/%s/scenarios/%s/associations/%s"),

    // COSTING TEMPLATES
    GET_COSTING_TEMPLATES("costing-templates"),
    GET_COSTING_TEMPLATE_SINGLE_BY_IDENTITY_ID("costing-templates/%s"),

    // CONNECTIONS
    GET_CONNECTIONS("connections"),

    // COMPONENTS
    GET_COMPONENTS("components"),
    POST_COMPONENTS("components"),
    GET_COMPONENT_SINGLE_BY_IDENTITY("components/%s");


    private final String endpoint;

    SDSAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }


    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return Constants.getApiUrl() + String.format(getEndpointString(), variables);
    }

}
