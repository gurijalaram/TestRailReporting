package com.apriori.entity.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum CidWorkorderApiEnum implements ExternalEndpointEnum {

    INITIALISE_FILE_UPLOAD("apriori/cost/session/ws/files"),
    CREATE_WORKORDER("apriori/cost/session/ws/workorder/orders"),
    SUBMIT_WORKORDER("apriori/cost/session/ws/workorder/orderstatus"),
    GET_WORKORDER_DETAILS("apriori/cost/session/ws/workorder/orders/%s"),
    CHECK_WORKORDER_STATUS("apriori/cost/session/ws/workorder/orderstatus/%s"),
    GET_ADMIN_INFO("apriori/cost/session/ws/workspace/%s/scenarios/%s/%s/%s/iterations/latest/admin-info"),
    GET_IMAGES("ws/viz/images/%s"),
    GET_IMAGE_INFO("apriori/cost/session/ws/workspace/%s/scenarios/%s/%s/%s/iterations/%s/image-info"),
    GET_LATEST_ITERATION("apriori/cost/session/ws/workspace/%s/scenarios/%s/%s/%s"),
    GET_IMAGE_BY_SCENARIO_ITERATION_KEY("apriori/cost/session/ws/workspace/%s/scenarios/%s/%s/%s/iterations/%s/image-info/"),
    GET_CAD_METADATA("apriori/cost/session/ws/workspace/cad-metadata/%s"),
    INITIALIZE_COST_SCENARIO("apriori/cost/session/ws/workspace/%s/scenarios/%s/%s/%s/iterations/%s/production-info");

    private final String endpoint;

    CidWorkorderApiEnum(String endpoint) {
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
