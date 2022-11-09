package com.apriori.tests.newendpoint;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.apriori.entity.apicalls.ScenarioIterationService;
import com.apriori.entity.enums.Direction;
import com.apriori.entity.request.Params;
import com.apriori.entity.request.ScenarioIterationRequest;
import com.apriori.entity.response.CostingInput;
import com.apriori.entity.response.CssComponentResponse;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ScenarioIterationPostTests {
    private static ScenarioIterationService scenarioIterationService = new ScenarioIterationService();

    @BeforeClass
    public static void testSetup() {
        scenarioIterationService.loadDataIfNotExists();
    }

    @Test
    @TestRail(testCaseId = {"12415"})
    @Description("Verify that following query: componentName[EQ]=foo, returns correct results")
    public void getOnePartTest() {
        ScenarioIterationRequest scenarioIterationRequest  =
            (ScenarioIterationRequest) JsonManager.deserializeJsonFromFile(
            FileResourceUtil.getResourceAsFile(
                "AndOperatorData.json"
            ).getPath(), ScenarioIterationRequest.class);

        Params params = new Params();
        params.setValue("bracket_basic");
        params.setProperty("componentName");
        scenarioIterationRequest.getQuery().getFilter().getAnd().get(0).setEquals(params);

        ResponseWrapper<CssComponentResponse> scenarioIterationRespond =
                scenarioIterationService.getScenarioIterationWithParamsPost(scenarioIterationRequest);

        ScenarioItem scenarioItem = scenarioIterationRespond.getResponseEntity().getItems().stream()
                .findFirst().orElse(null);

        assertEquals("BRACKET_BASIC",scenarioItem.getComponentDisplayName());
        assertEquals("bracket_basic.prt",scenarioItem.getComponentFilename());
    }

    @Test
    @TestRail(testCaseId = {"12416"})
    @Description("Verify that POST scenario-iterations returns exactly specified 5 items - paging , sorting Asc")
    public void getFivePartsSortedAscTest() {
        ScenarioIterationRequest scenarioIterationRequest  =
            (ScenarioIterationRequest) JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "PagingAndSortingData.json"
                ).getPath(), ScenarioIterationRequest.class);

        scenarioIterationRequest.getSorting().get(0).setProperty("componentName");
        scenarioIterationRequest.getSorting().get(0).setDirection(Direction.ASC);

        ResponseWrapper<CssComponentResponse> scenarioIterationRespond =
                scenarioIterationService.getScenarioIterationWithParamsPost(scenarioIterationRequest);

        assertEquals(5,scenarioIterationRespond.getResponseEntity().getItems().size());
    }

    @Test
    @TestRail(testCaseId = {"12417"})
    @Description("Verify that POST scenario-iterations returns parts with OR operator")
    public void getPartsWithOrOperatorTest() {
        ScenarioIterationRequest scenarioIterationRequest  =
            (ScenarioIterationRequest) JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "OrOperatorWithThreeEquals.json"
                ).getPath(), ScenarioIterationRequest.class);

        ResponseWrapper<CssComponentResponse> scenarioIterationRespondComponentNames =
                scenarioIterationService.getScenarioIterationWithParamsPost(scenarioIterationRequest);
        List<String> items = scenarioIterationRespondComponentNames.getResponseEntity().getItems().stream()
                        .map(ScenarioItem::getComponentName).collect(Collectors.toList());
        assertThat(items)
                .contains("bracket_basic","BasicScenario_Forging","oldham");
    }

    @Test
    @TestRail(testCaseId = {"12418"})
    @Description("Verify that POST scenario-iterations returns parts - NOT operator")
    public void getPartsWithNotOperatorTest() {
        ScenarioIterationRequest scenarioIterationRequest  =
            (ScenarioIterationRequest) JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "NotOperatorData.json"
                ).getPath(), ScenarioIterationRequest.class);

        Params params = new Params();
        params.setValue("bracket_basic");
        params.setProperty("componentName");
        scenarioIterationRequest.getQuery().getFilter().getNot().setEquals(params);


        ResponseWrapper<CssComponentResponse> scenarioIterationRespondComponentNames =
                scenarioIterationService.getScenarioIterationWithParamsPost(scenarioIterationRequest);
        List<String> items = scenarioIterationRespondComponentNames.getResponseEntity().getItems().stream()
                .map(ScenarioItem::getComponentName).collect(Collectors.toList());

        assertThat(items)
                .doesNotContain("bracket_basic");
    }

    @Test
    @TestRail(testCaseId = {"12419"})
    @Description("Verify that POST scenario-iterations returns parts - isNull operator")
    public void getPartsWithIsNullOperatorTest() {
        ScenarioIterationRequest scenarioIterationRequest  =
            (ScenarioIterationRequest) JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "IsNullOperatorData.json"
                ).getPath(), ScenarioIterationRequest.class);

        Params params = new Params();
        params.setProperty("componentName");
        scenarioIterationRequest.getQuery().getFilter().getAnd().get(0).setIsNull(params);

        ResponseWrapper<CssComponentResponse> scenarioIterationRespondComponentNames =
                scenarioIterationService.getScenarioIterationWithParamsPost(scenarioIterationRequest);
        List<CostingInput> costingInput = scenarioIterationRespondComponentNames.getResponseEntity().getItems().stream()
                .map(ScenarioItem::getCostingInput).collect(Collectors.toList());

        costingInput.stream()
                .forEach(t -> assertNull(t));
    }

    @Test
    @TestRail(testCaseId = {"12895"})
    @Description("Verify following query: (componentName[CN]=foo AND createdBy[IN]=currentUser|anotherUser) OR (assignedTo[EQ]=currentUser AND isPublic[EQ]=true)")
    public void getDataWithMoreComplexQueryTest() {
        ScenarioIterationRequest scenarioIterationRequest  =
            (ScenarioIterationRequest) JsonManager.deserializeJsonFromFile(
                FileResourceUtil.getResourceAsFile(
                    "MoreComplexQueryData.json"
                ).getPath(), ScenarioIterationRequest.class);

        ResponseWrapper<CssComponentResponse> scenarioIterationRespondComponentNames =
            scenarioIterationService.getScenarioIterationWithParamsPost(scenarioIterationRequest);
        List<ScenarioItem> items = scenarioIterationRespondComponentNames.getResponseEntity().getItems();

        assertTrue(verifyIfCorrect(items));
    }

    private boolean verifyIfCorrect(List<ScenarioItem> items) {
        List<String> lastAction = items.stream().map(ScenarioItem::getLastAction).collect(Collectors.toList());
        List<String> scenarioState = items.stream().map(ScenarioItem::getScenarioState).collect(Collectors.toList());
        List<String> componentType = items.stream().map(ScenarioItem::getComponentName).collect(Collectors.toList());
        List<String> scenarioType = items.stream().map(ScenarioItem::getScenarioType).collect(Collectors.toList());

        if (ifNotReturnsDifferentValue(lastAction,"CREATE") && ifNotReturnsDifferentValues(scenarioState,"PROCESSING_FAILED","NOT_COSTED")) {
            return true;
        }

        if (ifNotReturnsDifferentValue(componentType,"UNKNOWN") && ifNotReturnsDifferentValue(scenarioType,"VERIFIED")) {
            return true;
        }
        log.error("Compared values does not match expected results");
        return false;
    }

    private boolean ifNotReturnsDifferentValue(List<String> values,String value) {
        if (values.stream().allMatch(i -> i.equals(value))) {
            return true;
        }
        return false;
    }

    private boolean ifNotReturnsDifferentValues(List<String> values, String valueOne,String valueTwo) {
        if (values.stream().allMatch(i -> (i.equals(valueOne) || i.equals(valueTwo)))) {
            return true;
        }
        return false;
    }
}
