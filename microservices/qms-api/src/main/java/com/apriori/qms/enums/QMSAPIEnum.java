package com.apriori.qms.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum QMSAPIEnum implements ExternalEndpointEnum {

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


    //ITERATIONS
    COMPONENT_ITERATION_LATEST_BY_COMPONENT_SCENARIO_ID("components/%s/scenarios/%s/iterations/latest");

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
        return PropertiesContext.get("${env}.qms.api_url") + String.format(getEndpointString(), variables) + this.addQuery(getEndpointString());
    }

    private String addQuery(String endpointString) {
        String querySymbol = "?";

        if (endpointString.contains("?")) {
            querySymbol = "&";
        }

        return querySymbol + "key=" + PropertiesContext.get("${env}.secret_key");
    }
}
