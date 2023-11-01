package com.apriori;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.models.response.InstallationItems;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.cds.utils.RandomCustomerData;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.response.Customer;
import com.apriori.models.response.Deployment;
import com.apriori.models.response.Enablements;
import com.apriori.models.response.LicensedApplications;
import com.apriori.models.response.Site;
import com.apriori.models.response.User;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CdsUserMgmtSandboxPreviewTests {
    private final String appIdentity = Constants.getApProApplicationIdentity();
    private final String ciaIdentity = Constants.getCiaApplicationIdentity();
    private final String cirIdentity = Constants.getCirAppIdentity();
    private SoftAssertions soft = new SoftAssertions();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private String customerIdentity;
    private String siteIdentity;
    private String licensedApProIdentity;
    private String licensedCiaIdentity;
    private String licensedCirIdentity;
    private String installationIdentity;
    private String previewInstallationIdentity;
    private String sandboxInstallationIdentity;
    private ResponseWrapper<User> user;
    private String userName;
    private String userIdentity;
    private final String customerAssignedRole = "APRIORI_DEVELOPER";

    @AfterEach
    public void cleanUp() {
        if (customerIdentity != null && userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        }
        if (installationIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.INSTALLATION_BY_ID, installationIdentity);
        }
        if (licensedApProIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedApProIdentity);
        }
        if (licensedCirIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedCirIdentity);
        }
        if (licensedCiaIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedCiaIdentity);
        }
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(id = {28501, 28500})
    @Description("Flag sandboxEnabled is not persisted when Sandbox deployment does not exist")
    public void sandboxDeploymentNotExists() {
        setCustomerData();
        cdsTestUtil.createUpdateEnablements(customerIdentity, userIdentity, customerAssignedRole, false, false, false);
        ResponseWrapper<Enablements> getEnablements = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_ENABLEMENTS, Enablements.class, HttpStatus.SC_OK, customerIdentity, userIdentity);
        User getUser = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, User.class, HttpStatus.SC_OK, customerIdentity, userIdentity).getResponseEntity();

        soft.assertThat(getEnablements.getResponseEntity().getSandboxEnabled()).isFalse();
        soft.assertThat(getUser.getRoles().toString()).doesNotContain("AP_SANDBOX");

        cdsTestUtil.createUpdateEnablements(customerIdentity, userIdentity, customerAssignedRole, false, true, false);
        ResponseWrapper<Enablements> getUserUpdatedEnablements = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_ENABLEMENTS, Enablements.class, HttpStatus.SC_OK, customerIdentity, userIdentity);
        User getUpdatedUser = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, User.class, HttpStatus.SC_OK, customerIdentity, userIdentity).getResponseEntity();

        soft.assertThat(getUserUpdatedEnablements.getResponseEntity().getSandboxEnabled()).isFalse();
        soft.assertThat(getUpdatedUser.getRoles().toString()).doesNotContain("AP_SANDBOX");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {28936})
    @Description("The flag 'sandboxEnabled' is not persisted when Sandbox installation does not exist")
    public void sandboxInstallationNotExists() {
        setCustomerData();
        cdsTestUtil.addDeployment(customerIdentity, "Sandbox Deployment", siteIdentity, "SANDBOX");
        cdsTestUtil.createUpdateEnablements(customerIdentity, userIdentity, customerAssignedRole, false, true, false);
        ResponseWrapper<Enablements> getUserEnablements = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_ENABLEMENTS, Enablements.class, HttpStatus.SC_OK, customerIdentity, userIdentity);
        User getUser = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, User.class, HttpStatus.SC_OK, customerIdentity, userIdentity).getResponseEntity();

        soft.assertThat(getUserEnablements.getResponseEntity().getSandboxEnabled()).isFalse();
        soft.assertThat(getUser.getRoles().toString()).doesNotContain("AP_SANDBOX");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {28499, 28494})
    @Description("Flag 'sandboxEnabled' is persisted when Sandbox deployment exists and changed to 'false' when delete sandbox installation")
    public void sandboxDeploymentExists() {
        setCustomerData();
        createSandboxDeployment();
        cdsTestUtil.createUpdateEnablements(customerIdentity, userIdentity, customerAssignedRole, false, true, false);
        ResponseWrapper<Enablements> getUserEnablements = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_ENABLEMENTS, Enablements.class, HttpStatus.SC_OK, customerIdentity, userIdentity);
        User getUser = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, User.class, HttpStatus.SC_OK, customerIdentity, userIdentity).getResponseEntity();

        soft.assertThat(getUserEnablements.getResponseEntity().getSandboxEnabled()).isTrue();
        soft.assertThat(getUser.getRoles().toString()).contains("AP_SANDBOX");

        cdsTestUtil.delete(CDSAPIEnum.INSTALLATION_BY_ID, sandboxInstallationIdentity);
        ResponseWrapper<Enablements> getUpdatedEnablements = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_ENABLEMENTS, Enablements.class, HttpStatus.SC_OK, customerIdentity, userIdentity);
        User getUpdatedUser = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, User.class, HttpStatus.SC_OK, customerIdentity, userIdentity).getResponseEntity();

        soft.assertThat(getUpdatedEnablements.getResponseEntity().getSandboxEnabled()).isFalse();
        soft.assertThat(getUpdatedUser.getRoles().toString()).doesNotContain("AP_SANDBOX");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {28496, 28498})
    @Description("Flag 'previewEnabled is not persisted when Preview deployment doesn't exist")
    public void previewDeploymentNotExists() {
        setCustomerData();
        cdsTestUtil.createUpdateEnablements(customerIdentity, userIdentity, customerAssignedRole, false, false, false);
        ResponseWrapper<Enablements> getEnablements = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_ENABLEMENTS, Enablements.class, HttpStatus.SC_OK, customerIdentity, userIdentity);
        User getUser = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, User.class, HttpStatus.SC_OK, customerIdentity, userIdentity).getResponseEntity();

        soft.assertThat(getEnablements.getResponseEntity().getPreviewEnabled()).isFalse();
        soft.assertThat(getUser.getRoles().toString()).doesNotContain("AP_PREVIEW");

        cdsTestUtil.createUpdateEnablements(customerIdentity, userIdentity, customerAssignedRole, false, false, true);
        ResponseWrapper<Enablements> getUserUpdatedEnablements = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_ENABLEMENTS, Enablements.class, HttpStatus.SC_OK, customerIdentity, userIdentity);
        User getUpdatedUser = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, User.class, HttpStatus.SC_OK, customerIdentity, userIdentity).getResponseEntity();

        soft.assertThat(getUserUpdatedEnablements.getResponseEntity().getPreviewEnabled()).isFalse();
        soft.assertThat(getUpdatedUser.getRoles().toString()).doesNotContain("AP_PREVIEW");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {28937})
    @Description("The flag 'previewEnabled' is not persisted when Preview installation does not exist")
    public void previewInstallationNotExists() {
        setCustomerData();
        cdsTestUtil.addDeployment(customerIdentity, "Preview Deployment", siteIdentity, "PREVIEW");
        cdsTestUtil.createUpdateEnablements(customerIdentity, userIdentity, customerAssignedRole, false, false, true);
        ResponseWrapper<Enablements> getUserEnablements = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_ENABLEMENTS, Enablements.class, HttpStatus.SC_OK, customerIdentity, userIdentity);
        User getUser = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, User.class, HttpStatus.SC_OK, customerIdentity, userIdentity).getResponseEntity();

        soft.assertThat(getUserEnablements.getResponseEntity().getPreviewEnabled()).isFalse();
        soft.assertThat(getUser.getRoles().toString()).doesNotContain("AP_PREVIEW");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {28495, 28493, 28497})
    @Description("Validate when user is not APRIORI_DEVELOPER, when user is APRIORI_DEVELOPER and preview deployment exists, when delete installation")
    public void previewDeploymentExists() {
        setCustomerData();
        createPreviewDeployment();
        cdsTestUtil.createUpdateEnablements(customerIdentity, userIdentity, "APRIORI_DESIGNER", false, false, true);
        ResponseWrapper<Enablements> notDeveloperEnablements = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_ENABLEMENTS, Enablements.class, HttpStatus.SC_OK, customerIdentity, userIdentity);
        User getUserNotDeveloper = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, User.class, HttpStatus.SC_OK, customerIdentity, userIdentity).getResponseEntity();

        soft.assertThat(notDeveloperEnablements.getResponseEntity().getPreviewEnabled()).isFalse();
        soft.assertThat(getUserNotDeveloper.getRoles().toString()).doesNotContain("AP_PREVIEW");

        cdsTestUtil.createUpdateEnablements(customerIdentity, userIdentity, customerAssignedRole, false, false, true);
        ResponseWrapper<Enablements> getEnablements = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_ENABLEMENTS, Enablements.class, HttpStatus.SC_OK, customerIdentity, userIdentity);
        User getUser = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, User.class, HttpStatus.SC_OK, customerIdentity, userIdentity).getResponseEntity();

        soft.assertThat(getEnablements.getResponseEntity().getPreviewEnabled()).isTrue();
        soft.assertThat(getUser.getRoles().toString()).contains("AP_PREVIEW");

        cdsTestUtil.delete(CDSAPIEnum.INSTALLATION_BY_ID, previewInstallationIdentity);
        ResponseWrapper<Enablements> getUpdatedEnablements = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_ENABLEMENTS, Enablements.class, HttpStatus.SC_OK, customerIdentity, userIdentity);
        User getUpdatedUser = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, User.class, HttpStatus.SC_OK, customerIdentity, userIdentity).getResponseEntity();

        soft.assertThat(getUpdatedEnablements.getResponseEntity().getPreviewEnabled()).isFalse();
        soft.assertThat(getUpdatedUser.getRoles().toString()).doesNotContain("AP_PREVIEW");
        soft.assertAll();
    }

    private void setCustomerData() {
        RandomCustomerData rcd = new RandomCustomerData();
        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(rcd.getCustomerName(), rcd.getCustomerType(), rcd.getCloudRef(), rcd.getSalesForceId(), rcd.getEmailPattern());
        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, rcd.getSiteName(), rcd.getSiteID());
        siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, "Production Deployment", siteIdentity, "PRODUCTION");
        String deploymentIdentity = response.getResponseEntity().getIdentity();

        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, "Automation Installation", rcd.getRealmKey(), rcd.getCloudRef(), siteIdentity, false);
        installationIdentity = installation.getResponseEntity().getIdentity();

        ResponseWrapper<LicensedApplications> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        licensedApProIdentity = licensedApp.getResponseEntity().getIdentity();
        ResponseWrapper<LicensedApplications> ciaLicensed = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, ciaIdentity);
        licensedCiaIdentity = ciaLicensed.getResponseEntity().getIdentity();
        ResponseWrapper<LicensedApplications> cirLicensed = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, cirIdentity);
        licensedCirIdentity = cirLicensed.getResponseEntity().getIdentity();

        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, appIdentity, siteIdentity);
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, ciaIdentity, siteIdentity);
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, cirIdentity, siteIdentity);

        userName = generateStringUtil.generateUserName();
        user = cdsTestUtil.addUser(customerIdentity, userName, customer.getResponseEntity().getName());
        userIdentity = user.getResponseEntity().getIdentity();
    }

    private void createSandboxDeployment() {
        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, "Sandbox Deployment", siteIdentity, "SANDBOX");
        String deploymentSandboxIdentity = response.getResponseEntity().getIdentity();
        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentSandboxIdentity, "Sandbox Installation", generateStringUtil.generateRealmKey(), generateStringUtil.generateCloudReference(), siteIdentity, false);
        sandboxInstallationIdentity = installation.getResponseEntity().getIdentity();

        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentSandboxIdentity, sandboxInstallationIdentity, appIdentity, siteIdentity);
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentSandboxIdentity, sandboxInstallationIdentity, ciaIdentity, siteIdentity);
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentSandboxIdentity, sandboxInstallationIdentity, cirIdentity, siteIdentity);
    }

    private void createPreviewDeployment() {
        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, "Preview Deployment", siteIdentity, "PREVIEW");
        String deploymentPreviewIdentity = response.getResponseEntity().getIdentity();
        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentPreviewIdentity, "Preview Installation", generateStringUtil.generateRealmKey(), generateStringUtil.generateCloudReference(), siteIdentity, false);
        previewInstallationIdentity = installation.getResponseEntity().getIdentity();

        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentPreviewIdentity, previewInstallationIdentity, appIdentity, siteIdentity);
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentPreviewIdentity, previewInstallationIdentity, ciaIdentity, siteIdentity);
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentPreviewIdentity, previewInstallationIdentity, cirIdentity, siteIdentity);
    }
}