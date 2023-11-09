package com.apriori.cas.ui.tests.customer.users;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cas.ui.pageobjects.customer.CustomerWorkspacePage;
import com.apriori.cas.ui.pageobjects.customer.users.profile.UserProfilePage;
import com.apriori.cas.ui.pageobjects.login.CasLoginPage;
import com.apriori.cas.ui.pageobjects.newcustomer.CustomerProfilePage;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.response.InstallationItems;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.Constants;
import com.apriori.cds.api.utils.RandomCustomerData;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.Deployment;
import com.apriori.shared.util.models.response.LicensedApplications;
import com.apriori.shared.util.models.response.Site;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class EditUserTests extends TestBaseUI {
    private final String appIdentity = Constants.getApProApplicationIdentity();
    private final String ciaIdentity = Constants.getCiaApplicationIdentity();
    private final String cirIdentity = Constants.getCirAppIdentity();
    private final String acsIdentity = Constants.getACSAppIdentity();
    private final CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private static final String USER_NAME = new GenerateStringUtil().generateUserName();

    private Customer targetCustomer;
    private CustomerWorkspacePage customerViewPage;
    private String customerIdentity;
    private String email;
    private String userIdentity;
    private UserProfilePage userProfilePage;
    private UserCredentials currentUser = UserUtil.getUser();
    private String licensedApProIdentity;
    private String licensedCiaIdentity;
    private String licensedCirIdentity;
    private String licensedAcsIdentity;
    private String installationIdentity;
    private String siteIdentity;

    @BeforeEach
    public void setup() {
        setCustomerData();
        userProfilePage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .openCustomer(customerIdentity)
            .goToUsersPage()
            .goToCustomerStaff()
            .clickNew()
            .formFillNewUserDetails(USER_NAME, USER_NAME + "@" + email + ".com", "Test", "User")
            .save(UserProfilePage.class);
    }

    @AfterEach
    public void teardown() {
        if (installationIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.INSTALLATION_BY_ID, installationIdentity);
        }
        if (licensedApProIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedApProIdentity);
        }
        if (licensedCiaIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedCiaIdentity);
        }
        if (licensedCirIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedCirIdentity);
        }
        if (licensedAcsIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_LICENSED_APPLICATIONS_BY_IDS, customerIdentity, siteIdentity, licensedAcsIdentity);
        }
        cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, targetCustomer.getIdentity());
    }

    @Test
    @Tag(SMOKE)
    @Description("Test user profile details, edit mode and cancel edit button")
    @TestRail(id = {5576, 11952, 4374, 4382, 11962})
    public void testEditAndCancelUserProfile() {
        userIdentity = userProfilePage.getUserIdentity();
        SoftAssertions soft = new SoftAssertions();
        List<String> userProfileFields = Arrays.asList(
            "username",
            "identity",
            "email",
            "userType",
            "userProfile\\.givenName",
            "userProfile\\.familyName",
            "userProfile\\.prefix",
            "userProfile\\.suffix",
            "userProfile\\.jobTitle",
            "userProfile\\.department",
            "userProfile\\.townCity",
            "userProfile\\.county",
            "userProfile\\.countryCode",
            "userProfile\\.timezone",
            "userProfile\\.officePhoneCountryCode",
            "userProfile\\.officePhoneNumber",
            "authenticationType"
        );

        List<String> editModeFields = Arrays.asList(
            "userProfile.givenName",
            "userProfile.familyName",
            "userProfile.prefix",
            "userProfile.suffix",
            "userProfile.jobTitle",
            "userProfile.department",
            "userProfile.townCity",
            "userProfile.county",
            "country-code",
            "timezone",
            "userProfile.officePhoneCountryCode",
            "userProfile.officePhoneNumber"
        );

        userProfilePage.assertNonEditable(userProfileFields, soft);

        soft.assertThat(userProfilePage.edit())
            .overridingErrorMessage("Expected edit button to be displayed and clickable.")
            .isNotNull();

        userProfilePage.assertNonEditable(Arrays.asList("username", "identity", "email", "userType"), soft)
            .assertEditable(editModeFields, soft)
            .assertButtonAvailable(soft, "Cancel")
            .assertButtonAvailable(soft, "Save");

        userProfilePage.cancel()
            .assertButtonAvailable(soft, "Edit")
            .assertNonEditable(userProfileFields, soft);

        soft.assertAll();
    }

    @Test
    @Tag(SMOKE)
    @Description("Test that user details can be edited and saved")
    @TestRail(id = {11963})
    public void testEditUserAndSave() {
        userIdentity = userProfilePage.getUserIdentity();
        SoftAssertions soft = new SoftAssertions();

        userProfilePage = userProfilePage.edit();

        soft.assertThat(userProfilePage.canSave())
            .overridingErrorMessage("Expected save button to be disabled.")
            .isFalse();

        String newGivenName = "EditedGivenName";

        userProfilePage.editGivenName(newGivenName)
            .save();

        String savedGivenName = userProfilePage.getReadOnlyLabel("userProfile\\.givenName").getText();
        soft.assertThat(savedGivenName)
            .overridingErrorMessage(String.format("Expected changed description to equal %s. Actual %s.", newGivenName, savedGivenName))
            .isEqualTo(newGivenName);

        soft.assertAll();
    }

    @Test
    @Disabled("Status field is disabled")
    @Description("Status field is greyed out (non editable) if Customer is set to inactive")
    @TestRail(id = {10644, 10645})
    public void testUserStatusFieldNotEditableIfCustomerIsDisabled() {
        SoftAssertions soft = new SoftAssertions();
        UserProfilePage checkUserStatus = userProfilePage.edit();

        soft.assertThat(checkUserStatus.isStatusCheckboxEditable()).isTrue();

        userProfilePage.cancel();

        userIdentity = userProfilePage.getUserIdentity();

        CustomerProfilePage deactivateCustomer = userProfilePage.backToUsersListPage(CustomerWorkspacePage.class)
            .goToProfile()
            .clickEditButton()
            .changeCustomerStatus()
            .clickSaveButton();

        soft.assertThat(deactivateCustomer.getStatus()).isEqualTo("Inactive");

        customerViewPage = new CustomerWorkspacePage(driver);
        UserProfilePage checkIfReadOnly = customerViewPage.goToUsersPage()
            .goToCustomerStaff()
            .selectUser(customerIdentity, userIdentity, USER_NAME)
            .edit();

        soft.assertThat(checkIfReadOnly.isStatusCheckboxEditable()).isFalse();

        checkIfReadOnly.cancel();

        CustomerProfilePage activateCustomer = userProfilePage.backToUsersListPage(CustomerWorkspacePage.class)
            .goToProfile()
            .clickEditButton()
            .changeCustomerStatus()
            .clickSaveButton();

        soft.assertThat(activateCustomer.getStatus()).isEqualTo("Active");
        soft.assertAll();
    }

    private void setCustomerData() {
        RandomCustomerData rcd = new RandomCustomerData();
        String customerName = new GenerateStringUtil().generateCustomerName();
        email = customerName.toLowerCase();
        targetCustomer = cdsTestUtil.addCASCustomer(customerName, rcd.getCloudRef(), email, currentUser).getResponseEntity();
        customerIdentity = targetCustomer.getIdentity();
        ResponseWrapper<Site> site = cdsTestUtil.addSite(customerIdentity, rcd.getSiteName(), rcd.getSiteID());
        siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<Deployment> response = cdsTestUtil.addDeployment(customerIdentity, "Production Deployment", siteIdentity, "PRODUCTION");
        String deploymentIdentity = response.getResponseEntity().getIdentity();

        ResponseWrapper<InstallationItems> installation = cdsTestUtil.addInstallation(customerIdentity, deploymentIdentity, "Automation Installation", rcd.getRealmKey(), rcd.getCloudRef(), siteIdentity, false);
        installationIdentity = installation.getResponseEntity().getIdentity();

        ResponseWrapper<LicensedApplications> licensedApp = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, appIdentity);
        licensedApProIdentity = licensedApp.getResponseEntity().getIdentity();
        ResponseWrapper<LicensedApplications> ciaLicensed = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, ciaIdentity);
        licensedCiaIdentity = ciaLicensed.getResponseEntity().getIdentity();
        ResponseWrapper<LicensedApplications> cirLicensed = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, cirIdentity);
        licensedCirIdentity = cirLicensed.getResponseEntity().getIdentity();
        ResponseWrapper<LicensedApplications> ascLicensed = cdsTestUtil.addApplicationToSite(customerIdentity, siteIdentity, acsIdentity);
        licensedAcsIdentity = ascLicensed.getResponseEntity().getIdentity();

        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, appIdentity, siteIdentity);
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, ciaIdentity, siteIdentity);
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, cirIdentity, siteIdentity);
        cdsTestUtil.addApplicationInstallation(customerIdentity, deploymentIdentity, installationIdentity, acsIdentity, siteIdentity);
    }
}