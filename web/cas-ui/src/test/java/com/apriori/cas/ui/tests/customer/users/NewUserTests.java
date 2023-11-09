package com.apriori.cas.ui.tests.customer.users;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cas.ui.components.SourceListComponent;
import com.apriori.cas.ui.components.TableComponent;
import com.apriori.cas.ui.pageobjects.customer.users.UsersListPage;
import com.apriori.cas.ui.pageobjects.customer.users.profile.NewUserPage;
import com.apriori.cas.ui.pageobjects.customer.users.profile.UserProfilePage;
import com.apriori.cas.ui.pageobjects.login.CasLoginPage;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.response.InstallationItems;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.Constants;
import com.apriori.cds.api.utils.RandomCustomerData;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.Obligation;
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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class NewUserTests extends TestBaseUI {
    private final String appIdentity = Constants.getApProApplicationIdentity();
    private final String ciaIdentity = Constants.getCiaApplicationIdentity();
    private final String cirIdentity = Constants.getCirAppIdentity();
    private final String acsIdentity = Constants.getACSAppIdentity();
    private final CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private String customerIdentity;
    private String userIdentity;
    private NewUserPage newUserPage;
    private String email;
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
        newUserPage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .openCustomer(customerIdentity)
            .goToUsersPage()
            .goToCustomerStaff()
            .clickNew();
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
        if (userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        }
        cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
    }

    @Test
    @Tag(SMOKE)
    @Description("New User profile form has correct fields, user can be added by filling only mandatory fields")
    @TestRail(id = {4063, 4062, 4073})
    public void testUserIsCreatedWithOnlyRequiredFields() {
        SoftAssertions soft = new SoftAssertions();
        String userName = new GenerateStringUtil().generateUserName();
        List<String> labelsToCheck = Arrays.asList(
            "User Name:",
            "Identity:",
            "Email:",
            "User Type:",
            "Given Name:",
            "Family Name:",
            "Name Prefix:",
            "Name Suffix:",
            "Job Title:",
            "Department:",
            "Town or City:",
            "County:",
            "Country:",
            "Time Zone:",
            "Office Phone Country Code:",
            "Office Phone Number:",
            "Authentication:"
        );

        newUserPage.testNewUserLabelAvailable(labelsToCheck, soft);

        newUserPage.formFillNewUserDetails(userName, userName + "@" + email + ".com", "", "")
            .inputNamePrefix("");

        soft.assertThat(newUserPage.canSave())
            .overridingErrorMessage("Expected save button to be disabled.")
            .isFalse();
        soft.assertThat(newUserPage.getFieldFeedback("given-name"))
            .isEqualTo("Enter a given name.");
        soft.assertThat(newUserPage.getFieldFeedback("family-name"))
            .isEqualTo("Enter a family name.");

        newUserPage.inputGivenName("Test")
            .inputFamilyName("User")
            .save(UserProfilePage.class);

        userIdentity = new UserProfilePage(driver).getUserIdentity();

        UsersListPage usersListPage = newUserPage.backToUsersListPage();
        SourceListComponent users = usersListPage.getUsersList();
        TableComponent usersTable = Obligation.mandatory(users::getTable, "The users list table is missing");

        long rows = usersTable.getRows().count();
        soft.assertThat(rows).overridingErrorMessage("There are no users on a page.").isGreaterThan(0L);
        soft.assertAll();
    }

    @Test
    @Description("Clicking the cancel button returns the user to the user list.")
    @TestRail(id = {4072})
    public void testCancelReturnsToTheUserListPage() {
        SoftAssertions soft = new SoftAssertions();
        UsersListPage actual = newUserPage
            .formFillNewUserDetails("newUser", "email@com", "givenName", "familyName")
            .cancel(UsersListPage.class);
        soft.assertThat(actual).isNotNull();
        soft.assertAll();
    }

    private void setCustomerData() {
        RandomCustomerData rcd = new RandomCustomerData();
        String customerName = new GenerateStringUtil().generateCustomerName();
        email = customerName.toLowerCase();
        Customer targetCustomer = cdsTestUtil.addCASCustomer(customerName, rcd.getCloudRef(), email, currentUser).getResponseEntity();
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