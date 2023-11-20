package com.apriori.sds.api.controller;

import com.apriori.sds.api.enums.SDSAPIEnum;
import com.apriori.sds.api.models.response.ScenarioCostingDefaultsResponse;
import com.apriori.sds.api.util.SDSTestUtil;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.AuthUserContextUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;

public class IterationController extends SDSTestUtil {

    /**
     * Retrieves the materialMode value from SDS Costing Defaults
     *
     * @param scenario  Scenario identity
     * @param component Component Identity
     * @return Material Mode
     */
    public static String getMaterialMode(String scenario, String component) {
        final RequestEntity requestEntity =
            requestEntityUtil.init(SDSAPIEnum.GET_SCENARIO_COSTING_DEFAULTS_BY_COMPONENT_SCENARIO_IDS,
                ScenarioCostingDefaultsResponse.class)
                .apUserContext(new AuthUserContextUtil().getAuthUserContext(testingUser.getEmail()))
                .inlineVariables(
                    component, scenario
                );

        ResponseWrapper<ScenarioCostingDefaultsResponse> response = HTTPRequest.build(requestEntity).get();
        return response.getResponseEntity().getMaterialMode();
    }
}
