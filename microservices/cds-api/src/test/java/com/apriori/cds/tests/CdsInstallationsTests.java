package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.Deployment;
import com.apriori.cds.objects.response.InstallationItems;
import com.apriori.cds.objects.response.InstallationResponse;
import com.apriori.cds.objects.response.LicensedApplication;
import com.apriori.cds.objects.response.Site;
import com.apriori.cds.entity.IdentityHolder;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;

public class CdsInstallationsTests {

    private String customerIdentity;
    private IdentityHolder licensedAppIdentityHolder;
    private IdentityHolder installationIdentityHolder;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @After
    public void cleanUp() {
        if (installationIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.DELETE_INSTALLATIONS_BY_CUSTOMER_DEPLOYMENT_INSTALLATION_IDS,
                installationIdentityHolder.customerIdentity(),
                installationIdentityHolder.deploymentIdentity(),
                installationIdentityHolder.installationIdentity()
            );
        }
        if (licensedAppIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.DELETE_CUSTOMER_LICENSED_APPLICATIONS_BY_CUSTOMER_SITE_LICENSED_IDS,
                licensedAppIdentityHolder.customerIdentity(),
                licensedAppIdentityHolder.siteIdentity(),
                licensedAppIdentityHolder.licenseIdentity()
            );
        }
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.DELETE_CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(testCaseId = {"5823"})
    @Description("API returns a list of all the installations in the CDS DB")
    public void getInstallations() {
        ResponseWrapper<InstallationResponse> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_INSTALLATIONS, InstallationResponse.class);

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
        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteID);
        assertThat(site.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, "Production Deployment", siteIdentity, "PRODUCTION");
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String deploymentIdentity = response.getResponseEntity().getResponse().getIdentity();

        String appIdentity = Constants.getApProApplicationIdentity();

        ResponseWrapper<LicensedApplication> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        assertThat(licensedApp.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String licensedApplicationIdentity = licensedApp.getResponseEntity().getIdentity();

        licensedAppIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .siteIdentity(siteIdentity)
            .licenseIdentity(licensedApplicationIdentity)
            .build();

        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, realmKey, cloudRef, siteIdentity);
        assertThat(installation.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String installationIdentity = installation.getResponseEntity().getIdentity();
        installationIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .deploymentIdentity(deploymentIdentity)
            .installationIdentity(installationIdentity)
            .build();

        assertThat(installation.getResponseEntity().getName(), is(equalTo("Automation Installation")));
        assertThat(installation.getResponseEntity().getRegion(), is(equalTo("na-1")));
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
        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteID);
        assertThat(site.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, "Preview Deployment", siteIdentity, "PREVIEW");
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String deploymentIdentity = response.getResponseEntity().getResponse().getIdentity();

        String appIdentity = Constants.getApProApplicationIdentity();
        ResponseWrapper<LicensedApplication> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        assertThat(licensedApp.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String licensedApplicationIdentity = licensedApp.getResponseEntity().getIdentity();
        licensedAppIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .siteIdentity(siteIdentity)
            .licenseIdentity(licensedApplicationIdentity)
            .build();

        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, realmKey, cloudRef, siteIdentity);
        assertThat(installation.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String installationIdentity = installation.getResponseEntity().getIdentity();
        installationIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .deploymentIdentity(deploymentIdentity)
            .installationIdentity(installationIdentity)
            .build();

        ResponseWrapper<InstallationItems> identity = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_INSTALLATIONS_BY_CUSTOMER_DEPLOYMENT_INSTALLATION_IDS,
            InstallationItems.class,
            customerIdentity,
            deploymentIdentity,
            installationIdentity
        );

        assertThat(identity.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(identity.getResponseEntity().getIdentity(), is(equalTo(installationIdentity)));
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
        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteID);
        assertThat(site.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, "Sandbox Deployment", siteIdentity, "SANDBOX");
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String deploymentIdentity = response.getResponseEntity().getResponse().getIdentity();

        String appIdentity = Constants.getApProApplicationIdentity();
        ResponseWrapper<LicensedApplication> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        assertThat(licensedApp.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String licensedApplicationIdentity = licensedApp.getResponseEntity().getIdentity();
        licensedAppIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .siteIdentity(siteIdentity)
            .licenseIdentity(licensedApplicationIdentity)
            .build();

        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, realmKey, cloudRef, siteIdentity);
        assertThat(installation.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String installationIdentity = installation.getResponseEntity().getIdentity();
        installationIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .deploymentIdentity(deploymentIdentity)
            .installationIdentity(installationIdentity)
            .build();

        ResponseWrapper<InstallationItems> installationItemsResponse = cdsTestUtil.patchInstallation(customerIdentity, deploymentIdentity, installationIdentity);
        assertThat(installationItemsResponse.getResponseEntity().getCloudReference(), is(equalTo("eu-1")));
    }
}