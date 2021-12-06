package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.not;

import com.apriori.apibase.services.cas.Customer;
import com.apriori.apibase.services.cas.Customers;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.entity.response.Site;
import com.apriori.entity.response.Sites;
import com.apriori.entity.response.ValidateSite;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class CasSitesTests {

    private String token;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    @Before
    public void getToken() {
        token = new JwtTokenUtil().retrieveJwtToken();
    }

    @Test
    @TestRail(testCaseId = {"5649"})
    @Description("Returns a list of sites for the customer")
    public void getCustomerSites() {
        ResponseWrapper<Customers> response = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_CUSTOMER, Customers.class)
            .token(token)).get();

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        String identity = response.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<Sites> siteResponse = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.POST_SITES, Sites.class)
            .token(token)
            .inlineVariables(identity)).get();

        assertThat(siteResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(siteResponse.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
        assertThat(siteResponse.getResponseEntity().getItems().get(0).getSiteId(), is(not(emptyString())));
    }

    @Test
    @TestRail(testCaseId = {"5650"})
    @Description("Get the Site identified by its identity.")
    public void getSiteByIdentity() {
        ResponseWrapper<Customers> response = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_CUSTOMER, Customers.class)
            .token(token)).get();

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        String customerIdentity = response.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<Sites> sitesResponse = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.POST_SITES, Sites.class)
            .token(token)
            .inlineVariables(customerIdentity)).get();

        assertThat(sitesResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(sitesResponse.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));

        String siteIdentity = sitesResponse.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<Site> site = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.POST_SITES_ID, Site.class)
            .token(token)
            .inlineVariables(customerIdentity, siteIdentity)).get();

        assertThat(site.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(site.getResponseEntity().getIdentity(), is(equalTo(siteIdentity)));
    }

    @Test
    @TestRail(testCaseId = {"5651"})
    @Description("Validates Customer's Site record by site ID.")
    public void validateCustomerSite() {
        ResponseWrapper<Customers> response = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_CUSTOMER, Customers.class)
            .token(token)).get();

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        String identity = response.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<Sites> sitesResponse = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.POST_SITES, Sites.class)
            .token(token)
            .inlineVariables(identity)).get();

        assertThat(sitesResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(sitesResponse.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));

        String siteId = sitesResponse.getResponseEntity().getItems().get(0).getSiteId();

        ResponseWrapper<ValidateSite> siteResponse = CasTestUtil.validateSite(identity, siteId);

        assertThat(siteResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(siteResponse.getResponseEntity().getResponse().getStatus(), is(equalTo("EXISTS")));
    }

    @Test
    @Issue("MIC-1678")
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

        assertThat(response.getResponseEntity().getName(), is(equalTo(customerName)));

        String identity = response.getResponseEntity().getIdentity();

        ResponseWrapper<Site> site = CasTestUtil.addSite(identity, siteID, siteName);

        assertThat(site.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(site.getResponseEntity().getSiteId(), is(equalTo(siteID)));
    }
}