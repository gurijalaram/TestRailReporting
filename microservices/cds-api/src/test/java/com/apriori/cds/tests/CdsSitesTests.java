package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.cds.enums.CDSAPIEnum;
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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CdsSitesTests {
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private ResponseWrapper<Customer> customer;
    private String customerName;
    private String cloudRef;
    private String salesForceId;
    private String emailPattern;
    private String customerIdentity;

    @Before
    public void setDetails() {
        customerName = generateStringUtil.generateCustomerName();
        cloudRef = generateStringUtil.generateCloudReference();
        salesForceId = generateStringUtil.generateSalesForceId();
        emailPattern = "\\S+@".concat(customerName);
        String customerType = Constants.CLOUD_CUSTOMER;

        customer = cdsTestUtil.addCustomer(customerName, customerType, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();
    }

    @After
    public void cleanUp() {
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(testCaseId = {"5969"})
    @Description("Get a list of Sites in CDS Db")
    public void getSites() {
        ResponseWrapper<Sites> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.SITES, Sites.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
        assertThat(response.getResponseEntity().getItems().get(0).getSiteId(), is(not(emptyString())));
    }

    @Test
    @TestRail(testCaseId = {"5309"})
    @Description("Get details of a site by its Identity")
    public void getSiteByIdentity() {
        ResponseWrapper<Sites> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.SITES, Sites.class);
        String siteIdentity = response.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<Site> responseWrapper = cdsTestUtil.getCommonRequest(CDSAPIEnum.SITE_BY_ID, Site.class, siteIdentity);

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
        ResponseWrapper<Sites> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.SITES_BY_CUSTOMER_ID, Sites.class, customerIdentity);

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

        ResponseWrapper<Site> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.SITE_BY_CUSTOMER_SITE_ID, Site.class, customerIdentity, siteIdentity);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(site.getResponseEntity().getName(), is(equalTo(siteName)));
        assertThat(site.getResponseEntity().getCustomerIdentity(), is(equalTo(customerIdentity)));
    }
}
