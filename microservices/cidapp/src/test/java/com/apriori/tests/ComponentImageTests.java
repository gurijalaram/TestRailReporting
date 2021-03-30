package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.entity.response.PostComponentResponse;
import com.apriori.entity.response.componentiteration.ComponentIteration;
import com.apriori.entity.response.scenarios.CostResponse;
import com.apriori.tests.utils.CidAppTestUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class ComponentImageTests {

    private CidAppTestUtil cidAppTestUtil = new CidAppTestUtil();

    @Test
    @TestRail(testCaseId = "5834")
    @Description("Test bounding values are correct")
    public void boundingBoxValuesTest() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        ResponseWrapper<PostComponentResponse> postComponentResponse = cidAppTestUtil.postComponents(scenarioName, "Sheet Metal", "700-33770-01_A0.stp");

        assertThat(postComponentResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String componentIdentity = postComponentResponse.getResponseEntity().getComponentIdentity();
        String scenarioIdentity = postComponentResponse.getResponseEntity().getScenarioIdentity();

        ResponseWrapper<CostResponse> costResponse = cidAppTestUtil.postCostComponent(componentIdentity, scenarioIdentity);
        assertThat(costResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        ResponseWrapper<ComponentIteration> getComponentIterationResponse = cidAppTestUtil.getComponentIterationLatest(componentIdentity, scenarioIdentity);
        assertThat(getComponentIterationResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(getComponentIterationResponse.getResponseEntity().getResponse().getScenarioMetadata().getBoundingBox(), hasItems(-3.259932279586792, -200.0, -138.5635986328125, 276.724609375, 200.0, 15.436402320861816));
    }
}
