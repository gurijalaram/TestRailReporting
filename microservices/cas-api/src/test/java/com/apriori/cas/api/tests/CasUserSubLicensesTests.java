package com.apriori.cas.api.tests;

import static com.apriori.shared.util.enums.RolesEnum.APRIORI_DEVELOPER;

import com.apriori.cas.api.enums.CASAPIEnum;
import com.apriori.cas.api.models.IdentityHolder;
import com.apriori.cas.api.models.response.AssociationUser;
import com.apriori.cas.api.models.response.CasErrorMessage;
import com.apriori.cas.api.models.response.Customer;
import com.apriori.cas.api.models.response.LicenseResponse;
import com.apriori.cas.api.models.response.Sites;
import com.apriori.cas.api.models.response.SubLicenses;
import com.apriori.cas.api.models.response.SublicenseAssociation;
import com.apriori.cas.api.models.response.UserLicensing;
import com.apriori.cas.api.utils.CasTestUtil;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.CustomerInfrastructure;
import com.apriori.cds.api.utils.RandomCustomerData;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import lombok.SneakyThrows;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.extension.ExtendWith;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.UUID;

@ExtendWith(TestRulesAPI.class)
@EnabledIf(value = "com.apriori.shared.util.properties.PropertiesContext#isAPCustomer")
public class CasUserSubLicensesTests {
    private CustomerInfrastructure customerInfrastructure;
    private SoftAssertions soft = new SoftAssertions();
    private IdentityHolder deleteIdentityHolder;
    private String customerIdentity;
    private String customerName;
    private String userIdentity;
    private String siteIdentity;
    private String siteId;
    private CasTestUtil casTestUtil = new CasTestUtil();
    private CdsTestUtil cdsTestUtil;
    private UserCredentials currentUser = UserUtil.getUser(APRIORI_DEVELOPER);
    private String casLicense;
    private String casExpiredLicense;
    private String casApInternalLicense;
    private String casMasterLicense;

    @SneakyThrows
    @BeforeEach
    public void init() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        cdsTestUtil = new CdsTestUtil(requestEntityUtil);
        customerInfrastructure = new CustomerInfrastructure(requestEntityUtil);

        casLicense = new String(FileResourceUtil.getResourceFileStream("CasLicense.xml").readAllBytes(), StandardCharsets.UTF_8);
        casExpiredLicense = new String(FileResourceUtil.getResourceFileStream("CasExpiredLicense.xml").readAllBytes(), StandardCharsets.UTF_8);
        casApInternalLicense = new String(FileResourceUtil.getResourceFileStream("CasApInternalLicense.xml").readAllBytes(), StandardCharsets.UTF_8);
        casMasterLicense = new String(FileResourceUtil.getResourceFileStream("CasMasterLicense.xml").readAllBytes(), StandardCharsets.UTF_8);
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
        if (userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity
            );
        }
        customerInfrastructure.cleanUpCustomerInfrastructure(customerIdentity);
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

        ResponseWrapper<LicenseResponse> licenseResponse = casTestUtil.addLicense(casExpiredLicense, customerIdentity, siteIdentity, customerName, siteId, subLicenseId);
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

        ResponseWrapper<LicenseResponse> licenseResponse = casTestUtil.addLicense(casMasterLicense, customerIdentity, siteIdentity, customerName, siteId, subLicenseId);
        String licenseIdentity = licenseResponse.getResponseEntity().getIdentity();
        String subLicenseIdentity = licenseResponse.getResponseEntity().getSubLicenses().get(0).getIdentity();

        ResponseWrapper<CasErrorMessage> associationErrorResponse = casTestUtil.addSubLicenseAssociationUser(CasErrorMessage.class, customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity, HttpStatus.SC_CONFLICT);

        soft.assertThat(associationErrorResponse.getResponseEntity().getMessage())
            .isEqualTo(String.format("Error assigning sub license 'masterLicense' to user with identity '%s'. Sub license can only be assigned to an 'aPriori Internal' user.", userIdentity));
        soft.assertAll();
    }

    @Test
    @TestRail(id = {10879})
    @Description("Make sure users cannot be assigned to aP Internal License")
    public void aprioriInternalLicense() {
        setCustomerData();
        String subLicenseId = UUID.randomUUID().toString();

        ResponseWrapper<LicenseResponse> licenseResponse = casTestUtil.addLicense(casApInternalLicense, customerIdentity, siteIdentity, customerName, siteId, subLicenseId);
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

        ResponseWrapper<LicenseResponse> licenseResponse = casTestUtil.addLicense(casLicense, customerIdentity, siteIdentity, customerName, siteId, subLicenseId);
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

        ResponseWrapper<LicenseResponse> licenseResponse = casTestUtil.addLicense(casLicense, customerIdentity, siteIdentity, customerName, siteId, subLicenseId);
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

        customerInfrastructure.createCustomerInfrastructure(rcd, customerIdentity);
        ResponseWrapper<Sites> customerSites = casTestUtil.getCommonRequest(CASAPIEnum.SITES, Sites.class, HttpStatus.SC_OK, customerIdentity);
        siteIdentity = customerSites.getResponseEntity().getItems().get(0).getIdentity();
        siteId = customerSites.getResponseEntity().getItems().get(0).getSiteId();

        ResponseWrapper<User> user = casTestUtil.createUser(newCustomer);
        userIdentity = user.getResponseEntity().getIdentity();
    }
}