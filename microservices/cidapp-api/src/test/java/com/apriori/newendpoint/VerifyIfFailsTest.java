package com.apriori.newendpoint;

import static org.junit.Assert.assertEquals;

import com.apriori.FileResourceUtil;
import com.apriori.cidappapi.entity.enums.Direction;
import com.apriori.cidappapi.entity.request.ScenarioIterationRequest;
import com.apriori.cidappapi.entity.request.operators.LogicalOperator;
import com.apriori.cidappapi.entity.request.operators.Operator;
import com.apriori.cidappapi.entity.request.operators.Params;
import com.apriori.cidappapi.entity.request.operators.Query;
import com.apriori.cidappapi.utils.ScenarioIterationService;
import com.apriori.entity.request.ErrorRequestResponse;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.json.JsonManager;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.BeforeClass;
import org.junit.Test;

public class VerifyIfFailsTest {

    private static ScenarioIterationService scenarioIterationService = new ScenarioIterationService();

    @BeforeClass
    public static void testSetup() {
        scenarioIterationService.loadDataIfNotExists();
    }

    @Test
    @TestRail(id = {12420})
    @Description("Verify: if fails when using in query non existing operator: 'wrongOperator'")
    public void shouldFailWithNonExistingOperatorTest() {
        ScenarioIterationRequest scenarioIterationRequest =
            JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "WrongOperatorData.json").getPath(), ScenarioIterationRequest.class);

        Params params = new Params();
        params.setProperty("componentName");
        params.setValue("bracket_basic");
        scenarioIterationRequest.getQuery().getFilter().getWrongOperator().get(0).setEquals(params);

        ResponseWrapper<ErrorRequestResponse> scenarioIterationRespond =
            scenarioIterationService.getScenarioIterationWithParamsPostForErrors(scenarioIterationRequest);

        assertEquals("400", scenarioIterationRespond.getResponseEntity().getStatus());
        assertEquals("Bad Request", scenarioIterationRespond.getResponseEntity().getError());
        assertEquals("Unknown operator wrongOperator", scenarioIterationRespond.getResponseEntity().getMessage());
    }

    @Test
    @TestRail(id = {12421})
    @Description("Verify: if fails when using in query non invalid operator: (removed property)")
    public void shouldFailWithInvalidOperatorTest() {
        ScenarioIterationRequest scenarioIterationRequest =
            JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "AndOperatorData.json").getPath(), ScenarioIterationRequest.class);

        Params params = new Params();
        params.setValue("bracket_basic");
        scenarioIterationRequest.getQuery().getFilter().getAnd().get(0).setEquals(params);

        ResponseWrapper<ErrorRequestResponse> scenarioIterationRespond =
            scenarioIterationService.getScenarioIterationWithParamsPostForErrors(scenarioIterationRequest);

        assertEquals("400", scenarioIterationRespond.getResponseEntity().getStatus());
        assertEquals("Bad Request", scenarioIterationRespond.getResponseEntity().getError());
        assertEquals("Invalid value was provided for operator equals. Please, refer to the Swagger documentation for an example", scenarioIterationRespond.getResponseEntity().getMessage());
    }

    @Test
    @TestRail(id = {12422})
    @Description("Verify: if fails when using in query non parsable operator")
    public void shouldFailNotParsableOperatorTest() {
        ScenarioIterationRequest scenarioIterationRequest = setWithEmptyBracketNotOperator();

        ResponseWrapper<ErrorRequestResponse> scenarioIterationRespond =
            scenarioIterationService.getScenarioIterationWithParamsPostForErrors(scenarioIterationRequest);

        assertEquals("400", scenarioIterationRespond.getResponseEntity().getStatus());
        assertEquals("Bad Request", scenarioIterationRespond.getResponseEntity().getError());
        assertEquals("Unable to extract next operator from {}", scenarioIterationRespond.getResponseEntity().getMessage());
    }

    @Test
    @TestRail(id = {12423})
    @Description("Verify: if fails when using invalid value expected: double, used: string")
    public void shouldFailWithInvalidValueTest() {
        ScenarioIterationRequest scenarioIterationRequest =
            JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "AndOperatorData.json").getPath(), ScenarioIterationRequest.class);

        Params params = new Params();
        params.setValue("bracket_basic");
        params.setProperty("componentName");
        Operator operatorSecond = new Operator();
        operatorSecond.setEquals(new Params());

        scenarioIterationRequest.getQuery().getFilter().getAnd().get(0).setEquals(params);
        scenarioIterationRequest.getQuery().getFilter().getAnd().add(operatorSecond);

        ResponseWrapper<ErrorRequestResponse> scenarioIterationRespond =
            scenarioIterationService.getScenarioIterationWithParamsPostForErrors(scenarioIterationRequest);

        assertEquals("400", scenarioIterationRespond.getResponseEntity().getStatus());
        assertEquals("Bad Request", scenarioIterationRespond.getResponseEntity().getError());
        assertEquals("Invalid value was provided for operator equals. Please, refer to the Swagger documentation for an example",
            scenarioIterationRespond.getResponseEntity().getMessage());
    }

    @Test
    @TestRail(id = {12424})
    @Description("Verify: if fails when using invalid value for Sorting Direction enum")
    public void shouldFailWithWrongSortingDirectionValueTest() {
        ScenarioIterationRequest scenarioIterationRequest =
            JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "PagingAndSortingData.json").getPath(), ScenarioIterationRequest.class);

        scenarioIterationRequest.getSorting().get(0).setProperty("componentName");
        scenarioIterationRequest.getSorting().get(0).setDirection(Direction.WRONG);

        ResponseWrapper<ErrorRequestResponse> scenarioIterationRespond =
            scenarioIterationService.getScenarioIterationWithParamsPostForErrors(scenarioIterationRequest);

        assertEquals("400", scenarioIterationRespond.getResponseEntity().getStatus());
        assertEquals("Bad Request", scenarioIterationRespond.getResponseEntity().getError());
        assertEquals("WRONG doesn't fit the required type. Applicable values are: [ASC, DESC]",
            scenarioIterationRespond.getResponseEntity().getMessage());
    }

    @Test
    @TestRail(id = {12425})
    @Description("Verify: if fails when using Sorting with missing property")
    public void shouldFailWithMissingPropertyInDirectionTest() {
        ScenarioIterationRequest scenarioIterationRequest =
            JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "PagingAndSortingData.json").getPath(), ScenarioIterationRequest.class);

        scenarioIterationRequest.getSorting().get(0).setDirection(Direction.ASC);

        ResponseWrapper<ErrorRequestResponse> scenarioIterationRespond =
            scenarioIterationService.getScenarioIterationWithParamsPostForErrors(scenarioIterationRequest);

        assertEquals("400", scenarioIterationRespond.getResponseEntity().getStatus());
        assertEquals("Bad Request", scenarioIterationRespond.getResponseEntity().getError());
        assertEquals("Validation errors: sorting[0].property must not be empty",
            scenarioIterationRespond.getResponseEntity().getMessage());
    }

    @Test
    @TestRail(id = {12482})
    @Description("Verify: query fail when five operators are aggregated with one logical operator")
    public void shouldFailWithFiveOperatorsTest() {
        ScenarioIterationRequest scenarioIterationRequest =
            JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "FiveOperatorsOverLimitQueryData.json").getPath(), ScenarioIterationRequest.class);

        ResponseWrapper<ErrorRequestResponse> scenarioIterationRespond =
            scenarioIterationService.getScenarioIterationWithParamsPostForErrors(scenarioIterationRequest);

        assertEquals("400", scenarioIterationRespond.getResponseEntity().getStatus());
        assertEquals("Bad Request", scenarioIterationRespond.getResponseEntity().getError());
        assertEquals("Logical operator cannot contain more than 5 operators",
            scenarioIterationRespond.getResponseEntity().getMessage());
    }

    @Test
    @TestRail(id = {12483})
    @Description("Verify: query fail when query depth is used")
    public void shouldFailWithFourthQueryDepthTest() {
        ScenarioIterationRequest scenarioIterationRequest =
            JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "FourthDepthQueryData.json").getPath(), ScenarioIterationRequest.class);


        ResponseWrapper<ErrorRequestResponse> scenarioIterationRespond =
            scenarioIterationService.getScenarioIterationWithParamsPostForErrors(scenarioIterationRequest);

        assertEquals("400", scenarioIterationRespond.getResponseEntity().getStatus());
        assertEquals("Bad Request", scenarioIterationRespond.getResponseEntity().getError());
        assertEquals("The depth of operators cannot be more than 3",
            scenarioIterationRespond.getResponseEntity().getMessage());
    }

    private ScenarioIterationRequest setWithEmptyBracketNotOperator() {
        Query query = new Query();
        Operator not = new Operator();
        LogicalOperator filter = new LogicalOperator();
        filter.setNot(not);
        query.setFilter(filter);
        ScenarioIterationRequest scenarioIterationRequest = new ScenarioIterationRequest();
        scenarioIterationRequest.setQuery(query);
        return scenarioIterationRequest;
    }
}
