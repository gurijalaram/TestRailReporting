package com.apriori.sds.entity.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum SDSAPIEnum implements ExternalEndpointEnum {

    // AP FILES
    POST_AP_FILES("ap-files"),

    // ITERATIONS
    GET_ITERATIONS_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s/iterations"),
    GET_ITERATION_LATEST_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s/iterations/latest"),
    GET_ITERATION_SINGLE_BY_COMPONENT_SCENARIO_IDENTITY_IDS("components/%s/scenarios/%s/iterations/%s"),

    // SCENARIO
    GET_SCENARIOS_BY_COMPONENT_IDS("components/%s/scenarios"),
    GET_SCENARIO_SINGLE_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s"),
    GET_SCENARIO_COSTING_DEFAULTS_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s/costing-defaults"),
    GET_SCENARIO_CUSTOM_IMAGE_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s/custom-image"),
    GET_SCENARIO_WEB_IMAGE_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s/web-image"),
    GET_SCENARIO_MANIFEST_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s/manifest"),
    GET_WATCHPOINT_REPORT_SCENARIO_BY_COMPONENT_SCENARIO_IDs("components/%s/scenarios/%s/watchpoint-report"),


    POST_SCENARIO_BY_COMPONENT_ID("components/%s/scenarios"),
    PATCH_SCENARIO_BY_COMPONENT_SCENARIO_IDs("components/%s/scenarios/%s"),
    POST_COPY_SCENARIO_BY_COMPONENT_SCENARIO_IDs("components/%s/scenarios/%s/copy"),
    POST_COST_SCENARIO_BY_COMPONENT_SCENARIO_IDs("components/%s/scenarios/%s/cost"),
    POST_FORK_SCENARIO_BY_COMPONENT_SCENARIO_IDs("components/%s/scenarios/%s/fork"),
    POST_PUBLISH_SCENARIO_BY_COMPONENT_SCENARIO_IDs("components/%s/scenarios/%s/publish"),
    POST_WATCHPOINT_REPORT_SCENARIO_BY_COMPONENT_SCENARIO_IDs("components/%s/scenarios/%s/watchpoint-report"),
    DELETE_SCENARIO_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s"),

    // SCENARIO ITERATIONS
    POST_SCENARIO_ITERATIONS("/scenario-iterations"),

    // SCENARIO ASSOCIATIONS
    GET_ASSOCIATIONS_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s/associations"),
    GET_ASSOCIATIONS_SINGLE_BY_COMPONENT_SCENARIO_IDENTITY_IDS("components/%s/scenarios/%s/associations/%s"),
    POST_ASSOCIATION_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s/associations"),
    DELETE_ASSOCIATION_BY_COMPONENT_SCENARIO_IDENTITY_IDS("components/%s/scenarios/%s/associations/%s"),
    PATCH_ASSOCIATION_BY_COMPONENT_SCENARIO_IDENTITY_IDS("components/%s/scenarios/%s/associations/%s"),

    // COSTING TEMPLATES
    GET_COSTING_TEMPLATES("costing-templates"),
    GET_COSTING_TEMPLATE_SINGLE_BY_IDENTITY_ID("costing-templates/%s"),

    // CONNECTIONS
    GET_CONNECTIONS("connections"),
    POST_CONNECTIONS("connections"),
    DELETE_CONNECTIONS_BY_ID("connections/%s"),
    PATCH_CONNECTIONS_BY_ID("connections/%s"),

    // COMPONENTS
    GET_COMPONENTS("components"),
    POST_COMPONENTS("components"),
    GET_COMPONENT_SINGLE_BY_IDENTITY("components/%s"),

    // FEATURE DECISIONS

    FEATURE_DECISIONS("feature-decisions"),

    // SECONDARY PROCESS
    GET_SECONDARY_PROCESS_BY_COMPONENT_SCENARIO_IDS_VPE_PG_NAMES("components/%s/scenarios/%s/vpes/%s/process-groups/%s/secondary-processes");


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
        return PropertiesContext.get("${env}.sds.api_url") + String.format(getEndpointString(), variables) + "?key=" + PropertiesContext.get("${env}.secret_key");
    }

}
