package com.apriori.cds.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.apriori.apibase.services.cas.AttributeMappings;
import com.apriori.apibase.services.common.objects.IdentityProviderRequest;
import com.apriori.apibase.services.common.objects.IdentityProviderResponse;
import com.apriori.cds.entity.response.Customer;
import com.apriori.cds.entity.response.Deployment;
import com.apriori.cds.entity.response.Deployments;
import com.apriori.cds.entity.response.Site;
import com.apriori.cds.entity.response.User;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

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
        String customersEndpoint = String.format(url, "customers");

        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();

        ResponseWrapper<Customer> customer = addCustomer(customersEndpoint, Customer.class, customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        String siteEndpoint = String.format(url, String.format("customers/%s", customerIdentity.concat("/sites")));
        ResponseWrapper<Site> site = addSite(siteEndpoint, Site.class, siteName, siteID);
        assertThat(site.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String siteIdentity = site.getResponseEntity().getResponse().getIdentity();

        String deploymentsEndpoint = String.format(url, String.format("customers/%s", customerIdentity.concat("/deployments")));
        ResponseWrapper<Deployment> response = addDeployment(deploymentsEndpoint, Deployment.class, siteIdentity);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(response.getResponseEntity().getResponse().getName(), is(equalTo("Production Deployment")));
        assertThat(response.getResponseEntity().getResponse().getSites(), hasItem(siteIdentity));
    }

    @Test
    @TestRail(testCaseId = "5314")
    @Description("Get a list of deployments for a customer")
    public void getCustomerDeployments() {
        String customersEndpoint = String.format(url, "customers");

        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();

        ResponseWrapper<Customer> customer = addCustomer(customersEndpoint, Customer.class, customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        String siteEndpoint = String.format(url, String.format("customers/%s", customerIdentity.concat("/sites")));
        ResponseWrapper<Site> site = addSite(siteEndpoint, Site.class, siteName, siteID);
        assertThat(site.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String siteIdentity = site.getResponseEntity().getResponse().getIdentity();

        String deploymentsEndpoint = String.format(url, String.format("customers/%s", customerIdentity.concat("/deployments")));
        ResponseWrapper<Deployment> response = addDeployment(deploymentsEndpoint, Deployment.class, siteIdentity);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        ResponseWrapper<Deployments> deployment = getCommonRequest(deploymentsEndpoint, true, Deployments.class);

        assertThat(deployment.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(deployment.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
    }

    @Test
    @TestRail(testCaseId = "5315")
    @Description("Add a deployment to a customer")
    public void getDeploymentByIdentity() {
        String customersEndpoint = String.format(url, "customers");

        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();

        ResponseWrapper<Customer> customer = addCustomer(customersEndpoint, Customer.class, customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        String siteEndpoint = String.format(url, String.format("customers/%s", customerIdentity.concat("/sites")));
        ResponseWrapper<Site> site = addSite(siteEndpoint, Site.class, siteName, siteID);
        assertThat(site.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String siteIdentity = site.getResponseEntity().getResponse().getIdentity();

        String deploymentsEndpoint = String.format(url, String.format("customers/%s", customerIdentity.concat("/deployments")));
        ResponseWrapper<Deployment> response = addDeployment(deploymentsEndpoint, Deployment.class, siteIdentity);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String deploymentIdentity = response.getResponseEntity().getResponse().getIdentity();
        String deploymentIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity.concat("/deployments/".concat(deploymentIdentity))));

        ResponseWrapper<Deployment> deployment = getCommonRequest(deploymentIdentityEndpoint, true, Deployment.class);

        assertThat(deployment.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(deployment.getResponseEntity().getResponse().getIdentity(), is(equalTo(deploymentIdentity)));
    }

    @Test
    @TestRail(testCaseId = "5314")
    @Description("Get a list of deployments for a customer")
    public void postCustomerIdentityProviders() {
        String customersEndpoint = String.format(url, "customers");

        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<Customer> customer = addCustomer(customersEndpoint, Customer.class, customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));
        String usersEndpoint = String.format(url, String.format("customers/%s", customerIdentity.concat("/users")));

        ResponseWrapper<User> user = addUser(usersEndpoint, User.class, userName, customerName);
        String userIdentity = user.getResponseEntity().getResponse().getIdentity();

        String identityProviderEndpoint = String.format(url, String.format("customers/%s/identity-providers", customerIdentity));

        // TODO: 17/02/2021 this method should be moved to a utils and named eg. addSaml()
        RequestEntity requestEntity = RequestEntity.init(identityProviderEndpoint, IdentityProviderResponse.class)
            .setHeaders("Content-Type", "application/json")
            .setBody("identityProvider",
                new IdentityProviderRequest().setContact(userIdentity)
                    .setName(userName)
                    .setDisplayName(userName + "2")
                    .setIdpDomains(Arrays.asList(userName + ".com"))
                    .setIdentityProviderPlatform("AZURE AD")
                    .setDescription("Ciene Okta IdP using SAML")
                    .setActive(true)
                    .setCreatedBy("#SYSTEM00000")
                    .setSignInUrl(Constants.getSignInUrl())
                    .setSigningCertificate(Constants.getSignInCert())
                    .setSigningCertificateExpiresAt("2030-07-22T22:45Z")
                    .setSignRequest(true)
                    .setSignRequestAlgorithm("RSA_SHA256")
                    .setSignRequestAlgorithmDigest("SHA256")
                    .setProtocolBinding("HTTP_POST")
                    .setAttributeMappings(new AttributeMappings().setUser_id(Constants.getUserId())
                        .setEmail(Constants.getEmail())
                        .setName(Constants.getName())
                        .setGiven_name(Constants.getGivenName())
                        .setFamily_name(Constants.getFamilyName())));

        ResponseWrapper<IdentityProviderResponse> response = GenericRequestUtil.post(requestEntity, new RequestAreaApi());

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
    }
}
