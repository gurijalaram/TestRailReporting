package com;

import static com.apriori.css.entity.enums.CssSearch.COMPONENT_IDENTITY_EQ;
import static com.apriori.css.entity.enums.CssSearch.SCENARIO_IDENTITY_EQ;

import com.apriori.css.entity.response.CssComponentResponse;
import com.apriori.utils.CssComponent;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

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
        ResponseWrapper<CssComponentResponse> cssComponentResponses = cssComponent.getBaseCssComponents(currentUser, COMPONENT_IDENTITY_EQ.getOperand() + " 50MFHK5MA6FI",
            SCENARIO_IDENTITY_EQ.getOperand() + " 50N5K6J03I9F");

        SoftAssertions softAssertions = new SoftAssertions();

        cssComponentResponses.getResponseEntity().getItems().forEach(o -> {
            softAssertions.assertThat(o.getComponentIdentity()).isEqualTo("50MFHK5MA6FI");
            softAssertions.assertThat(o.getScenarioIdentity()).isEqualTo("50N5K6J03I9F");
        });

        softAssertions.assertAll();
    }
}
