package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.utils.CdsTestUtil;
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
    private Customer aprioriInternal;
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private String customerIdentity;

    @Before
    public void getToken() {
        token = new AuthorizationUtil().getTokenAsString();
        aprioriInternal = cdsTestUtil.getAprioriInternal();
        customerIdentity = aprioriInternal.getIdentity();
    }

    @Test
    @TestRail(testCaseId = {"5657"})
    @Description("Returns a list of deployments for the customer.")
    public void getCustomersDeployments() {

        ResponseWrapper<Deployments> responseDeployment = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_DEPLOYMENTS, Deployments.class)
            .token(token)
            .inlineVariables(customerIdentity)).get();

        assertThat(responseDeployment.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(responseDeployment.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"5658"})
    @Description("Get the deployment identified by its identity.")
    public void getDeploymentByIdentity() {

        ResponseWrapper<Deployments> responseDeployments = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_DEPLOYMENTS, Deployments.class)
            .token(token)
            .inlineVariables(customerIdentity)).get();

        assertThat(responseDeployments.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(responseDeployments.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));

        String deploymentIdentity = responseDeployments.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<Deployment> deploymentByID = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_CUSTOMER_DEPLOYMENT, Deployment.class)
            .token(token)
            .inlineVariables(customerIdentity, deploymentIdentity)).get();

        assertThat(deploymentByID.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(deploymentByID.getResponseEntity().getIdentity(), is(equalTo(deploymentIdentity)));
    }
}
