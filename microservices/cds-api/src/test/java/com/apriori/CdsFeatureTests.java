package com.apriori;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.models.IdentityHolder;
import com.apriori.cds.models.response.ErrorResponse;
import com.apriori.cds.models.response.FeatureResponse;
import com.apriori.cds.models.response.InstallationItems;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.cds.utils.RandomCustomerData;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.response.Customer;
import com.apriori.models.response.Deployment;
import com.apriori.models.response.LicensedApplications;
import com.apriori.models.response.Site;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CdsFeatureTests {

    private SoftAssertions soft = new SoftAssertions();
    private String customerIdentity;
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private IdentityHolder licensedAppIdentityHolder;
    private IdentityHolder installationIdentityHolder;
    private String installationIdentity;
    private String deploymentIdentity;

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
    @TestRail(id = {21921})
    @Description("Verify Create Installation Feature Flag")
    public void verifyCreateInstallationFlag() {
        setAllCustomerData();

        ResponseWrapper<FeatureResponse> addFeature = cdsTestUtil.addFeature(customerIdentity, deploymentIdentity, installationIdentity, true, false);

        ResponseWrapper<FeatureResponse> getFeature = cdsTestUtil.getCommonRequest(CDSAPIEnum.INSTALLATION_FEATURES,
            FeatureResponse.class,
            HttpStatus.SC_OK,
            customerIdentity,
            deploymentIdentity,
            installationIdentity
        );

        soft.assertThat(getFeature.getResponseEntity().getIdentity())
            .isEqualTo(addFeature.getResponseEntity().getIdentity());
        soft.assertThat(getFeature.getResponseEntity().getCreatedAt())
            .isEqualTo(addFeature.getResponseEntity().getCreatedAt());
        soft.assertThat(getFeature.getResponseEntity().getCreatedBy())
            .isEqualTo(addFeature.getResponseEntity().getCreatedBy());
        soft.assertThat(getFeature.getResponseEntity().getWorkOrderStatusUpdatesEnabled())
            .isEqualTo(addFeature.getResponseEntity().getWorkOrderStatusUpdatesEnabled());
        soft.assertAll();
    }

    @Test
    @TestRail(id = {21926})
    @Description("Verify invalid Installation Feature Flag with wrong deployment")
    public void verifyInvalidInstallationFlag() {
        setAllCustomerData();

        ErrorResponse errorResponse = cdsTestUtil.addFeatureWrongResponse(customerIdentity, "wrongDeployment", installationIdentity, true, false);

        soft.assertThat(errorResponse.getError())
            .isEqualTo("Bad Request");
        soft.assertThat(errorResponse.getMessage())
            .isEqualTo("'deploymentIdentity' is not a valid identity.");
        soft.assertAll();

    }

    @Test
    @TestRail(id = {21924})
    @Description("Verify create installation features on installation")
    public void verifyCreateInstallationFeaturesOnInstallation() {
        RandomCustomerData rcd = new RandomCustomerData();
        String siteIdentity = allCustomerDataForInstallationFeature(rcd);

        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallationWithFeature(customerIdentity, deploymentIdentity, rcd.getRealmKey(), rcd.getCloudRef(), siteIdentity, true, false);
        installationIdentity = installation.getResponseEntity().getIdentity();

        ResponseWrapper<FeatureResponse> getFeature = cdsTestUtil.getCommonRequest(CDSAPIEnum.INSTALLATION_FEATURES,
            FeatureResponse.class,
            HttpStatus.SC_OK,
            customerIdentity,
            deploymentIdentity,
            installationIdentity
        );

        String appIdentity = Constants.getApProApplicationIdentity();
        ResponseWrapper<LicensedApplications> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        String licensedApplicationIdentity = licensedApp.getResponseEntity().getIdentity();

        installationIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .deploymentIdentity(deploymentIdentity)
            .installationIdentity(installationIdentity)
            .build();

        licensedAppIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .siteIdentity(siteIdentity)
            .licenseIdentity(licensedApplicationIdentity)
            .build();

        soft.assertThat(getFeature.getResponseEntity().getIdentity())
            .isEqualTo(installation.getResponseEntity().getFeatures().getIdentity());
        soft.assertThat(getFeature.getResponseEntity().getCreatedAt())
            .isEqualTo(installation.getResponseEntity().getFeatures().getCreatedAt());
        soft.assertThat(getFeature.getResponseEntity().getCreatedBy())
            .isEqualTo(installation.getResponseEntity().getFeatures().getCreatedBy());
        soft.assertThat(getFeature.getResponseEntity().getWorkOrderStatusUpdatesEnabled())
            .isEqualTo(installation.getResponseEntity().getFeatures().getWorkOrderStatusUpdatesEnabled());
        soft.assertAll();
    }

    @Test
    @TestRail(id = {21927})
    @Description("Verify Update installation feature")
    public void verifyUpdateInstallationFeature() {
        setAllCustomerData();

        cdsTestUtil.addFeature(customerIdentity, deploymentIdentity, installationIdentity, true, false);
        ResponseWrapper<FeatureResponse> updateFeature = cdsTestUtil.updateFeature(customerIdentity, deploymentIdentity, installationIdentity, false);

        ResponseWrapper<FeatureResponse> getFeature = cdsTestUtil.getCommonRequest(CDSAPIEnum.INSTALLATION_FEATURES,
            FeatureResponse.class,
            HttpStatus.SC_OK,
            customerIdentity,
            deploymentIdentity,
            installationIdentity
        );

        soft.assertThat(getFeature.getResponseEntity().getIdentity())
            .isEqualTo(updateFeature.getResponseEntity().getIdentity());
        soft.assertThat(getFeature.getResponseEntity().getCreatedAt())
            .isEqualTo(updateFeature.getResponseEntity().getCreatedAt());
        soft.assertThat(getFeature.getResponseEntity().getCreatedBy())
            .isEqualTo(updateFeature.getResponseEntity().getCreatedBy());
        soft.assertThat(getFeature.getResponseEntity().getWorkOrderStatusUpdatesEnabled())
            .isEqualTo(updateFeature.getResponseEntity().getWorkOrderStatusUpdatesEnabled());
        soft.assertAll();
    }

    @Test
    @TestRail(id = {21928})
    @Description("Verify Update installation feature - wrong installation url")
    public void verifyUpdateInstallationFeatureWrong() {
        setAllCustomerData();

        cdsTestUtil.addFeature(customerIdentity, deploymentIdentity, installationIdentity, true, false);
        ErrorResponse errorResponse = cdsTestUtil.updateFeatureWrongResponse(customerIdentity, deploymentIdentity, "wrongInstallation", false);

        soft.assertThat(errorResponse.getError())
            .isEqualTo("Bad Request");
        soft.assertThat(errorResponse.getMessage())
            .isEqualTo("'installationIdentity' is not a valid identity.");

        soft.assertAll();
    }

    private void setAllCustomerData() {
        RandomCustomerData rcd = new RandomCustomerData();

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(rcd.getCustomerName(), rcd.getCustomerType(), rcd.getCloudRef(), rcd.getSalesForceId(), rcd.getEmailPattern());
        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, rcd.getSiteName(), rcd.getSiteID());
        String siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, "Preview Deployment", siteIdentity, "PREVIEW");
        deploymentIdentity = response.getResponseEntity().getIdentity();

        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, "Automation Installation", rcd.getRealmKey(), rcd.getCloudRef(), siteIdentity, false);
        installationIdentity = installation.getResponseEntity().getIdentity();

        String appIdentity = Constants.getApProApplicationIdentity();
        ResponseWrapper<LicensedApplications> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        String licensedApplicationIdentity = licensedApp.getResponseEntity().getIdentity();

        installationIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .deploymentIdentity(deploymentIdentity)
            .installationIdentity(installationIdentity)
            .build();

        licensedAppIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .siteIdentity(siteIdentity)
            .licenseIdentity(licensedApplicationIdentity)
            .build();
    }

    private String allCustomerDataForInstallationFeature(RandomCustomerData rcd) {

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(rcd.getCustomerName(), rcd.getCustomerType(), rcd.getCloudRef(), rcd.getSalesForceId(), rcd.getEmailPattern());
        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, rcd.getSiteName(), rcd.getSiteID());
        String siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, "Preview Deployment", siteIdentity, "PREVIEW");
        deploymentIdentity = response.getResponseEntity().getIdentity();

        return siteIdentity;
    }
}
