package com.apriori.cds.api.tests;

import static com.apriori.cds.api.enums.ApplicationEnum.AP_PRO;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.IdentityHolder;
import com.apriori.cds.api.models.response.ErrorResponse;
import com.apriori.cds.api.models.response.FeatureResponse;
import com.apriori.cds.api.models.response.InstallationItems;
import com.apriori.cds.api.utils.ApplicationUtil;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.InstallationUtil;
import com.apriori.cds.api.utils.RandomCustomerData;
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
public class CdsFeatureTests {
    private SoftAssertions soft = new SoftAssertions();
    private String customerIdentity;
    private CdsTestUtil cdsTestUtil;
    private ApplicationUtil applicationUtil;
    private InstallationUtil installationUtil;
    private IdentityHolder licensedAppIdentityHolder;
    private IdentityHolder installationIdentityHolder;
    private String installationIdentity;
    private String deploymentIdentity;

    @BeforeEach
    public void setup() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        cdsTestUtil = new CdsTestUtil(requestEntityUtil);
        applicationUtil = new ApplicationUtil(requestEntityUtil);
        installationUtil = new InstallationUtil(requestEntityUtil);
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
    @TestRail(id = {21921})
    @Description("Verify Create Installation Feature Flag")
    public void verifyCreateInstallationFlag() {
        setAllCustomerData();

        FeatureResponse addFeature = installationUtil.addFeature(false, customerIdentity, deploymentIdentity, installationIdentity);

        ResponseWrapper<FeatureResponse> getFeature = cdsTestUtil.getCommonRequest(CDSAPIEnum.INSTALLATION_FEATURES,
            FeatureResponse.class,
            HttpStatus.SC_OK,
            customerIdentity,
            deploymentIdentity,
            installationIdentity
        );

        soft.assertThat(getFeature.getResponseEntity().getIdentity())
            .isEqualTo(addFeature.getIdentity());
        soft.assertThat(getFeature.getResponseEntity().getCreatedAt())
            .isEqualTo(addFeature.getCreatedAt());
        soft.assertThat(getFeature.getResponseEntity().getCreatedBy())
            .isEqualTo(addFeature.getCreatedBy());
        soft.assertAll();
    }

    @Test
    @TestRail(id = {21926})
    @Description("Verify invalid Installation Feature Flag with wrong deployment")
    public void verifyInvalidInstallationFlag() {
        setAllCustomerData();

        ErrorResponse errorResponse = installationUtil.addFeatureWrongResponse(false, customerIdentity, "wrongDeployment", installationIdentity);

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

        ResponseWrapper<InstallationItems> installation = installationUtil.addInstallationWithFeature(customerIdentity, deploymentIdentity, rcd.getRealmKey(), rcd.getCloudRef(), siteIdentity, false);
        installationIdentity = installation.getResponseEntity().getIdentity();

        ResponseWrapper<FeatureResponse> getFeature = cdsTestUtil.getCommonRequest(CDSAPIEnum.INSTALLATION_FEATURES,
            FeatureResponse.class,
            HttpStatus.SC_OK,
            customerIdentity,
            deploymentIdentity,
            installationIdentity
        );

        String appIdentity = applicationUtil.getApplicationIdentity(AP_PRO);
        ResponseWrapper<LicensedApplications> licensedApp = applicationUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
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
        soft.assertAll();
    }

    @Test
    @TestRail(id = {21927})
    @Description("Verify Update installation feature")
    public void verifyUpdateInstallationFeature() {
        setAllCustomerData();

        installationUtil.addFeature(false, customerIdentity, deploymentIdentity, installationIdentity);
        FeatureResponse updateFeature = installationUtil.updateFeature(false, customerIdentity, deploymentIdentity, installationIdentity);

        ResponseWrapper<FeatureResponse> getFeature = cdsTestUtil.getCommonRequest(CDSAPIEnum.INSTALLATION_FEATURES,
            FeatureResponse.class,
            HttpStatus.SC_OK,
            customerIdentity,
            deploymentIdentity,
            installationIdentity
        );

        soft.assertThat(getFeature.getResponseEntity().getIdentity())
            .isEqualTo(updateFeature.getIdentity());
        soft.assertThat(getFeature.getResponseEntity().getCreatedAt())
            .isEqualTo(updateFeature.getCreatedAt());
        soft.assertThat(getFeature.getResponseEntity().getCreatedBy())
            .isEqualTo(updateFeature.getCreatedBy());
        soft.assertAll();
    }

    @Test
    @TestRail(id = {21928})
    @Description("Verify Update installation feature - wrong installation url")
    public void verifyUpdateInstallationFeatureWrong() {
        setAllCustomerData();

        installationUtil.addFeature(false, customerIdentity, deploymentIdentity, installationIdentity);
        ErrorResponse errorResponse = installationUtil.updateFeatureWrongResponse(customerIdentity, deploymentIdentity, "wrongInstallation");

        soft.assertThat(errorResponse.getError())
            .isEqualTo("Bad Request");
        soft.assertThat(errorResponse.getMessage())
            .isEqualTo("'installationIdentity' is not a valid identity.");

        soft.assertAll();
    }

    private void setAllCustomerData() {
        RandomCustomerData rcd = new RandomCustomerData();

        ResponseWrapper<Customer> customer = cdsTestUtil.createCustomer(rcd);
        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, rcd.getSiteName(), rcd.getSiteID());
        String siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, "Preview Deployment", siteIdentity, "PREVIEW");
        deploymentIdentity = response.getResponseEntity().getIdentity();

        ResponseWrapper<InstallationItems> installation = installationUtil.addInstallation(customerIdentity, deploymentIdentity, "Automation Installation", rcd.getRealmKey(), rcd.getCloudRef(), siteIdentity, false);
        installationIdentity = installation.getResponseEntity().getIdentity();

        String appIdentity = applicationUtil.getApplicationIdentity(AP_PRO);
        ResponseWrapper<LicensedApplications> licensedApp = applicationUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
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

        ResponseWrapper<Customer> customer = cdsTestUtil.createCustomer(rcd);
        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, rcd.getSiteName(), rcd.getSiteID());
        String siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, "Preview Deployment", siteIdentity, "PREVIEW");
        deploymentIdentity = response.getResponseEntity().getIdentity();

        return siteIdentity;
    }
}
