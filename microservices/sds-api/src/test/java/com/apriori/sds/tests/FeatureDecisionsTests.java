package com.apriori.sds.tests;

import com.apriori.sds.controller.FeatureDecisionController;
import com.apriori.sds.entity.response.FeatureDecisionsResponse;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

public class FeatureDecisionsTests {

    private SoftAssertions soft = new SoftAssertions();

    @Test
    @TestRail(testCaseId = "21945")
    @Description("Verify feature decisions test")
    public void testFeatureDecisionsTest() {
        FeatureDecisionController featureDecisionController = new FeatureDecisionController();
        ResponseWrapper<FeatureDecisionsResponse> response =  featureDecisionController.getFeatureDecisions();

        soft.assertThat(response.getResponseEntity().getItems()).isNotEmpty();
        soft.assertAll();
    }
}
