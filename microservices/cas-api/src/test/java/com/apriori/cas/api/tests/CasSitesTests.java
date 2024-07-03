package com.apriori.cas.api.tests;

import com.apriori.cas.api.enums.CASAPIEnum;
import com.apriori.cas.api.models.response.Customer;
import com.apriori.cas.api.models.response.Site;
import com.apriori.cas.api.models.response.Sites;
import com.apriori.cas.api.models.response.ValidateSite;
import com.apriori.cas.api.utils.CasTestUtil;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
@EnabledIf(value = "com.apriori.shared.util.properties.PropertiesContext#isAPCustomer")
public class CasSitesTests {
    private CasTestUtil casTestUtil;
    private SoftAssertions soft = new SoftAssertions();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private String customerIdentity;
    private CdsTestUtil cdsTestUtil;

    @BeforeEach
    public void setup() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser()
            .useTokenInRequests();
        casTestUtil = new CasTestUtil(requestEntityUtil);
        cdsTestUtil = new CdsTestUtil(requestEntityUtil);
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
        String aprioriIdentity = casTestUtil.getAprioriInternal().getIdentity();
        ResponseWrapper<Sites> siteResponse = casTestUtil.getCommonRequest(CASAPIEnum.SITES, Sites.class, HttpStatus.SC_OK, aprioriIdentity);

        soft.assertThat(siteResponse.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);
        soft.assertThat(siteResponse.getResponseEntity().getItems().get(0).getSiteId())
            .isNotEmpty();
    }

    @Test
    @TestRail(id = {5650})
    @Description("Get the Site identified by its identity.")
    public void getSiteByIdentity() {
        String aprioriIdentity = casTestUtil.getAprioriInternal().getIdentity();
        ResponseWrapper<Sites> sitesResponse = casTestUtil.getCommonRequest(CASAPIEnum.SITES, Sites.class, HttpStatus.SC_OK, aprioriIdentity);

        soft.assertThat(sitesResponse.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);

        String siteIdentity = sitesResponse.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<Site> site = casTestUtil.getCommonRequest(CASAPIEnum.SITE_ID, Site.class, HttpStatus.SC_OK, aprioriIdentity, siteIdentity);

        soft.assertThat(site.getResponseEntity().getIdentity())
            .isEqualTo(siteIdentity);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5651})
    @Description("Validates Customer's Site record by site ID.")
    public void validateCustomerSite() {
        String aprioriIdentity = casTestUtil.getAprioriInternal().getIdentity();
        ResponseWrapper<Sites> sitesResponse = casTestUtil.getCommonRequest(CASAPIEnum.SITES, Sites.class, HttpStatus.SC_OK, aprioriIdentity);

        soft.assertThat(sitesResponse.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);

        String siteId = sitesResponse.getResponseEntity().getItems().get(0).getSiteId();

        ResponseWrapper<ValidateSite> siteResponse = casTestUtil.validateSite(aprioriIdentity, siteId);

        soft.assertThat(siteResponse.getResponseEntity().getStatus())
            .isEqualTo("EXISTS");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5648})
    @Description("Create a new Site for the Customer")
    public void createCustomerSite() {
        String customerName = generateStringUtil.generateAlphabeticString("Customer", 6);
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";
        String siteName = generateStringUtil.generateAlphabeticString("Site", 5);
        String siteID = generateStringUtil.generateSiteID();

        ResponseWrapper<Customer> response = casTestUtil.addCustomer(customerName, cloudRef, description, email);

        soft.assertThat(response.getResponseEntity().getName())
            .isEqualTo(customerName);

        customerIdentity = response.getResponseEntity().getIdentity();

        ResponseWrapper<Site> site = casTestUtil.addSite(customerIdentity, siteID, siteName);

        soft.assertThat(site.getResponseEntity().getSiteId())
            .isEqualTo(siteID);
        soft.assertAll();
    }
}