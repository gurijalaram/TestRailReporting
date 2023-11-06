package com.apriori.acs.api.enums.acs;

import com.apriori.shared.util.interfaces.ExternalEndpointEnum;
import com.apriori.shared.util.properties.PropertiesContext;

public enum AcsApiEnum implements ExternalEndpointEnum {

    POST_AUTH_TOKEN("auth/token"),
    CREATE_MISSING_SCENARIO("ws/workspace/scenarios"),
    ACTIVE_DIMENSIONS("ws/workspace/%s/scenarios/%s/%s/%s/iterations/%s/gcd-info"),
    ACTIVE_AXES("ws/workspace/%s/scenarios/%s/%s/%s/iterations/%s/axes-info/active-axes"),
    ALL_MATERIAL_STOCKS_INFO("ws/workspace/vpes/%s/process-groups/%s/stocks"),
    MATERIALS_INFO("ws/workspace/vpes/%s/process-groups/%s/materials-info"),
    ARTIFACT_PROPERTIES("ws/workspace/%s/scenarios/%s/%s/%s/iterations/%s/artifact-properties/%s/artifacts"),
    ARTIFACT_TABLE_INFO("ws/workspace/process-groups/%s/artifact-tableinfos/%s"),
    CUSTOM_UNIT_VARIANT_SETTINGS("ws/workspace/users/%s/custom-unit-variant-settings"),
    COST_RESULTS("ws/workspace/%s/scenarios/%s/%s/%s/iterations/%s/cost-results"),
    DESIGN_GUIDANCE("ws/workspace/%s/scenarios/%s/%s/%s/iterations/%s/design-guidance"),
    DISPLAY_UNITS("ws/workspace/users/%s/display-units"),
    GCD_TYPES("ws/workspace/process-groups/%s/gcd-types"),
    GCD_PROPERTIES("ws/workspace/%s/scenarios/%s/%s/%s/iterations/%s/gcd-properties"),
    ENABLED_CURRENCY_RATE_VERSIONS("ws/workspace/global-info/enabledCurrency"),
    GCD_IMAGE_MAPPING("ws/workspace/%s/scenarios/%s/%s/%s/iterations/%s/image-mapping"),
    PART_PRIMARY_PROCESS_GROUPS("ws/workspace/global-info/valid-part-process-groups"),
    PRODUCTION_DEFAULTS("ws/workspace/users/%s/production-defaults"),
    TOLERANCE_POLICY_DEFAULTS("ws/workspace/users/%s/tolerance-policy-defaults"),
    USER_PREFERENCES("ws/workspace/users/%s/preferences/"),
    USER_PREFERENCE_BY_NAME("ws/workspace/users/%s/preferences/preference?key=%s"),
    PRODUCTION_INFO("ws/workspace/%s/scenarios/%s/%s/%s/iterations/%s/production-info"),
    SCENARIO_INFO_BY_SCENARIO_ITERATION_KEY("ws/workspace/%s/scenarios/%s/%s/%s/iterations/%s/scenario-info"),
    SCENARIOS_INFORMATION("ws/workspace/scenario-info/search"),
    AVAILABLE_ROUTINGS("ws/workspace/%s/scenarios/%s/%s/%s/iterations/%s/routing-selection?vpeName=%s&pgName=%s"),
    ROUTING_SELECTION("ws/workspace/%s/scenarios/%s/%s/%s/iterations/%s/routing-selection"),
    UNIT_VARIANT_SETTINGS("ws/workspace/global-info/unitVariantSettings"),
    WEB_IMAGE_BY_SCENARIO_ITERATION_KEY("ws/workspace/%s/scenarios/%s/%s/%s/iterations/%s/images/web"),
    DESKTOP_IMAGE_BY_SCENARIO_ITERATION_KEY("ws/workspace/%s/scenarios/%s/%s/%s/iterations/%s/images/desktop"),
    TWO_DIMENSIONAL_IMAGE("ws/workspace/%s/scenarios/%s/%s/%s/iterations/%s/image-2d");

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
        return PropertiesContext.get("base_api_sp_url") + String.format(getEndpointString(), variables);
    }
}
