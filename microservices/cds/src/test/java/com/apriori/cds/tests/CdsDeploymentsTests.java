package com.apriori.cds.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CdsDeploymentsTests extends CdsTestUtil {
    private String url;
    private String customerIdentityEndpoint;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }

    @After
    public void cleanUp() {
        if (customerIdentityEndpoint != null) {
            delete(customerIdentityEndpoint);
        }
    }

    @Test
    @TestRail(testCaseId = "3301")
    @Description("Add a deployment to a customer")
    public void addCustomerDeployment() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();

        ResponseWrapper<Customer> customer = addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<Site> site = addSite(customerIdentity, siteName, siteID);
        assertThat(site.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String siteIdentity = site.getResponseEntity().getResponse().getIdentity();

        ResponseWrapper<Deployment> response = addDeployment(customerIdentity, siteIdentity);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(response.getResponseEntity().getResponse().getName(), is(equalTo("Production Deployment")));
        assertThat(response.getResponseEntity().getResponse().getSites(), hasItem(siteIdentity));
    }

    @Test
    @TestRail(testCaseId = "5314")
    @Description("Get a list of deployments for a customer")
    public void getCustomerDeployments() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();

        ResponseWrapper<Customer> customer = addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<Site> site = addSite(customerIdentity, siteName, siteID);
        assertThat(site.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String siteIdentity = site.getResponseEntity().getResponse().getIdentity();

        String deploymentsEndpoint = String.format(url, String.format("customers/%s/deployments", customerIdentity));
        ResponseWrapper<Deployment> response = addDeployment(customerIdentity, siteIdentity);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        ResponseWrapper<Deployments> deployment = getCommonRequest(deploymentsEndpoint, Deployments.class);

        assertThat(deployment.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(deployment.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
    }

    @Test
    @TestRail(testCaseId = "5315")
    @Description("Add a deployment to a customer")
    public void getDeploymentByIdentity() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();

        ResponseWrapper<Customer> customer = addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<Site> site = addSite(customerIdentity, siteName, siteID);
        assertThat(site.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String siteIdentity = site.getResponseEntity().getResponse().getIdentity();

        ResponseWrapper<Deployment> response = addDeployment(customerIdentity, siteIdentity);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String deploymentIdentity = response.getResponseEntity().getResponse().getIdentity();
        String deploymentIdentityEndpoint = String.format(url, String.format("customers/%s/deployments/%s", customerIdentity, deploymentIdentity));

        ResponseWrapper<Deployment> deployment = getCommonRequest(deploymentIdentityEndpoint, Deployment.class);

        assertThat(deployment.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(deployment.getResponseEntity().getResponse().getIdentity(), is(equalTo(deploymentIdentity)));
    }
}
