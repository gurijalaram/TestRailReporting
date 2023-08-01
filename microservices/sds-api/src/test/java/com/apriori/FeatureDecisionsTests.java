package com.apriori;

import com.apriori.http.utils.ResponseWrapper;
import com.apriori.sds.controller.FeatureDecisionController;
import com.apriori.sds.entity.response.FeatureDecisionsResponse;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

public class FeatureDecisionsTests {

    private SoftAssertions soft = new SoftAssertions();

    @Test
    @TestRail(id = 21945)
    @Description("Verify feature decisions test")
    public void testFeatureDecisionsTest() {
        FeatureDecisionController featureDecisionController = new FeatureDecisionController();
        ResponseWrapper<FeatureDecisionsResponse> response = featureDecisionController.getFeatureDecisions();

        soft.assertThat(response.getResponseEntity().getItems()).isNotEmpty();
        soft.assertAll();
    }
}
