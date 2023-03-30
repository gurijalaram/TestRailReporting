package com;

import static com.apriori.entity.enums.CssSearch.COMPONENT_IDENTITY_EQ;
import static com.apriori.entity.enums.CssSearch.SCENARIO_IDENTITY_EQ;

import com.apriori.entity.response.CssComponentResponse;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.utils.CssComponent;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CssSearchTests {
    SoftAssertions soft = new SoftAssertions();
    private CssComponent cssComponent = new CssComponent();
    private static UserCredentials currentUser;
    private final String componentType = "PART";

    @Before
    public void setupUser() {
        currentUser = UserUtil.getUser();
    }

    @Test
    @Description("Test CSS base search")
    public void testCssBaseSearchCapability() {
        List<ScenarioItem> cssComponentResponses = cssComponent.getBaseCssComponents(currentUser, COMPONENT_IDENTITY_EQ.getKey() + " 50MFHK5MA6FI",
            SCENARIO_IDENTITY_EQ.getKey() + " 50N5K6J03I9F");

        cssComponentResponses.forEach(o -> {
            soft.assertThat(o.getComponentIdentity()).isEqualTo("50MFHK5MA6FI");
            soft.assertThat(o.getScenarioIdentity()).isEqualTo("50N5K6J03I9F");
        });

        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"22731"})
    @Description("Find scenario iterations for a given customer matching a specified query.")
    public void searchPartComponentScenarios() {
        ResponseWrapper<CssComponentResponse> components = cssComponent.postSearchRequest(currentUser, componentType);

        soft.assertThat(components.getResponseEntity().getPageItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertThat(components.getResponseEntity().getItems().get(0).getComponentType()).isEqualTo(componentType);
        soft.assertAll();
    }
}
