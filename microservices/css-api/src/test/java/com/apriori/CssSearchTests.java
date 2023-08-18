package com.apriori;

import static com.apriori.enums.CssSearch.COMPONENT_IDENTITY_EQ;
import static com.apriori.enums.CssSearch.SCENARIO_IDENTITY_EQ;

import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.response.CssComponentResponse;
import com.apriori.models.response.ScenarioItem;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;
import com.apriori.utils.CssComponent;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
    @TestRail(id = {22731})
    @Description("Find scenario iterations for a given customer matching a specified query.")
    public void searchPartComponentScenarios() {
        ResponseWrapper<CssComponentResponse> components = cssComponent.postSearchRequest(currentUser, componentType);

        soft.assertThat(components.getResponseEntity().getPageItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertThat(components.getResponseEntity().getItems().get(0).getComponentType()).isEqualTo(componentType);
        soft.assertAll();
    }
}
