package com.apriori.cds.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cds.entity.IdentityHolder;
import com.apriori.cds.enums.CDSAPIEnum;
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

    private static IdentityHolder licensedAppIdentityHolder;
    private static GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private static CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private static String customerIdentity;
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

        customerName = generateStringUtil.generateCustomerName();
        cloudRef = generateStringUtil.generateCloudReference();
        salesForceId = generateStringUtil.generateSalesForceId();
        emailPattern = "\\S+@".concat(customerName);

        customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();

        siteName = generateStringUtil.generateSiteName();
        siteID = generateStringUtil.generateSiteID();

        site = cdsTestUtil.addSite(customerIdentity, siteName, siteID);
        siteIdentity = site.getResponseEntity().getIdentity();
    }

    @AfterClass
    public static void cleanUp() {
        if (licensedAppIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.DELETE_CUSTOMER_LICENSED_APPLICATIONS_BY_CUSTOMER_SITE_LICENSED_IDS,
                licensedAppIdentityHolder.customerIdentity(),
                licensedAppIdentityHolder.siteIdentity(),
                licensedAppIdentityHolder.licenseIdentity()
            );
        }
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.DELETE_CUSTOMER_BY_ID, customerIdentity);
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
        licensedAppIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .siteIdentity(siteIdentity)
            .licenseIdentity(licensedApplicationIdentity)
            .build();
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
        licensedAppIdentityHolder = IdentityHolder.builder()
             .customerIdentity(customerIdentity)
             .siteIdentity(siteIdentity)
             .licenseIdentity(licensedApplicationIdentity)
             .build();

        ResponseWrapper<LicensedApplication> licensedApplicationResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_CUSTOMER_LICENSED_APPLICATIONS_BY_CUSTOMER_SITE_LICENSED_IDS,
            LicensedApplication.class,
            customerIdentity,
            siteIdentity,
            licensedApplicationIdentity
        );
        assertThat(licensedApplicationResponse.getResponseEntity().getApplication(), is(equalTo("Cost Insight Admin")));
        assertThat(licensedApplicationResponse.getResponseEntity().getApplicationIdentity(), is(equalTo(Constants.getCiaApplicationIdentity())));
    }
}