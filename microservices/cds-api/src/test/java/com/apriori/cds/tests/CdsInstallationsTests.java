package com.apriori.cds.tests;

import com.apriori.GenerateStringUtil;
import com.apriori.cds.entity.IdentityHolder;
import com.apriori.cds.entity.response.Customer;
import com.apriori.cds.entity.response.Deployment;
import com.apriori.cds.entity.response.LicensedApplication;
import com.apriori.cds.entity.response.Site;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.InstallationItems;
import com.apriori.cds.objects.response.InstallationResponse;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Test;

public class CdsInstallationsTests {
    private SoftAssertions soft = new SoftAssertions();
    private String customerIdentity;
    private IdentityHolder licensedAppIdentityHolder;
    private IdentityHolder installationIdentityHolder;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @After
    public void cleanUp() {
        if (installationIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.INSTALLATION_BY_CUSTOMER_DEPLOYMENT_INSTALLATION_IDS,
                installationIdentityHolder.customerIdentity(),
                installationIdentityHolder.deploymentIdentity(),
                installationIdentityHolder.installationIdentity()
            );
        }
        if (licensedAppIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS,
                licensedAppIdentityHolder.customerIdentity(),
                licensedAppIdentityHolder.siteIdentity(),
                licensedAppIdentityHolder.licenseIdentity()
            );
        }
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(id = {5823})
    @Description("API returns a list of all the installations in the CDS DB")
    public void getInstallations() {
        ResponseWrapper<InstallationResponse> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.INSTALLATIONS, InstallationResponse.class, HttpStatus.SC_OK);

        soft.assertThat(response.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertThat(response.getResponseEntity().getItems().get(0).getRegion()).isNotNull();
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5316})
    @Description("Add a installation to a customer")
    public void addCustomerInstallation() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();
        String realmKey = generateStringUtil.generateRealmKey();
        String customerType = Constants.CLOUD_CUSTOMER;

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(customerName, customerType, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteID);
        String siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, "Production Deployment", siteIdentity, "PRODUCTION");
        String deploymentIdentity = response.getResponseEntity().getIdentity();

        String appIdentity = Constants.getApProApplicationIdentity();

        ResponseWrapper<LicensedApplication> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        String licensedApplicationIdentity = licensedApp.getResponseEntity().getIdentity();

        licensedAppIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .siteIdentity(siteIdentity)
            .licenseIdentity(licensedApplicationIdentity)
            .build();

        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, realmKey, cloudRef, siteIdentity);
        String installationIdentity = installation.getResponseEntity().getIdentity();

        installationIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .deploymentIdentity(deploymentIdentity)
            .installationIdentity(installationIdentity)
            .build();

        soft.assertThat(installation.getResponseEntity().getName()).isEqualTo("Automation Installation");
        soft.assertThat(installation.getResponseEntity().getRegion()).isEqualTo("na-1");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5318})
    @Description("get Installations by Identity")
    public void getInstallationByIdentity() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();
        String realmKey = generateStringUtil.generateRealmKey();
        String customerType = Constants.CLOUD_CUSTOMER;

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(customerName, customerType, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteID);
        String siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, "Preview Deployment", siteIdentity, "PREVIEW");
        String deploymentIdentity = response.getResponseEntity().getIdentity();

        String appIdentity = Constants.getApProApplicationIdentity();
        ResponseWrapper<LicensedApplication> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        String licensedApplicationIdentity = licensedApp.getResponseEntity().getIdentity();

        licensedAppIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .siteIdentity(siteIdentity)
            .licenseIdentity(licensedApplicationIdentity)
            .build();

        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, realmKey, cloudRef, siteIdentity);
        String installationIdentity = installation.getResponseEntity().getIdentity();

        installationIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .deploymentIdentity(deploymentIdentity)
            .installationIdentity(installationIdentity)
            .build();

        ResponseWrapper<InstallationItems> identity = cdsTestUtil.getCommonRequest(CDSAPIEnum.INSTALLATION_BY_CUSTOMER_DEPLOYMENT_INSTALLATION_IDS,
            InstallationItems.class,
            HttpStatus.SC_OK,
            customerIdentity,
            deploymentIdentity,
            installationIdentity
        );

        soft.assertThat(identity.getResponseEntity().getIdentity()).isEqualTo(installationIdentity);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5317})
    @Description("Update an installation")
    public void patchInstallationByIdentity() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();
        String realmKey = generateStringUtil.generateRealmKey();
        String customerType = Constants.CLOUD_CUSTOMER;

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(customerName, customerType, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteID);
        String siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, "Sandbox Deployment", siteIdentity, "SANDBOX");
        String deploymentIdentity = response.getResponseEntity().getIdentity();

        String appIdentity = Constants.getApProApplicationIdentity();
        ResponseWrapper<LicensedApplication> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        String licensedApplicationIdentity = licensedApp.getResponseEntity().getIdentity();

        licensedAppIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .siteIdentity(siteIdentity)
            .licenseIdentity(licensedApplicationIdentity)
            .build();

        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, realmKey, cloudRef, siteIdentity);
        String installationIdentity = installation.getResponseEntity().getIdentity();

        installationIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .deploymentIdentity(deploymentIdentity)
            .installationIdentity(installationIdentity)
            .build();

        ResponseWrapper<InstallationItems> installationItemsResponse = cdsTestUtil.patchInstallation(customerIdentity, deploymentIdentity, installationIdentity);
        soft.assertThat(installationItemsResponse.getResponseEntity().getCloudReference()).isEqualTo("eu-1");
        soft.assertAll();
    }
}