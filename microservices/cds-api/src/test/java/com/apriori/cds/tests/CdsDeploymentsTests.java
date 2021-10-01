package com.apriori.cds.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.Deployment;
import com.apriori.cds.objects.response.Deployments;
import com.apriori.cds.objects.response.Site;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CdsDeploymentsTests {
    private static GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private static CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private static String customerName;
    private static String cloudRef;
    private static String salesForceId;
    private static String emailPattern;
    private static String customerIdentity;
    private static ResponseWrapper<Customer> customer;
    private static String siteName;
    private static String siteID;
    private static ResponseWrapper<Site> site;
    private static String siteIdentity;

    @BeforeClass
    public static void setDetails() {
        customerName = generateStringUtil.generateCustomerName();
        cloudRef = generateStringUtil.generateCloudReference();
        salesForceId = generateStringUtil.generateSalesForceId();
        emailPattern = "\\S+@".concat(customerName);

        customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();

        siteName = generateStringUtil.generateSiteName();
        siteID = generateStringUtil.generateSiteID();

        site = cdsTestUtil.addSite(customerIdentity, siteName, siteID);
        siteIdentity = site.getResponseEntity().getIdentity();
    }

    @AfterClass
    public static void cleanUp() {
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.DELETE_CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(testCaseId = {"3301"})
    @Description("Add a deployment to a customer")
    public void addCustomerDeployment() {
        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, "Production Deployment", siteIdentity, "PRODUCTION");
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(response.getResponseEntity().getResponse().getName(), is(equalTo("Production Deployment")));
        assertThat(response.getResponseEntity().getResponse().getCustomerIdentity(), is(equalTo(customerIdentity)));
    }

    @Test
    @TestRail(testCaseId = {"5314"})
    @Description("Get a list of deployments for a customer")
    public void getCustomerDeployments() {
        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, "Preview Deployment", siteIdentity, "PREVIEW");
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        ResponseWrapper<Deployments> deployment = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_DEPLOYMENTS_BY_CUSTOMER_ID,
            Deployments.class,
            customerIdentity
        );

        assertThat(deployment.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(deployment.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"5315"})
    @Description("Add a deployment to a customer")
    public void getDeploymentByIdentity() {
        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, "Sandbox Deployment", siteIdentity, "SANDBOX");
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String deploymentIdentity = response.getResponseEntity().getResponse().getIdentity();

        ResponseWrapper<Deployment> deployment = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_DEPLOYMENT_BY_CUSTOMER_DEPLOYMENT_IDS,
            Deployment.class,
            customerIdentity,
            deploymentIdentity
        );

        assertThat(deployment.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(deployment.getResponseEntity().getResponse().getIdentity(), is(equalTo(deploymentIdentity)));
    }
}
