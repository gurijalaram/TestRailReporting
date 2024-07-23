package com.apriori.cds.api.tests;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.Constants;
import com.apriori.cds.api.utils.CustomerUtil;
import com.apriori.cds.api.utils.DeploymentUtil;
import com.apriori.cds.api.utils.SiteUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.Deployment;
import com.apriori.shared.util.models.response.Deployments;
import com.apriori.shared.util.models.response.Site;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CdsDeploymentsTests {
    private SoftAssertions soft = new SoftAssertions();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil;
    private CustomerUtil customerUtil;
    private SiteUtil siteUtil;
    private DeploymentUtil deploymentUtil;
    private String customerName;
    private String cloudRef;
    private String salesForceId;
    private String emailPattern;
    private String customerIdentity;
    private ResponseWrapper<Customer> customer;
    private String siteName;
    private String siteID;
    private ResponseWrapper<Site> site;
    private String siteIdentity;

    @BeforeEach
    public void setDetails() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        cdsTestUtil = new CdsTestUtil(requestEntityUtil);
        customerUtil = new CustomerUtil(requestEntityUtil);
        siteUtil = new SiteUtil(requestEntityUtil);
        deploymentUtil = new DeploymentUtil(requestEntityUtil);

        customerName = generateStringUtil.generateAlphabeticString("Customer", 6);
        cloudRef = generateStringUtil.generateCloudReference();
        salesForceId = generateStringUtil.generateNumericString("SFID", 10);
        emailPattern = "\\S+@".concat(customerName);
        String customerType = Constants.CLOUD_CUSTOMER;

        customer = customerUtil.addCustomer(customerName, customerType, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();

        siteName = generateStringUtil.generateAlphabeticString("Site", 5);
        siteID = generateStringUtil.generateSiteID();

        site = siteUtil.addSite(customerIdentity, siteName, siteID);
        siteIdentity = site.getResponseEntity().getIdentity();
    }

    @AfterEach
    public void cleanUp() {
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(id = {3301})
    @Description("Add a deployment to a customer")
    public void addCustomerDeployment() {
        ResponseWrapper<Deployment> response = deploymentUtil.addDeployment(customerIdentity, "Production Deployment", siteIdentity, "PRODUCTION");
        soft.assertThat(response.getResponseEntity().getName()).isEqualTo("Production Deployment");
        soft.assertThat(response.getResponseEntity().getCustomerIdentity()).isEqualTo(customerIdentity);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5314})
    @Description("Get a list of deployments for a customer")
    public void getCustomerDeployments() {
        deploymentUtil.addDeployment(customerIdentity, "Preview Deployment", siteIdentity, "PREVIEW");

        ResponseWrapper<Deployments> deployment = cdsTestUtil.getCommonRequest(CDSAPIEnum.DEPLOYMENTS_BY_CUSTOMER_ID,
            Deployments.class,
            HttpStatus.SC_OK,
            customerIdentity
        );

        soft.assertThat(deployment.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5315})
    @Description("Add a deployment to a customer")
    public void getDeploymentByIdentity() {
        ResponseWrapper<Deployment> response = deploymentUtil.addDeployment(customerIdentity, "Sandbox Deployment", siteIdentity, "SANDBOX");
        String deploymentIdentity = response.getResponseEntity().getIdentity();

        ResponseWrapper<Deployment> deployment = cdsTestUtil.getCommonRequest(CDSAPIEnum.DEPLOYMENT_BY_CUSTOMER_DEPLOYMENT_IDS,
            Deployment.class,
            HttpStatus.SC_OK,
            customerIdentity,
            deploymentIdentity
        );

        soft.assertThat(deployment.getResponseEntity().getIdentity()).isEqualTo(deploymentIdentity);
        soft.assertAll();
    }
}
