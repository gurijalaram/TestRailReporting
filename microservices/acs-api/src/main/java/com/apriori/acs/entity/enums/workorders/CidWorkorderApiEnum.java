package com.apriori.acs.entity.enums.workorders;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum CidWorkorderApiEnum implements ExternalEndpointEnum {

    CREATE_WORKORDER("ws/workorder/orders"),
    SUBMIT_WORKORDER("ws/workorder/orderstatus"),
    WORKORDER_DETAILS("ws/workorder/orders/%s"),
    CHECK_WORKORDER_STATUS("ws/workorder/orderstatus/%s"),
    ADMIN_INFO("ws/workspace/%s/scenarios/%s/%s/%s/iterations/latest/admin-info"),
    IMAGES("ws/workspace/images/%s"),
    IMAGE_INFO("ws/workspace/%s/scenarios/%s/%s/%s/iterations/%s/image-info"),
    LATEST_ITERATION("ws/workspace/%s/scenarios/%s/%s/%s"),
    CAD_METADATA("ws/workspace/cad-metadata/%s"),
    INITIALIZE_COST_SCENARIO("ws/workspace/%s/scenarios/%s/%s/%s/iterations/%s/production-info");

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
        //return PropertiesContext.get("${env}.base_url").concat(String.format(getEndpointString(), variables));
    }
}
