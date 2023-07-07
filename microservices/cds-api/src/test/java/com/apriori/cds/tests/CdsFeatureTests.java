package com.apriori.cds.tests;

import com.apriori.cds.entity.IdentityHolder;
import com.apriori.cds.entity.response.ErrorResponse;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.FeatureResponse;
import com.apriori.cds.objects.response.InstallationItems;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.cds.utils.RandomCustomerData;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.common.customer.response.Customer;
import com.apriori.utils.common.customer.response.Deployment;
import com.apriori.utils.common.customer.response.LicensedApplication;
import com.apriori.utils.common.customer.response.Site;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Test;

public class CdsFeatureTests {

    private SoftAssertions soft = new SoftAssertions();
    private String customerIdentity;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private IdentityHolder licensedAppIdentityHolder;
    private IdentityHolder installationIdentityHolder;
    private String installationIdentity;
    private String deploymentIdentity;

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
    @TestRail(testCaseId = {"21921"})
    @Description("Verify Create Installation Feature Flag")
    public void verifyCreateInstallationFlag() {
        setAllCustomerData();

        ResponseWrapper<FeatureResponse> addFeature = cdsTestUtil.addFeature(customerIdentity, deploymentIdentity,installationIdentity,true);

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
    @TestRail(testCaseId = {"21926"})
    @Description("Verify invalid Installation Feature Flag with wrong deployment")
    public void verifyInvalidInstallationFlag() {
        setAllCustomerData();

        ErrorResponse errorResponse = cdsTestUtil.addFeatureWrongResponse(customerIdentity, "wrongDeployment",installationIdentity,true);

        soft.assertThat(errorResponse.getError())
            .isEqualTo("Bad Request");
        soft.assertThat(errorResponse.getMessage())
            .isEqualTo("'deploymentIdentity' is not a valid identity.");
        soft.assertAll();

    }

    @Test
    @TestRail(testCaseId = {"21924"})
    @Description("Verify create installation features on installation")
    public void verifyCreateInstallationFeaturesOnInstallation() {
        RandomCustomerData rcd = new RandomCustomerData();
        String siteIdentity = allCustomerDataForInstallationFeature(rcd);

        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallationWithFeature(customerIdentity, deploymentIdentity, rcd.getRealmKey(), rcd.getCloudRef(), siteIdentity,true);
        installationIdentity = installation.getResponseEntity().getIdentity();

        ResponseWrapper<FeatureResponse> getFeature = cdsTestUtil.getCommonRequest(CDSAPIEnum.INSTALLATION_FEATURES,
            FeatureResponse.class,
            HttpStatus.SC_OK,
            customerIdentity,
            deploymentIdentity,
            installationIdentity
        );

        String appIdentity = Constants.getApProApplicationIdentity();
        ResponseWrapper<LicensedApplication> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
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
    @TestRail(testCaseId = {"21927"})
    @Description("Verify Update installation feature")
    public void verifyUpdateInstallationFeature() {
        setAllCustomerData();

        cdsTestUtil.addFeature(customerIdentity, deploymentIdentity,installationIdentity,true);
        ResponseWrapper<FeatureResponse> updateFeature = cdsTestUtil.updateFeature(customerIdentity, deploymentIdentity,installationIdentity,false);

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
    @TestRail(testCaseId = {"21928"})
    @Description("Verify Update installation feature - wrong installation url")
    public void verifyUpdateInstallationFeatureWrong() {
        setAllCustomerData();

        cdsTestUtil.addFeature(customerIdentity, deploymentIdentity,installationIdentity,true);
        ErrorResponse errorResponse = cdsTestUtil.updateFeatureWrongResponse(customerIdentity, deploymentIdentity,"wrongInstallation",false);

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

        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, rcd.getRealmKey(), rcd.getCloudRef(), siteIdentity);
        installationIdentity = installation.getResponseEntity().getIdentity();

        String appIdentity = Constants.getApProApplicationIdentity();
        ResponseWrapper<LicensedApplication> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
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
