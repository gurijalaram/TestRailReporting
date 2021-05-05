package com.apriori.sds.tests;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.CommonRequestUtil;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.response.ScenarioIteration;
import com.apriori.sds.entity.response.ScenarioIterationItemsResponse;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;
import com.apriori.sds.tests.util.SDSTestUtil;

public class IterationsTest extends SDSTestUtil {

    @Test
    @TestRail(testCaseId = {"6932"})
    @Description("Find iterations for a given scenario matching a specified query.")
    public void getIterations() {
        ResponseWrapper<ScenarioIterationItemsResponse> response = new CommonRequestUtil().getCommonRequestWithInlineVariables(SDSAPIEnum.GET_ITERATIONS_BY_COMPONENT_SCENARIO_IDS, ScenarioIterationItemsResponse.class,
            new APIAuthentication().initAuthorizationHeaderContent(token), getComponentId(), getScenarioId()
        );

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"6930"})
    @Description("Get the latest iteration.")
    public void getIterationLatest() {
        ResponseWrapper<ScenarioIteration> response =
            new CommonRequestUtil().getCommonRequestWithInlineVariables(SDSAPIEnum.GET_ITERATION_LATEST_BY_COMPONENT_SCENARIO_IDS, ScenarioIteration.class,
                new APIAuthentication().initAuthorizationHeaderContent(token), getComponentId(), getScenarioId()
            );

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = {"6931"})
    @Description("Get the current representation of a iteration.")
    public void getIterationsByIdentity() {
        ResponseWrapper<ScenarioIteration> response =
            new CommonRequestUtil().getCommonRequestWithInlineVariables(SDSAPIEnum.GET_ITERATION_SINGLE_BY_COMPONENT_SCENARIO_IDENTITY_IDS, ScenarioIteration.class,
                new APIAuthentication().initAuthorizationHeaderContent(token), getComponentId(), getScenarioId(),
                getIterationId()
            );

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }
}
