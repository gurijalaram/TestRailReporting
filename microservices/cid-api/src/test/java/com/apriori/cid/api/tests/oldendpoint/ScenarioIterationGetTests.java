package com.apriori.cid.api.tests.oldendpoint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apriori.cid.api.utils.ScenarioIterationService;
import com.apriori.shared.util.http.utils.QueryParams;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.component.ComponentResponse;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class ScenarioIterationGetTests {
    private static ScenarioIterationService scenarioIterationService = new ScenarioIterationService();

    @BeforeAll
    public static void testSetup() {
        scenarioIterationService.loadDataIfNotExists();
    }

    @Test
    @TestRail(id = {12438})
    @Description("Verify that GET scenario-iterations returns exactly specified 5 items - paging")
    public void getFivePartsTest() {
        QueryParams queryParams = new QueryParams();
        queryParams.use("pageNumber", "1");
        queryParams.use("pageSize", "5");

        ResponseWrapper<ComponentResponse> scenarioIterationRespond =
            scenarioIterationService.getScenarioIterationWithParams(queryParams);

        assertEquals(5, scenarioIterationRespond.getResponseEntity().getItems().size());
    }

    @Test
    @TestRail(id = {12439})
    @Description("Verify that GET scenario-iterations returns exactly one specified part - condition EQ (equals)")
    public void getOnePartTest() {
        QueryParams queryParams = new QueryParams();
        queryParams.use("componentName[EQ]", "bracket_basic");

        ResponseWrapper<ComponentResponse> scenarioIterationRespond =
            scenarioIterationService.getScenarioIterationWithParams(queryParams);

        ScenarioItem scenarioItem = scenarioIterationRespond.getResponseEntity().getItems().stream()
            .findFirst().orElse(null);

        assertEquals("BRACKET_BASIC", scenarioItem.getComponentDisplayName());
        assertEquals("bracket_basic.prt", scenarioItem.getComponentFilename());
    }

    @Test
    @TestRail(id = {12440})
    @Description("Verify that GET scenario-iterations returns exactly one specified part - condition NE (not equals)")
    public void notGetOnePartTest() {
        QueryParams queryParams = new QueryParams();
        queryParams.use("componentName[NE]", "bracket_basic");

        ResponseWrapper<ComponentResponse> scenarioIterationRespond =
            scenarioIterationService.getScenarioIterationWithParams(queryParams);

        assertEquals(null, scenarioIterationRespond.getResponseEntity().getItems().stream()
            .filter(p -> p.getComponentDisplayName().equals("BRACKET_BASIC"))
            .findFirst()
            .orElse(null));
    }

    @Test
    @TestRail(id = {12441})
    @Description("Verify that GET scenario-iterations returns exactly specified parts in the range - condition IN")
    public void getPartsInRangeTest() {
        QueryParams queryParams = new QueryParams();
        queryParams.use("componentType[IN]", "bracket_basic");

        ResponseWrapper<ComponentResponse> scenarioIterationRespond =
            scenarioIterationService.getScenarioIterationWithParams(queryParams);

        assertTrue(scenarioIterationService.validateIfTrue(scenarioIterationRespond));
    }

    @Test
    @TestRail(id = {12442})
    @Description("Verify that GET scenario-iterations do not return exactly specified parts in the range - condition IN")
    public void notGetPartsInRangeTest() {
        QueryParams queryParams = new QueryParams();
        queryParams.use("componentType[NI]", "PART|ASSEMBLY");

        ResponseWrapper<ComponentResponse> scenarioIterationRespond =
            scenarioIterationService.getScenarioIterationWithParams(queryParams);

        assertFalse(scenarioIterationService.validateIfTrue(scenarioIterationRespond));
    }

    @Test
    @TestRail(id = {12443})
    @Description("Verify that GET scenario-iterations return correct parts when using  AND operator")
    public void getPartsTwoParamsWithAndTest() {
        QueryParams queryParams = new QueryParams();
        queryParams.use("componentType[EQ]", "PART");
        queryParams.use("componentName[EQ]", "bracket_basic");

        ResponseWrapper<ComponentResponse> scenarioIterationRespond =
            scenarioIterationService.getScenarioIterationWithParams(queryParams);

        assertTrue(scenarioIterationService.validateIfTrueWithAndOperator(scenarioIterationRespond));
    }

    @Test
    @TestRail(id = {12444})
    @Description("Verify that GET scenario-iterations return correct parts when using  SW(starts with / LIKE) operator")
    public void getPartsMatchTest() {
        QueryParams queryParams = new QueryParams();
        queryParams.use("componentName[SW]", "br");
        queryParams.use("componentName[EQ]", "bracket_basic");

        ResponseWrapper<ComponentResponse> scenarioIterationRespond =
            scenarioIterationService.getScenarioIterationWithParams(queryParams);

        assertTrue(scenarioIterationService.validateIfTrueWithSwOperator(scenarioIterationRespond));
    }
}


