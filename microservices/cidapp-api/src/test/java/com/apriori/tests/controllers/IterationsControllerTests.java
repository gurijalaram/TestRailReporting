package com.apriori.tests.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidapp.entity.response.componentiteration.ComponentIteration;
import com.apriori.cidapp.entity.response.css.Item;
import com.apriori.utils.CidAppTestUtil;
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

        Item postComponentResponse = cidAppTestUtil.postComponents("Casting.prt", scenarioName, "Casting - Die");

        String componentIdentity = postComponentResponse.getComponentIdentity();
        String scenarioIdentity = postComponentResponse.getScenarioIdentity();

        ResponseWrapper<ComponentIteration> getComponentIterationResponse = cidAppTestUtil.getComponentIterationLatest(componentIdentity, scenarioIdentity);

        assertThat(getComponentIterationResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
    }
}
