package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.cds.entity.response.LicenseResponse;
import com.apriori.cds.entity.response.SubLicense;
import com.apriori.cds.entity.response.SubLicenses;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.Licenses;
import com.apriori.cds.objects.response.Site;
import com.apriori.cds.objects.response.SubLicenseAssociation;
import com.apriori.cds.objects.response.SubLicenseAssociationUser;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class CdsLicenseTests {

    private String url;
    private String userIdentityEndpoint;
    private String customerIdentityEndpoint;
    private String userAssociationIdentity;
    private String deleteEndpoint;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }

    @After
    public void cleanUp() {
        if (deleteEndpoint != null) {
            cdsTestUtil.delete(deleteEndpoint);
        }
        if (userIdentityEndpoint != null) {
            cdsTestUtil.delete(userIdentityEndpoint);
        }
        if (customerIdentityEndpoint != null) {
            cdsTestUtil.delete(customerIdentityEndpoint);
        }
    }

    @Test
    @TestRail(testCaseId = "3302")
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
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteId);
        String siteIdentity = site.getResponseEntity().getResponse().getIdentity();

        ResponseWrapper<LicenseResponse> response = cdsTestUtil.addLicense(customerIdentity, siteIdentity, customerName, siteId, licenseId, subLicenseId);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
    }

    @Test
    @TestRail(testCaseId = "5313")
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
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteId);
        String siteIdentity = site.getResponseEntity().getResponse().getIdentity();

        ResponseWrapper<LicenseResponse> response = cdsTestUtil.addLicense(customerIdentity, siteIdentity, customerName, siteId, licenseId, subLicenseId);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String customerLicenseEndpoint = String.format(url, String.format("customers/%s/licenses", customerIdentity));

        ResponseWrapper<Licenses> license = cdsTestUtil.getCommonRequest(customerLicenseEndpoint, Licenses.class);
        assertThat(license.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(license.getResponseEntity().getResponse().getTotalItemCount(), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = "5968")
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
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteId);
        String siteIdentity = site.getResponseEntity().getResponse().getIdentity();

        ResponseWrapper<LicenseResponse> response = cdsTestUtil.addLicense(customerIdentity, siteIdentity, customerName, siteId, licenseId, subLicenseId);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String customerLicenseEndpoint = String.format(url, String.format("customers/%s/licenses", customerIdentity));

        ResponseWrapper<Licenses> license = cdsTestUtil.getCommonRequest(customerLicenseEndpoint, Licenses.class);
        assertThat(license.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        String licenseIdentity = license.getResponseEntity().getResponse().getItems().get(0).getIdentity();
        String licenseIdentityEndpoint = String.format(url, String.format("customers/%s/licenses/%s", customerIdentity, licenseIdentity));

        ResponseWrapper<LicenseResponse> licenseResponse = cdsTestUtil.getCommonRequest(licenseIdentityEndpoint, LicenseResponse.class);
        assertThat(licenseResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(licenseResponse.getResponseEntity().getResponse().getIdentity(), is(equalTo(licenseIdentity)));
    }

    @Test
    @TestRail(testCaseId = "6641")
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
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getResponse().getIdentity();
        userIdentityEndpoint = String.format(url, String.format("customers/%s/users/%s", customerIdentity, userIdentity));

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteId);
        String siteIdentity = site.getResponseEntity().getResponse().getIdentity();

        ResponseWrapper<LicenseResponse> licenseResponse = cdsTestUtil.addLicense(customerIdentity, siteIdentity, customerName, siteId, licenseId, subLicenseId);
        assertThat(licenseResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String licenseIdentity = licenseResponse.getResponseEntity().getResponse().getIdentity();
        String subLicenseEndpoint = String.format(url, String.format("customers/%s/sites/%s/licenses/%s/sub-licenses", customerIdentity, siteIdentity, licenseIdentity));

        ResponseWrapper<SubLicenses> subLicense = cdsTestUtil.getCommonRequest(subLicenseEndpoint, SubLicenses.class);

        assertThat(subLicense.getStatusCode(), Matchers.is(Matchers.equalTo(HttpStatus.SC_OK)));
        assertThat(subLicense.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
    }

    @Test
    @TestRail(testCaseId = "6642")
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
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getResponse().getIdentity();
        userIdentityEndpoint = String.format(url, String.format("customers/%s/users/%s", customerIdentity, userIdentity));

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteId);
        String siteIdentity = site.getResponseEntity().getResponse().getIdentity();

        ResponseWrapper<LicenseResponse> licenseResponse = cdsTestUtil.addLicense(customerIdentity, siteIdentity, customerName, siteId, licenseId, subLicenseId);
        assertThat(licenseResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String licenseIdentity = licenseResponse.getResponseEntity().getResponse().getIdentity();
        String subLicenseIdentity = licenseResponse.getResponseEntity().getResponse().getSubLicenses().get(1).getIdentity();
        String subLicenseEndpoint = String.format(url, String.format("customers/%s/sites/%s/licenses/%s/sub-licenses/%s", customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity));

        ResponseWrapper<SubLicense> subLicense = cdsTestUtil.getCommonRequest(subLicenseEndpoint, SubLicense.class);

        assertThat(subLicense.getStatusCode(), Matchers.is(Matchers.equalTo(HttpStatus.SC_OK)));
        assertThat(subLicense.getResponseEntity().getResponse().getName(),containsString("Sub License"));
    }

    @Test
    @TestRail(testCaseId = "6643")
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
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getResponse().getIdentity();
        userIdentityEndpoint = String.format(url, String.format("customers/%s/users/%s", customerIdentity, userIdentity));

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteId);
        String siteIdentity = site.getResponseEntity().getResponse().getIdentity();

        ResponseWrapper<LicenseResponse> licenseResponse = cdsTestUtil.addLicense(customerIdentity, siteIdentity, customerName, siteId, licenseId, subLicenseId);
        assertThat(licenseResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String customerLicenseEndpoint = String.format(url, String.format("customers/%s/licenses", customerIdentity));

        String subLicenseIdentity = licenseResponse.getResponseEntity().getResponse().getSubLicenses().get(1).getIdentity();

        ResponseWrapper<Licenses> license = cdsTestUtil.getCommonRequest(customerLicenseEndpoint, Licenses.class);
        assertThat(license.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        String licenseIdentity = license.getResponseEntity().getResponse().getItems().get(0).getIdentity();

        ResponseWrapper<SubLicenseAssociationUser> associationUserItemsResponse = cdsTestUtil.addSubLicenseAssociationUser(customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity);
        userAssociationIdentity = associationUserItemsResponse.getResponseEntity().getResponse().getIdentity();
        assertThat(associationUserItemsResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(associationUserItemsResponse.getResponseEntity().getResponse().getCreatedBy(), is(equalTo("#SYSTEM00000")));
        deleteEndpoint = String.format(url, String.format("customers/%s/sites/%s/licenses/%s/sub-licenses/%s/users/%s", customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity));
    }

    @Test
    @TestRail(testCaseId = "6644")
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
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getResponse().getIdentity();
        userIdentityEndpoint = String.format(url, String.format("customers/%s/users/%s", customerIdentity, userIdentity));

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteId);
        String siteIdentity = site.getResponseEntity().getResponse().getIdentity();

        ResponseWrapper<LicenseResponse> licenseResponse = cdsTestUtil.addLicense(customerIdentity, siteIdentity, customerName, siteId, licenseId, subLicenseId);
        assertThat(licenseResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String customerLicenseEndpoint = String.format(url, String.format("customers/%s/licenses", customerIdentity));

        String subLicenseIdentity = licenseResponse.getResponseEntity().getResponse().getSubLicenses().get(1).getIdentity();

        ResponseWrapper<Licenses> license = cdsTestUtil.getCommonRequest(customerLicenseEndpoint, Licenses.class);
        assertThat(license.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        String licenseIdentity = license.getResponseEntity().getResponse().getItems().get(0).getIdentity();

        ResponseWrapper<SubLicenseAssociationUser> associationUserItemsResponse = cdsTestUtil.addSubLicenseAssociationUser(customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity);
        userAssociationIdentity = associationUserItemsResponse.getResponseEntity().getResponse().getIdentity();

        String userAssociationEndpoint = String.format(serviceUrl, String.format("customers/%s/sites/%s/licenses/%s/sub-licenses/%s/users", customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity));
        ResponseWrapper<SubLicenseAssociation> associationUserResponse = cdsTestUtil.getCommonRequest(userAssociationEndpoint, SubLicenseAssociation.class);

        assertThat(associationUserResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        //assertThat(associationUserResponse.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));

        deleteEndpoint = String.format(url, String.format("customers/%s/sites/%s/licenses/%s/sub-licenses/%s/users/%s", customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity));
    }

    @Test
    @TestRail(testCaseId = "6145")
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
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getResponse().getIdentity();
        userIdentityEndpoint = String.format(url, String.format("customers/%s/users/%s", customerIdentity, userIdentity));

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteId);
        String siteIdentity = site.getResponseEntity().getResponse().getIdentity();

        ResponseWrapper<LicenseResponse> licenseResponse = cdsTestUtil.addLicense(customerIdentity, siteIdentity, customerName, siteId, licenseId, subLicenseId);
        assertThat(licenseResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String customerLicenseEndpoint = String.format(url, String.format("customers/%s/licenses", customerIdentity));

        String subLicenseIdentity = licenseResponse.getResponseEntity().getResponse().getSubLicenses().get(1).getIdentity();

        ResponseWrapper<Licenses> license = cdsTestUtil.getCommonRequest(customerLicenseEndpoint, Licenses.class);
        assertThat(license.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        String licenseIdentity = license.getResponseEntity().getResponse().getItems().get(0).getIdentity();

        ResponseWrapper<SubLicenseAssociationUser> associationUserItemsResponse = cdsTestUtil.addSubLicenseAssociationUser(customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity);
        userAssociationIdentity = associationUserItemsResponse.getResponseEntity().getResponse().getIdentity();

        deleteEndpoint = String.format(url, String.format("customers/%s/sites/%s/licenses/%s/sub-licenses/%s/users/%s", customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity));

        ResponseWrapper<String> deleteResponse = cdsTestUtil.delete(deleteEndpoint);
        assertThat(deleteResponse.getStatusCode(), is(equalTo(HttpStatus.SC_NO_CONTENT)));
    }
}
