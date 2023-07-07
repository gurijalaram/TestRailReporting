package com.apriori.cds.tests;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.common.customer.response.Customer;
import com.apriori.utils.common.customer.response.Deployment;
import com.apriori.utils.common.customer.response.Deployments;
import com.apriori.utils.common.customer.response.Site;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CdsDeploymentsTests {
    private SoftAssertions soft = new SoftAssertions();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
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

    @Before
    public void setDetails() {
        customerName = generateStringUtil.generateCustomerName();
        cloudRef = generateStringUtil.generateCloudReference();
        salesForceId = generateStringUtil.generateSalesForceId();
        emailPattern = "\\S+@".concat(customerName);
        String customerType = Constants.CLOUD_CUSTOMER;

        customer = cdsTestUtil.addCustomer(customerName, customerType, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();

        siteName = generateStringUtil.generateSiteName();
        siteID = generateStringUtil.generateSiteID();

        site = cdsTestUtil.addSite(customerIdentity, siteName, siteID);
        siteIdentity = site.getResponseEntity().getIdentity();
    }

    @After
    public void cleanUp() {
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(testCaseId = {"3301"})
    @Description("Add a deployment to a customer")
    public void addCustomerDeployment() {
        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, "Production Deployment", siteIdentity, "PRODUCTION");
        soft.assertThat(response.getResponseEntity().getName()).isEqualTo("Production Deployment");
        soft.assertThat(response.getResponseEntity().getCustomerIdentity()).isEqualTo(customerIdentity);
        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5314"})
    @Description("Get a list of deployments for a customer")
    public void getCustomerDeployments() {
        cdsTestUtil.addDeployment(customerIdentity, "Preview Deployment", siteIdentity, "PREVIEW");

        ResponseWrapper<Deployments> deployment = cdsTestUtil.getCommonRequest(CDSAPIEnum.DEPLOYMENTS_BY_CUSTOMER_ID,
            Deployments.class,
            HttpStatus.SC_OK,
            customerIdentity
        );

        soft.assertThat(deployment.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5315"})
    @Description("Add a deployment to a customer")
    public void getDeploymentByIdentity() {
        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, "Sandbox Deployment", siteIdentity, "SANDBOX");
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
