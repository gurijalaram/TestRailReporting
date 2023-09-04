package com.apriori;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.models.IdentityHolder;
import com.apriori.cds.models.response.SiteItems;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.response.Customer;
import com.apriori.models.response.LicensedApplications;
import com.apriori.models.response.Site;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CdsSitesApplicationsTests {
    private SoftAssertions soft = new SoftAssertions();
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

    @BeforeEach
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

    @AfterEach
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
    @TestRail(id = {6058})
    @Description("Add an application to a site")
    public void addApplicationSite() {
        String appIdentity = Constants.getApProApplicationIdentity();

        ResponseWrapper<LicensedApplications> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);

        String licensedApplicationIdentity = licensedApp.getResponseEntity().getIdentity();
        licensedAppIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .siteIdentity(siteIdentity)
            .licenseIdentity(licensedApplicationIdentity)
            .build();
        soft.assertThat(licensedApp.getResponseEntity().getApplication()).isEqualTo("aP Pro");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {6060})
    @Description("Returns a specific LicensedApplication for a specific customer site")
    public void getApplicationSite() {
        String appIdentity = Constants.getCiaApplicationIdentity();

        ResponseWrapper<LicensedApplications> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        String licensedApplicationIdentity = licensedApp.getResponseEntity().getIdentity();
        licensedAppIdentityHolder = IdentityHolder.builder()
             .customerIdentity(customerIdentity)
             .siteIdentity(siteIdentity)
             .licenseIdentity(licensedApplicationIdentity)
             .build();

        ResponseWrapper<SiteItems> licensedApplications = cdsTestUtil.getCommonRequest(CDSAPIEnum.APPLICATION_SITES_BY_CUSTOMER_SITE_IDS,
                SiteItems.class,
            HttpStatus.SC_OK,
            customerIdentity,
            siteIdentity
        );

        soft.assertThat(licensedApplications.getResponseEntity().getItems().size()).isGreaterThanOrEqualTo(1);

        ResponseWrapper<LicensedApplications> licensedApplicationResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS,
            LicensedApplications.class,
            HttpStatus.SC_OK,
            customerIdentity,
            siteIdentity,
            licensedApplicationIdentity
        );
        soft.assertThat(licensedApplicationResponse.getResponseEntity().getApplication()).isEqualTo("aP Admin");
        soft.assertThat(licensedApplicationResponse.getResponseEntity().getApplicationIdentity()).isEqualTo(Constants.getCiaApplicationIdentity());
        soft.assertAll();
    }
}