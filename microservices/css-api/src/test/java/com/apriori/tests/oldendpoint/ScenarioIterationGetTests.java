package com.apriori.tests.oldendpoint;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.apriori.css.entity.apicalls.ScenarioIterationService;
import com.apriori.css.entity.response.CssComponentResponse;
import com.apriori.css.entity.response.ScenarioItem;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ScenarioIterationGetTests {
    private static ScenarioIterationService scenarioIterationService = new ScenarioIterationService();

    @BeforeClass
    public static void testSetup() {
        scenarioIterationService.loadDataIfNotExists();
    }

    @Test
    @Description("Verify that GET scenario-iterations returns exactly specified 5 items - paging")
    public void getFivePartsTest() {

        Map paramsToUrl = new HashMap();
        paramsToUrl.put("pageNumber","1");
        paramsToUrl.put("pageSize","5");

        ResponseWrapper<CssComponentResponse> scenarioIterationRespond =
            scenarioIterationService.getScenarioIterationWithParams(Arrays.asList(paramsToUrl));

        assertEquals(5,scenarioIterationRespond.getResponseEntity().getItems().size());
    }

    @Test
    @Description("Verify that GET scenario-iterations returns exactly one specified part - condition EQ (equals)")
    public void getOnePartTest() {

        Map paramsToUrl = new HashMap();
        paramsToUrl.put("componentName[EQ]","bracket_basic");

        ResponseWrapper<CssComponentResponse> scenarioIterationRespond =
                scenarioIterationService.getScenarioIterationWithParams(Arrays.asList(paramsToUrl));

        ScenarioItem scenarioItem = scenarioIterationRespond.getResponseEntity().getItems().stream()
                        .findFirst().orElse(null);

        assertEquals("BRACKET_BASIC",scenarioItem.getComponentDisplayName());
        assertEquals("bracket_basic.prt",scenarioItem.getComponentFilename());
    }

    @Test
    @Description("Verify that GET scenario-iterations returns exactly one specified part - condition NE (not equals)")
    public void notGetOnePartTest() {

        Map paramsToUrl = new HashMap();
        paramsToUrl.put("componentName[NE]","bracket_basic");

        ResponseWrapper<CssComponentResponse> scenarioIterationRespond =
                scenarioIterationService.getScenarioIterationWithParams(Arrays.asList(paramsToUrl));

        assertEquals(null,scenarioIterationRespond.getResponseEntity().getItems().stream()
                .filter(p -> p.getComponentDisplayName().equals("BRACKET_BASIC"))
                .findFirst()
                .orElse(null));
    }

    @Test
    @Description("Verify that GET scenario-iterations returns exactly specified parts in the range - condition IN")
    public void getPartsInRangeTest() {

        Map paramsToUrl = new HashMap();
        paramsToUrl.put("componentType[IN]","PART|ASSEMBLY");

        ResponseWrapper<CssComponentResponse> scenarioIterationRespond =
                scenarioIterationService.getScenarioIterationWithParams(Arrays.asList(paramsToUrl));

        assertTrue(scenarioIterationService.validateIfTrue(scenarioIterationRespond));
    }

    @Test
    @Description("Verify that GET scenario-iterations do not return exactly specified parts in the range - condition IN")
    public void notGetPartsInRangeTest() {

        Map paramsToUrl = new HashMap();
        paramsToUrl.put("componentType[NI]","PART|ASSEMBLY");

        ResponseWrapper<CssComponentResponse> scenarioIterationRespond =
                scenarioIterationService.getScenarioIterationWithParams(Arrays.asList(paramsToUrl));

        assertFalse(scenarioIterationService.validateIfTrue(scenarioIterationRespond));
    }

    @Test
    @Description("Verify that GET scenario-iterations return correct parts when using  AND operator")
    public void getPartsTwoParamsWithAndTest() {

        Map paramsToUrl = new HashMap();
        paramsToUrl.put("componentType[EQ]","PART");
        paramsToUrl.put("componentName[EQ]","bracket_basic");

        ResponseWrapper<CssComponentResponse> scenarioIterationRespond =
                scenarioIterationService.getScenarioIterationWithParams(Arrays.asList(paramsToUrl));

        assertTrue(scenarioIterationService.validateIfTrueWithAndOperator(scenarioIterationRespond));
    }

    @Test
    @Description("Verify that GET scenario-iterations return correct parts when using  SW(starts with / LIKE) operator")
    public void getPartsMatchTest() {
        Map paramsToUrl = new HashMap();
        paramsToUrl.put("componentName[SW]","br");

        ResponseWrapper<CssComponentResponse> scenarioIterationRespond =
                scenarioIterationService.getScenarioIterationWithParams(Arrays.asList(paramsToUrl));

        assertTrue(scenarioIterationService.validateIfTrueWithSwOperator(scenarioIterationRespond));
    }
}


