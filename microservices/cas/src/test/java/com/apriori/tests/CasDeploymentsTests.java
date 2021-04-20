package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.CommonRequestUtil;
import com.apriori.apibase.utils.JwtTokenUtil;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.entity.response.Deployment;
import com.apriori.entity.response.Deployments;
import com.apriori.utils.Constants;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class CasDeploymentsTests extends TestUtil {
    private String token;

    @Before
    public void getToken() {
        token = new JwtTokenUtil().retrieveJwtToken(Constants.getSecretKey(),
                Constants.getCasServiceHost(),
                HttpStatus.SC_CREATED,
                Constants.getCasTokenUsername(),
                Constants.getCasTokenEmail(),
                Constants.getCasTokenIssuer(),
                Constants.getCasTokenSubject());
    }

    @Test
    @TestRail(testCaseId = {"5657"})
    @Description("Returns a list of deployments for the customer.")
    public void getCustomersDeployments() {
        String apiUrl = String.format(Constants.getApiUrl(), "customers/L2H992828LC1/deployments");

        ResponseWrapper<Deployments> responseDeployment = new CommonRequestUtil().getCommonRequest(apiUrl, true, Deployments.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(responseDeployment.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(responseDeployment.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"5658"})
    @Description("Get the deployment identified by its identity.")
    public void getDeploymentByIdentity() {
        String apiUrl = String.format(Constants.getApiUrl(), "customers/L2H992828LC1/deployments/");

        ResponseWrapper<Deployments> responseDeployments = new CommonRequestUtil().getCommonRequest(apiUrl, true, Deployments.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(responseDeployments.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(responseDeployments.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));

        String deploymentIdentity = responseDeployments.getResponseEntity().getResponse().getItems().get(0).getIdentity();
        String deploymentUrl = apiUrl + deploymentIdentity;

        ResponseWrapper<Deployment> deploymentByID = new CommonRequestUtil().getCommonRequest(deploymentUrl, true, Deployment.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(deploymentByID.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(deploymentByID.getResponseEntity().getResponse().getIdentity(), is(equalTo(deploymentIdentity)));
    }
}
