package com.apriori.tests.newendpoint;

import com.apriori.css.entity.apicalls.ScenarioIterationService;
import com.apriori.css.entity.enums.Direction;
import com.apriori.css.entity.request.ScenarioIterationRequest;
import com.apriori.css.entity.response.CostingInput;
import com.apriori.css.entity.response.CssComponentResponse;
import com.apriori.css.entity.response.ScenarioItem;
import com.apriori.utils.http.utils.ResponseWrapper;
import io.qameta.allure.Description;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import java.util.*;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ScenarioIterationPostTests {
    private static ScenarioIterationService scenarioIterationService = new ScenarioIterationService();

    @BeforeClass
    public static void testSetup() {
        scenarioIterationService.loadDataIfNotExists();
    }

    @Test
    @Description("Verify: componentName[EQ]=foo")
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
    @Description("Verify that POST scenario-iterations returns exactly specified 5 items - paging , sorting Asc")
    public void getFivePartsSortedAscTest() {

        ScenarioIterationRequest scenarioIterationRequest = ScenarioIterationRequest.builder()
                .paging(ScenarioIterationRequest.Paging.builder()
                        .pageNumber(1)
                        .pageSize(5)
                        .build())
                .sorting(Arrays.asList(ScenarioIterationRequest.Sorting.builder()
                                .property("componentName")
                                .direction(Direction.ASC)
                        .build()))
                .build();

        ResponseWrapper<CssComponentResponse> scenarioIterationRespond =
                scenarioIterationService.getScenarioIterationWithParamsNew(scenarioIterationRequest);

        Assertions.assertEquals(5,scenarioIterationRespond.getResponseEntity().getItems().size());
    }

    @Test
    @Description("Verify that POST scenario-iterations returns parts with OR operator")
    public void getPartsWithOrOperatorTest() {

        ScenarioIterationRequest scenarioIterationRequest = ScenarioIterationRequest.builder()
                .query(ScenarioIterationRequest.Query.builder()
                        .filter(ScenarioIterationRequest.Query.LogicalOperator.builder()
                                .or(Arrays.asList(ScenarioIterationRequest.Query.LogicalOperator.Operator.builder()
                                        .equals(ScenarioIterationRequest.Query.LogicalOperator.Params.builder()
                                                .property("componentName")
                                                .value("bracket_basic")
                                                .build())
                                        .build(),
                                        ScenarioIterationRequest.Query.LogicalOperator.Operator.builder()
                                                .equals(ScenarioIterationRequest.Query.LogicalOperator.Params.builder()
                                                        .property("componentName")
                                                        .value("BasicScenario_Forging")
                                                        .build())
                                                .build(),
                                        ScenarioIterationRequest.Query.LogicalOperator.Operator.builder()
                                                .equals(ScenarioIterationRequest.Query.LogicalOperator.Params.builder()
                                                        .property("componentName")
                                                        .value("oldham")
                                                        .build())
                                                .build()))
                                .build())
                        .build())
                .build();

        ResponseWrapper<CssComponentResponse> scenarioIterationRespondComponentNames =
                scenarioIterationService.getScenarioIterationWithParamsNew(scenarioIterationRequest);
        List<String> items = scenarioIterationRespondComponentNames.getResponseEntity().getItems().stream()
                        .map(ScenarioItem::getComponentName).collect(Collectors.toList());
        assertThat(items)
                .contains("bracket_basic","BasicScenario_Forging","oldham");
    }

    @Test
    @Description("Verify that POST scenario-iterations returns parts - NOT operator")
    public void getPartsWithNotOperatorTest() {

        ScenarioIterationRequest scenarioIterationRequest = ScenarioIterationRequest.builder()
                .query(ScenarioIterationRequest.Query.builder()
                        .filter(ScenarioIterationRequest.Query.LogicalOperator.builder()
                                .not(ScenarioIterationRequest.Query.LogicalOperator.Operator.builder()
                                        .equals(ScenarioIterationRequest.Query.LogicalOperator.Params.builder()
                                                .property("componentName")
                                                .value("bracket_basic")
                                                .build())
                                        .build())
                                .build())
                        .build())
                .build();

        ResponseWrapper<CssComponentResponse> scenarioIterationRespondComponentNames =
                scenarioIterationService.getScenarioIterationWithParamsNew(scenarioIterationRequest);
        List<String> items = scenarioIterationRespondComponentNames.getResponseEntity().getItems().stream()
                .map(ScenarioItem::getComponentName).collect(Collectors.toList());

        assertThat(items)
                .doesNotContain("bracket_basic");
    }

    @Test
    @Description("Verify that POST scenario-iterations returns parts - isNull operator")
    public void getPartsWithIsNullOperatorTest() {

        ScenarioIterationRequest scenarioIterationRequest = ScenarioIterationRequest.builder()
                .query(ScenarioIterationRequest.Query.builder()
                        .filter(ScenarioIterationRequest.Query.LogicalOperator.builder()
                                .and(Arrays.asList(ScenarioIterationRequest.Query.LogicalOperator.Operator.builder()
                                        .isNull(ScenarioIterationRequest.Query.LogicalOperator.Params.builder()
                                                .property("costingInput")
                                                .build())
                                        .build()))
                                .build())
                        .build())
                .build();

        ResponseWrapper<CssComponentResponse> scenarioIterationRespondComponentNames =
                scenarioIterationService.getScenarioIterationWithParamsNew(scenarioIterationRequest);
        List<CostingInput> costingInput = scenarioIterationRespondComponentNames.getResponseEntity().getItems().stream()
                .map(ScenarioItem::getCostingInput).collect(Collectors.toList());

        costingInput.stream()
                .forEach(t -> assertNull(t));
    }
}
