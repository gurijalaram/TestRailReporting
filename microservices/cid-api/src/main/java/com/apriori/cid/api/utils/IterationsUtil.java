package com.apriori.cid.api.utils;

import com.apriori.cid.api.enums.CidAppAPIEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.component.componentiteration.ComponentIteration;
import com.apriori.shared.util.models.response.component.componentiteration.ComponentIterations;

public class IterationsUtil {
    /**
     * GET latest iteration details for scenario matching an identity and component
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

    /**
     * GET iteration list for scenario matching an identity and component
     *
     * @param componentInfoBuilder - the component object
     * @return response object
     */
    public ResponseWrapper<ComponentIterations> getComponentIterations(ComponentInfoBuilder componentInfoBuilder) {
        RequestEntity requestEntity =
            RequestEntityUtil_Old.init(CidAppAPIEnum.COMPONENT_ITERATIONS_BY_COMPONENT_SCENARIO_IDS, ComponentIterations.class)
                .token(componentInfoBuilder.getUser().getToken())
                .inlineVariables(componentInfoBuilder.getComponentIdentity(), componentInfoBuilder.getScenarioIdentity());

        return HTTPRequest.build(requestEntity).get();
    }
}
