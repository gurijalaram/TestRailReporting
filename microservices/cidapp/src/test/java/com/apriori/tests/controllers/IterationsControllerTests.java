package com.apriori.tests.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.CidAppTestUtil;
import com.apriori.apibase.utils.JwtTokenUtil;
import com.apriori.entity.reponse.PostComponentResponse;
import com.apriori.entity.reponse.componentiteration.ComponentIteration;
import com.apriori.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;

public class IterationsControllerTests {

    private static String token;
    private CidAppTestUtil cidAppTestUtil = new CidAppTestUtil();

    @BeforeClass
    public static void getToken() {
        token = new JwtTokenUtil().retrieveJwtToken(Constants.getSecretKey(),
            Constants.getCidServiceHost(),
            HttpStatus.SC_CREATED,
            Constants.getCidTokenUsername(),
            Constants.getCidTokenEmail(),
            Constants.getCidTokenIssuer(),
            Constants.getCidTokenSubject());
    }

    @Test
    @Description("Get the latest iteration")
    public void getComponentsIterationsLatest() {
        String url = String.format(Constants.getApiUrl(), "components");
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        ResponseWrapper<PostComponentResponse> postComponentResponse = cidAppTestUtil.postComponents(url, token, scenarioName, "Casting - Die", "Casting.prt");

        assertThat(postComponentResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String componentIdentity = postComponentResponse.getResponseEntity().getComponentIdentity();
        String scenarioIdentity = postComponentResponse.getResponseEntity().getScenarioIdentity();

        String componentIterationLatestUrl = String.format(Constants.getApiUrl(), "components/" + componentIdentity + "/scenarios/" + scenarioIdentity + "/iterations/latest");

        ResponseWrapper<ComponentIteration> getComponentIterationResponse = cidAppTestUtil.getComponents(componentIterationLatestUrl, token, ComponentIteration.class);

        assertThat(getComponentIterationResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
    }
}
