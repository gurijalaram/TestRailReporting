package com.apriori;

import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.models.response.CasErrorMessage;
import com.apriori.cas.models.response.Customer;
import com.apriori.cas.models.response.LicenseByIdentity;
import com.apriori.cas.models.response.LicenseResponse;
import com.apriori.cas.models.response.Licenses;
import com.apriori.cas.models.response.Site;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.cas.utils.Constants;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    private UserCredentials currentUser = UserUtil.getUser();

    @BeforeEach
    public void setUp() {
        RequestEntityUtil.useTokenForRequests(currentUser.getToken());
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

    @AfterEach
    public void cleanUp() {
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity
            );
        }
    }

    @Test
    @Description("Gets licenses for a customer site")
    @TestRail(id = {5653})
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
    @TestRail(id = {5654})
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
    @TestRail(id = {16261})
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
    @TestRail(id = {16280, 16281})
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