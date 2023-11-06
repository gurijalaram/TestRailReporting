package com.apriori.css.api.tests;

import static com.apriori.css.api.enums.CssSearch.COMPONENT_IDENTITY_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_IDENTITY_EQ;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.API_SANITY;

import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.component.ComponentResponse;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class CssSearchTests {
    SoftAssertions soft = new SoftAssertions();
    private CssComponent cssComponent = new CssComponent();
    private static UserCredentials currentUser;
    private final String componentType = "PART";
    private ScenarioItem scenarioItem;

    @BeforeEach
    public void setupUser() {
        currentUser = UserUtil.getUser();
    }

    @Test
    @Description("Test CSS base search")
    public void testCssBaseSearchCapability() {
        scenarioItem = cssComponent.getIterationsRequest(currentUser).getResponseEntity().getItems().get(0);
        String componentIdentity = scenarioItem.getComponentIdentity();
        String scenarioIdentity = scenarioItem.getScenarioIdentity();

        List<ScenarioItem> cssComponentResponses = cssComponent.getBaseCssComponents(currentUser, COMPONENT_IDENTITY_EQ.getKey() + componentIdentity,
            SCENARIO_IDENTITY_EQ.getKey() + scenarioIdentity);

        cssComponentResponses.forEach(o -> {
            soft.assertThat(o.getComponentIdentity()).isEqualTo(componentIdentity);
            soft.assertThat(o.getScenarioIdentity()).isEqualTo(scenarioIdentity);
        });

        soft.assertAll();
    }

    @Test
    @Tag(API_SANITY)
    @TestRail(id = {22731})
    @Description("Find scenario iterations for a given customer matching a specified query.")
    public void searchPartComponentScenarios() {
        ResponseWrapper<ComponentResponse> components = cssComponent.postSearchRequest(currentUser, componentType);

        soft.assertThat(components.getResponseEntity().getPageItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertThat(components.getResponseEntity().getItems().get(0).getComponentType()).isEqualTo(componentType);
        soft.assertAll();
    }
}
