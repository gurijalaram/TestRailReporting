package com.apriori.tests.newendpoint;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.apriori.css.entity.apicalls.ScenarioIterationService;
import com.apriori.css.entity.request.ScenarioIterationRequest;
import com.apriori.css.entity.response.CostingInput;
import com.apriori.css.entity.response.CssComponentResponse;
import com.apriori.css.entity.response.ScenarioItem;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.json.utils.JsonManager;

import io.qameta.allure.Description;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

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
                "ScenarioIterationPostTest1Data.json"
            ).getPath(), ScenarioIterationRequest.class);

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
                    "ScenarioIterationPostTest2Data.json"
                ).getPath(), ScenarioIterationRequest.class);

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
                    "ScenarioIterationPostTest3Data.json"
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
                    "ScenarioIterationPostTest4Data.json"
                ).getPath(), ScenarioIterationRequest.class);

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
                    "ScenarioIterationPostTest5Data.json"
                ).getPath(), ScenarioIterationRequest.class);

        ResponseWrapper<CssComponentResponse> scenarioIterationRespondComponentNames =
                scenarioIterationService.getScenarioIterationWithParamsPost(scenarioIterationRequest);
        List<CostingInput> costingInput = scenarioIterationRespondComponentNames.getResponseEntity().getItems().stream()
                .map(ScenarioItem::getCostingInput).collect(Collectors.toList());

        costingInput.stream()
                .forEach(t -> assertNull(t));
    }
}
