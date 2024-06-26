package com.apriori.cds.api.tests;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.Constants;
import com.apriori.cds.api.utils.SiteUtil;
import com.apriori.shared.util.SharedCustomerUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.Site;
import com.apriori.shared.util.models.response.SiteExpand;
import com.apriori.shared.util.models.response.Sites;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CdsSitesTests {
    private SoftAssertions soft = new SoftAssertions();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil;
    private SiteUtil siteUtil;
    private ResponseWrapper<Customer> customer;
    private String customerName;
    private String cloudRef;
    private String salesForceId;
    private String emailPattern;
    private String customerIdentity;

    @BeforeEach
    public void setDetails() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        cdsTestUtil = new CdsTestUtil(requestEntityUtil);
        siteUtil = new SiteUtil(requestEntityUtil);

        customerName = generateStringUtil.generateCustomerName();
        cloudRef = generateStringUtil.generateCloudReference();
        salesForceId = generateStringUtil.generateNumericString("SFID", 10);
        emailPattern = "\\S+@".concat(customerName);
        String customerType = Constants.CLOUD_CUSTOMER;

        customer = cdsTestUtil.addCustomer(customerName, customerType, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();
    }

    @AfterEach
    public void cleanUp() {
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(id = {5969})
    @Description("Get a list of Sites in CDS Db")
    public void getSites() {
        ResponseWrapper<Sites> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.SITES, Sites.class, HttpStatus.SC_OK);

        soft.assertThat(response.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertThat(response.getResponseEntity().getItems().get(0).getSiteId()).isNotEmpty();
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5309})
    @Description("Get details of a site by its Identity")
    public void getSiteByIdentity() {
        ResponseWrapper<Sites> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.SITES, Sites.class, HttpStatus.SC_OK);
        String siteIdentity = response.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<Site> responseWrapper = cdsTestUtil.getCommonRequest(CDSAPIEnum.SITE_BY_ID, Site.class, HttpStatus.SC_OK, siteIdentity);

        soft.assertThat(responseWrapper.getResponseEntity().getIdentity()).isEqualTo(siteIdentity);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {3299})
    @Description("Add a site to a customer")
    public void addCustomerSite() {
        String siteName = generateStringUtil.generateAlphabeticString("Site", 5);
        String siteID = generateStringUtil.generateSiteID();

        ResponseWrapper<Site> site = siteUtil.addSite(customerIdentity, siteName, siteID);

        soft.assertThat(site.getResponseEntity().getName()).isEqualTo(siteName);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {3279})
    @Description("Get Sites for a customer")
    public void getCustomerSites() {
        ResponseWrapper<Sites> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.SITES_BY_CUSTOMER_ID, Sites.class, HttpStatus.SC_OK, customerIdentity);

        soft.assertThat(response.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(0);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5310})
    @Description("Add a site to a customer")
    public void getCustomerSiteDetails() {
        String siteName = generateStringUtil.generateAlphabeticString("Site", 5);
        String siteID = generateStringUtil.generateSiteID();

        ResponseWrapper<Site> site = siteUtil.addSite(customerIdentity, siteName, siteID);
        String siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<Site> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.SITE_BY_CUSTOMER_SITE_ID, Site.class, HttpStatus.SC_OK, customerIdentity, siteIdentity);

        soft.assertThat(response.getResponseEntity().getName()).isEqualTo(siteName);
        soft.assertThat(response.getResponseEntity().getCustomerIdentity()).isEqualTo(customerIdentity);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {30861})
    @Description("get API Expand for Customer Sites API")
    public void getApiExpandForCustomerSites() {
        String paramName = "_expand";
        String paramValue = "deployments,deployments.installations.features";
        Customer customer = SharedCustomerUtil.getCustomerData();
        String siteIdentity =
            cdsTestUtil.getCommonRequest(CDSAPIEnum.SITES_BY_CUSTOMER_ID, Sites.class, HttpStatus.SC_OK, customer.getIdentity())
                .getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<SiteExpand> response =
            cdsTestUtil.getCommonRequestWithParams(CDSAPIEnum.SITE_BY_CUSTOMER_SITE_ID, SiteExpand.class, HttpStatus.SC_OK, paramName, paramValue, customer.getIdentity(), siteIdentity);
        soft.assertThat(response.getResponseEntity().getExpand())
            .contains("deployments", "deployments.installations.applications", "deployments.installations.features");
        soft.assertAll();
    }
}
