package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.entity.reponse.PostComponentResponse;
import com.apriori.entity.reponse.componentiteration.ComponentIteration;
import com.apriori.utils.CidAppTestUtil;
import com.apriori.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class ComponentImageTests {

    private CidAppTestUtil cidAppTestUtil = new CidAppTestUtil();

    @Test
    @Description("Test bounding values are correct")
    public void boundingBoxValuesTest() {
        String url = String.format(Constants.getApiUrl(), "components");
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        ResponseWrapper<PostComponentResponse> postComponentResponse = cidAppTestUtil.postComponents(url, scenarioName, "Casting - Die", "Casting.prt");

        assertThat(postComponentResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String componentIdentity = postComponentResponse.getResponseEntity().getComponentIdentity();
        String scenarioIdentity = postComponentResponse.getResponseEntity().getScenarioIdentity();

        String componentIterationLatestUrl = String.format(Constants.getApiUrl(), "components/" + componentIdentity + "/scenarios/" + scenarioIdentity + "/iterations/latest");

        ResponseWrapper<ComponentIteration> getComponentIterationResponse = cidAppTestUtil.getComponents(componentIterationLatestUrl, ComponentIteration.class);

        assertThat(getComponentIterationResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(getComponentIterationResponse.getResponseEntity().getScenarioMetadata().getBoundingBox(), hasItems(-3.259932279586792, -200.0, -138.5635986328125, 276.724609375, 200.0, 15.436402320861816));
    }
}
