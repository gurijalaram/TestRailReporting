package com.apriori.tests;

import com.apriori.apibase.services.cas.Customer;
import com.apriori.apibase.services.cas.Customers;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.entity.response.Site;
import com.apriori.entity.response.Sites;
import com.apriori.entity.response.ValidateSite;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CasSitesTests {

    private SoftAssertions soft = new SoftAssertions();
    private String token;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private String customerIdentity;
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @Before
    public void getToken() {
        token = new AuthorizationUtil().getTokenAsString();
    }

    @After
    public void cleanUp() {
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(testCaseId = {"5649"})
    @Description("Returns a list of sites for the customer")
    public void getCustomerSites() {
        ResponseWrapper<Customers> response = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.CUSTOMERS, Customers.class)
            .token(token)).get();

        soft.assertThat(response.getStatusCode())
            .isEqualTo(HttpStatus.SC_OK);

        String customerIdentity = response.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<Sites> siteResponse = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_SITES, Sites.class)
            .token(token)
            .inlineVariables(customerIdentity)).get();

        soft.assertThat(siteResponse.getStatusCode())
            .isEqualTo(HttpStatus.SC_OK);
        soft.assertThat(siteResponse.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);
        soft.assertThat(siteResponse.getResponseEntity().getItems().get(0).getSiteId())
            .isNotEmpty();
    }

    @Test
    @TestRail(testCaseId = {"5650"})
    @Description("Get the Site identified by its identity.")
    public void getSiteByIdentity() {
        ResponseWrapper<Customers> response = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.CUSTOMERS, Customers.class)
            .token(token)).get();

        soft.assertThat(response.getStatusCode())
            .isEqualTo(HttpStatus.SC_OK);

        String customerIdentity = response.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<Sites> sitesResponse = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_SITES, Sites.class)
            .token(token)
            .inlineVariables(customerIdentity)).get();

        soft.assertThat(sitesResponse.getStatusCode())
            .isEqualTo(HttpStatus.SC_OK);
        soft.assertThat(sitesResponse.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);

        String siteIdentity = sitesResponse.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<Site> site = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_SITE_ID, Site.class)
            .token(token)
            .inlineVariables(customerIdentity, siteIdentity)).get();

        soft.assertThat(site.getStatusCode())
            .isEqualTo(HttpStatus.SC_OK);
        soft.assertThat(site.getResponseEntity().getIdentity())
            .isEqualTo(siteIdentity);
        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5651"})
    @Description("Validates Customer's Site record by site ID.")
    public void validateCustomerSite() {
        ResponseWrapper<Customers> response = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.CUSTOMERS, Customers.class)
            .token(token)).get();

        soft.assertThat(response.getStatusCode())
            .isEqualTo(HttpStatus.SC_OK);

        String customerIdentity = response.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<Sites> sitesResponse = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_SITES, Sites.class)
            .token(token)
            .inlineVariables(customerIdentity)).get();

        soft.assertThat(sitesResponse.getStatusCode())
            .isEqualTo(HttpStatus.SC_OK);
        soft.assertThat(sitesResponse.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);

        String siteId = sitesResponse.getResponseEntity().getItems().get(0).getSiteId();

        ResponseWrapper<ValidateSite> siteResponse = CasTestUtil.validateSite(customerIdentity, siteId);

        soft.assertThat(siteResponse.getStatusCode())
            .isEqualTo(HttpStatus.SC_OK);
        soft.assertThat(siteResponse.getResponseEntity().getStatus())
            .isEqualTo("EXISTS");
        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5648"})
    @Description("Create a new Site for the Customer")
    public void createCustomerSite() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();

        ResponseWrapper<Customer> response = CasTestUtil.addCustomer(customerName, cloudRef, description, email);

        soft.assertThat(response.getResponseEntity().getName())
            .isEqualTo(customerName);

        customerIdentity = response.getResponseEntity().getIdentity();

        ResponseWrapper<Site> site = CasTestUtil.addSite(customerIdentity, siteID, siteName);

        soft.assertThat(site.getStatusCode())
            .isEqualTo(HttpStatus.SC_CREATED);
        soft.assertThat(site.getResponseEntity().getSiteId())
            .isEqualTo(siteID);
        soft.assertAll();
    }
}