package com.apriori.cid.api.utils;

import com.apriori.cid.api.enums.CidAppAPIEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.component.componentiteration.ComponentIteration;

public class IterationsUtil {
    /**
     * GET components for the current user matching an identity and component
     *
     * @param componentInfoBuilder - the cost component object
     * @return response object
     */
    public ResponseWrapper<ComponentIteration> getComponentIterationLatest(ComponentInfoBuilder componentInfoBuilder) {
        RequestEntity requestEntity =
            RequestEntityUtil_Old.init(CidAppAPIEnum.COMPONENT_ITERATION_LATEST_BY_COMPONENT_SCENARIO_IDS, ComponentIteration.class)
                .token(componentInfoBuilder.getUser().getToken())
                .inlineVariables(componentInfoBuilder.getComponentIdentity(), componentInfoBuilder.getScenarioIdentity());

        return HTTPRequest.build(requestEntity).get();
    }
}
