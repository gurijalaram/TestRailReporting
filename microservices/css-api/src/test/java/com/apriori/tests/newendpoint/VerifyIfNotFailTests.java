package com.apriori.tests.newendpoint;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.apriori.css.entity.apicalls.ScenarioIterationService;
import com.apriori.css.entity.request.ScenarioIterationRequest;
import com.apriori.css.entity.response.CssComponentResponse;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import org.junit.BeforeClass;
import org.junit.Test;

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
        ScenarioIterationRequest scenarioIterationRequest  =
            (ScenarioIterationRequest) JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "VerifyNotFailTest1Data.json"
                ).getPath(), ScenarioIterationRequest.class);

        ResponseWrapper<CssComponentResponse> scenarioIterationRespond = null;
        try {
            scenarioIterationRespond =
                    scenarioIterationService.getScenarioIterationWithParamsPost(scenarioIterationRequest);
        } finally {
            ResponseWrapper<CssComponentResponse> finalScenarioIterationRespond = scenarioIterationRespond;
            assertDoesNotThrow(
                () -> {
                    finalScenarioIterationRespond.getResponseEntity();
                });
        }
    }

    @Test
    @TestRail(testCaseId = {"12433"})
    @Description("Verify if convert string value to java enum type of thumbnail.imageType")
    public void verifyIfConvertStringIntoEnum() {
        ScenarioIterationRequest scenarioIterationRequest  =
            (ScenarioIterationRequest) JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "VerifyNotFailTest2Data.json"
                ).getPath(), ScenarioIterationRequest.class);

        ResponseWrapper<CssComponentResponse> scenarioIterationRespond = null;
        try {
            scenarioIterationRespond =
                    scenarioIterationService.getScenarioIterationWithParamsPost(scenarioIterationRequest);
        } finally {
            ResponseWrapper<CssComponentResponse> finalScenarioIterationRespond = scenarioIterationRespond;
            assertDoesNotThrow(
                () -> {
                    finalScenarioIterationRespond.getResponseEntity();
                });
        }
    }

    @Test
    @TestRail(testCaseId = {"12434"})
    @Description("Verify items found searching by Date value")
    public void verifyIfItemsFoundByDateValue() {
        ScenarioIterationRequest scenarioIterationRequest  =
            (ScenarioIterationRequest) JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "VerifyNotFailTest3Data.json"
                ).getPath(), ScenarioIterationRequest.class);

        ResponseWrapper<CssComponentResponse> scenarioIterationRespond = null;
        try {
            scenarioIterationRespond =
                    scenarioIterationService.getScenarioIterationWithParamsPost(scenarioIterationRequest);
        } finally {
            ResponseWrapper<CssComponentResponse> finalScenarioIterationRespond = scenarioIterationRespond;
            assertDoesNotThrow(
                () -> {
                    finalScenarioIterationRespond.getResponseEntity();
                });
        }
    }

    @Test
    @TestRail(testCaseId = {"12435"})
    @Description("verify if we have compliance check of value types for 'in' operator")
    public void verifyIfThereIsComplianceCheckForInOperator() {
        ScenarioIterationRequest scenarioIterationRequest  =
            (ScenarioIterationRequest) JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "VerifyNotFailTest4Data.json"
                ).getPath(), ScenarioIterationRequest.class);

        ResponseWrapper<CssComponentResponse> scenarioIterationRespond = null;
        try {
            scenarioIterationRespond =
                    scenarioIterationService.getScenarioIterationWithParamsPost(scenarioIterationRequest);
        } finally {
            ResponseWrapper<CssComponentResponse> finalScenarioIterationRespond = scenarioIterationRespond;
            assertDoesNotThrow(
                () -> {
                    finalScenarioIterationRespond.getResponseEntity();
                });
        }
    }

    @Test
    @TestRail(testCaseId = {"12436"})
    @Description("verify that Null value for any predicate operator does not leads to 500 error")
    public void verifyThatNullValueForAnyOperatorDoesNotLeadToError() {
        ScenarioIterationRequest scenarioIterationRequest  =
            (ScenarioIterationRequest) JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "VerifyNotFailTest5Data.json"
                ).getPath(), ScenarioIterationRequest.class);

        ResponseWrapper<CssComponentResponse> scenarioIterationRespond = null;
        try {
            scenarioIterationRespond =
                    scenarioIterationService.getScenarioIterationWithParamsPost(scenarioIterationRequest);
        } finally {
            ResponseWrapper<CssComponentResponse> finalScenarioIterationRespond = scenarioIterationRespond;
            assertDoesNotThrow(
                () -> {
                    finalScenarioIterationRespond.getResponseEntity();
                });
        }
    }

    @Test
    @TestRail(testCaseId = {"12437"})
    @Description("verify that between operator with different value types should not fail")
    public void verifyBetweenOperatorWithDiffValuesTypesNotFail() {
        ScenarioIterationRequest scenarioIterationRequest  =
            (ScenarioIterationRequest) JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "VerifyNotFailTest6Data.json"
                ).getPath(), ScenarioIterationRequest.class);

        ResponseWrapper<CssComponentResponse> scenarioIterationRespond = null;
        try {
            scenarioIterationRespond =
                    scenarioIterationService.getScenarioIterationWithParamsPost(scenarioIterationRequest);
        } finally {
            ResponseWrapper<CssComponentResponse> finalScenarioIterationRespond = scenarioIterationRespond;
            assertDoesNotThrow(
                () -> {
                    finalScenarioIterationRespond.getResponseEntity();
                });
        }
    }
}
