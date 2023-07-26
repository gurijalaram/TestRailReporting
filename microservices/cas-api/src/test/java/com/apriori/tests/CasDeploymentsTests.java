package com.apriori.tests;

import com.apriori.authorization.AuthorizationUtil;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.cds.entity.response.Customer;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.entity.response.Deployment;
import com.apriori.entity.response.Deployments;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

public class CasDeploymentsTests {
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private SoftAssertions soft = new SoftAssertions();
    private Customer aprioriInternal;
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private String customerIdentity;

    @Before
    public void setup() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
        aprioriInternal = cdsTestUtil.getAprioriInternal();
        customerIdentity = aprioriInternal.getIdentity();
    }

    @Test
    @TestRail(id = {5657})
    @Description("Returns a list of deployments for the customer.")
    public void getCustomersDeployments() {

        ResponseWrapper<Deployments> responseDeployment = casTestUtil.getCommonRequest(CASAPIEnum.DEPLOYMENTS, Deployments.class, HttpStatus.SC_OK, customerIdentity);

        soft.assertThat(responseDeployment.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5658})
    @Description("Get the deployment identified by its identity.")
    public void getDeploymentByIdentity() {

        ResponseWrapper<Deployments> responseDeployments = casTestUtil.getCommonRequest(CASAPIEnum.DEPLOYMENTS, Deployments.class, HttpStatus.SC_OK, customerIdentity);

        soft.assertThat(responseDeployments.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);

        String deploymentIdentity = responseDeployments.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<Deployment> deploymentByID = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMER_DEPLOYMENT, Deployment.class, HttpStatus.SC_OK, customerIdentity, deploymentIdentity);

        soft.assertThat(deploymentByID.getResponseEntity().getIdentity())
            .isEqualTo(deploymentIdentity);
        soft.assertAll();
    }
}
