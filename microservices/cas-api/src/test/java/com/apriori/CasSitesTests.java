package com.apriori;

import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.models.response.Customer;
import com.apriori.cas.models.response.Customers;
import com.apriori.cas.models.response.Site;
import com.apriori.cas.models.response.Sites;
import com.apriori.cas.models.response.ValidateSite;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.AuthorizationUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CasSitesTests {
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private SoftAssertions soft = new SoftAssertions();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private String customerIdentity;
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @BeforeEach
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
    }

    @AfterEach
    public void cleanUp() {
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(id = {5649})
    @Description("Returns a list of sites for the customer")
    public void getCustomerSites() {
        ResponseWrapper<Customers> response = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMERS, Customers.class, HttpStatus.SC_OK);
        String customerIdentity = response.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<Sites> siteResponse = casTestUtil.getCommonRequest(CASAPIEnum.SITES, Sites.class, HttpStatus.SC_OK, customerIdentity);

        soft.assertThat(siteResponse.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);
        soft.assertThat(siteResponse.getResponseEntity().getItems().get(0).getSiteId())
            .isNotEmpty();
    }

    @Test
    @TestRail(id = {5650})
    @Description("Get the Site identified by its identity.")
    public void getSiteByIdentity() {
        ResponseWrapper<Customers> response = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMERS, Customers.class, HttpStatus.SC_OK);
        String customerIdentity = response.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<Sites> sitesResponse = casTestUtil.getCommonRequest(CASAPIEnum.SITES, Sites.class, HttpStatus.SC_OK, customerIdentity);

        soft.assertThat(sitesResponse.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);

        String siteIdentity = sitesResponse.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<Site> site = casTestUtil.getCommonRequest(CASAPIEnum.SITE_ID, Site.class, HttpStatus.SC_OK, customerIdentity, siteIdentity);

        soft.assertThat(site.getResponseEntity().getIdentity())
            .isEqualTo(siteIdentity);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5651})
    @Description("Validates Customer's Site record by site ID.")
    public void validateCustomerSite() {
        ResponseWrapper<Customers> response = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMERS, Customers.class, HttpStatus.SC_OK);
        String customerIdentity = response.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<Sites> sitesResponse = casTestUtil.getCommonRequest(CASAPIEnum.SITES, Sites.class, HttpStatus.SC_OK, customerIdentity);

        soft.assertThat(sitesResponse.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);

        String siteId = sitesResponse.getResponseEntity().getItems().get(0).getSiteId();

        ResponseWrapper<ValidateSite> siteResponse = CasTestUtil.validateSite(customerIdentity, siteId);

        soft.assertThat(siteResponse.getResponseEntity().getStatus())
            .isEqualTo("EXISTS");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5648})
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

        soft.assertThat(site.getResponseEntity().getSiteId())
            .isEqualTo(siteID);
        soft.assertAll();
    }
}