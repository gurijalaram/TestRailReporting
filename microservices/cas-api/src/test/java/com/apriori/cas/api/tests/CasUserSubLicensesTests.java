package com.apriori.cas.api.tests;

import com.apriori.cas.api.enums.CASAPIEnum;
import com.apriori.cas.api.models.IdentityHolder;
import com.apriori.cas.api.models.response.AssociationUser;
import com.apriori.cas.api.models.response.CasErrorMessage;
import com.apriori.cas.api.models.response.Customer;
import com.apriori.cas.api.models.response.LicenseResponse;
import com.apriori.cas.api.models.response.SubLicenses;
import com.apriori.cas.api.models.response.SublicenseAssociation;
import com.apriori.cas.api.models.response.UserLicensing;
import com.apriori.cas.api.utils.CasTestUtil;
import com.apriori.cas.api.utils.Constants;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.response.InstallationItems;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.RandomCustomerData;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.Deployment;
import com.apriori.shared.util.models.response.LicensedApplications;
import com.apriori.shared.util.models.response.Site;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.util.UUID;

@ExtendWith(TestRulesAPI.class)
public class CasUserSubLicensesTests {
    private final String appIdentity = Constants.getApProApplicationIdentity();
    private final String ciaIdentity = Constants.getCiaApplicationIdentity();
    private final String cirIdentity = Constants.getCirAppIdentity();
    private final String acsIdentity = Constants.getACSAppIdentity();
    private SoftAssertions soft = new SoftAssertions();
    private IdentityHolder deleteIdentityHolder;
    private String customerIdentity;
    private String customerName;
    private String userIdentity;
    private String siteIdentity;
    private String siteId;
    private CasTestUtil casTestUtil = new CasTestUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private UserCredentials currentUser = UserUtil.getUser();
    private String licensedApProIdentity;
    private String licensedCiaIdentity;
    private String licensedCirIdentity;
    private String licensedAcsIdentity;
    private String installationIdentity;

    @BeforeEach
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(currentUser.getToken());
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
        if (licensedApProIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedApProIdentity);
        }
        if (licensedCiaIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedCiaIdentity);
        }
        if (licensedCirIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedCirIdentity);
        }
        if (licensedAcsIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedAcsIdentity);
        }
        if (userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity
            );
        }
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity
            );
        }
    }

    @Test
    @TestRail(id = {10877})
    @Description("Make sure users cannot be assigned to a sublicense under an expired license")
    public void expiredLicense() {
        setCustomerData();
        String subLicenseId = UUID.randomUUID().toString();

        ResponseWrapper<LicenseResponse> licenseResponse = casTestUtil.addLicense(com.apriori.cas.api.utils.Constants.CAS_EXPIRED_LICENSE, customerIdentity, siteIdentity, customerName, siteId, subLicenseId);
        String licenseIdentity = licenseResponse.getResponseEntity().getIdentity();
        String subLicenseIdentity = licenseResponse.getResponseEntity().getSubLicenses().get(0).getIdentity();
        LocalDate expireDate = licenseResponse.getResponseEntity().getSubLicenses().get(0).getExpiresAt();

        ResponseWrapper<CasErrorMessage> associationErrorResponse = casTestUtil.addSubLicenseAssociationUser(CasErrorMessage.class, customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity, HttpStatus.SC_CONFLICT);

        soft.assertThat(associationErrorResponse.getResponseEntity().getMessage())
            .isEqualTo(String.format("Sub License with identity '%s' expired on '%s' and cannot be assigned to a user.", subLicenseIdentity, expireDate));
        soft.assertAll();
    }

    @Test
    @TestRail(id = {10878})
    @Description("Make sure users cannot be assigned to a masterLicense")
    public void masterLicense() {
        setCustomerData();
        String subLicenseId = UUID.randomUUID().toString();

        ResponseWrapper<LicenseResponse> licenseResponse = casTestUtil.addLicense(Constants.CAS_MASTER_LICENSE, customerIdentity, siteIdentity, customerName, siteId, subLicenseId);
        String licenseIdentity = licenseResponse.getResponseEntity().getIdentity();
        String subLicenseIdentity = licenseResponse.getResponseEntity().getSubLicenses().get(0).getIdentity();

        ResponseWrapper<CasErrorMessage> associationErrorResponse = casTestUtil.addSubLicenseAssociationUser(CasErrorMessage.class, customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity, HttpStatus.SC_CONFLICT);

        soft.assertThat(associationErrorResponse.getResponseEntity().getMessage())
            .isEqualTo(String.format("Error assigning sub license 'masterLicense' to user with identity '%s'. Sub license can only be assigned to an 'aPriori Internal' user.", userIdentity));
        soft.assertAll();
    }

    @Test
    @TestRail(id = {10879})
    @Description("Make sure users cannot be assigned to a masterLicense")
    public void aPrioriInternalLicense() {
        setCustomerData();
        String subLicenseId = UUID.randomUUID().toString();

        ResponseWrapper<LicenseResponse> licenseResponse = casTestUtil.addLicense(Constants.CAS_APRIORI_INTERNAL_LICENSE, customerIdentity, siteIdentity, customerName, siteId, subLicenseId);
        String licenseIdentity = licenseResponse.getResponseEntity().getIdentity();
        String subLicenseIdentity = licenseResponse.getResponseEntity().getSubLicenses().get(0).getIdentity();

        ResponseWrapper<CasErrorMessage> associationErrorResponse = casTestUtil.addSubLicenseAssociationUser(CasErrorMessage.class, customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity, HttpStatus.SC_CONFLICT);

        soft.assertThat(associationErrorResponse.getResponseEntity().getMessage())
            .isEqualTo(String.format("Error assigning sub license 'aPrioriInternalLicense' to user with identity '%s'. Sub license can only be assigned to an 'aPriori Internal' user.", userIdentity));
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5652, 5655, 5676, 5677, 5678})
    @Description("Validate that user can be added to a sub license")
    public void assignSublicenseToUser() {
        setCustomerData();
        String subLicenseId = UUID.randomUUID().toString();

        ResponseWrapper<LicenseResponse> licenseResponse = casTestUtil.addLicense(Constants.CAS_LICENSE, customerIdentity, siteIdentity, customerName, siteId, subLicenseId);
        String licenseIdentity = licenseResponse.getResponseEntity().getIdentity();

        ResponseWrapper<SubLicenses> subLicenses = casTestUtil.getCommonRequest(CASAPIEnum.SUBLICENSES_BY_LICENSE_ID, SubLicenses.class, HttpStatus.SC_OK,
            customerIdentity,
            siteIdentity,
            licenseIdentity);

        String subLicenseIdentity = subLicenses.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<AssociationUser> associationUserResponse = casTestUtil.addSubLicenseAssociationUser(AssociationUser.class, customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity, HttpStatus.SC_CREATED);

        soft.assertThat(associationUserResponse.getResponseEntity().getUserIdentity())
            .isEqualTo(userIdentity);

        ResponseWrapper<SublicenseAssociation> sublicenseAssociations = casTestUtil.getCommonRequest(CASAPIEnum.SUBLICENSE_ASSOCIATIONS, SublicenseAssociation.class, HttpStatus.SC_OK,
            customerIdentity,
            siteIdentity,
            licenseIdentity,
            subLicenseIdentity);

        soft.assertThat(sublicenseAssociations.getResponseEntity().getItems().get(0).getIdentity())
            .isEqualTo(userIdentity);
        soft.assertAll();

        casTestUtil.delete(CASAPIEnum.SPECIFIC_USER_SUB_LICENSE_USERS,
            customerIdentity,
            siteIdentity,
            licenseIdentity,
            subLicenseIdentity,
            userIdentity);
    }

    @Test
    @TestRail(id = {13105})
    @Description("Returns assigned sub license information for the specified user")
    public void getUsersSublicense() {
        setCustomerData();
        String subLicenseId = UUID.randomUUID().toString();

        ResponseWrapper<LicenseResponse> licenseResponse = casTestUtil.addLicense(Constants.CAS_LICENSE, customerIdentity, siteIdentity, customerName, siteId, subLicenseId);
        String licenseIdentity = licenseResponse.getResponseEntity().getIdentity();

        ResponseWrapper<SubLicenses> subLicenses = casTestUtil.getCommonRequest(CASAPIEnum.SUBLICENSES_BY_LICENSE_ID, SubLicenses.class, HttpStatus.SC_OK,
            customerIdentity,
            siteIdentity,
            licenseIdentity);

        String subLicenseIdentity = subLicenses.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<AssociationUser> associationUserResponse = casTestUtil.addSubLicenseAssociationUser(AssociationUser.class, customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity, HttpStatus.SC_CREATED);

        soft.assertThat(associationUserResponse.getResponseEntity().getUserIdentity())
            .isEqualTo(userIdentity);

        ResponseWrapper<UserLicensing> usersSubLicenses = casTestUtil.getCommonRequest(CASAPIEnum.USERS_SUBLICENSES, UserLicensing.class, HttpStatus.SC_ACCEPTED, customerIdentity, userIdentity);

        soft.assertThat(usersSubLicenses.getResponseEntity().get(0).getSubLicenseIdentity())
            .isEqualTo(subLicenseIdentity);
        soft.assertAll();

        deleteIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .siteIdentity(siteIdentity)
            .licenseIdentity(licenseIdentity)
            .subLicenseIdentity(subLicenseIdentity)
            .userIdentity(userIdentity)
            .build();
    }

    private void setCustomerData() {
        RandomCustomerData rcd = new RandomCustomerData();
        Customer newCustomer = casTestUtil.createCustomer().getResponseEntity();
        customerIdentity = newCustomer.getIdentity();
        customerName = newCustomer.getName();

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, rcd.getSiteName(), rcd.getSiteID());
        siteIdentity = site.getResponseEntity().getIdentity();
        siteId = site.getResponseEntity().getSiteId();

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
        ResponseWrapper<LicensedApplications> acsLicensed = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, acsIdentity);
        licensedAcsIdentity = acsLicensed.getResponseEntity().getIdentity();

        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, appIdentity, siteIdentity);
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, ciaIdentity, siteIdentity);
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, cirIdentity, siteIdentity);
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, acsIdentity, siteIdentity);

        ResponseWrapper<User> user = casTestUtil.createUser(newCustomer);
        userIdentity = user.getResponseEntity().getIdentity();
    }
}