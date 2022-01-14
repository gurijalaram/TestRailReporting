package com.apriori.cidappapi.entity.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum CidAppAPIEnum implements ExternalEndpointEnum {

    //COMPONENTS
    GET_COMPONENTS("components"),
    GET_COMPONENT_BY_COMPONENT_ID("components/%s"),
    GET_HOOPS_IMAGE_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s/hoops-image"),
    GET_SCENARIO_REPRESENTATION_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s"),
    POST_COMPONENTS("components"),
    POST_COMPONENT_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s/cost"),

    //ITERATIONS
    GET_COMPONENT_ITERATION_LATEST_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s/iterations/latest"),

    //PREFERENCES
    GET_PREFERENCES("users/current/preferences?pageSize=100"),
    PATCH_PREFERENCES("users/current/preferences"),

    //SCENARIO
    POST_COST_SCENARIO_BY_COMPONENT_SCENARIO_IDs("components/%s/scenarios/%s/cost"),

    //COSTING TEMPLATES
    GET_COSTING_TEMPLATES("costing-templates"),

    //PUBLISH
    POST_PUBLISH_SCENARIO("components/%s/scenarios/%s/publish"),

    //USER
    GET_CURRENT_USER("users/current"),

    //PEOPLE
    GET_CURRENT_PERSON("people?username[EQ]=%s"),

    //CUSTOMIZATIONS
    GET_CUSTOMIZATIONS("customizations");

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
        return String.format((PropertiesContext.get("${env}.cidapp.api_url")).concat("%s"), String.format(getEndpointString(), variables));
    }
}

