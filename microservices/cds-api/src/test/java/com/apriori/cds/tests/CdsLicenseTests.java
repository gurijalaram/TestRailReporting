package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.cds.entity.IdentityHolder;
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
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;
import java.util.stream.Collectors;

public class CdsLicenseTests {

    private static GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private static CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private static ResponseWrapper<Customer> customer;
    private static ResponseWrapper<Site> site;
    private static ResponseWrapper<LicenseResponse> license;
    private static String customerName;
    private static String cloudRef;
    private static String salesForceId;
    private static String emailPattern;
    private static String customerIdentity;
    private static String siteName;
    private static String siteId;
    private static String siteIdentity;
    private static String licenseId;
    private static String subLicenseId;
    private static String licenseIdentity;
    private static String subLicenseIdentity;
    private IdentityHolder deleteIdentityHolder;
    private IdentityHolder userIdentityHolder;

    @BeforeClass
    public static void setDetails() {
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
        if (userIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS,
                userIdentityHolder.customerIdentity(),
                userIdentityHolder.userIdentity()
            );
        }
    }

    @AfterClass
    public static void deleteCustomer() {
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(testCaseId = {"5313"})
    @Description("Get list of licenses for customer")
    public void getCustomerLicense() {

        ResponseWrapper<Licenses> license = cdsTestUtil.getCommonRequest(CDSAPIEnum.LICENSES_BY_CUSTOMER_ID,
            Licenses.class,
            customerIdentity
        );

        assertThat(license.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(license.getResponseEntity().getTotalItemCount(), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"5968"})
    @Description("Get list of licenses for customer")
    public void getCustomerLicenseByIdentity() {
        ResponseWrapper<LicenseResponse> licenseResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.SPECIFIC_LICENSE_BY_CUSTOMER_LICENSE_ID,
            LicenseResponse.class,
            customerIdentity,
            licenseIdentity
        );
        assertThat(licenseResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(licenseResponse.getResponseEntity().getIdentity(), is(equalTo(licenseIdentity)));
    }

    @Test
    @TestRail(testCaseId = {"5653"})
    @Description("Returns a list of licenses for a specific customer site.")
    public void getLicensesOfTheSite() {

        ResponseWrapper<Licenses> siteLicenses = cdsTestUtil.getCommonRequest(CDSAPIEnum.LICENSE_BY_CUSTOMER_SITE_IDS,
            Licenses.class,
            customerIdentity,
            siteIdentity
        );

        assertThat(siteLicenses.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(siteLicenses.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"5654"})
    @Description("Returns a specific license for a specific customer site")
    public void getLicenseOfSiteById() {
        ResponseWrapper<LicenseResponse> licenseById = cdsTestUtil.getCommonRequest(CDSAPIEnum.LICENSE_BY_CUSTOMER_SITE_LICENSE_IDS,
            LicenseResponse.class,
            customerIdentity,
            siteIdentity,
            licenseIdentity
        );

        assertThat(licenseById.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(licenseById.getResponseEntity().getIdentity(), is(equalTo(licenseIdentity)));
    }

    @Test
    @TestRail(testCaseId = {"6641"})
    @Description("Get a sub license")
    public void getSubLicenses() {
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getIdentity();
        userIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .build();

        ResponseWrapper<SubLicenses> subLicense = cdsTestUtil.getCommonRequest(CDSAPIEnum.SUB_LICENSES,
            SubLicenses.class,
            customerIdentity,
            siteIdentity,
            licenseIdentity
        );

        assertThat(subLicense.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(subLicense.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"6642"})
    @Description("Get a sub license by Identity")
    public void getSubLicenseIdentity() {
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getIdentity();
        userIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .build();

        ResponseWrapper<SubLicense> subLicense = cdsTestUtil.getCommonRequest(CDSAPIEnum.SPECIFIC_SUB_LICENSE,
            SubLicense.class,
            customerIdentity,
            siteIdentity,
            licenseIdentity,
            subLicenseIdentity
        );

        assertThat(subLicense.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(subLicense.getResponseEntity().getName(), containsString("License"));
    }

    @Test
    @TestRail(testCaseId = {"6643"})
    @Description("Adds a user sub-license association")
    public void addUserSubLicense() {
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getIdentity();
        userIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .build();

        ResponseWrapper<LicenseResponse> licenseResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.SPECIFIC_LICENSE_BY_CUSTOMER_LICENSE_ID,
            LicenseResponse.class,
            customerIdentity,
            licenseIdentity
        );
        String subLicenseIdentity = licenseResponse.getResponseEntity().getSubLicenses().stream()
            .filter(x -> !x.getName().contains("master"))
            .collect(Collectors.toList()).get(0).getIdentity();

        ResponseWrapper<SubLicenseAssociationUser> associationUserItemsResponse = cdsTestUtil.addSubLicenseAssociationUser(customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity);

        assertThat(associationUserItemsResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(associationUserItemsResponse.getResponseEntity().getCreatedBy(), is(equalTo("#SYSTEM00000")));

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
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getIdentity();
        userIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .build();

        ResponseWrapper<LicenseResponse> licenseResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.SPECIFIC_LICENSE_BY_CUSTOMER_LICENSE_ID,
            LicenseResponse.class,
            customerIdentity,
            licenseIdentity
        );

        String subLicenseIdentity = licenseResponse.getResponseEntity().getSubLicenses().stream()
            .filter(x -> !x.getName().contains("master"))
            .collect(Collectors.toList()).get(0).getIdentity();

        cdsTestUtil.addSubLicenseAssociationUser(customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity);

        ResponseWrapper<SubLicenseAssociation> associationUserResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.SUBLICENSE_ASSOCIATIONS_USERS,
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
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getIdentity();
        userIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .build();

        ResponseWrapper<LicenseResponse> licenseResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.SPECIFIC_LICENSE_BY_CUSTOMER_LICENSE_ID,
            LicenseResponse.class,
            customerIdentity,
            licenseIdentity
        );

        String subLicenseIdentity = licenseResponse.getResponseEntity().getSubLicenses().stream()
            .filter(x -> !x.getName().contains("master"))
            .collect(Collectors.toList()).get(0).getIdentity();


        cdsTestUtil.addSubLicenseAssociationUser(customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity);

        deleteIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .siteIdentity(siteIdentity)
            .licenseIdentity(licenseIdentity)
            .subLicenseIdentity(subLicenseIdentity)
            .userIdentity(userIdentity)
            .build();

        ResponseWrapper<String> deleteResponse = cdsTestUtil.delete(CDSAPIEnum.SUBLICENSE_ASSOCIATIONS_USER_BY_ID,
            customerIdentity,
            siteIdentity,
            licenseIdentity,
            subLicenseIdentity,
            userIdentity
        );
        assertThat(deleteResponse.getStatusCode(), is(equalTo(HttpStatus.SC_NO_CONTENT)));
    }
}
