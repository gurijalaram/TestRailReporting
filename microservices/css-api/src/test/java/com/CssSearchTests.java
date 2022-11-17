package com;

import static com.apriori.entity.enums.CssSearch.COMPONENT_IDENTITY_EQ;
import static com.apriori.entity.enums.CssSearch.SCENARIO_IDENTITY_EQ;

import com.apriori.entity.response.ScenarioItem;
import com.apriori.utils.CssComponent;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CssSearchTests {
    private CssComponent cssComponent = new CssComponent();
    private static UserCredentials currentUser;

    @Before
    public void setupUser() {
        currentUser = UserUtil.getUser();
    }

    @Test
    @Description("Test CSS base search")
    public void testCssBaseSearchCapability() {
        List<ScenarioItem> cssComponentResponses = cssComponent.getBaseCssComponents(currentUser, COMPONENT_IDENTITY_EQ.getKey() + " 50MFHK5MA6FI",
            SCENARIO_IDENTITY_EQ.getKey() + " 50N5K6J03I9F");

        SoftAssertions softAssertions = new SoftAssertions();

        cssComponentResponses.forEach(o -> {
            softAssertions.assertThat(o.getComponentIdentity()).isEqualTo("50MFHK5MA6FI");
            softAssertions.assertThat(o.getScenarioIdentity()).isEqualTo("50N5K6J03I9F");
        });

        softAssertions.assertAll();
    }
}
