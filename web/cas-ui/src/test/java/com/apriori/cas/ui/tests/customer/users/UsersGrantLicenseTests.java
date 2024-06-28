package com.apriori.cas.ui.tests.customer.users;

import com.apriori.cas.ui.components.CardsViewComponent;
import com.apriori.cas.ui.components.SourceListComponent;
import com.apriori.cas.ui.pageobjects.customer.users.UsersListPage;
import com.apriori.cas.ui.pageobjects.customer.users.profile.UserProfilePage;
import com.apriori.cas.ui.pageobjects.login.CasLoginPage;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.response.LicenseResponse;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.LicenseUtil;
import com.apriori.cds.api.utils.CdsUserUtil;
import com.apriori.cds.api.utils.SiteUtil;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.Obligation;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.Site;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;
import com.apriori.web.app.util.PageUtils;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.UUID;

@Disabled("Feature is not available on qa test now")
public class UsersGrantLicenseTests extends TestBaseUI {
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil;
    private LicenseUtil licenseUtil;
    private SiteUtil siteUtil;
    private Customer targetCustomer;
    private String customerIdentity;
    private String customerName;
    private String siteName;
    private String siteIdentity;
    private User user;
    private String userIdentity;
    private String userName;
    private ResponseWrapper<LicenseResponse> license;
    private String licenseId;
    private String siteID;
    private String subLicenseId;
    private String licenseName;
    private String subLicenseName;
    private UserProfilePage userProfilePage;
    private UsersListPage usersListPage;
    private CdsUserUtil cdsUserUtil;

    @BeforeEach
    public void setup() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        cdsTestUtil = new CdsTestUtil(requestEntityUtil);
        cdsUserUtil = new CdsUserUtil(requestEntityUtil);
        licenseUtil = new LicenseUtil(requestEntityUtil);
        siteUtil = new SiteUtil(requestEntityUtil);

        customerName = generateStringUtil.generateAlphabeticString("Customer", 6);
        userName = generateStringUtil.generateUserName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        targetCustomer = cdsTestUtil.addCASCustomer(customerName, cloudRef, email, requestEntityUtil.getEmbeddedUser()).getResponseEntity();
        customerIdentity = targetCustomer.getIdentity();
        user = cdsUserUtil.addUser(customerIdentity, userName, email).getResponseEntity();
        userIdentity = user.getIdentity();
        siteName = generateStringUtil.generateAlphabeticString("Site", 5);
        siteID = generateStringUtil.generateSiteID();
        ResponseWrapper<Site> site = siteUtil.addSite(customerIdentity, siteName, siteID);
        siteIdentity = site.getResponseEntity().getIdentity();
        licenseId = UUID.randomUUID().toString();
        subLicenseId = UUID.randomUUID().toString();

        usersListPage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .openCustomer(customerIdentity)
            .goToUsersPage()
            .goToCustomerStaff();
    }

    @AfterEach
    public void cleanUp() {
        if (userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS,
                customerIdentity,
                userIdentity
            );
        }
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @Description("Grant sublicense to a customer user")
    @TestRail(id = {16825, 16815, 16816, 16817, 16818, 16819, 16821, 16823, 16824, 16827})
    public void grantLicenseToAUser() {
        license = licenseUtil.addLicense(customerIdentity, siteIdentity, customerName, siteID, licenseId, subLicenseId);
        licenseName = license.getResponseEntity().getDescription();
        subLicenseName = license.getResponseEntity().getSubLicenses().get(0).getName();

        userProfilePage = usersListPage.selectUser(customerIdentity, userIdentity, userName);

        SoftAssertions soft = new SoftAssertions();
        PageUtils utils = new PageUtils(driver);
        SourceListComponent grantedLicenses = userProfilePage.getGrantedLicensesContainer();

        CardsViewComponent availableLicense = Obligation.mandatory(grantedLicenses::getCardGrid, "The license was not found");
        long cards = availableLicense.getCards("apriori-card").count();

        soft.assertThat(cards).isEqualTo(1L);
        soft.assertThat(userProfilePage.getLicenseName()).isEqualTo("None (Unassigned)");
        soft.assertThat(userProfilePage.getSubLicenseName()).isEqualTo("None (Unassigned)");

        UserProfilePage addLicense = userProfilePage
            .clickModifyLicenseButton()
            .cancel()
            .clickModifyLicenseButton()
            .clickCloseModalButton()
            .clickModifyLicenseButton();

        soft.assertThat(addLicense.canSave()).isFalse();

        addLicense.selectLicense(licenseName)
            .selectSubLicense(subLicenseName)
            .save();
        utils.waitForCondition(availableLicense::isStable, PageUtils.DURATION_LOADING);

        soft.assertThat(addLicense.getLicenseName()).isEqualTo(licenseName);
        soft.assertThat(addLicense.getSubLicenseName()).isEqualTo(subLicenseName);

        addLicense.clickModifyLicenseButton()
            .deleteLicenseFromUser()
            .save()
            .refreshLicensesContainer();
        utils.waitForCondition(availableLicense::isStable, PageUtils.DURATION_LOADING);

        soft.assertThat(addLicense.getLicenseName()).isEqualTo("None (Unassigned)");
        soft.assertThat(addLicense.getSubLicenseName()).isEqualTo("None (Unassigned)");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {16810, 16811})
    @Description("Validates empty granted licenses container")
    public void emptyLicenseContainer() {
        SoftAssertions soft = new SoftAssertions();
        userProfilePage = usersListPage.selectUser(customerIdentity, userIdentity, userName);
        SourceListComponent grantedLicenses = userProfilePage.getGrantedLicensesContainer();

        soft.assertThat(grantedLicenses.canRefresh())
            .overridingErrorMessage("The container is missing refresh button")
            .isTrue();
        soft.assertThat(userProfilePage.getNoLicensesMessage()).isEqualTo("No licenses have been issued for this customer");
        soft.assertAll();
    }
}