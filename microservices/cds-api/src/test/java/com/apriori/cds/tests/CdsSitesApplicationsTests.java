package com.apriori.cds.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cds.entity.IdentityHolder;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.LicensedApplication;
import com.apriori.cds.objects.response.LicensedApplications;
import com.apriori.cds.objects.response.Site;
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

public class CdsSitesApplicationsTests {

    private IdentityHolder licensedAppIdentityHolder;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private String customerIdentity;
    private String customerName;
    private String cloudRef;
    private String salesForceId;
    private String emailPattern;
    private ResponseWrapper<Customer> customer;
    private String siteName;
    private String siteID;
    private ResponseWrapper<Site> site;
    private String siteIdentity;

    @Before
    public void setDetails() {
        customerName = generateStringUtil.generateCustomerName();
        cloudRef = generateStringUtil.generateCloudReference();
        salesForceId = generateStringUtil.generateSalesForceId();
        emailPattern = "\\S+@".concat(customerName);
        String customerType = Constants.CLOUD_CUSTOMER;

        customer = cdsTestUtil.addCustomer(customerName, customerType, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();

        siteName = generateStringUtil.generateSiteName();
        siteID = generateStringUtil.generateSiteID();

        site = cdsTestUtil.addSite(customerIdentity, siteName, siteID);
        siteIdentity = site.getResponseEntity().getIdentity();
    }

    @After
    public void cleanUp() {
        if (licensedAppIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS,
                licensedAppIdentityHolder.customerIdentity(),
                licensedAppIdentityHolder.siteIdentity(),
                licensedAppIdentityHolder.licenseIdentity()
            );
        }
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
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

        ResponseWrapper<LicensedApplications> licensedApplications = cdsTestUtil.getCommonRequest(CDSAPIEnum.APPLICATION_SITES_BY_CUSTOMER_SITE_IDS,
            LicensedApplications.class,
            customerIdentity,
            siteIdentity
        );

        assertThat(licensedApplications.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(licensedApplications.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));

        ResponseWrapper<LicensedApplication> licensedApplicationResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS,
            LicensedApplication.class,
            customerIdentity,
            siteIdentity,
            licensedApplicationIdentity
        );
        assertThat(licensedApplicationResponse.getResponseEntity().getApplication(), is(equalTo("Cost Insight Admin")));
        assertThat(licensedApplicationResponse.getResponseEntity().getApplicationIdentity(), is(equalTo(Constants.getCiaApplicationIdentity())));
    }
}