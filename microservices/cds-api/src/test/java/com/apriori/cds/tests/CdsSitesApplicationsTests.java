package com.apriori.cds.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.LicensedApplication;
import com.apriori.cds.objects.response.Site;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CdsSitesApplicationsTests {

    private static String customerIdentityEndpoint;
    private static String licensedAppIdentityEndpoint;
    private static GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private static CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private static String customerIdentity;
    private static String url;
    private static String customerName;
    private static String cloudRef;
    private static String salesForceId;
    private static String emailPattern;
    private static ResponseWrapper<Customer> customer;
    private static String siteName;
    private static String siteID;
    private static ResponseWrapper<Site> site;
    private static String siteIdentity;

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

        siteName = generateStringUtil.generateSiteName();
        siteID = generateStringUtil.generateSiteID();

        site = cdsTestUtil.addSite(customerIdentity, siteName, siteID);
        siteIdentity = site.getResponseEntity().getIdentity();
    }

    @AfterClass
    public static void cleanUp() {
        if (licensedAppIdentityEndpoint != null) {
            cdsTestUtil.delete(licensedAppIdentityEndpoint);
        }
        if (customerIdentityEndpoint != null) {
            cdsTestUtil.delete(customerIdentityEndpoint);
        }
    }

    @Test
    @TestRail(testCaseId = {"6058"})
    @Description("Add an application to a site")
    public void addApplicationSite() {
        String appIdentity = Constants.getApProApplicationIdentity();

        ResponseWrapper<LicensedApplication> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        assertThat(licensedApp.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String licensedApplicationIdentity = licensedApp.getResponseEntity().getIdentity();
        licensedAppIdentityEndpoint = String.format(url, String.format("customers/%s/sites/%s/licensed-applications/%s", customerIdentity, siteIdentity, licensedApplicationIdentity));
        assertThat(licensedApp.getResponseEntity().getApplication(), is(equalTo("aPriori Professional")));
    }

    @Test
    @TestRail(testCaseId = {"6060"})
    @Description("Returns a specific LicensedApplication for a specific customer site")
    public void getApplicationSite() {
        String appIdentity = Constants.getCiaApplicationIdentity();

        ResponseWrapper<LicensedApplication> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        assertThat(licensedApp.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String licensedApplicationIdentity = licensedApp.getResponseEntity().getIdentity();
        licensedAppIdentityEndpoint = String.format(url, String.format("customers/%s/sites/%s/licensed-applications/%s", customerIdentity, siteIdentity, licensedApplicationIdentity));

        ResponseWrapper<LicensedApplication> licensedApplicationResponse = cdsTestUtil.getCommonRequest(licensedAppIdentityEndpoint, LicensedApplication.class);
        assertThat(licensedApplicationResponse.getResponseEntity().getApplication(), is(equalTo("Cost Insight Admin")));
        assertThat(licensedApplicationResponse.getResponseEntity().getApplicationIdentity(), is(equalTo(Constants.getCiaApplicationIdentity())));
    }
}