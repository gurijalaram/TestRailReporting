package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.Deployment;
import com.apriori.cds.objects.response.InstallationItems;
import com.apriori.cds.objects.response.InstallationResponse;
import com.apriori.cds.objects.response.Site;
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

public class CdsInstallationsTests extends CdsTestUtil {
    private String url;

    private String customerIdentity;
    private String customerIdentityEndpoint;
    private String installationIdentityEndpoint;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }

    @After
    public void cleanUp() {
        if (installationIdentityEndpoint != null) {
            delete(installationIdentityEndpoint);
        }
        if (customerIdentityEndpoint != null) {
            delete(customerIdentityEndpoint);
        }
    }

    @Test
    @TestRail(testCaseId = "5823")
    @Description("API returns a list of all the installations in the CDS DB")
    public void getInstallations() {
        url = String.format(url, "installations");

        ResponseWrapper<InstallationResponse> response = getCommonRequest(url, true, InstallationResponse.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
        assertThat(response.getResponseEntity().getResponse().getItems().get(0).getRegion(), is(not(nullValue())));
    }

    @Test
    @TestRail(testCaseId = "5316")
    @Description("Add a installation to a customer")
    public void addCustomerInstallation() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();
        String realmKey = generateStringUtil.generateRealmKey();

        ResponseWrapper<Customer> customer = addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        String siteEndpoint = String.format(url, String.format("customers/%s/sites", customerIdentity));
        ResponseWrapper<Site> site = addSite(customerIdentity, siteName, siteID);
        assertThat(site.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String siteIdentity = site.getResponseEntity().getResponse().getIdentity();

        ResponseWrapper<Deployment> response = addDeployment(customerIdentity, siteIdentity);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String deploymentIdentity = response.getResponseEntity().getResponse().getIdentity();

        String installationEndpoint = String.format(url, String.format("customers/%s/deployments/%s/installations", customerIdentity, deploymentIdentity));
        ResponseWrapper<InstallationItems> installation = addInstallation(installationEndpoint, InstallationItems.class, realmKey, cloudRef);
        assertThat(installation.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String installationIdentity = installation.getResponseEntity().getResponse().getIdentity();
        installationIdentityEndpoint = String.format(url, String.format("customers/%s/deployments/%s/installations/%s", customerIdentity, deploymentIdentity, installationIdentity));

        assertThat(installation.getResponseEntity().getResponse().getName(), is(equalTo("Automation Installation")));
        assertThat(installation.getResponseEntity().getResponse().getRegion(), is(equalTo("na-1")));
    }

    @Test
    @TestRail(testCaseId = "5318")
    @Description("get Installations by Identity")
    public void getInstallationByIdentity() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();
        String realmKey = generateStringUtil.generateRealmKey();

        ResponseWrapper<Customer> customer = addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<Site> site = addSite(customerIdentity, siteName, siteID);
        assertThat(site.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String siteIdentity = site.getResponseEntity().getResponse().getIdentity();

        ResponseWrapper<Deployment> response = addDeployment(customerIdentity, siteIdentity);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String deploymentIdentity = response.getResponseEntity().getResponse().getIdentity();

        String installationsEndpoint = String.format(url, String.format("customers/%s/deployments/%s/installations", customerIdentity, deploymentIdentity));
        ResponseWrapper<InstallationItems> installation = addInstallation(installationsEndpoint, InstallationItems.class, realmKey, cloudRef);
        assertThat(installation.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String installationIdentity = installation.getResponseEntity().getResponse().getIdentity();
        installationIdentityEndpoint = String.format(url, String.format("customers/%s/deployments/%s/installations/%s", customerIdentity, deploymentIdentity, installationIdentity));

        ResponseWrapper<InstallationItems> identity = getCommonRequest(installationIdentityEndpoint, true, InstallationItems.class);

        assertThat(identity.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(identity.getResponseEntity().getResponse().getIdentity(), is(equalTo(installationIdentity)));
    }

    @Test
    @TestRail(testCaseId = "5317")
    @Description("Update an installation")
    public void patchInstallationByIdentity() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();
        String realmKey = generateStringUtil.generateRealmKey();

        ResponseWrapper<Customer> customer = addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<Site> site = addSite(customerIdentity, siteName, siteID);
        assertThat(site.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String siteIdentity = site.getResponseEntity().getResponse().getIdentity();

        ResponseWrapper<Deployment> response = addDeployment(customerIdentity, siteIdentity);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String deploymentIdentity = response.getResponseEntity().getResponse().getIdentity();

        String installationsEndpoint = String.format(url, String.format("customers/%s/deployments/%s/installations", customerIdentity, deploymentIdentity));
        ResponseWrapper<InstallationItems> installation = addInstallation(installationsEndpoint, InstallationItems.class, realmKey, cloudRef);
        assertThat(installation.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String installationIdentity = installation.getResponseEntity().getResponse().getIdentity();
        installationIdentityEndpoint = String.format(url, String.format("customers/%s/deployments/%s/installations/%s", customerIdentity, deploymentIdentity, installationIdentity));

        RequestEntity requestEntity = RequestEntity.init(installationIdentityEndpoint, InstallationItems.class)
            .setHeaders("Content-Type", "application/json")
            .setBody("installation",
                new InstallationItems().setCloudReference("eu-1"));

        ResponseWrapper<InstallationItems> updatedCloudRef = GenericRequestUtil.patch(requestEntity, new RequestAreaApi());
        assertThat(updatedCloudRef.getResponseEntity().getResponse().getCloudReference(), is(equalTo("eu-1")));
    }
}