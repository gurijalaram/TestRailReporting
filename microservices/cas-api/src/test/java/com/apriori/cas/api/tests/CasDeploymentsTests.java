package com.apriori.cas.api.tests;

import static com.apriori.shared.util.enums.CustomerEnum.AP_INT;
import static com.apriori.shared.util.enums.RolesEnum.APRIORI_DEVELOPER;

import com.apriori.cas.api.enums.CASAPIEnum;
import com.apriori.cas.api.models.response.Deployment;
import com.apriori.cas.api.models.response.Deployments;
import com.apriori.cas.api.utils.CasTestUtil;
import com.apriori.shared.util.CustomerUtil;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
@EnabledIfSystemProperty(named = "customer", matches = AP_INT)
public class CasDeploymentsTests {
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private SoftAssertions soft = new SoftAssertions();
    private String customerIdentity;
    private UserCredentials currentUser = UserUtil.getUser(APRIORI_DEVELOPER);

    @BeforeEach
    public void setup() {
        RequestEntityUtil_Old.useTokenForRequests(currentUser.getToken());
        customerIdentity = CustomerUtil.getCustomerData().getIdentity();
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
