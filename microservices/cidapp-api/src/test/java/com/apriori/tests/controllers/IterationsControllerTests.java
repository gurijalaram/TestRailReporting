package com.apriori.tests.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.response.componentiteration.ComponentIteration;
import com.apriori.cidappapi.utils.CidAppTestUtil;
import com.apriori.css.entity.response.Item;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class IterationsControllerTests {

    private CidAppTestUtil cidAppTestUtil = new CidAppTestUtil();
    private UserCredentials currentUser;

    @Test
    @Description("Get the latest iteration")
    public void getComponentsIterationsLatest() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        Item postComponentResponse = cidAppTestUtil.postCssComponents("Casting.prt", scenarioName, "Casting - Die", currentUser);

        String componentIdentity = postComponentResponse.getComponentIdentity();
        String scenarioIdentity = postComponentResponse.getScenarioIdentity();

        ResponseWrapper<ComponentIteration> getComponentIterationResponse = cidAppTestUtil.getComponentIterationLatest(componentIdentity, scenarioIdentity);

        assertThat(getComponentIterationResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
    }
}
