package com.apriori.sds.tests;

import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.response.ScenarioIteration;
import com.apriori.sds.entity.response.ScenarioIterationItemsResponse;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class IterationsTest extends SDSTestUtil {

    @Test
    @TestRail(testCaseId = {"6932"})
    @Description("Find iterations for a given scenario matching a specified query.")
    public void getIterations() {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.GET_ITERATIONS_BY_COMPONENT_SCENARIO_IDS, ScenarioIterationItemsResponse.class)
                .inlineVariables(
                    getComponentId(), getScenarioId()
                )
                .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }

    @Test
    @TestRail(testCaseId = {"6930"})
    @Description("Get the latest iteration.")
    public void getIterationLatest() {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.GET_ITERATION_LATEST_BY_COMPONENT_SCENARIO_IDS, ScenarioIteration.class)
                .inlineVariables(
                    getComponentId(), getScenarioId()
                )
                .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }

    @Test
    @TestRail(testCaseId = {"6931"})
    @Description("Get the current representation of a iteration.")
    public void getIterationsByIdentity() {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.GET_ITERATION_SINGLE_BY_COMPONENT_SCENARIO_IDENTITY_IDS, ScenarioIteration.class)
                .inlineVariables(
                    getComponentId(), getScenarioId(), getIterationId()
                )
                .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }
}
