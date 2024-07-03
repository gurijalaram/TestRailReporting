package com.apriori.cas.api.tests;

import com.apriori.cas.api.enums.CASAPIEnum;
import com.apriori.cas.api.models.response.CasErrorMessage;
import com.apriori.cas.api.models.response.Customer;
import com.apriori.cas.api.models.response.LicenseByIdentity;
import com.apriori.cas.api.models.response.LicenseResponse;
import com.apriori.cas.api.models.response.Licenses;
import com.apriori.cas.api.models.response.Site;
import com.apriori.cas.api.utils.CasTestUtil;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import lombok.SneakyThrows;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.extension.ExtendWith;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@ExtendWith(TestRulesAPI.class)
@EnabledIf(value = "com.apriori.shared.util.properties.PropertiesContext#isAPCustomer")
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
    private CasTestUtil casTestUtil;
    private CdsTestUtil cdsTestUtil;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private String casLicense;

    @SneakyThrows
    @BeforeEach
    public void setUp() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser()
            .useTokenInRequests();
        casTestUtil = new CasTestUtil(requestEntityUtil);
        cdsTestUtil = new CdsTestUtil(requestEntityUtil);
        customerName = generateStringUtil.generateAlphabeticString("Customer", 6);
        cloudRef = generateStringUtil.generateCloudReference();
        email = customerName.toLowerCase();
        description = customerName + " Description";
        siteName = generateStringUtil.generateAlphabeticString("Site", 5);
        siteID = generateStringUtil.generateSiteID();
        casLicense = new String(FileResourceUtil.getResourceFileStream("CasLicense.xml").readAllBytes(), StandardCharsets.UTF_8);

        ResponseWrapper<Customer> customer = casTestUtil.addCustomer(customerName, cloudRef, description, email);
        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<Site> site = casTestUtil.addSite(customerIdentity, siteID, siteName);
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

        casTestUtil.addLicense(casLicense, customerIdentity, siteIdentity, customerName, siteID, subLicenseId);

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

        ResponseWrapper<LicenseResponse> licenseResponse = casTestUtil.addLicense(casLicense, customerIdentity, siteIdentity, customerName, siteID, subLicenseId);

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

        ResponseWrapper<LicenseResponse> licenseResponse = casTestUtil.addLicense(casLicense, customerIdentity, siteIdentity, customerName, siteID, subLicenseId);

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