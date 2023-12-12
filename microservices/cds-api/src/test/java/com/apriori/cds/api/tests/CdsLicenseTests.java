package com.apriori.cds.api.tests;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.IdentityHolder;
import com.apriori.cds.api.models.response.ActiveLicenseModules;
import com.apriori.cds.api.models.response.CdsErrorResponse;
import com.apriori.cds.api.models.response.LicenseResponse;
import com.apriori.cds.api.models.response.Licenses;
import com.apriori.cds.api.models.response.SubLicense;
import com.apriori.cds.api.models.response.SubLicenseAssociation;
import com.apriori.cds.api.models.response.SubLicenseAssociationUser;
import com.apriori.cds.api.models.response.SubLicenses;
import com.apriori.cds.api.models.response.UsersLicensing;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.CustomerInfrastructure;
import com.apriori.cds.api.utils.RandomCustomerData;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.Sites;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.properties.PropertiesContext;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.UUID;
import java.util.stream.Collectors;

@ExtendWith(TestRulesAPI.class)
public class CdsLicenseTests {
    private SoftAssertions soft = new SoftAssertions();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private ResponseWrapper<LicenseResponse> license;
    private String customerIdentity;
    private String customerName;
    private String userIdentity;
    private String siteIdentity;
    private String licenseId;
    private String subLicenseId;
    private String licenseIdentity;
    private String subLicenseIdentity;
    private IdentityHolder deleteIdentityHolder;
    private CustomerInfrastructure customerInfrastructure = new CustomerInfrastructure();

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
        if (customerIdentity != null && userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        }
        customerInfrastructure.cleanUpCustomerInfrastructure(customerIdentity);
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(id = {5313})
    @Description("Get list of licenses for customer")
    public void getCustomerLicense() {
        setCustomerData();
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
        setCustomerData();
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
        setCustomerData();
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
        setCustomerData();
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
        setCustomerData();
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
        setCustomerData();
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
        setCustomerData();
        ResponseWrapper<CdsErrorResponse> notActiveLicenseModules = cdsTestUtil.getCommonRequest(CDSAPIEnum.ACTIVE_MODULES, CdsErrorResponse.class, HttpStatus.SC_NOT_FOUND, customerIdentity, siteIdentity);
        soft.assertThat(notActiveLicenseModules.getResponseEntity().getMessage()).isEqualTo(String.format("Site, '%s', does not have an active license", siteIdentity));
        soft.assertAll();
    }

    @Test
    @TestRail(id = {6641})
    @Description("Get a sub license")
    public void getSubLicenses() {
        setCustomerData();
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();

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
        setCustomerData();
        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();

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
        setCustomerData();
        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();

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
        setCustomerData();
        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();

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
        setCustomerData();
        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();

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
        setCustomerData();
        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();

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

    private void setCustomerData() {
        RandomCustomerData rcd = new RandomCustomerData();
        ResponseWrapper<Customer> customer = cdsTestUtil.createCustomer(rcd);
        customerIdentity = customer.getResponseEntity().getIdentity();
        customerName = customer.getResponseEntity().getName();

        customerInfrastructure.createCustomerInfrastructure(rcd, customerIdentity);
        ResponseWrapper<Sites> customerSite = cdsTestUtil.getCommonRequest(CDSAPIEnum.SITES_BY_CUSTOMER_ID, Sites.class, HttpStatus.SC_OK, customerIdentity);
        siteIdentity = customerSite.getResponseEntity().getItems().get(0).getIdentity();
        String siteId = customerSite.getResponseEntity().getItems().get(0).getSiteId();

        licenseId = UUID.randomUUID().toString();
        subLicenseId = UUID.randomUUID().toString();

        license = cdsTestUtil.addLicense(customerIdentity, siteIdentity, customerName, siteId, licenseId, subLicenseId);
        licenseIdentity = license.getResponseEntity().getIdentity();
        subLicenseIdentity = license.getResponseEntity().getSubLicenses().get(1).getIdentity();
    }
}
