package com.apriori.sds.api.tests;

import com.apriori.sds.api.enums.SDSAPIEnum;
import com.apriori.sds.api.models.response.ScenarioIteration;
import com.apriori.sds.api.models.response.ScenarioIterationItemsResponse;
import com.apriori.sds.api.util.SDSTestUtil;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class IterationsTest extends SDSTestUtil {

    @Test
    @TestRail(id = {6932})
    @Description("Find iterations for a given scenario matching a specified query.")
    public void getIterations() {
        final RequestEntity requestEntity =
            requestEntityUtil.init(SDSAPIEnum.GET_ITERATIONS_BY_COMPONENT_SCENARIO_IDS, ScenarioIterationItemsResponse.class)
                .inlineVariables(
                    getComponentId(), getScenarioId()
                )
                .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }

    @Test
    @TestRail(id = {6930})
    @Description("Get the latest iteration.")
    public void getIterationLatest() {
        final RequestEntity requestEntity =
            requestEntityUtil.init(SDSAPIEnum.GET_ITERATION_LATEST_BY_COMPONENT_SCENARIO_IDS, ScenarioIteration.class)
                .inlineVariables(
                    getComponentId(), getScenarioId()
                )
                .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }

    @Test
    @TestRail(id = {6931})
    @Description("Get the current representation of a iteration.")
    public void getIterationsByIdentity() {
        final RequestEntity requestEntity =
            requestEntityUtil.init(SDSAPIEnum.GET_ITERATION_SINGLE_BY_COMPONENT_SCENARIO_IDENTITY_IDS, ScenarioIteration.class)
                .inlineVariables(
                    getComponentId(), getScenarioId(), getIterationId()
                )
                .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }
}
