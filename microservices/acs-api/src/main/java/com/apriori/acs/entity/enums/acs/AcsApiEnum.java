package com.apriori.acs.entity.enums.acs;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum AcsApiEnum implements ExternalEndpointEnum {

    GET_DISPLAY_UNITS("ws/workspace/users/%s/display-units"),
    SET_DISPLAY_UNITS("ws/workspace/users/%s/display-units"),
    GET_UNIT_VARIANT_SETTINGS("ws/workspace/global-info/unitVariantSettings"),
    GET_CUSTOM_UNIT_VARIANT_SETTINGS("ws/workspace/users/%s/custom-unit-variant-settings"),
    GET_ENABLED_CURRENCY_RATE_VERSIONS("ws/workspace/global-info/enabledCurrency"),
    CREATE_MISSING_SCENARIO("ws/workspace/0/scenarios"),
    GET_SCENARIO_INFO_BY_SCENARIO_ITERATION_KEY("ws/workspace/0/scenarios/%s/%s/%s/iterations/%s/scenario-info"),
    GET_SCENARIOS_INFORMATION("ws/workspace/scenario-info/search"),
    GET_SET_TOLERANCE_POLICY_DEFAULTS("ws/workspace/users/%s/tolerance-policy-defaults"),
    GET_SET_PRODUCTION_DEFAULTS("ws/workspace/users/%s/production-defaults"),
    GET_PART_PRIMARY_PROCESS_GROUPS("ws/workspace/global-info/valid-part-process-groups"),
    GET_SET_USER_PREFERENCES("ws/workspace/users/%s/preferences/"),
    GET_SET_USER_PREFERENCE_BY_NAME("ws/workspace/users/%s/preferences/preference?key=%s"),
    GET_2D_IMAGE("ws/workspace/%s/scenarios/%s/%s/%s/iterations/%s/image-2d"),
    GET_ACTIVE_DIMENSIONS("ws/workspace/%s/scenarios/%s/%s/%s/iterations/%s/gcd-info"),
    GET_ACTIVE_AXES("ws/workspace/%s/scenarios/%s/%s/%s/iterations/%s/axes-info/active-axes");

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
        return PropertiesContext.get("${env}.base_url") + String.format(getEndpointString(), variables);
    }
}