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
import com.apriori.cds.objects.response.LicensedApplication;
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

public class CdsInstallationsTests {
    private String url;

    private String customerIdentity;
    private String licensedAppIdentityEndpoint;
    private String customerIdentityEndpoint;
    private String installationIdentityEndpoint;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }

    @After
    public void cleanUp() {
        if (installationIdentityEndpoint != null) {
            cdsTestUtil.delete(installationIdentityEndpoint);
        }
        if (licensedAppIdentityEndpoint != null) {
            cdsTestUtil.delete(licensedAppIdentityEndpoint);
        }
        if (customerIdentityEndpoint != null) {
            cdsTestUtil.delete(customerIdentityEndpoint);
        }
    }

    @Test
    @TestRail(testCaseId = {"5823"})
    @Description("API returns a list of all the installations in the CDS DB")
    public void getInstallations() {
        url = String.format(url, "installations");

        ResponseWrapper<InstallationResponse> response = cdsTestUtil.getCommonRequest(url, InstallationResponse.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
        assertThat(response.getResponseEntity().getResponse().getItems().get(0).getRegion(), is(not(nullValue())));
    }

    @Test
    @TestRail(testCaseId = {"5316"})
    @Description("Add a installation to a customer")
    public void addCustomerInstallation() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();
        String realmKey = generateStringUtil.generateRealmKey();

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteID);
        assertThat(site.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, siteIdentity, "PRODUCTION");
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String deploymentIdentity = response.getResponseEntity().getResponse().getIdentity();

        ResponseWrapper<LicensedApplication> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity);
        assertThat(licensedApp.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String licensedApplicationIdentity = licensedApp.getResponseEntity().getResponse().getIdentity();
        licensedAppIdentityEndpoint = String.format(url, String.format("customers/%s/sites/%s/licensed-applications/%s", customerIdentity, siteIdentity, licensedApplicationIdentity));

        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, realmKey, cloudRef, siteIdentity);
        assertThat(installation.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String installationIdentity = installation.getResponseEntity().getResponse().getIdentity();
        installationIdentityEndpoint = String.format(url, String.format("customers/%s/deployments/%s/installations/%s", customerIdentity, deploymentIdentity, installationIdentity));

        assertThat(installation.getResponseEntity().getResponse().getName(), is(equalTo("Automation Installation")));
        assertThat(installation.getResponseEntity().getResponse().getRegion(), is(equalTo("na-1")));
    }

    @Test
    @TestRail(testCaseId = {"5318"})
    @Description("get Installations by Identity")
    public void getInstallationByIdentity() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();
        String realmKey = generateStringUtil.generateRealmKey();

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteID);
        assertThat(site.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, siteIdentity, "PREVIEW");
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String deploymentIdentity = response.getResponseEntity().getResponse().getIdentity();

        ResponseWrapper<LicensedApplication> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity);
        assertThat(licensedApp.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String licensedApplicationIdentity = licensedApp.getResponseEntity().getResponse().getIdentity();
        licensedAppIdentityEndpoint = String.format(url, String.format("customers/%s/sites/%s/licensed-applications/%s", customerIdentity, siteIdentity, licensedApplicationIdentity));

        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, realmKey, cloudRef, siteIdentity);
        assertThat(installation.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String installationIdentity = installation.getResponseEntity().getResponse().getIdentity();
        installationIdentityEndpoint = String.format(url, String.format("customers/%s/deployments/%s/installations/%s", customerIdentity, deploymentIdentity, installationIdentity));

        ResponseWrapper<InstallationItems> identity = cdsTestUtil.getCommonRequest(installationIdentityEndpoint, InstallationItems.class);

        assertThat(identity.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(identity.getResponseEntity().getResponse().getIdentity(), is(equalTo(installationIdentity)));
    }

    @Test
    @TestRail(testCaseId = {"5317"})
    @Description("Update an installation")
    public void patchInstallationByIdentity() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();
        String realmKey = generateStringUtil.generateRealmKey();

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteID);
        assertThat(site.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, siteIdentity, "SANDBOX");
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String deploymentIdentity = response.getResponseEntity().getResponse().getIdentity();

        ResponseWrapper<LicensedApplication> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity);
        assertThat(licensedApp.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String licensedApplicationIdentity = licensedApp.getResponseEntity().getResponse().getIdentity();
        licensedAppIdentityEndpoint = String.format(url, String.format("customers/%s/sites/%s/licensed-applications/%s", customerIdentity, siteIdentity, licensedApplicationIdentity));

        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, realmKey, cloudRef, siteIdentity);
        assertThat(installation.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String installationIdentity = installation.getResponseEntity().getResponse().getIdentity();
        installationIdentityEndpoint = String.format(url, String.format("customers/%s/deployments/%s/installations/%s", customerIdentity, deploymentIdentity, installationIdentity));

        ResponseWrapper<InstallationItems> installationItemsResponse = cdsTestUtil.patchInstallation(customerIdentity, deploymentIdentity, installationIdentity);
        assertThat(installationItemsResponse.getResponseEntity().getResponse().getCloudReference(), is(equalTo("eu-1")));
    }
}