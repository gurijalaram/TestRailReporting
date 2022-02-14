package com.sitesAndLicenses;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.Sites;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.login.CasLoginPage;
import com.apriori.newcustomer.SitesLicensesPage;
import com.apriori.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class LicenseTests extends TestBase {
    private SitesLicensesPage sitesLicensesPage;
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
        ResponseWrapper<Sites> aprioriInternalSites = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_SITE_BY_CUSTOMER_ID, Sites.class, customerIdentity);
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
    public void validateUploadInvalidStructureLicense() {

        sitesLicensesPage.uploadLicense(Constants.INVALID_STRUCTURE_LICENSE, customerName, siteId, subLicenseId, SitesLicensesPage.class);
        assertThat(sitesLicensesPage.getErrorMessage(), containsString("Cannot upload license file. The internal structure of the license file is invalid."));

        String missingCustomerName = "";
        sitesLicensesPage.uploadLicense(Constants.CAS_LICENSE, missingCustomerName, siteId, subLicenseId, SitesLicensesPage.class);
                assertThat(sitesLicensesPage.getErrorMessage(), containsString("Cannot upload license file. The license does not specify the customer name."));

        String customerName = "NotExistingCustomerName";
        sitesLicensesPage.uploadLicense(Constants.CAS_LICENSE, customerName, siteId, subLicenseId, SitesLicensesPage.class);
                assertThat(sitesLicensesPage.getErrorMessage(), containsString("Cannot upload license file. The customer specified in the license file does not exist."));

    }

    @Test
    public void validateUploadMissingCustomerNameLicense() {
        String missingCustomerName = "";

        sitesLicensesPage.uploadLicense(Constants.CAS_LICENSE, missingCustomerName, siteId, subLicenseId, SitesLicensesPage.class);
    }

    @Test
    public void validateUploadLicenseNotExistingCustomer() {
        String customerName = "NotExistingCustomerName";

        sitesLicensesPage.uploadLicense(Constants.CAS_LICENSE, customerName, siteId, subLicenseId, SitesLicensesPage.class);
        assertThat(sitesLicensesPage.getErrorMessage(), is(equalTo("Cannot upload license file. The customer specified in the license file does not exist.")));
    }

    @Test
    public void validateUploadLicenseCustomerDoesntMatch() {
        String customerName = "Gadgets";

        sitesLicensesPage.uploadLicense(Constants.CAS_LICENSE, customerName, siteId, subLicenseId, SitesLicensesPage.class);
        assertThat(sitesLicensesPage.getErrorMessage(), is(equalTo("Cannot upload license file. The customer specified by the license does not match the current customer.")));
    }

    @Test
    public void validateUploadLicenseMissingSiteId() {
        String missingSiteId = "";

        sitesLicensesPage.uploadLicense(Constants.CAS_LICENSE, customerName, missingSiteId, subLicenseId, SitesLicensesPage.class);
        assertThat(sitesLicensesPage.getErrorMessage(), is(equalTo("Cannot upload license file. The license is missing the site ID")));
    }

    @Test
    public void validateUploadLicenseSiteIdUsedByAnotherCustomer() {
        String siteId = "17a6c35b-efb4-4651-87f1-2f9e4341247f";

        sitesLicensesPage.uploadLicense(Constants.CAS_LICENSE, customerName, siteId, subLicenseId, SitesLicensesPage.class);
        assertThat(sitesLicensesPage.getErrorMessage(), is(equalTo("Cannot upload license file. The license site ID is in use by another customer.")));
    }

    @Test
    public void validateUploadLicenseSiteIdIsNotExist() {
          String siteId = new GenerateStringUtil().generateSiteID();

        sitesLicensesPage.uploadLicense(Constants.CAS_LICENSE, customerName, siteId, subLicenseId, SitesLicensesPage.class);
        assertThat(sitesLicensesPage.getErrorMessage(), is(equalTo("Cannot upload license file.  License file site ID does not exist in the customer record.")));
    }
}
