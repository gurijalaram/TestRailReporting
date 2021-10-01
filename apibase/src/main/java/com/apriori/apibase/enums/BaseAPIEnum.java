package com.apriori.apibase.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum BaseAPIEnum implements ExternalEndpointEnum {

    GET_TOLERANCE_VALUE("workspace/users/me/tolerance-policy-defaults"),
    POST_AUTH_TOKEN("auth/token"),
    POST_TOLERANCE_POLICY("workspace/users/me/tolerance-policy-defaults"),
    POST_PREFERENCES("workspace/users/me/preferences/preference?key=cost.table.decimal.places"),
    POST_PREFERENCES_WITH_COLOR("workspace/users/me/preferences/preference?key=selectionColor"),
    POST_PREFERENCES_SCENARIO_NAME("workspace/users/me/preferences/preference?key=defaultScenarioName"),
    POST_PRODUCTION_DEFAULTS("workspace/users/me/production-defaults"),
    POST_TOLERANCE_DEFAULTS("workspace/users/me/tolerance-policy-defaults"),
    POST_DISPLAY_UNITS("workspace/users/me/display-units");

    private final String endpoint;

    BaseAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }


    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("${env}.base_url") + "ws/" + String.format(getEndpointString(), variables);
    }

}
