package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cds.entity.response.LicenseResponse;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.Licenses;
import com.apriori.cds.objects.response.Site;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class CdsLicenseTests extends CdsTestUtil {

    private String url;
    private String userIdentityEndpoint;
    private String customerIdentityEndpoint;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }

    @After
    public void cleanUp() {
        if (userIdentityEndpoint != null) {
            delete(userIdentityEndpoint);
        }
        if (customerIdentityEndpoint != null) {
            delete(customerIdentityEndpoint);
        }
    }

    @Test
    @TestRail(testCaseId = "3302")
    @Description("Post user licenses")
    public void postUserLicense() {
        String customersEndpoint = String.format(url, "customers");

        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteId = generateStringUtil.generateSiteID();
        String licenseId = UUID.randomUUID().toString();
        String subLicenseId = UUID.randomUUID().toString();

        ResponseWrapper<Customer> customer = addCustomer(customersEndpoint, Customer.class, customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));
        String siteEndpoint = String.format(url, String.format("customers/%s/sites", customerIdentity));

        ResponseWrapper<Site> site = addSite(siteEndpoint, Site.class, siteName, siteId);
        String siteIdentity = site.getResponseEntity().getResponse().getIdentity();
        String licenseEndpoint = String.format(url, String.format("customers/%s/sites/%s/licenses", customerIdentity, siteIdentity));

        ResponseWrapper<LicenseResponse> response = addLicense(licenseEndpoint, LicenseResponse.class, customerName, siteId, licenseId, subLicenseId);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
    }

    @Test
    @TestRail(testCaseId = "5313")
    @Description("Get list of licenses for customer")
    public void getCustomerLicense() {
        String customersEndpoint = String.format(url, "customers");

        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteId = generateStringUtil.generateSiteID();
        String licenseId = UUID.randomUUID().toString();
        String subLicenseId = UUID.randomUUID().toString();

        ResponseWrapper<Customer> customer = addCustomer(customersEndpoint, Customer.class, customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));
        String siteEndpoint = String.format(url, String.format("customers/%s/sites", customerIdentity));

        ResponseWrapper<Site> site = addSite(siteEndpoint, Site.class, siteName, siteId);
        String siteIdentity = site.getResponseEntity().getResponse().getIdentity();
        String licenseEndpoint = String.format(url, String.format("customers/%s/sites/%s/licenses", customerIdentity, siteIdentity));

        ResponseWrapper<LicenseResponse> response = addLicense(licenseEndpoint, LicenseResponse.class, customerName, siteId, licenseId, subLicenseId);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String customerLicenseEndpoint = String.format(url, String.format("customers/%s/licenses", customerIdentity));

        ResponseWrapper<Licenses> license = getCommonRequest(customerLicenseEndpoint, true, Licenses.class);
        assertThat(license.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(license.getResponseEntity().getResponse().getTotalItemCount(), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = "5968")
    @Description("Get list of licenses for customer")
    public void getCustomerLicenseByIdentity() {
        String customersEndpoint = String.format(url, "customers");

        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteId = generateStringUtil.generateSiteID();
        String licenseId = UUID.randomUUID().toString();
        String subLicenseId = UUID.randomUUID().toString();

        ResponseWrapper<Customer> customer = addCustomer(customersEndpoint, Customer.class, customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));
        String siteEndpoint = String.format(url, String.format("customers/%s/sites", customerIdentity));

        ResponseWrapper<Site> site = addSite(siteEndpoint, Site.class, siteName, siteId);
        String siteIdentity = site.getResponseEntity().getResponse().getIdentity();
        String licenseEndpoint = String.format(url, String.format("customers/%s/sites/%s/licenses", customerIdentity, siteIdentity));

        ResponseWrapper<LicenseResponse> response = addLicense(licenseEndpoint, LicenseResponse.class, customerName, siteId, licenseId, subLicenseId);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String customerLicenseEndpoint = String.format(url, String.format("customers/%s/licenses", customerIdentity));

        ResponseWrapper<Licenses> license = getCommonRequest(customerLicenseEndpoint, true, Licenses.class);
        assertThat(license.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        String licenseIdentity = license.getResponseEntity().getResponse().getItems().get(0).getIdentity();
        String licenseIdentityEndpoint = String.format(url, String.format("customers/%s/licenses/%s", customerIdentity, licenseIdentity));

        ResponseWrapper<LicenseResponse> licenseResponse = getCommonRequest(licenseIdentityEndpoint, true, LicenseResponse.class);
        assertThat(licenseResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(licenseResponse.getResponseEntity().getResponse().getIdentity(), is(equalTo(licenseIdentity)));
    }

    @Test
    @Description("")
    @TestRail(testCaseId = "")
    public void deleteCustomerSublicense() {
        String customersEndpoint = String.format(url, "customers");

        String customerName = generateStringUtil.generateCustomerName();
        String userName = generateStringUtil.generateUserName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteId = generateStringUtil.generateSiteID();
        String licenseId = UUID.randomUUID().toString();
        String subLicenseId = UUID.randomUUID().toString();

        ResponseWrapper<Customer> customer = addCustomer(customersEndpoint, Customer.class, customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));
        String usersEndpoint = String.format(url, String.format("customers/%s/users", customerIdentity));
        String siteEndpoint = String.format(url, String.format("customers/%s/sites", customerIdentity));

        ResponseWrapper<User> user = addUser(usersEndpoint, User.class, userName, customerName);
        String userIdentity = user.getResponseEntity().getResponse().getIdentity();
        userIdentityEndpoint = String.format(url, String.format("customers/%s/users/%s", customerIdentity, userIdentity));

        ResponseWrapper<Site> site = addSite(siteEndpoint, Site.class, siteName, siteId);
        String siteIdentity = site.getResponseEntity().getResponse().getIdentity();
        String licenseEndpoint = String.format(url, String.format("customers/%s/sites/%s/licenses", customerIdentity, siteIdentity));

        ResponseWrapper<LicenseResponse> response = addLicense(licenseEndpoint, LicenseResponse.class, customerName, siteId, licenseId, subLicenseId);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String customerLicenseEndpoint = String.format(url, String.format("customers/%s/licenses", customerIdentity));

        ResponseWrapper<Licenses> license = getCommonRequest(customerLicenseEndpoint, true, Licenses.class);
        assertThat(license.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        String licenseIdentity = license.getResponseEntity().getResponse().getItems().get(0).getIdentity();
        String licenseIdentityEndpoint = String.format(url, String.format("customers/%s/licenses/%s", customerIdentity, licenseIdentity));

        ResponseWrapper<LicenseResponse> licenseResponse = getCommonRequest(licenseIdentityEndpoint, true, LicenseResponse.class);
        assertThat(licenseResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(licenseResponse.getResponseEntity().getResponse().getIdentity(), is(equalTo(licenseIdentity)));

        String subId1 = licenseResponse.getResponseEntity().getResponse().getSubLicenses().get(0).getIdentity();
        String deleteEndpoint1 = String.format(url, String.format("customers/%s/sites/%s/licenses/%s/sub-licenses/%s/users/%s", customerIdentity, siteIdentity, licenseIdentity, subId1, userIdentity));
        ResponseWrapper<String> deleteResponse = delete(deleteEndpoint1);
        assertThat(deleteResponse.getStatusCode(), is(equalTo(HttpStatus.SC_NO_CONTENT)));

        String subId2 = licenseResponse.getResponseEntity().getResponse().getSubLicenses().get(0).getIdentity();
        String deleteEndpoint2 = String.format(url, String.format("customers/%s/sites/%s/licenses/%s/sub-licenses/%s/users/%s", customerIdentity, siteIdentity, licenseIdentity, subId2, userIdentity));
        ResponseWrapper<String> deleteResponse2 = delete(deleteEndpoint2);
        assertThat(deleteResponse2.getStatusCode(), is(equalTo(HttpStatus.SC_NO_CONTENT)));
    }
}
