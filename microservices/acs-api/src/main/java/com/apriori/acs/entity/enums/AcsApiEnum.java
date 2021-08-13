package com.apriori.acs.entity.enums;

import com.apriori.acs.utils.Constants;
import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum AcsApiEnum implements ExternalEndpointEnum {

    GET_DISPLAY_UNITS(String.format("ws/workspace/users/%s/display-units", Constants.USERNAME)),
    SET_DISPLAY_UNITS(String.format("ws/workspace/users/%s/display-units", Constants.USERNAME)),
    GET_UNIT_VARIANT_SETTINGS("ws/workspace/global-info/unitVariantSettings"),
    GET_CUSTOM_UNIT_VARIANT_SETTINGS(String.format("ws/workspace/users/%s/custom-unit-variant-settings", Constants.USERNAME)),
    GET_ENABLED_CURRENCY_RATE_VERSIONS("ws/workspace/global-info/enabledCurrency"),
    CREATE_MISSING_SCENARIO("ws/workspace/0/scenarios"),
    GET_SCENARIO_INFO_BY_SCENARIO_ITERATION_KEY("ws/workspace/0/scenarios/%s/%s/%s/iterations/%s/scenario-info");

    private final String endpoint;

    AcsApiEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("${env}.base_url") + String.format(getEndpointString(), variables)
                + "?key=" + PropertiesContext.get("${env}.secret_key");
    }
}
