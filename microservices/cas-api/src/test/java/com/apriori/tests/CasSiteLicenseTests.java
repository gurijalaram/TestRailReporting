package com.apriori.tests;

import com.apriori.entity.response.Customer;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.cas.utils.Constants;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.entity.response.CasErrorMessage;
import com.apriori.entity.response.LicenseByIdentity;
import com.apriori.entity.response.LicenseResponse;
import com.apriori.entity.response.Licenses;
import com.apriori.entity.response.Site;
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

public class CasSiteLicenseTests {
    private SoftAssertions soft = new SoftAssertions();
    private String customerName;
    private String customerIdentity;
    private String cloudRef;
    private String email;
    private String description;
    private String siteName;
    private String siteID;
    private String siteIdentity;
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
    }

    @After
    public void cleanUp() {
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity
            );
        }
    }

    @Test
    @Description("Gets licenses for a customer site")
    @TestRail(testCaseId = {"5653"})
    public void getSitesLicenses() {
        String subLicenseId = UUID.randomUUID().toString();

        casTestUtil.addLicense(Constants.CAS_LICENSE, customerIdentity, siteIdentity, customerName, siteID, subLicenseId);

        ResponseWrapper<Licenses> siteLicenses = casTestUtil.getCommonRequest(CASAPIEnum.LICENSE_BY_CUSTOMER_SITE_IDS, Licenses.class, HttpStatus.SC_OK, customerIdentity, siteIdentity);

        soft.assertThat(siteLicenses.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }

    @Test
    @Description("Gets license details by license identity")
    @TestRail(testCaseId = {"5654"})
    public void getSiteLicenseByIdentity() {
        String subLicenseId = UUID.randomUUID().toString();

        ResponseWrapper<LicenseResponse> licenseResponse = casTestUtil.addLicense(Constants.CAS_LICENSE, customerIdentity, siteIdentity, customerName, siteID, subLicenseId);

        String licenseIdentity = licenseResponse.getResponseEntity().getIdentity();

        ResponseWrapper<LicenseByIdentity> license = casTestUtil.getCommonRequest(CASAPIEnum.LICENSE_BY_ID, LicenseByIdentity.class, HttpStatus.SC_OK, customerIdentity, siteIdentity, licenseIdentity);

        soft.assertThat(license.getResponseEntity().getIdentity())
            .isEqualTo(licenseIdentity);
        soft.assertAll();
    }

    @Test
    @Description("Activates a site license")
    @TestRail(testCaseId = {"16261"})
    public void activateLicense() {
        String subLicenseId = UUID.randomUUID().toString();

        ResponseWrapper<LicenseResponse> licenseResponse = casTestUtil.addLicense(Constants.CAS_LICENSE, customerIdentity, siteIdentity, customerName, siteID, subLicenseId);

        String licenseIdentity = licenseResponse.getResponseEntity().getIdentity();

        ResponseWrapper<LicenseResponse> activateLicenseResponse = casTestUtil.activateLicense(LicenseResponse.class, customerIdentity, siteIdentity, licenseIdentity, HttpStatus.SC_CREATED);
        soft.assertThat(activateLicenseResponse.getResponseEntity().getActive())
            .isTrue();
        soft.assertAll();
    }

    @Test
    @Description("Get license with not existing identity and activate")
    @TestRail(testCaseId = {"16280", "16281"})
    public void getAndActivateLicenseThatNotExist() {
        String notExistingLicenseId = "000000000000";

        ResponseWrapper<CasErrorMessage> getNotExistingLicense = casTestUtil.getCommonRequest(CASAPIEnum.LICENSE_BY_ID, CasErrorMessage.class, HttpStatus.SC_NOT_FOUND, customerIdentity, siteIdentity, notExistingLicenseId);
        soft.assertThat(getNotExistingLicense.getResponseEntity().getMessage())
            .isEqualTo(String.format("Resource 'License' with identity '%s' was not found", notExistingLicenseId));

        ResponseWrapper<CasErrorMessage> activateNotExistingLicense = casTestUtil.activateLicense(CasErrorMessage.class, customerIdentity, siteIdentity, notExistingLicenseId, HttpStatus.SC_NOT_FOUND);
        soft.assertThat(activateNotExistingLicense.getResponseEntity().getMessage())
            .isEqualTo(String.format("Resource 'License' with identity '%s' was not found", notExistingLicenseId));
        soft.assertAll();
    }
}