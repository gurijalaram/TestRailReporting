package com.apriori.sds.controller;

import com.apriori.AuthUserContextUtil;
import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.response.ScenarioCostingDefaultsResponse;

public class IterationController extends RequestEntityUtil {

    private static UserCredentials testingUser = UserUtil.getUser();

    /**
     * Retrieves the materialMode value from SDS Costing Defaults
     *
     * @param scenario  Scenario identity
     * @param component Component Identity
     * @return Material Mode
     */
    public static String getMaterialMode(String scenario, String component) {
        final RequestEntity requestEntity =
            init(SDSAPIEnum.GET_SCENARIO_COSTING_DEFAULTS_BY_COMPONENT_SCENARIO_IDS,
                ScenarioCostingDefaultsResponse.class)
                .apUserContext(new AuthUserContextUtil().getAuthUserContext(testingUser.getEmail()))
                .inlineVariables(
                    component, scenario
                );

        ResponseWrapper<ScenarioCostingDefaultsResponse> response = HTTPRequest.build(requestEntity).get();
        return response.getResponseEntity().getMaterialMode();
    }
}
