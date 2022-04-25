package com.apriori.tests.newendpoint;

import com.apriori.css.entity.apicalls.ScenarioIterationService;
import com.apriori.css.entity.request.ScenarioIterationRequest;
import com.apriori.css.entity.response.CssComponentResponse;
import com.apriori.css.entity.response.ScenarioItem;
import com.apriori.utils.http.utils.ResponseWrapper;
import io.qameta.allure.Description;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class VerifyIfNotFailTests {
    private static ScenarioIterationService scenarioIterationService = new ScenarioIterationService();

    @BeforeClass
    public static void testSetup() {
        scenarioIterationService.loadDataIfNotExists();
    }

    @Test
    @Description("Verify that POST scenario-iterations returns exactly one specified part - condition EQ (equals)")
    public void getOnePartTest() {

        ScenarioIterationRequest scenarioIterationRequest = ScenarioIterationRequest.builder()
                .query(ScenarioIterationRequest.Query.builder()
                        .filter(ScenarioIterationRequest.Query.LogicalOperator.builder()
                                .and(Arrays.asList(ScenarioIterationRequest.Query.LogicalOperator.Operator.builder()
                                        .equals(ScenarioIterationRequest.Query.LogicalOperator.Params.builder()
                                                .property("componentName")
                                                .value("bracket_basic")
                                                .build())
                                        .build()))
                                .build())
                        .build())
                .build();

        ResponseWrapper<CssComponentResponse> scenarioIterationRespond =
                scenarioIterationService.getScenarioIterationWithParamsNew(scenarioIterationRequest);

        ScenarioItem scenarioItem = scenarioIterationRespond.getResponseEntity().getItems().stream()
                .findFirst().orElse(null);

        assertEquals("BRACKET_BASIC",scenarioItem.getComponentDisplayName());
        assertEquals("bracket_basic.prt",scenarioItem.getComponentFilename());
    }

    @Test
    @Description("Verify that POST scenario-iterations do not fail on dot notation")
    public void verifyIfNotFailOnDotNotation() {

        ScenarioIterationRequest scenarioIterationRequest = ScenarioIterationRequest.builder()
                .query(ScenarioIterationRequest.Query.builder()
                        .filter(ScenarioIterationRequest.Query.LogicalOperator.builder()
                                .and(Arrays.asList(ScenarioIterationRequest.Query.LogicalOperator.Operator.builder()
                                        .equals(ScenarioIterationRequest.Query.LogicalOperator.Params.builder()
                                                .property("scenarioIterationKey.workspaceId")
                                                .value("0")
                                                .build())
                                        .build()))
                                .build())
                        .build())
                .build();
        ResponseWrapper<CssComponentResponse> scenarioIterationRespond = null;

        try {
            scenarioIterationRespond =
                    scenarioIterationService.getScenarioIterationWithParamsNew(scenarioIterationRequest);
        } finally {
            ResponseWrapper<CssComponentResponse> finalScenarioIterationRespond = scenarioIterationRespond;
            assertDoesNotThrow(
                    () -> {
                        finalScenarioIterationRespond.getResponseEntity();
                    });
        }
    }

    @Test
    @Description("Verify if convert string value to java enum type of thumbnail.imageType")
    public void verifyIfConvertStringIntoEnum() {

        ScenarioIterationRequest scenarioIterationRequest = ScenarioIterationRequest.builder()
                .query(ScenarioIterationRequest.Query.builder()
                        .filter(ScenarioIterationRequest.Query.LogicalOperator.builder()
                                .and(Arrays.asList(ScenarioIterationRequest.Query.LogicalOperator.Operator.builder()
                                        .starts(ScenarioIterationRequest.Query.LogicalOperator.Params.builder()
                                                .property("thumbnail.imageType")
                                                .value("THUMBNA")
                                                .build())
                                        .build()))
                                .build())
                        .build())
                .build();
        ResponseWrapper<CssComponentResponse> scenarioIterationRespond = null;

        try {
            scenarioIterationRespond =
                    scenarioIterationService.getScenarioIterationWithParamsNew(scenarioIterationRequest);
        } finally {
            ResponseWrapper<CssComponentResponse> finalScenarioIterationRespond = scenarioIterationRespond;
            assertDoesNotThrow(
                    () -> {
                        finalScenarioIterationRespond.getResponseEntity();
                    });
        }
    }

    @Test
    @Description("Verify items found searching by Date value")
    public void verifyIfItemsFoundByDateValue() {

        ScenarioIterationRequest scenarioIterationRequest = ScenarioIterationRequest.builder()
                .query(ScenarioIterationRequest.Query.builder()
                        .filter(ScenarioIterationRequest.Query.LogicalOperator.builder()
                                .and(Arrays.asList(ScenarioIterationRequest.Query.LogicalOperator.Operator.builder()
                                        .equals(ScenarioIterationRequest.Query.LogicalOperator.Params.builder()
                                                .property("componentCreatedAt")
                                                .value("2022-04-07T08:33:26.644Z")
                                                .build())
                                        .build()))
                                .build())
                        .build())
                .build();
        ResponseWrapper<CssComponentResponse> scenarioIterationRespond = null;

        try {
            scenarioIterationRespond =
                    scenarioIterationService.getScenarioIterationWithParamsNew(scenarioIterationRequest);
        } finally {
            ResponseWrapper<CssComponentResponse> finalScenarioIterationRespond = scenarioIterationRespond;
            assertDoesNotThrow(
                    () -> {
                        finalScenarioIterationRespond.getResponseEntity();
                    });
        }
    }

    @Test
    @Description("verify if we have compliance check of value types for 'in' operator")
    public void verifyIfThereIsComplianceCheckForInOperator() {

        ScenarioIterationRequest scenarioIterationRequest = ScenarioIterationRequest.builder()
                .query(ScenarioIterationRequest.Query.builder()
                        .filter(ScenarioIterationRequest.Query.LogicalOperator.builder()
                                .and(Arrays.asList(ScenarioIterationRequest.Query.LogicalOperator.Operator.builder()
                                        .in(ScenarioIterationRequest.Query.LogicalOperator.Params.builder()
                                                .property("iteration")
                                                .values(Arrays.asList("1","2","3"))
                                                .build())
                                        .build()))
                                .build())
                        .build())
                .build();
        ResponseWrapper<CssComponentResponse> scenarioIterationRespond = null;

        try {
            scenarioIterationRespond =
                    scenarioIterationService.getScenarioIterationWithParamsNew(scenarioIterationRequest);
        } finally {
            ResponseWrapper<CssComponentResponse> finalScenarioIterationRespond = scenarioIterationRespond;
            assertDoesNotThrow(
                    () -> {
                        finalScenarioIterationRespond.getResponseEntity();
                    });
        }
    }

    @Test
    @Description("verify that Null value for any predicate operator does not leads to 500 error")
    public void verifyThatNullValueForAnyOperatorDoesNotLeadToError() {

        ScenarioIterationRequest scenarioIterationRequest = ScenarioIterationRequest.builder()
                .query(ScenarioIterationRequest.Query.builder()
                        .filter(ScenarioIterationRequest.Query.LogicalOperator.builder()
                                .and(Arrays.asList(ScenarioIterationRequest.Query.LogicalOperator.Operator.builder()
                                        .starts(ScenarioIterationRequest.Query.LogicalOperator.Params.builder()
                                                .property("componentName")
                                                .value(null)
                                                .build())
                                        .build()))
                                .build())
                        .build())
                .build();
        ResponseWrapper<CssComponentResponse> scenarioIterationRespond = null;

        try {
            scenarioIterationRespond =
                    scenarioIterationService.getScenarioIterationWithParamsNew(scenarioIterationRequest);
        } finally {
            ResponseWrapper<CssComponentResponse> finalScenarioIterationRespond = scenarioIterationRespond;
            assertDoesNotThrow(
                    () -> {
                        finalScenarioIterationRespond.getResponseEntity();
                    });
        }
    }

    @Test
    @Description("verify that between operator with different value types should not fail")
    public void verifyBetweenOperatorWithDiffValuesTypesNotFail() {

        ScenarioIterationRequest scenarioIterationRequest = ScenarioIterationRequest.builder()
                .query(ScenarioIterationRequest.Query.builder()
                        .filter(ScenarioIterationRequest.Query.LogicalOperator.builder()
                                .and(Arrays.asList(ScenarioIterationRequest.Query.LogicalOperator.Operator.builder()
                                        .between(ScenarioIterationRequest.Query.LogicalOperator.Params.builder()
                                                .property("iteration")
                                                .min(1)
                                                .max("2")
                                                .build())
                                        .build()))
                                .build())
                        .build())
                .build();
        ResponseWrapper<CssComponentResponse> scenarioIterationRespond = null;

        try {
            scenarioIterationRespond =
                    scenarioIterationService.getScenarioIterationWithParamsNew(scenarioIterationRequest);
        } finally {
            ResponseWrapper<CssComponentResponse> finalScenarioIterationRespond = scenarioIterationRespond;
            assertDoesNotThrow(
                    () -> {
                        finalScenarioIterationRespond.getResponseEntity();
                    });
        }
    }
}
