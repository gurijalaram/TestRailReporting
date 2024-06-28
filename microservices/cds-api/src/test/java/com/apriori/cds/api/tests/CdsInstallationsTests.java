package com.apriori.cds.api.tests;

import static com.apriori.cds.api.enums.ApplicationEnum.AP_PRO;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.IdentityHolder;
import com.apriori.cds.api.models.response.InstallationItems;
import com.apriori.cds.api.models.response.InstallationResponse;
import com.apriori.cds.api.utils.ApplicationUtil;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.Constants;
import com.apriori.cds.api.utils.CustomerUtil;
import com.apriori.cds.api.utils.InstallationUtil;
import com.apriori.cds.api.utils.SiteUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.Deployment;
import com.apriori.shared.util.models.response.LicensedApplications;
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
public class CdsInstallationsTests {
    private SoftAssertions soft = new SoftAssertions();
    private String customerIdentity;
    private IdentityHolder licensedAppIdentityHolder;
    private IdentityHolder installationIdentityHolder;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil;
    private ApplicationUtil applicationUtil;
    private CustomerUtil customerUtil;
    private InstallationUtil installationUtil;
    private SiteUtil siteUtil;

    @BeforeEach
    public void setup() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        cdsTestUtil = new CdsTestUtil(requestEntityUtil);
        applicationUtil = new ApplicationUtil(requestEntityUtil);
        customerUtil = new CustomerUtil(requestEntityUtil);
        installationUtil = new InstallationUtil(requestEntityUtil);
        siteUtil = new SiteUtil(requestEntityUtil);
    }

    @AfterEach
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
        String customerName = generateStringUtil.generateAlphabeticString("Customer", 6);
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateNumericString("SFID", 10);
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateAlphabeticString("Site", 5);
        String siteID = generateStringUtil.generateSiteID();
        String realmKey = generateStringUtil.generateNumericString("RealmKey", 26);
        String customerType = Constants.CLOUD_CUSTOMER;

        ResponseWrapper<Customer> customer = customerUtil.addCustomer(customerName, customerType, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<Site> site = siteUtil.addSite(customerIdentity, siteName, siteID);
        String siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, "Production Deployment", siteIdentity, "PRODUCTION");
        String deploymentIdentity = response.getResponseEntity().getIdentity();

        String appIdentity = applicationUtil.getApplicationIdentity(AP_PRO);

        ResponseWrapper<LicensedApplications> licensedApp = applicationUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        String licensedApplicationIdentity = licensedApp.getResponseEntity().getIdentity();

        licensedAppIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .siteIdentity(siteIdentity)
            .licenseIdentity(licensedApplicationIdentity)
            .build();

        ResponseWrapper<InstallationItems> installation = installationUtil.addInstallation(customerIdentity, deploymentIdentity, "Automation Installation", realmKey, cloudRef, siteIdentity, false);
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
        String customerName = generateStringUtil.generateAlphabeticString("Customer", 6);
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateNumericString("SFID", 10);
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateAlphabeticString("Site", 5);
        String siteID = generateStringUtil.generateSiteID();
        String realmKey = generateStringUtil.generateNumericString("RealmKey", 26);
        String customerType = Constants.CLOUD_CUSTOMER;

        ResponseWrapper<Customer> customer = customerUtil.addCustomer(customerName, customerType, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<Site> site = siteUtil.addSite(customerIdentity, siteName, siteID);
        String siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, "Preview Deployment", siteIdentity, "PREVIEW");
        String deploymentIdentity = response.getResponseEntity().getIdentity();

        String appIdentity = applicationUtil.getApplicationIdentity(AP_PRO);
        ResponseWrapper<LicensedApplications> licensedApp = applicationUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        String licensedApplicationIdentity = licensedApp.getResponseEntity().getIdentity();

        licensedAppIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .siteIdentity(siteIdentity)
            .licenseIdentity(licensedApplicationIdentity)
            .build();

        ResponseWrapper<InstallationItems> installation = installationUtil.addInstallation(customerIdentity, deploymentIdentity, "Automation Installation", realmKey, cloudRef, siteIdentity, false);
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
        String customerName = generateStringUtil.generateAlphabeticString("Customer", 6);
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateNumericString("SFID", 10);
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateAlphabeticString("Site", 5);
        String siteID = generateStringUtil.generateSiteID();
        String realmKey = generateStringUtil.generateNumericString("RealmKey", 26);
        String customerType = Constants.CLOUD_CUSTOMER;

        ResponseWrapper<Customer> customer = customerUtil.addCustomer(customerName, customerType, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<Site> site = siteUtil.addSite(customerIdentity, siteName, siteID);
        String siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, "Sandbox Deployment", siteIdentity, "SANDBOX");
        String deploymentIdentity = response.getResponseEntity().getIdentity();

        String appIdentity = applicationUtil.getApplicationIdentity(AP_PRO);
        ResponseWrapper<LicensedApplications> licensedApp = applicationUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        String licensedApplicationIdentity = licensedApp.getResponseEntity().getIdentity();

        licensedAppIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .siteIdentity(siteIdentity)
            .licenseIdentity(licensedApplicationIdentity)
            .build();

        ResponseWrapper<InstallationItems> installation = installationUtil.addInstallation(customerIdentity, deploymentIdentity, "Automation Installation", realmKey, cloudRef, siteIdentity, false);
        String installationIdentity = installation.getResponseEntity().getIdentity();

        installationIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .deploymentIdentity(deploymentIdentity)
            .installationIdentity(installationIdentity)
            .build();

        ResponseWrapper<InstallationItems> installationItemsResponse = installationUtil.patchInstallation(customerIdentity, deploymentIdentity, installationIdentity);
        soft.assertThat(installationItemsResponse.getResponseEntity().getCloudReference()).isEqualTo("eu-1");
        soft.assertAll();
    }
}