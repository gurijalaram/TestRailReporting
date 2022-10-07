package com.apriori.cisapi.entity.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum CisAPIEnum implements ExternalEndpointEnum {

    //APPLICATION METADATA
    GET_APPLICATION_METADATA("application-metadata"),

    //COMPONENT GROUP
    COMPONENTS_CREATE("components/create"),

    //COMPONENTS
    SCENARIO_REPRESENTATION_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s"),
    COMPONENT_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s/cost"),
    HOOPS_IMAGE_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s/hoops-image"),
    GROUP_COST_COMPONENTS("scenarios/cost"),
    COMPONENTS("components"),
    COMPONENTS_BY_COMPONENT_ID("components/%s"),

    //ITERATIONS
    COMPONENT_ITERATION_LATEST_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s/iterations/latest"),

    //PEOPLE
    PEOPLE("people"),

    //CAD FILES
    CAD_FILES("cad-files"),

    //COSTING TEMPLATES
    COSTING_TEMPLATES("costing-templates"),

    //USER PREFERENCES
    USERS_CURRENT_PREFERENCES("users/current/preferences?pageSize=100"),
    PATCH_USERS_CURRENT_PREFERENCES("users/current/preferences"),

    //USERS
    CURRENT_USER("users/current"),

    //PEOPLE
    CURRENT_PERSON("people?username[EQ]=%s"),

    //PUBLISH
    PUBLISH_SCENARIO("components/%s/scenarios/%s/publish"),
    PUBLISH_SCENARIOS("scenarios/publish"),

    //SCENARIO
    COST_SCENARIO_BY_COMPONENT_SCENARIO_IDs("components/%s/scenarios/%s/cost"),
    EDIT_SCENARIO_BY_COMPONENT_SCENARIO_IDs("components/%s/scenarios/%s/fork"),
    MANIFEST_SCENARIO_BY_COMPONENT_SCENARIO_IDs("components/%s/scenarios/%s/manifest"),
    EDIT_SCENARIOS("scenarios/fork"),
    SCENARIO_BY_COMPONENT_SCENARIO_ID("components/%s/scenarios/%s"),
    COPY_SCENARIO_BY_COMPONENT_SCENARIO_IDs("components/%s/scenarios/%s/copy"),

    //SCENARIO ASSOCIATION GROUP OPERATIONS
    SCENARIO_ASSOCIATIONS("components/%s/scenarios/%s/associations");


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