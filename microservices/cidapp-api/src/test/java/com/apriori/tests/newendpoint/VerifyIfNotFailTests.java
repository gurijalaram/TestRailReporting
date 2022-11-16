package com.apriori.tests.newendpoint;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.apriori.cidappapi.entity.request.ScenarioIterationRequest;
import com.apriori.cidappapi.entity.request.operators.Params;
import com.apriori.cidappapi.utils.ScenarioIterationService;
import com.apriori.entity.request.ErrorRequestResponse;
import com.apriori.entity.response.CssComponentResponse;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Arrays;

public class VerifyIfNotFailTests {
    private static ScenarioIterationService scenarioIterationService = new ScenarioIterationService();

    @BeforeClass
    public static void testSetup() {
        scenarioIterationService.loadDataIfNotExists();
    }

    @Test
    @TestRail(testCaseId = {"12432"})
    @Description("Verify that POST scenario-iterations do not fail on dot notation")
    public void verifyIfNotFailOnDotNotation() {
        ScenarioIterationRequest scenarioIterationRequest =
            JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "AndOperatorData.json").getPath(), ScenarioIterationRequest.class);

        Params params = new Params();
        params.setValue("0");
        params.setProperty("costingInput.identity");
        scenarioIterationRequest.getQuery().getFilter().getAnd().get(0).setEquals(params);

        ResponseWrapper<CssComponentResponse> scenarioIterationRespond = null;
        try {
            scenarioIterationRespond =
                scenarioIterationService.getScenarioIterationWithParamsPost(scenarioIterationRequest);
        } finally {
            ResponseWrapper<CssComponentResponse> finalScenarioIterationRespond = scenarioIterationRespond;
            assertDoesNotThrow(
                (Executable) finalScenarioIterationRespond::getResponseEntity);
        }
    }

    @Test
    @TestRail(testCaseId = {"12433"})
    @Description("Verify if convert string value to java enum type of thumbnail.imageType")
    public void verifyIfConvertStringIntoEnum() {
        ScenarioIterationRequest scenarioIterationRequest =
            JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "AndOperatorData.json").getPath(), ScenarioIterationRequest.class);

        Params params = new Params();
        params.setValue("THUMBNAIL");
        params.setProperty("thumbnail.imageType");
        scenarioIterationRequest.getQuery().getFilter().getAnd().get(0).setEquals(params);

        ResponseWrapper<CssComponentResponse> scenarioIterationRespond = null;
        try {
            scenarioIterationRespond =
                scenarioIterationService.getScenarioIterationWithParamsPost(scenarioIterationRequest);
        } finally {
            ResponseWrapper<CssComponentResponse> finalScenarioIterationRespond = scenarioIterationRespond;
            assertDoesNotThrow(
                (Executable) finalScenarioIterationRespond::getResponseEntity);
        }
    }

    @Test
    @TestRail(testCaseId = {"12434"})
    @Description("Verify items found searching by Date value")
    public void verifyIfItemsFoundByDateValue() {
        ScenarioIterationRequest scenarioIterationRequest =
            JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "AndOperatorData.json").getPath(), ScenarioIterationRequest.class);

        Params params = new Params();
        params.setValue("2022-04-07T08:33:26.644Z");
        params.setProperty("componentCreatedAt");
        scenarioIterationRequest.getQuery().getFilter().getAnd().get(0).setEquals(params);

        ResponseWrapper<CssComponentResponse> scenarioIterationRespond = null;
        try {
            scenarioIterationRespond =
                scenarioIterationService.getScenarioIterationWithParamsPost(scenarioIterationRequest);
        } finally {
            ResponseWrapper<CssComponentResponse> finalScenarioIterationRespond = scenarioIterationRespond;
            assertDoesNotThrow(
                (Executable) finalScenarioIterationRespond::getResponseEntity);
        }
    }

    @Test
    @TestRail(testCaseId = {"12435"})
    @Description("verify if we have compliance check of value types for 'in' operator")
    public void verifyIfThereIsComplianceCheckForInOperator() {
        ScenarioIterationRequest scenarioIterationRequest =
            JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "EqualOperatorData.json").getPath(), ScenarioIterationRequest.class);

        Params params = new Params();
        params.setValues(Arrays.asList("1", "2", "3"));
        params.setProperty("iteration");
        scenarioIterationRequest.getQuery().getFilter().getAnd().get(0).setIn(params);

        ResponseWrapper<CssComponentResponse> scenarioIterationRespond = null;
        try {
            scenarioIterationRespond =
                scenarioIterationService.getScenarioIterationWithParamsPost(scenarioIterationRequest);
        } finally {
            ResponseWrapper<CssComponentResponse> finalScenarioIterationRespond = scenarioIterationRespond;
            assertDoesNotThrow(
                (Executable) finalScenarioIterationRespond::getResponseEntity);
        }
    }

    @Test
    @TestRail(testCaseId = {"12436"})
    @Description("verify that Null value for any predicate operator does not leads to 500 error")
    public void verifyThatNullValueForAnyOperatorDoesNotLeadToError() {
        ScenarioIterationRequest scenarioIterationRequest =
            JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "AndOperatorData.json").getPath(), ScenarioIterationRequest.class);

        Params params = new Params();
        params.setValue(null);
        params.setProperty("componentName");
        scenarioIterationRequest.getQuery().getFilter().getAnd().get(0).setEquals(params);


        ResponseWrapper<ErrorRequestResponse> scenarioIterationRespond =
            scenarioIterationService.getScenarioIterationWithParamsPostForErrors(scenarioIterationRequest);

        assertEquals("400", scenarioIterationRespond.getResponseEntity().getStatus());
        assertEquals("Bad Request", scenarioIterationRespond.getResponseEntity().getError());
        assertEquals("Invalid value was provided for operator equals. Please, refer to the Swagger documentation for an example",
            scenarioIterationRespond.getResponseEntity().getMessage());
    }

    @Test
    @TestRail(testCaseId = {"12437"})
    @Description("verify that between operator with different value types should not fail")
    public void verifyBetweenOperatorWithDiffValuesTypesNotFail() {
        ScenarioIterationRequest scenarioIterationRequest =
            JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "BetweenOperatorData.json").getPath(), ScenarioIterationRequest.class);

        Params params = new Params();
        params.setMax("2");
        params.setMin(1);
        params.setProperty("iteration");
        scenarioIterationRequest.getQuery().getFilter().getAnd().get(0).setBetween(params);

        ResponseWrapper<CssComponentResponse> scenarioIterationRespond = null;
        try {
            scenarioIterationRespond =
                scenarioIterationService.getScenarioIterationWithParamsPost(scenarioIterationRequest);
        } finally {
            ResponseWrapper<CssComponentResponse> finalScenarioIterationRespond = scenarioIterationRespond;
            assertDoesNotThrow(
                (Executable) finalScenarioIterationRespond::getResponseEntity);
        }
    }
}