package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.Site;
import com.apriori.cds.objects.response.Sites;
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
import org.junit.BeforeClass;
import org.junit.Test;

public class CdsSitesTests {
    private static GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private static CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private static ResponseWrapper<Customer> customer;
    private static String customerName;
    private static String cloudRef;
    private static String salesForceId;
    private static String emailPattern;
    private static String customerIdentity;
    private String url;
    private static String customerIdentityEndpoint;

    @BeforeClass
    public static void setDetails() {
        customerName = generateStringUtil.generateCustomerName();
        cloudRef = generateStringUtil.generateCloudReference();
        salesForceId = generateStringUtil.generateSalesForceId();
        emailPattern = "\\S+@".concat(customerName);

        customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
    }

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }

    @After
    public void cleanUp() {
        if (customerIdentityEndpoint != null) {
            cdsTestUtil.delete(customerIdentityEndpoint);
        }
    }

    @Test
    @TestRail(testCaseId = {"5969"})
    @Description("Get a list of Sites in CDS Db")
    public void getSites() {
        url = String.format(url, "sites");

        ResponseWrapper<Sites> response = cdsTestUtil.getCommonRequest(url, Sites.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
        assertThat(response.getResponseEntity().getResponse().getItems().get(0).getSiteId(), is(not(emptyString())));
    }

    @Test
    @TestRail(testCaseId = {"5309"})
    @Description("Get details of a site by its Identity")
    public void getSiteByIdentity() {
        String sitesUrl = String.format(url, "sites");

        ResponseWrapper<Sites> response = cdsTestUtil.getCommonRequest(sitesUrl, Sites.class);
        String siteIdentity = response.getResponseEntity().getResponse().getItems().get(0).getIdentity();

        String identityUrl = String.format(url, String.format("sites/%s", siteIdentity));
        ResponseWrapper<Site> responseWrapper = cdsTestUtil.getCommonRequest(identityUrl, Site.class);

        assertThat(responseWrapper.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(responseWrapper.getResponseEntity().getResponse().getIdentity(), is(equalTo(siteIdentity)));
    }

    @Test
    @TestRail(testCaseId = {"3299"})
    @Description("Add a site to a customer")
    public void addCustomerSite() {
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();

        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteID);

        assertThat(site.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(site.getResponseEntity().getResponse().getName(), is(equalTo(siteName)));
    }

    @Test
    @TestRail(testCaseId = {"3279"})
    @Description("Get Sites for a customer")
    public void getCustomerSites() {
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));
        String siteEndpoint = String.format(url, String.format("customers/%s/sites", customerIdentity));

        ResponseWrapper<Sites> response = cdsTestUtil.getCommonRequest(siteEndpoint, Sites.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), is(equalTo(0)));
    }

    @Test
    @TestRail(testCaseId = {"5310"})
    @Description("Add a site to a customer")
    public void getCustomerSiteDetails() {
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();

        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteID);
        String siteIdentity = site.getResponseEntity().getResponse().getIdentity();
        String identityEndpoint = String.format(url, String.format("customers/%s/sites/%s", customerIdentity, siteIdentity));

        ResponseWrapper<Site> response = cdsTestUtil.getCommonRequest(identityEndpoint, Site.class);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(site.getResponseEntity().getResponse().getName(), is(equalTo(siteName)));
        assertThat(site.getResponseEntity().getResponse().getCustomerIdentity(), is(equalTo(customerIdentity)));
    }

    @Test
    @TestRail(testCaseId = {"5970"})
    public void deleteSite() {
        String userName = generateStringUtil.generateUserName();

        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);

        assertThat(user.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String userIdentity = user.getResponseEntity().getResponse().getIdentity();
        String deleteEndpoint = String.format(url, String.format("customers/%s/users/%s", customerIdentity, userIdentity));

        ResponseWrapper<String> deleteResponse = cdsTestUtil.delete(deleteEndpoint);

        assertThat(deleteResponse.getStatusCode(), is(equalTo(HttpStatus.SC_NO_CONTENT)));
    }
}
