package com.apriori.tests.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import com.apriori.cidapp.entity.response.css.Item;
import com.apriori.utils.CidAppTestUtil;
import com.apriori.entity.response.ComponentIdentityResponse;
import com.apriori.entity.response.GetComponentResponse;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class ComponentsControllerTests {

    private CidAppTestUtil cidAppTestUtil = new CidAppTestUtil();

    @Test
    @Description("Add a new component")
    public void postComponents() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        Item postComponentResponse = cidAppTestUtil.postComponents("Casting.prt", scenarioName, "Casting - Die");
    }

    @Test
    @Description("Find components for the current user matching a specified query")
    public void getComponents() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        Item postComponentResponse = cidAppTestUtil.postComponents("Casting.prt", scenarioName, "Casting - Die");

        ResponseWrapper<GetComponentResponse> getComponentResponse = cidAppTestUtil.getComponents();

        assertThat(getComponentResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(getComponentResponse.getResponseEntity().getResponse().getItems().size(), is(greaterThan(0)));
    }

    @Test
    @Description("Get the current representation of a component")
    public void getComponentIdentity() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        Item postComponentResponse = cidAppTestUtil.postComponents("Casting.prt", scenarioName, "Casting - Die");

        String componentIdentity = postComponentResponse.getComponentIdentity();

        ResponseWrapper<ComponentIdentityResponse> componentIdentityResponse = cidAppTestUtil.getComponentIdentity(componentIdentity);

        assertThat(componentIdentityResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(componentIdentityResponse.getResponseEntity().getResponse().getIdentity(), is(equalTo(componentIdentity)));
    }
}
