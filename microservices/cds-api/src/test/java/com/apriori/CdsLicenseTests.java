package com.apriori;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.models.IdentityHolder;
import com.apriori.cds.models.response.ActiveLicenseModules;
import com.apriori.cds.models.response.CdsErrorResponse;
import com.apriori.cds.models.response.InstallationItems;
import com.apriori.cds.models.response.LicenseResponse;
import com.apriori.cds.models.response.Licenses;
import com.apriori.cds.models.response.SubLicense;
import com.apriori.cds.models.response.SubLicenseAssociation;
import com.apriori.cds.models.response.SubLicenseAssociationUser;
import com.apriori.cds.models.response.SubLicenses;
import com.apriori.cds.models.response.User;
import com.apriori.cds.models.response.UsersLicensing;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.response.Customer;
import com.apriori.models.response.Deployment;
import com.apriori.models.response.LicensedApplications;
import com.apriori.models.response.Site;
import com.apriori.properties.PropertiesContext;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.UUID;
import java.util.stream.Collectors;

@ExtendWith(TestRulesAPI.class)
public class CdsLicenseTests {
    private SoftAssertions soft = new SoftAssertions();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private ResponseWrapper<Customer> customer;
    private ResponseWrapper<Site> site;
    private ResponseWrapper<LicenseResponse> license;
    private String customerName;
    private String cloudRef;
    private String salesForceId;
    private String emailPattern;
    private String customerIdentity;
    private String siteName;
    private String siteId;
    private String siteIdentity;
    private String licenseId;
    private String subLicenseId;
    private String licenseIdentity;
    private String subLicenseIdentity;
    private IdentityHolder deleteIdentityHolder;
    private IdentityHolder userIdentityHolder;
    private String deploymentIdentity;
    private String licensedApplicationIdentity;
    private String installationIdentity;

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
        siteId = generateStringUtil.generateSiteID();
        site = cdsTestUtil.addSite(customerIdentity, siteName, siteId);
        siteIdentity = site.getResponseEntity().getIdentity();

        licenseId = UUID.randomUUID().toString();
        subLicenseId = UUID.randomUUID().toString();

        license = cdsTestUtil.addLicense(customerIdentity, siteIdentity, customerName, siteId, licenseId, subLicenseId);
        licenseIdentity = license.getResponseEntity().getIdentity();
        subLicenseIdentity = license.getResponseEntity().getSubLicenses().get(1).getIdentity();
    }

    @AfterEach
    public void cleanUp() {
        if (deleteIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.SUBLICENSE_ASSOCIATIONS_USER_BY_ID,
                deleteIdentityHolder.customerIdentity(),
                deleteIdentityHolder.siteIdentity(),
                deleteIdentityHolder.licenseIdentity(),
                deleteIdentityHolder.subLicenseIdentity(),
                deleteIdentityHolder.userIdentity()
            );
        }
        if (installationIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.INSTALLATION_BY_ID, installationIdentity);
        }
        if (licensedApplicationIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedApplicationIdentity);
        }
        if (userIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS,
                userIdentityHolder.customerIdentity(),
                userIdentityHolder.userIdentity()
            );
        }
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(id = {5313})
    @Description("Get list of licenses for customer")
    public void getCustomerLicense() {

        ResponseWrapper<Licenses> license = cdsTestUtil.getCommonRequest(CDSAPIEnum.LICENSES_BY_CUSTOMER_ID,
            Licenses.class,
            HttpStatus.SC_OK,
            customerIdentity
        );

        soft.assertThat(license.getResponseEntity().getTotalItemCount()).isEqualTo(1);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5968})
    @Description("Get list of licenses for customer")
    public void getCustomerLicenseByIdentity() {
        ResponseWrapper<LicenseResponse> licenseResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.SPECIFIC_LICENSE_BY_CUSTOMER_LICENSE_ID,
            LicenseResponse.class,
            HttpStatus.SC_OK,
            customerIdentity,
            licenseIdentity
        );
        soft.assertThat(licenseResponse.getResponseEntity().getIdentity()).isEqualTo(licenseIdentity);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {6623})
    @Description("Activate a license.")
    public void activateLicenseTest() {
        String userIdentity = PropertiesContext.get("user_identity");
        cdsTestUtil.activateLicense(customerIdentity, siteIdentity, licenseIdentity, userIdentity);
        ResponseWrapper<LicenseResponse> license = cdsTestUtil.getCommonRequest(CDSAPIEnum.SPECIFIC_LICENSE_BY_CUSTOMER_LICENSE_ID,
            LicenseResponse.class,
            HttpStatus.SC_OK,
            customerIdentity,
            licenseIdentity);

        soft.assertThat(license.getResponseEntity().getActive()).isTrue();
        soft.assertAll();
    }

    @Test
    @TestRail(id = {13301})
    @Description("Returns a list of licenses for a specific customer site.")
    public void getLicensesOfTheSite() {

        ResponseWrapper<Licenses> siteLicenses = cdsTestUtil.getCommonRequest(CDSAPIEnum.LICENSE_BY_CUSTOMER_SITE_IDS,
            Licenses.class,
            HttpStatus.SC_OK,
            customerIdentity,
            siteIdentity
        );
        soft.assertThat(siteLicenses.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {13302})
    @Description("Returns a specific license for a specific customer site")
    public void getLicenseOfSiteById() {
        ResponseWrapper<LicenseResponse> licenseById = cdsTestUtil.getCommonRequest(CDSAPIEnum.LICENSE_BY_CUSTOMER_SITE_LICENSE_IDS,
            LicenseResponse.class,
            HttpStatus.SC_OK,
            customerIdentity,
            siteIdentity,
            licenseIdentity
        );

        soft.assertThat(licenseById.getResponseEntity().getIdentity()).isEqualTo(licenseIdentity);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {24043})
    @Description("Get a list of active licensed sub-modules")
    public void getActiveModules() {
        String userIdentity = PropertiesContext.get("user_identity");
        cdsTestUtil.activateLicense(customerIdentity, siteIdentity, licenseIdentity, userIdentity);
        ResponseWrapper<ActiveLicenseModules> activeModules = cdsTestUtil.getCommonRequest(CDSAPIEnum.ACTIVE_MODULES, ActiveLicenseModules.class, HttpStatus.SC_OK, customerIdentity, siteIdentity);
        soft.assertThat(activeModules.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertThat(activeModules.getResponseEntity().getItems().get(0).getName()).isNotEmpty();
        soft.assertAll();
    }

    @Test
    @TestRail(id = {24044})
    @Description("Get a list of modules for inactive license")
    public void getModulesInactiveLicense() {
        ResponseWrapper<CdsErrorResponse> notActiveLicenseModules = cdsTestUtil.getCommonRequest(CDSAPIEnum.ACTIVE_MODULES, CdsErrorResponse.class, HttpStatus.SC_NOT_FOUND, customerIdentity, siteIdentity);
        soft.assertThat(notActiveLicenseModules.getResponseEntity().getMessage()).isEqualTo(String.format("Site, '%s', does not have an active license", siteIdentity));
        soft.assertAll();
    }

    @Test
    @TestRail(id = {6641})
    @Description("Get a sub license")
    public void getSubLicenses() {
        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<Deployment> deployment = cdsTestUtil.addDeployment(customerIdentity, "Production Deployment", siteIdentity, "PRODUCTION");
        deploymentIdentity = deployment.getResponseEntity().getIdentity();
        String realmKey = generateStringUtil.generateRealmKey();
        String appIdentity = Constants.getApProApplicationIdentity();
        ResponseWrapper<LicensedApplications> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        licensedApplicationIdentity = licensedApp.getResponseEntity().getIdentity();
        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, "Automation Installation", realmKey, cloudRef, siteIdentity, false);
        installationIdentity = installation.getResponseEntity().getIdentity();
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, appIdentity, siteIdentity);

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getIdentity();
        userIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .build();

        ResponseWrapper<SubLicenses> subLicense = cdsTestUtil.getCommonRequest(CDSAPIEnum.SUB_LICENSES,
            SubLicenses.class,
            HttpStatus.SC_OK,
            customerIdentity,
            siteIdentity,
            licenseIdentity
        );

        soft.assertThat(subLicense.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {6642})
    @Description("Get a sub license by Identity")
    public void getSubLicenseIdentity() {
        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<Deployment> deployment = cdsTestUtil.addDeployment(customerIdentity, "Production Deployment", siteIdentity, "PRODUCTION");
        deploymentIdentity = deployment.getResponseEntity().getIdentity();
        String realmKey = generateStringUtil.generateRealmKey();
        String appIdentity = Constants.getApProApplicationIdentity();
        ResponseWrapper<LicensedApplications> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        licensedApplicationIdentity = licensedApp.getResponseEntity().getIdentity();
        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, "Automation Installation", realmKey, cloudRef, siteIdentity, false);
        installationIdentity = installation.getResponseEntity().getIdentity();
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, appIdentity, siteIdentity);

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getIdentity();
        userIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .build();

        ResponseWrapper<SubLicense> subLicense = cdsTestUtil.getCommonRequest(CDSAPIEnum.SPECIFIC_SUB_LICENSE,
            SubLicense.class,
            HttpStatus.SC_OK,
            customerIdentity,
            siteIdentity,
            licenseIdentity,
            subLicenseIdentity
        );

        soft.assertThat(subLicense.getResponseEntity().getName()).contains("License");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {6643})
    @Description("Adds a user sub-license association")
    public void addUserSubLicense() {
        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<Deployment> deployment = cdsTestUtil.addDeployment(customerIdentity, "Production Deployment", siteIdentity, "PRODUCTION");
        deploymentIdentity = deployment.getResponseEntity().getIdentity();
        String realmKey = generateStringUtil.generateRealmKey();
        String appIdentity = Constants.getApProApplicationIdentity();
        ResponseWrapper<LicensedApplications> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        licensedApplicationIdentity = licensedApp.getResponseEntity().getIdentity();
        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, "Automation Installation", realmKey, cloudRef, siteIdentity, false);
        installationIdentity = installation.getResponseEntity().getIdentity();
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, appIdentity, siteIdentity);

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getIdentity();
        userIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .build();

        ResponseWrapper<LicenseResponse> licenseResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.SPECIFIC_LICENSE_BY_CUSTOMER_LICENSE_ID,
            LicenseResponse.class,
            HttpStatus.SC_OK,
            customerIdentity,
            licenseIdentity
        );
        String subLicenseIdentity = licenseResponse.getResponseEntity().getSubLicenses().stream()
            .filter(x -> !x.getName().contains("master"))
            .collect(Collectors.toList()).get(0).getIdentity();

        ResponseWrapper<SubLicenseAssociationUser> associationUserItemsResponse = cdsTestUtil.addSubLicenseAssociationUser(customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity);

        soft.assertThat(associationUserItemsResponse.getResponseEntity().getCreatedBy()).isEqualTo("#SYSTEM00000");
        soft.assertAll();

        deleteIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .siteIdentity(siteIdentity)
            .licenseIdentity(licenseIdentity)
            .subLicenseIdentity(subLicenseIdentity)
            .userIdentity(userIdentity)
            .build();
    }

    @Test
    @TestRail(id = {6644})
    @Description("Gets a list of users with a sub-license association")
    public void getUsersAssociatedWithSubLicense() {
        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<Deployment> deployment = cdsTestUtil.addDeployment(customerIdentity, "Production Deployment", siteIdentity, "PRODUCTION");
        deploymentIdentity = deployment.getResponseEntity().getIdentity();
        String realmKey = generateStringUtil.generateRealmKey();
        String appIdentity = Constants.getApProApplicationIdentity();
        ResponseWrapper<LicensedApplications> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        licensedApplicationIdentity = licensedApp.getResponseEntity().getIdentity();
        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, "Automation Installation", realmKey, cloudRef, siteIdentity, false);
        installationIdentity = installation.getResponseEntity().getIdentity();
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, appIdentity, siteIdentity);

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getIdentity();
        userIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .build();

        ResponseWrapper<LicenseResponse> licenseResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.SPECIFIC_LICENSE_BY_CUSTOMER_LICENSE_ID,
            LicenseResponse.class,
            HttpStatus.SC_OK,
            customerIdentity,
            licenseIdentity
        );

        String subLicenseIdentity = licenseResponse.getResponseEntity().getSubLicenses().stream()
            .filter(x -> !x.getName().contains("master"))
            .collect(Collectors.toList()).get(0).getIdentity();

        cdsTestUtil.addSubLicenseAssociationUser(customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity);

        ResponseWrapper<SubLicenseAssociation> associationUserResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.SUBLICENSE_ASSOCIATIONS_USERS,
            SubLicenseAssociation.class,
            HttpStatus.SC_OK,
            customerIdentity,
            siteIdentity,
            licenseIdentity,
            subLicenseIdentity
        );

        soft.assertThat(associationUserResponse.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertAll();

        deleteIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .siteIdentity(siteIdentity)
            .licenseIdentity(licenseIdentity)
            .subLicenseIdentity(subLicenseIdentity)
            .userIdentity(userIdentity)
            .build();
    }

    @Test
    @TestRail(id = {13303})
    @Description("Returns a list of sites, licenses, and sub-licenses that the user is associated with")
    public void getUsersLicenses() {
        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<Deployment> deployment = cdsTestUtil.addDeployment(customerIdentity, "Production Deployment", siteIdentity, "PRODUCTION");
        deploymentIdentity = deployment.getResponseEntity().getIdentity();
        String realmKey = generateStringUtil.generateRealmKey();
        String appIdentity = Constants.getApProApplicationIdentity();
        ResponseWrapper<LicensedApplications> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        licensedApplicationIdentity = licensedApp.getResponseEntity().getIdentity();
        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, "Automation Installation", realmKey, cloudRef, siteIdentity, false);
        installationIdentity = installation.getResponseEntity().getIdentity();
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, appIdentity, siteIdentity);

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getIdentity();
        userIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .build();

        ResponseWrapper<LicenseResponse> licenseResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.SPECIFIC_LICENSE_BY_CUSTOMER_LICENSE_ID,
            LicenseResponse.class,
            HttpStatus.SC_OK,
            customerIdentity,
            licenseIdentity
        );

        String subLicenseIdentity = licenseResponse.getResponseEntity().getSubLicenses().stream()
            .filter(x -> !x.getName().contains("master"))
            .collect(Collectors.toList()).get(0).getIdentity();

        cdsTestUtil.addSubLicenseAssociationUser(customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity);

        ResponseWrapper<UsersLicensing> licensing = cdsTestUtil.getCommonRequest(CDSAPIEnum.USERS_LICENSES,
            UsersLicensing.class,
            HttpStatus.SC_OK,
            customerIdentity,
            userIdentity
        );

        soft.assertThat(licensing.getResponseEntity().getResponse().get(0).getSubLicenseIdentity()).isEqualTo(subLicenseIdentity);
        soft.assertAll();

        deleteIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .siteIdentity(siteIdentity)
            .licenseIdentity(licenseIdentity)
            .subLicenseIdentity(subLicenseIdentity)
            .userIdentity(userIdentity)
            .build();
    }

    @Test
    @TestRail(id = {6145})
    @Description("Deletes an existing user sub-license association")
    public void deleteCustomerSubLicense() {
        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<Deployment> deployment = cdsTestUtil.addDeployment(customerIdentity, "Production Deployment", siteIdentity, "PRODUCTION");
        deploymentIdentity = deployment.getResponseEntity().getIdentity();
        String realmKey = generateStringUtil.generateRealmKey();
        String appIdentity = Constants.getApProApplicationIdentity();
        ResponseWrapper<LicensedApplications> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        licensedApplicationIdentity = licensedApp.getResponseEntity().getIdentity();
        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, "Automation Installation", realmKey, cloudRef, siteIdentity, false);
        installationIdentity = installation.getResponseEntity().getIdentity();
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, appIdentity, siteIdentity);

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getIdentity();
        userIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .build();

        ResponseWrapper<LicenseResponse> licenseResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.SPECIFIC_LICENSE_BY_CUSTOMER_LICENSE_ID,
            LicenseResponse.class,
            HttpStatus.SC_OK,
            customerIdentity,
            licenseIdentity
        );

        String subLicenseIdentity = licenseResponse.getResponseEntity().getSubLicenses().stream()
            .filter(x -> !x.getName().contains("master"))
            .collect(Collectors.toList()).get(0).getIdentity();

        cdsTestUtil.addSubLicenseAssociationUser(customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity);

        cdsTestUtil.delete(CDSAPIEnum.SUBLICENSE_ASSOCIATIONS_USER_BY_ID,
            customerIdentity,
            siteIdentity,
            licenseIdentity,
            subLicenseIdentity,
            userIdentity
        );
    }
}
