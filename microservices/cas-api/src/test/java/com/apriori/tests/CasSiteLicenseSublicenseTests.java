package com.apriori.tests;

import com.apriori.entity.response.Customer;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.cas.utils.Constants;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.entity.response.CasErrorMessage;
import com.apriori.entity.response.LicenseResponse;
import com.apriori.entity.response.Site;
import com.apriori.entity.response.SubLicense;
import com.apriori.entity.response.SubLicenses;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class CasSiteLicenseSublicenseTests {
    private SoftAssertions soft = new SoftAssertions();
    private String customerName;
    private String customerIdentity;
    private String cloudRef;
    private String email;
    private String description;
    private String siteName;
    private String siteID;
    private String siteIdentity;
    private String licenseIdentity;
    private CasTestUtil casTestUtil = new CasTestUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    @Before
    public void setUp() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
        customerName = generateStringUtil.generateCustomerName();
        cloudRef = generateStringUtil.generateCloudReference();
        email = customerName.toLowerCase();
        description = customerName + " Description";
        siteName = generateStringUtil.generateSiteName();
        siteID = generateStringUtil.generateSiteID();

        ResponseWrapper<Customer> customer = CasTestUtil.addCustomer(customerName, cloudRef, description, email);
        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<Site> site = CasTestUtil.addSite(customerIdentity, siteID, siteName);
        siteIdentity = site.getResponseEntity().getIdentity();
        String subLicenseId = UUID.randomUUID().toString();

        ResponseWrapper<LicenseResponse> licenseResponse = casTestUtil.addLicense(Constants.CAS_LICENSE, customerIdentity, siteIdentity, customerName, siteID, subLicenseId);
        licenseIdentity = licenseResponse.getResponseEntity().getIdentity();
    }

    @After
    public void cleanUp() {
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity
            );
        }
    }

    @Test
    @Description("Get the sub License identified by its identity for the customer site in the given license.")
    @TestRail(testCaseId = {"5656"})
    public void getSublicenseById() {
        ResponseWrapper<SubLicenses> subLicenses = casTestUtil.getCommonRequest(CASAPIEnum.SUBLICENSES_BY_LICENSE_ID, SubLicenses.class, HttpStatus.SC_OK,
            customerIdentity,
            siteIdentity,
            licenseIdentity);

        String subLicenseIdentity = subLicenses.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<SubLicense> sublicense = casTestUtil.getCommonRequest(CASAPIEnum.SUBLICENSE_BY_ID, SubLicense.class, HttpStatus.SC_OK,
            customerIdentity,
            siteIdentity,
            licenseIdentity,
            subLicenseIdentity);

        soft.assertThat(sublicense.getResponseEntity().getIdentity())
            .isEqualTo(subLicenseIdentity);
        soft.assertAll();
    }

    @Test
    @Description("Get sublicense with not existing identity")
    @TestRail(testCaseId = {"16377"})
    public void getSubLicenseThatNotExist() {
        String notExistingSubLicenseId = "000000000000";

        ResponseWrapper<CasErrorMessage> getNotExistingSubLicense = casTestUtil.getCommonRequest(CASAPIEnum.SUBLICENSE_BY_ID, CasErrorMessage.class, HttpStatus.SC_NOT_FOUND, customerIdentity, siteIdentity, licenseIdentity, notExistingSubLicenseId);
        soft.assertThat(getNotExistingSubLicense.getResponseEntity().getMessage())
            .isEqualTo(String.format("Unable to get SubLicense with identity '%s' for license with identity '%s' and Site with identity '%s'.", notExistingSubLicenseId, licenseIdentity, siteIdentity));
        soft.assertAll();
    }
}