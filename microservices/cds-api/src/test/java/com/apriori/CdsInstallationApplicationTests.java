package com.apriori;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.models.IdentityHolder;
import com.apriori.cds.models.response.InstallationItems;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.response.Customer;
import com.apriori.models.response.Deployment;
import com.apriori.models.response.LicensedApplications;
import com.apriori.models.response.Site;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CdsInstallationApplicationTests {
    private SoftAssertions soft = new SoftAssertions();
    private IdentityHolder licensedAppIdentityHolder;
    private IdentityHolder installationIdentityHolder;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private String customerIdentity;
    private String customerName;
    private String cloudRef;
    private String salesForceId;
    private String emailPattern;
    private ResponseWrapper<Customer> customer;
    private String siteName;
    private String siteID;
    private ResponseWrapper<Site> site;
    private String siteIdentity;
    private ResponseWrapper<Deployment> deployment;
    private String deploymentIdentity;

    @BeforeEach
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

        deployment = cdsTestUtil.addDeployment(customerIdentity, "Production Deployment", siteIdentity, "PRODUCTION");
        deploymentIdentity = deployment.getResponseEntity().getIdentity();
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
    @TestRail(id = {13183, 13184})
    @Description("Adds an application to an installation and deletes it")
    public void addApplicationToInstallation() {
        String realmKey = generateStringUtil.generateRealmKey();
        String appIdentity = Constants.getApProApplicationIdentity();

        ResponseWrapper<LicensedApplications> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
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

        ResponseWrapper<InstallationItems> application = cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, appIdentity, siteIdentity);
        soft.assertThat(application.getResponseEntity().getApplications().get(0).getIdentity()).isEqualTo(appIdentity);
        soft.assertAll();

        cdsTestUtil.delete(CDSAPIEnum.APPLICATION_INSTALLATION_BY_ID, customerIdentity, deploymentIdentity, installationIdentity, appIdentity);
    }
}