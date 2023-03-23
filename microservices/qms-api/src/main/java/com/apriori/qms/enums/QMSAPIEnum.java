package com.apriori.qms.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum QMSAPIEnum implements ExternalEndpointEnum {

    // PROJECTS
    PROJECTS("projects"),
    PROJECT("projects/%s"),

    //USER PREFERENCE
    USER_PREFERENCE("user-preferences"),

    //BID PACKAGE
    BID_PACKAGES("bid-packages"),
    BID_PACKAGE("bid-packages/%s"),
    BID_PACKAGE_PROJECTS("bid-packages/%s/projects"),
    BID_PACKAGE_PROJECT("bid-packages/%s/projects/%s"),
    BID_PACKAGE_PROJECT_USERS("bid-packages/%s/projects/%s/project-users"),
    BID_PACKAGE_PROJECT_USER("bid-packages/%s/projects/%s/project-users/%s"),
    BID_PACKAGE_PROJECT_ITEMS("bid-packages/%s/projects/%s/project-items"),
    BID_PACKAGE_PROJECT_ITEM("bid-packages/%s/projects/%s/project-items/%s"),
    BID_PACKAGE_ITEMS("bid-packages/%s/bid-package-items"),
    BID_PACKAGE_ITEM("bid-packages/%s/bid-package-items/%s"),

    //COMPONENTS
    COMPONENT("components/%s"),

    //SCENARIOS
    COMPONENT_SCENARIOS("components/%s/scenarios"),
    COMPONENT_SCENARIO("components/%s/scenarios/%s"),
    COMPONENT_SCENARIO_USERS("components/%s/scenarios/%s/users"),

    //SCENARIO_DISCUSSIONS
    SCENARIO_DISCUSSIONS("scenario-discussions"),
    SCENARIO_DISCUSSIONS_FILTER("scenario-discussions/filter"),
    SCENARIO_DISCUSSION("scenario-discussions/%s"),
    SCENARIO_DISCUSSION_COMMENTS("scenario-discussions/%s/comments"),
    SCENARIO_DISCUSSION_COMMENT("scenario-discussions/%s/comments/%s"),

    //ITERATIONS
    COMPONENT_ITERATION_LATEST_BY_COMPONENT_SCENARIO_ID("components/%s/scenarios/%s/iterations/latest"),

    //PARTICIPANTS
    PARTICIPANTS("participants"),

    //LAYOUTS AND VIEW ELEMENTS
    LAYOUTS("layouts"),
    LAYOUT(LAYOUTS.getEndpointString().concat("/%s")),
    LAYOUT_VIEW_ELEMENTS(LAYOUT.getEndpointString().concat("/view-elements")),
    LAYOUT_VIEW_ELEMENT(LAYOUT_VIEW_ELEMENTS.getEndpointString().concat("/%s")),

    VIEW_ELEMENTS("view-elements"),
    VIEW_ELEMENT(VIEW_ELEMENTS.getEndpointString().concat("/%s")),
    VIEW_ELEMENT_LAYOUT_CONFIGURATIONS(VIEW_ELEMENT.getEndpointString().concat("/layout-configurations")),
    VIEW_ELEMENT_LAYOUT_CONFIGURATION(VIEW_ELEMENT_LAYOUT_CONFIGURATIONS.getEndpointString().concat("/%s")),
    VIEW_ELEMENT_LAYOUT_CONFIGURATION_SHARE(VIEW_ELEMENT_LAYOUT_CONFIGURATION.getEndpointString().concat("/share"));

    private final String endpoint;

    QMSAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("qms.api_url") + String.format(getEndpointString(), variables) + this.addQuery(getEndpointString());
    }

    private String addQuery(String endpointString) {
        String querySymbol = "?";

        if (endpointString.contains("?")) {
            querySymbol = "&";
        }

        return querySymbol + "key=" + PropertiesContext.get("secret_key");
    }
}
