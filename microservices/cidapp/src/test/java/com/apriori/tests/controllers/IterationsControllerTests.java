package com.apriori.tests.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidapp.entity.response.PostComponentResponse;
import com.apriori.cidapp.entity.response.componentiteration.ComponentIteration;
import com.apriori.cidapp.utils.CidAppTestUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class IterationsControllerTests {

    private CidAppTestUtil cidAppTestUtil = new CidAppTestUtil();

    @Test
    @Description("Get the latest iteration")
    public void getComponentsIterationsLatest() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        ResponseWrapper<PostComponentResponse> postComponentResponse = cidAppTestUtil.postComponents(scenarioName, "Casting - Die", "Casting.prt");

        assertThat(postComponentResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String componentIdentity = postComponentResponse.getResponseEntity().getComponentIdentity();
        String scenarioIdentity = postComponentResponse.getResponseEntity().getScenarioIdentity();

        ResponseWrapper<ComponentIteration> getComponentIterationResponse = cidAppTestUtil.getComponentIterationLatest(componentIdentity, scenarioIdentity);

        assertThat(getComponentIterationResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
    }
}
