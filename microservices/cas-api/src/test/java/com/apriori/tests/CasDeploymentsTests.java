package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.entity.response.Deployment;
import com.apriori.entity.response.Deployments;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class CasDeploymentsTests {
    private String token;

    @Before
    public void getToken() {
        token = new AuthorizationUtil().getTokenAsString();
    }

    @Test
    @TestRail(testCaseId = {"5657"})
    @Description("Returns a list of deployments for the customer.")
    public void getCustomersDeployments() {

        ResponseWrapper<Deployments> responseDeployment = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_CUSTOMER, Deployments.class)
            .token(token)
            .inlineVariables("deployments/")).get();

        assertThat(responseDeployment.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(responseDeployment.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"5658"})
    @Description("Get the deployment identified by its identity.")
    public void getDeploymentByIdentity() {

        ResponseWrapper<Deployments> responseDeployments = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_CUSTOMER, Deployments.class)
            .token(token)).get();

        assertThat(responseDeployments.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(responseDeployments.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));

        String deploymentIdentity = responseDeployments.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<Deployment> deploymentByID = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_CUSTOMER_DEPLOYMENT, Deployment.class)
            .token(token)
            .inlineVariables(deploymentIdentity)).get();

        assertThat(deploymentByID.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(deploymentByID.getResponseEntity().getIdentity(), is(equalTo(deploymentIdentity)));
    }
}
