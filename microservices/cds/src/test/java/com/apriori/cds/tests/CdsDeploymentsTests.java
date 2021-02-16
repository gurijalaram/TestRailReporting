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

        //addSaml()
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
                    .setSignInUrl("https://aprioritest.oktapreview.com/app/aprioritest_aprioritechnologies_1/exkuyvviKdbHl5IMa1d5/sso/sam")
                    .setSigningCertificate("-----BEGIN CERTIFICATE-----\\nMIIDpjCCAo6gAwIBAgIGAXN4tBWjMA0GCSqGSIb3DQEBCwUAMIGTMQswCQYDVQQGEwJVUzETMBEG\\nA1UECAwKQ2FsaWZvcm5pYTEWMBQGA1UEBwwNU2FuIEZyYW5jaXNjbzENMAsGA1UECgwET2t0YTEU\\nMBIGA1UECwwLU1NPUHJvdmlkZXIxFDASBgNVBAMMC2Fwcmlvcml0ZXN0MRwwGgYJKoZIhvcNAQkB\\nFg1pbmZvQG9rdGEuY29tMB4XDTIwMDcyMjIyNDQzNFoXDTMwMDcyMjIyNDUzNFowgZMxCzAJBgNV\\nBAYTAlVTMRMwEQYDVQQIDApDYWxpZm9ybmlhMRYwFAYDVQQHDA1TYW4gRnJhbmNpc2NvMQ0wCwYD\\nVQQKDARPa3RhMRQwEgYDVQQLDAtTU09Qcm92aWRlcjEUMBIGA1UEAwwLYXByaW9yaXRlc3QxHDAa\\nBgkqhkiG9w0BCQEWDWluZm9Ab2t0YS5jb20wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIB\\nAQC6bGJ+reDF+25VjGlhORysq9o13sZnEPeoIJ9LoQEmGvklVl8b1I50zYv4Pa7p6P8hZ/L2skhV\\n2vCPIJw89mdQBElcQNsugWMP/wws8tmAd0tegn8wEOleSgm5NHiqNEr8Xk+uV6cqpDPITEaLXPBQ\\n1NNgZPY4HA1x9Jyd9qAY3d26LcAEbII3VKx9Fzbgi65Z33ZuQaux87HyrUEfHYB+7pwt+MLBVZlF\\nFzv/e/nkI8nZmY1wRy0kDMFwaMyvwGvl+UaYERzUiUK6KAmJEuLlRkBrc76Q3Vg5SgBDAczYGnNV\\ni9okyB9eKPZfrJYrTuWmfl2I9LVwllNbhPzxkJxrAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAATX\\nIZfcr93OtjT5l+925e35SuM4APUhl/c/qvmDtAoLimlHbX/9kKyLrh9ahf7F8WIX7QgnKLRWIUfW\\npkfcmVzQARHqWABPQVA0Hh3QvUVsmGInohq8DdRwh+1yz/ZC/lkGEACLRqN/PFH4ONQ90MM7EiWl\\nzHXqb0tp5bNW0NcXjK0DfxL6AKYvdzooKc2tCTa7h9fhJD6WDenN4xZVkALSE96bGvF5zQkoLPQq\\nE+xAIuaYd75Cpu9FLafSSc6eEEg3HpKbzGp0Ku5Du+UwqEatPhCrjc7y2tT5lOYHfsWboCCHmfn7\\n0oSMqaURZv2R1xoFRvL1P1apxxavljTHdiY=\\n-----END CERTIFICATE-----\\n")
                    .setSigningCertificateExpiresAt("2030-07-22T22:45Z")
                    .setSignRequest(true)
                    .setSignRequestAlgorithm("RSA_SHA256")
                    .setSignRequestAlgorithmDigest("SHA256")
                    .setProtocolBinding("HTTP_POST")
                    .setAttributeMappings(new AttributeMappings().setUser_id("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier")
                        .setEmail("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/emailaddress")
                        .setName("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name")
                        .setGiven_name("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/givenname")
                        .setFamily_name("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/surname")));

        ResponseWrapper<IdentityProviderResponse> response = GenericRequestUtil.post(requestEntity, new RequestAreaApi());

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
    }
}
