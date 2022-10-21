package com;

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
    public void testCssBaseSearch() {
        ResponseWrapper<CssComponentResponse> cssComponentResponses = cssComponent.getBaseCssComponents(currentUser);

        SoftAssertions softAssertions = new SoftAssertions();

        cssComponentResponses.getResponseEntity().getItems().forEach(o -> softAssertions.assertThat(o.getComponentIdentity()).isNotEmpty());

        softAssertions.assertAll();
    }

    @Test
    @Description("Test CSS with query params")
    public void testCssWithQueryParams() {
        ResponseWrapper<CssComponentResponse> cssComponentsResponse = cssComponent.getCssComponentsQueryParams(currentUser, "componentIdentity, 50MFHK5MA6FI", "lastAction, create");
        ResponseWrapper<CssComponentResponse> cssComponentsResponse2 = cssComponent.getCssComponentsQueryParams(currentUser, "customerIdentity, 6C1F8C1D4D75",
            "scenarioIdentity, 50N5K6J03I9F", "lastAction, create");

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(cssComponentsResponse.getResponseEntity().getItems()).hasSizeGreaterThan(0);
        softAssertions.assertThat(cssComponentsResponse2.getResponseEntity().getItems()).hasSize(1);

        softAssertions.assertAll();
    }
}
