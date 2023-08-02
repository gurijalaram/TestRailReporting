package com.apriori.cidappapi.utils;

import com.apriori.cidappapi.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.enums.CidAppAPIEnum;
import com.apriori.cidappapi.models.response.componentiteration.ComponentIteration;
import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;

public class IterationsUtil {
    /**
     * GET components for the current user matching an identity and component
     *
     * @param componentInfoBuilder - the cost component object
     * @return response object
     */
    public ResponseWrapper<ComponentIteration> getComponentIterationLatest(ComponentInfoBuilder componentInfoBuilder) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.COMPONENT_ITERATION_LATEST_BY_COMPONENT_SCENARIO_IDS, ComponentIteration.class)
                .token(componentInfoBuilder.getUser().getToken())
                .inlineVariables(componentInfoBuilder.getComponentIdentity(), componentInfoBuilder.getScenarioIdentity());

        return HTTPRequest.build(requestEntity).get();
    }
}
