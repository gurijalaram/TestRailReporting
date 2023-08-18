package com.apriori;

import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.sds.enums.SDSAPIEnum;
import com.apriori.sds.models.response.ScenarioIteration;
import com.apriori.sds.models.response.ScenarioIterationItemsResponse;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

public class IterationsTest extends SDSTestUtil {

    @Test
    @TestRail(id = {6932})
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
    @TestRail(id = {6930})
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
    @TestRail(id = {6931})
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