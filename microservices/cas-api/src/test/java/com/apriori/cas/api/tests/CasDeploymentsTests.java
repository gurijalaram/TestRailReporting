package com.apriori.cas.api.tests;

import com.apriori.cas.api.enums.CASAPIEnum;
import com.apriori.cas.api.models.response.Deployment;
import com.apriori.cas.api.models.response.Deployments;
import com.apriori.cas.api.utils.CasTestUtil;
import com.apriori.shared.util.SharedCustomerUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
@EnabledIf(value = "com.apriori.shared.util.properties.PropertiesContext#isAPCustomer")
public class CasDeploymentsTests {
    private CasTestUtil casTestUtil;
    private SoftAssertions soft = new SoftAssertions();
    private String customerIdentity;

    @BeforeEach
    public void setup() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser()
            .useTokenInRequests();
        casTestUtil = new CasTestUtil(requestEntityUtil);
        customerIdentity = SharedCustomerUtil.getCustomerData().getIdentity();
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
