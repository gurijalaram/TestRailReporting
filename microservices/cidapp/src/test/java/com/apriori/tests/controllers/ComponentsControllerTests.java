package com.apriori.tests.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import com.apriori.entity.reponse.ComponentIdentityResponse;
import com.apriori.entity.reponse.GetComponentResponse;
import com.apriori.entity.reponse.PostComponentResponse;
import com.apriori.utils.CidAppTestUtil;
import com.apriori.utils.Constants;
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
        String url = String.format(Constants.getApiUrl(), "components");
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        ResponseWrapper<PostComponentResponse> postComponentResponse = cidAppTestUtil.postComponents(url, scenarioName, "Casting - Die", "Casting.prt");

        assertThat(postComponentResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
    }

    @Test
    @Description("Find components for the current user matching a specified query")
    public void getComponents() {
        String url = String.format(Constants.getApiUrl(), "components");
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        ResponseWrapper<PostComponentResponse> postComponentResponse = cidAppTestUtil.postComponents(url, scenarioName, "Casting - Die", "Casting.prt");

        assertThat(postComponentResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        ResponseWrapper<GetComponentResponse> getComponentResponse = cidAppTestUtil.getComponents(url, GetComponentResponse.class);

        assertThat(getComponentResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(getComponentResponse.getResponseEntity().getResponse().getItems().size(), is(greaterThan(0)));
    }

    @Test
    @Description("Get the current representation of a component")
    public void getComponentIdentity() {
        String url = String.format(Constants.getApiUrl(), "components");
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        ResponseWrapper<PostComponentResponse> postComponentResponse = cidAppTestUtil.postComponents(url, scenarioName, "Casting - Die", "Casting.prt");

        assertThat(postComponentResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String componentIdentity = postComponentResponse.getResponseEntity().getComponentIdentity();

        String identityUrl = String.format(Constants.getApiUrl(), "components/" + componentIdentity);

        ResponseWrapper<ComponentIdentityResponse> componentIdentityResponse = cidAppTestUtil.getComponents(identityUrl, ComponentIdentityResponse.class);

        assertThat(componentIdentityResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(componentIdentityResponse.getResponseEntity().getResponse().getIdentity(), is(equalTo(componentIdentity)));
    }
}
