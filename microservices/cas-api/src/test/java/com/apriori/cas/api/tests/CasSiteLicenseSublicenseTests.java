package com.apriori.cas.api.tests;

import com.apriori.cas.api.enums.CASAPIEnum;
import com.apriori.cas.api.models.response.CasErrorMessage;
import com.apriori.cas.api.models.response.Customer;
import com.apriori.cas.api.models.response.LicenseResponse;
import com.apriori.cas.api.models.response.Site;
import com.apriori.cas.api.models.response.SubLicense;
import com.apriori.cas.api.models.response.SubLicenses;
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
        String subLicenseId = UUID.randomUUID().toString();

        ResponseWrapper<LicenseResponse> licenseResponse = casTestUtil.addLicense(casLicense, customerIdentity, siteIdentity, customerName, siteID, subLicenseId);
        licenseIdentity = licenseResponse.getResponseEntity().getIdentity();
    }

    @AfterEach
    public void cleanUp() {
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity
            );
        }
    }

    @Test
    @Description("Get the sub License identified by its identity for the customer site in the given license.")
    @TestRail(id = {5656})
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
    @TestRail(id = {16377})
    public void getSubLicenseThatNotExist() {
        String notExistingSubLicenseId = "000000000000";

        ResponseWrapper<CasErrorMessage> getNotExistingSubLicense = casTestUtil.getCommonRequest(CASAPIEnum.SUBLICENSE_BY_ID, CasErrorMessage.class, HttpStatus.SC_NOT_FOUND, customerIdentity, siteIdentity, licenseIdentity, notExistingSubLicenseId);
        soft.assertThat(getNotExistingSubLicense.getResponseEntity().getMessage())
            .isEqualTo(String.format("Unable to get SubLicense with identity '%s' for license with identity '%s' and Site with identity '%s'.", notExistingSubLicenseId, licenseIdentity, siteIdentity));
        soft.assertAll();
    }
}