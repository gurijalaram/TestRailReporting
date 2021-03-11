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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CdsSitesApplicationsTests extends CdsTestUtil {
    private String url;

    private String customerIdentity;
    private String customerIdentityEndpoint;
    private String installationIdentityEndpoint;
    private String licensedAppIdentityEndpoint;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }

    @After
    public void cleanUp() {
        if (licensedAppIdentityEndpoint != null) {
            delete(licensedAppIdentityEndpoint);
        }
        if (customerIdentityEndpoint != null) {
            delete(customerIdentityEndpoint);
        }
    }

    @Test
    @TestRail(testCaseId = "6058")
    @Description("Add an application to a site")
    public void addApplicationSite() {
        String customersEndpoint = String.format(url, "customers");

        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();
        String realmKey = generateStringUtil.generateRealmKey();

        ResponseWrapper<Customer> customer = addCustomer(customersEndpoint, Customer.class, customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        String siteEndpoint = String.format(url, String.format("customers/%s/sites", customerIdentity));
        ResponseWrapper<Site> site = addSite(siteEndpoint, Site.class, siteName, siteID);
        assertThat(site.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String siteIdentity = site.getResponseEntity().getResponse().getIdentity();

        String licensedApplicationsEndpoint = String.format(url, String.format("customers/%s/sites/%s/licensed-applications", customerIdentity, siteIdentity));
        ResponseWrapper<LicensedApplication> licensedApp = addApplicationToSite(licensedApplicationsEndpoint, LicensedApplication.class);
        assertThat(licensedApp.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String licensedApplicationIdentity = licensedApp.getResponseEntity().getResponse().getIdentity();
        licensedAppIdentityEndpoint = String.format(url, String.format("customers/%s/sites/%s/licensed-applications/%s", customerIdentity, siteIdentity, licensedApplicationIdentity));
        assertThat(licensedApp.getResponseEntity().getResponse().getApplication(), is(equalTo("aPriori Professional")));
    }
}