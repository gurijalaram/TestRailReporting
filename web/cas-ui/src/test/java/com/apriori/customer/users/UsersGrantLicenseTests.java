package com.apriori.customer.users;

import com.apriori.PageUtils;
import com.apriori.TestBaseUI;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.models.response.Customer;
import com.apriori.cds.models.response.LicenseResponse;
import com.apriori.models.response.Site;
import com.apriori.cds.models.response.User;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.components.CardsViewComponent;
import com.apriori.components.SourceListComponent;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.Obligation;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.pageobjects.customer.users.UsersListPage;
import com.apriori.pageobjects.customer.users.profile.UserProfilePage;
import com.apriori.pageobjects.login.CasLoginPage;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

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

    @BeforeEach
    public void setup() {
        customerName = generateStringUtil.generateCustomerName();
        userName = generateStringUtil.generateUserName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        cdsTestUtil = new CdsTestUtil();
        targetCustomer = cdsTestUtil.addCASCustomer(customerName, cloudRef, email).getResponseEntity();
        customerIdentity = targetCustomer.getIdentity();
        user = cdsTestUtil.addUser(customerIdentity, userName, email).getResponseEntity();
        userIdentity = user.getIdentity();
        siteName = generateStringUtil.generateSiteName();
        siteID = generateStringUtil.generateSiteID();
        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, siteName, siteID);
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
        license = cdsTestUtil.addLicense(customerIdentity, siteIdentity, customerName, siteID, licenseId, subLicenseId);
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