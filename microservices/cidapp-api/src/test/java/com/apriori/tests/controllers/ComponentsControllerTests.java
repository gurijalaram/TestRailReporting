package com.apriori.tests.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import com.apriori.cidappapi.entity.response.ComponentIdentityResponse;
import com.apriori.cidappapi.entity.response.GetComponentResponse;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.css.entity.response.Item;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class ComponentsControllerTests {

    private ComponentsUtil componentsUtil = new ComponentsUtil();
    private UserCredentials currentUser;

    @Test
    @Description("Add a new component")
    public void postComponents() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        Item postComponentResponse = componentsUtil.postCssComponent("Casting.prt", scenarioName, "Casting - Die", currentUser);
    }

    @Test
    @Description("Find components for the current user matching a specified query")
    public void getComponents() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        Item postComponentResponse = componentsUtil.postCssComponent("Casting.prt", scenarioName, "Casting - Die", currentUser);

        ResponseWrapper<GetComponentResponse> getComponentResponse = componentsUtil.getComponents();

        assertThat(getComponentResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(getComponentResponse.getResponseEntity().getItems().size(), is(greaterThan(0)));
    }

    @Test
    @Description("Get the current representation of a component")
    public void getComponentIdentity() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        Item postComponentResponse = componentsUtil.postCssComponent("Casting.prt", scenarioName, "Casting - Die", currentUser);

        String componentIdentity = postComponentResponse.getComponentIdentity();

        ResponseWrapper<ComponentIdentityResponse> componentIdentityResponse = componentsUtil.getComponentIdentity(componentIdentity);

        assertThat(componentIdentityResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(componentIdentityResponse.getResponseEntity().getIdentity(), is(equalTo(componentIdentity)));
    }
}
