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
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
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
    private static String url;
    private static String customerIdentityEndpoint;

    @BeforeClass
    public static void setDetails() {
        url = Constants.getServiceUrl();

        customerName = generateStringUtil.generateCustomerName();
        cloudRef = generateStringUtil.generateCloudReference();
        salesForceId = generateStringUtil.generateSalesForceId();
        emailPattern = "\\S+@".concat(customerName);

        customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));
    }

    @AfterClass
    public static void cleanUp() {
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
        assertThat(response.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
        assertThat(response.getResponseEntity().getItems().get(0).getSiteId(), is(not(emptyString())));
    }

    @Test
    @TestRail(testCaseId = {"5309"})
    @Description("Get details of a site by its Identity")
    public void getSiteByIdentity() {
        String sitesUrl = String.format(url, "sites");

        ResponseWrapper<Sites> response = cdsTestUtil.getCommonRequest(sitesUrl, Sites.class);
        String siteIdentity = response.getResponseEntity().getItems().get(0).getIdentity();

        String identityUrl = String.format(Constants.getServiceUrl(), String.format("sites/%s", siteIdentity));
        ResponseWrapper<Site> responseWrapper = cdsTestUtil.getCommonRequest(identityUrl, Site.class);

        assertThat(responseWrapper.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(responseWrapper.getResponseEntity().getIdentity(), is(equalTo(siteIdentity)));
    }

    @Test
    @TestRail(testCaseId = {"3299"})
    @Description("Add a site to a customer")
    public void addCustomerSite() {
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteID);

        assertThat(site.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(site.getResponseEntity().getName(), is(equalTo(siteName)));
    }

    @Test
    @TestRail(testCaseId = {"3279"})
    @Description("Get Sites for a customer")
    public void getCustomerSites() {
        String siteEndpoint = String.format(url, String.format("customers/%s/sites", customerIdentity));

        ResponseWrapper<Sites> response = cdsTestUtil.getCommonRequest(siteEndpoint, Sites.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(0)));
    }

    @Test
    @TestRail(testCaseId = {"5310"})
    @Description("Add a site to a customer")
    public void getCustomerSiteDetails() {
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();

        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteID);
        String siteIdentity = site.getResponseEntity().getIdentity();
        String identityEndpoint = String.format(url, String.format("customers/%s/sites/%s", customerIdentity, siteIdentity));

        ResponseWrapper<Site> response = cdsTestUtil.getCommonRequest(identityEndpoint, Site.class);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(site.getResponseEntity().getName(), is(equalTo(siteName)));
        assertThat(site.getResponseEntity().getCustomerIdentity(), is(equalTo(customerIdentity)));
    }
}
