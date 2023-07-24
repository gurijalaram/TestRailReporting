package com.apriori.tests;

import com.apriori.entity.response.Customer;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.cas.utils.Constants;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.entity.IdentityHolder;
import com.apriori.entity.response.AssociationUser;
import com.apriori.entity.response.CasErrorMessage;
import com.apriori.entity.response.CustomerUser;
import com.apriori.entity.response.LicenseResponse;
import com.apriori.entity.response.Site;
import com.apriori.entity.response.SubLicenses;
import com.apriori.entity.response.SublicenseAssociation;
import com.apriori.entity.response.UserLicensing;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.UUID;

public class CasUserSubLicensesTests {
    private SoftAssertions soft = new SoftAssertions();
    private IdentityHolder deleteIdentityHolder;
    private String customerName;
    private String customerIdentity;
    private String cloudRef;
    private String email;
    private String description;
    private String userName;
    private String userIdentity;
    private String siteName;
    private String siteID;
    private String siteIdentity;
    private CasTestUtil casTestUtil = new CasTestUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    @Before
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
        customerName = generateStringUtil.generateCustomerName();
        cloudRef = generateStringUtil.generateCloudReference();
        email = customerName.toLowerCase();
        description = customerName + " Description";
        userName = generateStringUtil.generateUserName();
        siteName = generateStringUtil.generateSiteName();
        siteID = generateStringUtil.generateSiteID();

        ResponseWrapper<Customer> customer = CasTestUtil.addCustomer(customerName, cloudRef, description, email);
        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<CustomerUser> user = CasTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();

        ResponseWrapper<Site> site = CasTestUtil.addSite(customerIdentity, siteID, siteName);
        siteIdentity = site.getResponseEntity().getIdentity();
    }

    @After
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
    @TestRail(testCaseId = {"10877"})
    @Description("Make sure users cannot be assigned to a sublicense under an expired license")
    public void expiredLicense() {
        String subLicenseId = UUID.randomUUID().toString();

        ResponseWrapper<LicenseResponse> licenseResponse = casTestUtil.addLicense(Constants.CAS_EXPIRED_LICENSE, customerIdentity, siteIdentity, customerName, siteID, subLicenseId);
        String licenseIdentity = licenseResponse.getResponseEntity().getIdentity();
        String subLicenseIdentity = licenseResponse.getResponseEntity().getSubLicenses().get(0).getIdentity();
        LocalDate expireDate = licenseResponse.getResponseEntity().getSubLicenses().get(0).getExpiresAt();

        ResponseWrapper<CasErrorMessage> associationErrorResponse = casTestUtil.addSubLicenseAssociationUser(CasErrorMessage.class, customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity, HttpStatus.SC_CONFLICT);

        soft.assertThat(associationErrorResponse.getResponseEntity().getMessage())
            .isEqualTo(String.format("Sub License with identity '%s' expired on '%s' and cannot be assigned to a user.", subLicenseIdentity, expireDate));
        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10878"})
    @Description("Make sure users cannot be assigned to a masterLicense")
    public void masterLicense() {
        String subLicenseId = UUID.randomUUID().toString();

        ResponseWrapper<LicenseResponse> licenseResponse = casTestUtil.addLicense(Constants.CAS_MASTER_LICENSE, customerIdentity, siteIdentity, customerName, siteID, subLicenseId);
        String licenseIdentity = licenseResponse.getResponseEntity().getIdentity();
        String subLicenseIdentity = licenseResponse.getResponseEntity().getSubLicenses().get(0).getIdentity();

        ResponseWrapper<CasErrorMessage> associationErrorResponse = casTestUtil.addSubLicenseAssociationUser(CasErrorMessage.class, customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity, HttpStatus.SC_CONFLICT);

        soft.assertThat(associationErrorResponse.getResponseEntity().getMessage())
            .isEqualTo(String.format("Error assigning sub license 'masterLicense' to user with identity '%s'. Sub license can only be assigned to an 'aPriori Internal' user.", userIdentity));
        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10879"})
    @Description("Make sure users cannot be assigned to a masterLicense")
    public void aPrioriInternalLicense() {
        String subLicenseId = UUID.randomUUID().toString();

        ResponseWrapper<LicenseResponse> licenseResponse = casTestUtil.addLicense(Constants.CAS_APRIORI_INTERNAL_LICENSE, customerIdentity, siteIdentity, customerName, siteID, subLicenseId);
        String licenseIdentity = licenseResponse.getResponseEntity().getIdentity();
        String subLicenseIdentity = licenseResponse.getResponseEntity().getSubLicenses().get(0).getIdentity();

        ResponseWrapper<CasErrorMessage> associationErrorResponse = casTestUtil.addSubLicenseAssociationUser(CasErrorMessage.class, customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity, HttpStatus.SC_CONFLICT);

        soft.assertThat(associationErrorResponse.getResponseEntity().getMessage())
            .isEqualTo(String.format("Error assigning sub license 'aPrioriInternalLicense' to user with identity '%s'. Sub license can only be assigned to an 'aPriori Internal' user.", userIdentity));
        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5652", "5655", "5676", "5677", "5678"})
    @Description("Validate that user can be added to a sub license")
    public void assignSublicenseToUser() {
        String subLicenseId = UUID.randomUUID().toString();

        ResponseWrapper<LicenseResponse> licenseResponse = casTestUtil.addLicense(Constants.CAS_LICENSE, customerIdentity, siteIdentity, customerName, siteID, subLicenseId);
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
    @TestRail(testCaseId = {"13105"})
    @Description("Returns assigned sub license information for the specified user")
    public void getUsersSublicense() {
        String subLicenseId = UUID.randomUUID().toString();

        ResponseWrapper<LicenseResponse> licenseResponse = casTestUtil.addLicense(Constants.CAS_LICENSE, customerIdentity, siteIdentity, customerName, siteID, subLicenseId);
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
}