package com.apriori.cidappapi.entity.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum CidAppAPIEnum implements ExternalEndpointEnum {

    //COMPONENT GROUP
    COMPONENTS_CREATE("components/create"),

    //COMPONENTS
    COMPONENTS("components"),
    COMPONENTS_BY_COMPONENT_ID("components/%s"),
    HOOPS_IMAGE_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s/hoops-image"),
    SCENARIO_REPRESENTATION_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s"),
    COMPONENT_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s/cost"),
    GROUP_COST_COMPONENTS("scenarios/cost"),

    //CAD FILES
    CAD_FILES("cad-files"),

    //ITERATIONS
    COMPONENT_ITERATION_LATEST_BY_COMPONENT_SCENARIO_IDS("components/%s/scenarios/%s/iterations/latest"),

    //ROUTINGS
    ROUTINGS("components/%s/scenarios/%s/available-routings"),

    //PREFERENCES
    PREFERENCES("users/current/preferences"),
    PREFERENCES_PAGE_SIZE("users/current/preferences?pageSize=100"),

    //SCENARIO
    COST_SCENARIO_BY_COMPONENT_SCENARIO_IDs("components/%s/scenarios/%s/cost"),
    COPY_SCENARIO_BY_COMPONENT_SCENARIO_IDs("components/%s/scenarios/%s/copy"),
    EDIT_SCENARIO_BY_COMPONENT_SCENARIO_IDs("components/%s/scenarios/%s/fork"),
    MANIFEST_SCENARIO_BY_COMPONENT_SCENARIO_IDs("components/%s/scenarios/%s/manifest"),
    EDIT_SCENARIOS("scenarios/fork"),
    DELETE_SCENARIO("components/%s/scenarios/%s"),

    //COSTING TEMPLATES
    COSTING_TEMPLATES("costing-templates"),
    COSTING_TEMPLATES_ID("costing-templates/%s"),

    //PUBLISH
    PUBLISH_SCENARIO("components/%s/scenarios/%s/publish"),
    PUBLISH_SCENARIOS("scenarios/publish"),

    //USER
    CURRENT_USER("users/current"),

    //PEOPLE
    CURRENT_PERSON("people?username[EQ]=%s"),

    //CUSTOMIZATIONS
    CUSTOMIZATIONS("customizations"),

    //SCENARIO ASSOCIATION GROUP OPERATIONS
    SCENARIO_ASSOCIATIONS("components/%s/scenarios/%s/associations"),

    //APPLICATION METADATA
    APPLICATION_METADATA("application-metadata");

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

