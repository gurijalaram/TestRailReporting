package com.apriori.sds.tests;

import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.response.ScenarioIteration;
import com.apriori.sds.entity.response.ScenarioIterationItemsResponse;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;
import util.SDSTestUtil;

public class IterationsTest extends SDSTestUtil {

    @Test
    @TestRail(testCaseId = {"6932"})
    @Description("Find iterations for a given scenario matching a specified query.")
    public void getIterations() {
        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_ITERATIONS_BY_COMPONENT_SCENARIO_IDS, ScenarioIterationItemsResponse.class)
                .inlineVariables(
                    getComponentId(), getScenarioId()
                );

        ResponseWrapper<ScenarioIterationItemsResponse> response = HTTP2Request.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"6930"})
    @Description("Get the latest iteration.")
    public void getIterationLatest() {
        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_ITERATION_LATEST_BY_COMPONENT_SCENARIO_IDS, ScenarioIteration.class)
                .inlineVariables(
                    getComponentId(), getScenarioId()
                );

        ResponseWrapper<ScenarioIteration> response = HTTP2Request.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"6931"})
    @Description("Get the current representation of a iteration.")
    public void getIterationsByIdentity() {
        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_ITERATION_SINGLE_BY_COMPONENT_SCENARIO_IDENTITY_IDS, ScenarioIteration.class)
                .inlineVariables(
                    getComponentId(), getScenarioId(), getIterationId()
                );

        ResponseWrapper<ScenarioIteration> response = HTTP2Request.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }
}
