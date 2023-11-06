package com.apriori.sds.api.tests;

import com.apriori.sds.api.controller.FeatureDecisionController;
import com.apriori.sds.api.models.response.FeatureDecisionsResponse;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
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
