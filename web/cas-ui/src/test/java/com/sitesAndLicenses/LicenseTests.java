package com.sitesAndLicenses;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.Sites;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.login.CasLoginPage;
import com.apriori.siteLicenses.SitesLicensesPage;
import com.apriori.siteLicenses.UploadLicensePopUp;
import com.apriori.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class LicenseTests extends TestBase {
    private SitesLicensesPage sitesLicensesPage;
    private UploadLicensePopUp uploadLicensePopUp;
    private static Customer aprioriInternal;
    private static CdsTestUtil cdsTestUtil;
    private static String customerName;
    private static String siteId;
    private static String subLicenseId;

    @BeforeClass
    public static void getData() {
        cdsTestUtil = new CdsTestUtil();
        aprioriInternal = cdsTestUtil.getAprioriInternal();
        String customerIdentity = aprioriInternal.getIdentity();
        customerName = aprioriInternal.getName();
        ResponseWrapper<Sites> aprioriInternalSites = cdsTestUtil.getCommonRequest(CDSAPIEnum.SITES_BY_CUSTOMER_ID, Sites.class, customerIdentity);
        siteId = aprioriInternalSites.getResponseEntity().getItems().get(0).getSiteId();
        subLicenseId = UUID.randomUUID().toString();
    }

    @Before
    public void setup() {
        sitesLicensesPage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .openAprioriInternal()
            .goToSitesLicenses();
    }

    @Test
    @TestRail(testCaseId = {"5604"})
    @Description("Validate error messages when uploading an invalid license for a customer")
    public void validateUploadInvalidLicenses() {

        sitesLicensesPage.uploadLicense(Constants.INVALID_STRUCTURE_LICENSE, customerName, siteId, subLicenseId, SitesLicensesPage.class);
        assertThat(sitesLicensesPage.getErrorMessage(), is(equalTo("Cannot upload license file. The internal structure of the license file is invalid.")));

        String missingCustomerName = "";
        sitesLicensesPage.uploadLicense(Constants.CAS_LICENSE, missingCustomerName, siteId, subLicenseId, SitesLicensesPage.class);
        assertThat(sitesLicensesPage.getErrorMessage(), is(equalTo("Cannot upload license file. The license does not specify the customer name.")));

        String customerNameNotExist = "NotExistingCustomerName";
        sitesLicensesPage.uploadLicense(Constants.CAS_LICENSE, customerNameNotExist, siteId, subLicenseId, SitesLicensesPage.class);
        assertThat(sitesLicensesPage.getErrorMessage(), is(equalTo("Cannot upload license file. The customer specified in the license file does not exist.")));

        String existingCustomer = "Gadgets";
        sitesLicensesPage.uploadLicense(Constants.CAS_LICENSE, existingCustomer, siteId, subLicenseId, SitesLicensesPage.class);
        assertThat(sitesLicensesPage.getErrorMessage(), is(equalTo("Cannot upload license file. The customer specified by the license does not match the current customer.")));

        String missingSiteId = "";
        sitesLicensesPage.uploadLicense(Constants.CAS_LICENSE, customerName, missingSiteId, subLicenseId, SitesLicensesPage.class);
        assertThat(sitesLicensesPage.getErrorMessage(), is(equalTo("Cannot upload license file. The license is missing the site ID")));

        String anotherCustomerSiteId = "17a6c35b-efb4-4651-87f1-2f9e4341247f";
        sitesLicensesPage.uploadLicense(Constants.CAS_LICENSE, customerName, anotherCustomerSiteId, subLicenseId, SitesLicensesPage.class);
        assertThat(sitesLicensesPage.getErrorMessage(), is(equalTo("Cannot upload license file. The license site ID is in use by another customer.")));

        String siteIdNotExist = new GenerateStringUtil().generateSiteID();
        sitesLicensesPage.uploadLicense(Constants.CAS_LICENSE, customerName, siteIdNotExist, subLicenseId, SitesLicensesPage.class);
        assertThat(sitesLicensesPage.getErrorMessage(), is(equalTo("Cannot upload license file.  License file site ID does not exist in the customer record.")));
    }

    @Test
    @TestRail(testCaseId = {"11024", "11025"})
    @Description("Validate Cancel button in Upload License pop-up, validate required fields for uploading license")
    public void testUploadingLicenseCanBeCanceled() {
        String licenseName = new GenerateStringUtil().getRandomString();
        SoftAssertions soft = new SoftAssertions();
        List<String> labels = Arrays.asList(
                "1. License Confirmation",
                "2. License Details",
                "aPriori Version",
                "Name"
        );

        uploadLicensePopUp = sitesLicensesPage.uploadLicense(Constants.CAS_LICENSE, customerName, siteId, subLicenseId, UploadLicensePopUp.class)
                .testUploadLicenseLabels(labels, soft)
                .testNecessaryFieldsAreRequired(soft);
        soft.assertAll();

        assertThat(uploadLicensePopUp.isLoadButtonEnabled(), is(equalTo(false)));

        sitesLicensesPage = uploadLicensePopUp.enterCustomerName(customerName)
                .selectApVersion("2021 R1")
                .enterLicenseName(licenseName)
                .clickCancel();

        assertThat(sitesLicensesPage.isLicenseCardDisplayed(licenseName), is(equalTo(false)));
    }
}
