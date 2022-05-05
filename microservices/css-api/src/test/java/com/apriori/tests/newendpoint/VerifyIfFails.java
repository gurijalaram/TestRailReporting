package com.apriori.tests.newendpoint;

import static org.junit.Assert.assertEquals;

import com.apriori.css.entity.apicalls.ScenarioIterationService;
import com.apriori.css.entity.request.ErrorRequestResponse;
import com.apriori.css.entity.request.LogicalOperator;
import com.apriori.css.entity.request.Operator;
import com.apriori.css.entity.request.Query;
import com.apriori.css.entity.request.ScenarioIterationRequest;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import org.junit.BeforeClass;
import org.junit.Test;

public class VerifyIfFails {

    private static ScenarioIterationService scenarioIterationService = new ScenarioIterationService();

    @BeforeClass
    public static void testSetup() {
        scenarioIterationService.loadDataIfNotExists();
    }

    @Test
    @TestRail(testCaseId = {"12420"})
    @Description("Verify: if fails when using in query non existing operator: 'wrongOperator'")
    public void shouldFailWithNonExistingOperatorTest() {
        ScenarioIterationRequest scenarioIterationRequest  =
            (ScenarioIterationRequest) JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "VerifyIfFailsTest1Data.json"
                ).getPath(), ScenarioIterationRequest.class);

        ResponseWrapper<ErrorRequestResponse> scenarioIterationRespond =
            scenarioIterationService.getScenarioIterationWithParamsPostForErrors(scenarioIterationRequest);

        assertEquals("400",scenarioIterationRespond.getResponseEntity().getStatus());
        assertEquals("Bad Request",scenarioIterationRespond.getResponseEntity().getError());
        assertEquals("Unknown operator wrongOperator",scenarioIterationRespond.getResponseEntity().getMessage());
    }

    @Test
    @TestRail(testCaseId = {"12421"})
    @Description("Verify: if fails when using in query non invalid operator: (removed property)")
    public void shouldFailWithInvalidOperatorTest() {
        ScenarioIterationRequest scenarioIterationRequest  =
            (ScenarioIterationRequest) JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "VerifyIfFailsTest2Data.json"
                ).getPath(), ScenarioIterationRequest.class);

        ResponseWrapper<ErrorRequestResponse> scenarioIterationRespond =
            scenarioIterationService.getScenarioIterationWithParamsPostForErrors(scenarioIterationRequest);

        assertEquals("400",scenarioIterationRespond.getResponseEntity().getStatus());
        assertEquals("Bad Request",scenarioIterationRespond.getResponseEntity().getError());
        assertEquals("Unknown operator wrongOperator",scenarioIterationRespond.getResponseEntity().getMessage());
    }

    @Test
    @TestRail(testCaseId = {"12422"})
    @Description("Verify: if fails when using in query non parsable operator")
    public void shouldFailNotParsableOperatorTest() {
        ScenarioIterationRequest scenarioIterationRequest  = setWithEmptyBracketNotOperator();

        ResponseWrapper<ErrorRequestResponse> scenarioIterationRespond =
            scenarioIterationService.getScenarioIterationWithParamsPostForErrors(scenarioIterationRequest);

        assertEquals("400",scenarioIterationRespond.getResponseEntity().getStatus());
        assertEquals("Bad Request",scenarioIterationRespond.getResponseEntity().getError());
        assertEquals("Unable to extract next operator from {}",scenarioIterationRespond.getResponseEntity().getMessage());
    }

    @Test
    @TestRail(testCaseId = {"12423"})
    @Description("Verify: if fails when using invalid value expected: double, used: string")
    public void shouldFailWithInvalidValueTest() {
        ScenarioIterationRequest scenarioIterationRequest  =
            (ScenarioIterationRequest) JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "VerifyIfFailsTest3Data.json"
                ).getPath(), ScenarioIterationRequest.class);

        ResponseWrapper<ErrorRequestResponse> scenarioIterationRespond =
            scenarioIterationService.getScenarioIterationWithParamsPostForErrors(scenarioIterationRequest);

        assertEquals("400",scenarioIterationRespond.getResponseEntity().getStatus());
        assertEquals("Bad Request",scenarioIterationRespond.getResponseEntity().getError());
        assertEquals("Invalid value was provided for operator equals. Please, refer to the Swagger documentation for an example",
            scenarioIterationRespond.getResponseEntity().getMessage());
    }

    @Test
    @TestRail(testCaseId = {"12424"})
    @Description("Verify: if fails when using invalid value for Sorting Direction enum")
    public void shouldFailWithWrongSortingDirectionValueTest() {
        ScenarioIterationRequest scenarioIterationRequest  =
            (ScenarioIterationRequest) JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "VerifyIfFailsTest4Data.json"
                ).getPath(), ScenarioIterationRequest.class);

        ResponseWrapper<ErrorRequestResponse> scenarioIterationRespond =
            scenarioIterationService.getScenarioIterationWithParamsPostForErrors(scenarioIterationRequest);

        assertEquals("400",scenarioIterationRespond.getResponseEntity().getStatus());
        assertEquals("Bad Request",scenarioIterationRespond.getResponseEntity().getError());
        assertEquals("WRONG doesn't fit the required type. Applicable values are: [ASC, DESC]",
            scenarioIterationRespond.getResponseEntity().getMessage());
    }

    @Test
    @TestRail(testCaseId = {"12425"})
    @Description("Verify: if fails when using Sorting with missing property")
    public void shouldFailWithMissingPropertyInDirectionTest() {
        ScenarioIterationRequest scenarioIterationRequest  =
            (ScenarioIterationRequest) JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "VerifyIfFailsTest5Data.json"
                ).getPath(), ScenarioIterationRequest.class);

        ResponseWrapper<ErrorRequestResponse> scenarioIterationRespond =
            scenarioIterationService.getScenarioIterationWithParamsPostForErrors(scenarioIterationRequest);

        assertEquals("400",scenarioIterationRespond.getResponseEntity().getStatus());
        assertEquals("Bad Request",scenarioIterationRespond.getResponseEntity().getError());
        assertEquals("Validation errors: sorting[0].property must not be empty",
            scenarioIterationRespond.getResponseEntity().getMessage());
    }

    private ScenarioIterationRequest setWithEmptyBracketNotOperator() {
        Query query = new Query();
        Operator not = new Operator();
        LogicalOperator filter = new LogicalOperator();
        filter.setNot(not);
        query.setFilter(filter);
        ScenarioIterationRequest scenarioIterationRequest  = new ScenarioIterationRequest();
        scenarioIterationRequest.setQuery(query);
        return scenarioIterationRequest;
    }

}
