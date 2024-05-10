package com.apriori.ags.api.tests;

import static com.apriori.css.api.enums.CssSearch.COMPONENT_IDENTITY_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_IDENTITY_EQ;

import com.apriori.ags.api.utils.AgsUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.component.ComponentResponse;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AgsCssTests extends AgsUtil {
    private static SoftAssertions softAssertions = new SoftAssertions();
    private ScenarioItem scenarioItem;

    @Test
    @TestRail(id = {30992})
    @Description("Test CSS base search")
    public void testCssBaseSearchCapability() {
        scenarioItem = getIterationsRequest().getResponseEntity().getItems().get(0);
        String componentIdentity = scenarioItem.getComponentIdentity();
        String scenarioIdentity = scenarioItem.getScenarioIdentity();

        List<ScenarioItem> cssComponentResponses = getBaseCssComponents(COMPONENT_IDENTITY_EQ.getKey() + componentIdentity,
            SCENARIO_IDENTITY_EQ.getKey() + scenarioIdentity);

        cssComponentResponses.forEach(o -> {
            softAssertions.assertThat(o.getComponentIdentity()).isEqualTo(componentIdentity);
            softAssertions.assertThat(o.getScenarioIdentity()).isEqualTo(scenarioIdentity);
        });

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {30993})
    @Description("Find scenario iterations for a given customer matching a specified query.")
    public void searchPartComponentScenarios() {
        String componentType = "PART";
        ResponseWrapper<ComponentResponse> components = postSearchRequest(componentType);
        softAssertions.assertThat(components.getResponseEntity().getPageItemCount()).isGreaterThanOrEqualTo(1);
        softAssertions.assertThat(components.getResponseEntity().getItems().get(0).getComponentType()).isEqualTo(componentType);
        softAssertions.assertAll();
    }
}