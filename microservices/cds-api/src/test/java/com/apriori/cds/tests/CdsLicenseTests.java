package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.cds.entity.response.LicenseResponse;
import com.apriori.cds.entity.response.SubLicense;
import com.apriori.cds.entity.response.SubLicenses;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.Licenses;
import com.apriori.cds.objects.response.Site;
import com.apriori.cds.objects.response.SubLicenseAssociation;
import com.apriori.cds.objects.response.SubLicenseAssociationUser;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.tests.entitie.IdentityHolder;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;

import java.util.UUID;

public class CdsLicenseTests {

    private String userAssociationIdentity;
    private IdentityHolder deleteIdentityHolder;
    private IdentityHolder userIdentityHolder;
    private IdentityHolder customerIdentityHolder;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @After
    public void cleanUp() {
        if (deleteIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.DELETE_SPECIFIC_USER_SUB_LICENSE_USERS,
                deleteIdentityHolder.customerIdentity(),
                deleteIdentityHolder.siteIdentity(),
                deleteIdentityHolder.licenseIdentity(),
                deleteIdentityHolder.subLicenseIdentity(),
                deleteIdentityHolder.userIdentity()
            );
        }
        if (userIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.DELETE_USERS_BY_CUSTOMER_USER_IDS,
                userIdentityHolder.customerIdentity(),
                userIdentityHolder.userIdentity()
            );
        }
        if (customerIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.DELETE_CUSTOMER_BY_CUSTOMER_ID,
                customerIdentityHolder.customerIdentity()
            );
        }
    }

    @Test
    @TestRail(testCaseId = {"3302"})
    @Description("Post user licenses")
    public void postUserLicense() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteId = generateStringUtil.generateSiteID();
        String licenseId = UUID.randomUUID().toString();
        String subLicenseId = UUID.randomUUID().toString();

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getIdentity();
        customerIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .build();

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteId);
        String siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<LicenseResponse> response = cdsTestUtil.addLicense(customerIdentity, siteIdentity, customerName, siteId, licenseId, subLicenseId);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
    }

    @Test
    @TestRail(testCaseId = {"5313"})
    @Description("Get list of licenses for customer")
    public void getCustomerLicense() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteId = generateStringUtil.generateSiteID();
        String licenseId = UUID.randomUUID().toString();
        String subLicenseId = UUID.randomUUID().toString();

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getIdentity();
        customerIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .build();

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteId);
        String siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<LicenseResponse> response = cdsTestUtil.addLicense(customerIdentity, siteIdentity, customerName, siteId, licenseId, subLicenseId);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        ResponseWrapper<Licenses> license = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_LICENSES_BY_CUSTOMER_ID,
            Licenses.class,
            customerIdentity
        );

        assertThat(license.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(license.getResponseEntity().getResponse().getTotalItemCount(), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"5968"})
    @Description("Get list of licenses for customer")
    public void getCustomerLicenseByIdentity() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteId = generateStringUtil.generateSiteID();
        String licenseId = UUID.randomUUID().toString();
        String subLicenseId = UUID.randomUUID().toString();

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getIdentity();
        customerIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .build();

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteId);
        String siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<LicenseResponse> response = cdsTestUtil.addLicense(customerIdentity, siteIdentity, customerName, siteId, licenseId, subLicenseId);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        ResponseWrapper<Licenses> license = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_LICENSES_BY_CUSTOMER_ID,
            Licenses.class,
            customerIdentity
        );
        assertThat(license.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        String licenseIdentity = license.getResponseEntity().getResponse().getItems().get(0).getIdentity();
        ResponseWrapper<LicenseResponse> licenseResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_SPECIFIC_LICENSE_BY_CUSTOMER_LICENSE_ID,
            LicenseResponse.class,
            customerIdentity,
            licenseIdentity
        );
        assertThat(licenseResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(licenseResponse.getResponseEntity().getResponse().getIdentity(), is(equalTo(licenseIdentity)));
    }

    @Test
    @TestRail(testCaseId = {"6641"})
    @Description("Get a sub license")
    public void getSubLicenses() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteId = generateStringUtil.generateSiteID();
        String licenseId = UUID.randomUUID().toString();
        String subLicenseId = UUID.randomUUID().toString();
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getIdentity();
        customerIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .build();

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getIdentity();
        userIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .build();

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteId);
        String siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<LicenseResponse> licenseResponse = cdsTestUtil.addLicense(customerIdentity, siteIdentity, customerName, siteId, licenseId, subLicenseId);
        assertThat(licenseResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String licenseIdentity = licenseResponse.getResponseEntity().getResponse().getIdentity();

        ResponseWrapper<SubLicenses> subLicense = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_SUB_LICENSES,
            SubLicenses.class,
            customerIdentity,
            siteIdentity,
            licenseIdentity
        );

        assertThat(subLicense.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(subLicense.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"6642"})
    @Description("Get a sub license by Identity")
    public void getSubLicenseIdentity() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteId = generateStringUtil.generateSiteID();
        String licenseId = UUID.randomUUID().toString();
        String subLicenseId = UUID.randomUUID().toString();
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getIdentity();
        customerIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .build();

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getIdentity();
        userIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .build();

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteId);
        String siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<LicenseResponse> licenseResponse = cdsTestUtil.addLicense(customerIdentity, siteIdentity, customerName, siteId, licenseId, subLicenseId);
        assertThat(licenseResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String licenseIdentity = licenseResponse.getResponseEntity().getResponse().getIdentity();
        String subLicenseIdentity = licenseResponse.getResponseEntity().getResponse().getSubLicenses().get(1).getIdentity();

        ResponseWrapper<SubLicense> subLicense = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_SPECIFIC_SUB_LICENSE,
            SubLicense.class,
            customerIdentity,
            siteIdentity,
            licenseIdentity,
            subLicenseIdentity
        );

        assertThat(subLicense.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(subLicense.getResponseEntity().getResponse().getName(), containsString("License"));
    }

    @Test
    @TestRail(testCaseId = {"6643"})
    @Description("Adds a user sub-license association")
    public void addUserSubLicense() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteId = generateStringUtil.generateSiteID();
        String licenseId = UUID.randomUUID().toString();
        String subLicenseId = UUID.randomUUID().toString();
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getIdentity();
        customerIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .build();

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getIdentity();
        userIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .build();

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteId);
        String siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<LicenseResponse> licenseResponse = cdsTestUtil.addLicense(customerIdentity, siteIdentity, customerName, siteId, licenseId, subLicenseId);
        assertThat(licenseResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String subLicenseIdentity = licenseResponse.getResponseEntity().getResponse().getSubLicenses().get(1).getIdentity();

        ResponseWrapper<Licenses> license = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_LICENSES_BY_CUSTOMER_ID,
            Licenses.class,
            customerIdentity
        );

        assertThat(license.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        String licenseIdentity = license.getResponseEntity().getResponse().getItems().get(0).getIdentity();

        ResponseWrapper<SubLicenseAssociationUser> associationUserItemsResponse = cdsTestUtil.addSubLicenseAssociationUser(customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity);
        userAssociationIdentity = associationUserItemsResponse.getResponseEntity().getResponse().getIdentity();

        assertThat(associationUserItemsResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(associationUserItemsResponse.getResponseEntity().getResponse().getCreatedBy(), is(equalTo("#SYSTEM00000")));
        deleteIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .siteIdentity(siteIdentity)
            .licenseIdentity(licenseIdentity)
            .subLicenseIdentity(subLicenseIdentity)
            .userIdentity(userIdentity)
            .build();
    }

    @Test
    @TestRail(testCaseId = {"6644"})
    @Description("Gets a list of users with a sub-license association")
    public void getUsersAssociatedWithSubLicense() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteId = generateStringUtil.generateSiteID();
        String licenseId = UUID.randomUUID().toString();
        String subLicenseId = UUID.randomUUID().toString();
        String userName = generateStringUtil.generateUserName();
        String serviceUrl = Constants.getServiceUrl();

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getIdentity();
        customerIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .build();

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getIdentity();
        userIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .build();

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteId);
        String siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<LicenseResponse> licenseResponse = cdsTestUtil.addLicense(customerIdentity, siteIdentity, customerName, siteId, licenseId, subLicenseId);
        assertThat(licenseResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String subLicenseIdentity = licenseResponse.getResponseEntity().getResponse().getSubLicenses().get(1).getIdentity();

        ResponseWrapper<Licenses> license = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_LICENSES_BY_CUSTOMER_ID,
            Licenses.class,
            customerIdentity
        );
        assertThat(license.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        String licenseIdentity = license.getResponseEntity().getResponse().getItems().get(0).getIdentity();

        ResponseWrapper<SubLicenseAssociationUser> associationUserItemsResponse = cdsTestUtil.addSubLicenseAssociationUser(customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity);
        userAssociationIdentity = associationUserItemsResponse.getResponseEntity().getResponse().getIdentity();

        ResponseWrapper<SubLicenseAssociation> associationUserResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_SPECIFIC_SUB_LICENSE_USERS,
            SubLicenseAssociation.class,
            customerIdentity,
            siteIdentity,
            licenseIdentity,
            subLicenseIdentity
        );

        assertThat(associationUserResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(associationUserResponse.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
        deleteIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .siteIdentity(siteIdentity)
            .licenseIdentity(licenseIdentity)
            .subLicenseIdentity(subLicenseIdentity)
            .userIdentity(userIdentity)
            .build();
    }

    @Test
    @TestRail(testCaseId = {"6145"})
    @Description("Deletes an existing user sub-license association")
    public void deleteCustomerSubLicense() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteId = generateStringUtil.generateSiteID();
        String licenseId = UUID.randomUUID().toString();
        String subLicenseId = UUID.randomUUID().toString();
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getIdentity();
        customerIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .build();

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getIdentity();
        userIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .build();

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteId);
        String siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<LicenseResponse> licenseResponse = cdsTestUtil.addLicense(customerIdentity, siteIdentity, customerName, siteId, licenseId, subLicenseId);
        assertThat(licenseResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String subLicenseIdentity = licenseResponse.getResponseEntity().getResponse().getSubLicenses().get(1).getIdentity();

        ResponseWrapper<Licenses> license = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_LICENSES_BY_CUSTOMER_ID,
            Licenses.class,
            customerIdentity
        );
        assertThat(license.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        String licenseIdentity = license.getResponseEntity().getResponse().getItems().get(0).getIdentity();

        ResponseWrapper<SubLicenseAssociationUser> associationUserItemsResponse = cdsTestUtil.addSubLicenseAssociationUser(customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity);
        userAssociationIdentity = associationUserItemsResponse.getResponseEntity().getResponse().getIdentity();

        deleteIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .siteIdentity(siteIdentity)
            .licenseIdentity(licenseIdentity)
            .subLicenseIdentity(subLicenseIdentity)
            .userIdentity(userIdentity)
            .build();

        ResponseWrapper<String> deleteResponse = cdsTestUtil.delete(CDSAPIEnum.DELETE_SPECIFIC_USER_SUB_LICENSE_USERS,
            customerIdentity,
            siteIdentity,
            licenseIdentity,
            subLicenseIdentity,
            userIdentity
        );
        assertThat(deleteResponse.getStatusCode(), is(equalTo(HttpStatus.SC_NO_CONTENT)));
    }
}
