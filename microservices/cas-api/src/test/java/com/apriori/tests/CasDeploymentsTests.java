package com.apriori.tests;

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
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

public class CasDeploymentsTests {
    private SoftAssertions soft = new SoftAssertions();
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

        soft.assertThat(responseDeployment.getStatusCode())
            .isEqualTo(HttpStatus.SC_OK);
        soft.assertThat(responseDeployment.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5658"})
    @Description("Get the deployment identified by its identity.")
    public void getDeploymentByIdentity() {

        ResponseWrapper<Deployments> responseDeployments = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_DEPLOYMENTS, Deployments.class)
            .token(token)
            .inlineVariables(customerIdentity)).get();

        soft.assertThat(responseDeployments.getStatusCode())
            .isEqualTo(HttpStatus.SC_OK);
        soft.assertThat(responseDeployments.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);

        String deploymentIdentity = responseDeployments.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<Deployment> deploymentByID = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_CUSTOMER_DEPLOYMENT, Deployment.class)
            .token(token)
            .inlineVariables(customerIdentity, deploymentIdentity)).get();

        soft.assertThat(deploymentByID.getStatusCode())
            .isEqualTo(HttpStatus.SC_OK);
        soft.assertThat(deploymentByID.getResponseEntity().getIdentity())
            .isEqualTo(deploymentIdentity);
        soft.assertAll();
    }
}
