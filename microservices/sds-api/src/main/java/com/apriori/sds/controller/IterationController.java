package com.apriori.sds.controller;

import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.response.ScenarioCostingDefaultsResponse;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.enums.EndpointEnum;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;

public class IterationController extends RequestEntityUtil {
    /**
     * Retrieves the materialMode value from SDS Costing Defaults
     * @param scenario Scenario identity
     * @param component Component Identity
     * @return Material Mode
     */
    public static String getMaterialMode(String scenario, String component) {
        final RequestEntity requestEntity =
                initWithApUserContext(SDSAPIEnum.GET_SCENARIO_COSTING_DEFAULTS_BY_COMPONENT_SCENARIO_IDS,
                        ScenarioCostingDefaultsResponse.class)
                        .inlineVariables(
                                component, scenario
                        );

        ResponseWrapper<ScenarioCostingDefaultsResponse> response = HTTPRequest.build(requestEntity).get();
        return response.getResponseEntity().getMaterialMode();
    }

    /**
     * Initialize http2 SDS request
     * @param endpoint
     * @param returnType
     * @return
     */
    public static RequestEntity initWithApUserContext(EndpointEnum endpoint, Class<?> returnType) {
        return initBuilder(endpoint, returnType)
                .header("ap-user-context", PropertiesContext.get("${env}.ap_user_context"))
                .build();
    }


}
